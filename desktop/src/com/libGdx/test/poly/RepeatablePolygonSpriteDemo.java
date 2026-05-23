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
import com.badlogic.gdx.graphics.g2d.RepeatablePolygonSprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class RepeatablePolygonSpriteDemo extends ApplicationAdapter {
	private static final float WORLD_WIDTH = 960f;
	private static final float WORLD_HEIGHT = 720f;

	private final float[] basePolygon = new float[] {
		0f, 0f,
		0f, 500f,
		500f, 500f,
		500f, 350f,
		300f, 350f,
		300f, 150f,
		500f, 150f,
		500f, 0f
	};

	private PolygonSpriteBatch batch;
	private ShapeRenderer shapeRenderer;
	private BitmapFont font;
	private OrthographicCamera camera;
	private FitViewport viewport;

	private Texture texture;
	private TextureRegion region;
	private RepeatablePolygonSprite repeatablePolygonSprite;
	private Polygon outlinePolygon;

	private float density = 6f;
	private final float polygonX = 180f;
	private final float polygonY = 110f;

	public static void main (String[] args) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "RepeatablePolygonSprite Demo";
		config.width = (int)WORLD_WIDTH;
		config.height = (int)WORLD_HEIGHT;
		new LwjglApplication(new RepeatablePolygonSpriteDemo(), config);
	}

	@Override
	public void create () {
		camera = new OrthographicCamera();
		viewport = new FitViewport(WORLD_WIDTH, WORLD_HEIGHT, camera);
		viewport.apply(true);

		batch = new PolygonSpriteBatch();
		shapeRenderer = new ShapeRenderer();
		font = new BitmapFont();

		texture = new Texture(Gdx.files.internal("assets/wood.png"));
		texture.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
		region = new TextureRegion(texture);

		repeatablePolygonSprite = new RepeatablePolygonSprite();
		outlinePolygon = new Polygon(basePolygon.clone());
		outlinePolygon.setPosition(polygonX, polygonY);

		rebuildPolygon();
	}

	private void rebuildPolygon () {
		// RepeatablePolygonSprite 会修改传入的顶点数组，所以这里每次都传 clone。
		repeatablePolygonSprite.setPolygon(region, basePolygon.clone(), density);
		repeatablePolygonSprite.setPosition(polygonX, polygonY);
		repeatablePolygonSprite.setColor(Color.WHITE);
	}

	@Override
	public void render () {
		handleInput();
		camera.update();

		ScreenUtils.clear(0.08f, 0.09f, 0.12f, 1f);

		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		repeatablePolygonSprite.draw(batch);
		font.draw(batch, "RepeatablePolygonSprite 测试案例", 20f, WORLD_HEIGHT - 20f);
		font.draw(batch, "Left/Right 调整 density，R 重置", 20f, WORLD_HEIGHT - 50f);
		font.draw(batch, "当前 density: " + density, 20f, WORLD_HEIGHT - 80f);
		font.draw(batch, "纹理: assets/wood.png", 20f, WORLD_HEIGHT - 110f);
		batch.end();

		shapeRenderer.setProjectionMatrix(camera.combined);
		shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
		shapeRenderer.setColor(Color.BLACK);
		shapeRenderer.polygon(outlinePolygon.getTransformedVertices());
		shapeRenderer.end();
	}

	private void handleInput () {
		boolean changed = false;
		if (Gdx.input.isKeyJustPressed(Input.Keys.RIGHT)) {
			density = Math.min(14f, density + 1f);
			changed = true;
		}
		if (Gdx.input.isKeyJustPressed(Input.Keys.LEFT)) {
			density = Math.max(1f, density - 1f);
			changed = true;
		}
		if (Gdx.input.isKeyJustPressed(Input.Keys.R)) {
			density = 6f;
			changed = true;
		}

		if (changed) {
			rebuildPolygon();
		}
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
