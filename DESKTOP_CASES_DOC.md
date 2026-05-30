# 📖 LibgdxTool — Desktop 案例主文档

> 仓库：[dm-kangwang/LibgdxTool](https://github.com/dm-kangwang/LibgdxTool/tree/libgdx1.13.1/desktop/src/com/libGdx/test)
> 分支：`libgdx1.13.1`
> 语言：Java (98.7%) + GLSL (1.3%)
> 最后更新：2026-04-25

---

## 1. 这份仓库是什么

这个仓库不是单一游戏工程，而是一个 **LibGDX 技术案例库 + 工具库 + 学习笔记库**。
它覆盖了：

- 2D / 3D 渲染
- UI / Scene2D
- Shader / 后处理
- 物理 / 碰撞 / 射线
- 文件 / JSON / XML / CSV
- 多线程 / 定时任务 / 网络
- 地图 / 寻路 / 视口 / 摄像机
- Spine / 粒子 / 动画
- 一些可直接复用的工具类

整体价值很高，已经不只是 demo 集合，而是一个很完整的 **LibGDX 实践实验室**。

---

## 2. 学习建议：先看什么

如果你是第一次看这个仓库，建议按下面顺序：

### 入门顺序
1. `base`
2. `action`
3. `sprite`
4. `table`
5. `camera`
6. `view`
7. `file`
8. `json`
9. `thread`
10. `shader`
11. `sc`

### 进阶顺序
1. `render`
2. `stencil`
3. `bloom`
4. `model`
5. `spine`
6. `bullet`
7. `npath`
8. `trile`
9. `touch`
10. `freecenterscale`

### 工程化优先看
1. `sc`
2. `mdesl`
3. `json`
4. `file`
5. `generator`
6. `version`
7. `watch`
8. `anr`

---

## 3. 基础框架

### `LibGdxTestMain`
所有案例的通用入口基类。典型结构：

```java
public class XxxApp extends LibGdxTestMain {
    public static void main(String[] args) {
        new XxxApp().start();
    }

    @Override
    public void useShow(Stage stage) {
        super.useShow(stage);
        // 在这里添加测试内容
    }
}
```

### `@GameInfo`
用于标记分辨率和 batch 类型：

```java
@GameInfo(width = 720, height = 1280, batch = Constant.COUPOLYGONBATCH)
public class WorldPolygonTest extends LibGdxTestMain { }
```

---

## 4. 全部案例总览

下面按模块逐一整理。每个模块都包含：

- 作用
- 核心价值
- 关键知识点
- 如果有代表性文件，会额外标明

---

### 4.1 `action` — 动画系统
- 代表文件：`NumActionTest.java`
- 用途：自定义数值动画，常用于进度条、计数器、缓动效果
- 价值：展示如何把 Action 从“位移/缩放”扩展到“数值插值”
- 关键点：`Action` 子类、`addAction()`、插值更新

### 4.2 `alpha` — 透明度
- 代表文件：`AlphaTest.java`, `AlphaTestApp.java`
- 用途：淡入淡出、透明度动画、UI 过渡
- 关键点：`color.a`、`fadeIn/fadeOut`、Group 透明继承

### 4.3 `anr` — 卡顿/ANR 检测
- 用途：监测主线程卡顿，类似看门狗
- 关键点：主线程 tick 监控、超时报警、日志输出

### 4.4 `asset` — 资源管理
- 用途：统一管理纹理、字体、音频等资源
- 关键点：`AssetManager.load()`、`finishLoading()`、资源释放

### 4.5 `ball` — 球体运动
- 用途：抛物线、弹跳、简单物理运动
- 关键点：向量、速度、重力、弹性

### 4.6 `base` — 公共基础框架
- 用途：所有案例的父类和启动基础
- 关键点：窗口初始化、Stage 创建、统一生命周期

### 4.7 `beser` — 贝塞尔曲线
- 代表文件：`B.java`, `BU.java`, `BUL.java`, `BUL1.java`
- 用途：曲线绘制、路径跟随、轨迹测试
- 关键点：曲线公式、分段采样、路径运动

### 4.8 `bianyuan` — 描边
- 用途：给图像或文字增加轮廓线
- 关键点：多次偏移采样、Shader 描边、边缘判定

### 4.9 `bloom` — 辉光后处理
- 代表文件：`Bloom.java`, `BloomDemo.java`, `BloomShaderLoader.java`, `Test.java`
- 用途：实现高亮发光效果
- 关键点：FBO、亮度提取、双向高斯模糊、多 Pass 合成

### 4.10 `bullet` — 3D 物理引擎
- 代表文件：`App.java`, `BulletFlow.java`
- 用途：刚体、重力、碰撞、飞行轨迹
- 关键点：`btRigidBody`、`btDiscreteDynamicsWorld`

### 4.11 `camera` — 摄像机控制
- 代表文件：`App.java`, `Closeup.java`, `DemoCamera.java`, `OrthographicProjection.java`
- 用途：平移、缩放、跟随、正交投影
- 关键点：`OrthographicCamera`、`zoom`、`unproject()`

### 4.12 `cir` — 圆形绘制
- 用途：圆、圆弧、圆形 UI
- 关键点：`ShapeRenderer.circle()`、Pixmap 画圆

### 4.13 `cirprogres` — 圆形进度条
- 用途：技能 CD、扇形进度
- 关键点：角度裁切、Stencil、Shader

### 4.14 `click` — 点击事件
- 用途：Scene2D 点击、触摸交互
- 关键点：`ClickListener`、`InputListener`

### 4.15 `clip` — 裁剪
- 用途：矩形裁剪、区域遮罩
- 关键点：`ScissorStack`

### 4.16 `cocos` — Cocos 资源兼容
- 代表文件：`CocosApp.java`
- 用途：加载 Cocos Creator 导出的 JSON
- 关键点：节点树还原、坐标转换

### 4.17 `color` — 颜色工具
- 代表文件：`ImageColor.java`, `ColorConvert.java`
- 用途：HSV / Hex 颜色处理
- 关键点：`fromHsv()`、`Color.valueOf()`

### 4.18 `colorcircle` — 色环选择器
- 用途：HSV 色轮交互
- 关键点：Pixmap 渐变、拖拽取色

### 4.19 `common` — 公共组件
- 用途：通用工具、共享组件

### 4.20 `connectdot` — 连点游戏
- 代表文件：`ConnectDotApp.java`, `ConnectDotApp2.java`, `Dots.java`
- 用途：滑动连接同色点
- 关键点：路径检测、连线绘制、状态机

### 4.21 `csv` — CSV 解析
- 用途：策划配置、数据导入
- 关键点：字符串解析、对象映射

### 4.22 `cut` — 图片裁切
- 用途：运行时切图、区域截取
- 关键点：`TextureRegion`、`Pixmap`

### 4.23 `dfs` — 深度优先搜索
- 用途：迷宫、连通区域、搜索可视化
- 关键点：递归、栈、网格遍历

### 4.24 `down` — 下载
- 用途：文件下载、资源更新
- 关键点：HTTP 请求、进度回调、写文件

### 4.25 `dyn` — 动态效果
- 用途：动态生成和销毁对象、动态背景
- 关键点：Actor 管理、实时更新

### 4.26 `ecode` — 二维码
- 用途：二维码生成/读取
- 关键点：ZXing、Pixmap 绘制二维码

### 4.27 `effect` — 视觉特效
- 用途：爆炸、闪光、粒子、组合特效
- 关键点：Shader、粒子、Action

### 4.28 `endless` — 无限滚动
- 用途：跑酷背景循环
- 关键点：双图复位、速度控制

### 4.29 `event` — 事件系统
- 用途：模块解耦通信
- 关键点：发布/订阅、注解

### 4.30 `file` — 文件与坐标转换
- 代表文件：`TestFile.java`, `FileConvert.java`
- 用途：读写文件、XML/PLIST 坐标转换
- 关键点：`XmlReader`、图片中心坐标系

### 4.31 `fivestar` — 星级评分
- 用途：评分 UI
- 关键点：星星填充、触摸映射

### 4.32 `format` — 格式化
- 用途：数字缩写、时间格式
- 关键点：`String.format()`、K/M/B

### 4.33 `freecenterscale` — 自由中心缩放
- 用途：地图/图片双指缩放
- 关键点：`GestureDetector`、缩放中心修正

### 4.34 `game` — 游戏综合测试
- 用途：总体集成验证

### 4.35 `generator` — 代码生成器
- 用途：自动生成配置或代码

### 4.36 `hit` — 命中检测
- 用途：点击、碰撞、形状判断
- 关键点：`Actor.hit()`、`Intersector`

### 4.37 `interf` — 接口模式
- 用途：回调、策略、组件化接口

### 4.38 `json` — JSON 工具
- 代表文件：`Bean.java`, `JsonToJavaBeanGenerator.java`
- 用途：JSON 序列化/反序列化，自动生成 JavaBean
- 关键点：类型推断、嵌套对象、getter/setter 生成

### 4.39 `label` — 文本控件
- 用途：字体、富文本、描边、打字机
- 关键点：`BitmapFont`、`LabelStyle`

### 4.40 `language` — 多语言
- 用途：国际化
- 关键点：`I18NBundle`

### 4.41 `learn` — 学习案例
- 代表文件：`App2.java`
- 用途：课程示例、冰柱游戏

### 4.42 `lib3d` — 3D 入门
- 用途：3D 场景、光照、相机

### 4.43 `light` — 2D 光照
- 用途：点光、锥形光、阴影

### 4.44 `line` — 线条动画
- 代表文件：`LineTime.java`
- 用途：进度线填充、宽度增长动画
- 关键点：`TextureWrap.Repeat`、`NumAction`

### 4.45 `listener` — 监听器
- 用途：事件回调、状态监听

### 4.46 `lizi` — 粒子系统
- 代表文件：`LiziUtils.java`, `BtnGroup.java`, `ProcessGroup.java`
- 用途：粒子按钮、粒子进度条
- 关键点：`ParticleEffectPool`

### 4.47 `log` — 日志
- 用途：日志分级、文件持久化

### 4.48 `mdesl` — 固定列表
- 代表文件：`FixedList.java`
- 用途：拖尾、固定容量历史数据
- 关键点：头部插入、不扩容

### 4.49 `mesh` — Mesh 绘制
- 用途：底层几何绘制
- 关键点：顶点、索引、属性

### 4.50 `model` — 3D 模型
- 代表文件：`ModelExample.java`, `TransparentModelApp.java`, `DecalExample.java`
- 用途：模型加载、透明渲染、公告板

### 4.51 `modelnew` — 新版 3D 模型
- 用途：升级版 3D 封装

### 4.52 `moni` — 模拟测试
- 用途：功能验证场景

### 4.53 `movetest` — 移动测试
- 用途：移动、缓动、插值

### 4.54 `mult` — 批量对象
- 用途：大量对象管理、性能测试

### 4.55 `net` — 网络
- 代表文件：`NetTest.java`, `LibGdxTest.java`
- 用途：HTTP 请求、异步回调

### 4.56 `npath` — 寻路
- 用途：A*、格子导航

### 4.57 `other` — Shader 曲线变形
- 代表文件：`Desk.java`
- 用途：完整 ShaderProgram 使用示例

### 4.58 `pan` — 平移拖拽
- 用途：单指拖动、边界限制

### 4.59 `path` — 路径跟随
- 用途：角色/对象沿路径移动

### 4.60 `pengzhuang` — 碰撞检测
- 代表文件：`BroadApp.java`, `BroadImage.java`
- 用途：宽相位 AABB 检测

### 4.61 `pet` — 跟随 AI
- 用途：宠物跟随、状态切换

### 4.62 `pic` — 图片处理
- 用途：翻转、旋转、缩放、合成

### 4.63 `pictureTrail` — 拖尾
- 用途：残影、轨迹、刀光

### 4.64 `pix` — 像素操作
- 用途：像素级读写、滤镜

### 4.65 `pixmap` — 程序化纹理
- 用途：运行时生成图形纹理

### 4.66 `point` — 坐标点操作
- 用途：坐标转换、点判断

### 4.67 `poly` — 多边形
- 代表文件：`PolyActor.java`, `WorldPolygonTest.java`
- 用途：多边形三角化、纹理绘制、轮廓显示

### 4.68 `position` — 定位
- 用途：对齐、锚点、布局

### 4.69 `process` — 进度条
- 用途：线性进度条

### 4.70 `qx` — 曲线绘制
- 用途：波形、曲线可视化

### 4.71 `ray` — 射线检测
- 用途：Raycast、拾取、视野

### 4.72 `render` — FrameBuffer / 渲染管线
- 用途：离屏渲染、后处理

### 4.73 `roll` — 滚动
- 用途：数字滚动、循环翻转

### 4.74 `sc` — 虚拟列表
- 代表文件：`App.java`
- 用途：RecyclerView 式复用列表
- 关键点：只创建可见项、滚动 bind 数据

### 4.75 `scissortest` — 剪切测试
- 用途：Scissor 技术验证

### 4.76 `screen` — 屏幕切换
- 用途：多 Screen 管理

### 4.77 `scrollpanel` — 滚动面板
- 用途：ScrollPane 高级用法

### 4.78 `scrollroll` — 双向滚动
- 用途：X/Y 双轴滑动

### 4.79 `shader` — Shader 集合
- 代表文件：`HuiDuZhuanC.java`, `ShaderDemo.java`, `StarField.java`, `WaterGroup.java`, `ChristmasTree.java`, `OpenAiGroup.java`, `TreeGroup.java`, `ColorCirGroup.java`, `FullQuadToy.java`, `CollapsableTextWindow.java`
- 用途：灰度、星空、水波、圣诞树、OpenAI Logo、树摇摆、色环、全屏 ShaderToy、窗口特效
- 关键点：`BaseGroup`、顶点/片元着色器、统一加载

### 4.80 `shaper` — ShapeRenderer
- 用途：线框/填充图形绘制

### 4.81 `sixteen` — 十六进制
- 用途：位运算、颜色/字节十六进制

### 4.82 `spine` — Spine 骨骼动画
- 代表文件：`SpineTest.java`, `ActorSpine.java`
- 用途：骨骼动画播放、封装为 Actor

### 4.83 `spineanimation` — Spine 切换控制
- 用途：动画队列、混合过渡

### 4.84 `sprite` — 精灵渲染
- 用途：Sprite 位置、旋转、缩放、翻转

### 4.85 `stencil` — 模板缓冲
- 代表文件：`Cir.java`, `SeneTest.java`, `Test.java`
- 用途：圆形遮罩、探照灯、区域裁剪
- 关键点：Stencil 写入 + 读取的两步流程

### 4.86 `table` — 布局
- 用途：表格布局、UI 排版

### 4.87 `task` — 任务调度
- 用途：定时任务、异步任务

### 4.88 `terrin` — 地形高度图
- 代表文件：`TerrinApp.java`
- 用途：灰度图生成 heightmap

### 4.89 `tetris` — 俄罗斯方块
- 代表文件：`TetrisApp.java`
- 用途：完整小游戏逻辑

### 4.90 `textfield` — 文本输入
- 用途：输入框、过滤、监听

### 4.91 `thread` — 多线程加载
- 代表文件：`ThreadTest.java`
- 用途：后台任务 + 回调更新 UI

### 4.92 `throwa` — 抛物线
- 用途：弹道、轨迹预测

### 4.93 `tietu` — UV 贴图
- 用途：纹理滚动、UV 变换

### 4.94 `time` — 计时器
- 代表文件：`TimeApp.java`
- 用途：倒计时、服务器时间解析

### 4.95 `touch` — 多点触控
- 用途：双指缩放、旋转

### 4.96 `trile` — Tiled 地图
- 用途：TMX 地图加载、对象层

### 4.97 `trycatch` — 异常处理
- 用途：健壮性、容错

### 4.98 `tt` — 综合测试
- 用途：临时整合验证

### 4.99 `vect` — 向量运算
- 用途：Vector2 / Vector3 数学

### 4.100 `version` — 版本管理
- 用途：版本检测、热更新判断

### 4.101 `vertices` — 顶点操作
- 用途：底层顶点变形、颜色插值

### 4.102 `video` — 视频播放
- 用途：开场动画、过场视频

### 4.103 `view` — 视口管理
- 用途：多分辨率适配

### 4.104 `wak` — 行走导航
- 用途：寻路 + 行走动画

### 4.105 `wakong` — 挖空遮罩
- 代表文件：`App.java`, `Wk.java`
- 用途：新手引导聚光灯、黑暗遮罩挖洞

### 4.106 `watch` — 看门狗
- 代表文件：`WatchDogTest.java`
- 用途：主线程监控、防卡死

### 4.107 `xiaoguo` — 特效综合
- 用途：技能特效、组合效果

### 4.108 `xml` — XML 解析
- 用途：配置文件、关卡数据

### 4.109 `zhujie` — 注解
- 用途：声明式配置、反射读取

---

## 5. 可以直接复用的高价值工具

这些不是“演示”，而是很值得复用到项目里的：

- `LibGdxTestMain`：统一启动框架
- `FixedList`：固定容量历史队列
- `JsonToJavaBeanGenerator`：JSON 转 JavaBean
- `FileConvert`：坐标转换工具
- `CocosResource`：Cocos 资源适配
- `BaseGroup`：Shader 统一封装
- `ThreadUtils` 使用方式：异步 + 主线程回调
- `BloomShaderLoader`：Shader 管理

---

## 6. 我对这个仓库的综合评价

### 优点
- 覆盖广
- 实战性强
- 有工具化沉淀
- 有明显项目经验痕迹
- 很适合学习和查阅

### 可提升点
- 增加学习路线
- 增加难度标签
- 增加入口类说明
- 增加依赖资源说明
- 命名再统一一些

---

## 7. 建议补充的文档结构

建议主文档继续扩展为下面这种结构：

1. 仓库简介
2. 学习路线图
3. 工程结构
4. 案例总览
5. 工具类索引
6. Shader 索引
7. 3D 索引
8. UI 索引
9. 最值得先学的 10 个案例
10. FAQ / 常见运行问题

---

## 8. 最后一句话

这份仓库最大的价值，不是“有多少个 demo”，而是它已经积累出了一套 **LibGDX 的知识组织方式**。
如果继续把“入口、难度、用途、依赖、运行方式”这些信息补齐，它会非常适合长期教学、查阅和复用。
