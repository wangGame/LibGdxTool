# 添加忽略

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

## 动态加载关卡

根据使用需要加载关卡

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

