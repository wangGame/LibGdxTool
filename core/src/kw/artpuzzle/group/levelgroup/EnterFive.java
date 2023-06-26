package kw.artpuzzle.group.levelgroup;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.kw.gdx.asset.Asset;

import kw.artpuzzle.theme.GameTheme;

class EnterFive extends Group {
    public EnterFive(float width, float height) {
        setSize(width,height);

        EgeToMiddle spreed = new EgeToMiddle(width,300);
        addActor(spreed);
        spreed.setDelay(0f);
        spreed.setSpeed(1.3f);

        CirSpreed cirSpreed = new CirSpreed(width,height);
        cirSpreed.setDelay(0.5f);
        cirSpreed.setSpeed(1.5f);
        addActor(cirSpreed);
    }
}
