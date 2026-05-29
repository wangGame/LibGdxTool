package com.libGdx.test.poly;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;

import java.util.ArrayList;
import java.util.List;

public class CurveTextureDragDemo extends ApplicationAdapter {
	private static final float WORLD_WIDTH = 1280f;
	private static final float WORLD_HEIGHT = 720f;
	private static final int SAMPLE_COUNT = 72;
	private static final float BASE_HALF_WIDTH = 52f;
	private static final float REPEAT_LENGTH = 170f;
	private static final float PULL_SPEED = 720f;
	private static final float STRAIGHTEN_BAND = 260f;
	private static final float EXIT_MARGIN = 160f;
	private static final float TRAIL_LIFE = 0.45f;
	private static final int TRAIL_MAX = 18;

	private final Vector2[] originalPoints = new Vector2[SAMPLE_COUNT];
	private final Vector2[] animatedPoints = new Vector2[SAMPLE_COUNT];
	private final float[] originalDistances = new float[SAMPLE_COUNT];
	private final float[] animatedDistances = new float[SAMPLE_COUNT];
	private final Vector2 flyDirection = new Vector2(1f, 0f);
	private final Vector2 headStart = new Vector2();
	private final Vector2 tangent = new Vector2();
	private final Vector2 normal = new Vector2();
	private final List<TrailPoint> trail = new ArrayList<>();

	private OrthographicCamera camera;
	private FitViewport viewport;
	private PolygonSpriteBatch batch;
	private ShapeRenderer shapes;
	private BitmapFont font;
	private Texture texture;

	private float[] vertices = new float[0];
	private short[] indices = new short[0];
	private int vertexCount;
	private int indexCount;
	private float curveLength;
	private float elapsed;
	private float halfWidth = BASE_HALF_WIDTH;
	private boolean flying;
	private boolean finished;

	public static void main (String[] args) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "Curve Pull Out Demo";
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
		shapes = new ShapeRenderer();
		font = new BitmapFont();

		texture = new Texture(Gdx.files.internal("assets/wood.png"));
		texture.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);

		for (int i = 0; i < SAMPLE_COUNT; i++) {
			originalPoints[i] = new Vector2();
			animatedPoints[i] = new Vector2();
		}
		resetCurve();
	}

	@Override
	public void render () {
		handleInput();
		float delta = Math.min(Gdx.graphics.getDeltaTime(), 1f / 30f);
		update(delta);

		camera.update();
		ScreenUtils.clear(0.08f, 0.09f, 0.11f, 1f);

		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		if (vertexCount > 0 && indexCount > 0) {
			batch.draw(texture, vertices, 0, vertexCount, indices, 0, indexCount);
		}
		drawText();
		batch.end();

		drawGuide();
		drawHeadGlow();
	}

	private void handleInput () {
		if (Gdx.input.isKeyJustPressed(Input.Keys.R)) {
			resetCurve();
		}
		if (Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
			halfWidth = Math.min(120f, halfWidth + 6f);
			buildMesh(animatedPoints, animatedDistances, animatedDistances[SAMPLE_COUNT - 1]);
		}
		if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN)) {
			halfWidth = Math.max(18f, halfWidth - 6f);
			buildMesh(animatedPoints, animatedDistances, animatedDistances[SAMPLE_COUNT - 1]);
		}
		if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE) || Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
			launch();
		}
	}

	private void resetCurve () {
		flying = false;
		finished = false;
		elapsed = 0f;
		trail.clear();

		buildOriginalCurve();
		for (int i = 0; i < SAMPLE_COUNT; i++) {
			animatedPoints[i].set(originalPoints[i]);
			animatedDistances[i] = originalDistances[i];
		}
		buildMesh(animatedPoints, animatedDistances, curveLength);
	}

	private void launch () {
		if (flying || finished) {
			return;
		}
		headStart.set(originalPoints[SAMPLE_COUNT - 1]);
		flyDirection.set(originalPoints[SAMPLE_COUNT - 1]).sub(originalPoints[SAMPLE_COUNT - 3]);
		if (flyDirection.isZero(0.0001f)) {
			flyDirection.set(1f, 0f);
		}
		flyDirection.nor();
		elapsed = 0f;
		flying = true;
		trail.clear();
		addTrail(headStart.x, headStart.y);
	}

	private void update (float delta) {
		ageTrail(delta);
		if (!flying) {
			return;
		}

		elapsed += delta;
		float pullDistance = elapsed * PULL_SPEED;
		animatedDistances[0] = 0f;

		for (int i = 0; i < SAMPLE_COUNT; i++) {
			float distanceFromHead = curveLength - originalDistances[i];
			float targetDistance = pullDistance - distanceFromHead;
			float targetX = headStart.x + flyDirection.x * targetDistance;
			float targetY = headStart.y + flyDirection.y * targetDistance;
			float alpha = smoothStep((pullDistance + STRAIGHTEN_BAND - distanceFromHead) / STRAIGHTEN_BAND);

			Vector2 original = originalPoints[i];
			animatedPoints[i].set(
					MathUtils.lerp(original.x, targetX, alpha),
					MathUtils.lerp(original.y, targetY, alpha)
			);

			if (i > 0) {
				animatedDistances[i] = animatedDistances[i - 1] + animatedPoints[i].dst(animatedPoints[i - 1]);
			}
		}

		Vector2 head = animatedPoints[SAMPLE_COUNT - 1];
		addTrail(head.x, head.y);
		buildMesh(animatedPoints, animatedDistances, animatedDistances[SAMPLE_COUNT - 1]);

		if (isOutOfWorld()) {
			flying = false;
			finished = true;
		}
	}

	private void buildOriginalCurve () {
		Vector2 p0 = new Vector2(120f, 160f);
		Vector2 p1 = new Vector2(320f, 650f);
		Vector2 p2 = new Vector2(760f, 70f);
		Vector2 p3 = new Vector2(1140f, 560f);

		originalDistances[0] = 0f;
		for (int i = 0; i < SAMPLE_COUNT; i++) {
			float t = i / (float)(SAMPLE_COUNT - 1);
			sampleCubic(p0, p1, p2, p3, t, originalPoints[i]);
			if (i > 0) {
				originalDistances[i] = originalDistances[i - 1] + originalPoints[i].dst(originalPoints[i - 1]);
			}
		}
		curveLength = originalDistances[SAMPLE_COUNT - 1];
	}

	private void sampleCubic (Vector2 p0, Vector2 p1, Vector2 p2, Vector2 p3, float t, Vector2 out) {
		float u = 1f - t;
		float tt = t * t;
		float uu = u * u;
		float uuu = uu * u;
		float ttt = tt * t;
		out.set(
				uuu * p0.x + 3f * uu * t * p1.x + 3f * u * tt * p2.x + ttt * p3.x,
				uuu * p0.y + 3f * uu * t * p1.y + 3f * u * tt * p2.y + ttt * p3.y
		);
	}

	private void buildMesh (Vector2[] points, float[] distances, float length) {
		vertices = new float[SAMPLE_COUNT * 10];
		indices = new short[(SAMPLE_COUNT - 1) * 6];
		float color = Color.WHITE.toFloatBits();
		float totalLength = Math.max(1f, length);

		for (int i = 0; i < SAMPLE_COUNT; i++) {
			if (i == 0) {
				tangent.set(points[1]).sub(points[0]);
			} else if (i == SAMPLE_COUNT - 1) {
				tangent.set(points[i]).sub(points[i - 1]);
			} else {
				tangent.set(points[i + 1]).sub(points[i - 1]);
			}
			if (tangent.isZero(0.0001f)) {
				tangent.set(1f, 0f);
			}
			tangent.nor();
			normal.set(-tangent.y, tangent.x).scl(halfWidth);

			Vector2 point = points[i];
			float v = distances[i] / REPEAT_LENGTH;
			if (!flying && finished) {
				v = distances[i] / totalLength;
			}

			int base = i * 10;
			vertices[base] = point.x + normal.x;
			vertices[base + 1] = point.y + normal.y;
			vertices[base + 2] = color;
			vertices[base + 3] = 0f;
			vertices[base + 4] = v;
			vertices[base + 5] = point.x - normal.x;
			vertices[base + 6] = point.y - normal.y;
			vertices[base + 7] = color;
			vertices[base + 8] = 1f;
			vertices[base + 9] = v;
		}

		for (int i = 0; i < SAMPLE_COUNT - 1; i++) {
			int base = i * 6;
			short left = (short)(i * 2);
			indices[base] = left;
			indices[base + 1] = (short)(left + 1);
			indices[base + 2] = (short)(left + 2);
			indices[base + 3] = (short)(left + 2);
			indices[base + 4] = (short)(left + 1);
			indices[base + 5] = (short)(left + 3);
		}

		vertexCount = vertices.length;
		indexCount = indices.length;
	}

	private boolean isOutOfWorld () {
		float minX = Float.MAX_VALUE;
		float minY = Float.MAX_VALUE;
		float maxX = -Float.MAX_VALUE;
		float maxY = -Float.MAX_VALUE;
		for (Vector2 point : animatedPoints) {
			minX = Math.min(minX, point.x);
			minY = Math.min(minY, point.y);
			maxX = Math.max(maxX, point.x);
			maxY = Math.max(maxY, point.y);
		}
		return maxX < -EXIT_MARGIN || minX > WORLD_WIDTH + EXIT_MARGIN
				|| maxY < -EXIT_MARGIN || minY > WORLD_HEIGHT + EXIT_MARGIN;
	}

	private float smoothStep (float value) {
		float t = MathUtils.clamp(value, 0f, 1f);
		return t * t * (3f - 2f * t);
	}

	private void addTrail (float x, float y) {
		trail.add(new TrailPoint(x, y));
		while (trail.size() > TRAIL_MAX) {
			trail.remove(0);
		}
	}

	private void ageTrail (float delta) {
		for (int i = trail.size() - 1; i >= 0; i--) {
			TrailPoint point = trail.get(i);
			point.age += delta;
			if (point.age >= TRAIL_LIFE) {
				trail.remove(i);
			}
		}
	}

	private void drawText () {
		font.setColor(Color.WHITE);
		font.draw(batch, "Click / Space: pull curve out    R: reset    Up/Down: width", 20f, WORLD_HEIGHT - 24f);
		font.draw(batch, "Effect: head flies first, curve body is pulled straight by distance delay.", 20f, WORLD_HEIGHT - 52f);
		if (finished) {
			font.setColor(Color.GOLD);
			font.draw(batch, "Finished. Press R to replay.", 20f, WORLD_HEIGHT - 80f);
		}
	}

	private void drawGuide () {
		shapes.setProjectionMatrix(camera.combined);
		Gdx.gl.glEnable(GL20.GL_BLEND);
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

		shapes.begin(ShapeRenderer.ShapeType.Line);
		shapes.setColor(0.2f, 0.8f, 1f, flying ? 0.28f : 0.75f);
		for (int i = 0; i < SAMPLE_COUNT - 1; i++) {
			shapes.line(animatedPoints[i], animatedPoints[i + 1]);
		}
		shapes.end();

		Gdx.gl.glDisable(GL20.GL_BLEND);
	}

	private void drawHeadGlow () {
		if (trail.isEmpty()) {
			return;
		}

		shapes.setProjectionMatrix(camera.combined);
		Gdx.gl.glEnable(GL20.GL_BLEND);
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		shapes.begin(ShapeRenderer.ShapeType.Filled);

		for (int i = 0; i < trail.size(); i++) {
			TrailPoint point = trail.get(i);
			float life = 1f - MathUtils.clamp(point.age / TRAIL_LIFE, 0f, 1f);
			float order = (i + 1f) / trail.size();
			float alpha = life * order;
			shapes.setColor(1f, 0.5f, 0.08f, 0.16f * alpha);
			shapes.circle(point.x, point.y, 34f * alpha + 8f, 24);
			shapes.setColor(1f, 0.9f, 0.16f, 0.55f * alpha);
			shapes.circle(point.x, point.y, 15f * alpha + 4f, 18);
		}

		shapes.end();
		Gdx.gl.glDisable(GL20.GL_BLEND);
	}

	@Override
	public void resize (int width, int height) {
		viewport.update(width, height, true);
	}

	@Override
	public void dispose () {
		if (texture != null) texture.dispose();
		if (font != null) font.dispose();
		if (shapes != null) shapes.dispose();
		if (batch != null) batch.dispose();
	}

	private static class TrailPoint {
		float x;
		float y;
		float age;

		TrailPoint (float x, float y) {
			this.x = x;
			this.y = y;
		}
	}
}
