package com.libGdx.test.textpacker;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.math.MathUtils;

import java.util.EnumMap;

public class TextureChannelPacker2 {

    public enum InputSlot {
        INPUT1,
        INPUT2,
        INPUT3,
        INPUT4
    }

    public enum ColorChannel {
        R, G, B, A
    }

    public enum OutputChannel {
        R, G, B, A
    }

    public enum SizeMode {
        STRICT,
        SCALE_TO_FIRST
    }

    public enum SourceKind {
        INPUT,
        CONSTANT
    }

    public static class InputSource {
        public FileHandle file;
        public String label;

        public InputSource(FileHandle file) {
            this(file, null);
        }

        public InputSource(FileHandle file, String label) {
            this.file = file;
            this.label = label;
        }
    }

    public static class ChannelRef {
        public final InputSlot slot;
        public final ColorChannel channel;

        public ChannelRef(InputSlot slot, ColorChannel channel) {
            if (slot == null) {
                throw new IllegalArgumentException("slot cannot be null");
            }
            if (channel == null) {
                throw new IllegalArgumentException("channel cannot be null");
            }
            this.slot = slot;
            this.channel = channel;
        }

        public static ChannelRef of(InputSlot slot, ColorChannel channel) {
            return new ChannelRef(slot, channel);
        }
    }

    public static class ChannelSource {
        public final SourceKind kind;
        public final ChannelRef ref;
        public final int constant;
        public final boolean invert;
        public final float multiplier;
        public final Integer fallbackConstant;

        private ChannelSource(
                SourceKind kind,
                ChannelRef ref,
                int constant,
                boolean invert,
                float multiplier,
                Integer fallbackConstant
        ) {
            this.kind = kind;
            this.ref = ref;
            this.constant = MathUtils.clamp(constant, 0, 255);
            this.invert = invert;
            this.multiplier = multiplier;
            this.fallbackConstant = fallbackConstant == null ? null : MathUtils.clamp(fallbackConstant, 0, 255);
        }

        public static ChannelSource fromInput(InputSlot slot, ColorChannel channel) {
            return new ChannelSource(SourceKind.INPUT, ChannelRef.of(slot, channel), 0, false, 1f, null);
        }

        public static ChannelSource fromInput(InputSlot slot, ColorChannel channel, boolean invert, float multiplier) {
            return new ChannelSource(SourceKind.INPUT, ChannelRef.of(slot, channel), 0, invert, multiplier, null);
        }

        public static ChannelSource fromInput(
                InputSlot slot,
                ColorChannel channel,
                boolean invert,
                float multiplier,
                Integer fallbackConstant
        ) {
            return new ChannelSource(
                    SourceKind.INPUT,
                    ChannelRef.of(slot, channel),
                    0,
                    invert,
                    multiplier,
                    fallbackConstant
            );
        }

        public static ChannelSource constant(int value) {
            return new ChannelSource(SourceKind.CONSTANT, null, value, false, 1f, null);
        }

        public static ChannelSource constant(int value, boolean invert, float multiplier) {
            return new ChannelSource(SourceKind.CONSTANT, null, value, invert, multiplier, null);
        }

        public ChannelSource withInvert(boolean invert) {
            return new ChannelSource(kind, ref, constant, invert, multiplier, fallbackConstant);
        }

        public ChannelSource withMultiplier(float multiplier) {
            return new ChannelSource(kind, ref, constant, invert, multiplier, fallbackConstant);
        }

        public ChannelSource withFallbackConstant(Integer fallbackConstant) {
            return new ChannelSource(kind, ref, constant, invert, multiplier, fallbackConstant);
        }
    }

    public static class PackConfig {
        public final EnumMap<InputSlot, InputSource> inputs = new EnumMap<>(InputSlot.class);
        public final EnumMap<OutputChannel, ChannelSource> mapping = new EnumMap<>(OutputChannel.class);

        public SizeMode sizeMode = SizeMode.STRICT;
        public boolean disposeLoadedPixmaps = true;
        public Pixmap.Filter scalingFilter = Pixmap.Filter.BiLinear;

        /**
         * 如果为 true，寻找第一个有效输入作为基准尺寸；
         * 如果为 false，则必须 INPUT1 存在且以它为基准。
         */
        public boolean useFirstAvailableInputAsSizeBase = true;
    }

    public static class PackResult {
        public final Pixmap pixmap;
        public final int width;
        public final int height;

        public PackResult(Pixmap pixmap) {
            this.pixmap = pixmap;
            this.width = pixmap.getWidth();
            this.height = pixmap.getHeight();
        }
    }

    public static PackResult pack(PackConfig config) {
        validateConfig(config);

        EnumMap<InputSlot, Pixmap> loaded = new EnumMap<>(InputSlot.class);
        try {
            for (InputSlot slot : InputSlot.values()) {
                InputSource source = config.inputs.get(slot);
                if (source != null && source.file != null) {
                    loaded.put(slot, new Pixmap(source.file));
                }
            }

            Pixmap output = packLoaded(config, loaded);
            return new PackResult(output);
        } finally {
            if (config.disposeLoadedPixmaps) {
                for (Pixmap pixmap : loaded.values()) {
                    if (pixmap != null) {
                        pixmap.dispose();
                    }
                }
            }
        }
    }

    public static void packToFile(PackConfig config, FileHandle outputFile) {
        PackResult result = pack(config);
        try {
            PixmapIOUtil.writePng(outputFile, result.pixmap);
        } finally {
            result.pixmap.dispose();
        }
    }

    private static Pixmap packLoaded(PackConfig config, EnumMap<InputSlot, Pixmap> loaded) {
        InputSlot baseSlot = findBaseSlot(config, loaded);
        Pixmap basePixmap = loaded.get(baseSlot);

        if (basePixmap == null) {
            throw new IllegalStateException("Base pixmap is null for slot: " + baseSlot);
        }

        int targetWidth = basePixmap.getWidth();
        int targetHeight = basePixmap.getHeight();

        EnumMap<InputSlot, Pixmap> prepared = new EnumMap<>(InputSlot.class);
        EnumMap<InputSlot, Pixmap> scaledTemps = new EnumMap<>(InputSlot.class);

        try {
            for (InputSlot slot : InputSlot.values()) {
                Pixmap src = loaded.get(slot);
                if (src == null) {
                    continue;
                }

                if (src.getWidth() == targetWidth && src.getHeight() == targetHeight) {
                    prepared.put(slot, src);
                    continue;
                }

                if (config.sizeMode == SizeMode.STRICT) {
                    throw new IllegalArgumentException(
                            "Input size mismatch at " + slot +
                                    ": expected " + targetWidth + "x" + targetHeight +
                                    ", got " + src.getWidth() + "x" + src.getHeight()
                    );
                }

                Pixmap scaled = new Pixmap(targetWidth, targetHeight, Pixmap.Format.RGBA8888);
                scaled.setFilter(config.scalingFilter);
                scaled.drawPixmap(
                        src,
                        0, 0, src.getWidth(), src.getHeight(),
                        0, 0, targetWidth, targetHeight
                );

                prepared.put(slot, scaled);
                scaledTemps.put(slot, scaled);
            }

            Pixmap output = new Pixmap(targetWidth, targetHeight, Pixmap.Format.RGBA8888);

            for (int y = 0; y < targetHeight; y++) {
                for (int x = 0; x < targetWidth; x++) {
                    int r = resolveChannel(config, prepared, OutputChannel.R, x, y, 0);
                    int g = resolveChannel(config, prepared, OutputChannel.G, x, y, 0);
                    int b = resolveChannel(config, prepared, OutputChannel.B, x, y, 0);
                    int a = resolveChannel(config, prepared, OutputChannel.A, x, y, 255);

                    int rgba = ((r & 0xff) << 24)
                            | ((g & 0xff) << 16)
                            | ((b & 0xff) << 8)
                            | (a & 0xff);

                    output.drawPixel(x, y, rgba);
                }
            }

            return output;
        } finally {
            for (Pixmap temp : scaledTemps.values()) {
                temp.dispose();
            }
        }
    }

    private static InputSlot findBaseSlot(PackConfig config, EnumMap<InputSlot, Pixmap> loaded) {
        if (!config.useFirstAvailableInputAsSizeBase) {
            if (!loaded.containsKey(InputSlot.INPUT1)) {
                throw new IllegalArgumentException("INPUT1 is required as size base.");
            }
            return InputSlot.INPUT1;
        }

        for (InputSlot slot : InputSlot.values()) {
            if (loaded.containsKey(slot)) {
                return slot;
            }
        }

        throw new IllegalArgumentException("No valid input pixmap loaded.");
    }

    private static int resolveChannel(
            PackConfig config,
            EnumMap<InputSlot, Pixmap> prepared,
            OutputChannel outputChannel,
            int x,
            int y,
            int defaultValue
    ) {
        ChannelSource source = config.mapping.get(outputChannel);
        if (source == null) {
            return defaultValue;
        }

        int value;

        if (source.kind == SourceKind.CONSTANT) {
            value = source.constant;
        } else {
            Pixmap pixmap = prepared.get(source.ref.slot);

            if (pixmap == null) {
                if (source.fallbackConstant != null) {
                    value = source.fallbackConstant;
                } else {
                    throw new IllegalArgumentException(
                            "Missing input for " + outputChannel + ": " + source.ref.slot
                    );
                }
            } else {
                int pixel = pixmap.getPixel(x, y);
                value = extractChannel(pixel, source.ref.channel);
            }
        }

        if (source.invert) {
            value = 255 - value;
        }

        value = Math.round(value * source.multiplier);
        return MathUtils.clamp(value, 0, 255);
    }

    private static int extractChannel(int rgba8888, ColorChannel channel) {
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
            throw new IllegalArgumentException("PackConfig cannot be null");
        }

        boolean hasAtLeastOneInput = false;
        for (InputSlot slot : InputSlot.values()) {
            InputSource source = config.inputs.get(slot);
            if (source != null && source.file != null) {
                hasAtLeastOneInput = true;
                break;
            }
        }

        if (!hasAtLeastOneInput) {
            throw new IllegalArgumentException("At least one input texture is required");
        }

        for (OutputChannel outputChannel : config.mapping.keySet()) {
            ChannelSource source = config.mapping.get(outputChannel);
            if (source == null) {
                throw new IllegalArgumentException("Null source for output channel: " + outputChannel);
            }

            if (source.kind == SourceKind.INPUT) {
                if (source.ref == null) {
                    throw new IllegalArgumentException("Missing ChannelRef for output channel: " + outputChannel);
                }

                InputSource input = config.inputs.get(source.ref.slot);
                if (input == null || input.file == null) {
                    if (source.fallbackConstant == null) {
                        throw new IllegalArgumentException(
                                "Output " + outputChannel + " references missing input " + source.ref.slot +
                                        " without fallbackConstant"
                        );
                    }
                }
            }

            if (Float.isNaN(source.multiplier) || Float.isInfinite(source.multiplier)) {
                throw new IllegalArgumentException("Invalid multiplier for output channel: " + outputChannel);
            }
        }
    }
}