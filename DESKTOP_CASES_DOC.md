# 📖 LibgdxTool — Desktop 案例完整文档

> 仓库：[dm-kangwang/LibgdxTool](https://github.com/dm-kangwang/LibgdxTool/tree/libgdx1.13.1/desktop/src/com/libGdx/test)
> 分支：`libgdx1.13.1`
> 语言：Java (98.7%) + GLSL (1.3%)

---

## 📁 目录结构总览

所有 Desktop 案例均位于：

```
desktop/src/com/libGdx/test/
```

共包含 **80+ 个子模块**，每个子目录对应一类具体技术演示案例。

---

## 🗂️ 案例分类索引

| 序号 | 包名 | 功能描述 | 链接 |
|------|------|----------|------|
| 1 | `action` | Action 动画系统 | [查看](https://github.com/dm-kangwang/LibgdxTool/tree/libgdx1.13.1/desktop/src/com/libGdx/test/action) |
| 2 | `alpha` | 透明度渐变效果 | [查看](https://github.com/dm-kangwang/LibgdxTool/tree/libgdx1.13.1/desktop/src/com/libGdx/test/alpha) |
| 3 | `anr` | ANR / 卡顿检测 | [查看](https://github.com/dm-kangwang/LibgdxTool/tree/libgdx1.13.1/desktop/src/com/libGdx/test/anr) |
| 4 | `asset` | 资源加载管理 | [查看](https://github.com/dm-kangwang/LibgdxTool/tree/libgdx1.13.1/desktop/src/com/libGdx/test/asset) |
| 5 | `ball` | 球体物理/运动 | [查看](https://github.com/dm-kangwang/LibgdxTool/tree/libgdx1.13.1/desktop/src/com/libGdx/test/ball) |
| 6 | `base` | 基础测试框架 | [查看](https://github.com/dm-kangwang/LibgdxTool/tree/libgdx1.13.1/desktop/src/com/libGdx/test/base) |
| 7 | `beser` | 贝塞尔曲线 | [查看](https://github.com/dm-kangwang/LibgdxTool/tree/libgdx1.13.1/desktop/src/com/libGdx/test/beser) |
| 8 | `bianyuan` | 描边/边缘效果 | [查看](https://github.com/dm-kangwang/LibgdxTool/tree/libgdx1.13.1/desktop/src/com/libGdx/test/bianyuan) |
| 9 | `bloom` | Bloom 辉光后处理 | [查看](https://github.com/dm-kangwang/LibgdxTool/tree/libgdx1.13.1/desktop/src/com/libGdx/test/bloom) |
| 10 | `bullet` | Bullet 物理引擎 3D | [查看](https://github.com/dm-kangwang/LibgdxTool/tree/libgdx1.13.1/desktop/src/com/libGdx/test/bullet) |
| 11 | `camera` | 摄像机控制 | [查看](https://github.com/dm-kangwang/LibgdxTool/tree/libgdx1.13.1/desktop/src/com/libGdx/test/camera) |
| 12 | `cir` | 圆形绘制 | [查看](https://github.com/dm-kangwang/LibgdxTool/tree/libgdx1.13.1/desktop/src/com/libGdx/test/cir) |
| 13 | `cirprogres` | 圆形进度条 | [查看](https://github.com/dm-kangwang/LibgdxTool/tree/libgdx1.13.1/desktop/src/com/libGdx/test/cirprogres) |
| 14 | `click` | 点击/触摸事件 | [查看](https://github.com/dm-kangwang/LibgdxTool/tree/libgdx1.13.1/desktop/src/com/libGdx/test/click) |
| 15 | `clip` | 裁剪区域 | [查看](https://github.com/dm-kangwang/LibgdxTool/tree/libgdx1.13.1/desktop/src/com/libGdx/test/clip) |
| 16 | `cocos` | Cocos 资源加载兼容 | [查看](https://github.com/dm-kangwang/LibgdxTool/tree/libgdx1.13.1/desktop/src/com/libGdx/test/cocos) |
| 17 | `color` | 颜色转换与调色 | [查看](https://github.com/dm-kangwang/LibgdxTool/tree/libgdx1.13.1/desktop/src/com/libGdx/test/color) |
| 18 | `colorcircle` | 色环/颜色选择器 | [查看](https://github.com/dm-kangwang/LibgdxTool/tree/libgdx1.13.1/desktop/src/com/libGdx/test/colorcircle) |
| 19 | `common` | 公共组件 | [查看](https://github.com/dm-kangwang/LibgdxTool/tree/libgdx1.13.1/desktop/src/com/libGdx/test/common) |
| 20 | `connectdot` | 连线/点连接 | [查看](https://github.com/dm-kangwang/LibgdxTool/tree/libgdx1.13.1/desktop/src/com/libGdx/test/connectdot) |
| 21 | `csv` | CSV 文件解析 | [查看](https://github.com/dm-kangwang/LibgdxTool/tree/libgdx1.13.1/desktop/src/com/libGdx/test/csv) |
| 22 | `cut` | 图片裁切 | [查看](https://github.com/dm-kangwang/LibgdxTool/tree/libgdx1.13.1/desktop/src/com/libGdx/test/cut) |
| 23 | `dfs` | DFS 深度优先搜索算法 | [查看](https://github.com/dm-kangwang/LibgdxTool/tree/libgdx1.13.1/desktop/src/com/libGdx/test/dfs) |
| 24 | `down` | 下载功能 | [查看](https://github.com/dm-kangwang/LibgdxTool/tree/libgdx1.13.1/desktop/src/com/libGdx/test/down) |
| 25 | `dyn` | 动态效果 | [查看](https://github.com/dm-kangwang/LibgdxTool/tree/libgdx1.13.1/desktop/src/com/libGdx/test/dyn) |
| 26 | `ecode` | 编码/二维码 | [查看](https://github.com/dm-kangwang/LibgdxTool/tree/libgdx1.13.1/desktop/src/com/libGdx/test/ecode) |
| 27 | `effect` | 视觉特效 | [查看](https://github.com/dm-kangwang/LibgdxTool/tree/libgdx1.13.1/desktop/src/com/libGdx/test/effect) |
| 28 | `endless` | 无限滚动 | [查看](https://github.com/dm-kangwang/LibgdxTool/tree/libgdx1.13.1/desktop/src/com/libGdx/test/endless) |
| 29 | `event` | 事件系统 | [查看](https://github.com/dm-kangwang/LibgdxTool/tree/libgdx1.13.1/desktop/src/com/libGdx/test/event) |
| 30 | `file` | 文件读写/坐标格式转换 | [查看](https://github.com/dm-kangwang/LibgdxTool/tree/libgdx1.13.1/desktop/src/com/libGdx/test/file) |
| 31 | `fivestar` | 五角星评分组件 | [查看](https://github.com/dm-kangwang/LibgdxTool/tree/libgdx1.13.1/desktop/src/com/libGdx/test/fivestar) |
| 32 | `format` | 数字/文本格式化 | [查看](https://github.com/dm-kangwang/LibgdxTool/tree/libgdx1.13.1/desktop/src/com/libGdx/test/format) |
| 33 | `freecenterscale` | 自由中心缩放 | [查看](https://github.com/dm-kangwang/LibgdxTool/tree/libgdx1.13.1/desktop/src/com/libGdx/test/freecenterscale) |
| 34 | `game` | 游戏逻辑综合 | [查看](https://github.com/dm-kangwang/LibgdxTool/tree/libgdx1.13.1/desktop/src/com/libGdx/test/game) |
| 35 | `generator` | 代码生成器 | [查看](https://github.com/dm-kangwang/LibgdxTool/tree/libgdx1.13.1/desktop/src/com/libGdx/test/generator) |
| 36 | `hit` | 碰撞命中检测 | [查看](https://github.com/dm-kangwang/LibgdxTool/tree/libgdx1.13.1/desktop/src/com/libGdx/test/hit) |
| 37 | `interf` | 接口/Interface 使用 | [查看](https://github.com/dm-kangwang/LibgdxTool/tree/libgdx1.13.1/desktop/src/com/libGdx/test/interf) |
| 38 | `json` | JSON 解析与 JavaBean 生成 | [查看](https://github.com/dm-kangwang/LibgdxTool/tree/libgdx1.13.1/desktop/src/com/libGdx/test/json) |
| 39 | `label` | Label 文本控件 | [查看](https://github.com/dm-kangwang/LibgdxTool/tree/libgdx1.13.1/desktop/src/com/libGdx/test/label) |
| 40 | `language` | 国际化/多语言 | [查看](https://github.com/dm-kangwang/LibgdxTool/tree/libgdx1.13.1/desktop/src/com/libGdx/test/language) |
| 41 | `learn` | 学习示例 (IciclesGame等) | [查看](https://github.com/dm-kangwang/LibgdxTool/tree/libgdx1.13.1/desktop/src/com/libGdx/test/learn) |
| 42 | `lib3d` | 3D 功能入门 | [查看](https://github.com/dm-kangwang/LibgdxTool/tree/libgdx1.13.1/desktop/src/com/libGdx/test/lib3d) |
| 43 | `light` | 光照效果 | [查看](https://github.com/dm-kangwang/LibgdxTool/tree/libgdx1.13.1/desktop/src/com/libGdx/test/light) |
| 44 | `line` | 线段/LineTime 动画 | [查看](https://github.com/dm-kangwang/LibgdxTool/tree/libgdx1.13.1/desktop/src/com/libGdx/test/line) |
| 45 | `listener` | 监听器系统 | [查看](https://github.com/dm-kangwang/LibgdxTool/tree/libgdx1.13.1/desktop/src/com/libGdx/test/listener) |
| 46 | `lizi` | 粒子系统 | [查看](https://github.com/dm-kangwang/LibgdxTool/tree/libgdx1.13.1/desktop/src/com/libGdx/test/lizi) |
| 47 | `log` | 日志系统 | [查看](https://github.com/dm-kangwang/LibgdxTool/tree/libgdx1.13.1/desktop/src/com/libGdx/test/log) |
| 48 | `mdesl` | 自定义滑动列表 (FixedList) | [查看](https://github.com/dm-kangwang/LibgdxTool/tree/libgdx1.13.1/desktop/src/com/libGdx/test/mdesl) |
| 49 | `mesh` | Mesh 网格绘制 | [查看](https://github.com/dm-kangwang/LibgdxTool/tree/libgdx1.13.1/desktop/src/com/libGdx/test/mesh) |
| 50 | `model` | 3D 模型加载 | [查看](https://github.com/dm-kangwang/LibgdxTool/tree/libgdx1.13.1/desktop/src/com/libGdx/test/model) |
| 51 | `modelnew` | 新版 3D 模型 | [查看](https://github.com/dm-kangwang/LibgdxTool/tree/libgdx1.13.1/desktop/src/com/libGdx/test/modelnew) |
| 52 | `moni` | 模拟测试 | [查看](https://github.com/dm-kangwang/LibgdxTool/tree/libgdx1.13.1/desktop/src/com/libGdx/test/moni) |
| 53 | `movetest` | 移动/运动测试 | [查看](https://github.com/dm-kangwang/LibgdxTool/tree/libgdx1.13.1/desktop/src/com/libGdx/test/movetest) |
| 54 | `mult` | 多人/多处理 | [查看](https://github.com/dm-kangwang/LibgdxTool/tree/libgdx1.13.1/desktop/src/com/libGdx/test/mult) |
| 55 | `net` | 网络通信 | [查看](https://github.com/dm-kangwang/LibgdxTool/tree/libgdx1.13.1/desktop/src/com/libGdx/test/net) |
| 56 | `npath` | 导航/寻路 | [查看](https://github.com/dm-kangwang/LibgdxTool/tree/libgdx1.13.1/desktop/src/com/libGdx/test/npath) |
| 57 | `other` | Shader 曲线变形 (Desk) | [查看](https://github.com/dm-kangwang/LibgdxTool/tree/libgdx1.13.1/desktop/src/com/libGdx/test/other) |
| 58 | `pan` | 平移/拖拽 | [查看](https://github.com/dm-kangwang/LibgdxTool/tree/libgdx1.13.1/desktop/src/com/libGdx/test/pan) |
| 59 | `path` | 路径跟随动画 | [查看](https://github.com/dm-kangwang/LibgdxTool/tree/libgdx1.13.1/desktop/src/com/libGdx/test/path) |
| 60 | `pengzhuang` | 碰撞检测 | [查看](https://github.com/dm-kangwang/LibgdxTool/tree/libgdx1.13.1/desktop/src/com/libGdx/test/pengzhuang) |
| 61 | `pet` | 宠物/跟随 AI | [查看](https://github.com/dm-kangwang/LibgdxTool/tree/libgdx1.13.1/desktop/src/com/libGdx/test/pet) |
| 62 | `pic` | 图片处理 | [查看](https://github.com/dm-kangwang/LibgdxTool/tree/libgdx1.13.1/desktop/src/com/libGdx/test/pic) |
| 63 | `pictureTrail` | 图片拖尾效果 | [查看](https://github.com/dm-kangwang/LibgdxTool/tree/libgdx1.13.1/desktop/src/com/libGdx/test/pictureTrail) |
| 64 | `pix` | 像素级操作 | [查看](https://github.com/dm-kangwang/LibgdxTool/tree/libgdx1.13.1/desktop/src/com/libGdx/test/pix) |
| 65 | `pixmap` | Pixmap 纹理生成 | [查看](https://github.com/dm-kangwang/LibgdxTool/tree/libgdx1.13.1/desktop/src/com/libGdx/test/pixmap) |
| 66 | `point` | 点/坐标操作 | [查看](https://github.com/dm-kangwang/LibgdxTool/tree/libgdx1.13.1/desktop/src/com/libGdx/test/point) |
| 67 | `poly` | 多边形绘制与裁剪 | [查看](https://github.com/dm-kangwang/LibgdxTool/tree/libgdx1.13.1/desktop/src/com/libGdx/test/poly) |
| 68 | `position` | 坐标系/定位 | [查看](https://github.com/dm-kangwang/LibgdxTool/tree/libgdx1.13.1/desktop/src/com/libGdx/test/position) |
| 69 | `process` | 进度条 | [查看](https://github.com/dm-kangwang/LibgdxTool/tree/libgdx1.13.1/desktop/src/com/libGdx/test/process) |
| 70 | `qx` | 曲线绘制 | [查看](https://github.com/dm-kangwang/LibgdxTool/tree/libgdx1.13.1/desktop/src/com/libGdx/test/qx) |
| 71 | `ray` | 射线检测 | [查看](https://github.com/dm-kangwang/LibgdxTool/tree/libgdx1.13.1/desktop/src/com/libGdx/test/ray) |
| 72 | `render` | 渲染管线/FrameBuffer | [查看](https://github.com/dm-kangwang/LibgdxTool/tree/libgdx1.13.1/desktop/src/com/libGdx/test/render) |
| 73 | `roll` | 滚动效果 | [查看](https://github.com/dm-kangwang/LibgdxTool/tree/libgdx1.13.1/desktop/src/com/libGdx/test/roll) |
| 74 | `sc` | 虚拟列表/邮件列表复用 | [查看](https://github.com/dm-kangwang/LibgdxTool/tree/libgdx1.13.1/desktop/src/com/libGdx/test/sc) |
| 75 | `scissortest` | 剪切测试 | [查看](https://github.com/dm-kangwang/LibgdxTool/tree/libgdx1.13.1/desktop/src/com/libGdx/test/scissortest) |
| 76 | `screen` | 屏幕切换管理 | [查看](https://github.com/dm-kangwang/LibgdxTool/tree/libgdx1.13.1/desktop/src/com/libGdx/test/screen) |
| 77 | `scrollpanel` | 滚动面板 | [查看](https://github.com/dm-kangwang/LibgdxTool/tree/libgdx1.13.1/desktop/src/com/libGdx/test/scrollpanel) |
| 78 | `scrollroll` | 双向滚动 | [查看](https://github.com/dm-kangwang/LibgdxTool/tree/libgdx1.13.1/desktop/src/com/libGdx/test/scrollroll) |
| 79 | `shader` | GLSL Shader 效果 (灰度/波浪) | [查看](https://github.com/dm-kangwang/LibgdxTool/tree/libgdx1.13.1/desktop/src/com/libGdx/test/shader) |
| 80 | `shaper` | ShapeRenderer 形状绘制 | [查看](https://github.com/dm-kangwang/LibgdxTool/tree/libgdx1.13.1/desktop/src/com/libGdx/test/shaper) |
| 81 | `sixteen` | 十六进制相关 | [查看](https://github.com/dm-kangwang/LibgdxTool/tree/libgdx1.13.1/desktop/src/com/libGdx/test/sixteen) |
| 82 | `spine` | Spine 骨骼动画 | [查看](https://github.com/dm-kangwang/LibgdxTool/tree/libgdx1.13.1/desktop/src/com/libGdx/test/spine) |
| 83 | `spineanimation` | Spine 动画切换控制 | [查看](https://github.com/dm-kangwang/LibgdxTool/tree/libgdx1.13.1/desktop/src/com/libGdx/test/spineanimation) |
| 84 | `sprite` | Sprite/精灵渲染 | [查看](https://github.com/dm-kangwang/LibgdxTool/tree/libgdx1.13.1/desktop/src/com/libGdx/test/sprite) |
| 85 | `stencil` | 模板缓冲 Stencil | [查看](https://github.com/dm-kangwang/LibgdxTool/tree/libgdx1.13.1/desktop/src/com/libGdx/test/stencil) |
| 86 | `table` | Table 布局 | [查看](https://github.com/dm-kangwang/LibgdxTool/tree/libgdx1.13.1/desktop/src/com/libGdx/test/table) |
| 87 | `task` | 异步任务调度 | [查看](https://github.com/dm-kangwang/LibgdxTool/tree/libgdx1.13.1/desktop/src/com/libGdx/test/task) |
| 88 | `terrin` | 地形生成 (Heightmap) | [查看](https://github.com/dm-kangwang/LibgdxTool/tree/libgdx1.13.1/desktop/src/com/libGdx/test/terrin) |
| 89 | `tetris` | 俄罗斯方块游戏 | [查看](https://github.com/dm-kangwang/LibgdxTool/tree/libgdx1.13.1/desktop/src/com/libGdx/test/tetris) |
| 90 | `textfield` | 文本输入框 | [查看](https://github.com/dm-kangwang/LibgdxTool/tree/libgdx1.13.1/desktop/src/com/libGdx/test/textfield) |
| 91 | `thread` | 多线程异步加载 | [查看](https://github.com/dm-kangwang/LibgdxTool/tree/libgdx1.13.1/desktop/src/com/libGdx/test/thread) |
| 92 | `throwa` | 投掷/抛物线 | [查看](https://github.com/dm-kangwang/LibgdxTool/tree/libgdx1.13.1/desktop/src/com/libGdx/test/throwa) |
| 93 | `tietu` | 贴图 UV 操作 | [查看](https://github.com/dm-kangwang/LibgdxTool/tree/libgdx1.13.1/desktop/src/com/libGdx/test/tietu) |
| 94 | `time` | 计时器/倒计时 | [查看](https://github.com/dm-kangwang/LibgdxTool/tree/libgdx1.13.1/desktop/src/com/libGdx/test/time) |
| 95 | `touch` | 触摸/多点触控 | [查看](https://github.com/dm-kangwang/LibgdxTool/tree/libgdx1.13.1/desktop/src/com/libGdx/test/touch) |
| 96 | `trile` | Tiled 地图 | [查看](https://github.com/dm-kangwang/LibgdxTool/tree/libgdx1.13.1/desktop/src/com/libGdx/test/trile) |
| 97 | `trycatch` | 异常处理 | [查看](https://github.com/dm-kangwang/LibgdxTool/tree/libgdx1.13.1/desktop/src/com/libGdx/test/trycatch) |
| 98 | `tt` | 综合测试 | [查看](https://github.com/dm-kangwang/LibgdxTool/tree/libgdx1.13.1/desktop/src/com/libGdx/test/tt) |
| 99 | `vect` | 向量运算 | [查看](https://github.com/dm-kangwang/LibgdxTool/tree/libgdx1.13.1/desktop/src/com/libGdx/test/vect) |
| 100 | `version` | 版本控制/管理 | [查看](https://github.com/dm-kangwang/LibgdxTool/tree/libgdx1.13.1/desktop/src/com/libGdx/test/version) |
| 101 | `vertices` | 顶点操作 | [查看](https://github.com/dm-kangwang/LibgdxTool/tree/libgdx1.13.1/desktop/src/com/libGdx/test/vertices) |
| 102 | `video` | 视频播放 | [查看](https://github.com/dm-kangwang/LibgdxTool/tree/libgdx1.13.1/desktop/src/com/libGdx/test/video) |
| 103 | `view` | 视图/视口管理 | [查看](https://github.com/dm-kangwang/LibgdxTool/tree/libgdx1.13.1/desktop/src/com/libGdx/test/view) |
| 104 | `wak` | 行走/导航 | [查看](https://github.com/dm-kangwang/LibgdxTool/tree/libgdx1.13.1/desktop/src/com/libGdx/test/wak) |
| 105 | `wakong` | 挖空/遮罩效果 | [查看](https://github.com/dm-kangwang/LibgdxTool/tree/libgdx1.13.1/desktop/src/com/libGdx/test/wakong) |
| 106 | `watch` | 表盘/时钟动画 | [查看](https://github.com/dm-kangwang/LibgdxTool/tree/libgdx1.13.1/desktop/src/com/libGdx/test/watch) |
| 107 | `xiaoguo` | 特效综合 | [查看](https://github.com/dm-kangwang/LibgdxTool/tree/libgdx1.13.1/desktop/src/com/libGdx/test/xiaoguo) |
| 108 | `xml` | XML 解析 | [查看](https://github.com/dm-kangwang/LibgdxTool/tree/libgdx1.13.1/desktop/src/com/libGdx/test/xml) |
| 109 | `zhujie` | 注解/Annotation 使用 | [查看](https://github.com/dm-kangwang/LibgdxTool/tree/libgdx1.13.1/desktop/src/com/libGdx/test/zhujie) |

---

## 🔬 重点案例详细解析

### 1️⃣ `sc` — 虚拟列表（View Recycling）

**文件：** [`desktop/src/com/libGdx/test/sc/App.java`](https://github.com/dm-kangwang/LibgdxTool/blob/libgdx1.13.1/desktop/src/com/libGdx/test/sc/App.java)

**功能：** 实现类似 Android RecyclerView 的**行复用虚拟列表**，用于高性能展示大量数据（如邮件列表）。

**核心技术要点：**

| 要素 | 说明 |
|------|------|
| `EmailData` | 数据模型（from/subject/preview 字段） |
| `EmailRow` | 可复用行视图，继承 `Table` |
| `visibleRows` | 固定大小的可见行池（屏幕高度 / 行高 + buffer） |
| `updateRows()` | 根据 `scrollPane.getVisualScrollY()` 计算首行索引，动态 bind 数据 |
| `FixedList<T>` | 固定容量数组，支持头部插入不扩容 |

```java
// 核心更新逻辑
private void updateRows() {
    float scrollY = scrollPane.getVisualScrollY();
    int firstIndex = (int)(scrollY / rowHeight);
    for (int i = 0; i < visibleRows.size; i++) {
        int index = firstIndex + i;
        EmailRow row = visibleRows.get(i);
        if (index >= 0 && index < emails.size) {
            row.setVisible(true);
            row.bind(emails.get(index));  // 复用行，只更新数据
        } else {
            row.setVisible(false);
        }
    }
}
```

---

### 2️⃣ `shader` — GLSL Shader 效果

**文件：** [`desktop/src/com/libGdx/test/shader/HuiDuZhuanC.java`](https://github.com/dm-kangwang/LibgdxTool/blob/libgdx1.13.1/desktop/src/com/libGdx/test/shader/HuiDuZhuanC.java)

**功能：** 使用自定义顶点着色器 + 片段着色器实现**灰度/波浪图像效果**。

**Shader 文件路径：**
- 顶点：`shader/huidu/wave.vert`
- 片段：`shader/huidu/wave.glsl`

```java
// 继承 BaseGroup，传入 vert 和 frag 路径
public class HuiDuZhuanC extends BaseGroup {
    public HuiDuZhuanC() {
        super("shader/huidu/wave.vert", "shader/huidu/wave.glsl");
        image = new Image(Asset.getAsset().getTexture("img_1.png"));
        addActor(image);
        image.setPosition(0, 0, Align.center);
    }
}
```

---

### 3️⃣ `poly` — 多边形裁剪/网格

**文件：** [`desktop/src/com/libGdx/test/poly/PolyActor.java`](https://github.com/dm-kangwang/LibgdxTool/blob/libgdx1.13.1/desktop/src/com/libGdx/test/poly/PolyActor.java)

**功能：** 通过 `EarClippingTriangulator` 将任意多边形顶点集转换为三角网格，渲染为带纹理的多边形贴图区域。

**关键类：**

| 类 | 用途 |
|----|------|
| `EarClippingTriangulator` | 耳切法三角剖分 |
| `PolygonRegion` | 多边形纹理区域 |
| `PolygonSprite` | 多边形精灵（需 `PolygonSpriteBatch`） |

```java
float fv[] = { /* 7个顶点的坐标 */ };
EarClippingTriangulator triangulator = new EarClippingTriangulator();
ShortArray shortArray = triangulator.computeTriangles(fv);
PolygonRegion polyReg = new PolygonRegion(region, fv, shortArray.toArray());
poly = new PolygonSprite(polyReg);
```

---

### 4️⃣ `poly` — 世界坐标多边形测试

**文件：** [`desktop/src/com/libGdx/test/poly/WorldPolygonTest.java`](https://github.com/dm-kangwang/LibgdxTool/blob/libgdx1.13.1/desktop/src/com/libGdx/test/poly/WorldPolygonTest.java)

**功能：** 从文件读取顶点坐标（格式 `(x,y)`），用 `ShapeRenderer` 绘制多边形轮廓。

```java
@GameInfo(width = 720, height = 1280, batch = Constant.COUPOLYGONBATCH)
public class WorldPolygonTest extends LibGdxTestMain { ... }
```

---

### 5️⃣ `thread` — 多线程异步加载

**文件：** [`desktop/src/com/libGdx/test/thread/ThreadTest.java`](https://github.com/dm-kangwang/LibgdxTool/blob/libgdx1.13.1/desktop/src/com/libGdx/test/thread/ThreadTest.java)

**功能：** 演示 LibGDX 中的**异步线程任务**，后台线程延迟 3 秒后在 GL 线程回调显示新图片。

```java
threadUtils.doTask(new Task<Boolean>() {
    @Override
    public Boolean doRunnable() {
        Thread.sleep(3000);   // 后台线程
        return true;
    }
    @Override
    public void success(Boolean texture) {
        // 回到主线程，安全添加 Actor
        Image image = new Image(Asset.getAsset().getTexture("assets/000.png"));
        stage.addActor(image);
    }
});
```

---

### 6️⃣ `color` — 颜色系统

#### `ImageColor.java` — HSV 颜色色块

```java
// 通过 HSV 创建颜色并应用到 Image
color.fromHsv(index, 0.7F, 0.8F);
image.setColor(color);
```

#### `ColorConvert.java` — Hex 颜色解析

```java
Color color = Color.valueOf("#4c493f");
System.out.println(color.r + " " + color.g + " " + color.b);
```

---

### 7️⃣ `file` — 文件操作工具

#### `TestFile.java` — 文件读写
测试 `FileTest` 工具类，写入多行内容后读取并打印。

#### `FileConvert.java` — 坐标格式转换工具

**作用：** libGDX ↔ Cocos 物理刚体坐标系转换
- 读取 `.xml` 或 `.plist` 格式的多边形顶点数据
- 将坐标从图片局部坐标系转换为以图片中心为原点的坐标系
- 输出为新文件

---

### 8️⃣ `json` — JSON 工具

**功能：** 给定 JSON 字符串，**自动生成对应的 Java Bean 类文件**（含嵌套对象和数组支持）。

```java
String json = "{ \"name\": \"John Doe\", \"age\": 30, \"address\": {...} }";
generateJavaBeans(json, "Person");
// → 生成 Person.java, Address.java 等文件
```

**字段类型推断：**

| JSON 类型 | Java 类型 |
|-----------|-----------|
| 字符串 | `String` |
| 整数 | `int` |
| 浮点 | `double` |
| 布尔 | `boolean` |
| 对象 | 嵌套 class |
| 数组 | 嵌套 class / `Object` |

---

### 9️⃣ `time` — 计时器/倒计时

**功能：** 解析 HTTP 服务器时间（GMT 格式），和本地截止时间对比，每秒刷新倒计时显示 `天 HH:MM:SS`。

```java
// 解析 "Fri, 23 Feb 2024 08:14:55 GMT"
SimpleDateFormat formatter = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz", Locale.ENGLISH);
Date date = formatter.parse(str);
timer.schedule(new TimerTask() {
    public void run() { l += 1000; showEnd(startTime - l); }
}, 0, 1000);
```

---

### 🔟 `line` — LineTime 线宽动画

**功能：** 利用 `Texture.TextureWrap.Repeat` + 自定义 `NumAction`，实现图像**从宽度 0 渐增到 1000** 的动画。

```java
texture.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
NumAction action = new NumAction(0, 1000) {
    public boolean act(float delta) {
        region.setRegionWidth((int) getValue());
        image.setWidth(region.getRegionWidth());
        return super.act(delta);
    }
};
action.setDuration(3);  // 3 秒完成
```

---

### 1️⃣1️⃣ `cocos` — Cocos 资源加载兼容

**功能：** 直接加载 Cocos Creator 导出的 `.json` 场景文件，在 libGDX 中还原节点树。

```java
group = CocosResource.loadFile("cocos/level2.json");
addActor(group);
group.setPosition(Constant.GAMEWIDTH/2.0f, Constant.GAMEHIGHT/2.0f, Align.center);
```

---

### 1️⃣2️⃣ `terrin` — 地形高度图生成

**功能：** 将一张灰度图解析为 1000×1000 的高度图（Height Map），用于 3D 地形生成。

```java
int rgba = pixmap.getPixel(ty, tx);
sample.set(rgba);
heightMap[y][x] = AMPLITUDE * (sample.r - 0.5f);  // 范围 [-10, 10]
```

---

### 1️⃣3️⃣ `other` — GLSL 曲线变形 Shader

**功能：** 完整的 LibGDX `ApplicationListener` + 自定义 `ShaderProgram` 示例，加载外部 GLSL 文件实现图像曲线变形效果。

```java
String vertexShaderCode = Gdx.files.internal("vvv.glsl").readString();
String fragmentShaderCode = Gdx.files.internal("fff.glsl").readString();
shader = new ShaderProgram(vertexShaderCode, fragmentShaderCode);
batch.setShader(shader);
batch.draw(texture, 0, 0, width, height);
```

---

### 1️⃣4️⃣ `mdesl` — 固定容量 FixedList

**功能：** 扩展 libGDX `Array<T>`，实现**头部插入不扩容**的固定长度数组，常用于拖尾轨迹点存储。

```java
public void insert(T t) {
    size = Math.min(size + 1, items.length);
    for (int i = size - 1; i > 0; i--) {
        items[i] = items[i - 1];  // 整体右移
    }
    items[0] = t;  // 插入头部
}
```

---

### 1️⃣5️⃣ `learn/demo2` — IciclesGame 学习项目

**功能：** 启动 `IciclesGame`，一个 Udacity 课程来源的**冰柱躲避游戏**学习案例。

```java
config.height = (int) (1920 * 0.25f);
config.width  = (int) (1080 * 0.5f);
new LwjglApplication(new IciclesGame(), config);
```

---

## 🏗️ 基础框架结构

### `LibGdxTestMain` — 所有案例的基类

路径：`desktop/src/com/libGdx/test/base/LibGdxTestMain.java`

**所有案例通用启动模式：**

```java
public class XxxApp extends LibGdxTestMain {
    public static void main(String[] args) {
        XxxApp app = new XxxApp();
        app.start();              // 一键启动，自动创建窗口
    }

    @Override
    public void useShow(Stage stage) {
        super.useShow(stage);
        // 在此添加 Actor、设置 UI...
    }
}
```

### `@GameInfo` 注解

```java
@GameInfo(width = 720, height = 1280, batch = Constant.COUPOLYGONBATCH)
public class WorldPolygonTest extends LibGdxTestMain { ... }
```

用于声明游戏分辨率和使用的 Batch 类型（普通 SpriteBatch / PolygonBatch 等）。

---

## 📊 案例分类统计

| 分类 | 包含案例 | 数量 |
|------|----------|------|
| 🎨 渲染/绘图 | poly, mesh, vertices, shaper, sprite, render, pixmap, pix | 8 |
| ✨ Shader/特效 | shader, bloom, bianyuan, wakong, xiaoguo, stencil, scissortest | 7 |
| 🦴 动画 | spine, spineanimation, action, lizi, pictureTrail, line, path | 7 |
| 📐 UI/布局 | table, label, textfield, scrollpanel, scrollroll, sc, process, cirprogres, fivestar | 9 |
| 🎮 游戏逻辑 | tetris, ball, pengzhuang, hit, ray, throwa, endless, pet | 8 |
| 📁 数据/工具 | json, xml, csv, file, format, generator, zhujie | 7 |
| 🧵 系统/并发 | thread, task, anr, event, listener, log, trycatch | 7 |
| 🌐 网络 | net, down | 2 |
| 📷 摄像机/视图 | camera, view, screen, freecenterscale, pan | 5 |
| 🗺️ 地图/寻路 | trile, npath, dfs, wak, path | 5 |
| 🎓 学习/其他 | learn, cocos, color, colorcircle, time, version 等 | 20+ |

---

> 📌 **提示：** 本文档由 GitHub Copilot 自动生成，基于 `desktop/src/com/libGdx/test/` 下所有子模块的代码结构分析整理。如需深入了解某一具体案例，可直接点击对应链接查看源码。
