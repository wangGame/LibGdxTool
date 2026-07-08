package com.kw.gdx.exporter;

import com.badlogic.gdx.files.FileHandle;

public class SpineExportService {
    public ExportResult export(ExportJob job) {
        validate(job);
        long start = System.currentTimeMillis();
        ExportResult result = new ExportResult();
        job.settings.normalize();

        if (job.settings instanceof DataExportSettings) {
            exportData(job, result);
        } else if (job.settings instanceof RenderExportSettings) {
            throw new UnsupportedOperationException("Render export requires a skeleton renderer and format encoder integration.");
        } else {
            throw new UnsupportedOperationException("Unsupported export settings: " + job.settings.getClass().getName());
        }

        result.elapsedMillis = System.currentTimeMillis() - start;
        return result;
    }

    private void exportData(ExportJob job, ExportResult result) {
        String extension = job.settings.defaultExtension();
        String outputName = job.outputName == null || job.outputName.length() == 0 ? job.input.nameWithoutExtension() : job.outputName;
        FileHandle output = job.outputDir.child(outputName + extension);
        output.parent().mkdirs();

        boolean sourceJson = "json".equalsIgnoreCase(job.input.extension());
        boolean sourceBinary = "skel".equalsIgnoreCase(job.input.extension());
        if (job.settings instanceof JsonExportSettings && !sourceJson) {
            result.warnings.add("Input is not JSON. Copying source bytes instead of converting skeleton data.");
        }
        if (job.settings instanceof BinaryExportSettings && !sourceBinary) {
            result.warnings.add("Input is not SKEL. Copying source bytes instead of converting skeleton data.");
        }
        output.writeBytes(job.input.readBytes(), false);
        result.outputFiles.add(output);

        DataExportSettings dataSettings = (DataExportSettings) job.settings;
        if (dataSettings.packAtlas != null && dataSettings.packAtlas.enabled) {
            result.warnings.add("Texture atlas packing is configured but not executed by this lightweight service.");
        }
        if (dataSettings.cleanUp) {
            result.warnings.add("Clean up unnecessary keys requires editable animation data and is not applied to copied files.");
        }
    }

    private static void validate(ExportJob job) {
        if (job == null) throw new IllegalArgumentException("job cannot be null");
        if (job.settings == null) throw new IllegalArgumentException("settings cannot be null");
        if (job.outputDir == null) throw new IllegalArgumentException("outputDir cannot be null");
        if (job.settings instanceof DataExportSettings) {
            if (job.input == null) throw new IllegalArgumentException("input cannot be null for data export");
            if (!job.input.exists()) throw new IllegalArgumentException("input does not exist: " + job.input.path());
            if (job.input.isDirectory()) throw new IllegalArgumentException("input must be a file: " + job.input.path());
        }
    }
}
