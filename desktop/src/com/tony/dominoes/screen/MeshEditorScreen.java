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
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.kw.gdx.BaseGame;
import com.kw.gdx.asset.Asset;
import com.kw.gdx.screen.BaseScreen;
import com.tony.dominoes.mesh.EditableMesh;
import com.tony.dominoes.mesh.LinkedEditableMesh;
import com.tony.dominoes.mesh.MeshAttachmentModel;
import com.tony.dominoes.mesh.MeshBounds;
import com.tony.dominoes.mesh.MeshEditMode;
import com.tony.dominoes.mesh.MeshEditToolState;
import com.tony.dominoes.mesh.MeshEditorScene;
import com.tony.dominoes.mesh.MeshEdge;
import com.tony.dominoes.mesh.MeshGenerateSettings;
import com.tony.dominoes.mesh.MeshTraceSettings;
import com.tony.dominoes.mesh.MeshTracer;
import com.tony.dominoes.mesh.MeshTriangle;
import com.tony.dominoes.mesh.MeshVertexSnapshot;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MeshEditorScreen extends BaseScreen {
    private static final float MESH_WIDTH = 620.0f;
    private static final float MESH_HEIGHT = 460.0f;

    private final MeshEditorScene editorScene = new MeshEditorScene();
    private final MeshEditToolState toolState = new MeshEditToolState();
    private final Vector2 pointerWorld = new Vector2();
    private final Vector2 lastPointerWorld = new Vector2();
    private ShapeRenderer shapes;
    private BitmapFont font;
    private Texture texture;
    private Pixmap tracePixmap;
    private int hoverVertex = -1;
    private int selectedVertex = -1;
    private int draggingVertex = -1;
    private float originX;
    private float originY;
    private String statusText = "";

    public MeshEditorScreen(BaseGame game) {
        super(game);
    }

    @Override
    public void initView() {
        shapes = new ShapeRenderer();
        font = Asset.getAsset().loadBitFont("Ma-B_46.fnt");
        font.setColor(Color.WHITE);
        texture = Asset.getAsset().getTexture("img.png");
        tracePixmap = Asset.getAsset().getPixmap("dikuai2.png");

        EditableMesh sourceMesh = EditableMesh.grid(MESH_WIDTH, MESH_HEIGHT, 4, 3);
        MeshAttachmentModel source = editorScene.addAttachment("source", sourceMesh);
        MeshAttachmentModel linked = editorScene.addAttachment("linked", sourceMesh.copy());
        linked.createLinkedMesh(source.mesh(), true);
        editorScene.setCurrentAttachment(0);
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
        drawAttachments();
        drawText();
    }

    private void updateLayout() {
        float width = stage.getViewport().getWorldWidth();
        float height = stage.getViewport().getWorldHeight();
        originX = (width - MESH_WIDTH) * 0.5f;
        originY = (height - MESH_HEIGHT) * 0.5f - 42.0f;
    }

    private void handleInput() {
        screenToWorld(Gdx.input.getX(), Gdx.input.getY(), pointerWorld);
        MeshAttachmentModel current = currentAttachment();
        EditableMesh mesh = current.mesh();
        hoverVertex = nearestVertex(mesh, pointerWorld, 26.0f);

        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_1)) setMode(MeshEditMode.MODIFY);
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_2)) setMode(MeshEditMode.CREATE);
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_3)) setMode(MeshEditMode.DELETE);
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_4)) newMesh();
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_5)) selectAttachment(0);
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_6)) selectAttachment(1);

        if (Gdx.input.isKeyJustPressed(Input.Keys.R)) resetToCorners();
        if (Gdx.input.isKeyJustPressed(Input.Keys.G)) generateVertices();
        if (Gdx.input.isKeyJustPressed(Input.Keys.H)) traceImage();
        if (Gdx.input.isKeyJustPressed(Input.Keys.F)) freezeCurrentTransform();
        if (Gdx.input.isKeyJustPressed(Input.Keys.BACKSPACE)) resetDeformation();
        if (Gdx.input.isKeyJustPressed(Input.Keys.W)) toggleWireframe();
        if (Gdx.input.isKeyJustPressed(Input.Keys.T)) toggleTriangles();
        if (Gdx.input.isKeyJustPressed(Input.Keys.D)) toggleDim();
        if (Gdx.input.isKeyJustPressed(Input.Keys.I)) toggleIsolate();
        if (Gdx.input.isKeyJustPressed(Input.Keys.E)) toggleDeformed();
        if (Gdx.input.isKeyJustPressed(Input.Keys.L)) createLinkedAttachment();
        if (Gdx.input.isKeyJustPressed(Input.Keys.U)) unlinkCurrentAttachment();
        if (Gdx.input.isKeyJustPressed(Input.Keys.O)) toggleInheritDeform();

        if (Gdx.input.justTouched()) {
            if (toolState.mode() == MeshEditMode.CREATE) {
                addVertexAtPointer(mesh);
            } else if (toolState.mode() == MeshEditMode.DELETE) {
                deleteAtPointer(mesh);
            } else if (toolState.mode() == MeshEditMode.MODIFY) {
                draggingVertex = hoverVertex;
                selectedVertex = hoverVertex;
                lastPointerWorld.set(pointerWorld);
            }
        }

        if (!Gdx.input.isTouched()) {
            draggingVertex = -1;
        }
        if (draggingVertex >= 0 && toolState.mode() == MeshEditMode.MODIFY) {
            float localX = pointerWorld.x - originX;
            float localY = pointerWorld.y - originY;
            if (toolState.deformed()) {
                mesh.deformVertex(draggingVertex, localX, localY);
            } else {
                mesh.moveSetupVertex(draggingVertex, localX, localY);
            }
            lastPointerWorld.set(pointerWorld);
        }
    }

    private void setMode(MeshEditMode mode) {
        toolState.setMode(mode);
        statusText = "Mode: " + mode;
    }

    private void newMesh() {
        EditableMesh mesh = currentAttachment().localMesh();
        mesh.clear();
        MeshVertexSnapshot a = mesh.addVertex(0, 0, 0, 1).snapshot();
        MeshVertexSnapshot b = mesh.addVertex(MESH_WIDTH, 0, 1, 1).snapshot();
        mesh.addEdge(a.id(), b.id());
        toolState.setMode(MeshEditMode.NEW);
        statusText = "New mesh mode: create edge vertices.";
    }

    private void selectAttachment(int index) {
        if (index >= editorScene.attachments().size()) {
            return;
        }
        editorScene.setCurrentAttachment(index);
        selectedVertex = -1;
        draggingVertex = -1;
        statusText = "Selected attachment " + index + ": " + currentAttachment().name();
    }

    private void resetToCorners() {
        currentAttachment().localMesh().resetToCorners(MESH_WIDTH, MESH_HEIGHT);
        statusText = "Reset mesh to four corners.";
    }

    private void generateVertices() {
        MeshGenerateSettings settings = new MeshGenerateSettings();
        settings.setColumns(5);
        settings.setRows(4);
        currentAttachment().localMesh().generateVertices(MESH_WIDTH, MESH_HEIGHT, settings);
        statusText = "Generated vertices.";
    }

    private void traceImage() {
        MeshTraceSettings settings = new MeshTraceSettings();
        settings.setDetail(64);
        settings.setAlphaThreshold(16);
        settings.setPadding(1.0f);
        EditableMesh traced = MeshTracer.traceAlpha(tracePixmap, settings, MESH_WIDTH, MESH_HEIGHT);
        replaceCurrentMesh(traced);
        statusText = "Traced image edge.";
    }

    private void replaceCurrentMesh(EditableMesh mesh) {
        int index = editorScene.currentIndex();
        editorScene.replaceAttachment(index, currentAttachment().name(), mesh);
        editorScene.setCurrentAttachment(index);
    }

    private void freezeCurrentTransform() {
        currentAttachment().freezeCurrentTransform(MESH_WIDTH, MESH_HEIGHT, false);
        statusText = "Freeze current transform.";
    }

    private void resetDeformation() {
        currentAttachment().resetDeformation();
        statusText = "Reset mesh deformation.";
    }

    private void toggleWireframe() {
        MeshAttachmentModel current = currentAttachment();
        current.display().setVerticesAndEdgesVisible(!current.display().verticesAndEdgesVisible());
        statusText = "Wireframe: " + current.display().verticesAndEdgesVisible();
    }

    private void toggleTriangles() {
        toolState.setTriangles(!toolState.triangles());
        currentAttachment().display().setTriangleLinesVisible(toolState.triangles());
        statusText = "Triangles: " + toolState.triangles();
    }

    private void toggleDim() {
        toolState.setDim(!toolState.dim());
        currentAttachment().display().setImageDimmed(toolState.dim());
        statusText = "Dim: " + toolState.dim();
    }

    private void toggleIsolate() {
        toolState.setIsolate(!toolState.isolate());
        editorScene.isolateCurrentAttachment(toolState.isolate());
        statusText = "Isolate: " + toolState.isolate();
    }

    private void toggleDeformed() {
        toolState.setDeformed(!toolState.deformed());
        currentAttachment().setEditDeformedMesh(toolState.deformed());
        statusText = "Edit deformed mesh: " + toolState.deformed();
    }

    private void createLinkedAttachment() {
        MeshAttachmentModel source = editorScene.attachments().get(0);
        MeshAttachmentModel linked = editorScene.addAttachment("linked-" + editorScene.attachments().size(), source.mesh().copy());
        linked.createLinkedMesh(source.mesh(), true);
        editorScene.setCurrentAttachment(editorScene.attachments().size() - 1);
        statusText = "Created linked mesh with inherit deform.";
    }

    private void unlinkCurrentAttachment() {
        currentAttachment().unlinkLinkedMesh();
        statusText = "Unlinked mesh.";
    }

    private void toggleInheritDeform() {
        MeshAttachmentModel current = currentAttachment();
        LinkedEditableMesh linkedMesh = current.linkedMesh();
        if (linkedMesh == null) {
            statusText = "Current attachment is not linked.";
            return;
        }
        linkedMesh.setInheritParentDeform(!linkedMesh.inheritParentDeform());
        statusText = "Inherit deform: " + linkedMesh.inheritParentDeform();
    }

    private void addVertexAtPointer(EditableMesh mesh) {
        float localX = pointerWorld.x - originX;
        float localY = pointerWorld.y - originY;
        if (localX < 0 || localY < 0 || localX > MESH_WIDTH || localY > MESH_HEIGHT) {
            return;
        }
        float u = localX / MESH_WIDTH;
        float v = 1.0f - localY / MESH_HEIGHT;
        selectedVertex = mesh.addVertexBySplittingTriangle(localX, localY, u, v).id();
        statusText = "Created vertex " + selectedVertex + ".";
    }

    private void deleteAtPointer(EditableMesh mesh) {
        if (hoverVertex >= 0) {
            mesh.removeVertex(hoverVertex);
            statusText = "Deleted vertex " + hoverVertex + ".";
            hoverVertex = -1;
            selectedVertex = -1;
        }
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

    private void drawAttachments() {
        List<MeshAttachmentModel> attachments = editorScene.visibleAttachments();
        for (MeshAttachmentModel attachment : attachments) {
            drawMeshTexture(attachment);
        }
        for (MeshAttachmentModel attachment : attachments) {
            if (attachment.display().triangleLinesVisible()) {
                drawTriangles(attachment.mesh(), attachment == currentAttachment());
            }
            if (attachment.display().verticesAndEdgesVisible()) {
                drawEdges(attachment.mesh(), attachment == currentAttachment());
                drawVertices(attachment.mesh(), attachment == currentAttachment());
            }
        }
    }

    private void drawMeshTexture(MeshAttachmentModel attachment) {
        Batch batch = game.getBatch();
        if (!(batch instanceof PolygonBatch)) {
            return;
        }
        EditableMesh mesh = attachment.mesh();
        float alpha = attachment.display().imageDimmed() ? 0.32f : 1.0f;
        float color = Color.toFloatBits(1.0f, 1.0f, 1.0f, alpha);
        float[] vertices = mesh.toPolygonVertices(originX, originY, color);
        short[] indices = mesh.toTriangleIndices();
        if (vertices.length == 0 || indices.length == 0) {
            return;
        }
        batch.begin();
        ((PolygonBatch) batch).draw(texture, vertices, 0, vertices.length, indices, 0, indices.length);
        batch.end();
    }

    private void drawTriangles(EditableMesh mesh, boolean active) {
        Map<Integer, MeshVertexSnapshot> byId = verticesById(mesh.vertices());
        shapes.begin(ShapeRenderer.ShapeType.Line);
        shapes.setColor(active ? new Color(1.0f, 0.86f, 0.25f, 0.45f) : new Color(0.5f, 0.7f, 1.0f, 0.22f));
        for (MeshTriangle triangle : mesh.triangles()) {
            MeshVertexSnapshot a = byId.get(triangle.a());
            MeshVertexSnapshot b = byId.get(triangle.b());
            MeshVertexSnapshot c = byId.get(triangle.c());
            if (a != null && b != null && c != null) {
                shapes.line(originX + a.x(), originY + a.y(), originX + b.x(), originY + b.y());
                shapes.line(originX + b.x(), originY + b.y(), originX + c.x(), originY + c.y());
                shapes.line(originX + c.x(), originY + c.y(), originX + a.x(), originY + a.y());
            }
        }
        shapes.end();
    }

    private void drawEdges(EditableMesh mesh, boolean active) {
        Map<Integer, MeshVertexSnapshot> byId = verticesById(mesh.vertices());
        shapes.begin(ShapeRenderer.ShapeType.Line);
        shapes.setColor(active ? new Color(1.0f, 1.0f, 1.0f, 0.72f) : new Color(0.62f, 0.78f, 1.0f, 0.34f));
        for (MeshEdge edge : mesh.edges()) {
            MeshVertexSnapshot a = byId.get(edge.a());
            MeshVertexSnapshot b = byId.get(edge.b());
            if (a != null && b != null) {
                shapes.line(originX + a.x(), originY + a.y(), originX + b.x(), originY + b.y());
            }
        }
        shapes.end();
    }

    private void drawVertices(EditableMesh mesh, boolean active) {
        shapes.begin(ShapeRenderer.ShapeType.Filled);
        for (MeshVertexSnapshot vertex : mesh.vertices()) {
            if (active && vertex.id() == hoverVertex) {
                shapes.setColor(1.0f, 0.36f, 0.22f, 1.0f);
                shapes.circle(originX + vertex.x(), originY + vertex.y(), 8.0f);
            } else if (active && vertex.id() == selectedVertex) {
                shapes.setColor(1.0f, 0.86f, 0.25f, 1.0f);
                shapes.circle(originX + vertex.x(), originY + vertex.y(), 7.0f);
            } else {
                shapes.setColor(active ? Color.CYAN : new Color(0.35f, 0.55f, 1.0f, 0.65f));
                shapes.circle(originX + vertex.x(), originY + vertex.y(), active ? 5.0f : 4.0f);
            }
        }
        shapes.end();
    }

    private void drawText() {
        Batch batch = game.getBatch();
        float height = stage.getViewport().getWorldHeight();
        MeshAttachmentModel current = currentAttachment();
        EditableMesh mesh = current.mesh();
        MeshBounds bounds = mesh.bounds();
        batch.begin();
        font.draw(batch, "Mesh editor screen", 36.0f, height - 42.0f);
        font.draw(batch, "1 Modify  2 Create  3 Delete  4 New  R Reset  G Generate  H Trace  F Freeze  Backspace ResetDeform", 36.0f, height - 76.0f);
        font.draw(batch, "W Wireframe  T Triangles  D Dim  I Isolate  E Deformed  L Link  U Unlink  O Inherit  5/6 Select", 36.0f, height - 110.0f);
        font.draw(batch, "attachment=" + current.name() + " type=" + current.meshType()
                + " mode=" + toolState.mode()
                + " deformed=" + toolState.deformed()
                + " vertices=" + mesh.vertexCount()
                + " edges=" + mesh.edgeCount()
                + " triangles=" + mesh.triangleCount()
                + " bounds=" + (int) bounds.width() + "x" + (int) bounds.height(), 36.0f, height - 144.0f);
        if (statusText.length() > 0) {
            font.draw(batch, statusText, 36.0f, 44.0f);
        }
        batch.end();
    }

    private MeshAttachmentModel currentAttachment() {
        MeshAttachmentModel attachment = editorScene.currentAttachment();
        if (attachment == null) {
            throw new IllegalStateException("no current attachment");
        }
        return attachment;
    }

    private void screenToWorld(int screenX, int screenY, Vector2 out) {
        Viewport viewport = stage.getViewport();
        out.set(screenX, screenY);
        viewport.unproject(out);
    }

    private int nearestVertex(EditableMesh mesh, Vector2 world, float maxDistance) {
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

    private static Map<Integer, MeshVertexSnapshot> verticesById(List<MeshVertexSnapshot> vertices) {
        Map<Integer, MeshVertexSnapshot> byId = new HashMap<Integer, MeshVertexSnapshot>();
        for (MeshVertexSnapshot vertex : vertices) {
            byId.put(vertex.id(), vertex);
        }
        return byId;
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
    }
}
