package com.libGdx.test.terrin;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Vector3;

public class GpuHeightMapDemo extends ApplicationAdapter {

    private PerspectiveCamera camera;

    private ShaderProgram shader;

    private Mesh mesh;

    private Texture heightTexture;

    private float yaw = 45;
    private float pitch = -30;

    private float lastX;
    private float lastY;

    @Override
    public void create() {

        initCamera();

        initShader();

        buildMesh();

        heightTexture =
                new Texture("000.png");
    }

    private void initCamera() {

        camera =
                new PerspectiveCamera(
                        67,
                        Gdx.graphics.getWidth(),
                        Gdx.graphics.getHeight()
                );

        camera.near = 0.1f;

        camera.far = 50000f;

        camera.position.set(
                0,
                500,
                600
        );

        camera.lookAt(0,0,0);

        camera.update();
    }

    private void initShader() {

        String vertexShader =

                "attribute vec4 a_position;\n" +
                        "attribute vec4 a_color;\n" +
                        "attribute vec2 a_texCoord0;\n" +

                        "varying vec4 v_color;\n" +
                        "varying vec2 v_textCoords;\n" +

                        "uniform mat4 u_projTrans;\n" +
                        "uniform sampler2D u_texture;\n" +
                        "uniform float u_height;\n" +

                        "vec3 W = vec3(\n" +
                        "0.2125,\n" +
                        "0.7154,\n" +
                        "0.0721);\n" +

                        "void main(){\n" +

                        "v_color = a_color;\n" +

                        "v_textCoords = a_texCoord0;\n" +

                        "vec4 tex = texture2D(\n" +
                        "u_texture,\n" +
                        "a_texCoord0);\n" +

                        "float luminance =\n" +
                        "dot(tex.rgb,W);\n" +

                        "float h =\n" +
                        "(luminance - 0.5)\n" +
                        "* u_height;\n" +

                        "vec4 pos = a_position;\n" +

                        "pos.y += h;\n" +

                        "gl_Position =\n" +
                        "u_projTrans * pos;\n" +

                        "}";

        String fragmentShader =

                "#ifdef GL_ES\n" +
                        "precision mediump float;\n" +
                        "#endif\n" +

                        "varying vec4 v_color;\n" +
                        "varying vec2 v_textCoords;\n" +

                        "uniform sampler2D u_texture;\n" +

                        "void main(){\n" +

                        "vec4 texColor =\n" +
                        "texture2D(\n" +
                        "u_texture,\n" +
                        "v_textCoords);\n" +

                        "gl_FragColor =\n" +
                        "texColor * v_color;\n" +

                        "}";

        shader =
                new ShaderProgram(
                        vertexShader,
                        fragmentShader
                );

        if (!shader.isCompiled()) {

            throw new RuntimeException(
                    shader.getLog()
            );
        }
    }

    private void buildMesh() {

        final int SIZE = 256;

        int vertexCount =
                SIZE * SIZE;

        int quadCount =
                (SIZE - 1) *
                        (SIZE - 1);

        int indexCount =
                quadCount * 6;

        float[] vertices =
                new float[vertexCount * 9];

        short[] indices =
                new short[indexCount];

        int v = 0;

        for(int z = 0; z < SIZE; z++){

            for(int x = 0; x < SIZE; x++){

                float fx =
                        x * 4f;

                float fz =
                        z * 4f;

                float u =
                        x / (float)(SIZE - 1);

                float vv =
                        z / (float)(SIZE - 1);

                // position
                vertices[v++] = fx;
                vertices[v++] = 0;
                vertices[v++] = fz;

                // color
                vertices[v++] = 1;
                vertices[v++] = 1;
                vertices[v++] = 1;
                vertices[v++] = 1;

                // uv
                vertices[v++] = u;
                vertices[v++] = vv;
            }
        }

        int i = 0;

        for(int z = 0; z < SIZE - 1; z++){

            for(int x = 0; x < SIZE - 1; x++){

                short topLeft =
                        (short)(
                                z * SIZE + x
                        );

                short topRight =
                        (short)(
                                topLeft + 1
                        );

                short bottomLeft =
                        (short)(
                                (z + 1)
                                        * SIZE + x
                        );

                short bottomRight =
                        (short)(
                                bottomLeft + 1
                        );

                indices[i++] = topLeft;
                indices[i++] = topRight;
                indices[i++] = bottomLeft;

                indices[i++] = topRight;
                indices[i++] = bottomRight;
                indices[i++] = bottomLeft;
            }
        }

        mesh =
                new Mesh(
                        true,
                        vertexCount,
                        indexCount,

                        new VertexAttribute(
                                VertexAttributes.Usage.Position,
                                3,
                                "a_position"
                        ),

                        new VertexAttribute(
                                VertexAttributes.Usage.ColorUnpacked,
                                4,
                                "a_color"
                        ),

                        new VertexAttribute(
                                VertexAttributes.Usage.TextureCoordinates,
                                2,
                                "a_texCoord0"
                        )
                );

        mesh.setVertices(vertices);

        mesh.setIndices(indices);
    }

    private void updateCamera(){

        float delta =
                Gdx.graphics.getDeltaTime();

        float speed =
                200f * delta;

        Vector3 forward =
                camera.direction.cpy();

        forward.y = 0;

        forward.nor();

        Vector3 right =
                forward.cpy()
                        .crs(Vector3.Y)
                        .nor();

        if(Gdx.input.isKeyPressed(Input.Keys.W)){

            camera.position.add(
                    forward.cpy()
                            .scl(speed)
            );
        }

        if(Gdx.input.isKeyPressed(Input.Keys.S)){

            camera.position.sub(
                    forward.cpy()
                            .scl(speed)
            );
        }

        if(Gdx.input.isKeyPressed(Input.Keys.A)){

            camera.position.sub(
                    right.cpy()
                            .scl(speed)
            );
        }

        if(Gdx.input.isKeyPressed(Input.Keys.D)){

            camera.position.add(
                    right.cpy()
                            .scl(speed)
            );
        }

        if(Gdx.input.isButtonPressed(
                Input.Buttons.LEFT
        )){

            float mx =
                    Gdx.input.getX();

            float my =
                    Gdx.input.getY();

            float dx =
                    mx - lastX;

            float dy =
                    my - lastY;

            yaw -= dx * 0.2f;

            pitch -= dy * 0.2f;

            pitch =
                    Math.max(
                            -89,
                            Math.min(89,pitch)
                    );
        }

        lastX =
                Gdx.input.getX();

        lastY =
                Gdx.input.getY();

        Vector3 dir =
                new Vector3();

        dir.x =
                (float)(
                        Math.cos(
                                Math.toRadians(pitch)
                        ) *
                                Math.sin(
                                        Math.toRadians(yaw)
                                )
                );

        dir.y =
                (float)Math.sin(
                        Math.toRadians(pitch)
                );

        dir.z =
                (float)(
                        Math.cos(
                                Math.toRadians(pitch)
                        ) *
                                Math.cos(
                                        Math.toRadians(yaw)
                                )
                );

        camera.direction
                .set(dir)
                .nor();

        camera.update();
    }

    @Override
    public void render() {

        updateCamera();

        Gdx.gl.glViewport(
                0,
                0,
                Gdx.graphics.getWidth(),
                Gdx.graphics.getHeight()
        );

        Gdx.gl.glClearColor(
                0.1f,
                0.1f,
                0.15f,
                1f
        );

        Gdx.gl.glClear(
                GL20.GL_COLOR_BUFFER_BIT
                        |
                        GL20.GL_DEPTH_BUFFER_BIT
        );

        Gdx.gl.glEnable(
                GL20.GL_DEPTH_TEST
        );

        heightTexture.bind(0);

        shader.begin();

        shader.setUniformi(
                "u_texture",
                0
        );

        shader.setUniformf(
                "u_height",
                80f
        );

        shader.setUniformMatrix(
                "u_projTrans",
                camera.combined
        );

        mesh.render(
                shader,
                GL20.GL_TRIANGLES
        );

        shader.end();
    }

    @Override
    public void dispose() {

        mesh.dispose();

        shader.dispose();

        heightTexture.dispose();
    }

    public static void main(String[] args) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.x = 1000;
        config.stencil=8;
        config.y = 0;
        config.height = (int) (1000);
        config.width = (int) (1000);
        new LwjglApplication(new GpuHeightMapDemo(), config);
    }
}