package com.tony.dominoes.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.TextInputListener;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.PolygonBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.kw.gdx.BaseGame;
import com.kw.gdx.asset.Asset;
import com.kw.gdx.screen.BaseScreen;
import com.tony.dominoes.mesh.EditableMesh;
import com.tony.dominoes.mesh.MeshEdge;
import com.tony.dominoes.mesh.MeshVertex;
import com.tony.dominoes.mesh.MeshVertexSnapshot;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MeshDemoScreen extends BaseScreen {
    private final EditableMesh mesh = EditableMesh.grid(620.0f, 460.0f, 4, 3);
    private final Vector2 pointerWorld = new Vector2();
    private final Vector2 lastPointerWorld = new Vector2();
    private ShapeRenderer shapes;
    private BitmapFont font;
    private Texture texture;
    private int draggingVertex = -1;
    private int hoverVertex = -1;
    private int selectedVertex = -1;
    private boolean softSelection;
    private boolean deformTexture;
    private float softSelectionRadius = 120.0f;
    private String statusText = "";
    private float originX;
    private float originY;

    public MeshDemoScreen(BaseGame game) {
        super(game);
    }

    @Override
    public void initView() {
        shapes = new ShapeRenderer();
        font = Asset.getAsset().loadBitFont("Ma-B_46.fnt");
        font.setColor(Color.WHITE);
        texture = createDemoTexture();
    }

    @Override
    public void render(float delta) {
        stage.act(delta);
        updateLayout();
        handleInput();

        Gdx.gl.glClearColor(0.055f, 0.060f, 0.066f, 1.0f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        Gdx.gl.glEnable(GL20.GL_BLEND);
        shapes.setProjectionMatrix(stage.getCamera().combined);
        game.getBatch().setProjectionMatrix(stage.getCamera().combined);

        drawBackground();
        drawTexturedMesh();
        drawEdges();
        drawSoftSelectionEffect();
        drawVertices();
        drawText();
    }

    private void updateLayout() {
        float width = stage.getViewport().getWorldWidth();
        float height = stage.getViewport().getWorldHeight();
        originX = (width - 620.0f) * 0.5f;
        originY = (height - 460.0f) * 0.5f - 40.0f;
    }

    private void handleInput() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.R)) {
            mesh.generateGrid(620.0f, 460.0f, 4, 3);
            draggingVertex = -1;
            selectedVertex = -1;
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_1)) {
            mesh.resetRectangle(620.0f, 460.0f);
            draggingVertex = -1;
            selectedVertex = -1;
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_2)) {
            mesh.generateGrid(620.0f, 460.0f, 4, 3);
            draggingVertex = -1;
            selectedVertex = -1;
        }

        screenToWorld(Gdx.input.getX(), Gdx.input.getY(), pointerWorld);
        hoverVertex = nearestVertex(pointerWorld, 28.0f);
        if (Gdx.input.isKeyJustPressed(Input.Keys.S)) {
            softSelection = !softSelection;
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.D)) {
            deformTexture = !deformTexture;
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.LEFT_BRACKET)) {
            softSelectionRadius = Math.max(20.0f, softSelectionRadius - 20.0f);
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.RIGHT_BRACKET)) {
            softSelectionRadius = Math.min(360.0f, softSelectionRadius + 20.0f);
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.P)) {
            openPositionInput();
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.A)) {
            MeshVertex added = addVertexAtPointer();
            if (added != null) {
                selectedVertex = added.id();
                hoverVertex = added.id();
            }
        }
        if ((Gdx.input.isKeyJustPressed(Input.Keys.DEL) || Gdx.input.isKeyJustPressed(Input.Keys.FORWARD_DEL))
                && activeVertex() >= 0) {
            mesh.removeVertex(activeVertex());
            draggingVertex = -1;
            hoverVertex = -1;
            selectedVertex = -1;
        }
        handleKeyboardMove();
        if (Gdx.input.justTouched()) {
            draggingVertex = hoverVertex;
            selectedVertex = hoverVertex;
            lastPointerWorld.set(pointerWorld);
        }
        if (!Gdx.input.isTouched()) {
            draggingVertex = -1;
        }
        if (draggingVertex >= 0) {
            if (softSelection) {
                float dx = pointerWorld.x - lastPointerWorld.x;
                float dy = pointerWorld.y - lastPointerWorld.y;
                if (dx != 0.0f || dy != 0.0f) {
                    mesh.translateSoftSelection(draggingVertex, dx, dy, softSelectionRadius);
                    syncUvToPositionsIfNeeded();
                }
            } else {
                moveVertexTo(draggingVertex, pointerWorld.x - originX, pointerWorld.y - originY);
            }
            lastPointerWorld.set(pointerWorld);
        }
    }

    private MeshVertex addVertexAtPointer() {
        float localX = pointerWorld.x - originX;
        float localY = pointerWorld.y - originY;
        if (localX < 0.0f || localY < 0.0f || localX > 620.0f || localY > 460.0f) {
            return null;
        }
        float u = localX / 620.0f;
        float v = 1.0f - localY / 460.0f;
        return mesh.addVertexBySplittingTriangle(localX, localY, u, v);
    }

    private void handleKeyboardMove() {
        int vertex = activeVertex();
        if (vertex < 0) {
            return;
        }
        float step = Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT) || Gdx.input.isKeyPressed(Input.Keys.SHIFT_RIGHT) ? 10.0f : 2.0f;
        float dx = 0.0f;
        float dy = 0.0f;
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            dx -= step;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            dx += step;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            dy -= step;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            dy += step;
        }
        if (dx == 0.0f && dy == 0.0f) {
            return;
        }
        selectedVertex = vertex;
        if (softSelection) {
            mesh.translateSoftSelection(vertex, dx, dy, softSelectionRadius);
            syncUvToPositionsIfNeeded();
        } else {
            MeshVertexSnapshot snapshot = vertexById(vertex);
            if (snapshot != null) {
                moveVertexTo(vertex, snapshot.x() + dx, snapshot.y() + dy);
            }
        }
    }

    private int activeVertex() {
        if (hoverVertex >= 0) {
            return hoverVertex;
        }
        return selectedVertex;
    }

    private void openPositionInput() {
        final int vertexId = activeVertex();
        final MeshVertexSnapshot vertex = vertexById(vertexId);
        if (vertex == null) {
            statusText = "Select or hover a vertex before pressing P.";
            return;
        }
        selectedVertex = vertexId;
        String current = Math.round(vertex.x()) + "," + Math.round(vertex.y());
        Gdx.input.getTextInput(new TextInputListener() {
            @Override
            public void input(String text) {
                applyPositionInput(vertexId, text);
            }

            @Override
            public void canceled() {
                statusText = "Position edit canceled.";
            }
        }, "Set vertex position", current, "x,y");
    }

    private void applyPositionInput(int vertexId, String text) {
        String[] parts = text == null ? new String[0] : text.trim().split("[,\\s]+");
        if (parts.length != 2) {
            statusText = "Invalid position. Use x,y, for example 110,100.";
            return;
        }
        try {
            float x = Float.parseFloat(parts[0]);
            float y = Float.parseFloat(parts[1]);
            moveVertexTo(vertexId, x, y);
            selectedVertex = vertexId;
            statusText = "Vertex " + vertexId + " moved to " + round(x) + "," + round(y) + ".";
        } catch (NumberFormatException ex) {
            statusText = "Invalid position. Use numbers like 110,100.";
        }
    }

    private void moveVertexTo(int vertexId, float x, float y) {
        mesh.moveVertex(vertexId, x, y);
        syncUvToPositionIfNeeded(vertexId);
    }

    private void syncUvToPositionIfNeeded(int vertexId) {
        if (deformTexture) {
            return;
        }
        MeshVertexSnapshot vertex = vertexById(vertexId);
        if (vertex == null) {
            return;
        }
        mesh.setUv(vertexId, vertex.x() / 620.0f, 1.0f - vertex.y() / 460.0f);
    }

    private void syncUvToPositionsIfNeeded() {
        if (deformTexture) {
            return;
        }
        for (MeshVertexSnapshot vertex : mesh.vertices()) {
            mesh.setUv(vertex.id(), vertex.x() / 620.0f, 1.0f - vertex.y() / 460.0f);
        }
    }

    private void screenToWorld(int screenX, int screenY, Vector2 out) {
        Viewport viewport = stage.getViewport();
        out.set(screenX, screenY);
        viewport.unproject(out);
    }

    private int nearestVertex(Vector2 world, float maxDistance) {
        int nearest = -1;
        float best = maxDistance * maxDistance;
        for (MeshVertexSnapshot vertex : mesh.vertices()) {
            float dx = originX + vertex.x() - world.x;
            float dy = originY + vertex.y() - world.y;
            float distance2 = dx * dx + dy * dy;
            if (distance2 <= best) {
                best = distance2;
                nearest = vertex.id();
            }
        }
        return nearest;
    }

    private void drawBackground() {
        float width = stage.getViewport().getWorldWidth();
        float height = stage.getViewport().getWorldHeight();
        shapes.begin(ShapeRenderer.ShapeType.Filled);
        shapes.setColor(0.055f, 0.060f, 0.066f, 1.0f);
        shapes.rect(0.0f, 0.0f, width, height);
        shapes.setColor(0.090f, 0.098f, 0.106f, 1.0f);
        shapes.rect(originX - 28.0f, originY - 28.0f, 676.0f, 516.0f);
        shapes.end();
    }

    private void drawTexturedMesh() {
        Batch batch = game.getBatch();
        if (!(batch instanceof PolygonBatch)) {
            return;
        }

        float[] polygonVertices = mesh.toPolygonVertices(originX, originY, Color.WHITE_FLOAT_BITS);
        short[] triangleIndices = mesh.toTriangleIndices();

        batch.begin();
        ((PolygonBatch) batch).draw(texture, polygonVertices, 0, polygonVertices.length, triangleIndices, 0, triangleIndices.length);
        batch.end();
    }

    private void drawEdges() {
        Map<Integer, MeshVertexSnapshot> byId = verticesById(mesh.vertices());
        shapes.begin(ShapeRenderer.ShapeType.Line);
        shapes.setColor(1.0f, 1.0f, 1.0f, 0.38f);
        for (MeshEdge edge : mesh.edges()) {
            MeshVertexSnapshot a = byId.get(edge.a());
            MeshVertexSnapshot b = byId.get(edge.b());
            if (a != null && b != null) {
                shapes.line(originX + a.x(), originY + a.y(), originX + b.x(), originY + b.y());
            }
        }
        shapes.end();
    }

    private void drawSoftSelectionEffect() {
        if (!softSelection) {
            return;
        }
        int centerId = draggingVertex >= 0 ? draggingVertex : activeVertex();
        MeshVertexSnapshot center = vertexById(centerId);
        if (center == null) {
            return;
        }

        shapes.begin(ShapeRenderer.ShapeType.Line);
        shapes.setColor(1.0f, 0.82f, 0.24f, 0.55f);
        shapes.circle(originX + center.x(), originY + center.y(), softSelectionRadius);
        shapes.end();

        shapes.begin(ShapeRenderer.ShapeType.Filled);
        Map<Integer, Float> weights = mesh.softSelectionWeights(centerId, softSelectionRadius);
        for (MeshVertexSnapshot vertex : mesh.vertices()) {
            Float weight = weights.get(vertex.id());
            if (weight == null || vertex.id() == centerId) {
                continue;
            }
            shapes.setColor(1.0f, 0.82f, 0.24f, 0.20f + 0.45f * weight);
            shapes.circle(originX + vertex.x(), originY + vertex.y(), 5.0f + 7.0f * weight);
        }
        shapes.end();
    }

    private void drawVertices() {
        shapes.begin(ShapeRenderer.ShapeType.Filled);
        for (MeshVertexSnapshot vertex : mesh.vertices()) {
            if (vertex.id() == draggingVertex) {
                shapes.setColor(1.0f, 0.35f, 0.22f, 1.0f);
                shapes.circle(originX + vertex.x(), originY + vertex.y(), 13.0f);
            } else if (vertex.id() == selectedVertex) {
                shapes.setColor(0.92f, 0.18f, 1.0f, 1.0f);
                shapes.circle(originX + vertex.x(), originY + vertex.y(), 12.0f);
            } else if (vertex.id() == hoverVertex) {
                shapes.setColor(1.0f, 0.78f, 0.26f, 1.0f);
                shapes.circle(originX + vertex.x(), originY + vertex.y(), 11.0f);
            } else {
                shapes.setColor(0.28f, 0.62f, 1.0f, 1.0f);
                shapes.circle(originX + vertex.x(), originY + vertex.y(), 8.0f);
            }
        }
        shapes.end();
    }

    private void drawText() {
        Batch batch = game.getBatch();
        float height = stage.getViewport().getWorldHeight();
        batch.begin();
        font.draw(batch, "Textured mesh demo: drag blue vertices", 36.0f, height - 42.0f);
        font.draw(batch, "A add   Delete remove   P set x,y   arrows move   Shift faster   S soft " + (softSelection ? "on" : "off") + "   D deform " + (deformTexture ? "on" : "off"), 36.0f, height - 76.0f);
        font.draw(batch, "vertices=" + mesh.vertexCount()
                + " edges=" + mesh.edgeCount() + " triangles=" + mesh.triangleCount()
                + " radius=" + (int) softSelectionRadius
                + " selected=" + selectedVertex, 36.0f, height - 110.0f);
        if (statusText != null && statusText.length() > 0) {
            font.draw(batch, statusText, 36.0f, height - 144.0f);
        }
        if (draggingVertex >= 0) {
            font.draw(batch, "dragging vertex " + draggingVertex, 36.0f, 44.0f);
        } else if (hoverVertex >= 0) {
            font.draw(batch, "hover vertex " + hoverVertex, 36.0f, 44.0f);
        }
        batch.end();
    }

    private static Map<Integer, MeshVertexSnapshot> verticesById(List<MeshVertexSnapshot> vertices) {
        Map<Integer, MeshVertexSnapshot> byId = new HashMap<Integer, MeshVertexSnapshot>();
        for (MeshVertexSnapshot vertex : vertices) {
            byId.put(vertex.id(), vertex);
        }
        return byId;
    }

    private MeshVertexSnapshot vertexById(int id) {
        if (id < 0) {
            return null;
        }
        for (MeshVertexSnapshot vertex : mesh.vertices()) {
            if (vertex.id() == id) {
                return vertex;
            }
        }
        return null;
    }

    private static String round(float value) {
        return String.format("%.1f", value);
    }

    private static Texture createDemoTexture() {
//        int size = 256;
//        Pixmap pixmap = new Pixmap(size, size, Pixmap.Format.RGBA8888);
//        pixmap.setColor(0.18f, 0.20f, 0.23f, 1.0f);
//        pixmap.fill();
//
//        for (int y = 0; y < size; y += 32) {
//            for (int x = 0; x < size; x += 32) {
//                boolean bright = ((x + y) / 32 & 1) == 0;
//                if (bright) {
//                    pixmap.setColor(0.28f, 0.34f, 0.42f, 1.0f);
//                } else {
//                    pixmap.setColor(0.12f, 0.16f, 0.20f, 1.0f);
//                }
//                pixmap.fillRectangle(x, y, 32, 32);
//            }
//        }
//
//        pixmap.setColor(0.96f, 0.38f, 0.24f, 1.0f);
//        pixmap.fillRectangle(18, 18, 82, 82);
//        pixmap.setColor(0.22f, 0.62f, 1.0f, 1.0f);
//        pixmap.fillRectangle(156, 24, 76, 92);
//        pixmap.setColor(0.32f, 0.84f, 0.52f, 1.0f);
//        pixmap.fillRectangle(40, 154, 88, 70);
//        pixmap.setColor(1.0f, 0.82f, 0.24f, 1.0f);
//        pixmap.fillCircle(190, 190, 36);
//
//        pixmap.setColor(1.0f, 1.0f, 1.0f, 0.95f);
//        pixmap.drawRectangle(0, 0, size, size);
//        pixmap.drawLine(0, 0, size - 1, size - 1);
//        pixmap.drawLine(0, size - 1, size - 1, 0);
//        pixmap.drawLine(size / 2, 0, size / 2, size - 1);
//        pixmap.drawLine(0, size / 2, size - 1, size / 2);
//
//        Texture texture = new Texture(pixmap);
//        texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
//        pixmap.dispose();
        return Asset.getAsset().getTexture("img.png");
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
        if (texture != null) {
            texture.dispose();
        }
    }
}
