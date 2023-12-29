package com.libGdx.test.other;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.glutils.ImmediateModeRenderer;
import com.badlogic.gdx.graphics.glutils.ImmediateModeRenderer20;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

/**
 *
 * 移动到Gdx-test
 *
 * @Auther jian xian si qi
 * @Date 2023/6/28 15:19
 */
public class ImmediateModeRenderer2001 implements ImmediateModeRenderer {
    private int primitiveType;
    private int vertexIdx;
    private final int maxVertices;
    private int numVertices;
    private final Mesh mesh;
    private ShaderProgram shader;
    private boolean ownsShader;
    private final int vertexSize;
    private final int colorOffset;
    private final Matrix4 projModelView = new Matrix4();
    private final float[] vertices;

    public ImmediateModeRenderer2001 (int maxVertices, boolean hasNormals, boolean hasColors, int numTexCoords) {
        this(maxVertices,createDefaultShader(hasNormals, hasColors, numTexCoords));
        ownsShader = true;
    }

    public ImmediateModeRenderer2001 (int maxVertices, ShaderProgram shader) {
        this.maxVertices = maxVertices;
        this.shader = shader;
        VertexAttribute[] attribs = buildVertexAttributes();
        mesh = new Mesh(false, maxVertices, 0, attribs);
        vertices = new float[maxVertices * (mesh.getVertexAttributes().vertexSize / 4)];
        vertexSize = mesh.getVertexAttributes().vertexSize / 4;
        colorOffset = mesh.getVertexAttribute(VertexAttributes.Usage.ColorPacked) != null ? mesh.getVertexAttribute(VertexAttributes.Usage.ColorPacked).offset / 4 : 0;
    }

    private VertexAttribute[] buildVertexAttributes () {
        Array<VertexAttribute> attribs = new Array<VertexAttribute>();
        attribs.add(new VertexAttribute(VertexAttributes.Usage.Position, 3, ShaderProgram.POSITION_ATTRIBUTE));
        VertexAttribute[] array = new VertexAttribute[attribs.size];
        for (int i = 0; i < attribs.size; i++) {
            array[i] = attribs.get(i);
        }
        return array;
    }

    public void begin (Matrix4 projModelView, int primitiveType) {
        this.projModelView.set(projModelView);
        this.primitiveType = primitiveType;
    }

    public void color (Color color) {
        vertices[vertexIdx + colorOffset] = color.toFloatBits();
    }

    public void color (float r, float g, float b, float a) {
        vertices[vertexIdx + colorOffset] = Color.toFloatBits(r, g, b, a);
    }

    public void color (float colorBits) {
        vertices[vertexIdx + colorOffset] = colorBits;
    }

    @Override
    public void texCoord(float u, float v) {

    }


    @Override
    public void normal(float x, float y, float z) {

    }

    public void vertex (float x, float y, float z) {
        final int idx = vertexIdx;
        vertices[idx] = x;
        vertices[idx + 1] = y;
        vertices[idx + 2] = z;

        texCoord(x,y);
        vertexIdx += vertexSize;
        numVertices++;
    }

    public void flush () {
        if (numVertices == 0) return;
        shader.begin();
        shader.setUniformMatrix("u_projModelView", projModelView);
        mesh.setVertices(vertices, 0, vertexIdx);
        mesh.render(shader, primitiveType);
        shader.end();
        vertexIdx = 0;
        numVertices = 0;
    }

    public void end () {
        flush();
    }

    public int getNumVertices () {
        return numVertices;
    }

    @Override
    public int getMaxVertices () {
        return maxVertices;
    }

    public void dispose () {
        if (ownsShader && shader != null) shader.dispose();
        mesh.dispose();
    }

    static private String createVertexShader (boolean hasNormals, boolean hasColors, int numTexCoords) {
        String shader = "attribute vec4 " + ShaderProgram.POSITION_ATTRIBUTE+";";
        shader += "uniform mat4 u_projModelView;\n";
        shader += "void main() {\n" + "   gl_Position = u_projModelView * " + ShaderProgram.POSITION_ATTRIBUTE + ";\n";
        shader += "   gl_PointSize = 1.0;\n";
        shader += "}\n";
        return shader;
    }

    static private String createFragmentShader () {
        String shader = "#ifdef GL_ES\n" + "precision mediump float;\n" + "#endif\n";
        shader += "void main() {\n" + "   gl_FragColor = " +  "vec4(1, 1, 1, 1)";
        shader += ";\n}";
        return shader;
    }

    /** Returns a new instance of the default shader used by SpriteBatch for GL2 when no shader is specified. */
    static public ShaderProgram createDefaultShader (boolean hasNormals, boolean hasColors, int numTexCoords) {
        String vertexShader = createVertexShader(hasNormals, hasColors, numTexCoords);
        String fragmentShader = createFragmentShader();
        ShaderProgram program = new ShaderProgram(vertexShader, fragmentShader);
        return program;
    }
}