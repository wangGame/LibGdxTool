# Core 模块设计思路详解文档

---

## 一、项目整体架构概览

`core` 模块并不是一个"功能库"，而是整个游戏项目的**演示/测试入口模块**（Demo Layer）。它依赖于三个底层库模块，整体分层如下：

```
┌─────────────────────────────────────┐
│           core  (演示/测试入口)        │
│   TestGame  /  LoadingScreen         │
└──────────────┬──────────────────────┘
               │ 依赖
┌──────────────▼──────────────────────┐
│         libGdxLib  (核心框架库)        │
│  BaseGame / BaseScreen / Asset /     │
│  Constant / Sound / View / Dialog    │
└──────────────┬──────────────────────┘
               │ 依赖
┌──────────────▼──────────────────────┐
│    libGdx  /  libCommon  (基础工具库)  │
└─────────────────────────────────────┘
```

**`core` 模块的职责：**
- 继承 `BaseGame`，覆写 `loadingView()` 设置第一个游戏屏幕
- 包含具体的 `Screen` 实现（如 `LoadingScreen`），展示各功能用法
- 验证框架设计是否可行

> 因此，理解 `core` 的设计思路，重点在于理解其依赖的 **`libGdxLib`** 框架的设计哲学。

---

## 二、入口设计：`BaseGame`

### 2.1 类继承关系

```
com.badlogic.gdx.Game  (LibGDX 官方)
        ▲
  BaseGame  (框架封装)
        ▲
  TestGame  (业务实现，core模块中)
```

### 2.2 生命周期流程

`BaseGame.create()` 是程序入口，其内部按顺序调用了 6 个步骤：

```java
@Override
public void create() {
    printInfo();        // 1. 打印GL版本、LibGDX版本信息
    gameInfoConfig();   // 2. 读取注解 @GameInfo 配置游戏参数
    anrTest();          // 3. 启动ANR监控（可选）
    initInstance();     // 4. 初始化全局输入（如屏蔽系统返回键）
    initViewport();     // 5. 初始化视口（核心的屏幕适配）
    initScreen();       // 6. 启动第一个Screen（通过postRunnable延迟到GL线程）
}
```

**设计亮点：**

1. **注解驱动配置**：游戏宽高、Batch类型、Viewport类型均通过 `@GameInfo` 注解声明在子类上，无需在代码中硬编码：

```java
@GameInfo(width = 1080, height = 1920, viewportType = Constant.FITVIEWPORT)
public class TestGame extends BaseGame {
    @Override
    protected void loadingView() {
        setScreen(new LoadingScreen(this));
    }
}
```

2. **延迟初始化第一个 Screen**：`initScreen()` 通过 `Gdx.app.postRunnable()` 将 `loadingView()` 放入 GL 线程队列，避免初始化时序问题：

```java
protected void initScreen() {
    Gdx.app.postRunnable(() -> {
        if (Constant.crashlog) {
            Constant.SDPATH = Gdx.files.local("/").file().getAbsolutePath();
            new CrashUtils();
        }
        loadingView();
    });
}
```

3. **转场屏幕（zhuanCScreen）机制**：框架在普通 Screen 之上支持一个独立的"转场层" Screen，用于屏幕切换动画，不��响主 Screen 的生命周期：

```java
// 普通切换
game.setScreen(new NextScreen(this));

// 带转场动画切换（isGc=true 时进入zhuanCScreen层）
game.setScreen(new TransitionScreen(this), true);
```

### 2.3 Viewport 适配体系

`BaseGame.initViewport()` 根据 `Constant.viewportType` 创建对应 Viewport，支持 **7 种适配策略**：

| 常量值 | Viewport 类型 | 说明 |
|---|---|---|
| `EXTENDVIEWPORT` (0) | `ExtendViewport` | 扩展填充，超出设计尺寸的区域也渲染（默认） |
| `FITVIEWPORT` (1) | `FitViewport` | 等比缩放，两侧留黑边 |
| `SCREENVIEWPORT` (2) | `ScreenViewport` | 1:1像素，不缩放 |
| `FILLVIEWPORT` (3) | `FillViewport` | 填满屏幕，可能裁剪 |
| `STRETCHVIEWPORT` (4) | `StretchViewport` | 强制拉伸，不保持比例 |
| `SCALINGVIEWPORTX` (5) | `ScalingViewport(fillX)` | 按X轴填充 |
| `SCALINGVIEWPORTY` (6) | `ScalingViewport(fillY)` | 按Y轴填充 |

`resize()` 时同步更新 `Constant.GAMEWIDTH` / `Constant.GAMEHIGHT`（实际世界坐标系大小），与设计尺寸（`Constant.WIDTH` / `Constant.HIGHT`）区分：

```java
public static void updateSize(Viewport stageViewport) {
    Constant.GAMEWIDTH = stageViewport.getWorldWidth();
    Constant.GAMEHIGHT = stageViewport.getWorldHeight();
}
```

### 2.4 Batch 选择策略

支持两种 Batch，通过 `Constant.batchType` 控制：

| 类型 | Batch | 说明 |
|---|---|---|
| `COUPOLYGONBATCH` (0) | `CpuPolygonSpriteBatch` | 支持多边形裁剪，兼容Spine（默认） |
| `SPRITEBATCH` (1) | `SpriteBatch` | 标准精灵批次，性能好 |

Batch 在 `BaseGame` 层**懒加载单例**，所有 Screen 共享同一个 Batch，节省 GPU 资源。

---

## 三、屏幕设计：`BaseScreen`

### 3.1 类继承关系

```
com.badlogic.gdx.Screen  (LibGDX 接口)
        ▲
  BaseScreen  (框架封装，core模块Screen的父类)
        ▲
  LoadingScreen / GameScreen 等  (业务实现)
```

### 3.2 Screen 的完整生命周期

```
构造函数 BaseScreen(game)
    │
    ├─ 创建 Stage（共享 Batch 和 Viewport）
    ├─ 创建 DialogManager（弹窗管理）
    ├─ 创建 BannerManager（广告Banner管理）
    ├─ 设置 InputMultiplexer（多输入源）
    └─ uiResize()（初次计算偏移量）

show()  ←── 被 setScreen 触发
    │
    ├─ initTouch()     注册返回键监听、启用触摸
    ├─ initRootView()  创建 rootView（Group容器，或从Cocos资源加载）
    ├─ initAnnotation() 子类可重写，做注解初始化
    ├─ initData()      子类重写，加载数据
    └─ initView()      子类重写，构建UI

render(delta)
    ├─ stage.act()     更新所有Actor的Action
    ├─ stage.draw()    绘制整个场景图
    └─ bannerManager.toFront()  广告置顶

resize(w, h)
    └─ uiResize()      重算偏移量、通知 DialogManager

hide() / dispose()
    └─ 卸载Cocos资源（如有）、销毁Stage
```

### 3.3 rootView 设计

`rootView` 是一个 `Group`，作为**所有UI元素的统一容器**，其中心点默认与屏幕中心对齐：

```java
rootView.setPosition(Constant.GAMEWIDTH/2, Constant.GAMEHIGHT/2, Align.center);
```

**两种初始化方式：**

1. **手动模式**（无注解）：自动创建空的 `Group`，尺寸为设计分辨率 `WIDTH×HIGHT`
2. **Cocos资源模式**（有 `@ScreenResource` 注解）：从 Cocos Creator 导出的 JSON 文件加载完整 UI 树：

```java
@ScreenResource("ui/loading_screen.json")
public class LoadingScreen extends BaseScreen { ... }
```

### 3.4 偏移量体系（Safe Area 适配）

针对刘海屏、异形屏的适配，BaseScreen 维护四个方向的偏移量：

```
offsetTop    ← ExtendViewport超出设计尺寸的顶部距离（扣除刘海高度）
offsetBottom ← 底部偏移
offsetLeft   ← 左侧偏移
offsetRight  ← 右侧偏移
```

提供工具方法 `actorOffset()` 快速将 Actor 向指定方向偏移到安全区边缘：

```java
// 将按钮推到屏幕右侧安全边缘
actorOffset(myButton, Align.right);
```

### 3.5 Screen 切换方式

支持三种切换方式：

```java
// 方式1：通过实例切换
setScreen(new GameScreen(game));

// 方式2：通过Class反射切换（不需要手动传game引用）
setScreen(GameScreen.class);

// 方式3：带转场动画切换
setScreen(TransitionScreen.class, true /* isGc=转场层 */);
```

---

## 四、全局常量设计：`Constant` & `Configuration`

### 4.1 `Constant` —— 运行时全局状态

`Constant` 是整个框架的**全局状态中心**，所有模块通过静态字段共享状态：

```java
// 设计分辨率（固定）
Constant.WIDTH = 1080;
Constant.HIGHT = 1920;

// 实际世界坐标系大小（随Viewport变化）
Constant.GAMEWIDTH = ...;   // 动态更新
Constant.GAMEHIGHT = ...;

// 当前活跃屏幕（方便全局访问）
Constant.currentActiveScreen = screen;

// 调试开关
Constant.DEBUG = true;
Constant.SHOWFRAMESPERSECOND = true;
Constant.SHOWRENDERCALL = true;

// 音效全局开关
Constant.isSound = true;
Constant.isMusic = true;
```

**三套尺寸概念对比：**

| 字段 | 含义 | 是否变化 |
|---|---|---|
| `WIDTH` / `HIGHT` | 设计分辨率（开发时参考坐标系） | 不变 |
| `STDWIDTH` / `STDHIGHT` | 标准尺寸（1080×1920固定常量） | 永不变 |
| `GAMEWIDTH` / `GAMEHIGHT` | 实际世界坐标系大小（由Viewport决定） | 随窗口resize变化 |

### 4.2 `Configuration` —— 设备性能分级

`Configuration` 用于**设备性能分级**，支持在低端设备上降低纹理质量：

```java
// 性能等级
Configuration.DeviceState.good  // 正常设备
Configuration.DeviceState.poor  // 低端设备

// 低端设备时纹理缩放比例
Configuration.scale = 0.5f; // 纹理缩小为50%
```

`Asset` 在初始化时会根据 `DeviceState` 决定是否使用 `MiniTextureLoader` / `MiniTextureAtlasLoader`：

```java
if (Configuration.device_state == Configuration.DeviceState.poor) {
    assetManager.setLoader(Texture.class,
        new MiniTextureLoader(resolver, Configuration.scale));
}
```

---

## 五、资源管理设计：`Asset`

### 5.1 单例 + 双 AssetManager 架构

`Asset` 是框架的**资源管理中枢**，采用**严格单例**（构造函数内计数器防止重复创建），内部维护两个 `AssetManager`：

```
Asset (单例)
  ├── assetManager        ← 内部资源（internal，打包在APK中）
  └── localAssetManager   ← 本地资源（local，设备存储路径）
```

通过 `Constant.ASSETMANAGERTYPE` 控制创建哪个（0=只创建internal，1=只创建local，2=两个都创建）。

### 5.2 扩展 Loader 注册

`Asset` 在初始化时为 `AssetManager` 注册了大量**自定义加载器**，支持非标准资源格式：

```java
assetManager.setLoader(ManagerUIEditor.class, new ManagerUILoader(...));  // UI编辑器格式
assetManager.setLoader(PlistAtlas.class, new PlistAtlasLoader(...));       // Cocos Plist图集
assetManager.setLoader(SkeletonData.class, new SkeletonDataLoader(...));   // Spine骨骼动画
assetManager.setLoader(ParticleEffect.class, new ParticleEffectLoader(...)); // 粒子效果
assetManager.setLoader(ArrayResult.class, new CsvLoader(...));             // CSV配置表
```

### 5.3 资源获取：懒加载 + 同步等待

所有 `getXxx()` 方法均遵循同一模式：**先检查是否已加载，未加载则同步加载**：

```java
public Texture getTexture(String path) {
    if (!assetManager.isLoaded(path)) {
        // 设置线性过滤，避免像素化
        TextureLoader.TextureParameter parameter = new TextureLoader.TextureParameter();
        parameter.magFilter = Texture.TextureFilter.Linear;
        parameter.minFilter = Texture.TextureFilter.Linear;
        assetManager.load(path, Texture.class, parameter);
        assetManager.finishLoading(); // 同步等待
    }
    return assetManager.get(path, Texture.class);
}
```

**支持的资源类型：**

| 方法 | 资源类型 |
|---|---|
| `getTexture(path)` | 单张纹理 `Texture` |
| `getAtlas(path)` | 纹理图集 `TextureAtlas` |
| `getPlist(path)` | Cocos Plist图集 `PlistAtlas` |
| `getSprite(path)` | 精灵 `Sprite` |
| `loadBitFont(path)` | 位图字体 `BitmapFont` |
| `loadCsv(name, param)` | CSV配置表 |
| `getCsv(name)` | 获取已加载CSV |
| `buffer()` | 帧缓冲 `FrameBuffer`（自适应颜色格式） |

### 5.4 注解驱动的批量资源加载

`Asset.loadAsset(ob)` / `Asset.getResource(ob)` 通过**Java反射扫描字段注解**，实现声明式资源管理：

```java
// 在Screen中声明资源
public class GameScreen extends BaseScreen {
    @TextureReginAnnotation("atlas/game.atlas")
    private TextureAtlas gameAtlas;

    @FtResource("fonts/num.fnt")
    private BitmapFont numFont;

    @SpineResource(isSpine = true)
    private String spineData = "spine/hero.json";
}

// 批量加载（异步）
Asset.getAsset().loadAsset(this);
// 全部加载完成后注入
Asset.getAsset().getResource(this);
```

---

## 六、注解体系设计

框架广泛使用**自定义注解**代替配置文件，实现声明式编程：

### 6.1 `@GameInfo` —— 游戏全局配置

```java
@GameInfo(
    width = 1080,
    height = 1920,
    batch = Constant.COUPOLYGONBATCH,
    viewportType = Constant.FITVIEWPORT
)
public class MyGame extends BaseGame { ... }
```

在 `BaseGame.gameInfoConfig()` 中通过 `AnnotationInfo.checkClassAnnotation()` 反射读取并写入 `Constant`。

### 6.2 `@ScreenResource` —— Screen 绑定 UI 资源

```java
@ScreenResource("ui/main_menu.json")
public class MainMenuScreen extends BaseScreen { ... }
```

`BaseScreen.initRootView()` 读取此注解，自动通过 `CocosResource.loadFile()` 加载 Cocos Creator 导出的 UI 布局文件，构建完整 Actor 树并赋给 `rootView`。

### 6.3 `@ANRDEMO` —— ANR 监控开关

```java
@ANRDEMO(delaytime = 5000)
public class MyGame extends BaseGame { ... }
```

开启后启动 `ANRWatchDog`，超时则触发 `ANRListener.onAppNotResponding()`。

### 6.4 资源注解组

| 注解 | 作用 |
|---|---|
| `@TextureReginAnnotation` | 标记 TextureAtlas 路径字段 |
| `@FtResource` | 标记 BitmapFont 路径字段 |
| `@SpineResource` | 标记 Spine/ParticleEffect 路径字段 |
| `@I18BundleAnnotation` | 标记国际化资源路径字段 |
| `@AssetResource` | 通用资源标记（扩展用） |

---

## 七、音频系统设计：`sound` 包

音频系统包含以下几个类，分工明确：

```
AudioProcess   ← 音频播放核心控制类（全局单例）
SoundAsset     ← 音效（短音频）管理
MusicAsset     ← 音乐（长音频/BGM）管理
AudioType      ← 音频类型枚举定义
AAsset         ← 音频资源声明接口
```

- `Constant.isSound` / `Constant.isMusic` 作为**全局静态开关**，音频类在播放前检查开关
- `Constant.soundV` 控制全局音效音量
- 支持通过 `AudioProcess` 统一管理播放、停止、音量设置

---

## 八、弹窗系统设计：`DialogManager`

`BaseScreen` 中内置了 `DialogManager`，提供**栈式弹窗管理**：

```java
// 显示弹窗
showDialog(new SettingsDialog());
// 或通过Class反射
showDialog(SettingsDialog.class);

// 关闭当前弹窗（返回上一层）
dialogManager.back();
```

- 按下系统返回键（`BACK` / `ESCAPE`）时，`BaseScreen` 自动调用 `dialogManager.back()`，优先关闭最顶层弹窗
- 弹窗基类为 `BaseDialog`，所有弹窗继承它

---

## 九、Actor 扩展设计：`actor` 包

框架在标准 LibGDX `Actor`/`Group` 基础上提供了扩展：

| 类 | 功能 |
|---|---|
| `PolygonClipGroup` | 多边形裁剪容器，子Actor超出多边形范围的部分被裁剪 |
| `ShaderGroup` | 对整个Group应用GLSL Shader效果 |
| `ShaperRenerInteface` | 自定义形状渲染接口 |

---

## 十、整体设计思路总结

### 核心设计原则

```
1. 约定优于配置   → @GameInfo/@ScreenResource 注解替代XML配置文件
2. 分层职责清晰   → Game层负责生命周期/Viewport；Screen层负责UI/交互
3. 资源统一管理   → Asset单例 + 双AssetManager + 懒加载
4. 全局状态集中   → Constant作为全局黑板，所有模块读写同一份状态
5. 多端适配内置   → 7种Viewport策略 + Safe Area偏移 + 低端设备降质
```

### 完整调用链路（从启动到第一帧渲染）

```
DesktopLauncher.main()
  └─ new LwjglApplication(new TestGame(), config)
       └─ TestGame.create()  →  BaseGame.create()
            ├─ gameInfoConfig()  读取@GameInfo注解 → 更新Constant
            ├─ initViewport()    创建 FitViewport(1080, 1920)
            ├─ Asset.getAsset()  初始化资源管理器，注册各种Loader
            └─ postRunnable → loadingView()
                  └─ setScreen(new LoadingScreen(this))
                        └─ LoadingScreen.show()
                              ├─ initRootView()  创建rootView Group
                              └─ initView()
                                    ├─ Asset.getAsset().getTexture("0_1_41_512.jpg")
                                    └─ new Image(texture) → stage.addActor(image)

每帧：
  BaseGame.render()
    ├─ glClear()
    └─ Game.render() → LoadingScreen.render()
          ├─ stage.act()   执行所有Action动画
          └─ stage.draw()  批量提交绘制命令
```

---

> **最后更新**: 2026-04-24
> **版本**: LibGDX 1.13.1
> **分支**: libgdx1.13.1
