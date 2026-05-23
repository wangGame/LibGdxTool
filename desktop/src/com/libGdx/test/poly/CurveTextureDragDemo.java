package com.libGdx.test.poly;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;

import java.util.ArrayList;
import java.util.List;

public class CurveTextureDragDemo extends ApplicationAdapter {
	private static final float WORLD_WIDTH = 1280f;
	private static final float WORLD_HEIGHT = 720f;
	private static final int SAMPLE_COUNT = 48;
	private static final float HANDLE_RADIUS = 18f;
	private static final float INSERT_EDGE_RADIUS = 32f;
	private static final float DEFAULT_HALF_WIDTH = 60f;
	private static final float DEFAULT_REPEAT_LENGTH = 180f;
	private static final int MAX_CONTROL_POINTS = 24;

	private final List<Vector2> controls = new ArrayList<>();
	private final List<Vector2> casteljauCache = new ArrayList<>();
	private final Vector3 mouse = new Vector3();

	private PolygonSpriteBatch batch;
	private ShapeRenderer shapeRenderer;
	private BitmapFont font;
	private OrthographicCamera camera;
	private FitViewport viewport;
	private Texture texture;

	private float[] meshVertices;
	private short[] meshIndices;
	private int meshVertexCount;
	private int meshIndexCount;
	private int draggedHandle = -1;
	private int selectedHandle = -1;
	private float halfWidth = DEFAULT_HALF_WIDTH;
	private float repeatLength = DEFAULT_REPEAT_LENGTH;
	private boolean repeatTexture = true;
	private String statusText = "";

	public static void main (String[] args) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "Curve Texture Drag Demo";
		config.width = (int)WORLD_WIDTH;
		config.height = (int)WORLD_HEIGHT;
		new LwjglApplication(new CurveTextureDragDemo(), config);
	}

	@Override
	public void create () {
		camera = new OrthographicCamera();
		viewport = new FitViewport(WORLD_WIDTH, WORLD_HEIGHT, camera);
		viewport.apply(true);

		batch = new PolygonSpriteBatch();
		shapeRenderer = new ShapeRenderer();
		font = new BitmapFont();

		controls.clear();
		controls.add(new Vector2(120f, 150f));
		controls.add(new Vector2(340f, 620f));
		controls.add(new Vector2(920f, 100f));
		controls.add(new Vector2(1140f, 560f));

		texture = new Texture(Gdx.files.internal("assets/wood.png"));
		texture.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);

		rebuildMesh();
	}

	@Override
	public void render () {
		handleInput();
		camera.update();

		ScreenUtils.clear(0.1f, 0.1f, 0.13f, 1f);

		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		batch.draw(texture, meshVertices, 0, meshVertexCount, meshIndices, 0, meshIndexCount);
		drawLabels();
		batch.end();

		drawDebug();
	}

	private void drawLabels () {
		font.setColor(Color.WHITE);
		font.draw(batch, "Curve mesh demo (not RepeatablePolygonSprite)", 20f, WORLD_HEIGHT - 20f);
		font.draw(batch, "Drag control points with left mouse.", 20f, WORLD_HEIGHT - 48f);
		font.draw(batch, "Up/Down = width, Left/Right = repeat length, Space = repeat/stretch", 20f, WORLD_HEIGHT - 76f);
		font.draw(batch, "Left(point) = drag, Left(segment) = insert between neighbors", 20f, WORLD_HEIGHT - 104f);
		font.draw(batch, "Shift+Left(empty) = append endpoint", 20f, WORLD_HEIGHT - 132f);
		font.draw(batch, "Right on point or Del = remove", 20f, WORLD_HEIGHT - 160f);
		font.draw(batch, "Mode: " + (repeatTexture ? "Repeat" : "Stretch") + "   points: " + controls.size() + "/" + MAX_CONTROL_POINTS, 20f, WORLD_HEIGHT - 188f);
		font.draw(batch, "Width: " + halfWidth * 2f + "   repeatLength: " + repeatLength, 20f, WORLD_HEIGHT - 216f);
		if (!statusText.isEmpty()) {
			font.setColor(Color.GOLD);
			font.draw(batch, statusText, 20f, WORLD_HEIGHT - 244f);
		}
	}

	private void drawDebug () {
		shapeRenderer.setProjectionMatrix(camera.combined);
		shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
		shapeRenderer.setColor(Color.ORANGE);
		for (int i = 0; i < controls.size() - 1; i++) {
			shapeRenderer.line(controls.get(i), controls.get(i + 1));
		}
		shapeRenderer.setColor(Color.CYAN);
		for (int i = 0; i < SAMPLE_COUNT - 1; i++) {
			Vector2 p0 = sample(i / (float)(SAMPLE_COUNT - 1));
			Vector2 p1 = sample((i + 1) / (float)(SAMPLE_COUNT - 1));
			shapeRenderer.line(p0, p1);
		}
		shapeRenderer.end();

		shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
		for (int i = 0; i < controls.size(); i++) {
			shapeRenderer.setColor(i == draggedHandle ? Color.YELLOW : (i == selectedHandle ? Color.LIME : Color.RED));
			shapeRenderer.circle(controls.get(i).x, controls.get(i).y, HANDLE_RADIUS);
		}
		shapeRenderer.end();
	}

	private void handleInput () {
		if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
			repeatTexture = !repeatTexture;
			rebuildMesh();
		}
		if (Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
			halfWidth = Math.min(140f, halfWidth + 8f);
			rebuildMesh();
		}
		if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN)) {
			halfWidth = Math.max(20f, halfWidth - 8f);
			rebuildMesh();
		}
		if (Gdx.input.isKeyJustPressed(Input.Keys.RIGHT)) {
			repeatLength = Math.min(480f, repeatLength + 20f);
			rebuildMesh();
		}
		if (Gdx.input.isKeyJustPressed(Input.Keys.LEFT)) {
			repeatLength = Math.max(40f, repeatLength - 20f);
			rebuildMesh();
		}

		if (Gdx.input.isKeyJustPressed(Input.Keys.DEL) || Gdx.input.isKeyJustPressed(Input.Keys.FORWARD_DEL)) {
			removeControlPoint(selectedHandle);
		}

		mouse.set(Gdx.input.getX(), Gdx.input.getY(), 0f);
		viewport.unproject(mouse);

		if (Gdx.input.isButtonJustPressed(Input.Buttons.RIGHT)) {
			removeControlPoint(findHandle(mouse.x, mouse.y));
		}

		if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
			int touched = findHandle(mouse.x, mouse.y);
			boolean shift = Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT) || Gdx.input.isKeyPressed(Input.Keys.SHIFT_RIGHT);
			if (touched >= 0) {
				draggedHandle = touched;
				selectedHandle = touched;
				statusText = "";
			} else {
				int insertIndex = findInsertIndex(mouse.x, mouse.y);
				if (insertIndex > 0 && insertIndex < controls.size()) {
					if (controls.size() >= MAX_CONTROL_POINTS) {
						statusText = "Reached max control points.";
					} else {
						Vector2 projected = projectToSegment(controls.get(insertIndex - 1), controls.get(insertIndex), mouse.x, mouse.y);
						controls.add(insertIndex, projected);
						selectedHandle = insertIndex;
						draggedHandle = selectedHandle;
						statusText = "Inserted between neighbor points.";
						rebuildMesh();
					}
				} else if (shift) {
					if (controls.size() >= MAX_CONTROL_POINTS) {
						statusText = "Reached max control points.";
					} else {
						int appendIndex = controls.size();
						controls.add(new Vector2(mouse.x, mouse.y));
						selectedHandle = appendIndex;
						draggedHandle = selectedHandle;
						statusText = "Appended a new endpoint.";
						rebuildMesh();
					}
				} else {
					draggedHandle = -1;
					selectedHandle = -1;
					statusText = "Click near a segment to insert. Shift+Left empty to append.";
				}
			}
		}

		if (!Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
			draggedHandle = -1;
			return;
		}

		if (draggedHandle >= 0) {
			controls.get(draggedHandle).set(mouse.x, mouse.y);
			rebuildMesh();
		}
	}

	private int findHandle (float x, float y) {
		float radiusSquared = HANDLE_RADIUS * HANDLE_RADIUS * 1.8f;
		for (int i = 0; i < controls.size(); i++) {
			if (controls.get(i).dst2(x, y) <= radiusSquared) {
				return i;
			}
		}
		return -1;
	}

	private int findInsertIndex (float x, float y) {
		if (controls.size() < 2) {
			return -1;
		}
		float best = INSERT_EDGE_RADIUS * INSERT_EDGE_RADIUS;
		int bestIndex = -1;
		for (int i = 0; i < controls.size() - 1; i++) {
			float dst2 = pointToSegmentDst2(controls.get(i), controls.get(i + 1), x, y);
			if (dst2 <= best) {
				best = dst2;
				bestIndex = i + 1;
			}
		}
		return bestIndex;
	}

	private float pointToSegmentDst2 (Vector2 a, Vector2 b, float px, float py) {
		float abx = b.x - a.x;
		float aby = b.y - a.y;
		float apx = px - a.x;
		float apy = py - a.y;
		float len2 = abx * abx + aby * aby;
		if (len2 <= 0.000001f) {
			return a.dst2(px, py);
		}
		float alpha = MathUtils.clamp((apx * abx + apy * aby) / len2, 0f, 1f);
		float projX = a.x + abx * alpha;
		float projY = a.y + aby * alpha;
		float dx = px - projX;
		float dy = py - projY;
		return dx * dx + dy * dy;
	}

	private Vector2 projectToSegment (Vector2 a, Vector2 b, float px, float py) {
		float abx = b.x - a.x;
		float aby = b.y - a.y;
		float len2 = abx * abx + aby * aby;
		if (len2 <= 0.000001f) {
			return new Vector2((a.x + b.x) * 0.5f, (a.y + b.y) * 0.5f);
		}
		float alpha = MathUtils.clamp(((px - a.x) * abx + (py - a.y) * aby) / len2, 0f, 1f);
		return new Vector2(a.x + abx * alpha, a.y + aby * alpha);
	}

	private void removeControlPoint (int index) {
		if (index < 0 || index >= controls.size()) {
			return;
		}
		if (controls.size() <= 2) {
			statusText = "At least 2 control points are required.";
			return;
		}
		controls.remove(index);
		statusText = "";
		if (selectedHandle == index) {
			selectedHandle = -1;
		} else if (selectedHandle > index) {
			selectedHandle--;
		}
		if (draggedHandle == index) {
			draggedHandle = -1;
		} else if (draggedHandle > index) {
			draggedHandle--;
		}
		rebuildMesh();
	}

	private void rebuildMesh () {
		if (controls.size() < 2) {
			meshVertices = new float[0];
			meshIndices = new short[0];
			meshVertexCount = 0;
			meshIndexCount = 0;
			return;
		}

		Vector2[] points = new Vector2[SAMPLE_COUNT];
		float[] distances = new float[SAMPLE_COUNT];
		for (int i = 0; i < SAMPLE_COUNT; i++) {
			float t = i / (float)(SAMPLE_COUNT - 1);
			points[i] = sample(t);
			if (i > 0) {
				distances[i] = distances[i - 1] + points[i].dst(points[i - 1]);
			}
		}
		float totalLength = Math.max(1f, distances[SAMPLE_COUNT - 1]);

		meshVertices = new float[SAMPLE_COUNT * 10];
		meshIndices = new short[(SAMPLE_COUNT - 1) * 6];

		float colorBits = Color.WHITE.toFloatBits();
		for (int i = 0; i < SAMPLE_COUNT; i++) {
			Vector2 point = points[i];
			Vector2 tangent;
			if (i == 0) {
				tangent = new Vector2(points[1]).sub(points[0]);
			} else if (i == SAMPLE_COUNT - 1) {
				tangent = new Vector2(points[i]).sub(points[i - 1]);
			} else {
				tangent = new Vector2(points[i + 1]).sub(points[i - 1]);
			}
			if (tangent.isZero(0.0001f)) {
				tangent.set(1f, 0f);
			}
			tangent.nor();
			Vector2 normal = new Vector2(tangent).rotate90(1).scl(halfWidth);

			Vector2 left = new Vector2(point).add(normal);
			Vector2 right = new Vector2(point).sub(normal);
			float pathV = repeatTexture ? distances[i] / repeatLength : distances[i] / totalLength;

			int v = i * 10;
			meshVertices[v] = left.x;
			meshVertices[v + 1] = left.y;
			meshVertices[v + 2] = colorBits;
			meshVertices[v + 3] = 0f;
			meshVertices[v + 4] = pathV;

			meshVertices[v + 5] = right.x;
			meshVertices[v + 6] = right.y;
			meshVertices[v + 7] = colorBits;
			meshVertices[v + 8] = 1f;
			meshVertices[v + 9] = pathV;
		}

		for (int i = 0; i < SAMPLE_COUNT - 1; i++) {
			int base = i * 6;
			short index = (short)(i * 2);
			meshIndices[base] = index;
			meshIndices[base + 1] = (short)(index + 1);
			meshIndices[base + 2] = (short)(index + 2);
			meshIndices[base + 3] = (short)(index + 2);
			meshIndices[base + 4] = (short)(index + 1);
			meshIndices[base + 5] = (short)(index + 3);
		}

		meshVertexCount = meshVertices.length;
		meshIndexCount = meshIndices.length;
	}

	private Vector2 sample (float t) {
		float clamped = MathUtils.clamp(t, 0f, 1f);
		for (int i = casteljauCache.size(); i < controls.size(); i++) {
			casteljauCache.add(new Vector2());
		}
		for (int i = 0; i < controls.size(); i++) {
			casteljauCache.get(i).set(controls.get(i));
		}
		for (int level = controls.size() - 1; level > 0; level--) {
			for (int i = 0; i < level; i++) {
				casteljauCache.get(i).lerp(casteljauCache.get(i + 1), clamped);
			}
		}
		return new Vector2(casteljauCache.get(0));
	}

	@Override
	public void resize (int width, int height) {
		viewport.update(width, height, true);
	}

	@Override
	public void dispose () {
		if (font != null) font.dispose();
		if (shapeRenderer != null) shapeRenderer.dispose();
		if (batch != null) batch.dispose();
		if (texture != null) texture.dispose();
	}
}
