package com.libGdx.test.beser;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.kw.gdx.asset.Asset;
import com.libGdx.test.base.LibGdxTestMain;

import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileNameExtensionFilter;


public class BezierTool extends LibGdxTestMain {
    private static final String DEFAULT_JSON_FILE = "bezier_points.json";

    private ShapeRenderer renderer;
    private final Array<Vector2> controlPoints = new Array<>();
    private Image previewImage;
    private BezierEditorCanvas editorCanvas;
    private float previewDuration = 2f;
    private String lastJsonPath = DEFAULT_JSON_FILE;

    public static void main(String[] args) {
        BezierTool b = new BezierTool();
        b.start();
    }

    @Override
    public void useShow(Stage stage) {
        super.useShow(stage);

        renderer = new ShapeRenderer();

        // 给一个默认样例，打开后可以直接预览。
        controlPoints.add(new Vector2(100, 260));
        controlPoints.add(new Vector2(280, 1100));
        controlPoints.add(new Vector2(700, 1300));
        controlPoints.add(new Vector2(900, 380));

        Texture texture = Asset.getAsset().getTexture("assets/7.png");
        if (texture == null) {
            texture = new Texture("assets/7.png");
        }

        previewImage = new Image(texture);
        previewImage.setSize(60, 60);
        previewImage.setOrigin(previewImage.getWidth() * 0.5f, previewImage.getHeight() * 0.5f);
        addActor(previewImage);

        editorCanvas = new BezierEditorCanvas();
        editorCanvas.setBounds(0, 0, stage.getViewport().getWorldWidth(), stage.getViewport().getWorldHeight());
        addActor(editorCanvas);
        stage.setKeyboardFocus(editorCanvas);

        updatePreviewImageToStart();
        printGuide();
    }

    private void printGuide() {
        Gdx.app.log("BezierEditor", "左键空白处: 加点 | 左键点上拖拽: 移动点 | 右键点上: 删点");
        Gdx.app.log("BezierEditor", "左键点在线段附近: 在两点中间插点 | 空格: 运行预览 | E: 导出(可选路径和文件名) | I: 读取(可选文件) | C: 清空点");
    }

    private void startPreview() {
        if (controlPoints.size < 2) {
            Gdx.app.log("BezierEditor", "至少需要 2 个点才能预览");
            return;
        }

        previewImage.clearActions();

        BUL2 action = new BUL2(copyControlPoints());
        action.setDuration(previewDuration);
        action.setInterpolation(Interpolation.sineOut);
        previewImage.addAction(action);
    }

    private Array<Vector2> copyControlPoints() {
        Array<Vector2> copy = new Array<>();
        for (Vector2 point : controlPoints) {
            copy.add(new Vector2(point));
        }
        return copy;
    }

    private void updatePreviewImageToStart() {
        if (controlPoints.size == 0) {
            return;
        }
        Vector2 start = controlPoints.first();
        previewImage.setPosition(start.x, start.y);
    }

    private void exportPoints() {
        String chosenPath = chooseJsonFile(true);
        if (chosenPath == null) {
            Gdx.app.log("BezierEditor", "已取消导出");
            return;
        }

        FileHandle handle = Gdx.files.absolute(chosenPath);
        Json json = new Json();
        ExportData data = new ExportData();
        data.version = 1;
        data.name = fileNameWithoutExt(handle.file().getName());
        data.points = new Array<>();

        for (int i = 0; i < controlPoints.size; i++) {
            Vector2 point = controlPoints.get(i);
            ExportPoint exportPoint = new ExportPoint();
            exportPoint.index = i;
            exportPoint.x = point.x;
            exportPoint.y = point.y;
            data.points.add(exportPoint);
        }

        String out = json.prettyPrint(data);
        handle.writeString(out, false, "UTF-8");
        lastJsonPath = handle.file().getAbsolutePath();
        Gdx.app.log("BezierEditor", "已导出: " + handle.file().getAbsolutePath());
    }

    private void importPoints() {
        String chosenPath = chooseJsonFile(false);
        if (chosenPath == null) {
            Gdx.app.log("BezierEditor", "已取消读取");
            return;
        }

        FileHandle handle = Gdx.files.absolute(chosenPath);
        if (!handle.exists()) {
            Gdx.app.log("BezierEditor", "读取失败, 文件不存在: " + handle.file().getAbsolutePath());
            return;
        }

        try {
            String text = handle.readString("UTF-8");
            Json json = new Json();
            ExportData data = json.fromJson(ExportData.class, text);
            if (data == null || data.points == null || data.points.size == 0) {
                Gdx.app.log("BezierEditor", "读取成功, 但点集为空");
                return;
            }

            controlPoints.clear();
            for (ExportPoint p : data.points) {
                controlPoints.add(new Vector2(p.x, p.y));
            }

            previewImage.clearActions();
            updatePreviewImageToStart();
            lastJsonPath = handle.file().getAbsolutePath();
            Gdx.app.log("BezierEditor", "已读取 " + controlPoints.size + " 个点: " + handle.file().getAbsolutePath());
        } catch (Exception e) {
            Gdx.app.error("BezierEditor", "读取失败, JSON 格式不正确", e);
        }
    }

    private String chooseJsonFile(boolean saveMode) {
        final String[] selectedPath = new String[1];
        Runnable chooserTask = () -> {
            JFileChooser chooser = new JFileChooser();
            chooser.setDialogTitle(saveMode ? "导出贝塞尔点" : "读取贝塞尔点");
            chooser.setFileFilter(new FileNameExtensionFilter("JSON 文件 (*.json)", "json"));

            File seed = Gdx.files.local(lastJsonPath).file();
            File directory = seed.isDirectory() ? seed : seed.getParentFile();
            if (directory != null && directory.exists()) {
                chooser.setCurrentDirectory(directory);
            }

            if (saveMode) {
                String name = seed.isDirectory() ? DEFAULT_JSON_FILE : seed.getName();
                chooser.setSelectedFile(new File(name));
            }

            int result = saveMode ? chooser.showSaveDialog(null) : chooser.showOpenDialog(null);
            if (result == JFileChooser.APPROVE_OPTION && chooser.getSelectedFile() != null) {
                String path = chooser.getSelectedFile().getAbsolutePath();
                if (saveMode && !path.toLowerCase().endsWith(".json")) {
                    path += ".json";
                }
                selectedPath[0] = path;
            }
        };

        try {
            if (SwingUtilities.isEventDispatchThread()) {
                chooserTask.run();
            } else {
                SwingUtilities.invokeAndWait(chooserTask);
            }
        } catch (Exception e) {
            Gdx.app.error("BezierEditor", "打开文件选择器失败", e);
            return null;
        }

        return selectedPath[0];
    }

    private String fileNameWithoutExt(String fileName) {
        int dot = fileName.lastIndexOf('.');
        if (dot <= 0) {
            return fileName;
        }
        return fileName.substring(0, dot);
    }

    private class BezierEditorCanvas extends Actor {
        private final Vector2 temp = new Vector2();
        private final Vector2 temp2 = new Vector2();
        private final int samples = 120;
        private final float pointRadius = 20f;
        private final float segmentHitRadius = 18f;

        private int selectedIndex = -1;

        BezierEditorCanvas() {
            addListener(new InputListener() {
                @Override
                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                    int index = findPointIndex(x, y);

                    if (button == Input.Buttons.RIGHT) {
                        if (index >= 0) {
                            controlPoints.removeIndex(index);
                            selectedIndex = -1;
                            previewImage.clearActions();
                            updatePreviewImageToStart();
                        }
                        return true;
                    }

                    if (button != Input.Buttons.LEFT) {
                        return false;
                    }

                    if (index >= 0) {
                        selectedIndex = index;
                    } else {
                        int insertIndex = findSegmentInsertIndex(x, y);
                        if (insertIndex >= 0) {
                            Vector2 p0 = controlPoints.get(insertIndex - 1);
                            Vector2 p1 = controlPoints.get(insertIndex);
                            Vector2 mid = new Vector2((p0.x + p1.x) * 0.5f, (p0.y + p1.y) * 0.5f);
                            controlPoints.insert(insertIndex, mid);
                            selectedIndex = insertIndex;
                        } else {
                            controlPoints.add(new Vector2(x, y));
                            selectedIndex = controlPoints.size - 1;
                        }
                        previewImage.clearActions();
                        updatePreviewImageToStart();
                    }
                    return true;
                }

                @Override
                public void touchDragged(InputEvent event, float x, float y, int pointer) {
                    if (selectedIndex < 0 || selectedIndex >= controlPoints.size) {
                        return;
                    }
                    controlPoints.get(selectedIndex).set(x, y);
                    previewImage.clearActions();
                    updatePreviewImageToStart();
                }

                @Override
                public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                    selectedIndex = -1;
                }

                @Override
                public boolean keyDown(InputEvent event, int keycode) {
                    if (keycode == Input.Keys.SPACE) {
                        startPreview();
                        return true;
                    }
                    if (keycode == Input.Keys.E) {
                        exportPoints();
                        return true;
                    }
                    if (keycode == Input.Keys.I) {
                        importPoints();
                        return true;
                    }
                    if (keycode == Input.Keys.C) {
                        controlPoints.clear();
                        previewImage.clearActions();
                        return true;
                    }
                    return false;
                }
            });
        }

        @Override
        public void draw(Batch batch, float parentAlpha) {
            batch.end();

            renderer.setProjectionMatrix(batch.getProjectionMatrix());

            renderer.begin(ShapeRenderer.ShapeType.Line);

            renderer.setColor(Color.DARK_GRAY);
            for (int i = 1; i < controlPoints.size; i++) {
                Vector2 p0 = controlPoints.get(i - 1);
                Vector2 p1 = controlPoints.get(i);
                renderer.line(p0.x, p0.y, p1.x, p1.y);
            }

            if (controlPoints.size > 1) {
                renderer.setColor(Color.CYAN);
                eval(0f, temp);
                for (int i = 1; i <= samples; i++) {
                    float t = i / (float) samples;
                    eval(t, temp2);
                    renderer.line(temp.x, temp.y, temp2.x, temp2.y);
                    temp.set(temp2);
                }
            }

            renderer.end();

            renderer.begin(ShapeRenderer.ShapeType.Filled);
            for (int i = 0; i < controlPoints.size; i++) {
                Vector2 point = controlPoints.get(i);
                renderer.setColor(i == selectedIndex ? Color.YELLOW : Color.WHITE);
                renderer.circle(point.x, point.y, pointRadius, 20);
            }
            renderer.end();

            batch.begin();
        }

        private int findPointIndex(float x, float y) {
            for (int i = controlPoints.size - 1; i >= 0; i--) {
                Vector2 point = controlPoints.get(i);
                if (point.dst2(x, y) <= pointRadius * pointRadius) {
                    return i;
                }
            }
            return -1;
        }

        private int findSegmentInsertIndex(float x, float y) {
            if (controlPoints.size < 2) {
                return -1;
            }

            float bestDist2 = segmentHitRadius * segmentHitRadius;
            int bestInsertIndex = -1;

            for (int i = 0; i < controlPoints.size - 1; i++) {
                Vector2 a = controlPoints.get(i);
                Vector2 b = controlPoints.get(i + 1);

                float abx = b.x - a.x;
                float aby = b.y - a.y;
                float abLen2 = abx * abx + aby * aby;
                if (abLen2 <= 0.0001f) {
                    continue;
                }

                float apx = x - a.x;
                float apy = y - a.y;
                float t = (apx * abx + apy * aby) / abLen2;
                if (t < 0f || t > 1f) {
                    continue;
                }

                float px = a.x + abx * t;
                float py = a.y + aby * t;
                float dx = x - px;
                float dy = y - py;
                float dist2 = dx * dx + dy * dy;
                if (dist2 <= bestDist2) {
                    bestDist2 = dist2;
                    bestInsertIndex = i + 1;
                }
            }

            return bestInsertIndex;
        }

        private void eval(float t, Vector2 out) {
            if (controlPoints.size == 0) {
                out.set(0, 0);
                return;
            }
            if (controlPoints.size == 1) {
                out.set(controlPoints.first());
                return;
            }

            t = MathUtils.clamp(t, 0f, 1f);
            Array<Vector2> tmp = new Array<>(controlPoints.size);
            for (Vector2 point : controlPoints) {
                tmp.add(new Vector2(point));
            }

            for (int level = tmp.size - 1; level > 0; level--) {
                for (int i = 0; i < level; i++) {
                    Vector2 a = tmp.get(i);
                    Vector2 b = tmp.get(i + 1);
                    a.x = a.x + (b.x - a.x) * t;
                    a.y = a.y + (b.y - a.y) * t;
                }
            }

            out.set(tmp.first());
        }
    }

    private static class ExportData {
        public int version;
        public String name;
        public Array<ExportPoint> points;
    }

    private static class ExportPoint {
        public int index;
        public float x;
        public float y;
    }
}
