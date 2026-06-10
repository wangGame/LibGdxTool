package com.libGdx.test.textpacker;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.math.MathUtils;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

public class TextureChannelPacker {

    public enum OutputChannel {
        R, G, B, A
    }

    public enum InputChannel {
        R, G, B, A
    }

    public enum SizeMode {
        STRICT,
        SCALE_TO_FIRST
    }

    public enum SourceType {
        INPUT_CHANNEL,
        CONSTANT
    }

    public static class InputSlot {
        public final String name;
        public final Pixmap pixmap;

        public InputSlot(String name, Pixmap pixmap) {
            this.name = name;
            this.pixmap = pixmap;
        }
    }

    public static class ChannelSource {
        public final SourceType sourceType;
        public final String inputName;
        public final InputChannel inputChannel;
        public final int constantValue; // 0~255

        private ChannelSource(SourceType sourceType, String inputName, InputChannel inputChannel, int constantValue) {
            this.sourceType = sourceType;
            this.inputName = inputName;
            this.inputChannel = inputChannel;
            this.constantValue = constantValue;
        }

        public static ChannelSource fromInput(String inputName, InputChannel inputChannel) {
            return new ChannelSource(SourceType.INPUT_CHANNEL, inputName, inputChannel, 0);
        }

        public static ChannelSource constant(int value) {
            return new ChannelSource(SourceType.CONSTANT, null, null, MathUtils.clamp(value, 0, 255));
        }
    }

    public static class PackConfig {
        public final Map<String, FileHandle> inputFiles = new HashMap<>();
        public final EnumMap<OutputChannel, ChannelSource> outputMapping = new EnumMap<>(OutputChannel.class);
        public SizeMode sizeMode = SizeMode.STRICT;
        public boolean disposeInputsAfterPack = true;
    }

    public static Pixmap pack(PackConfig config) {
        validateConfig(config);

        Map<String, Pixmap> loadedPixmaps = new HashMap<>();
        try {
            for (Map.Entry<String, FileHandle> e : config.inputFiles.entrySet()) {
                loadedPixmaps.put(e.getKey(), new Pixmap(e.getValue()));
            }

            return packLoadedPixmaps(loadedPixmaps, config);
        } finally {
            if (config.disposeInputsAfterPack) {
                for (Pixmap pixmap : loadedPixmaps.values()) {
                    if (pixmap != null) {
                        pixmap.dispose();
                    }
                }
            }
        }
    }

    public static void packToFile(PackConfig config, FileHandle outputFile) {
        Pixmap output = pack(config);
        try {
            PixmapIOUtil.writePng(outputFile, output);
        } finally {
            output.dispose();
        }
    }

    private static Pixmap packLoadedPixmaps(Map<String, Pixmap> loadedPixmaps, PackConfig config) {
        if (loadedPixmaps.isEmpty()) {
            throw new IllegalArgumentException("No input textures provided.");
        }

        String firstKey = config.inputFiles.keySet().iterator().next();
        Pixmap first = loadedPixmaps.get(firstKey);
        if (first == null) {
            throw new IllegalStateException("First input pixmap could not be loaded: " + firstKey);
        }

        int targetWidth = first.getWidth();
        int targetHeight = first.getHeight();

        Map<String, Pixmap> preparedPixmaps = new HashMap<>();
        Map<String, Pixmap> scaledTempPixmaps = new HashMap<>();

        try {
            for (Map.Entry<String, Pixmap> entry : loadedPixmaps.entrySet()) {
                String name = entry.getKey();
                Pixmap src = entry.getValue();

                if (src.getWidth() == targetWidth && src.getHeight() == targetHeight) {
                    preparedPixmaps.put(name, src);
                    continue;
                }

                if (config.sizeMode == SizeMode.STRICT) {
                    throw new IllegalArgumentException(
                            "Input size mismatch for '" + name + "'. Expected "
                                    + targetWidth + "x" + targetHeight + " but got "
                                    + src.getWidth() + "x" + src.getHeight()
                    );
                }

                Pixmap scaled = new Pixmap(targetWidth, targetHeight, Pixmap.Format.RGBA8888);
                scaled.setFilter(Pixmap.Filter.BiLinear);
                scaled.drawPixmap(
                        src,
                        0, 0, src.getWidth(), src.getHeight(),
                        0, 0, targetWidth, targetHeight
                );

                scaledTempPixmaps.put(name, scaled);
                preparedPixmaps.put(name, scaled);
            }

            Pixmap output = new Pixmap(targetWidth, targetHeight, Pixmap.Format.RGBA8888);

            for (int y = 0; y < targetHeight; y++) {
                for (int x = 0; x < targetWidth; x++) {
                    int r = resolveOutputChannel(OutputChannel.R, x, y, preparedPixmaps, config);
                    int g = resolveOutputChannel(OutputChannel.G, x, y, preparedPixmaps, config);
                    int b = resolveOutputChannel(OutputChannel.B, x, y, preparedPixmaps, config);
                    int a = resolveOutputChannel(OutputChannel.A, x, y, preparedPixmaps, config);

                    int rgba = ((r & 0xff) << 24)
                            | ((g & 0xff) << 16)
                            | ((b & 0xff) << 8)
                            | (a & 0xff);

                    output.drawPixel(x, y, rgba);
                }
            }

            return output;
        } finally {
            for (Pixmap temp : scaledTempPixmaps.values()) {
                temp.dispose();
            }
        }
    }

    private static int resolveOutputChannel(
            OutputChannel outputChannel,
            int x,
            int y,
            Map<String, Pixmap> preparedPixmaps,
            PackConfig config
    ) {
        ChannelSource source = config.outputMapping.get(outputChannel);
        if (source == null) {
            return outputChannel == OutputChannel.A ? 255 : 0;
        }

        if (source.sourceType == SourceType.CONSTANT) {
            return source.constantValue;
        }

        Pixmap pixmap = preparedPixmaps.get(source.inputName);
        if (pixmap == null) {
            throw new IllegalArgumentException("Missing referenced input texture: " + source.inputName);
        }

        int pixel = pixmap.getPixel(x, y);
        return extractChannel(pixel, source.inputChannel);
    }

    private static int extractChannel(int rgba8888, InputChannel channel) {
        switch (channel) {
            case R:
                return (rgba8888 >>> 24) & 0xff;
            case G:
                return (rgba8888 >>> 16) & 0xff;
            case B:
                return (rgba8888 >>> 8) & 0xff;
            case A:
                return rgba8888 & 0xff;
            default:
                throw new IllegalArgumentException("Unsupported channel: " + channel);
        }
    }

    private static void validateConfig(PackConfig config) {
        if (config == null) {
            throw new IllegalArgumentException("PackConfig cannot be null.");
        }
        if (config.inputFiles.isEmpty()) {
            throw new IllegalArgumentException("At least one input file is required.");
        }

        for (Map.Entry<OutputChannel, ChannelSource> e : config.outputMapping.entrySet()) {
            ChannelSource source = e.getValue();
            if (source == null) {
                throw new IllegalArgumentException("Null mapping for output channel: " + e.getKey());
            }

            if (source.sourceType == SourceType.INPUT_CHANNEL) {
                if (source.inputName == null || source.inputName.isEmpty()) {
                    throw new IllegalArgumentException("Input source name missing for channel: " + e.getKey());
                }
                if (!config.inputFiles.containsKey(source.inputName)) {
                    throw new IllegalArgumentException(
                            "Mapping for " + e.getKey() + " references unknown input: " + source.inputName
                    );
                }
                if (source.inputChannel == null) {
                    throw new IllegalArgumentException("Input channel missing for output: " + e.getKey());
                }
            }
        }
    }
}