package com.kw.gdx.exporter;

import com.badlogic.gdx.graphics.Color;

public abstract class RenderExportSettings extends ExportSettings {
    public String exportType = "current";
    public ExportSelection skeletonType = ExportSelection.CURRENT;
    public String skeleton;
    public ExportSelection animationType = ExportSelection.CURRENT;
    public String animation;
    public String[] animations;
    public ExportSelection skinType = ExportSelection.CURRENT;
    public boolean skinNone;
    public String skin;
    public boolean maxBounds;
    public boolean renderImages = true;
    public boolean renderBones;
    public boolean renderOthers = true;
    public boolean linearFiltering = true;
    public float scale = 100.0f;
    public int fitWidth;
    public int fitHeight;
    public boolean enlarge;
    public Color background = new Color(0, 0, 0, 0);
    public boolean transparentBackground = true;
    public float fps = 30.0f;
    public boolean lastFrame;
    public int cropX;
    public int cropY;
    public int cropWidth;
    public int cropHeight;
    public int rangeStart = -1;
    public int rangeEnd = -1;
    public boolean pad;
    public int msaa;

    protected RenderExportSettings() {
    }

    protected RenderExportSettings(String name) {
        super(name);
    }

    public boolean exportsCurrentOnly() {
        return "current".equals(exportType);
    }
}
