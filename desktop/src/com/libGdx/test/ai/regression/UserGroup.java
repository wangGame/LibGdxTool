package com.libGdx.test.ai.regression;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Array;
import com.kw.gdx.asset.Asset;
import com.kw.gdx.constant.Constant;

import org.nd4j.linalg.api.ndarray.INDArray;

/**
 *  数据回归
 */
public class UserGroup extends Group implements OnTrainingUpdateEventListener {
    TrainingTask task;
    private INDArray modelOut = null;
    private Array<Image> images;
    private Array<Image> imagesPoint;

    public UserGroup(){
        this.images = new Array<>();
        this.imagesPoint = new Array<>();
        this.task = new TrainingTask();
        this.task.setListener(this);
        this.task.executeTask("assets/csv/saturn_data_train.csv");
    }

    public void xx(){
        float w = Constant.GAMEWIDTH;
        float h = Constant.GAMEHIGHT;
        //draw the nn predictions:
        if (modelOut != null) {
            INDArray  xyGrid = task.getXyGrid();
            int nRows = xyGrid.rows();
            for (int i = 0; i< nRows; i++){
                int x =  (int)(xyGrid.getFloat(i, 0) * w);
                int y = (int) (xyGrid.getFloat(i, 1)  * h);
                float z = modelOut.getFloat(i, 0);
                Color color = (z >= 0.5f) ? Color.GREEN : Color.RED;
                Image image;
                if (images.size<=i){
                    image = new Image(Asset.getAsset().getTexture("assets/white_cir.png"));
                    images.add(image);
                    addActor(image);
                }else {
                    image = images.get(i);
                }
                image.setColor(color);
                image.setPosition(x,y);
            }
        }

        //draw the data set if we have one.
        if (null != task.getData()) {
            int index = 0 ;
            for (float[] datum : task.getData()) {
                index ++;
                int x = (int) (datum[1] * w);
                int y = (int) (datum[2] * h);
                Color color = (datum[0] == 0.0f) ? Color.BLUE : Color.WHITE;
                Image image;
                if (imagesPoint.size<=index){
                    image = new Image(Asset.getAsset().getTexture("assets/white_cir.png"));
                    imagesPoint.add(image);
                    addActor(image);
                }else {
                    image = imagesPoint.get(index);
                }
                image.setColor(color);
                image.setPosition(x,y);
            }
        }
    }


    @Override
    public void OnTrainingUpdate(INDArray modelOut) {
        this.modelOut = modelOut;
        Gdx.app.postRunnable(new Runnable() {
            @Override
            public void run() {
                xx();
            }
        });
    }
}
