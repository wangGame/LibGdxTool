package kw.artpuzzle.shadermanager;

public class BrightNessFactory extends ShaderFactory{
   public BrightNessFactory(){
      vertPath = "useshader/middlespread.vert";
      fragPath = "useshader/brightness.glsl";

//      vertPath = "shader/black/vert.vert";
//      fragPath = "shader/black/wipeUp3.glsl";

   }
}
