package com.kw.gdx;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.Vector4;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.kw.gdx.g3.ModelBatchUtils;
import com.kw.gdx.lib.NodeSpace;

import sun.java2d.loops.TransformBlit;

/**
 * 继承Actor
 */
public class ModelActor extends Actor {
    private ModelInstance modelInstance;
    /**
     * @engineInternal NOTE: this is engineInternal interface that doesn't have a side effect of updating the transforms
     */
    public Vector3 _pos = new Vector3();
    /**
     * @engineInternal NOTE: this is engineInternal interface that doesn't have a side effect of updating the transforms
     */
    public Quaternion _rot;
    /**
     * @engineInternal NOTE: this is engineInternal interface that doesn't have a side effect of updating the transforms
     */
    public Vector3 _scale;
    /**
     * @engineInternal NOTE: this is engineInternal interface that doesn't have a side effect of updating the transforms
     */
    public Matrix4 _mat;
    // local transform
//    @serializable
    protected Vector3 _lpos = new Vector3();

//    @serializable
    protected Quaternion _lrot = new Quaternion();

//    @serializable
    protected Vector3 _lscale = new Vector3(1, 1, 1);

    public ModelActor(){

    }

    public ModelActor(ModelInstance instance){
        this.modelInstance = instance;
    }

    // ===============================
    // transform helper, convenient but not the most efficient
    // ===============================

    private Vector3 v3_a = new Vector3();
    private Vector4 q_a = new Vector4();
    /**
     * @en Perform a translation on the node
     * @zh 移动节点
     * @param trans The increment on position
     * @param ns The operation coordinate space
     */
    public void translate (Vector3 trans, NodeSpace ns) {
        NodeSpace space = ns;
        if (space == NodeSpace.LOCAL) {
            Vector3.transformQuat(v3_a, trans, this._lrot);
            this._lpos.x += v3_a.x;
            this._lpos.y += v3_a.y;
            this._lpos.z += v3_a.z;
        } else if (space == NodeSpace.WORLD) {
            Group parent1 = getParent();
            if (parent1!=null) {
//                Quaternion.invert(q_a, parent1.worldRotation);
//                Vec3.transformQuat(v3_a, trans, q_a);
//                float scale = this.worldScale;
//                this._lpos.x += v3_a.x / scale.x;
//                this._lpos.y += v3_a.y / scale.y;
//                this._lpos.z += v3_a.z / scale.z;
//            } else {
//                this._lpos.x += trans.x;
//                this._lpos.y += trans.y;
//                this._lpos.z += trans.z;
//            }
        }
//        this.invalidateChildren(TransformBlit.POSITION);
//        if (this._eventMask & TRANSFORM_ON) {
//            this.emit(TRANSFORM_CHANGED, TransformBit.POSITION);
        }
    }
//
//    /**
//     * @en Perform a rotation on the node
//     * @zh 旋转节点
//     * @param rot The increment on rotation
//     * @param ns The operation coordinate space
//     */
//    public void rotate (Quaternion rot, NodeSpace ns) {
//        NodeSpace space = ns;
//        Quaternion.normalize(q_a, rot);
//        if (space == NodeSpace.LOCAL) {
//            Quaternion.multiply(this._lrot, this._lrot, q_a);
//        } else if (space == NodeSpace.WORLD) {
//            const worldRot = this.worldRotation;
//            Quat.multiply(q_b, q_a, worldRot);
//            Quat.invert(q_a, worldRot);
//            Quat.multiply(q_b, q_a, q_b);
//            Quat.multiply(this._lrot, this._lrot, q_b);
//        }
//        this._eulerDirty = true;
//        this.invalidateChildren(TransformBit.ROTATION);
//        if (this._eventMask & TRANSFORM_ON) {
//            this.emit(TRANSFORM_CHANGED, TransformBit.ROTATION);
//        }
//    }
//
//    /**
//     * @en Set the orientation of the node to face the target position, the node is facing minus z direction by default
//     * @zh 设置当前节点旋转为面向目标位置，默认前方为 -z 方向
//     * @param pos Target position
//     * @param up Up direction
//     */
//    public lookAt (pos: Readonly<Vec3>, up?: Readonly<Vec3>): void {
//        this.getWorldPosition(v3_a);
//        Vec3.subtract(v3_a, v3_a, pos);
//        Vec3.normalize(v3_a, v3_a);
//        Quat.fromViewUp(q_a, v3_a, up);
//        this.setWorldRotation(q_a);
//    }
//
//    /**
//     * @en Invalidate the world transform information
//     * for this node and all its children recursively
//     * @zh 递归标记节点世界变换为 dirty
//     * @param dirtyBit The dirty bits to setup to children, can be composed with multiple dirty bits
//     */
//    public invalidateChildren (dirtyBit: TransformBit): void {
//        let i = 0;
//        let j = 0;
//        let l = 0;
//        let cur: Node;
//        let children: Node[];
//        let hasChangedFlags = 0;
//        const childDirtyBit = dirtyBit | TransformBit.POSITION;
//
//        dirtyNodes[0] = this;
//
//        while (i >= 0) {
//            cur = dirtyNodes[i--];
//            hasChangedFlags = cur.hasChangedFlags;
//            if (cur.isValid && (cur._transformFlags & hasChangedFlags & dirtyBit) !== dirtyBit) {
//                cur._transformFlags |= dirtyBit;
//                cur.hasChangedFlags = hasChangedFlags | dirtyBit;
//
//                children = cur._children;
//                l = children.length;
//                for (j = 0; j < l; j++) {
//                    dirtyNodes[++i] = children[j];
//                }
//            }
//            dirtyBit = childDirtyBit;
//        }
//    }
//
//    /**
//     * @en Update the world transform information if outdated
//     * @zh 更新节点的世界变换信息
//     */
//    public updateWorldTransform (): void {
//        if (!this._transformFlags) { return; }
//        // we need to recursively iterate this
//        // eslint-disable-next-line @typescript-eslint/no-this-alias
//        let cur: Node | null = this;
//        let i = 0;
//        while (cur && cur._transformFlags) {
//            // top level node
//            dirtyNodes[i++] = cur;
//            cur = cur._parent;
//        }
//        let child: Node;
//        let childMat: Mat4;
//        let childPos: Vec3;
//        let dirtyBits = 0;
//        let positionDirty = 0;
//        let rotationScaleSkewDirty = 0;
//        let uiSkewComp: UISkew | null = null;
//        let foundSkewInAncestor = false;
//
//        while (i) {
//            child = dirtyNodes[--i];
//            childMat = child._mat;
//            childPos = child._pos;
//            dirtyBits |= child._transformFlags;
//            positionDirty = dirtyBits & TransformBit.POSITION;
//            rotationScaleSkewDirty = dirtyBits & TransformBit.RSS;
//            if (cur) {
//                if (positionDirty && !rotationScaleSkewDirty) {
//                    Vec3.transformMat4(childPos, child._lpos, cur._mat);
//                    childMat.m12 = childPos.x;
//                    childMat.m13 = childPos.y;
//                    childMat.m14 = childPos.z;
//                }
//                if (rotationScaleSkewDirty) {
//                    let originalWorldMatrix = childMat;
//                    Mat4.fromSRT(m4_1, child._lrot, child._lpos, child._lscale); // m4_1 stores local matrix
//                    if (USE_UI_SKEW && skewCompCount > 0) {
//                        foundSkewInAncestor = findSkewAndGetOriginalWorldMatrix(cur, m4_2); // m4_2 stores parent's world matrix without skew
//                        uiSkewComp = child._uiProps._uiSkewComp;
//                        if (uiSkewComp || foundSkewInAncestor) {
//                            // Save the original world matrix without skew side effect.
//                            Mat4.multiply(m4_2, m4_2, m4_1); // m4_2 stores orignal world matrix without skew
//                            if (uiSkewComp) {
//                                updateLocalMatrixBySkew(uiSkewComp, m4_1);
//                            }
//                            originalWorldMatrix = m4_2;
//                        }
//                    }
//
//                    Mat4.multiply(childMat, cur._mat, m4_1); // m4_1 stores local matrix with skew
//
//                    const rotTmp = dirtyBits & TransformBit.ROTATION ? child._rot : null;
//                    Mat4.toSRT(originalWorldMatrix, rotTmp, childPos, child._scale);
//
//                    if (USE_UI_SKEW && foundSkewInAncestor) {
//                        // NOTE: world position from Mat4.toSRT(originalWorldMatrix, ...) will not consider the skew factor.
//                        // So we need to update the world position manually here.
//                        Vec3.transformMat4(childPos, child._lpos, cur._mat);
//                    }
//                }
//            } else {
//                if (positionDirty) {
//                    Vec3.copy(childPos, child._lpos);
//                    childMat.m12 = childPos.x;
//                    childMat.m13 = childPos.y;
//                    childMat.m14 = childPos.z;
//                }
//                if (rotationScaleSkewDirty) {
//                    if (dirtyBits & TransformBit.ROTATION) {
//                        Quat.copy(child._rot, child._lrot);
//                    }
//                    if (dirtyBits & TransformBit.SCALE) {
//                        Vec3.copy(child._scale, child._lscale);
//                    }
//                    Mat4.fromSRT(childMat, child._rot, child._pos, child._scale);
//
//                    if (USE_UI_SKEW && skewCompCount > 0) {
//                        uiSkewComp = child._uiProps._uiSkewComp;
//                        if (uiSkewComp) {
//                            updateLocalMatrixBySkew(uiSkewComp, childMat);
//                        }
//                    }
//                }
//            }
//
//            child._transformFlags = TransformBit.NONE;
//            cur = child;
//        }
//    }
//
//    // ===============================
//    // transform
//    // ===============================
//
//    /**
//     * @en Set position in local coordinate system
//     * @zh 设置本地坐标
//     * @param position Target position
//     */
//    public setPosition(position: Readonly<Vec3>): void;
//
//    /**
//     * @en Set position in local coordinate system
//     * @zh 设置本地坐标
//     * @param x X axis position
//     * @param y Y axis position
//     * @param z Z axis position
//     */
//    public setPosition(x: number, y: number, z?: number): void;
//
//    public setPosition (val: Readonly<Vec3> | number, y?: number, z?: number): void {
//        const localPosition = this._lpos;
//
//        if (y === undefined) {
//            Vec3.copy(localPosition, val as Vec3);
//        } else {
//            if (z === undefined) {
//                z = localPosition.z;
//            }
//
//            Vec3.set(localPosition, val as number, y, z);
//        }
//
//        this.invalidateChildren(TransformBit.POSITION);
//        if (this._eventMask & TRANSFORM_ON) {
//            this.emit(TRANSFORM_CHANGED, TransformBit.POSITION);
//        }
//    }
//
//    /**
//     * @en Get position in local coordinate system, please try to pass `out` vector and reuse it to avoid garbage.
//     * @zh 获取本地坐标，注意，尽可能传递复用的 [[Vec3]] 以避免产生垃圾。
//     * @param out Set the result to out vector
//     * @return If `out` given, the return value equals to `out`, otherwise a new vector will be generated and return
//     */
//    public getPosition (out?: Vec3): Vec3 {
//        if (out) {
//            return Vec3.set(out, this._lpos.x, this._lpos.y, this._lpos.z);
//        }
//        return Vec3.copy(new Vec3(), this._lpos);
//    }
//
//    /**
//     * @en Set rotation in local coordinate system with a quaternion representing the rotation.
//     * Please make sure the rotation is normalized.
//     * @zh 用四元数设置本地旋转, 请确保设置的四元数已归一化。
//     * @param rotation Rotation in quaternion
//     */
//    public setRotation(rotation: Readonly<Quat>): void;
//
//    /**
//     * @en Set rotation in local coordinate system with a quaternion representing the rotation.
//     * Please make sure the rotation is normalized.
//     * @zh 用四元数设置本地旋转, 请确保设置的四元数已归一化。
//     * @param x X value in quaternion
//     * @param y Y value in quaternion
//     * @param z Z value in quaternion
//     * @param w W value in quaternion
//     */
//    public setRotation(x: number, y: number, z: number, w: number): void;
//
//    public setRotation (val: Readonly<Quat> | number, y?: number, z?: number, w?: number): void {
//        if (y === undefined) {
//            Quat.copy(this._lrot, val as Quat);
//        } else {
//            Quat.set(this._lrot, val as number, y, z!, w!);
//        }
//
//        this._eulerDirty = true;
//        this.invalidateChildren(TransformBit.ROTATION);
//        if (this._eventMask & TRANSFORM_ON) {
//            this.emit(TRANSFORM_CHANGED, TransformBit.ROTATION);
//        }
//    }
//
//    /**
//     * @en Set rotation in local coordinate system with a vector representing euler angles
//     * @zh 用欧拉角设置本地旋转
//     * @param rotation Rotation in vector
//     */
//    public setRotationFromEuler(rotation: Vec3): void;
//
//    /**
//     * @en Set rotation in local coordinate system with euler angles
//     * @zh 用欧拉角设置本地旋转
//     * @param x X axis rotation
//     * @param y Y axis rotation
//     * @param zOpt Z axis rotation
//     */
//    public setRotationFromEuler(x: number, y: number, zOpt?: number): void;
//
//    public setRotationFromEuler (val: Vec3 | number, y?: number, zOpt?: number): void {
//        if (y === undefined) {
//            Vec3.copy(this._euler, val as Vec3);
//            Quat.fromEuler(this._lrot, (val as Vec3).x, (val as Vec3).y, (val as Vec3).z);
//        } else {
//            const z = zOpt === undefined ? this._euler.z : zOpt;
//            Vec3.set(this._euler, val as number, y, z);
//            Quat.fromEuler(this._lrot, val as number, y, z);
//        }
//
//        this._eulerDirty = false;
//
//        this.invalidateChildren(TransformBit.ROTATION);
//        if (this._eventMask & TRANSFORM_ON) {
//            this.emit(TRANSFORM_CHANGED, TransformBit.ROTATION);
//        }
//    }
//
//    /**
//     * @en Get rotation as quaternion in local coordinate system, please try to pass `out` quaternion and reuse it to avoid garbage.
//     * @zh 获取本地旋转，注意，尽可能传递复用的 [[Quat]] 以避免产生垃圾。
//     * @param out Set the result to out quaternion
//     * @return If `out` given, the return value equals to `out`, otherwise a new quaternion will be generated and return
//     */
//    public getRotation (out?: Quat): Quat {
//        if (out) {
//            return Quat.set(out, this._lrot.x, this._lrot.y, this._lrot.z, this._lrot.w);
//        }
//        return Quat.copy(new Quat(), this._lrot);
//    }
//
//    /**
//     * @en Set scale in local coordinate system
//     * @zh 设置本地缩放
//     * @param scale Target scale
//     */
//    public setScale(scale: Readonly<Vec3>): void;
//
//    /**
//     * @en Set scale in local coordinate system
//     * @zh 设置本地缩放
//     * @param x X axis scale
//     * @param y Y axis scale
//     * @param z Z axis scale
//     */
//    public setScale(x: number, y: number, z?: number): void;
//
//    public setScale (val: Readonly<Vec3> | number, y?: number, z?: number): void {
//        const localScale = this._lscale;
//
//        if (y === undefined) {
//            Vec3.copy(localScale, val as Vec3);
//        } else {
//            if (z === undefined) {
//                z = localScale.z;
//            }
//            Vec3.set(localScale, val as number, y, z);
//        }
//
//        this.invalidateChildren(TransformBit.SCALE);
//
//        if (this._eventMask & TRANSFORM_ON) {
//            this.emit(TRANSFORM_CHANGED, TransformBit.SCALE);
//        }
//    }
//
//    /**
//     * @en Get scale in local coordinate system, please try to pass `out` vector and reuse it to avoid garbage.
//     * @zh 获取本地缩放，注意，尽可能传递复用的 [[Vec3]] 以避免产生垃圾。
//     * @param out Set the result to out vector
//     * @return If `out` given, the return value equals to `out`, otherwise a new vector will be generated and return
//     */
//    public getScale (out?: Vec3): Vec3 {
//        if (out) {
//            return Vec3.set(out, this._lscale.x, this._lscale.y, this._lscale.z);
//        }
//        return Vec3.copy(new Vec3(), this._lscale);
//    }
//
//    /**
//     * @en Inversely transform a point from world coordinate system to local coordinate system.
//     * @zh 逆向变换一个空间点，一般用于将世界坐标转换到本地坐标系中。
//     * @param out The result point in local coordinate system will be stored in this vector
//     * @param p A position in world coordinate system
//     */
//    public inverseTransformPoint (out: Vec3, p: Vec3): Vec3 {
//        Vec3.copy(out, p);
//        // we need to recursively iterate this
//        // eslint-disable-next-line @typescript-eslint/no-this-alias
//        let cur: Node = this;
//        let i = 0;
//        while (cur._parent) {
//            dirtyNodes[i++] = cur;
//            cur = cur._parent;
//        }
//        while (i >= 0) {
//            Vec3.transformInverseRTS(out, out, cur._lrot, cur._lpos, cur._lscale);
//            cur = dirtyNodes[--i];
//        }
//        return out;
//    }
//
//    /**
//     * @en Set position in world coordinate system
//     * @zh 设置世界坐标
//     * @param position Target position
//     */
//    public setWorldPosition(position: Vec3): void;
//
//    /**
//     * @en Set position in world coordinate system
//     * @zh 设置世界坐标
//     * @param x X axis position
//     * @param y Y axis position
//     * @param z Z axis position
//     */
//    public setWorldPosition(x: number, y: number, z: number): void;
//
//    public setWorldPosition (val: Vec3 | number, y?: number, z?: number): void {
//        const worldPosition = this._pos;
//
//        if (y === undefined) {
//            Vec3.copy(worldPosition, val as Vec3);
//        } else {
//            Vec3.set(worldPosition, val as number, y, z!);
//        }
//
//        const parent = this._parent;
//        const local = this._lpos;
//        if (parent) {
//            // TODO: benchmark these approaches
//            /* */
//            parent.updateWorldTransform();
//            Vec3.transformMat4(local, worldPosition, Mat4.invert(m4_1, parent._mat));
//            /* *
//            parent.inverseTransformPoint(local, this._pos);
//            /* */
//        } else {
//            Vec3.copy(local, worldPosition);
//        }
//
//        this.invalidateChildren(TransformBit.POSITION);
//        if (this._eventMask & TRANSFORM_ON) {
//            this.emit(TRANSFORM_CHANGED, TransformBit.POSITION);
//        }
//    }
//
//    /**
//     * @en Get position in world coordinate system, please try to pass `out` vector and reuse it to avoid garbage.
//     * @zh 获取世界坐标，注意，尽可能传递复用的 [[Vec3]] 以避免产生垃圾。
//     * @param out Set the result to out vector
//     * @return If `out` given, the return value equals to `out`, otherwise a new vector will be generated and return
//     */
//    public getWorldPosition (out?: Vec3): Vec3 {
//        this.updateWorldTransform();
//        if (out) {
//            return Vec3.copy(out, this._pos);
//        }
//        return Vec3.copy(new Vec3(), this._pos);
//    }
//
//    /**
//     * @en Set rotation in world coordinate system with a quaternion representing the rotation
//     * @zh 用四元数设置世界坐标系下的旋转
//     * @param rotation Rotation in quaternion
//     */
//    public setWorldRotation(rotation: Quat): void;
//
//    /**
//     * @en Set rotation in world coordinate system with a quaternion representing the rotation
//     * @zh 用四元数设置世界坐标系下的旋转
//     * @param x X value in quaternion
//     * @param y Y value in quaternion
//     * @param z Z value in quaternion
//     * @param w W value in quaternion
//     */
//    public setWorldRotation(x: number, y: number, z: number, w: number): void;
//
//    public setWorldRotation (val: Quat | number, y?: number, z?: number, w?: number): void {
//        const worldRotation = this._rot;
//        if (y === undefined) {
//            Quat.copy(worldRotation, val as Quat);
//        } else {
//            Quat.set(worldRotation, val as number, y, z!, w!);
//        }
//
//        if (this._parent) {
//            this._parent.updateWorldTransform();
//            Quat.multiply(this._lrot, Quat.conjugate(this._lrot, this._parent._rot), worldRotation);
//        } else {
//            Quat.copy(this._lrot, worldRotation);
//        }
//        this._eulerDirty = true;
//
//        this.invalidateChildren(TransformBit.ROTATION);
//        if (this._eventMask & TRANSFORM_ON) {
//            this.emit(TRANSFORM_CHANGED, TransformBit.ROTATION);
//        }
//    }
//
//    /**
//     * @en Set rotation in world coordinate system with euler angles
//     * @zh 用欧拉角设置世界坐标系下的旋转
//     * @param x X axis rotation
//     * @param y Y axis rotation
//     * @param z Z axis rotation
//     */
//    public setWorldRotationFromEuler (x: number, y: number, z: number): void {
//        Quat.fromEuler(q_a, x, y, z);
//        this.setWorldRotation(q_a);
//    }
//
//    /**
//     * @en Get rotation as quaternion in world coordinate system, please try to pass `out` quaternion and reuse it to avoid garbage.
//     * @zh 获取世界坐标系下的旋转，注意，尽可能传递复用的 [[Quat]] 以避免产生垃圾。
//     * @param out Set the result to out quaternion
//     * @return If `out` given, the return value equals to `out`, otherwise a new quaternion will be generated and return
//     */
//    public getWorldRotation (out?: Quat): Quat {
//        this.updateWorldTransform();
//        if (out) {
//            return Quat.copy(out, this._rot);
//        }
//        return Quat.copy(new Quat(), this._rot);
//    }
//
//    /**
//     * @en Set scale in world coordinate system
//     * @zh 设置世界坐标系下的缩放
//     * @param scale Target scale
//     */
//    public setWorldScale(scale: Vec3): void;
//
//    /**
//     * @en Set scale in world coordinate system
//     * @zh 设置世界坐标系下的缩放
//     * @param x X axis scale
//     * @param y Y axis scale
//     * @param z Z axis scale
//     */
//    public setWorldScale(x: number, y: number, z: number): void;
//
//    public void setWorldScale ( Vector3 val, float y, float z) {
//        const parent = self._parent;
//        if (parent) {
//            self.updateWorldTransform();
//        }
//
//        const worldScale = self._scale;
//        if (y === undefined) {
//            Vec3.copy(worldScale, val as Vec3);
//        } else {
//            Vec3.set(worldScale, val as number, y, z!);
//        }
//
//        let rotationFlag = TransformBit.NONE;
//        if (parent) {
//            const worldMatrix = self._mat;
//            const uiSkewComp = self._uiProps._uiSkewComp;
//            if (uiSkewComp) {
//                Mat4.fromSRT(m4_1, self._lrot, self._lpos, self._lscale);
//                Mat4.multiply(worldMatrix, parent._mat, m4_1);
//            }
//            const xScale = Vec3.set(v3_b, worldMatrix.m00, worldMatrix.m01, worldMatrix.m02).length();
//            const yScale = Vec3.set(v3_b, worldMatrix.m04, worldMatrix.m05, worldMatrix.m06).length();
//            const zScale = Vec3.set(v3_b, worldMatrix.m08, worldMatrix.m09, worldMatrix.m10).length();
//            if (xScale === 0) {
//                v3_a.x = worldScale.x;
//                worldMatrix.m00 = 1;
//                rotationFlag = TransformBit.ROTATION;
//            } else {
//                v3_a.x = worldScale.x / xScale;
//            }
//
//            if (yScale === 0) {
//                v3_a.y = worldScale.y;
//                worldMatrix.m05 = 1;
//                rotationFlag = TransformBit.ROTATION;
//            } else {
//                v3_a.y = worldScale.y / yScale;
//            }
//
//            if (zScale === 0) {
//                v3_a.z = worldScale.z;
//                worldMatrix.m10 = 1;
//                rotationFlag = TransformBit.ROTATION;
//            } else {
//                v3_a.z = worldScale.z / zScale;
//            }
//
//            Mat4.scale(m4_1, worldMatrix, v3_a);
//            Mat4.multiply(m4_2, Mat4.invert(m4_2, parent._mat), m4_1);
//            Mat3.fromQuat(m3_1, Quat.conjugate(qt_1, self._lrot));
//            Mat3.multiplyMat4(m3_1, m3_1, m4_2);
//
//            const localScale = self._lscale;
//            localScale.x = Vec3.set(v3_a, m3_1.m00, m3_1.m01, m3_1.m02).length();
//            localScale.y = Vec3.set(v3_a, m3_1.m03, m3_1.m04, m3_1.m05).length();
//            localScale.z = Vec3.set(v3_a, m3_1.m06, m3_1.m07, m3_1.m08).length();
//            if (localScale.x === 0 || localScale.y === 0 || localScale.z === 0) {
//                rotationFlag = TransformBit.ROTATION;
//            }
//        } else {
//            Vec3.copy(self._lscale, worldScale);
//        }
//
//        self.invalidateChildren(TransformBit.SCALE | rotationFlag);
//        if (self._eventMask & TRANSFORM_ON) {
//            self.emit(TRANSFORM_CHANGED, TransformBit.SCALE | rotationFlag);
//        }
//    }
//
//    /**
//     * @en Get scale in world coordinate system, please try to pass `out` vector and reuse it to avoid garbage.
//     * @zh 获取世界缩放，注意，尽可能传递复用的 [[Vec3]] 以避免产生垃圾。
//     * @param out Set the result to out vector
//     * @return If `out` given, the return value equals to `out`, otherwise a new vector will be generated and return
//     */
//    public getWorldScale (Vector3 out) {
//        this.updateWorldTransform();
//        return Vector3.copy(out, this._scale);
//    }
//
//    /**
//     * @en Get a world transform matrix
//     * @zh 获取世界变换矩阵
//     * @param out Set the result to out matrix
//     * @return If `out` given, the return value equals to `out`, otherwise a new matrix will be generated and return
//     */
//    public getWorldMatrix (Matrix4 out) {
//        this.updateWorldTransform();
//        Matrix4 target = out;
//        return Matrix4.copy(target, this._mat);
//    }


    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.flush();
        batch.end();
        ModelBatchUtils modelBatchUtils = ModelBatchUtils.getInstance();
        modelBatchUtils.begin();
        modelBatchUtils.draw(modelInstance);
        modelBatchUtils.end();
        batch.begin();
    }
}
