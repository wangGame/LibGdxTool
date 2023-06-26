package kw.artpuzzle.shadermanager;

import com.badlogic.gdx.graphics.glutils.ShaderProgram;

import java.util.HashMap;

public class ShaderManager {
   private static ShaderManager manager;
   private HashMap<ShaderType, ShaderProgram> cacheProgram;
   private ShaderManager(){
      cacheProgram = new HashMap<>();
   }

   public static ShaderManager getManager() {
      if (manager == null){
         manager = new ShaderManager();
      }
      return manager;
   }

   public ShaderProgram getType(ShaderType shaderType){
      if (cacheProgram.containsKey(shaderType))return cacheProgram.get(shaderType);
      ShaderProgram program = null;
      ShaderFactory factory = null;
      if (shaderType == ShaderType.MiddleSpread){
         factory = new MiddleSpreadFactory();
      }else if (shaderType == ShaderType.MiddleSpreadShow){
         factory = new MiddleSpreadShowFactory();
      }else if (shaderType == ShaderType.outSpreadMiddle){
         factory = new OutSpreadMiddleFactory();
      }else if (shaderType == ShaderType.levelSpriteUse){
         factory = new OtherFactory();
      }else if (shaderType == ShaderType.BrightNess){
         factory = new BrightNessFactory();
      }else if (shaderType == ShaderType.line){
         factory = new LineShaderFactory();
      }else if (shaderType == ShaderType.line1){
         factory = new LineShaderFactory1();
      }else if (shaderType == ShaderType.Hui){
         factory = new HuiShaderFactory();
      }else if (shaderType == ShaderType.Hui2){
         factory = new HuiShaderFactory2();
      }else if (shaderType == ShaderType.Hui3){
         factory = new HuiShaderFactory3();
      }else if (shaderType == ShaderType.Expose){
         factory = new ExposureShader();
      }else if (shaderType == ShaderType.black1){
         factory = new BlackFactory1();
      }else if (shaderType == ShaderType.black2){
         factory = new BlackFactory2();
      }
      if (factory!=null) {
         program = factory.createShderProgram();
         cacheProgram.put(shaderType, program);
      }
      return program;
   }
}
