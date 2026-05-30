# LibGdxLib 完整功能指南

LibGdxLib 是一个基于 LibGDX 1.13.1 的功能强大的游戏开发工具库，提供了从基础框架到高级效果的完整解决方案。

---

## 目录

1. [核心框架](#核心框架)
2. [UI 组件系统](#ui-组件系统)
3. [动画系统](#动画系统)
4. [渲染和效果](#渲染和效果)
5. [资源管理](#资源管理)
6. [输入处理](#输入处理)
7. [工具和工具函数](#工具和工具函数)
8. [实战案例](#实战案例)

---

## 核心框架

### BaseGame

`BaseGame` 是游戏的基础类，继承自 LibGDX 的 `Game` 类，提供了完整的生命周期管理、视口配置和屏幕转换功能。

**主要功能：**
- 游戏生命周期管理（创建、渲染、调整大小、销毁）
- 多种视口类型支持
- 批量渲染器管理
- ANR（Application Not Responding）监测
- 崩溃日志收集
- 屏幕转场管理

**关键配置常数：**
```java
// 视口类型
Constant.EXTENDVIEWPORT      // 扩展视口（推荐用于响应式布局）
Constant.FITVIEWPORT         // 适配视口
Constant.STRETCHVIEWPORT     // 拉伸视口
Constant.FILLVIEWPORT        // 填充视口
Constant.SCALINGVIEWPORTX    // X方向缩放
Constant.SCALINGVIEWPORTY    // Y方向缩放
Constant.SCREENVIEWPORT      // 屏幕视口

// 批量渲染器类型
Constant.SPRITEBATCH         // 标准 SpriteBatch
Constant.COUPOLYGONBATCH     // CPU 多边形批处理（性能更好）
```

**基础使用案例：**

```java
// 1. 创建游戏主类
@GameInfo(width = 1080, height = 1920, fps = 60)
@ANRDEMO(delaytime = 5000) // ANR 监测延迟时间（ms）
public class MyGame extends BaseGame {
    
    @Override
    protected void initExtends() {
        super.initExtends();
        // 初始化游戏资源
    }
    
    @Override
    protected void loadingView() {
        // 加载游戏首屏或加载界面
        setScreen(new LoadingScreen(this));
    }
    
    @Override
    protected void preDiapose() {
        // 清理游戏资源
    }
}

// 2. 创建游戏屏幕
public class GameScreen extends BaseScreen {
    private Stage stage;
    
    public GameScreen(BaseGame game) {
        super(game);
    }
    
    @Override
    public void show() {
        // 屏幕显示时调用
        stage = new Stage(game.getStageViewport(), game.getBatch());
        Gdx.input.setInputProcessor(stage);
    }
    
    @Override
    public void render(float delta) {
        // 清屏
        Gdx.gl.glClearColor(0.1f, 0.1f, 0.1f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        
        // 更新和渲染舞台
        stage.act(delta);
        stage.draw();
    }
    
    @Override
    public void hide() {
        // 屏幕隐藏时调用
    }
    
    @Override
    public void dispose() {
        stage.dispose();
    }
}

// 3. 在 Android 中启动游戏
public class AndroidLauncher extends AndroidApplication {
    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
        initialize(new MyGame(), config);
    }
}
```

**获取关键对象：**

```java
// 获取视口（用于响应式布局和摄像机控制）
Viewport viewport = game.getStageViewport();

// 获取批量渲染器
Batch batch = game.getBatch();

// 屏幕切换
game.setScreen(GameScreen.class);
game.setScreen(new GameScreen(game), true); // true 表示使用转场效果
```

---

## UI 组件系统

### Actor（演员/组件）

Actor 系统提供了灵活的场景图和事件处理。

**核心类：**
- `PolygonClipGroup` - 多边形剪切组，用于创建非矩形裁剪区域
- `ShaderGroup` - Shader 着色器组
- `ShaperRenerInteface` - 形状渲染接口

**PolygonClipGroup 使用案例：**

```java
// 创建多边形裁剪区域（如圆形头像）
PolygonClipGroup clipGroup = new PolygonClipGroup();

// 添加多边形顶点（定义裁剪区域形状）
float[] vertices = {
    100, 100,  // 顶点1
    200, 100,  // 顶点2
    150, 200   // 顶点3（形成三角形）
};
clipGroup.setClipRegion(vertices);

// 在裁剪区域内添加演员
Image head = new Image(headTexture);
clipGroup.addActor(head);

stage.addActor(clipGroup);
```

**Button（按钮）**

```java
// 创建按钮
TextButton button = new TextButton("开始游戏", skin);
button.setPosition(100, 100);
button.setSize(200, 50);

// 添加点击监听
button.addListener(new ClickListener() {
    @Override
    public void clicked(InputEvent event, float x, float y) {
        game.setScreen(GameScreen.class);
    }
});

stage.addActor(button);
```

---

## 动画系统

### Spine 骨骼动画

Spine 是一个强大的 2D 骨骼动画工具。LibGdxLib 提供了多种 Spine 相关功能。

**核心类：**
- `SpineAnimation` - Spine 动画标记
- `SpineReadAttribute` - Spine 属性读取器
- `DmnActions` - Spine 动作集合
- `MoveTimeLine` - 移动时间线
- `NewRGBAction` - RGB 颜色动作
- `NewRotationAction` - 旋转动作
- `ScaleTemporalAction` - 缩放动作

**Spine 动画基础使用案例：**

```java
// 1. 加载 Spine 资源
SpineActor spineActor = new SpineActor("assets/actorspine/hero");

// 2. 设置初始动画
spineActor.setAnimation("idle", true); // true 表示循环播放

// 3. 设置位置和大小
spineActor.setPosition(500, 400, Align.center);
spineActor.setScale(1.5f);

stage.addActor(spineActor);

// 4. 播放特定动画
spineActor.setAnimation("attack", false); // false 表示播放一次
```

**Spine 动画中插入自定义对象（如 UI 元素）：**

```java
// 根据 README 中的示例
SpineActor actor = new SpineActor("assets/actorspine/quan_tb");
actor.setAnimation("zhuanpan_ck", true);
actor.setPosition(450, 400, Align.center);
stage.addActor(actor);

// 创建要插入的 UI 组件
Group group = new Group();

// 添加图片
Image img = new Image(Asset.getAsset().getTexture("assets/7.png"));
group.addActor(img);
img.setPosition(0, 0, Align.center);

// 添加进度条
Image progressBar = new Image(Asset.getAsset().getTexture("assets/ad_progress.png"));
group.addActor(progressBar);
progressBar.setPosition(0, 0, Align.center);

// 创建自定义附件
ActorAttachment actorAttachment = new ActorAttachment("img");
actorAttachment.setActor(group);

// 获取骨骼数据并替换指定的附件
SkeletonData data = actor.getSkeleton().getData();
Skin defaultSkin = data.getDefaultSkin();
for (Skin.SkinEntry attachment : defaultSkin.getAttachments()) {
    if (attachment.getName().equals("xuanq2_00")) {
        attachment.setAttachment(actorAttachment);
    }
}
```

**Spine 动作和变换：**

```java
// 旋转动作
NewRotationAction rotateAction = new NewRotationAction(90, 2f);
spineActor.addAction(rotateAction);

// RGB 颜色动作（变红）
NewRGBAction colorAction = new NewRGBAction(
    Color.RED, 
    1f, // 持续时间
    Interpolation.linear
);
spineActor.addAction(colorAction);

// 缩放动作
ScaleTemporalAction scaleAction = new ScaleTemporalAction(2f, 1f);
spineActor.addAction(scaleAction);

// 序列动作（依次执行）
spineActor.addAction(
    Actions.sequence(
        Actions.moveTo(100, 100, 1f),
        rotateAction,
        colorAction
    )
);
```

---

## 渲染和效果

### 帧缓冲区（FrameBuffer）

用于离屏渲染、创建阴影、发光等高级效果。

```java
// 创建帧缓冲区
FrameBuffer frameBuffer = new FrameBuffer(
    Pixmap.Format.RGBA8888, 
    800, 
    600, 
    false
);

// 渲染到帧缓冲区
frameBuffer.begin();
Gdx.gl.glClearColor(0, 0, 0, 0);
Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

// 在这里进行渲染操作
batch.begin();
// 绘制内容
batch.end();

frameBuffer.end();

// 使用渲染结果
Texture texture = frameBuffer.getColorBufferTexture();
batch.draw(texture, 0, 0);
```

### Shader（着色器）

```java
// 创建着色器组
ShaderGroup shaderGroup = new ShaderGroup();

// 加载自定义 Shader 程序
String vertexShader = Gdx.files.internal("shaders/vertex.glsl").readString();
String fragmentShader = Gdx.files.internal("shaders/fragment.glsl").readString();
ShaderProgram program = new ShaderProgram(vertexShader, fragmentShader);

// 应用到组
shaderGroup.setShaderProgram(program);
stage.addActor(shaderGroup);
```

### 粒子效果（Particle）

```java
// 创建粒子发射器
ParticleEffect particleEffect = new ParticleEffect();
particleEffect.load(Gdx.files.internal("effects/explosion.p"), 
                    Gdx.files.internal("effects/"));
particleEffect.setPosition(500, 500);

// 在渲染时更新粒子
batch.begin();
particleEffect.draw(batch, delta);
batch.end();
```

---

## 资源管理

### Asset（资源加载器）

Asset 系统提供了一个单例的资源管理器。

```java
// 获取资源管理器
Asset asset = Asset.getAsset();

// 加载贴图
Texture texture = asset.getTexture("assets/player.png");

// 加载字体
BitmapFont font = asset.getFont("assets/fonts/default.fnt");

// 加载音效
Sound sound = Gdx.audio.newSound(Gdx.files.internal("audio/jump.wav"));

// 加载背景音乐
Music music = Gdx.audio.newMusic(Gdx.files.internal("audio/background.mp3"));
music.play();
```

### 资源卸载

```java
// 卸载单个资源
asset.unload("assets/player.png");

// 清空所有资源
asset.dispose();
```

---

## 输入处理

### Input（输入系统）

```java
// 键盘输入检测
if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
    // 空格键被按下
    jump();
}

if (Gdx.input.isKeyPressed(Input.Keys.A)) {
    // A 键被持续按住
    moveLeft();
}

// 触屏输入检测
if (Gdx.input.isTouched()) {
    float touchX = Gdx.input.getX();
    float touchY = Gdx.input.getY();
    // 处理触屏输入
}

// 通过舞台处理 UI 输入
Gdx.input.setInputProcessor(stage);
```

### 高级输入处理

```java
// 通过演员监听输入
actor.addListener(new ClickListener() {
    @Override
    public void clicked(InputEvent event, float x, float y) {
        Gdx.app.log("Tag", "Actor clicked!");
    }
});

// 监听长按
actor.addListener(new InputListener() {
    @Override
    public boolean touchDown(InputEvent event, float x, float y, 
                            int pointer, int button) {
        holdTime = 0;
        return true;
    }
    
    @Override
    public void touchUp(InputEvent event, float x, float y, 
                       int pointer, int button) {
        if (holdTime > 1f) {
            // 长按超过1秒
            longPress();
        }
    }
});
```

---

## 工具和工具函数

### 日期和时间（Date）

```java
import com.kw.gdx.date.*;

// 获取当前时间戳
long currentTime = System.currentTimeMillis();

// 时间格式化
SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
String formattedTime = sdf.format(new Date(currentTime));
```

### 文件操作（File）

```java
import com.kw.gdx.file.*;

// 读取文件
String content = readFile("save/data.json");

// 写入文件
writeFile("save/data.json", jsonContent);

// 检查文件是否存在
if (fileExists("save/data.json")) {
    // 文件存在
}
```

### 加密和编码（MD5、Ecode）

```java
import com.kw.gdx.md5.*;
import com.kw.gdx.ecode.*;

// MD5 加密
String hash = MD5.md5("password");

// 图片加密（基于异或）
// 简单异或加密
byte[] encrypted = xorEncrypt(data, 7); // 异或值为 7

// 复杂加密（ESA）
byte[] encrypted = esaEncrypt(data, startPos, length);
```

### 压缩文件（ZIP）

```java
import com.kw.gdx.zip.*;

// 压缩文件
zipFile("source.txt", "archive.zip");

// 解压文件
unzipFile("archive.zip", "output_folder/");
```

### 反射工具（Refleat）

```java
import com.kw.gdx.refleat.*;

// 通过反射创建实例
Object instance = ReflectionUtils.createInstance(MyClass.class);

// 通过反射调用方法
ReflectionUtils.invokeMethod(instance, "methodName", args);

// 通过反射获取字段值
Object value = ReflectionUtils.getField(instance, "fieldName");
```

### 颜色处理（Color）

```java
import com.kw.gdx.color.*;

// 创建颜色
Color red = new Color(1, 0, 0, 1);
Color blue = Color.BLUE;

// 颜色插值
Color interpolated = new Color();
interpolated.lerp(Color.RED, Color.BLUE, 0.5f);

// 颜色渐变
actor.addAction(
    Actions.color(Color.RED, 2f, Interpolation.linear)
);
```

### 常量管理（Constant）

```java
import com.kw.gdx.constant.*;

// 游戏配置
Constant.WIDTH = 1080;           // 游戏宽度
Constant.HIGHT = 1920;           // 游戏高度
Constant.viewColor = Color.BLACK; // 背景颜色
Constant.fps = 60;               // 帧率

// 视口配置
Constant.viewportType = Constant.EXTENDVIEWPORT;

// 批量渲染器配置
Constant.batchType = Constant.COUPOLYGONBATCH;

// 调试开关
Constant.SHOWFRAMESPERSECOND = true;  // 显示 FPS
Constant.SHOWRENDERCALL = true;       // 显示渲染调用次数
Constant.crashlog = true;             // 启用崩溃日志
```

---

## 实战案例

### 案例 1：简单的点击游戏

```java
// 游戏主类
@GameInfo(width = 1080, height = 1920, fps = 60)
public class ClickGame extends BaseGame {
    
    @Override
    protected void loadingView() {
        setScreen(new GamePlayScreen(this));
    }
}

// 游戏屏幕
public class GamePlayScreen extends BaseScreen {
    private Stage stage;
    private int score = 0;
    private Label scoreLabel;
    private Actor clickTarget;
    
    public GamePlayScreen(BaseGame game) {
        super(game);
    }
    
    @Override
    public void show() {
        stage = new Stage(game.getStageViewport(), game.getBatch());
        Gdx.input.setInputProcessor(stage);
        
        // 创建可点击的目标
        Image targetImage = new Image(
            Asset.getAsset().getTexture("assets/target.png")
        );
        targetImage.setSize(100, 100);
        targetImage.setPosition(500, 900, Align.center);
        targetImage.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                score += 10;
                scoreLabel.setText("Score: " + score);
                randomizeTargetPosition();
            }
        });
        stage.addActor(targetImage);
        clickTarget = targetImage;
        
        // 创建分数标签
        Skin skin = new Skin(Gdx.files.internal("ui/uiskin.json"));
        scoreLabel = new Label("Score: 0", skin);
        scoreLabel.setPosition(100, Constant.HIGHT - 100);
        stage.addActor(scoreLabel);
    }
    
    private void randomizeTargetPosition() {
        clickTarget.setPosition(
            MathUtils.random(100, Constant.WIDTH - 100),
            MathUtils.random(100, Constant.HIGHT - 100),
            Align.center
        );
    }
    
    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.2f, 0.2f, 0.3f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        
        stage.act(delta);
        stage.draw();
    }
    
    @Override
    public void dispose() {
        stage.dispose();
    }
}
```

### 案例 2：带 Spine 动画的角色游戏

```java
public class CharacterGameScreen extends BaseScreen {
    private Stage stage;
    private SpineActor hero;
    private Array<String> animations = new Array<>();
    
    public CharacterGameScreen(BaseGame game) {
        super(game);
    }
    
    @Override
    public void show() {
        stage = new Stage(game.getStageViewport(), game.getBatch());
        Gdx.input.setInputProcessor(stage);
        
        // 加载 Spine 角色
        hero = new SpineActor("assets/characters/warrior");
        hero.setPosition(Constant.WIDTH / 2, Constant.HIGHT / 2, Align.center);
        hero.setScale(2f);
        hero.setAnimation("idle", true);
        
        // 动画列表
        animations.addAll("idle", "run", "attack", "die");
        
        stage.addActor(hero);
        
        // 创建按钮控制动画
        createAnimationButtons();
    }
    
    private void createAnimationButtons() {
        Skin skin = new Skin(Gdx.files.internal("ui/uiskin.json"));
        float buttonWidth = 100;
        float buttonHeight = 50;
        float spacing = 10;
        
        for (int i = 0; i < animations.size; i++) {
            String animName = animations.get(i);
            TextButton btn = new TextButton(animName, skin);
            btn.setPosition(50 + i * (buttonWidth + spacing), 50);
            btn.setSize(buttonWidth, buttonHeight);
            
            final String anim = animName;
            btn.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    hero.setAnimation(anim, !anim.equals("attack"));
                }
            });
            
            stage.addActor(btn);
        }
    }
    
    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.1f, 0.1f, 0.1f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        
        // 键盘控制
        handleInput();
        
        stage.act(delta);
        stage.draw();
    }
    
    private void handleInput() {
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            hero.moveBy(-5, 0);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            hero.moveBy(5, 0);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
            hero.setAnimation("attack", false);
        }
    }
    
    @Override
    public void dispose() {
        stage.dispose();
    }
}
```

### 案例 3：粒子效果和音效

```java
public class EffectsScreen extends BaseScreen {
    private Stage stage;
    private ParticleEffect explosion;
    private Sound jumpSound;
    private Music backgroundMusic;
    
    public EffectsScreen(BaseGame game) {
        super(game);
    }
    
    @Override
    public void show() {
        stage = new Stage(game.getStageViewport(), game.getBatch());
        Gdx.input.setInputProcessor(stage);
        
        // 加载粒子效果
        explosion = new ParticleEffect();
        explosion.load(
            Gdx.files.internal("effects/explosion.p"),
            Gdx.files.internal("effects/")
        );
        
        // 加载音效
        jumpSound = Gdx.audio.newSound(Gdx.files.internal("audio/jump.wav"));
        backgroundMusic = Gdx.audio.newMusic(
            Gdx.files.internal("audio/background.mp3")
        );
        backgroundMusic.setLooping(true);
        backgroundMusic.play();
        
        // 创建点击区域以触发效果
        Image clickArea = new Image();
        clickArea.setSize(Constant.WIDTH, Constant.HIGHT);
        clickArea.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                playExplosion(x, y);
                jumpSound.play();
            }
        });
        stage.addActor(clickArea);
    }
    
    private void playExplosion(float x, float y) {
        explosion.setPosition(x, y);
        explosion.reset();
    }
    
    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        
        stage.act(delta);
        
        Batch batch = game.getBatch();
        batch.begin();
        explosion.draw(batch, delta);
        batch.end();
        
        stage.draw();
    }
    
    @Override
    public void dispose() {
        stage.dispose();
        jumpSound.dispose();
        backgroundMusic.dispose();
        explosion.dispose();
    }
}
```

---

## 常见问题和最佳实践

### Q1: 如何处理屏幕旋转？

```java
@Override
public void resize(int width, int height) {
    // BaseGame 会自动调用此方法
    // 视口会自动调整
    super.resize(width, height);
}
```

### Q2: 如何实现 UI 适配不同分辨率？

```java
// 使用 ExtendViewport 或 FitViewport
Constant.viewportType = Constant.EXTENDVIEWPORT;

// 所有 UI 位置都应该基于虚拟坐标而非像素坐标
actor.setPosition(Constant.WIDTH / 2, Constant.HIGHT / 2);
```

### Q3: 性能优化建议

- 使用 `CpuPolygonSpriteBatch` 而不是 `SpriteBatch`（性能更好）
- 合理使用帧缓冲区和离屏渲染
- 及时释放不需要的资源
- 使用对象池减少垃圾回收
- 避免在 render 循环中创建新对象

### Q4: 如何调试游戏？

```java
// 启用调试信息
Constant.SHOWFRAMESPERSECOND = true;  // 显示 FPS
Constant.SHOWRENDERCALL = true;       // 显示渲染调用
Constant.crashlog = true;             // 启用崩溃日志

// 打印日志
NLog.i("Message with %s", "parameter");
NLog.e("Error message");
```

---

## 相关资源

- [LibGDX 官方文档](https://libgdx.com/)
- [Spine 动画工具](http://esotericsoftware.com/)
- [LibgdxTool GitHub](https://github.com/wangGame/LibGdxTool)
- [LibgdxTool 3D 支持](https://github.com/wangGame/LibgdxTool3D)

---

**最后更新**: 2026-04-23
**版本**: LibGDX 1.13.1
