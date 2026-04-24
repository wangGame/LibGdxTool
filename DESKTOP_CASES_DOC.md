# 📖 LibgdxTool — Desktop 案例完整总结文档

> 仓库：[dm-kangwang/LibgdxTool](https://github.com/dm-kangwang/LibgdxTool/tree/libgdx1.13.1/desktop/src/com/libGdx/test)
> 分支：`libgdx1.13.1`
> 语言：Java (98.7%) + GLSL (1.3%)
> 文档更新：2026-04-24

---

## 📁 项目结构

```
desktop/
├── build.gradle
├── libs/
└── src/
    └── com/libGdx/test/        ← 所有案例根目录
        ├── base/               ← 基础框架（所有案例的公共父类）
        ├── action/             ← Action 动画系统
        ├── alpha/              ← 透明度
        ├── shader/             ← GLSL Shader 着色器
        ├── spine/              ← Spine 骨骼动画
        ├── model/              ← 3D 模型
        ... (共 109+ 个子模块)
```

---

## 🏗️ 基础框架

### `LibGdxTestMain` — 所有案例的通用基类

所有案例均继承 `LibGdxTestMain`，通过统一的启动模式运行：

```java
public class XxxApp extends LibGdxTestMain {
    public static void main(String[] args) {
        XxxApp app = new XxxApp();
        app.start();  // 自动创建窗口并启动 LibGDX
    }

    @Override
    public void useShow(Stage stage) {
        super.useShow(stage);
        // 在此添加 Actor、构建 UI
    }
}
```

### `@GameInfo` 注解

```java
@GameInfo(width = 720, height = 1280, batch = Constant.COUPOLYGONBATCH)
```

用于声明分辨率与渲染批次类型（SpriteBatch / PolygonBatch 等）。

---

## 🗂️ 全部案例总结（按字母排序）

---

### 1. `action` — Action 动画系统
**文件：** [NumActionTest.java](https://github.com/dm-kangwang/LibgdxTool/blob/libgdx1.13.1/desktop/src/com/libGdx/test/action/NumActionTest.java)

演示自定义 `NumAction` 的使用，该 Action 可在指定时间内对数值进行插值动画，例如让进度条数值从 0 平滑增长到目标值。是 LibGDX Action 系统的扩展实践案例。

**关键知识点：**
- 自定义 `Action` 子类
- `NumAction` 数值插值
- `addAction()` 挂载到 Actor

---

### 2. `alpha` — 透明度渐变
**文件：** [AlphaTest.java](https://github.com/dm-kangwang/LibgdxTool/blob/libgdx1.13.1/desktop/src/com/libGdx/test/alpha/AlphaTest.java) | [AlphaTestApp.java](https://github.com/dm-kangwang/LibgdxTool/blob/libgdx1.13.1/desktop/src/com/libGdx/test/alpha/AlphaTestApp.java)

演示 LibGDX 中 Actor 的透明度控制与渐变动画，包括 `setAlpha()`、`Actions.fadeIn()`、`Actions.fadeOut()` 等操作。

**关键知识点：**
- `actor.getColor().a` 透明度属性
- `Actions.fadeIn/fadeOut` 淡入淡出
- Group 透明度继承

---

### 3. `anr` — ANR / 卡顿检测
**文件：** [anr 目录](https://github.com/dm-kangwang/LibgdxTool/tree/libgdx1.13.1/desktop/src/com/libGdx/test/anr)

演示如何在 LibGDX 中检测主线程卡顿（类似 Android ANR）。使用 WatchDog 线程监控渲染帧时间，超时则报警。

**关键知识点：**
- 主线程监控
- 看门狗（WatchDog）机制
- 卡顿日志输出

---

### 4. `asset` — 资源加载管理
**文件：** [asset 目录](https://github.com/dm-kangwang/LibgdxTool/tree/libgdx1.13.1/desktop/src/com/libGdx/test/asset)

演示 LibGDX `AssetManager` 的异步资源加载，包括纹理、音频、字体等资源的统一管理与释放。

**关键知识点：**
- `AssetManager.load()` 异步加载
- `AssetManager.finishLoading()` 同步等待
- `Asset.getAsset()` 单例封装使用

---

### 5. `ball` — 球体物理/运动
**文件：** [ball 目录](https://github.com/dm-kangwang/LibgdxTool/tree/libgdx1.13.1/desktop/src/com/libGdx/test/ball)

演示圆形物体的运动模拟，包括抛物线、弹跳、碰撞等基础物理行为，使用 LibGDX 数学库实现。

**关键知识点：**
- 抛体运动公式
- 弹性碰撞处理
- `Vector2` 向量运算

---

### 6. `base` — 基础测试框架
**文件：** [base 目录](https://github.com/dm-kangwang/LibgdxTool/tree/libgdx1.13.1/desktop/src/com/libGdx/test/base)

项目公共基类目录，包含 `LibGdxTestMain`（所有 Desktop 案例的父类）。提供统一的 LibGDX 启动配置、Stage 初始化、窗口大小设置等。

**关键知识点：**
- `LwjglApplication` 桌面启动器
- `LwjglApplicationConfiguration` 窗口配置
- Stage / SpriteBatch 公共初始化

---

### 7. `beser` — 贝塞尔曲线
**文件：** [B.java](https://github.com/dm-kangwang/LibgdxTool/blob/libgdx1.13.1/desktop/src/com/libGdx/test/beser/B.java) | [BU.java](https://github.com/dm-kangwang/LibgdxTool/blob/libgdx1.13.1/desktop/src/com/libGdx/test/beser/BU.java) | [BUL.java](https://github.com/dm-kangwang/LibgdxTool/blob/libgdx1.13.1/desktop/src/com/libGdx/test/beser/BUL.java) | [BUL1.java](https://github.com/dm-kangwang/LibgdxTool/blob/libgdx1.13.1/desktop/src/com/libGdx/test/beser/BUL1.java)

多个版本的贝塞尔曲线演示。`B.java` 是基础版，`BU/BUL/BUL1` 是迭代优化版本，探索不同控制点数量（2阶、3阶）的曲线绘制与路径跟随。

**关键知识点：**
- 二阶/三阶贝塞尔曲线公式
- `ShapeRenderer` 分段绘制
- 曲线路径上的对象运动

---

### 8. `bianyuan` — 描边/边缘效果
**文件：** [bianyuan 目录](https://github.com/dm-kangwang/LibgdxTool/tree/libgdx1.13.1/desktop/src/com/libGdx/test/bianyuan)

演示图像描边效果的实现，通常基于 GLSL Shader 或多次偏移绘制技术，给图像添加轮廓线。

**关键知识点：**
- 描边 Shader（对纹理采样周围像素判断边缘）
- 多 Pass 偏移绘制法
- `ShaderProgram` 集成到 Actor

---

### 9. `bloom` — Bloom 辉光后处理
**文件：** [Bloom.java](https://github.com/dm-kangwang/LibgdxTool/blob/libgdx1.13.1/desktop/src/com/libGdx/test/bloom/Bloom.java) | [BloomDemo.java](https://github.com/dm-kangwang/LibgdxTool/blob/libgdx1.13.1/desktop/src/com/libGdx/test/bloom/BloomDemo.java) | [BloomShaderLoader.java](https://github.com/dm-kangwang/LibgdxTool/blob/libgdx1.13.1/desktop/src/com/libGdx/test/bloom/BloomShaderLoader.java)

完整的 Bloom（辉光/泛光）后处理特效实现。流程：先渲染场景到 `FrameBuffer`，对高亮区域做高斯模糊，最后叠加到原画面，产生发光效果。

**关键知识点：**
- `FrameBuffer` 离屏渲染
- 亮度提取 Shader
- 高斯模糊 Shader
- 多 Pass 后处理流程

---

### 10. `bullet` — Bullet 物理引擎 3D
**文件：** [App.java](https://github.com/dm-kangwang/LibgdxTool/blob/libgdx1.13.1/desktop/src/com/libGdx/test/bullet/App.java) | [BulletFlow.java](https://github.com/dm-kangwang/LibgdxTool/blob/libgdx1.13.1/desktop/src/com/libGdx/test/bullet/BulletFlow.java)

演示 LibGDX 集成 Bullet 3D 物理引擎，包括刚体创建、重力模拟、碰撞检测。`BulletFlow` 演示子弹飞行轨迹的物理模拟。

**关键知识点：**
- `btRigidBody` 刚体
- `btDiscreteDynamicsWorld` 物理世界
- LibGDX Bullet 绑定层使用

---

### 11. `camera` — 摄像机控制
**文件：** [App.java](https://github.com/dm-kangwang/LibgdxTool/blob/libgdx1.13.1/desktop/src/com/libGdx/test/camera/App.java) | [Closeup.java](https://github.com/dm-kangwang/LibgdxTool/blob/libgdx1.13.1/desktop/src/com/libGdx/test/camera/Closeup.java) | [DemoCamera.java](https://github.com/dm-kangwang/LibgdxTool/blob/libgdx1.13.1/desktop/src/com/libGdx/test/camera/DemoCamera.java) | [OrthographicProjection.java](https://github.com/dm-kangwang/LibgdxTool/blob/libgdx1.13.1/desktop/src/com/libGdx/test/camera/OrthographicProjection.java)

四个摄像机相关演示：基础摄像机控制、近景特写（Closeup）、完整相机 Demo、正交投影原理。覆盖 2D 正交摄像机的平移、缩放、边界限制。

**关键知识点：**
- `OrthographicCamera` 正交摄像机
- `camera.position`、`camera.zoom` 控制
- `camera.unproject()` 屏幕→世界坐标
- 相机跟随角色平滑移动

---

### 12. `cir` — 圆形绘制
**文件：** [cir 目录](https://github.com/dm-kangwang/LibgdxTool/tree/libgdx1.13.1/desktop/src/com/libGdx/test/cir)

演示使用 `ShapeRenderer` 或 Pixmap 绘制圆形、圆弧，以及基于圆形的 UI 组件（如圆形头像、圆形遮罩）。

**关键知识点：**
- `ShapeRenderer.circle()`
- `Pixmap` 手动绘制圆形
- 圆形遮罩裁剪

---

### 13. `cirprogres` — 圆形进度条
**文件：** [cirprogres 目录](https://github.com/dm-kangwang/LibgdxTool/tree/libgdx1.13.1/desktop/src/com/libGdx/test/cirprogres)

演示圆形进度条（扇形进度）的实现，常见于游戏技能冷却 CD 显示，基于 Shader 或 Stencil 模板缓冲实现扇形裁切。

**关键知识点：**
- 扇形进度 Shader（根据角度 discard 片段）
- Stencil Buffer 实现扇形遮罩
- 进度值 0.0～1.0 映射到 0°～360°

---

### 14. `click` — 点击/触摸事件
**文件：** [click 目录](https://github.com/dm-kangwang/LibgdxTool/tree/libgdx1.13.1/desktop/src/com/libGdx/test/click)

演示 LibGDX Scene2D 事件系统中的点击处理，包括 `ClickListener`、`InputListener`、触摸抬起/按下/拖动事件。

**关键知识点：**
- `ClickListener.clicked()` 点击回调
- `InputEvent` 事件对象
- `actor.addListener()` 事件注册
- 事件冒泡与捕获机制

---

### 15. `clip` — 裁剪区域
**文件：** [clip 目录](https://github.com/dm-kangwang/LibgdxTool/tree/libgdx1.13.1/desktop/src/com/libGdx/test/clip)

演示 LibGDX 的矩形裁剪区域（Scissor Rectangle），只在指定矩形范围内渲染内容，常用于 ScrollPane 或卡牌翻转等遮罩效果。

**关键知识点：**
- `ScissorStack.pushScissors()` / `popScissors()`
- `ScissorStack.calculateScissors()` 坐标转换
- 与 Stage/Camera 配合使用

---

### 16. `cocos` — Cocos 资源加载兼容
**文件：** [CocosApp.java](https://github.com/dm-kangwang/LibgdxTool/blob/libgdx1.13.1/desktop/src/com/libGdx/test/cocos/CocosApp.java)

演示用自研的 `CocosResource` 工具直接加载 Cocos Creator 导出的 JSON 场景文件，在 LibGDX 中还原节点树（位置、大小、层级关系），实现 Cocos → LibGDX 无缝迁移。

**关键知识点：**
- Cocos `.json` 场景格式解析
- `CocosResource.loadFile()` 转换为 LibGDX `Group`
- 坐标系差异处理（Y 轴方向）

---

### 17. `color` — 颜色系统
**文件：** [ImageColor.java](https://github.com/dm-kangwang/LibgdxTool/blob/libgdx1.13.1/desktop/src/com/libGdx/test/color/ImageColor.java) | [ColorConvert.java](https://github.com/dm-kangwang/LibgdxTool/blob/libgdx1.13.1/desktop/src/com/libGdx/test/color/ColorConvert.java)

两个颜色工具演示：
- `ImageColor`：通过 HSV 色彩模型批量生成色块，点击输出色值
- `ColorConvert`：将十六进制颜色字符串 `#RRGGBB` 解析为 LibGDX `Color` 的 RGB 分量

**关键知识点：**
- `Color.fromHsv(h, s, v)` HSV 模型
- `Color.valueOf("#rrggbb")` Hex 解析
- `Pixmap` 程序化生成纯色纹理

---

### 18. `colorcircle` — 色环/颜色选择器
**文件：** [colorcircle 目录](https://github.com/dm-kangwang/LibgdxTool/tree/libgdx1.13.1/desktop/src/com/libGdx/test/colorcircle)

实现 HSV 色轮（Color Wheel）UI 组件，用户可拖动选择颜色。通过 Pixmap 程序化生成圆形渐变纹理，支持色相、饱和度的交互选择。

**关键知识点：**
- HSV → RGB 颜色转换
- Pixmap 程序化圆形渐变贴图
- 触摸拖拽实时更新颜色

---

### 19. `common` — 公共组件
**文件：** [common 目录](https://github.com/dm-kangwang/LibgdxTool/tree/libgdx1.13.1/desktop/src/com/libGdx/test/common)

存放各案例共享的公共工具类、辅助组件，如通用 UI 控件、常量定义、工具方法等。

---

### 20. `connectdot` — 连线/点连接
**文件：** [ConnectDotApp.java](https://github.com/dm-kangwang/LibgdxTool/blob/libgdx1.13.1/desktop/src/com/libGdx/test/connectdot/ConnectDotApp.java) | [ConnectDotApp2.java](https://github.com/dm-kangwang/LibgdxTool/blob/libgdx1.13.1/desktop/src/com/libGdx/test/connectdot/ConnectDotApp2.java) | [Dots.java](https://github.com/dm-kangwang/LibgdxTool/blob/libgdx1.13.1/desktop/src/com/libGdx/test/connectdot/Dots.java)

演示"连接圆点"类游戏机制：屏幕上散布若干圆点，玩家滑动手指依次连接同色圆点。有两个版本，`App2` 为改进版，`Dots` 是单个圆点的数据模型。

**关键知识点：**
- 触摸路径检测与圆点碰撞
- `ShapeRenderer` 实时绘制连线
- 游戏状态机（选中、连接、完成）

---

### 21. `csv` — CSV 文件解析
**文件：** [csv 目录](https://github.com/dm-kangwang/LibgdxTool/tree/libgdx1.13.1/desktop/src/com/libGdx/test/csv)

演示从 CSV 文件读取游戏配置数据（如关卡参数、道具属性），并映射为 Java 对象，方便策划直接用 Excel 编辑游戏参数。

**关键知识点：**
- `Gdx.files.internal()` 读取文件
- 字符串分割解析 CSV
- 数据对象映射

---

### 22. `cut` — 图片裁切
**文件：** [cut 目录](https://github.com/dm-kangwang/LibgdxTool/tree/libgdx1.13.1/desktop/src/com/libGdx/test/cut)

演示运行时对图片进行裁切操作，基于 `Pixmap` 像素级复制或 `TextureRegion` 区域截取，用于动态生成子纹理（如卡牌碎片、图片分割）。

**关键知识点：**
- `TextureRegion(texture, x, y, w, h)` 区域截取
- `Pixmap` 像素复制
- 运行时纹理分割

---

### 23. `dfs` — DFS 深度优先搜索
**文件：** [dfs 目录](https://github.com/dm-kangwang/LibgdxTool/tree/libgdx1.13.1/desktop/src/com/libGdx/test/dfs)

演示深度优先搜索算法在游戏中的应用，例如迷宫生成、连通区域检测（消消乐同色块检测）等。用 LibGDX ShapeRenderer 可视化搜索过程。

**关键知识点：**
- DFS 递归/栈实现
- 二维格子遍历
- 搜索过程动画可视化

---

### 24. `down` — 下载功能
**文件：** [down 目录](https://github.com/dm-kangwang/LibgdxTool/tree/libgdx1.13.1/desktop/src/com/libGdx/test/down)

演示 LibGDX 的网络资源下载，包括异步下载文件、进度回调、下载完成后加载资源（如热更新场景中的资源包下载）。

**关键知识点：**
- `Gdx.net.sendHttpRequest()` 网络请求
- 字节流写入本地文件
- 下载进度监听

---

### 25. `dyn` — 动态效果
**文件：** [dyn 目录](https://github.com/dm-kangwang/LibgdxTool/tree/libgdx1.13.1/desktop/src/com/libGdx/test/dyn)

演示动态视觉效果，包括动态背景、流动效果、动态生成/删除 Actor 等场景，测试 Stage 动态管理能力。

**关键知识点：**
- 运行时动态 `addActor` / `removeActor`
- 动态纹理更新
- 帧率相关动效

---

### 26. `ecode` — 编码/二维码
**文件：** [ecode 目录](https://github.com/dm-kangwang/LibgdxTool/tree/libgdx1.13.1/desktop/src/com/libGdx/test/ecode)

演示二维码生成与读取，在 LibGDX 中集成 ZXing 库，将字符串编码为 QR 码纹理显示，或从图片中解析二维码内容。

**关键知识点：**
- ZXing 二维码生成
- `Pixmap` 绘制 QR 矩阵
- 二维码纹理显示

---

### 27. `effect` — 视觉特效
**文件：** [effect 目录](https://github.com/dm-kangwang/LibgdxTool/tree/libgdx1.13.1/desktop/src/com/libGdx/test/effect)

综合视觉特效演示，包括爆炸、闪光、涟漪等游戏常见特效，组合使用粒子系统、Shader、Action 动画实现。

**关键知识点：**
- `ParticleEffect` 粒子特效
- Shader + FrameBuffer 特效合成
- Action 时序控制特效播放

---

### 28. `endless` — 无限滚动
**文件：** [endless 目录](https://github.com/dm-kangwang/LibgdxTool/tree/libgdx1.13.1/desktop/src/com/libGdx/test/endless)

演示无限循环滚动背景，常见于跑酷游戏。通过两张图片交替复位实现视觉上的无限滚动效果，支持水平/垂直方向。

**关键知识点：**
- 双图交替位移复位技术
- 相机跟随配合滚动
- 速度可变的滚动控制

---

### 29. `event` — 事件系统
**文件：** [event 目录](https://github.com/dm-kangwang/LibgdxTool/tree/libgdx1.13.1/desktop/src/com/libGdx/test/event)

演示 LibGDX Event Bus（事件总线）的使用，实现模块间解耦通信。使用 `libGdxEvent` 库的事件发布/订阅机制，避免直接依赖。

**关键知识点：**
- 事件发布 `EventManager.post()`
- 事件订阅 `@Subscribe` 注解
- 事件优先级与线程调度

---

### 30. `file` — 文件读写/坐标格式转换
**文件：** [TestFile.java](https://github.com/dm-kangwang/LibgdxTool/blob/libgdx1.13.1/desktop/src/com/libGdx/test/file/TestFile.java) | [FileConvert.java](https://github.com/dm-kangwang/LibgdxTool/blob/libgdx1.13.1/desktop/src/com/libGdx/test/file/FileConvert.java)

两个文件工具演示：
- `TestFile`：测试自定义 `FileTest` 工具的读写功能（版本记录文件）
- `FileConvert`：**libGDX ↔ Cocos 物理刚体坐标批量转换工具**，读取 `.xml`/`.plist` 格式坐标，输出以图片中心为原点的新坐标文件

**关键知识点：**
- `Gdx.files.internal()` / `Gdx.files.local()` 文件访问
- `XmlReader` 解析 XML
- 坐标系转换（图片左上角 → 中心点）

---

### 31. `fivestar` — 五角星评分组件
**文件：** [fivestar 目录](https://github.com/dm-kangwang/LibgdxTool/tree/libgdx1.13.1/desktop/src/com/libGdx/test/fivestar)

实现游戏中常见的星级评分 UI（1-5星），支持整星/半星切换、动画反馈、触摸交互选星。

**关键知识点：**
- 自定义 `Group` 组合多个 `Image`
- 触摸位置映射到星级
- 星星填充动画（`Actions.scaleTo`）

---

### 32. `format` — 数字/文本格式化
**文件：** [format 目录](https://github.com/dm-kangwang/LibgdxTool/tree/libgdx1.13.1/desktop/src/com/libGdx/test/format)

演示游戏中常用的数字格式化，如金币数量缩写（1000→1K, 1000000→1M）、时间格式化（秒→mm:ss）、小数点保留位数等。

**关键知识点：**
- `String.format()` 格式化
- 大数缩写算法（K/M/B）
- 时间秒数转换

---

### 33. `freecenterscale` — 自由中心缩放
**文件：** [freecenterscale 目录](https://github.com/dm-kangwang/LibgdxTool/tree/libgdx1.13.1/desktop/src/com/libGdx/test/freecenterscale)

演示以任意点为中心进行缩放（双指捏合缩放），常用于地图、图片查看器场景。处理多点触控缩放时的位置偏移修正。

**关键知识点：**
- `GestureDetector` 手势检测
- 双指 `pinch` 捏合缩放
- 缩放中心点坐标修正

---

### 34. `game` — 游戏逻辑综合
**文件：** [GameTest.java](https://github.com/dm-kangwang/LibgdxTool/blob/libgdx1.13.1/desktop/src/com/libGdx/test/game/GameTest.java)

综合游戏逻辑入口测试，用于快速验证核心模块集成，通常是在整个游戏框架搭建后的启动测试入口。

---

### 35. `generator` — 代码生成器
**文件：** [generator 目录](https://github.com/dm-kangwang/LibgdxTool/tree/libgdx1.13.1/desktop/src/com/libGdx/test/generator)

演示运行时代码/配置生成工具，如批量生成场景配置 JSON、自动生成资源路径常量类等，提升开发效率。

---

### 36. `hit` — 碰撞命中检测
**文件：** [hit 目录](https://github.com/dm-kangwang/LibgdxTool/tree/libgdx1.13.1/desktop/src/com/libGdx/test/hit)

演示精确的像素级或形状级碰撞检测，包括矩形、圆形、多边形的碰撞判断，以及 `Actor.hit()` 方法的自定义重写。

**关键知识点：**
- `Actor.hit(x, y, touchable)` 点击检测重写
- `Intersector` 几何相交检测
- AABB、圆形、多边形碰撞

---

### 37. `interf` — 接口/Interface 模式
**文件：** [interf 目录](https://github.com/dm-kangwang/LibgdxTool/tree/libgdx1.13.1/desktop/src/com/libGdx/test/interf)

演示 Java 接口在 LibGDX 游戏架构中的应用，如回调接口、策略模式、组件接口等设计模式实践。

---

### 38. `json` — JSON 解析与 JavaBean 生成
**文件：** [json 目录](https://github.com/dm-kangwang/LibgdxTool/tree/libgdx1.13.1/desktop/src/com/libGdx/test/json)

包含两个功能：
1. **LibGDX Json 解析**：使用 `com.badlogic.gdx.utils.Json` 序列化/反序列化对象（`Bean.java`）
2. **自动 JavaBean 生成器**（`JsonToJavaBeanGenerator.java`）：输入 JSON 字符串，自动生成带 getter/setter 的 Java 类文件，支持嵌套对象和数组

```java
// 字段类型推断：String/int/double/boolean/嵌套class
generateJavaBeans(jsonStr, "Person");
// 输出 Person.java, Address.java ...
```

---

### 39. `label` — Label 文本控件
**文件：** [label 目录](https://github.com/dm-kangwang/LibgdxTool/tree/libgdx1.13.1/desktop/src/com/libGdx/test/label)

演示 LibGDX `Label` 控件的高级用法，包括自定义字体、富文本颜色标记、多行文本对齐、文字描边、打字机逐字效果。

**关键知识点：**
- `BitmapFont` 位图字体加载
- `LabelStyle` 样式配置
- `[RED]文字[]` 颜色标记语法
- 打字机 Action 动画

---

### 40. `language` — 国际化/多语言
**文件：** [language 目录](https://github.com/dm-kangwang/LibgdxTool/tree/libgdx1.13.1/desktop/src/com/libGdx/test/language)

演示多语言本地化方案，通过 `.properties` 文件或 JSON 文件存储各语言文本，运行时根据系统语言动态切换，支持中英文等多语言。

**关键知识点：**
- `I18NBundle` 国际化包
- 语言切换与热更新
- 占位符文本格式化

---

### 41. `learn` — 学习示例
**文件：** [App2.java](https://github.com/dm-kangwang/LibgdxTool/blob/libgdx1.13.1/desktop/src/com/libGdx/test/learn/demo2/App2.java)

收录学习过程中的教学案例，其中 `demo2` 包含来自 **Udacity 游戏开发课程**的 `IciclesGame`（冰柱躲避游戏）。

```java
// 窗口配置为手机比例
config.height = (int)(1920 * 0.25f);
config.width  = (int)(1080 * 0.5f);
new LwjglApplication(new IciclesGame(), config);
```

---

### 42. `lib3d` — 3D 功能入门
**文件：** [lib3d 目录](https://github.com/dm-kangwang/LibgdxTool/tree/libgdx1.13.1/desktop/src/com/libGdx/test/lib3d)

LibGDX 3D 功能入门演示，包括 3D 场景搭建、ModelBatch 渲染、Environment 光照配置、PerspectiveCamera 透视摄像机。

**关键知识点：**
- `ModelBatch` 3D 渲染批次
- `PerspectiveCamera` 透视相机
- `Environment` + `DirectionalLight` 光照
- `ModelInstance` 模型实例

---

### 43. `light` — 光照效果
**文件：** [light 目录](https://github.com/dm-kangwang/LibgdxTool/tree/libgdx1.13.1/desktop/src/com/libGdx/test/light)

演示 LibGDX Box2DLights 2D 光照效果，包括点光源、锥形光、平行光等，实现类似 2D 游戏中的动态光影效果。

**关键知识点：**
- `RayHandler` 光照渲染器
- `PointLight` 点光源
- `ConeLight` 锥形光
- 光照与阴影渲染

---

### 44. `line` — 线段/LineTime 动画
**文件：** [LineTime.java](https://github.com/dm-kangwang/LibgdxTool/blob/libgdx1.13.1/desktop/src/com/libGdx/test/line/LineTime.java)

利用 `Texture.TextureWrap.Repeat` 纹理重复模式 + 自定义 `NumAction` 数值动画，实现图像宽度从 0 逐渐增长到 1000 的**进度线填充动画**（3秒完成）。

```java
texture.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
NumAction action = new NumAction(0, 1000) {
    public boolean act(float delta) {
        region.setRegionWidth((int) getValue());
        image.setWidth(region.getRegionWidth());
        return super.act(delta);
    }
};
action.setDuration(3);
```

---

### 45. `listener` — 监听器系统
**文件：** [listener 目录](https://github.com/dm-kangwang/LibgdxTool/tree/libgdx1.13.1/desktop/src/com/libGdx/test/listener)

演示 LibGDX 自定义监听器模式的实现与使用，包括游戏状态变化监听、生命值变化监听等游戏逻辑回调设计。

---

### 46. `lizi` — 粒子系统
**文件：** [LiziUtils.java](https://github.com/dm-kangwang/LibgdxTool/blob/libgdx1.13.1/desktop/src/com/libGdx/test/lizi/LiziUtils.java) | [BtnGroup.java](https://github.com/dm-kangwang/LibgdxTool/blob/libgdx1.13.1/desktop/src/com/libGdx/test/lizi/BtnGroup.java) | [ProcessGroup.java](https://github.com/dm-kangwang/LibgdxTool/blob/libgdx1.13.1/desktop/src/com/libGdx/test/lizi/ProcessGroup.java)

粒子系统工具演示，`LiziUtils` 封装了粒子特效的加载与播放，`BtnGroup` 是带粒子效果的按钮组件，`ProcessGroup` 是带粒子动效的进度组件。

**关键知识点：**
- `ParticleEffect` 加载 `.p` 粒子文件
- `ParticleEffectPool` 粒子对象池
- 粒子位置跟随 Actor

---

### 47. `log` — 日志系统
**文件：** [log 目录](https://github.com/dm-kangwang/LibgdxTool/tree/libgdx1.13.1/desktop/src/com/libGdx/test/log)

演示游戏日志系统封装，支持分级输出（DEBUG/INFO/WARN/ERROR），可将日志写入文件，方便线上问题排查。

**关键知识点：**
- `Gdx.app.log()` / `Gdx.app.error()` LibGDX 日志
- 自定义日志级别过滤
- 日志写文件持久化

---

### 48. `mdesl` — 虚拟滑动列表
**文件：** [FixedList.java](https://github.com/dm-kangwang/LibgdxTool/blob/libgdx1.13.1/desktop/src/com/libGdx/test/mdesl/swipe/FixedList.java)

`FixedList<T>` 扩展 LibGDX `Array<T>`，实现**头部插入不扩容**的固定容量数组，用于拖尾轨迹点管理（最多保存 N 个最新点位，旧点自动丢弃）。

```java
public void insert(T t) {
    size = Math.min(size + 1, items.length);
    for (int i = size - 1; i > 0; i--) items[i] = items[i - 1];
    items[0] = t; // 新点插入头部，尾部自动丢弃
}
```

---

### 49. `mesh` — Mesh 网格绘制
**文件：** [Demo01.java](https://github.com/dm-kangwang/LibgdxTool/blob/libgdx1.13.1/desktop/src/com/libGdx/test/mesh/Demo01.java)

演示 LibGDX 底层 `Mesh` API 的使用，直接定义顶点和索引数组绘制自定义几何形状（三角形、四边形等），是理解 OpenGL 渲染管线的基础。

**关键知识点：**
- `Mesh` 顶点数组对象
- `VertexAttribute` 顶点属性（位置/UV/颜色）
- `mesh.render()` 绘制调用

---

### 50. `model` — 3D 模型加载
**文件：** [ModelExample.java](https://github.com/dm-kangwang/LibgdxTool/blob/libgdx1.13.1/desktop/src/com/libGdx/test/model/ModelExample.java) | [ModelTest.java](https://github.com/dm-kangwang/LibgdxTool/blob/libgdx1.13.1/desktop/src/com/libGdx/test/model/ModelTest.java) | [TransparentModelApp.java](https://github.com/dm-kangwang/LibgdxTool/blob/libgdx1.13.1/desktop/src/com/libGdx/test/model/TransparentModelApp.java) | [DecalExample.java](https://github.com/dm-kangwang/LibgdxTool/blob/libgdx1.13.1/desktop/src/com/libGdx/test/model/DecalExample.java)

完整的 3D 模型系列演示：
- **ModelExample**：加载 `.g3db`/`.obj` 模型文件，`ModelBatch` 渲染，`PerspectiveCamera` 透视相机
- **TransparentModelApp**：半透明 3D 模型渲染（开启深度测试 + 透明混合）
- **DecalExample**：`Decal` 公告板（始终面向摄像机的 2D 图片）
- **MyGdxGame2/3**：复杂 3D 场景搭建

---

### 51. `modelnew` — 新版 3D 模型
**文件：** [modelnew 目录](https://github.com/dm-kangwang/LibgdxTool/tree/libgdx1.13.1/desktop/src/com/libGdx/test/modelnew)

对 `model` 模块的升级版本，采用更新的 LibGDX 3D API 或自研封装，优化了模型实例管理、动画混合等。

---

### 52. `moni` — 模拟测试
**文件：** [moni 目录](https://github.com/dm-kangwang/LibgdxTool/tree/libgdx1.13.1/desktop/src/com/libGdx/test/moni)

模拟特定游戏场景的测试案例，用于快速验证某种交互或渲染逻辑的可行性。

---

### 53. `movetest` — 移动/运动测试
**文件：** [movetest 目录](https://github.com/dm-kangwang/LibgdxTool/tree/libgdx1.13.1/desktop/src/com/libGdx/test/movetest)

演示 Actor 的各种移动方式：`Actions.moveTo()`、`Actions.moveBy()`、手动速度更新、匀速/加速/减速运动比较。

**关键知识点：**
- `Actions.moveTo(x, y, duration, interpolation)` 插值运动
- 手动 `delta` 积分更新位置
- 缓动函数（`Interpolation.bounce` 等）

---

### 54. `mult` — 多处理/批量操作
**文件：** [mult 目录](https://github.com/dm-kangwang/LibgdxTool/tree/libgdx1.13.1/desktop/src/com/libGdx/test/mult)

演示批量创建、管理多个游戏对象（如大量子弹、敌人），测试 LibGDX 对大量 Actor 的渲染性能与对象池优化。

**关键知识点：**
- `Pool<T>` 对象池
- 大量 Actor 渲染性能优化
- 批处理 Batch 的 draw call 优化

---

### 55. `net` — 网络通信
**文件：** [NetTest.java](https://github.com/dm-kangwang/LibgdxTool/blob/libgdx1.13.1/desktop/src/com/libGdx/test/net/NetTest.java) | [LibGdxTest.java](https://github.com/dm-kangwang/LibgdxTool/blob/libgdx1.13.1/desktop/src/com/libGdx/test/net/LibGdxTest.java)

演示 LibGDX 的 HTTP 网络请求，发送 GET/POST 请求获取服务器数据（如玩家排行榜、游戏配置），处理异步响应回调。

```java
Gdx.net.sendHttpRequest(request, new HttpResponseListener() {
    public void handleHttpResponse(HttpResponse response) {
        String json = response.getResultAsString();
        // 解析响应...
    }
});
```

---

### 56. `npath` — 导航/A*寻路
**文件：** [npath 目录](https://github.com/dm-kangwang/LibgdxTool/tree/libgdx1.13.1/desktop/src/com/libGdx/test/npath)

演示 A* 寻路算法（或 LibGDX `gdx-ai` 库）在 2D 格子地图中的实现，可视化路径搜索过程，用于 NPC 自动寻路。

**关键知识点：**
- A* 算法（开放列表/关闭列表）
- `IndexedAStarPathFinder`（gdx-ai）
- 格子代价与启发函数

---

### 57. `other` — GLSL 曲线变形 Shader
**文件：** [Desk.java](https://github.com/dm-kangwang/LibgdxTool/blob/libgdx1.13.1/desktop/src/com/libGdx/test/other/Desk.java)

完整的 LibGDX `ApplicationListener` 实现，加载外部 GLSL 文件（`vvv.glsl` / `fff.glsl`）实现图像曲线变形效果，展示 ShaderProgram 的完整使用流程。

```java
shader = new ShaderProgram(vertCode, fragCode);
batch.setShader(shader);
batch.draw(texture, 0, 0, width, height);
batch.setShader(null); // 恢复默认
```

---

### 58. `pan` — 平移/拖拽
**文件：** [pan 目录](https://github.com/dm-kangwang/LibgdxTool/tree/libgdx1.13.1/desktop/src/com/libGdx/test/pan)

演示地图或图片的手势平移拖动，使用 `GestureDetector.pan()` 检测单指平移手势，实现带边界限制的视图拖动。

**关键知识点：**
- `GestureDetector.pan(x, y, dx, dy)`
- 边界 clamp 限制拖动范围
- 惯性滑动（松手后继续滑行）

---

### 59. `path` — 路径跟随动画
**文件：** [path 目录](https://github.com/dm-kangwang/LibgdxTool/tree/libgdx1.13.1/desktop/src/com/libGdx/test/path)

演示对象沿预定义路径（折线、贝塞尔曲线）运动，常见于游戏中的巡逻路径、弹道路径、引导动画。

**关键知识点：**
- `CatmullRomSpline` 样条曲线
- 路径参数化（t∈[0,1] → 位置）
- `Actions.moveTo` 多段路径串联

---

### 60. `pengzhuang` — 碰撞检测（宽相位）
**文件：** [BroadApp.java](https://github.com/dm-kangwang/LibgdxTool/blob/libgdx1.13.1/desktop/src/com/libGdx/test/pengzhuang/BroadApp.java) | [BroadImage.java](https://github.com/dm-kangwang/LibgdxTool/blob/libgdx1.13.1/desktop/src/com/libGdx/test/pengzhuang/BroadImage.java)

演示碰撞检测的宽相位（Broad Phase）算法，先用 AABB 快速剔除不可能碰撞的对象对，再做精确检测，大幅提升多物体碰撞性能。

**关键知识点：**
- AABB 包围盒检测
- `Intersector.overlaps()` 矩形相交
- 宽相位 + 窄相位两阶段碰撞

---

### 61. `pet` — 宠物/跟随 AI
**文件：** [pet 目录](https://github.com/dm-kangwang/LibgdxTool/tree/libgdx1.13.1/desktop/src/com/libGdx/test/pet)

演示宠物跟随逻辑：宠物角色平滑跟随主角移动，保持一定距离，支持闲置/跟随/追赶等状态切换的简单 AI。

**关键知识点：**
- 向量方向 + 速度插值跟随
- 状态机（IDLE/FOLLOW/CHASE）
- `Vector2.lerp()` 平滑插值

---

### 62. `pic` — 图片处理
**文件：** [pic 目录](https://github.com/dm-kangwang/LibgdxTool/tree/libgdx1.13.1/desktop/src/com/libGdx/test/pic)

演示运行时图片处理，包括图片翻转、旋转、缩放、色彩调整，以及图片合并（多图合为一张）等操作。

---

### 63. `pictureTrail` — 图片拖尾效果
**文件：** [pictureTrail 目录](https://github.com/dm-kangwang/LibgdxTool/tree/libgdx1.13.1/desktop/src/com/libGdx/test/pictureTrail)

演示移动对象的拖尾残影效果，记录历史位置并绘制透明度渐变的图像序列，常用于剑光、子弹轨迹等特效。结合 `FixedList` 管理历史轨迹点。

**关键知识点：**
- `FixedList` 存储历史位置队列
- 透明度渐变残影绘制
- 拖尾长度与衰减速度调节

---

### 64. `pix` — 像素级操作
**文件：** [pix 目录](https://github.com/dm-kangwang/LibgdxTool/tree/libgdx1.13.1/desktop/src/com/libGdx/test/pix)

演示 `Pixmap` 的像素级读写操作，实现像素画绘制、颜色替换、简单图像滤镜等效果。

**关键知识点：**
- `Pixmap.getPixel(x, y)` 读取像素
- `Pixmap.drawPixel(x, y, color)` 写入像素
- Pixmap → Texture 转换更新

---

### 65. `pixmap` — Pixmap 纹理生成
**文件：** [pixmap 目录](https://github.com/dm-kangwang/LibgdxTool/tree/libgdx1.13.1/desktop/src/com/libGdx/test/pixmap)

演示程序化纹理生成，使用 `Pixmap` 绘制几何图形（圆形、渐变、网格）后转为 Texture，用于动态生成 UI 背景、调试可视化等。

**关键知识点：**
- `new Pixmap(w, h, Format.RGBA8888)` 创建
- `pixmap.setColor()` + 绘制方法
- `new Texture(pixmap)` + `pixmap.dispose()`

---

### 66. `point` — 点/坐标操作
**文件：** [point 目录](https://github.com/dm-kangwang/LibgdxTool/tree/libgdx1.13.1/desktop/src/com/libGdx/test/point)

演示坐标系转换：屏幕坐标 ↔ 世界坐标 ↔ Actor 局部坐标，以及点与各种形状的包含检测。

**关键知识点：**
- `camera.unproject(vec)` 屏幕→世界坐标
- `actor.stageToLocalCoordinates(vec)` 坐标转换
- 点在多边形内判断

---

### 67. `poly` — 多边形绘制与裁剪
**文件：** [PolyActor.java](https://github.com/dm-kangwang/LibgdxTool/blob/libgdx1.13.1/desktop/src/com/libGdx/test/poly/PolyActor.java) | [WorldPolygonTest.java](https://github.com/dm-kangwang/LibgdxTool/blob/libgdx1.13.1/desktop/src/com/libGdx/test/poly/WorldPolygonTest.java)

两个多边形演示：
- `PolyActor`：用耳切法将任意多边形顶点三角化，使用 `PolygonSprite` 渲染带纹理的多边形
- `WorldPolygonTest`：从文件读取顶点，用 `ShapeRenderer` 绘制多边形轮廓

```java
EarClippingTriangulator t = new EarClippingTriangulator();
ShortArray indices = t.computeTriangles(vertices);
PolygonRegion polyReg = new PolygonRegion(region, vertices, indices.toArray());
poly = new PolygonSprite(polyReg);
```

---

### 68. `position` — 坐标系/定位
**文件：** [position 目录](https://github.com/dm-kangwang/LibgdxTool/tree/libgdx1.13.1/desktop/src/com/libGdx/test/position)

演示 LibGDX 中各种对齐定位方式：`Align.center`、相对父容器定位、锚点设置、以及不同分辨率下的自适应布局。

---

### 69. `process` — 进度条
**文件：** [process 目录](https://github.com/dm-kangwang/LibgdxTool/tree/libgdx1.13.1/desktop/src/com/libGdx/test/process)

演示水平进度条的实现，包括 LibGDX 内置 `ProgressBar` 控件的使用，以及自定义进度条（带填充动画、分段颜色变化）。

**关键知识点：**
- `ProgressBar` + `ProgressBarStyle`
- 自定义进度条 Drawable
- 进度变化 Action 动画

---

### 70. `qx` — 曲线绘制
**文件：** [qx 目录](https://github.com/dm-kangwang/LibgdxTool/tree/libgdx1.13.1/desktop/src/com/libGdx/test/qx)

演示多种曲线（正弦波、余弦波、样条曲线）的实时绘制，用于游戏中的波形 UI、技能特效轨迹等视觉效果。

---

### 71. `ray` — 射线检测
**文件：** [ray 目录](https://github.com/dm-kangwang/LibgdxTool/tree/libgdx1.13.1/desktop/src/com/libGdx/test/ray)

演示射线投射（Raycast）检测，发射一条射线判断与场景中物体的交叉点，用于瞄准线、视野检测、鼠标拾取等场景。

**关键知识点：**
- `Intersector.intersectRayBounds()` 射线-AABB 检测
- Box2D `world.rayCast()` 物理射线
- 射线可视化（`ShapeRenderer`）

---

### 72. `render` — 渲染管线/FrameBuffer
**文件：** [render 目录](https://github.com/dm-kangwang/LibgdxTool/tree/libgdx1.13.1/desktop/src/com/libGdx/test/render)

演示 `FrameBufferObject`（FBO）离屏渲染，先将场景渲染到纹理，再对纹理做后处理（模糊、调色等），最后输出到屏幕。

**关键知识点：**
- `FrameBuffer.begin()` / `end()` 离屏渲染
- 渲染到纹理（RTT）
- 后处理 Shader 应用

---

### 73. `roll` — 滚动效果
**文件：** [roll 目录](https://github.com/dm-kangwang/LibgdxTool/tree/libgdx1.13.1/desktop/src/com/libGdx/test/roll)

演示元素滚动效果，如数字滚动抽奖（老虎机效果）、卡片翻滚，通过 `Actions` 控制 Y 轴位移与节奏。

---

### 74. `sc` — 虚拟列表（View Recycling）
**文件：** [App.java](https://github.com/dm-kangwang/LibgdxTool/blob/libgdx1.13.1/desktop/src/com/libGdx/test/sc/App.java)

高性能**虚拟列表**（类 Android RecyclerView），只创建屏幕可见行数 + 缓冲行数量的 `EmailRow` 视图对象，滚动时复用行组件并更新绑定数据，避免为所有数据项创建 Actor。

```java
// 核心：根据滚动位置动态 bind 数据，而不是创建新对象
int firstIndex = (int)(scrollY / rowHeight);
for (int i = 0; i < visibleRows.size; i++) {
    visibleRows.get(i).bind(emails.get(firstIndex + i));
}
```

---

### 75. `scissortest` — 剪切测试
**文件：** [scissortest 目录](https://github.com/dm-kangwang/LibgdxTool/tree/libgdx1.13.1/desktop/src/com/libGdx/test/scissortest)

演示 OpenGL Scissor Test（剪切测试）的各种应用场景，与 LibGDX `ScissorStack` 配合实现精准区域裁剪。

---

### 76. `screen` — 屏幕切换管理
**文件：** [screen 目录](https://github.com/dm-kangwang/LibgdxTool/tree/libgdx1.13.1/desktop/src/com/libGdx/test/screen)

演示 LibGDX `Screen` 接口的多屏幕管理，包括主菜单→游戏→结算屏幕的切换，以及切换时的过渡动画（淡入淡出、滑动）。

**关键知识点：**
- `Game.setScreen(screen)` 切换屏幕
- `Screen` 生命周期（show/hide/dispose）
- 屏幕切换过渡动画

---

### 77. `scrollpanel` — 滚动面板
**文件：** [scrollpanel 目录](https://github.com/dm-kangwang/LibgdxTool/tree/libgdx1.13.1/desktop/src/com/libGdx/test/scrollpanel)

演示 LibGDX `ScrollPane` 的高级用法，包括水平/垂直滚动、惯性滚动、自定义滚动条样式、嵌套滚动冲突处理。

---

### 78. `scrollroll` — 双向滚动
**文件：** [scrollroll 目录](https://github.com/dm-kangwang/LibgdxTool/tree/libgdx1.13.1/desktop/src/com/libGdx/test/scrollroll)

演示同时支持水平和垂直双向滚动的面板，类似地图浏览的双轴自由拖动，处理双向滚动的手势优先级与边界弹簧效果。

---

### 79. `shader` — GLSL Shader 特效集合
**文件：** [HuiDuZhuanC.java](https://github.com/dm-kangwang/LibgdxTool/blob/libgdx1.13.1/desktop/src/com/libGdx/test/shader/HuiDuZhuanC.java) | [ShaderDemo.java](https://github.com/dm-kangwang/LibgdxTool/blob/libgdx1.13.1/desktop/src/com/libGdx/test/shader/ShaderDemo.java) | [StarField.java](https://github.com/dm-kangwang/LibgdxTool/blob/libgdx1.13.1/desktop/src/com/libGdx/test/shader/StarField.java) | [WaterGroup.java](https://github.com/dm-kangwang/LibgdxTool/blob/libgdx1.13.1/desktop/src/com/libGdx/test/shader/WaterGroup.java) | [ChristmasTree.java](https://github.com/dm-kangwang/LibgdxTool/blob/libgdx1.13.1/desktop/src/com/libGdx/test/shader/ChristmasTree.java) | [OpenAiGroup.java](https://github.com/dm-kangwang/LibgdxTool/blob/libgdx1.13.1/desktop/src/com/libGdx/test/shader/OpenAiGroup.java)

**Shader 特效大集合，共 10+ 个效果：**

| 文件 | Shader 效果 |
|------|-------------|
| `HuiDuZhuanC` | 灰度/波浪扭曲 |
| `ShaderDemo` | 基础 Shader 演示 |
| `StarField` | 星空粒子流（纯 GLSL 实现） |
| `WaterGroup` / `WaterShader` | 水面波纹涟漪效果 |
| `ChristmasTree` | 圣诞树动态光效 |
| `OpenAiGroup` | OpenAI Logo 旋转光圈 Shader |
| `TreeGroup` | 树木摇摆风效果 |
| `ColorCirGroup` | 彩色圆形 Shader |
| `FullQuadToy` | 全屏 ShaderToy 效果 |
| `CollapsableTextWindow` | 可折叠文本 Shader 窗口 |

---

### 80. `shaper` — ShapeRenderer 形状绘制
**文件：** [shaper 目录](https://github.com/dm-kangwang/LibgdxTool/tree/libgdx1.13.1/desktop/src/com/libGdx/test/shaper)

演示 `ShapeRenderer` 的完整 API，包括绘制线段、矩形、圆形、三角形、多边形，以及填充模式与线框模式切换。

**关键知识点：**
- `ShapeRenderer.begin(ShapeType.Line/Filled)`
- 与 `SpriteBatch` 配合时的 flush 时机
- 颜色渐变线段绘制

---

### 81. `sixteen` — 十六进制操作
**文件：** [sixteen 目录](https://github.com/dm-kangwang/LibgdxTool/tree/libgdx1.13.1/desktop/src/com/libGdx/test/sixteen)

演示十六进制数据的处理，包括颜色十六进制转换、字节流十六进制显示、二进制位操作等在游戏数据处理中的应用。

---

### 82. `spine` — Spine 骨骼动画
**文件：** [SpineTest.java](https://github.com/dm-kangwang/LibgdxTool/blob/libgdx1.13.1/desktop/src/com/libGdx/test/spine/SpineTest.java) | [ActorSpine.java](https://github.com/dm-kangwang/LibgdxTool/blob/libgdx1.13.1/desktop/src/com/libGdx/test/spine/ActorSpine.java)

演示 Spine 骨骼动画在 LibGDX 中的集成，`ActorSpine` 将 Spine 动画封装为 `Actor` 便于加入 Stage 管理，`SpineTest` 是完整的动画播放测试。

**关键知识点：**
- `SkeletonRenderer` 骨骼渲染器
- `AnimationState.setAnimation()` 设置动画
- `SpineActor` 封装（`com.esotericsoftware.spine`）
- 动画混合与过渡

---

### 83. `spineanimation` — Spine 动画切换控制
**文件：** [spineanimation 目录](https://github.com/dm-kangwang/LibgdxTool/tree/libgdx1.13.1/desktop/src/com/libGdx/test/spineanimation)

进阶版 Spine 动画演示，重点测试动画状态切换（行走→跑步→攻击→死亡）、动画队列、混合过渡时间配置。

**关键知识点：**
- `AnimationState.addAnimation()` 动画队列
- `AnimationStateData.setMix()` 过渡时间
- 监听动画事件（开始/结束/帧事件）

---

### 84. `sprite` — Sprite/精灵渲染
**文件：** [sprite 目录](https://github.com/dm-kangwang/LibgdxTool/tree/libgdx1.13.1/desktop/src/com/libGdx/test/sprite)

演示 LibGDX `Sprite` 类的使用，包括精灵的位置、旋转、缩放、翻转、颜色叠加，以及 `SpriteBatch` 的批量渲染优化。

**关键知识点：**
- `Sprite` vs `Image` 的区别
- `SpriteBatch.draw()` 参数
- Atlas 精灵表动画帧序列

---

### 85. `stencil` — 模板缓冲 Stencil
**文件：** [Cir.java](https://github.com/dm-kangwang/LibgdxTool/blob/libgdx1.13.1/desktop/src/com/libGdx/test/stencil/Cir.java) | [SeneTest.java](https://github.com/dm-kangwang/LibgdxTool/blob/libgdx1.13.1/desktop/src/com/libGdx/test/stencil/SeneTest.java) | [Test.java](https://github.com/dm-kangwang/LibgdxTool/blob/libgdx1.13.1/desktop/src/com/libGdx/test/stencil/Test.java)

演示 OpenGL Stencil Buffer（模板缓冲）的实际应用：
- `Cir`：圆形 Stencil 遮罩（圆形内显示内容）
- `SeneTest`：场景级 Stencil 效果（如探照灯视野）
- 控制 `GL_STENCIL_TEST` 实现复杂遮罩

**关键知识点：**
- `Gdx.gl.glEnable(GL20.GL_STENCIL_TEST)`
- `glStencilFunc` / `glStencilOp` 配置
- Stencil 写入遮罩 → 根据 Stencil 值渲染内容

---

### 86. `table` — Table 布局
**文件：** [table 目录](https://github.com/dm-kangwang/LibgdxTool/tree/libgdx1.13.1/desktop/src/com/libGdx/test/table)

演示 LibGDX Scene2D `Table` 布局系统，类似 HTML `<table>`，通过 `add()`, `row()`, `pad()`, `expand()`, `fill()` 实现复杂 UI 自适应布局。

**关键知识点：**
- `table.add(widget).width(x).height(y).pad(p)`
- `table.row()` 换行
- `expand()` / `fill()` 拉伸占满
- 嵌套 Table 构建复杂界面

---

### 87. `task` — 异步任务调度
**文件：** [task 目录](https://github.com/dm-kangwang/LibgdxTool/tree/libgdx1.13.1/desktop/src/com/libGdx/test/task)

演示 LibGDX `Timer` 定时任务与 `ThreadUtils` 异步任务调度，包括延迟执行、周期执行、后台计算后回到 GL 主线程更新 UI。

**关键知识点：**
- `Timer.schedule(task, delay, interval)` 定时
- `ThreadUtils.doTask()` 异步 + 主线程回调
- 避免在非 GL 线程操作 OpenGL 资源

---

### 88. `terrin` — 地形高度图生成
**文件：** [TerrinApp.java](https://github.com/dm-kangwang/LibgdxTool/blob/libgdx1.13.1/desktop/src/com/libGdx/test/terrin/TerrinApp.java)

将灰度图转换为 1000×1000 浮点高度图数组，用于 3D 地形网格生成。像素 R 通道值（0-1）映射为高度（-10 到 +10），是程序化地形的基础。

```java
// 灰度图 R 通道 → 地形高度
heightMap[y][x] = AMPLITUDE * (sample.r - 0.5f);
```

---

### 89. `tetris` — 俄罗斯方块游戏
**文件：** [TetrisApp.java](https://github.com/dm-kangwang/LibgdxTool/blob/libgdx1.13.1/desktop/src/com/libGdx/test/tetris/TetrisApp.java)

完整的俄罗斯方块游戏实现，包含七种方块形状（I/O/T/S/Z/J/L）、旋转、碰撞、消行、计分等核心逻辑，是综合性游戏逻辑案例。

**关键知识点：**
- 二维格子数组表示棋盘
- 方块旋转矩阵变换
- 碰撞检测与消行算法
- 游戏循环（下落计时）

---

### 90. `textfield` — 文本输入框
**文件：** [textfield 目录](https://github.com/dm-kangwang/LibgdxTool/tree/libgdx1.13.1/desktop/src/com/libGdx/test/textfield)

演示 LibGDX `TextField` 文本输入控件，包括自定义样式、输入过滤（只允许数字）、软键盘弹出处理、输入内容监听。

**关键知识点：**
- `TextField` + `TextFieldStyle`
- `TextFieldFilter` 输入过滤
- `TextField.TextFieldListener` 文本变化回调

---

### 91. `thread` — 多线程异步加载
**文件：** [ThreadTest.java](https://github.com/dm-kangwang/LibgdxTool/blob/libgdx1.13.1/desktop/src/com/libGdx/test/thread/ThreadTest.java)

演示 LibGDX 中安全的多线程使用：后台线程执行耗时操作（模拟延迟 3 秒），完成后通过回调在 GL 主线程更新 UI，避免线程安全问题。

```java
threadUtils.doTask(new Task<Boolean>() {
    public Boolean doRunnable() { /* 后台线程 */ }
    public void success(Boolean r) { /* GL 主线程回调 */ }
});
```

---

### 92. `throwa` — 投掷/抛物线
**文件：** [throwa 目录](https://github.com/dm-kangwang/LibgdxTool/tree/libgdx1.13.1/desktop/src/com/libGdx/test/throwa)

演示抛体运动物理模拟，计算给定初速度和角度的抛物线轨迹，可视化落点预测，用于投掷武器、炮弹轨迹等游戏场景。

**关键知识点：**
- 抛体运动公式（x=v₀cosθ·t, y=v₀sinθ·t-½gt²）
- 轨迹预测点计算
- `ShapeRenderer` 绘制轨迹曲线

---

### 93. `tietu` — 贴图 UV 操作
**文件：** [tietu 目录](https://github.com/dm-kangwang/LibgdxTool/tree/libgdx1.13.1/desktop/src/com/libGdx/test/tietu)

演示 UV 贴图坐标操作，包括纹理重复、UV 动画（滚动贴图）、UV 扭曲变形等，常用于水面流动、传送带移动等视觉效果。

**关键知识点：**
- `TextureRegion.setRegion()` UV 范围设置
- `Texture.TextureWrap.Repeat` 重复模式
- 每帧偏移 UV 实现流动效果

---

### 94. `time` — 计时器/倒计时
**文件：** [TimeApp.java](https://github.com/dm-kangwang/LibgdxTool/blob/libgdx1.13.1/desktop/src/com/libGdx/test/time/TimeApp.java)

解析 HTTP 服务器 GMT 时间字符串，计算与本地截止时间的差值，通过 `Timer` 每秒刷新，显示 `天 HH:MM:SS` 格式倒计时，适用于活动倒计时场景。

```java
// 解析服务器时间 "Fri, 23 Feb 2024 08:14:55 GMT"
SimpleDateFormat fmt = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz", Locale.ENGLISH);
// 每秒更新倒计时
timer.schedule(task, 0, 1000);
```

---

### 95. `touch` — 触摸/多点触控
**文件：** [touch 目录](https://github.com/dm-kangwang/LibgdxTool/tree/libgdx1.13.1/desktop/src/com/libGdx/test/touch)

演示 LibGDX 多点触控处理，获取多个触摸点的位置与状态，实现双指捏合缩放、双指旋转等多指手势。

**关键知识点：**
- `Gdx.input.isTouched(pointer)` 多点触控
- `InputAdapter.touchDown/touchUp/touchDragged`
- `GestureDetector` 手势识别封装

---

### 96. `trile` — Tiled 地图
**文件：** [trile/utils 目录](https://github.com/dm-kangwang/LibgdxTool/tree/libgdx1.13.1/desktop/src/com/libGdx/test/trile/utils)

演示 LibGDX 加载 Tiled（TMX 格式）地图，包括图层渲染、对象层读取（碰撞体、出生点），以及地图与摄像机的配合移动。

**关键知识点：**
- `TmxMapLoader` 加载 TMX 文件
- `OrthogonalTiledMapRenderer` 渲染
- `MapObjects` 读取对象层数据

---

### 97. `trycatch` — 异常处理
**文件：** [trycatch 目录](https://github.com/dm-kangwang/LibgdxTool/tree/libgdx1.13.1/desktop/src/com/libGdx/test/trycatch)

演示 LibGDX 游戏中的异常捕获与处理策略，包括资源加载失败的降级处理、网络超时重试、崩溃日志上报等健壮性设计。

---

### 98. `tt` — 综合功能测试
**文件：** [tt 目录](https://github.com/dm-kangwang/LibgdxTool/tree/libgdx1.13.1/desktop/src/com/libGdx/test/tt)

综合测试目录，用于快速验证各种功能组合的可行性，通常是最新开发特性的临时测试场所。

---

### 99. `vect` — 向量运算
**文件：** [vect 目录](https://github.com/dm-kangwang/LibgdxTool/tree/libgdx1.13.1/desktop/src/com/libGdx/test/vect)

演示 LibGDX `Vector2`/`Vector3` 向量运算，包括加减法、点积、叉积、归一化、旋转、插值，以及在游戏移动、力学计算中的应用。

**关键知识点：**
- `Vector2.add/sub/scl/nor`
- `Vector2.dot()` 点积（计算夹角）
- `Vector2.crs()` 叉积（判断左右侧）
- `Vector2.lerp()` 线性插值

---

### 100. `version` — 版本管理
**文件：** [version 目录](https://github.com/dm-kangwang/LibgdxTool/tree/libgdx1.13.1/desktop/src/com/libGdx/test/version)

演示游戏版本号管理与热更新检测，比较本地版本与服务器版本，决定是否需要更新资源包。

---

### 101. `vertices` — 顶点操作
**文件：** [vertices 目录](https://github.com/dm-kangwang/LibgdxTool/tree/libgdx1.13.1/desktop/src/com/libGdx/test/vertices)

演示直接操作 SpriteBatch 底层顶点数据，实现扭曲变形（桶形/鱼眼变形）、顶点色插值渐变等特效，比 Shader 更低层次的渲染控制。

**关键知识点：**
- `SpriteBatch.draw(texture, float[] spriteVertices, ...)` 自定义顶点
- 顶点格式（x/y/color/u/v）
- 手动构建四边形顶点数组

---

### 102. `video` — 视频播放
**文件：** [video 目录](https://github.com/dm-kangwang/LibgdxTool/tree/libgdx1.13.1/desktop/src/com/libGdx/test/video)

演示在 LibGDX 中播放视频文件（通常借助 `gdx-video` 扩展库），用于游戏开场动画、过场 CG 等。

**关键知识点：**
- `VideoPlayer` 视频播放器
- 视频纹理实时更新到 Texture
- 播放完毕回调处理

---

### 103. `view` — 视图/视口管理
**文件：** [view 目录](https://github.com/dm-kangwang/LibgdxTool/tree/libgdx1.13.1/desktop/src/com/libGdx/test/view)

演示 LibGDX 各种 `Viewport`（视口）类型的使用与差异比较：`FitViewport`、`StretchViewport`、`ExtendViewport`，解决多分辨率适配问题。

**关键知识点：**
- `FitViewport`：保持比例，两侧留黑边
- `StretchViewport`：拉伸填满（可能变形）
- `ExtendViewport`：扩展显示区域
- `viewport.update(w, h, true)` 窗口大小变化响应

---

### 104. `wak` — 行走/导航
**文件：** [wak 目录](https://github.com/dm-kangwang/LibgdxTool/tree/libgdx1.13.1/desktop/src/com/libGdx/test/wak)

演示角色行走动画与移动的结合，点击目标位置后角色通过寻路算法行走过去，同时播放行走动画，到达后切换为站立动画。

**关键知识点：**
- 点击位置 → 寻路 → 移动
- 行走/站立动画状态切换
- 移动方向与角色朝向同步

---

### 105. `wakong` — 挖空/遮罩效果
**文件：** [App.java](https://github.com/dm-kangwang/LibgdxTool/blob/libgdx1.13.1/desktop/src/com/libGdx/test/wakong/App.java) | [Wk.java](https://github.com/dm-kangwang/LibgdxTool/blob/libgdx1.13.1/desktop/src/com/libGdx/test/wakong/Wk.java)

演示挖空（Punch-through/Knockout）遮罩效果，在黑暗遮罩层上挖出透明孔洞显示下方场景，常用于新手引导聚光灯、探照灯视野效果。基于 Stencil Buffer 或 Alpha 混合实现。

**关键知识点：**
- Stencil Buffer 挖空技术
- `BlendingAttribute` Alpha 混合反转
- 新手引导遮罩高亮实现

---

### 106. `watch` — 表盘/看门狗
**文件：** [WatchDogTest.java](https://github.com/dm-kangwang/LibgdxTool/blob/libgdx1.13.1/desktop/src/com/libGdx/test/watch/WatchDogTest.java)

演示 WatchDog（看门狗）机制测试，监控主线程是否正常运行，防止游戏卡死不响应，超时后触发报警或强制重启。

---

### 107. `xiaoguo` — 特效综合
**文件：** [xiaoguo 目录](https://github.com/dm-kangwang/LibgdxTool/tree/libgdx1.13.1/desktop/src/com/libGdx/test/xiaoguo)

综合视觉特效案例，组合使用 Shader、粒子、Action 动画实现游戏中的复杂特效，如技能释放特效、道具拾取特效等。

---

### 108. `xml` — XML 解析
**文件：** [xml 目录](https://github.com/dm-kangwang/LibgdxTool/tree/libgdx1.13.1/desktop/src/com/libGdx/test/xml)

演示使用 LibGDX `XmlReader` 解析 XML 格式的游戏配置文件，读取关卡数据、技能配置、对话脚本等结构化数据。

**关键知识点：**
- `XmlReader.parse(file)` 解析
- `element.getAttribute()` 读取属性
- `element.getChildByName()` 读取子节点

---

### 109. `zhujie` — 注解/Annotation
**文件：** [zhujie 目录](https://github.com/dm-kangwang/LibgdxTool/tree/libgdx1.13.1/desktop/src/com/libGdx/test/zhujie)

演示 Java 自定义注解在游戏框架中的应用，如 `@GameInfo`（声明游戏分辨率）、`@Subscribe`（事件订阅）、`@Resource`（资源路径注入）等，实现声明式编程。

**关键知识点：**
- `@interface` 定义注解
- `RetentionPolicy.RUNTIME` 运行时注解
- 反射读取注解参数
- 注解驱动的框架配置

---

## 📊 案例分类统计

| 分类 | 案例包 | 数量 |
|------|--------|------|
| 🎨 渲染/绘图 | poly, mesh, vertices, shaper, sprite, render, pixmap, pix, tietu | 9 |
| ✨ Shader/特效 | shader, bloom, bianyuan, wakong, xiaoguo, stencil, scissortest, effect | 8 |
| 🦴 动画/运动 | spine, spineanimation, action, lizi, pictureTrail, line, path, movetest, roll, dyn | 10 |
| 📐 UI/布局 | table, label, textfield, scrollpanel, scrollroll, sc, process, cirprogres, fivestar, view, position, format | 12 |
| 🎮 游戏逻辑 | tetris, ball, pengzhuang, hit, ray, throwa, endless, pet, connectdot, mdesl | 10 |
| 📁 数据/工具 | json, xml, csv, file, format, generator, zhujie, ecode, sixteen | 9 |
| 🧵 系统/并发 | thread, task, anr, event, listener, log, trycatch, watch | 8 |
| 🌐 网络/下载 | net, down | 2 |
| 📷 摄像机/视图 | camera, view, screen, freecenterscale, pan, touch | 6 |
| 🗺️ 地图/寻路 | trile, npath, dfs, wak, path | 5 |
| 🎓 学习/兼容 | learn, cocos, language, version | 4 |
| 🔧 基础/工具 | base, common, color, colorcircle, time, vect, point, interf, clip, cut, cir | 11 |
| 🎲 3D | model, modelnew, lib3d, bullet, light, terrin | 6 |

---

## 🚀 快速开始

### 运行任意案例

1. 找到对应包下的 `XxxApp.java` 或 `XxxTest.java`
2. 直接运行其 `main()` 方法即可

```java
// 示例：运行 Spine 案例
public class SpineTest extends LibGdxTestMain {
    public static void main(String[] args) {
        SpineTest test = new SpineTest();
        test.start();  // 启动 LibGDX 桌面窗口
    }
}
```

### 添加新案例

```java
package com.libGdx.test.mycase;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.libGdx.test.base.LibGdxTestMain;

public class MyCaseApp extends LibGdxTestMain {
    public static void main(String[] args) {
        new MyCaseApp().start();
    }

    @Override
    public void useShow(Stage stage) {
        super.useShow(stage);
        // 在这里添加你的演示代码
    }
}
```

---

> 📌 **说明：** 本文档由 GitHub Copilot 基于 `desktop/src/com/libGdx/test/` 下全部 109+ 个子模块的源码结构与文件内容自动整理生成，最后更新于 2026-04-24。
