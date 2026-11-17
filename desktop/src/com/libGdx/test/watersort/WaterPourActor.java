package com.libGdx.test.watersort;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.utils.Array;

/**
 * A reusable, asset-free water-sort pouring widget.
 *
 * <p>The timing follows the inspected game: every transferred cell takes 0.4 seconds.
 * A pour consists of move -> rotate -> transfer -> rotate back -> return. Liquid data is
 * committed only after the animation, so cancelled/invalid moves never corrupt the tubes.</p>
 */
public class WaterPourActor extends Actor {
    private static final float TUBE_W = 116f;
    private static final float TUBE_H = 360f;
    private static final float WALL = 10f;
    private static final float CELL_H = 77f;
    private static final float MOVE_TIME = .34f;
    private static final float ROTATE_TIME = .27f;
    private static final float RETURN_TIME = .32f;
    private static final float POUR_TIME_PER_CELL = .4f;

    private static final Color[] PALETTE = {
            new Color(0f, 0f, 0f, 0f),
            Color.valueOf("ff4f72"), Color.valueOf("48c6ff"),
            Color.valueOf("ffd84a"), Color.valueOf("8c64ff"),
            Color.valueOf("53dc8c"), Color.valueOf("ff914d")
    };

    private final ShapeRenderer shapes = new ShapeRenderer();
    private final Array<Tube> tubes = new Array<Tube>();
    private final Vector2 tmp = new Vector2();
    private Tube selected;
    private Pour pour;

    public WaterPourActor() {
        setBounds(0, 0, 1080, 1920);
        addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (button != Input.Buttons.LEFT || pour != null) return false;
                Tube hit = hit(x, y);
                if (hit == null) return false;
                if (selected == null) {
                    if (!hit.isEmpty()) selected = hit;
                } else if (selected == hit) {
                    selected = null;
                } else if (!pour(selected, hit)) {
                    selected = hit.isEmpty() ? null : hit;
                }
                return true;
            }
        });
    }

    /** Colors are IDs 1..6, listed from bottom to top; 0 means an empty cell. */
    public Tube addTube(float centerX, float bottomY, int... bottomToTop) {
        Tube tube = new Tube(centerX, bottomY, bottomToTop);
        tubes.add(tube);
        return tube;
    }

    /** Starts a legal pour and returns true; illegal moves leave both tubes unchanged. */
    public boolean pour(Tube from, Tube to) {
        if (pour != null || from == null || to == null || from == to) return false;
        int color = from.topColor();
        int count = Math.min(from.topRun(), to.free());
        if (color == 0 || count == 0 || (!to.isEmpty() && to.topColor() != color)) return false;
        selected = null;
        pour = new Pour(from, to, color, count);
        return true;
    }

    public boolean isAnimating() {
        return pour != null;
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        if (pour != null && pour.update(delta)) pour = null;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.end();
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        shapes.setProjectionMatrix(batch.getProjectionMatrix());
        shapes.begin(ShapeRenderer.ShapeType.Filled);

        // Soft board behind the demonstration.
        shapes.setColor(0.055f, .075f, .13f, 1f);
        shapes.rect(0, 0, getWidth(), getHeight());
        for (Tube tube : tubes) drawTube(tube);
        if (pour != null && pour.phase == 2) drawStream(pour);

        shapes.end();
        Gdx.gl.glDisable(GL20.GL_BLEND);
        batch.begin();
    }

    private void drawTube(Tube tube) {
        float angle = tube.angle;
        float innerLeft = -TUBE_W / 2f + WALL;
        float innerRight = TUBE_W / 2f - WALL;
        float bottom = -TUBE_H + 21f;

        int size = tube.size;
        boolean transferring = pour != null && pour.phase == 2;
        float sourceLoss = 0f;
        float targetGain = 0f;
        int movingColor = 0;
        if (transferring) {
            if (tube == pour.from) {
                sourceLoss = pour.transfer * pour.count;
            } else if (tube == pour.to) {
                targetGain = pour.transfer * pour.count;
                movingColor = pour.color;
            }
        }

        // Stable liquid cells.
        int stableSourceCells = transferring && tube == pour.from ? size - pour.count : size;
        for (int i = 0; i < stableSourceCells; i++) {
            drawLocalRect(tube, innerLeft, bottom + i * CELL_H, innerRight - innerLeft,
                    CELL_H + 1f, PALETTE[tube.colors[i]], angle);
        }
        if (transferring && tube == pour.from) {
            float remaining = pour.count - sourceLoss;
            if (remaining > 0f) {
                drawLocalRect(tube, innerLeft, bottom + stableSourceCells * CELL_H,
                        innerRight - innerLeft, remaining * CELL_H, PALETTE[pour.color], angle);
            }
        } else if (targetGain > 0f) {
            drawLocalRect(tube, innerLeft, bottom + size * CELL_H,
                    innerRight - innerLeft, targetGain * CELL_H, PALETTE[movingColor], angle);
        }

        // Glass: translucent body plus bright rim. It rotates around the mouth at local (0,0).
        shapes.setColor(.62f, .78f, .91f, .16f);
        drawLocalRect(tube, -TUBE_W / 2f, -TUBE_H, TUBE_W, TUBE_H, shapes.getColor(), angle);
        shapes.setColor(.78f, .9f, 1f, .9f);
        drawLocalRect(tube, -TUBE_W / 2f, -TUBE_H, WALL, TUBE_H, shapes.getColor(), angle);
        drawLocalRect(tube, TUBE_W / 2f - WALL, -TUBE_H, WALL, TUBE_H, shapes.getColor(), angle);
        drawLocalRect(tube, -TUBE_W / 2f, -TUBE_H, TUBE_W, WALL, shapes.getColor(), angle);
        drawLocalRect(tube, -TUBE_W / 2f - 5f, -8f, TUBE_W + 10f, 12f, shapes.getColor(), angle);

        if (tube == selected) {
            shapes.setColor(1f, .92f, .35f, .32f);
            shapes.circle(tube.pivotX, tube.pivotY + 20f, 75f, 32);
        }
    }

    private void drawStream(Pour p) {
        tmp.set(p.from.pivotX, p.from.pivotY);
        // Stream begins at the lower lip on the target side.
        float lipX = p.direction * TUBE_W * .45f;
        float rad = p.from.angle * MathUtils.degreesToRadians;
        float sx = tmp.x + lipX * MathUtils.cos(rad);
        float sy = tmp.y + lipX * MathUtils.sin(rad) - 5f;
        float targetSurface = p.to.homeY - TUBE_H + 21f +
                (p.to.size + p.transfer * p.count) * CELL_H;
        shapes.setColor(PALETTE[p.color]);
        float width = 17f + 3f * MathUtils.sin(p.transfer * MathUtils.PI);
        shapes.rect(sx - width / 2f, targetSurface, width, Math.max(0, sy - targetSurface));
        shapes.circle(sx, targetSurface, width / 2f, 16);
    }

    private void drawLocalRect(Tube tube, float x, float y, float w, float h, Color color, float angle) {
        float r = angle * MathUtils.degreesToRadians;
        float c = MathUtils.cos(r), s = MathUtils.sin(r);
        float x1 = tube.pivotX + x * c - y * s;
        float y1 = tube.pivotY + x * s + y * c;
        float x2 = tube.pivotX + (x + w) * c - y * s;
        float y2 = tube.pivotY + (x + w) * s + y * c;
        float x3 = tube.pivotX + (x + w) * c - (y + h) * s;
        float y3 = tube.pivotY + (x + w) * s + (y + h) * c;
        float x4 = tube.pivotX + x * c - (y + h) * s;
        float y4 = tube.pivotY + x * s + (y + h) * c;
        shapes.setColor(color);
        shapes.triangle(x1, y1, x2, y2, x3, y3);
        shapes.triangle(x3, y3, x4, y4, x1, y1);
    }

    private Tube hit(float x, float y) {
        Tube best = null;
        float bestDistance = Float.MAX_VALUE;
        for (Tube tube : tubes) {
            float dx = x - tube.homeX;
            float dy = y - (tube.homeY - TUBE_H / 2f);
            float d = dx * dx + dy * dy;
            if (Math.abs(dx) <= TUBE_W && Math.abs(dy) <= TUBE_H * .65f && d < bestDistance) {
                best = tube;
                bestDistance = d;
            }
        }
        return best;
    }

    public static final class Tube {
        private final int[] colors = new int[4];
        private final float homeX, homeY;
        private float pivotX, pivotY, angle;
        private int size;

        private Tube(float centerX, float bottomY, int... values) {
            homeX = pivotX = centerX;
            homeY = pivotY = bottomY + TUBE_H;
            for (int value : values) {
                if (value != 0 && size < colors.length) colors[size++] = MathUtils.clamp(value, 1, 6);
            }
        }

        public boolean isEmpty() { return size == 0; }
        public int size() { return size; }
        public int topColor() { return size == 0 ? 0 : colors[size - 1]; }
        public int free() { return colors.length - size; }

        private int topRun() {
            if (size == 0) return 0;
            int color = topColor(), run = 1;
            while (run < size && colors[size - 1 - run] == color) run++;
            return run;
        }

        private void remove(int count) {
            while (count-- > 0) colors[--size] = 0;
        }

        private void add(int color, int count) {
            while (count-- > 0) colors[size++] = color;
        }
    }

    private static final class Pour {
        private final Tube from, to;
        private final int color, count, direction;
        private final float pourTime;
        private int phase;
        private float time, transfer;
        private boolean committed;

        private Pour(Tube from, Tube to, int color, int count) {
            this.from = from;
            this.to = to;
            this.color = color;
            this.count = count;
            direction = from.homeX < to.homeX ? 1 : -1;
            pourTime = POUR_TIME_PER_CELL * count;
        }

        private boolean update(float delta) {
            time += delta;
            if (phase == 0) {
                float a = Interpolation.sine.apply(MathUtils.clamp(time / MOVE_TIME, 0, 1));
                float targetX = to.homeX - direction * 82f;
                float targetY = to.homeY + 105f;
                from.pivotX = MathUtils.lerp(from.homeX, targetX, a);
                from.pivotY = MathUtils.lerp(from.homeY, targetY, a);
                if (time >= MOVE_TIME) next();
            } else if (phase == 1) {
                float a = Interpolation.sine.apply(MathUtils.clamp(time / ROTATE_TIME, 0, 1));
                from.angle = MathUtils.lerp(0, -direction * 68f, a);
                if (time >= ROTATE_TIME) next();
            } else if (phase == 2) {
                transfer = Interpolation.smooth.apply(MathUtils.clamp(time / pourTime, 0, 1));
                if (time >= pourTime) {
                    from.remove(count);
                    to.add(color, count);
                    committed = true;
                    next();
                }
            } else if (phase == 3) {
                float a = Interpolation.sine.apply(MathUtils.clamp(time / ROTATE_TIME, 0, 1));
                from.angle = MathUtils.lerp(-direction * 68f, 0, a);
                if (time >= ROTATE_TIME) next();
            } else {
                float a = Interpolation.sine.apply(MathUtils.clamp(time / RETURN_TIME, 0, 1));
                from.pivotX = MathUtils.lerp(to.homeX - direction * 82f, from.homeX, a);
                from.pivotY = MathUtils.lerp(to.homeY + 105f, from.homeY, a);
                if (time >= RETURN_TIME) {
                    from.pivotX = from.homeX;
                    from.pivotY = from.homeY;
                    from.angle = 0;
                    return true;
                }
            }
            return false;
        }

        private void next() {
            phase++;
            time = 0;
        }
    }

    public void dispose() {
        shapes.dispose();
    }
}
