package com.kw.gdx.label;//package com.read.csv.com.kw.gdx.label;
//
//import com.badlogic.gdx.scenes.scene2d.Group;
//import com.badlogic.gdx.scenes.scene2d.ui.Label;
//import com.badlogic.gdx.utils.Align;
//import com.read.csv.RiderGame;
//
//public class DLabel extends Group {
//    private Label4 label1;
//    private Label4 label2;
//    private int old;
//    private int target;
//    private float countTime = 0;
//    public DLabel(){
//        label1 = new Label4("",new Label.LabelStyle(){{
//            font = RiderGame.instence().getAsset().getX_62_129_1();
//        }});
//
//       addActor(label1);
//        setHeight(66);
//        label1.setY(getHeight()/2, Align.center);
//    }
//
//    private int lastAddScore;
//
//    public void setText(int text1,int text2){
//        if (text2!=0) {
//            if (lastAddScore != text2){
//                label2 = new Label4("",new Label.LabelStyle(){{
//                    font = RiderGame.instence().getAsset().getPx_86();;
//                }});
//                addActor(label2);
//                label2.setX(label1.getPrefWidth(),Align.center);
//                label2.setY(0,Align.bottom);
////                float offsetY = 25;
////                label2.addAction(Actions.parallel(
////                        Actions.sequence(Actions.alpha(0,0),
////                                Actions.alpha(1,0.06667F),
////                                Actions.alpha(1,0.53F),
////                                Actions.alpha(0,0.2F)),
////                        Actions.sequence(
////                                Actions.moveTo(label1.getPrefWidth()+10+42.26F, 73.05F+offsetY,0),
////                                Actions.moveTo(label1.getPrefWidth()+10+0,      0+offsetY,0.16667F),
////                                Actions.moveTo(label1.getPrefWidth()+10+0,      0+offsetY,0.4333F,new BseInterpolation(0.25F,0,0.75F,1)),
////                                Actions.moveTo(label1.getPrefWidth()+10-22.85F,-18.14F+offsetY,0.2F)),
////
////                        Actions.sequence(
////                                Actions.scaleTo(1.809F,1.809F,0),
////                                Actions.scaleTo(1,1,0.1667F),
////                                Actions.scaleTo(1F,1F,0.433F),
////                                Actions.scaleTo(0.789F,0.789F,0.2F))
////                ));
//            }
//        }
//        this.lastAddScore = text2;
//        target = text1;
//        if (old>target){
//            old = target;
//        }
//        label1.setText(old+"");
//        if (text2 != 0) {
//            if (label2!=null) {
//
//                label2.setText("+" + text2);
//                label2.pack();
//                label2.setY(label1.getY(Align.bottom),Align.bottom);
//            }
//        }else {
//            float prefWidth1 = label1.getPrefWidth();
//            setWidth(prefWidth1);
//            if (label2!=null) {
//                label2.setText("");
//                label2.pack();
//                label2.setY(label1.getY(Align.bottom),Align.bottom);
//            }
//        }
//        label1.setX(0);
//    }
//
//
//    @Override
//    public void act(float delta) {
//        super.act(delta);
//        if (old<target) {
//            countTime += delta;
//            if (countTime > 0.1F) {
//                countTime = 0;
//                old ++;
//                label1.setText(old+"");
//            }
//        }else {
//            old = target;
//            label1.setText(old+"");
//        }
//    }
//
//    public void setTextForce(int currentScore, int i) {
//        old = currentScore;
//        target = old;
//        label1.setText(old+"");
//        label1.pack();
//        setWidth(label1.getPrefWidth());
//        label1.setX(0);
//    }
//}
