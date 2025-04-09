package com.libGdx.test.model;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.particles.ParticleController;
import com.badlogic.gdx.graphics.g3d.particles.ParticleEffect;
import com.badlogic.gdx.graphics.g3d.particles.ParticleEffectLoader;
import com.badlogic.gdx.graphics.g3d.particles.ParticleSystem;
import com.badlogic.gdx.graphics.g3d.particles.batches.BillboardParticleBatch;
import com.badlogic.gdx.graphics.g3d.particles.batches.PointSpriteParticleBatch;
import com.badlogic.gdx.utils.Array;

public class GdxTest extends ApplicationAdapter {
    public OrthographicCamera cam;
    public ModelBatch modelBatch;
    public AssetManager assets;
    private ParticleEffect currentEffects;
    private ParticleSystem particleSystem;

    @Override
    public void create () {
        modelBatch = new ModelBatch();

        cam = new OrthographicCamera(18.0f, 18.0f);
        assets = new AssetManager();

        particleSystem = ParticleSystem.get();
        BillboardParticleBatch pointSpriteBatch = new BillboardParticleBatch();
        pointSpriteBatch.setCamera(cam);
        particleSystem = ParticleSystem.get();

        particleSystem.add(pointSpriteBatch);
        ParticleEffectLoader.ParticleEffectLoadParameter loadParam = new ParticleEffectLoader.ParticleEffectLoadParameter(particleSystem.getBatches());
        ParticleEffectLoader loader = new ParticleEffectLoader(new InternalFileHandleResolver());
        assets.setLoader(ParticleEffect.class, loader);
        assets.load("assets/effects/gKeyEffect.pfx", ParticleEffect.class, loadParam);
        // halt the main thread until assets are loaded.
        // this is bad for actual games, but okay for demonstration purposes.
        assets.finishLoading();

        currentEffects=assets.get("assets/effects/gKeyEffect.pfx",ParticleEffect.class).copy();
        currentEffects.init();
        particleSystem.add(currentEffects);
        Array<ParticleController> controllers = currentEffects.getControllers();
        for (ParticleController controller : controllers) {
            controller.renderer.setBatch(pointSpriteBatch);
        }
    }

    @Override
    public void render () {
        Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
        Gdx.gl.glEnable(GL20.GL_TEXTURE_2D);



        modelBatch.begin(cam);
        particleSystem.update();
        particleSystem.begin();
        particleSystem.draw();
        particleSystem.end();
        modelBatch.render(particleSystem);
        modelBatch.end();
    }

    @Override
    public void dispose () {
        if (currentEffects!=null) currentEffects.dispose();
        modelBatch.dispose();
        assets.dispose();
    }

    public static void main(String[] args) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.x = 1000;
        config.stencil=8;
        config.y = 0;
        config.height = (int) (1920 * 0.5f);
        config.width = (int) (1080 * 0.5f);
        new LwjglApplication(new GdxTest(), config);
    }
}