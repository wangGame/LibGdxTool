//package com.libGdx.test.other;
//
//import com.badlogic.gdx.ApplicationListener;
//import com.badlogic.gdx.Gdx;
//import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
//import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
//import com.badlogic.gdx.graphics.GL20;
//import com.badlogic.gdx.graphics.Texture;
//import com.badlogic.gdx.graphics.glutils.ImmediateModeRenderer20;
//import com.badlogic.gdx.graphics.glutils.ShaderProgram;
//import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
//import com.badlogic.gdx.math.Matrix4;
//import com.badlogic.gdx.math.Vector2;
//import com.badlogic.gdx.utils.Array;
//
///**
// * @Auther jian xian si qi
// * @Date 2023/6/28 15:18
// */
//public class Other implements ApplicationListener {
//
//    public static void main(String[] args) {
//        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
//        config.x = 1000;
//        config.y = 0;
//        config.height = (int) (900 );
//        config.width = (int) (980  );
//
//        Other dekstop = new Other();
//        new LwjglApplication(dekstop, config);
//    }
//
//    //@off
//    String vertexShaderCode =
//            "#define highp\n" +
//                    "attribute highp vec4 a_position; \n" +
//                    "attribute highp vec2 a_texCoord0;" +
//                    "uniform highp mat4 u_projModelView;" +
//                    "varying highp vec2 v_texCoord0;" +
//                    "void main() \n" +
//                    "{ \n" +
//                    " gl_Position = u_projModelView * a_position; \n" +
//                    " v_texCoord0 = a_texCoord0;\n" +
//                    "} \n";
//
//    String fragmentShaderCode =
//                    "uniform sampler2D u_sampler0;" +
//                    "varying vec2 v_texCoord0;" +
//                    "void main()                 \n" +
//                    "{                           \n" +
//                    "  gl_FragColor = texture2D(u_sampler0, v_texCoord0);    \n" +
//                    "}";
//    //@on
//    private ShapeRenderer renderer1;
//    private ShaderProgram shader;
//    private Matrix4 transform;
//    private Texture frame;
//    private ImmediateModeRenderer20 renderer;
//
//    private Array<Vector2> curve;
//
//    public void create() {
//        renderer1 = new ShapeRenderer();
//        shader = new ShaderProgram(vertexShaderCode, fragmentShaderCode);
//        renderer = new ImmediateModeRenderer20(500000, false, false, 1);
//        renderer.setShader(shader);
//        transform = new Matrix4().setToOrtho2D(0, 0f, 1, 1);
//        frame = new Texture("banner.png");
//        curve = renderer.curve(0, 0, 100.2f, 100.2f, 300f, 200f, 500f, 100f, 100);
//
//        ImmediateModeRenderer20 renderer = (ImmediateModeRenderer20) renderer1.getRenderer();
//        float[] vertices = renderer.getVertices();
//        curve.clear();
//        int ii = 0;
//
////        curve.clear();
////        curve.add(new Vector2(0,0));
////        curve.add(new Vector2(1,1));
////        curve.add(new Vector2(1,0));
//
//    }
//
//    @Override
//    public void resize(int width, int height) {
//
//    }
//
//    @Override
//    public void render() {
//        Gdx.gl.glClearColor(0, 0, 0, 1);
//        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
//
////        shader.begin();
////        frame.bind();
////        Gdx.gl.glActiveTexture(GL20.GL_TEXTURE0);
////        shader.setUniformi("u_sampler0", 0);
////        renderer.begin(transform, GL20.GL_TRIANGLE_STRIP);
//////        for (Vector2 vector2 : curve) {
//////            renderer.texCoord(vector2.x, vector2.y);
//////            renderer.vertex(vector2.x, vector2.y, 0);
//////            System.out.println(vector2.x+"------------"+vector2.y);
//////        }
////        Color color = Color.BLUE;
////        float colorBits = color.toFloatBits();
////        float x = 0;
////        float y = 0;
////        float z = 0;
////        float width = 0.9f;
////        float height = 0.5f;
////        float depth = 0;
////        // Front
////        renderer.color(colorBits);
////        renderer.vertex(x, y, z);
////        renderer.color(colorBits);
////        renderer.vertex(x + width, y, z);
////        renderer.color(colorBits);
////        renderer.vertex(x + width, y + height, z);
////
////        renderer.color(colorBits);
////        renderer.vertex(x, y, z);
////        renderer.color(colorBits);
////        renderer.vertex(x + width, y + height, z);
////        renderer.color(colorBits);
////        renderer.vertex(x, y + height, z);
////
////        // Back
////        renderer.color(colorBits);
////        renderer.vertex(x + width, y, z + depth);
////        renderer.color(colorBits);
////        renderer.vertex(x, y, z + depth);
////        renderer.color(colorBits);
////        renderer.vertex(x + width, y + height, z + depth);
////
////        renderer.color(colorBits);
////        renderer.vertex(x, y + height, z + depth);
////        renderer.color(colorBits);
////        renderer.vertex(x, y, z + depth);
////        renderer.color(colorBits);
////        renderer.vertex(x + width, y + height, z + depth);
////
////        // Left
////        renderer.color(colorBits);
////        renderer.vertex(x, y, z + depth);
////        renderer.color(colorBits);
////        renderer.vertex(x, y, z);
////        renderer.color(colorBits);
////        renderer.vertex(x, y + height, z);
////
////        renderer.color(colorBits);
////        renderer.vertex(x, y, z + depth);
////        renderer.color(colorBits);
////        renderer.vertex(x, y + height, z);
////        renderer.color(colorBits);
////        renderer.vertex(x, y + height, z + depth);
////
////        // Right
////        renderer.color(colorBits);
////        renderer.vertex(x + width, y, z);
////        renderer.color(colorBits);
////        renderer.vertex(x + width, y, z + depth);
////        renderer.color(colorBits);
////        renderer.vertex(x + width, y + height, z + depth);
////
////        renderer.color(colorBits);
////        renderer.vertex(x + width, y, z);
////        renderer.color(colorBits);
////        renderer.vertex(x + width, y + height, z + depth);
////        renderer.color(colorBits);
////        renderer.vertex(x + width, y + height, z);
////
////        // Top
////        renderer.color(colorBits);
////        renderer.vertex(x, y + height, z);
////        renderer.color(colorBits);
////        renderer.vertex(x + width, y + height, z);
////        renderer.color(colorBits);
////        renderer.vertex(x + width, y + height, z + depth);
////
////        renderer.color(colorBits);
////        renderer.vertex(x, y + height, z);
////        renderer.color(colorBits);
////        renderer.vertex(x + width, y + height, z + depth);
////        renderer.color(colorBits);
////        renderer.vertex(x, y + height, z + depth);
////
////        // Bottom
////        renderer.color(colorBits);
////        renderer.vertex(x, y, z + depth);
////        renderer.color(colorBits);
////        renderer.vertex(x + width, y, z + depth);
////        renderer.color(colorBits);
////        renderer.vertex(x + width, y, z);
////
////        renderer.color(colorBits);
////        renderer.vertex(x, y, z + depth);
////        renderer.color(colorBits);
////        renderer.vertex(x + width, y, z);
////        renderer.color(colorBits);
////        renderer.vertex(x, y, z);
////
////        renderer.end();
//
//
//
//
//
//        renderer1.begin(ShapeRenderer.ShapeType.Filled);
//        renderer1.curve(0,0,100.0f,100.f,300,200,300,100,100);
//        renderer1.end();
//    }
//
//    @Override
//    public void pause () {
//
//    }
//
//    @Override
//    public void resume () {
//
//    }
//
//    @Override
//    public void dispose () {
//    }
//}
