package com.tony.dominoes.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.PolygonBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.kw.gdx.BaseGame;
import com.kw.gdx.asset.Asset;
import com.kw.gdx.screen.BaseScreen;
import com.tony.dominoes.mesh.EditableMesh;
import com.tony.dominoes.mesh.MeshEdge;
import com.tony.dominoes.mesh.MeshTracer;
import com.tony.dominoes.mesh.MeshVertexSnapshot;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TraceMeshDemoScreen extends BaseScreen {
    private static final float MESH_WIDTH = 560.0f;
    private static final float MESH_HEIGHT = 560.0f;

    private ShapeRenderer shapes;
    private BitmapFont font;
    private Pixmap sourcePixmap;
    private Texture sourceTexture;
    private EditableMesh mesh;
    private int samples = 48;
    private int alphaThreshold = 16;
    private float originX;
    private float originY;

    public TraceMeshDemoScreen(BaseGame game) {
        super(game);
    }

    @Override
    public void initView() {
        shapes = new ShapeRenderer();
        font = Asset.getAsset().loadBitFont("Ma-B_46.fnt");
        font.setColor(Color.WHITE);
        sourcePixmap = createTraceSourcePixmap();
        sourcePixmap = Asset.getAsset().getPixmap("trace/test.png");
        sourceTexture = new Texture(sourcePixmap);
        sourceTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        trace();
    }

    @Override
    public void render(float delta) {
        stage.act(delta);
        updateLayout();
        handleInput();

        Gdx.gl.glClearColor(0.050f, 0.055f, 0.062f, 1.0f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        Gdx.gl.glEnable(GL20.GL_BLEND);
        shapes.setProjectionMatrix(stage.getCamera().combined);
        game.getBatch().setProjectionMatrix(stage.getCamera().combined);

        drawBackground();
        drawTracedTextureMesh();
        drawEdges();
        drawVertices();
        drawText();
    }

    private void updateLayout() {
        float width = stage.getViewport().getWorldWidth();
        float height = stage.getViewport().getWorldHeight();
        originX = (width - MESH_WIDTH) * 0.5f;
        originY = (height - MESH_HEIGHT) * 0.5f - 40.0f;
    }

    private void handleInput() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.T) || Gdx.input.isKeyJustPressed(Input.Keys.R)) {
            trace();
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.LEFT_BRACKET)) {
            samples = Math.max(8, samples - 8);
            trace();
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.RIGHT_BRACKET)) {
            samples = Math.min(128, samples + 8);
            trace();
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.MINUS)) {
            alphaThreshold = Math.max(0, alphaThreshold - 8);
            trace();
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.EQUALS)) {
            alphaThreshold = Math.min(255, alphaThreshold + 8);
            trace();
        }
    }

    private void trace() {
        mesh = MeshTracer.traceAlphaRadial(sourcePixmap, samples, alphaThreshold, MESH_WIDTH, MESH_HEIGHT);
    }

    private void drawBackground() {
        float width = stage.getViewport().getWorldWidth();
        float height = stage.getViewport().getWorldHeight();
        shapes.begin(ShapeRenderer.ShapeType.Filled);
        shapes.setColor(0.050f, 0.055f, 0.062f, 1.0f);
        shapes.rect(0.0f, 0.0f, width, height);
        shapes.setColor(0.090f, 0.098f, 0.106f, 1.0f);
        shapes.rect(originX - 30.0f, originY - 30.0f, MESH_WIDTH + 60.0f, MESH_HEIGHT + 60.0f);
        shapes.end();
    }

    private void drawTracedTextureMesh() {
        Batch batch = game.getBatch();
        if (!(batch instanceof PolygonBatch)) {
            return;
        }
        float[] polygonVertices = mesh.toPolygonVertices(originX, originY, Color.WHITE_FLOAT_BITS);
        short[] triangleIndices = mesh.toTriangleIndices();

        batch.begin();
        ((PolygonBatch) batch).draw(sourceTexture, polygonVertices, 0, polygonVertices.length, triangleIndices, 0,
                triangleIndices.length);
        batch.end();
    }

    private void drawEdges() {
        Map<Integer, MeshVertexSnapshot> byId = verticesById(mesh.vertices());
        shapes.begin(ShapeRenderer.ShapeType.Line);
        shapes.setColor(1.0f, 1.0f, 1.0f, 0.50f);
        for (MeshEdge edge : mesh.edges()) {
            MeshVertexSnapshot a = byId.get(edge.a());
            MeshVertexSnapshot b = byId.get(edge.b());
            if (a != null && b != null) {
                shapes.line(originX + a.x(), originY + a.y(), originX + b.x(), originY + b.y());
            }
        }
        shapes.end();
    }

    private void drawVertices() {
        shapes.begin(ShapeRenderer.ShapeType.Filled);
        int index = 0;
        for (MeshVertexSnapshot vertex : mesh.vertices()) {
            if (index++ == 0) {
                shapes.setColor(0.95f, 0.22f, 0.28f, 1.0f);
                shapes.circle(originX + vertex.x(), originY + vertex.y(), 8.0f);
            } else {
                shapes.setColor(0.20f, 0.72f, 1.0f, 1.0f);
                shapes.circle(originX + vertex.x(), originY + vertex.y(), 5.0f);
            }
        }
        shapes.end();
    }

    private void drawText() {
        Batch batch = game.getBatch();
        float height = stage.getViewport().getWorldHeight();
        batch.begin();
        font.draw(batch, "Mesh Trace demo: alpha outline to mesh", 36.0f, height - 42.0f);
        font.draw(batch, "T/R retrace   [ ] samples   - + alpha threshold", 36.0f, height - 76.0f);
        font.draw(batch, "samples=" + samples
                + " alpha=" + alphaThreshold
                + " vertices=" + mesh.vertexCount()
                + " triangles=" + mesh.triangleCount(), 36.0f, height - 110.0f);
        batch.end();
    }

    private static Map<Integer, MeshVertexSnapshot> verticesById(List<MeshVertexSnapshot> vertices) {
        Map<Integer, MeshVertexSnapshot> byId = new HashMap<Integer, MeshVertexSnapshot>();
        for (MeshVertexSnapshot vertex : vertices) {
            byId.put(vertex.id(), vertex);
        }
        return byId;
    }

    private static Pixmap createTraceSourcePixmap() {
        int size = 256;
        Pixmap pixmap = new Pixmap(size, size, Pixmap.Format.RGBA8888);
        pixmap.setColor(0.0f, 0.0f, 0.0f, 0.0f);
        pixmap.fill();

        pixmap.setColor(0.95f, 0.30f, 0.22f, 1.0f);
        fillStar(pixmap, 128, 128, 104, 52, 18);
        pixmap.setColor(1.0f, 0.82f, 0.20f, 1.0f);
        pixmap.fillCircle(128, 128, 42);
        pixmap.setColor(0.12f, 0.20f, 0.32f, 1.0f);
        pixmap.fillCircle(112, 116, 10);
        pixmap.fillCircle(146, 116, 10);
        pixmap.setColor(0.12f, 0.20f, 0.32f, 1.0f);
        pixmap.fillRectangle(104, 148, 48, 8);
        return pixmap;
    }

    private static void fillStar(Pixmap pixmap, int centerX, int centerY, float outerRadius, float innerRadius, int points) {
        float previousX = centerX + outerRadius;
        float previousY = centerY;
        for (int i = 1; i <= points; i++) {
            float radius = (i & 1) == 0 ? outerRadius : innerRadius;
            double angle = Math.PI * 2.0 * i / points;
            float x = centerX + (float) Math.cos(angle) * radius;
            float y = centerY + (float) Math.sin(angle) * radius;
            pixmap.fillTriangle(centerX, centerY, Math.round(previousX), Math.round(previousY), Math.round(x), Math.round(y));
            previousX = x;
            previousY = y;
        }
    }

    @Override
    public void dispose() {
        super.dispose();
        if (shapes != null) {
            shapes.dispose();
        }
        if (font != null) {
            font.dispose();
        }
        if (sourceTexture != null) {
            sourceTexture.dispose();
        }
        if (sourcePixmap != null) {
            sourcePixmap.dispose();
        }
    }
}
