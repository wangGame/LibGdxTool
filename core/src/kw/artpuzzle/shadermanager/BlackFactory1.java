package kw.artpuzzle.shadermanager;

public class BlackFactory1 extends ShaderFactory {
   public BlackFactory1(){
      vertPath = "shader/black/vert.vert";
      fragPath = "shader/black/wipeUp.glsl";
//      fragPath = "shader/black/blendDivide.glsl";
   }
}
