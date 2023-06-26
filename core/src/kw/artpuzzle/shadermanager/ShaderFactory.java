package kw.artpuzzle.shadermanager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.utils.GdxRuntimeException;

public abstract class ShaderFactory {
    protected static ShaderProgram program;
    protected String vertPath;
    protected String fragPath;

    public ShaderProgram createShderProgram(){
        if (vertPath == null || fragPath == null){
            throw new GdxRuntimeException("vert or frag path is null!");
        }
        program = new ShaderProgram(Gdx.files.internal(vertPath),Gdx.files.internal(fragPath));
        return program;
    }
}
