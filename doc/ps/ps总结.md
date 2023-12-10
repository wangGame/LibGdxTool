# ps

ps包含颜色空间、滤镜效果、图层混合、基本运算、图像调整

## 颜色空间

### RGB

### 黑白、灰度

### HSV HSL HSI

RGB to HSV

RGB值分为255份将255变为0-1之间。

```java
R` = R / 255.f;
G` = G / 255.f;
B` = B / 255.f;

Cmax = max(R`,G`,B`)
Cmin = min(R`,G`,B`)

range = Cmax - Cmin;

if(range == 0){
    hsv[0] = 0;
}else if(Cmax == r){
    hsv[0] = (60*(g-b)/range + 360) % 360;
}else if(Cmax == g){
    hsv[0] = 60*(b-r)/range + 360;
}else{
    hsv[0] = 60 * (r - g) / range + 240;    
}

if (Cmax > 0){
    hsv[1] = 1 - min/max;
}else{
    hsv[1] = 0;    
}

hsv[2] = max;
```


RGB to HSL

```java
R` = R / 255.f;
G` = G / 255.f;
B` = B / 255.f;

Cmax = max(R`,G`,B`)
Cmin = min(R`,G`,B`)

range = Cmax - Cmin;

if(range == 0){
    hsl[0] = 0;
}else if(Cmax == r){
    hsl[0] = (60*(g-b)/range + 360) % 360;
}else if(Cmax == g){
    hsl[0] = 60*(b-r)/range + 360;
}else{
    hsl[0] = 60 * (r - g) / range + 240;    
}
L = (Cmax + Cmin) / 2.f;
if (range != 0){
    hsl[1] = range / (1-Math.abs(2*L - 1));
}else{
    hsl[1] = 0;    
}

hsl[2] = L;
```

RGB to HSI
```java
I=(R+G+B)/3;
S=1-3*Math.min(R,G,B)/(R+G+B);
H = cos^(-1)((0.5*((R-G)+(R-B))) / ((R-G)^2 + (R-B)*(G-B))^(0.5))
if(S = 0)  H =0 ;
If(B > G)  H=360-H;
```


### Lab



### CIE_XYZ

### YUV

## 图像滤镜效果

### 渐变、百叶窗、镜像渐隐

### 高反差保留

### 染色玻璃

### 浮雕效果

### 照亮边缘

### 马赛克

### 曝光过度

### 素描效果

### 运动模糊、径向模糊、旋转迷糊

### 极坐标变换

### 球面化、鱼眼特效

### 水波、波纹特效

### 镜头光晕

### 万花筒

### perlin噪声

## 图层混合

### 不透明

### 正片叠底

### 颜色加深、颜色减淡

### 线性加深、线性减淡

### 滤色

### 叠加

### 柔光、强光

### 亮光、点光、线性光

### 实色混合

### 饱和度、色相、颜色、亮度

### 差值

### 溶解

### 排除

## 基本运算

### 值变化

- 亮度调整
- 颜色调整
- 局部滤波运算

### 坐标变换

- 平移
- 旋转
- 尺寸变化

## 图像调整

### 自动色阶

### 亮度调整

### 对比度调整

### 饱和度调整

### 色调

- 老照片
- 电影色调
- 日系色调
- 黑白化
- 颜色梯度
- 颜色映射
- 通道混合

## 基本运算


