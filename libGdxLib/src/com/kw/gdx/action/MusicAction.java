package com.kw.gdx.action;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.scenes.scene2d.actions.TemporalAction;

/**
 * eg：
 * simple use:
 *      MusicAction musicChengeBigAction = new MusicAction();
 *        musicChengeBigAction.setMusic(currentPlayMusic);
 *        musicChengeBigAction.setStart(0);
 *        musicChengeBigAction.setEnd(1);
 *        musicChengeBigAction.setDuration(3f);
 *
 *
 *
 *      Music currentPlayMusic = AudioProcess.getCurrentPlayMusic();
 *      if (currentPlayMusic!=null){
 *      MusicAction musicChengeBigAction = new MusicAction();
 *      musicChengeBigAction.setMusic(currentPlayMusic);
 *      musicChengeBigAction.setStart(0);
 *      musicChengeBigAction.setEnd(1);
 *      musicChengeBigAction.setDuration(3f);
 *      MusicAction musicChangeSmallAction = new MusicAction();
 *      musicChangeSmallAction.setMusic(currentPlayMusic);
 *      musicChangeSmallAction.setStart(1);
 *      musicChangeSmallAction.setEnd(0);
 *      musicChangeSmallAction.setDuration(3f);
 *
 *      stage.addAction(Actions.forever(Actions.sequence(musicChengeBigAction,musicChangeSmallAction)));
 *
 */
public class MusicAction extends TemporalAction {
    private double start, end;
    private double value;
    private Music music;
    public MusicAction(){

    }

    /** Creates an IntAction that transitions from start to end. */
    public MusicAction (Music music,Number start, Number end) {
        this.music = music;
        this.start = Double.valueOf(start.toString());
        this.end = Double.valueOf(end.toString());
        this.value = this.start;
    }

    protected void begin () {
        value = start;
    }

    protected void update (float percent) {
        value = start + (end - start) * percent;
        if (music!=null) {
            music.setVolume((float) value);
        }
    }

    @Override
    protected void end() {
        super.end();
        value = end;
        if(music!=null) {
            music.setVolume((float) end);
        }
    }

    /**
     * Gets the current int value.
     */
    public double getValue () {
        return value;
    }

    public void setStart(double start) {
        this.start = start;
        this.value = start;
    }

    public void setEnd(double end) {
        this.end = end;
    }

    public void setMusic(Music music) {
        this.music = music;
    }
}
