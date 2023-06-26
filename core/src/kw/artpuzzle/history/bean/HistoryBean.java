package kw.artpuzzle.history.bean;

import com.badlogic.gdx.utils.Array;

public class HistoryBean {
    private Array<String> alreadyPic;
    private int cengshu;

    public void setAlreadyPic(Array<String> alreadyPic) {
        this.alreadyPic = alreadyPic;
    }

    public void setCengshu(int cengshu) {
        this.cengshu = cengshu;
    }

    public Array<String> getAlreadyPic() {
        return alreadyPic;
    }

    public int getCengshu() {
        return cengshu;
    }
}
