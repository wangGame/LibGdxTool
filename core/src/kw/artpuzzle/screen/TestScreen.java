package kw.artpuzzle.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.kw.gdx.BaseGame;
import com.kw.gdx.asset.Asset;
import com.kw.gdx.screen.BaseScreen;

public class TestScreen extends BaseScreen {
    private ShaderProgram shadowProgram = new ShaderProgram(
                    Gdx.files.internal("shader/hui.vert"),
                    Gdx.files.internal("shader/hui.frag"));
    public TestScreen(BaseGame game) {
        super(game);
    }

    @Override
    public void show() {
        super.show();
        Gdx.files.internal("");
        Group group = new Group(){
            @Override
            public void draw(Batch batch, float parentAlpha) {
                if (shadowProgram !=null) {
                    batch.flush();
                    batch.setShader(shadowProgram);
                    super.draw(batch, parentAlpha);
                    batch.flush();
                    batch.setShader(null);
                }else {
                    super.draw(batch, parentAlpha);
                }
            }
        };

        Image image = new Image(Asset.getAsset().getTexture("progress_light.png"));
        group.addActor(image);

        stage.addActor(group);
    }
}
