//package com.libGdx.test.asset;
//
//import com.badlogic.gdx.Gdx;
//import com.badlogic.gdx.files.FileHandle;
//import com.badlogic.gdx.scenes.scene2d.Actor;
//import com.badlogic.gdx.scenes.scene2d.Group;
//import com.badlogic.gdx.scenes.scene2d.Stage;
//import com.badlogic.gdx.utils.Array;
//import com.kw.gdx.resource.cocosload.CocosResource;
//import com.libGdx.test.base.LibGdxTestMain;
//import com.ui.ManagerUIEditor;
//
//import net.mwplay.cocostudio.ui.model.CCExport;
//import net.mwplay.cocostudio.ui.model.ObjectData;
//
//public class App extends LibGdxTestMain {
//    public static void main(String[] args) {
//        App app = new App();
//        app.start();
//    }
//
//    @Override
//    public void useShow(Stage stage) {
//        super.useShow(stage);
//
//        String fileName = null;
//        String s = fileName.split("\\.")[0];
//        FileHandle local = Gdx.files.local("levelout/"+s+".txt");
//        if (local.exists()) {
//            local.delete();
//        }
//        LevelData data = new LevelData();
//        Array<LayerBean> shapes = new Array<>();
//        Array<HolePosBean> holes = new Array<>();
//        Array<Integer> dingzis = new Array<>();
//        Array<PosBean> locks = new Array<>();
//        data.setDingZiDatas(dingzis);
//        data.setHoleDatas(holes);
//        data.setLockDatas(locks);
//        data.setLayerBeans(shapes);
//
//
//        ManagerUIEditor managerUIEditor = CocosResource.loadFil1e1("cocos/level2.json");
//        CCExport export1 = managerUIEditor.getExport1();
//        System.out.println(export1.Content);
//        ObjectData objectData = export1.Content.Content.ObjectData;
//        System.out.println(objectData.Name);
//        for (ObjectData child : objectData.Children) {
//            System.out.println(child.Name);
//            System.out.println("--------------");
//            if (child.Name.startsWith("bg")){
//                System.out.println(child.Name);
//            }else {
//                if (child!=null&&child.Children!=null) {
//                    for (ObjectData data : child.Children) {
//                        String name = data.Name;
//                    }
//
//                    for (ObjectData data : child.Children) {
//                        System.out.println(data.Name);
//                    }
//                }
//            }
//            System.out.println("--------------");
//        }
//    }
//
//    private void setGroupVisible(Actor group) {
//        group.setVisible(true);
//        if (group instanceof Group){
//            for (Actor child : ((Group)group).getChildren()) {
//                setGroupVisible(child);
//            }
//        }
//    }
//}
