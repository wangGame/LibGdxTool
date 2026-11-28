# LibGdxTool iOS 模块

iOS 入口为 `com.joker.puzzle.ios.IOSLauncher`，启动与 Android 相同的 `GameTest`。
文本输入使用 libGDX 的 iOS 原生输入框。后端和原生库使用 libGDX 1.13.1，
与项目内 `libGdx` 源码版本一致；RoboVM 使用 3.0.1。

## Windows 本地编译

安装 JDK 17，在项目根目录运行：

```powershell
.\gradlew.bat -PiosOnly=true :ios:classes
```

`iosOnly` 仅加载 iOS、core 及它们依赖的 Java 模块，无需 Android SDK。
首次执行需要联网下载 Gradle 和 Maven 依赖。

需要清理 iOS 编译缓存后重新编译时：

```powershell
.\gradlew.bat -PiosOnly=true :ios:clean :ios:classes
```

已经配置 Android SDK 的完整项目也可以直接运行：

```powershell
.\gradlew.bat :ios:classes
```

输出目录：`ios/build/classes/java/main/`。
这些命令验证 Java 编译和依赖解析；Windows 上不能完成 iOS 原生链接或生成 IPA。

## 配置文件

- `ios/build.gradle`：Java/RoboVM 插件、编译依赖和 ARM64 构建配置。
- `ios/robovm.properties`：Bundle ID、入口类、应用名称和版本。
- `ios/robovm.xml`：打包 `assets/`、iOS 资源和反射保留规则。
- `ios/Info.plist.xml`：最低 iOS 版本和横竖屏配置。

IPA 原生打包需要 macOS/Xcode；本次配置以 Windows 本地编译通过为验证范围。
仓库目前未配置 iOS Actions 工作流，`ios/scripts/` 中的下载脚本不能直接启动云端构建。
