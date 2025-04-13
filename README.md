# LibGdxTool

[![Build Status](https://img.shields.io/github/actions/workflow/status/wangGame/LibGdxTool/gradle.yml?branch=master)](https://github.com/wangGame/LibGdxTool/actions)


[![](https://jitpack.io/v/wangGame/LibGdxTool.svg)](https://jitpack.io/#wangGame/LibGdxTool)

## 1.13.1

已经更新1.13.1版本，切换分支

## 添加忽略

```
.gradle/
.idea/
android/build/
core/build/
desktop/build/
libGdxLib/build/
libGdxLib/libGdxLib.iml
local.properties
desktop/desktop.iml
core/core.iml
```

git rm -r -f --cached .

git add .

git commit -m "xx"

## 3d部分已经拆出去了

[3D部分](https://github.com/wangGame/LibgdxTool3D)

## 仓库使用方法

```
maven { url 'https://jitpack.io' }
   
//如果全都要
implementation 'com.github.wangGame:LibGdxTool:pre-release1.0.0'
//只是要部分
// libGdx源码   
implementation 'com.github.wangGame.LibGdxTool:libGdx:pre-release1.0.0'
//自己的工具包
implementation 'com.github.wangGame.LibGdxTool:libGdxLib:pre-release1.0.0'
//desktop快速启动
implementation 'com.github.wangGame.LibGdxTool:desktop:pre-release1.0.0'
```

## group tranfrom

- SpriteBatch
 
```
@Override
public void setTransformMatrix (Matrix4 transform) {
    if (drawing) flush();
    transformMatrix.set(transform);
    if (drawing) setupMatrices();
}
```

- CpuPolygonSpriteBatch

```
@Override
public void setTransformMatrix (Matrix4 transform) {
    Matrix4 realMatrix = super.getTransformMatrix();

    if (checkEqual(realMatrix, transform)) {
        adjustNeeded = false;
    } else {
        if (isDrawing()) {
            virtualMatrix.setAsAffine(transform);
            adjustNeeded = true;

            // adjust = inverse(real) x virtual
            // real x adjust x vertex = virtual x vertex

            if (haveIdentityRealMatrix) {
                adjustAffine.set(transform);
            } else {
                tmpAffine.set(transform);
                adjustAffine.set(realMatrix).inv().mul(tmpAffine);
            }
        } else {
            realMatrix.setAsAffine(transform);
            haveIdentityRealMatrix = checkIdt(realMatrix);
        }
    }
}
```

原来的比较简单，直接绘制一次，变换完成在绘制一次。

## 图片解密

图片可能使用的格式png webp。
找一张正常和png webp，读取前几个字节，需要解密和正常的图片进行二进制对比，确定那几位发生变化

图片随机位（我自己知道那几位就行）变化，可以解决上面异解密的问题

## 加密比较

与加密

47 43 42 41 36 31 30 28 33 28 35

原始数据
46 41 40 40 37 31 29 28 31 28 35

ESA
788 660 646 633 633 641 633 639 633 632 653

## 加密使用异或运算

简单的异或容易被破解，所以本次使用ESA加密字段  

目前暂定加密数值
- 异或值7
- 异或范围，不全部使用异或，从图片的第20位开始异或，异或长度10

好处：
- 不会花费时间去求异或值 ，取几位让图片无法正常显示就可以了

## 弹窗使用说明

弹窗如果有点击非弹窗区域关闭的操作，建议布局刚好包括布局区域【推荐的做法】

如果点击非UI区域关闭，点击使用closeBg，如果有自己的实现，可以复写下面的方法
```java
closeBg.addListener(new ClickListener(){
    @Override
    public void clicked(InputEvent event, float x, float y) {
        super.clicked(event, x, y);
        closeFlag = true;
        if (entered){
            closeDialog();
        }
    }
});

一般的复写closeDialog既可以操作，如果弹窗不需要开始这个功能，可以子类删除closeBg，即可
```

## BaseScreen

增加了maxTopGroup和dialogGroup

dialogGroup为设计尺寸，在游戏使用弹窗时候，可以不需要设置位置【其实baseDialog已经设置了，就当没说】

maxTopGroup用来显示提示之类，需要显示在最上层的玩意。

## 版本说明

- alpha：内部版本
- beta：测试版
- demo：演示版
- enhance：增强版
- free：自由版
- full version：完整版，即正式版
- lts：长期维护版本
- release：发行版
- rc：即将作为正式版发布
- standard：标准版
- ultimate：旗舰版
- upgrade：升级版


## 适配

ExtendViewPort

写最大尺寸和最小尺寸的，可以在范围内不拉伸，大于范围会拉伸
