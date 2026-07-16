package com.kw.gdx.exporter;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter;

public final class ExportSettingsIO {
    private ExportSettingsIO() {
    }

    public static void save(FileHandle file, ExportSettings settings) {
        if (file == null) throw new IllegalArgumentException("file cannot be null");
        if (settings == null) throw new IllegalArgumentException("settings cannot be null");
        settings.normalize();
        Json json = createJson();
        ExportSettingsEnvelope envelope = new ExportSettingsEnvelope();
        envelope.type = settings.getClass().getName();
        envelope.format = settings.format().name();
        envelope.settings = settings;
        file.parent().mkdirs();
        json.toJson(envelope, file);
    }

    public static ExportSettings load(FileHandle file) {
        if (file == null) throw new IllegalArgumentException("file cannot be null");
        if (!file.exists()) throw new IllegalArgumentException("settings file does not exist: " + file.path());
        Json json = createJson();
        ExportSettingsEnvelope envelope = json.fromJson(ExportSettingsEnvelope.class, file);
        if (envelope == null || envelope.settings == null) {
            throw new IllegalArgumentException("invalid export settings file: " + file.path());
        }
        envelope.settings.normalize();
        return envelope.settings;
    }

    private static Json createJson() {
        Json json = new Json();
        json.setOutputType(JsonWriter.OutputType.json);
        json.setUsePrototypes(false);
        json.addClassTag("json", JsonExportSettings.class);
        json.addClassTag("binary", BinaryExportSettings.class);
        json.addClassTag("png", PngExportSettings.class);
        json.addClassTag("jpeg", JpegExportSettings.class);
        json.addClassTag("gif", GifExportSettings.class);
        json.addClassTag("apng", ApngExportSettings.class);
        json.addClassTag("psd", PsdExportSettings.class);
        json.addClassTag("avi", AviExportSettings.class);
        json.addClassTag("mov", MovExportSettings.class);
        return json;
    }

    public static class ExportSettingsEnvelope {
        public String type;
        public String format;
        public ExportSettings settings;
    }
}
