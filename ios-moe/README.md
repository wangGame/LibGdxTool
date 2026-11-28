# LibGdxTool iOS MOE 模块

入口为 `com.joker.puzzle.iosmoe.Main`，启动项目现有的 `GameTest`。
文本输入通过 `UserInputListener` 接入 MOE 的原生输入框。
使用 MOE 1.10.1 和 gdx-backend-moe 1.13.1，并复用本项目的 `libGdx`、
`libGdxLib` 和 JNI loader，避免引入重复的官方 gdx 类。

## Windows 本地编译

使用 JDK 17，在项目根目录运行：

```powershell
.\gradlew.bat -PiosMoeOnly=true :ios-moe:classes
```

`iosMoeOnly=true` 只加载 MOE、core 及其依赖的 Java 模块，无需 Android SDK。
Windows 默认采用 Java 编译模式，不加载需要 macOS 的 MOE 原生构建插件。
首次构建需要联网下载同版本 MOE SDK，并提取 `moe-core.jar` 和 `moe-ios.jar`
作为编译依赖。输出目录为 `ios-moe/build/classes/java/main/`。

也可以显式指定 Java 编译模式，此命令同样可用于 macOS：

```powershell
.\gradlew.bat -PiosMoeOnly=true -PiosJavaOnly=true :ios-moe:classes
```

已配置 Android SDK 时，可在完整项目中直接运行：

```powershell
.\gradlew.bat :ios-moe:classes
```

本次验证范围为 Windows Java 源码编译和依赖解析。
生成 IPA、原生链接及运行测试需要 macOS/Xcode。

## 文件说明

- `build.gradle`：MOE SDK 提取、Java 编译和原生构建配置。
- `src/com/joker/puzzle/iosmoe/`：入口、文本输入和 MOE 后端适配代码。
- `xcode/ios-moe/Info.plist`：入口类和应用信息。
- `xcode/ios-moe.xcodeproj`：原生工程，默认 Bundle ID 为 `com.joker.puzzle`。
- `prepareAssets` / `prepareNatives`：原生打包前的资源准备，Java 编译无需执行。

资源中新增 OGG 音频时，原生打包前需使用 `convert-audio.py` 转换为 MP3。
当前仓库未配置 MOE 的 GitHub Actions 打包工作流。
