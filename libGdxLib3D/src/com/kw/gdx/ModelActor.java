package com.kw.gdx;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.Vector4;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.utils.Align;
import com.kw.gdx.g3.ModelBatchUtils;
import com.kw.gdx.lib.NodeSpace;


/**
 * 继承Actor
 */
public class ModelActor extends Actor {
    private boolean isDrity;
    private ModelInstance modelInstance;
    /**
     * @engineInternal NOTE: this is engineInternal interface that doesn't have a side effect of updating the transforms
     */
    public Vector3 _pos = new Vector3();
    /**
     * @engineInternal NOTE: this is engineInternal interface that doesn't have a side effect of updating the transforms
     */
    public Quaternion _rot = new Quaternion();
    /**
     * @engineInternal NOTE: this is engineInternal interface that doesn't have a side effect of updating the transforms
     */
    public Vector3 _scale = new Vector3();
    /**
     * @engineInternal NOTE: this is engineInternal interface that doesn't have a side effect of updating the transforms
     */
    public Matrix4 _mat = new Matrix4();
    public Matrix4 useMat = new Matrix4();
    // local transform
//    @serializable
    protected Vector3 _lpos = new Vector3();

//    @serializable
    protected Vector3 _lrot = new Vector3();

//    @serializable
    protected Vector3 _lscale = new Vector3(1, 1, 1);

    public ModelActor(){

    }

    public ModelActor(ModelInstance instance){
        this.modelInstance = instance;
        setSize(0,0);
        setDebug(true);
    }

    public Vector3 get_lrot() {
        return _lrot;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.flush();
        batch.end();
        ModelBatchUtils modelBatchUtils = ModelBatchUtils.getInstance();
        modelBatchUtils.begin();
//        batch.getProjectionMatrix().getScale(_scale);
//        batch.getProjectionMatrix().getTranslation(_pos);
//        batch.getProjectionMatrix().getRotation(_rot);
        calMatr(batch);
        modelBatchUtils.draw(modelInstance);
        modelBatchUtils.end();
        batch.begin();
    }

    public void setPosition(Vector3 vector3){
        setPosition(vector3.x,vector3.y, Align.center);
        //设置本地坐标
        _lpos.set(vector3);
        isDrity = true;
    }

    @Override
    public void setScale(float scaleX, float scaleY) {
        super.setScale(scaleX, scaleY);
        _lscale.set(scaleX,scaleY,scaleX);
        isDrity = true;
    }

    @Override
    public void setPosition(float x, float y, int alignment){
        super.setPosition(x,y,alignment);
        _lpos.set(x,y,-400);
        isDrity = true;
    }

    public void setScale(Vector3 scale){
        _lscale.set(scale);
        isDrity = true;
    }

    public void rotation(float rox,float roy,float roz){
        _lrot.set(rox,roy,roz);
        setRotation(_lrot.z);
        isDrity = true;
    }

    public void rotation(Vector3 rotation){
           _lrot.set(rotation);
           setRotation(rotation.z);
           isDrity = true;
    }



    public void rotation(Vector3 rotation,NodeSpace nodeSpace){
        if(nodeSpace == NodeSpace.LOCAL){

        }else {
            _lrot.set(rotation);
            setRotation(rotation.z);
        }
    }

    public BoundingBox boundingBox() {
        return modelInstance.calculateBoundingBox(new BoundingBox());
    }



    public void calMatr(Batch batch){
        //必须先平移在旋转   不知道这是什么丑毛病
        if (  isDrity ) {
            isDrity = false;
            _mat.idt();

            _mat.mul(batch.getTransformMatrix());
            //获取父类位置加过来
            //本地位置
            _mat.translate(_lpos.x, _lpos.y, _lpos.z);
            //旋转
            //获取父类的乘过来
            _mat.rotate(Vector3.X, _lrot.x);
            _mat.rotate(Vector3.Y, _lrot.y);
            _mat.rotate(Vector3.Z, _lrot.z);
            //缩放
            //获取父类的乘过来
            _mat.scale(_lscale.x, _lscale.y, _lscale.z);

            useMat.set(_mat);
        }

        modelInstance.transform.set(useMat);
    }



    float o = 0;
    @Override
    public void act(float delta) {
//        o += delta * 20;
//        rotation(new Vector3(0,o,50));
        super.act(delta);
    }
}
