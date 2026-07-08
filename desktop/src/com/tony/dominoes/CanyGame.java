package com.tony.dominoes;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.loader.G3dModelLoader;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.UBJsonReader;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.kw.gdx.BaseGame;
import com.kw.gdx.asset.Asset;
import com.kw.gdx.constant.Constant;
import com.kw.gdx.g3.ModelBatchUtils;
import com.kw.gdx.resource.annotation.GameInfo;
import com.kw.gdx.screen.CurvePanelScreen;
import com.kw.gdx.screen.PathBoundingClippingScreen;
import com.kw.gdx.screen.SlotColorScreen;
import com.kw.gdx.screen.TexturePackerScreen;
import com.kw.gdx.screen.TextureUnpackerScreen;
import com.kw.gdx.screen.TraceScreen;
import com.kw.gdx.utils.log.NLog;
import com.tony.dominoes.screen.LoadingScreen;
import com.tony.dominoes.screen.MeshDemoScreen;
import com.tony.dominoes.screen.MeshEditorScreen;
import com.tony.dominoes.screen.TraceMeshDemoScreen;

@GameInfo(width = 1080, height = 1920, batch = Constant.COUPOLYGONBATCH)
public class CanyGame extends BaseGame {
    public CanyGame() {
        NLog.isLog = false;
        otherDispose();
    }

    @Override
    public void create() {
        super.create();
        Constant.TOUEABLETYPE = 1; //有点击效果
        //增加3D模型加载器
        AssetManager assetManager = Asset.getAsset().getAssetManager();
        assetManager.setLoader(Model.class, new G3dModelLoader(new JsonReader(), assetManager.getFileHandleResolver()));
        assetManager.setLoader(Model.class, ".g3db", new G3dModelLoader(new UBJsonReader(), assetManager.getFileHandleResolver()));
        Constant.viewColor.set(0.f, 0.f, 0.0f, 1.0f);
        NLog.i("create -->");

    }

    @Override
    protected void loadingView() {
        super.loadingView();
        setScreen(CurvePanelScreen.class);
    }

    @Override
    protected void initViewport() {
        stageViewport = new ExtendViewport(Constant.WIDTH, Constant.HIGHT);
        Constant.camera = stageViewport.getCamera();
        Constant.camera.far = 7000;
        NLog.i("stageViewport :" + Constant.WIDTH + "," + Constant.HIGHT);
        NLog.i("camera far :" + 7000);
    }

    @Override
    public void resume() {
        super.resume();
    }

    @Override
    protected void otherDispose() {
        Asset.disposeNull();
        ModelBatchUtils.disposeAll();
    }
}
