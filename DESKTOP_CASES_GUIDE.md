# Desktop 测试案例详细文档

Desktop 模块包含了 **100+ 个实战测试案例**，涵盖游戏开发的各个方面。这份文档将详细介绍这些案例的用途和实现方法。

---

## 📚 目录

- [基础框架](#基础框架)
- [UI 和演员系统](#ui-和演员系统)
- [动画系统](#动画系统)
- [特效和渲染](#特效和渲染)
- [游戏逻辑](#游戏逻辑)
- [输入和交互](#输入和交互)
- [数据处理](#数据处理)
- [高级特性](#高级特性)

---

## 基础框架

### LibGdxTestMain - 测试基类

所有桌面测试都继承自 `LibGdxTestMain`，这是一个快速启动游戏的基类。

```java
@ANRDEMO
@GameInfo(width = 1080, height = 1920)
public class LibGdxTestMain extends BaseGame {
    protected static float screenWidth = 2060;
    protected static float screenHight = 1900;
    private Stage stageMain;

    @Override
    protected void loadingView() {
        super.loadingView();
        setScreen(new TestScreen(this));
    }

    class TestScreen extends BaseScreen {
        public TestScreen(BaseGame game) {
            super(game);
            stageMain = stage;
        }

        @Override
        public void show() {
            super.show();
            useShow(stage);
        }
    }

    public void useShow(Stage stage) {
        // 重写此方法实现游戏逻辑
    }

    public void addActor(Actor actor) {
        stageMain.addActor(actor);
    }

    public void start(LibGdxTestMain test) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.x = 1000;
        config.stencil = 8;
        config.y = 0;
        config.height = (int) (screenHight * 0.5f);
        config.width = (int) (screenWidth * 0.5f);
        new LwjglApplication(test, config);
    }

    @Override
    protected void initViewport() {
        stageViewport = new ExtendViewport(1080, 1920);
    }
}
```

**快速开始模板：**

```java
public class MyTestGame extends LibGdxTestMain {
    public static void main(String[] args) {
        MyTestGame test = new MyTestGame();
        test.start(test);
    }

    @Override
    public void useShow(Stage stage) {
        super.useShow(stage);
        // 添加你的游戏对象
        Image img = new Image(...);
        addActor(img);
    }
}
```

---

## UI 和演员系统

### AppC - 图片显示

**文件：** `AppC.java`

```java
public class AppC extends LibGdxTestMain {
    public static void main(String[] args) {
        AppC appC = new AppC();
        appC.start();
    }

    @Override
    public void useShow(Stage stage) {
        super.useShow(stage);
        // 显示第一张图片
        Image image = new Image(Asset.getAsset().getTexture("assets/board1.png"));
        addActor(image);

        // 显示第二张图片，设置y位置
        Image image1 = new Image(Asset.getAsset().getTexture("assets/board2.png"));
        addActor(image1);
        image1.setY(600);
    }
}
```

**用途：** 基础图片显示和位置管理

---

### Label 系统

**文件目录：** `label/`

**用途：** 文本显示、标签管理

**示例用法：**

```java
Skin skin = new Skin(Gdx.files.internal("ui/uiskin.json"));
Label label = new Label("Score: 100", skin);
label.setPosition(100, 500);
stage.addActor(label);
```

---

### Table 系统

**文件目录：** `table/`

**用途：** UI 布局和表格显示

**示例用法：**

```java
Table table = new Table();
table.add(new Label("Player", skin)).row();
table.add(new Label("Score", skin)).row();
stage.addActor(table);
```

---

### ScrollPanel - 滚动面板

**文件目录：** `scrollpanel/`

**用途：** 可滚动的内容区域

**示例用法：**

```java
ScrollPane scrollPane = new ScrollPane(table);
scrollPane.setPosition(100, 100);
scrollPane.setSize(800, 600);
stage.addActor(scrollPane);
```

---

## 动画系统

### Spine 骨骼动画

#### SpineTest - 基础 Spine 动画

**文件：** `spine/SpineTest.java`

```java
public class SpineTest extends LibGdxTestMain {
    public static void main(String[] args) {
        SpineTest test = new SpineTest();
        test.start(test);
    }

    @Override
    public void useShow(Stage stage) {
        super.useShow(stage);
        // 加载 Spine 骨骼动画
        SpineActor actor = new SpineActor("assets/xxxx/piggg_coin");
        actor.setAnimation("animation", true);  // 循环播放
        actor.setSkin("1");  // 设置皮肤
        addActor(actor);
        actor.setPosition(450, 400);
    }
}
```

**核心 Spine 功能：**

- **加载动画：** `new SpineActor("path/to/skeleton")`
- **播放动画：** `setAnimation(animationName, loop)`
- **切换皮肤：** `setSkin(skinName)`
- **设置位置：** `setPosition(x, y)`
- **裁剪效果：** `setClip(true)`、`setBeginX/Y()`、`setW/H()`

---

#### ActorSpine - 在 Spine 中插入 UI 元素

**文件：** `spine/ActorSpine.java`

这是一个高级用法，演示了如何在 Spine 骨骼动画中嵌入 UI 元素：

```java
public class ActorSpine extends LibGdxTestMain {
    public static void main(String[] args) {
        ActorSpine test = new ActorSpine();
        test.start(test);
    }

    @Override
    public void useShow(Stage stage) {
        super.useShow(stage);
        // 加载 Spine 角色
        SpineActor actor = new SpineActor("assets/actorspine/quan_tb");
        actor.setAnimation("zhuanpan_ck", true);
        addActor(actor);
        actor.setPosition(450, 400);

        // 创建 UI 组（包含多个元素）
        Group group = new Group();

        // 添加背景图片
        Image img = new Image(Asset.getAsset().getTexture("assets/7.png"));
        group.addActor(img);
        img.setPosition(0, 0, Align.center);

        // 添加进度条
        Image progressBar = new Image(Asset.getAsset().getTexture("assets/ad_progress.png"));
        group.addActor(progressBar);
        progressBar.setPosition(0, 0, Align.center);

        // 创建 ActorAttachment（将 UI 元素附加到 Spine）
        ActorAttachment actorAttachment = new ActorAttachment("img");
        actorAttachment.setActor(group);

        // 获取 Spine 数据并替换指定的附件
        SkeletonData data = actor.getSkeleton().getData();
        Skin defaultSkin = data.getDefaultSkin();
        for (Skin.SkinEntry attachment : defaultSkin.getAttachments()) {
            if (attachment.getName().equals("xuanq2_00")) {
                attachment.setAttachment(actorAttachment);  // 替换附件
            }
        }
    }
}
```

**关键步骤：**

1. 创建 `Group` 并添加 UI 元素
2. 创建 `ActorAttachment` 并绑定 `Group`
3. 获取 Spine 的皮肤数据
4. 遍历附件并替换目标附件

**应用场景：** 创意广告、特殊 UI 效果、动态内容显示

---

### Action - 动作系统

**文件目录：** `action/`

**用途：** 管理演员的动画和行为（移动、旋转、缩放等）

```java
// 移动
actor.addAction(Actions.moveTo(100, 200, 1f));

// 旋转
actor.addAction(Actions.rotateTo(360, 2f));

// 缩放
actor.addAction(Actions.scaleTo(2f, 2f, 1f));

// 颜色变化
actor.addAction(Actions.color(Color.RED, 1f));

// 序列执行
actor.addAction(
    Actions.sequence(
        Actions.moveTo(100, 100, 1f),
        Actions.rotateTo(360, 1f),
        Actions.scaleTo(2f, 2f, 1f)
    )
);

// 并行执行
actor.addAction(
    Actions.parallel(
        Actions.moveTo(100, 100, 1f),
        Actions.rotateTo(360, 1f)
    )
);

// 重复
actor.addAction(
    Actions.forever(
        Actions.sequence(
            Actions.moveBy(50, 0, 0.5f),
            Actions.moveBy(-50, 0, 0.5f)
        )
    )
);
```

---

## 特效和渲染

### Shader - 着色器系统

#### ShaderDemo - 基础 Shader

**文件：** `shader/ShaderDemo.java`

```java
public class ShaderDemo extends ApplicationAdapter {
    private ShaderProgram shader;
    private Mesh mesh;
    private float time;

    @Override
    public void create() {
        // 加载顶点和片段着色器
        shader = new ShaderProgram(
            Gdx.files.internal("shader/shengdanshu/xxx.v"),
            Gdx.files.internal("shader/shengdanshu/yyy.f")
        );
        
        // 创建全屏四边形
        mesh = new Mesh(
            true,
            4,
            6,
            new VertexAttribute(
                VertexAttributes.Usage.Position,
                3,
                "a_position"
            )
        );

        // 设置顶点坐标
        mesh.setVertices(new float[]{
            -1f, -1f, 0f,
            1f, -1f, 0f,
            1f,  1f, 0f,
            -1f,  1f, 0f
        });

        // 设置三角形索引
        mesh.setIndices(new short[]{
            0, 1, 2,
            2, 3, 0
        });
    }

    @Override
    public void render() {
        time += Gdx.graphics.getDeltaTime();

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // 绑定着色器并传递 uniform 变量
        shader.bind();
        shader.setUniformf("u_time", time);
        shader.setUniformf("u_resolution", 
            Gdx.graphics.getWidth(),
            Gdx.graphics.getHeight()
        );

        // 渲染网格
        mesh.render(shader, GL20.GL_TRIANGLES);
    }

    @Override
    public void dispose() {
        shader.dispose();
        mesh.dispose();
    }

    public static void main(String[] args) {
        new LwjglApplication(
            new ShaderDemo(),
            new LwjglApplicationConfiguration() {{
                title = "Shader Demo";
                width = 1280;
                height = 720;
            }}
        );
    }
}
```

**着色器文件示例：**

```glsl
// vertex.glsl
#ifdef GL_ES
precision mediump float;
#endif

attribute vec3 a_position;

void main() {
    gl_Position = vec4(a_position, 1.0);
}
```

```glsl
// fragment.glsl
#ifdef GL_ES
precision mediump float;
#endif

uniform float u_time;
uniform vec2 u_resolution;

void main() {
    vec2 uv = gl_FragCoord.xy / u_resolution.xy;
    vec3 color = vec3(sin(u_time + uv.x * 10.0), 
                      cos(u_time + uv.y * 10.0), 
                      0.5);
    gl_FragColor = vec4(color, 1.0);
}
```

**着色器应用目录：**

- `shader/` - 着色器示例
  - `ShaderDemo.java` - 基础着色器演示
  - `ShaderImage.java` - 图片着色器
  - `StarField.java` - 星场效果
  - `WaterGroup.java` - 水波效果
  - `TreeGroup.java` - 树木效果
  - `ChristmasTree.java` - 圣诞树
  - `CollapsableTextWindow.java` - 可折叠窗口

---

### Particle - 粒子效果

**文件目录：** `particle/`（目录为空，但可创建粒子系统）

**粒子效果模板：**

```java
public class ParticleDemo extends LibGdxTestMain {
    public static void main(String[] args) {
        ParticleDemo test = new ParticleDemo();
        test.start(test);
    }

    private ParticleEffect particleEffect;

    @Override
    public void useShow(Stage stage) {
        super.useShow(stage);
        
        // 加载粒子效果
        particleEffect = new ParticleEffect();
        particleEffect.load(
            Gdx.files.internal("effects/explosion.p"),
            Gdx.files.internal("effects/")
        );
        particleEffect.setPosition(540, 960);
        particleEffect.start();
    }

    @Override
    public void render(float delta) {
        // 更新粒子
        particleEffect.update(delta);
        
        // 渲染粒子
        Batch batch = game.getBatch();
        batch.begin();
        particleEffect.draw(batch);
        batch.end();
    }
}
```

---

### 其他特效

- **Bloom**（`bloom/`）- 发光效果
- **Light**（`light/`）- 光照系统
- **Trail**（`trail/`）- 拖尾效果
- **Effect**（`effect/`）- 通用特效
- **Xiaoguo**（`xiaoguo/`）- 中文"效果"，特效集合

---

## 游戏逻辑

### Click - 点击游戏

**文件目录：** `click/`

**实现示例：**

```java
public class ClickGame extends LibGdxTestMain {
    private int score = 0;
    private Label scoreLabel;

    public static void main(String[] args) {
        ClickGame test = new ClickGame();
        test.start(test);
    }

    @Override
    public void useShow(Stage stage) {
        super.useShow(stage);
        
        // 创建分数标签
        Skin skin = new Skin(Gdx.files.internal("ui/uiskin.json"));
        scoreLabel = new Label("Score: 0", skin);
        scoreLabel.setPosition(100, 1800);
        stage.addActor(scoreLabel);
        
        // 创建可点击的目标
        for (int i = 0; i < 5; i++) {
            Image target = new Image(Asset.getAsset().getTexture("assets/target.png"));
            target.setSize(100, 100);
            target.setPosition(
                MathUtils.random(0, 1080),
                MathUtils.random(0, 1920)
            );
            
            final Image t = target;
            target.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    score += 10;
                    scoreLabel.setText("Score: " + score);
                    t.setPosition(
                        MathUtils.random(0, 1080),
                        MathUtils.random(0, 1920)
                    );
                }
            });
            
            stage.addActor(target);
        }
    }
}
```

---

### Ball - 球体游戏

**文件目录：** `ball/`

**用途：** 物理模拟、碰撞检测

---

### Game 相关

**文件目录：** `game/`

各种游戏逻辑示例：

- **Tetris**（`tetris/`）- 俄罗斯方块
- **Endless**（`endless/`）- 无尽游戏
- **Pengzhuang**（`pengzhuang/`）- 碰撞游戏

---

## 输入和交互

### Touch - 触屏输入

**文件目录：** `touch/`

```java
public class TouchDemo extends LibGdxTestMain {
    @Override
    public void useShow(Stage stage) {
        super.useShow(stage);
        
        Gdx.input.setInputProcessor(stage);
    }
}
```

---

### Camera - 摄像机控制

**文件：** `CameraDemo.java`

```java
public class CameraDemo extends LibGdxTestMain {
    public static void main(String[] args) {
        run(CameraDemo.class);
    }

    ShapeRenderer renderer;
    DemoCamera demoCamera;

    @Override
    public void useShow(Stage stage) {
        super.useShow(stage);
        renderer = new ShapeRenderer();
        demoCamera = new DemoCamera();
        // 设置摄像机作为输入处理器
        Gdx.input.setInputProcessor(demoCamera);
    }

    @Override
    public void resize(int width, int height) {
        demoCamera.resize(width, height);
    }
}
```

---

### Listener - 事件监听

**文件目录：** `listener/`

```java
actor.addListener(new ClickListener() {
    @Override
    public void clicked(InputEvent event, float x, float y) {
        // 点击事件
    }
    
    @Override
    public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
        // 鼠标进入
    }
    
    @Override
    public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
        // 鼠标离开
    }
});
```

---

## 数据处理

### JSON - JSON 数据

**文件目录：** `json/`

```java
// 读取 JSON
FileHandle fileHandle = Gdx.files.internal("data/config.json");
String json = fileHandle.readString();
JsonValue root = new JsonReader().parse(json);

// 访问 JSON 数据
String name = root.getString("name");
int value = root.getInt("value");
JsonValue items = root.get("items");
```

---

### CSV - CSV 数据

**文件目录：** `csv/`

```java
// 读取 CSV
FileHandle fileHandle = Gdx.files.internal("data/data.csv");
String csv = fileHandle.readString();
String[] lines = csv.split("\n");
for (String line : lines) {
    String[] fields = line.split(",");
    // 处理数据
}
```

---

### File - 文件操作

**文件目录：** `file/`

```java
// 写入文件
FileHandle file = Gdx.files.local("save/game.dat");
file.writeString("Save Data", false);

// 读取文件
String data = file.readString();

// 检查文件是否存在
if (file.exists()) {
    // 文件存在
}

// 删除文件
file.delete();
```

---

### Ecode - 加密解密

**文件目录：** `ecode/`

```java
// 简单异或加密
byte[] data = "Hello".getBytes();
byte[] encrypted = encryptXor(data, 7);

// 解密
byte[] decrypted = decryptXor(encrypted, 7);
```

---

## 高级特性

### 3D 系统

**文件目录：** `lib3d/`、`model/`、`modelnew/`

- 3D 模型加载
- 3D 摄像机
- 3D 网格渲染

---

### Path - 路径和动画

**文件目录：** `path/`、`npath/`

```java
// 贝塞尔曲线路径
Path path = new Path();
path.add(new Path.PathPoint(0, 0));
path.add(new Path.PathPoint(100, 50));
path.add(new Path.PathPoint(200, 0));

// 沿路径移动
actor.addAction(
    Actions.moveTo(
        path.getPoint(0.5f).x,
        path.getPoint(0.5f).y,
        1f
    )
);
```

---

### Mesh - 网格和顶点

**文件目录：** `mesh/`、`vertices/`

```java
Mesh mesh = new Mesh(
    true,
    4,
    6,
    new VertexAttribute(VertexAttributes.Usage.Position, 3, "a_position"),
    new VertexAttribute(VertexAttributes.Usage.ColorPacked, 4, "a_color")
);

mesh.setVertices(new float[]{
    0, 0, 0,    1, 0, 0, 1, 1,  // 位置 + 颜色
    100, 0, 0,  0, 1, 0, 1, 1,
    100, 100, 0, 0, 0, 1, 1, 1,
    0, 100, 0,   1, 1, 0, 1, 1
});
```

---

### 其他高级功能

- **Polygon**（`poly/`）- 多边形处理
- **Ray**（`ray/`）- 射线检测
- **Clip**（`clip/`）- 裁剪效果
- **Stencil**（`stencil/`）- 模板测试
- **Scissor**（`scissortest/`）- 剪刀测试
- **Zip**（`zip/`）- 压缩处理
- **Network**（`net/`）- 网络通信

---

## 快速参考

### 常用测试类列表

| 类名 | 用途 | 文件位置 |
|------|------|--------|
| LibGdxTestMain | 基础测试框架 | `base/` |
| SpineTest | Spine 动画 | `spine/` |
| ActorSpine | Spine+UI | `spine/` |
| ShaderDemo | 着色器 | `shader/` |
| CameraDemo | 摄像机 | 根目录 |
| ClickGame | 点击游戏 | `click/` |
| AppC | 图片显示 | 根目录 |

### 启动测试的标准模式

```java
public class MyTest extends LibGdxTestMain {
    public static void main(String[] args) {
        MyTest test = new MyTest();
        test.start(test);
    }

    @Override
    public void useShow(Stage stage) {
        super.useShow(stage);
        // 添加你的测试代码
    }
}
```

---

## 提示和技巧

1. **快速开发：** 继承 `LibGdxTestMain` 快速创建测试
2. **资源管理：** 使用 `Asset.getAsset()` 获取资源
3. **舞台布局：** 使用 `stage.addActor()` 添加演员
4. **调试：** 使用 `Gdx.app.log()` 打印调试信息
5. **性能：** 在 render 前检查 `Gdx.graphics.getDeltaTime()`

---

**最后更新**: 2026-04-23
**总案例数**: 100+
**涵盖领域**: UI、动画、特效、游戏逻辑、输入、数据处理、3D
