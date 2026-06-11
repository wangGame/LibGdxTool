# 虚拟键盘高度变化处理 - 详细说明文档

## 📖 概述

`onKeyboardHeightChanged()` 是一个**键盘高度变化回调方法**，在Android虚拟键盘（软键盘）的高度发生改变时被调用。

这个方法主要用于：
1. 监听虚拟键盘的显示和隐藏事件
2. 动态调整UI布局以避免被键盘遮挡
3. 适配不同形状的屏幕（刘海屏、药丸屏等）
4. 处理沉浸模式（Immersive Mode）下的特殊情况

---

## 🎯 方法签名

```java
@Override
public void onKeyboardHeightChanged(int height, int leftInset, int rightInset, int orientation)
```

### 参数说明

| 参数 | 类型 | 说明 |
|-----|------|------|
| `height` | `int` | 虚拟键盘的高度（像素）。0=键盘已关闭（隐藏） |
| `leftInset` | `int` | 屏幕左边距离（用于刘海屏左侧缺口） |
| `rightInset` | `int` | 屏幕右边距离（用于刘海屏右侧缺口） |
| `orientation` | `int` | 屏幕方向（0=竖屏，1=横屏） |

---

## 🔍 代码详解

### 1. 键盘关闭处理 (height == 0)

```java
if (height == 0) {
    // 不要在浮动键盘上关闭输入框
    if (!isStandardHeightProvider && (...)) {
        closeTextInputField(false);
    }
    
    // 重置输入框位置到原位
    relativeLayoutField.setY(0);
    return;
}
```

**作用**：当键盘关闭时，将输入框恢复到原来的位置。

**浮动键盘处理**：浮动键盘（如三星的浮动键盘）需要特殊对待，不能直接关闭输入框。

---

### 2. Immersive模式处理

```java
if (config.useImmersiveMode && isStandardHeightProvider) {
    height += getSoftButtonsBarHeight();
}
```

**作用**：在全屏沉浸模式下，加上系统软按钮栏的高度。

**场景**：游戏应用通常使用Immersive模式隐藏系统栏，此时需要加上系统按钮栏的高度以准确计算键盘覆盖的实际空间。

---

### 3. 键盘打开动画处理

```java
relativeLayoutField.animate()
    .y(-height)                                    // 向上移动
    .scaleX((float)...-rightInset-leftInset)/...)  // 缩放宽度
    .x((float)(leftInset-rightInset)/2)            // 水平偏移
    .setDuration(100)                              // 100ms动画
    .setListener(...)                              // 监听动画事件
    .start();
```

**动画步骤**：
1. **Y轴移动**：`setY(-height)` 把输入框向上移动键盘高度的距离
2. **X轴缩放**：根据左右insets调整输入框宽度
3. **X轴平移**：计算居中位置

**例子**：
```
原始位置：    输入框在屏幕下方
             ┌─────────────────┐
             │                 │
             │   输入框位置    │
             └─────────────────┘

键盘打开后：  输入框上移了键盘高度
             ┌─────────────────┐
             │   输入框位置    │ ← 上移
             ├─────────────────┤
             │   虚拟键盘      │ ← 高度=400px
             └─────────────────┘
```

---

### 4. 特殊屏幕处理（刘海屏）

```
标准屏幕：
┌─────────────────┐
│  输入框         │
├─────────────────┤
│  虚拟键盘       │
└─────────────────┘

刘海屏（左右缺口）：
┌──┐───────────┐──┐
│LS│ 输入框    │RS│  LS=左缺口  RS=右缺口
├──┼───────────┼──┤
│  │ 虚拟键盘  │  │
└──┴───────────┴──┘

缩放和偏移计算：
scaleX = (屏幕宽度 - 右缺口 - 左缺口) / 屏幕宽度
offsetX = (左缺口 - 右缺口) / 2
```

---

## 📱 使用场景

### 场景1：简单的输入框应用
```
需求：防止输入框被键盘遮挡

实现流程：
1. 监听键盘高度变化事件
2. 当键盘打开时（height > 0）
   → 计算输入框应该移动的距离
   → 使用动画平滑移动
3. 当键盘关闭时（height == 0）
   → 将输入框恢复到原位
```

### 场景2：游戏应用
```
需求：在Immersive模式下正确处理键盘

实现流程：
1. 检查是否启用了Immersive模式
2. 如果启用，加上软按钮栏的高度
3. 通知游戏逻辑键盘已打开
4. 暂停游戏或调整UI
```

### 场景3：刘海屏设备
```
需求：适配刘海屏等特殊屏幕

实现流程：
1. 接收leftInset和rightInset参数
2. 根据insets调整输入框的宽度和位置
3. 确保内容不会被屏幕缺口遮挡
```

---

## 🔧 完整实现步骤

### Step 1: 创建键盘观察者
```java
class KeyboardHeightObserver implements KeyboardHeightObserver {
    @Override
    public void onKeyboardHeightChanged(int height, int leftInset, int rightInset, int orientation) {
        // 处理键盘高度变化
    }
}
```

### Step 2: 注册键盘提供者
```java
KeyboardHeightProvider provider = new StandardKeyboardHeightProvider(activity);
provider.addObserver(observer);
```

### Step 3: 在Activity中处理
```java
@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    
    // 获取输入框容器
    RelativeLayout inputContainer = findViewById(R.id.input_container);
    
    // 按照演示代码处理键盘事件
}
```

---

## 🎨 动画效果详解

### 动画属性

| 属性 | 作用 | 示例值 |
|-----|------|--------|
| `y()` | 垂直位置 | `-400` (向上移动400px) |
| `scaleX()` | 水平缩放 | `0.9` (缩放到90%) |
| `x()` | 水平位置 | `25` (向右偏移25px) |
| `setDuration()` | 动画时长 | `100` (100毫秒) |

### 动画监听器

```java
.setListener(new Animator.AnimatorListener() {
    @Override
    public void onAnimationStart(Animator animation) {
        // 动画开始时调用
    }
    
    @Override
    public void onAnimationEnd(Animator animation) {
        // 动画结束时调用
        // 常用于刷新自动补全菜单位置
        if (editText.isPopupShowing()) {
            editText.showDropDown();
        }
    }
    
    @Override
    public void onAnimationCancel(Animator animation) {
        // 动画被取消时调用
    }
    
    @Override
    public void onAnimationRepeat(Animator animation) {
        // 动画重复时调用
    }
})
```

---

## 🐛 常见问题

### Q1: 动画后输入框位置错误？
**A**: 在动画前重置位置和缩放：
```java
relativeLayoutField.setX(0);
relativeLayoutField.setScaleX(1);
relativeLayoutField.setY(0);
```

### Q2: 如何检查键盘是否真的打开了？
**A**: 检查height是否大于0：
```java
boolean isKeyboardOpen = (height > 0);
```

### Q3: 自动补全菜单被键盘遮挡？
**A**: 在动画结束后更新菜单位置：
```java
if (editText.isPopupShowing()) {
    editText.showDropDown();
}
```

### Q4: Immersive模式下键盘高度不对？
**A**: 加上软按钮栏的高度：
```java
if (config.useImmersiveMode) {
    height += getSoftButtonsBarHeight();
}
```

---

## 📊 测试数据

### 典型键盘高度（设备相关）

| 设备类型 | 竖屏高度 | 横屏高度 |
|---------|--------|--------|
| 手机 | 400-500px | 200-300px |
| 平板 | 500-600px | 400-500px |
| 浮动键盘 | 200-400px | 200-400px |

### Insets参考值（刘海屏）

| 屏幕类型 | 左缺口 | 右缺口 |
|---------|--------|--------|
| 无缺口 | 0 | 0 |
| 左侧刘海 | 50-100 | 0 |
| 右侧刘海 | 0 | 50-100 |
| 两侧刘海 | 50-100 | 50-100 |

---

## 🚀 最佳实践

✅ **该做**：
- ✓ 在动画前重置位置
- ✓ 考虑不同的屏幕insets
- ✓ 在Immersive模式下加上软按钮栏高度
- ✓ 动画结束后刷新自动补全菜单
- ✓ 监听键盘类型（标准vs浮动）

❌ **不该做**：
- ✗ 直接设置绝对Y坐标（使用相对动画）
- ✗ 忽视insets（会导致刘海屏适配问题）
- ✗ 在非Immersive模式下加软按钮栏高度
- ✗ 使用同步方法阻塞UI线程
- ✗ 在动画期间修改容器属性

---

## 📚 相关类和接口

| 类/接口 | 说明 |
|---------|------|
| `KeyboardHeightObserver` | 键盘高度观察者接口 |
| `KeyboardHeightProvider` | 键盘高度提供者 |
| `StandardKeyboardHeightProvider` | 标准键盘提供者 |
| `AndroidXKeyboardHeightProvider` | AndroidX版键盘提供者 |
| `Animator` | 属性动画类 |

---

## 🔗 相关链接

- [Android官方文档 - 输入法](https://developer.android.com/guide/topics/text/creating-input-method)
- [Android动画概览](https://developer.android.com/guide/topics/graphics/prop-animation)
- [刘海屏适配指南](https://developer.android.com/training/display-cutout)
- [Immersive模式](https://developer.android.com/training/system-ui/immersive)

---

## 📝 总结

`onKeyboardHeightChanged()` 方法是Android应用中处理虚拟键盘的核心回调。通过正确实现这个方法，可以：

1. **提升用户体验** - 防止内容被键盘遮挡
2. **支持特殊屏幕** - 适配刘海屏、药丸屏
3. **优化游戏应用** - 处理Immersive模式
4. **平滑动画效果** - 创建专业级的UI过渡

关键要点：
- 监听height参数的变化
- 根据height值调整UI位置
- 考虑insets进行屏幕适配
- 使用动画创建平滑效果
- 测试不同的设备和屏幕形状

