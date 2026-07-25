# tool传package

- 申请凭证
- 配置gradle 
- 上传 


## 凭证

- Developer Settings
- Token classic

## 配置gradle

```gradle

def githubPackagesOwner = 'wangGame'
def githubPackagesRepo = 'LibGdxTool'

tasks.register('syncReadmeVersion') {
    doLast {
        def readme = file("${rootDir}/README.md")
        if (!readme.exists()) {
            return
        }

        def markerPattern = /- 当前版本: `[^`]+` \[auto-sync\]/
        def currentText = readme.getText('UTF-8')
        def updatedText = currentText.replaceFirst(markerPattern, "- 当前版本: `${version}` [auto-sync]")

        if (currentText != updatedText) {
            readme.write(updatedText, 'UTF-8')
            println "README version synced to ${version}"
        }
    }
}

gradle.projectsEvaluated {
    allprojects {
        tasks.matching { it.name == 'publish' }.configureEach {
            dependsOn rootProject.tasks.named('syncReadmeVersion')
        }
    }
}

subprojects {
    // Publish Java modules as Maven artifacts.
    pluginManager.withPlugin('java') {
        apply plugin: 'maven-publish'

        java {
            withSourcesJar()
            withJavadocJar()
        }

        tasks.withType(Javadoc).configureEach {
            options.encoding = 'UTF-8'
            if (JavaVersion.current().isJava9Compatible()) {
                options.addBooleanOption('Xdoclint:none', true)
            }
        }

        publishing {
            publications {
                mavenJava(MavenPublication) {
                    from components.java
                    artifactId = project.name.toLowerCase()
                }
            }
            repositories {
                mavenLocal()
                maven {
                    name = 'GitHubPackages'
                    url = uri("https://maven.pkg.github.com/${githubPackagesOwner}/${githubPackagesRepo}")
                    credentials {
                        username = findProperty('gpr.user') ?: System.getenv('GITHUB_ACTOR')
                        password = findProperty('gpr.key') ?: System.getenv('GITHUB_TOKEN')
                    }
                }
            }
        }
    }

    // Publish Android library modules (AAR) from release variant.
    pluginManager.withPlugin('com.android.library') {
        apply plugin: 'maven-publish'

        android {
            publishing {
                singleVariant('release') {
                    withSourcesJar()
                }
            }
        }

        afterEvaluate {
            publishing {
                publications {
                    release(MavenPublication) {
                        from components.release
                        artifactId = project.name.toLowerCase()
                    }
                }
                repositories {
                    mavenLocal()
                    maven {
                        name = 'GitHubPackages'
                        url = uri("https://maven.pkg.github.com/${githubPackagesOwner}/${githubPackagesRepo}")
                        credentials {
                            username = findProperty('gpr.user') ?: System.getenv('GITHUB_ACTOR')
                            password = findProperty('gpr.key') ?: System.getenv('GITHUB_TOKEN')
                        }
                    }
                }
            }
        }
    }
}

def toolJarStateFile = file("${rootProject.projectDir}/.gradle/tool-packaging/last-jar-version.properties")
def toolJarBuildTasks = ((findProperty('toolJarBuildTasks') ?: ':desktop:dist') as String)
        .split(',')
        .collect { it.trim() }
        .findAll { !it.isEmpty() }

def readLastPackedToolJarVersion = {
    if (!toolJarStateFile.exists()) {
        return null
    }
    def props = new Properties()
    toolJarStateFile.withInputStream { props.load(it) }
    return props.getProperty('version')
}

def writeLastPackedToolJarVersion = { String packedVersion ->
    toolJarStateFile.parentFile.mkdirs()
    def props = new Properties()
    props.setProperty('version', packedVersion)
    toolJarStateFile.withOutputStream { props.store(it, 'Auto-generated. Last packaged tool jar version.') }
}

tasks.register('packageToolJarIfNeeded') {
    group = 'build'
    description = 'Packages tool jar tasks only when project version changed.'
    dependsOn(toolJarBuildTasks)

    onlyIf {
        def currentVersion = rootProject.version.toString()
        def lastPackedVersion = readLastPackedToolJarVersion()
        def shouldPack = currentVersion != lastPackedVersion
        if (!shouldPack) {
            println "Skip tool jar packaging: version ${currentVersion} already packaged."
        }
        return shouldPack
    }

    doFirst {
        println "Packaging tool jar because version changed to ${rootProject.version}."
    }

    doLast {
        writeLastPackedToolJarVersion(rootProject.version.toString())
        println "Tool jar package state updated: version ${rootProject.version}."
    }
}

tasks.register('packageToolJarStatus') {
    group = 'help'
    description = 'Prints current and last packaged tool jar versions.'
    doLast {
        def currentVersion = rootProject.version.toString()
        def lastPackedVersion = readLastPackedToolJarVersion() ?: 'none'
        println "toolJarBuildTasks = ${toolJarBuildTasks}"
        println "currentVersion   = ${currentVersion}"
        println "lastPackedVersion= ${lastPackedVersion}"
    }
}

tasks.register('packRelease') {
    group = 'build'
    description = 'Builds release APK and packages tool jar only when version changes.'
    dependsOn ':android:assembleRelease'
    dependsOn tasks.named('packageToolJarIfNeeded')
}
```

