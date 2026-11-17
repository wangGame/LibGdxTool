package com.libGdx.test.lizi;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEmitter;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.lang.reflect.Proxy;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/** Bakes libGDX 2D particle effects into Spine 4.2 animations without an OpenGL window. */
public final class ParticleToSpine {
    @FunctionalInterface
    public interface Motion {
        void positionAt(float seconds, Vector2 position);
    }

    private ParticleToSpine() {
    }

    public static void main(String[] args) throws IOException {
        if (args.length != 4 && args.length != 6) {
            System.err.println("Usage: ParticleToSpine <effect> <output-dir> <seconds> <fps> [end-x end-y]");
            return;
        }
        float seconds = Float.parseFloat(args[2]);
        float endX = args.length == 6 ? Float.parseFloat(args[4]) : 0;
        float endY = args.length == 6 ? Float.parseFloat(args[5]) : 0;
        if (!Float.isFinite(endX) || !Float.isFinite(endY)) {
            throw new IllegalArgumentException("Motion coordinates must be finite.");
        }
        Path json = convert(Paths.get(args[0]), Paths.get(args[1]), seconds, Integer.parseInt(args[3]),
                (time, position) -> position.set(endX * (time / seconds), endY * (time / seconds)));
        System.out.println(json.toAbsolutePath());
    }

    public static Path convert(Path input, Path output, float seconds, int fps, Motion motion) throws IOException {
        if (!Float.isFinite(seconds) || seconds <= 0 || fps < 1 || fps > 240
                || (double) seconds * fps >= Integer.MAX_VALUE) {
            throw new IllegalArgumentException("Use a positive duration and an FPS between 1 and 240.");
        }
        input = input.toAbsolutePath().normalize();
        String stem = input.getFileName().toString().replaceFirst("\\.[^.]+$", "");
        Map<Path, ImageSprite> images = new LinkedHashMap<>();
        ParticleEffect effect = new ParticleEffect() {
            @Override
            protected ParticleEmitter newEmitter(BufferedReader reader) throws IOException {
                return new RecordingEmitter(reader);
            }
        };
        effect.loadEmitters(new FileHandle(input.toFile()));

        for (ParticleEmitter emitter : effect.getEmitters()) {
            if (emitter.isPremultipliedAlpha()) {
                throw new IOException("Premultiplied particle blending is unsupported: " + emitter.getName());
            }
            Array<Sprite> sprites = new Array<>();
            for (String imagePath : emitter.getImagePaths()) {
                String portable = imagePath.replace('\\', '/');
                Path imageFile = input.resolveSibling(portable.substring(portable.lastIndexOf('/') + 1));
                ImageSprite sprite = images.get(imageFile);
                if (sprite == null) {
                    if (!Files.isRegularFile(imageFile)) {
                        throw new IOException("Missing particle image: " + imageFile);
                    }
                    BufferedImage image = ImageIO.read(imageFile.toFile());
                    if (image == null) throw new IOException("Unsupported image: " + imageFile);
                    sprite = new ImageSprite("particle_" + images.size(), image);
                    images.put(imageFile, sprite);
                }
                sprites.add(sprite);
            }
            if (sprites.isEmpty()) throw new IOException("Emitter has no images: " + emitter.getName());
            emitter.setSprites(sprites);
        }

        effect.start();
        int frames = (int) Math.ceil((double) seconds * fps);
        Vector2 position = new Vector2();
        float previous = 0;
        for (int frame = 0; frame <= frames; frame++) {
            float time = Math.min(seconds, frame / (float) fps);
            position.setZero();
            if (motion != null) motion.positionAt(time, position);
            effect.setPosition(position.x, position.y);
            if (frame > 0) effect.update(time - previous);
            for (ParticleEmitter emitter : effect.getEmitters()) {
                ((RecordingEmitter) emitter).record(time);
            }
            if (time >= seconds) break;
            previous = time;
        }

        JSONArray bones = new JSONArray().put(new JSONObject().put("name", "root"));
        JSONArray slots = new JSONArray();
        JSONObject skin = new JSONObject();
        JSONObject boneTimelines = new JSONObject();
        JSONObject slotTimelines = new JSONObject();
        int slotIndex = 0;
        for (ParticleEmitter emitter : effect.getEmitters()) {
            for (Track track : ((RecordingEmitter) emitter).tracks) {
                String name = "particle_slot_" + slotIndex++;
                bones.put(new JSONObject().put("name", name).put("parent", "root"));
                slots.put(new JSONObject().put("name", name).put("bone", name)
                        .put("blend", emitter.isAdditive() ? "additive" : "normal"));
                skin.put(name, track.regions);
                boneTimelines.put(name, new JSONObject().put("translate", track.translate)
                        .put("rotate", track.rotate).put("scale", track.scale));
                // Keep the requested animation duration even if emission stopped early.
                track.attachment(seconds, track.visibleImage, true);
                slotTimelines.put(name, new JSONObject().put("attachment", track.attachment)
                        .put("rgba", track.rgba));
            }
        }
        if (slotIndex == 0) throw new IOException("No particles appeared within the export duration.");

        JSONObject data = new JSONObject()
                .put("skeleton", new JSONObject().put("spine", "4.2.43").put("fps", fps))
                .put("bones", bones).put("slots", slots)
                .put("skins", new JSONArray().put(new JSONObject().put("name", "default").put("attachments", skin)))
                .put("animations", new JSONObject().put("animation", new JSONObject()
                        .put("bones", boneTimelines).put("slots", slotTimelines)));

        Files.createDirectories(output);
        StringBuilder atlas = new StringBuilder();
        for (ImageSprite sprite : images.values()) {
            // Each image is a separate page so texture sizes and pixels are preserved.
            String page = stem + "_" + sprite.region + ".png";
            ImageIO.write(sprite.image, "png", output.resolve(page).toFile());
            atlas.append(page).append('\n')
                    .append("size: ").append(sprite.image.getWidth()).append(',').append(sprite.image.getHeight())
                    .append("\nfilter: Linear,Linear\npma: false\n").append(sprite.region)
                    .append("\nbounds: 0,0,").append(sprite.image.getWidth()).append(',').append(sprite.image.getHeight())
                    .append("\n\n");
        }
        Files.write(output.resolve(stem + ".atlas"), atlas.toString().getBytes(StandardCharsets.UTF_8));
        Path json = output.resolve(stem + ".json");
        Files.write(json, data.toString().getBytes(StandardCharsets.UTF_8));
        return json;
    }

    private static final class ImageSprite extends Sprite {
        final String region;
        final BufferedImage image;

        ImageSprite(String region, BufferedImage image) {
            this.region = region;
            this.image = image;
            setSize(image.getWidth(), image.getHeight());
            setOriginCenter();
        }
    }

    private static final class RecordingEmitter extends ParticleEmitter {
        final List<Track> tracks = new ArrayList<>();
        int drawn;
        float time;

        RecordingEmitter(BufferedReader reader) throws IOException {
            super(reader);
        }

        @Override
        protected Particle newParticle(Sprite sprite) {
            return new RecordingParticle(sprite, this);
        }

        void record(float time) {
            this.time = time;
            drawn = 0;
            // draw visits live particles in their original render order.
            draw(NO_RENDER);
            for (int i = drawn; i < tracks.size(); i++) tracks.get(i).attachment(time, null, false);
        }

        void capture(RecordingParticle particle) {
            if (drawn == tracks.size()) tracks.add(new Track());
            tracks.get(drawn++).sample(time, particle);
        }
    }

    private static final class RecordingParticle extends ParticleEmitter.Particle {
        private final RecordingEmitter emitter;
        private ImageSprite image;

        RecordingParticle(Sprite sprite, RecordingEmitter emitter) {
            super(sprite);
            this.emitter = emitter;
        }

        @Override
        public void set(Sprite sprite) {
            super.set(sprite);
            image = (ImageSprite) sprite;
        }

        @Override
        public void setRegion(TextureRegion region) {
            // Animated emitters swap the source sprite here. No GPU texture is needed.
            image = (ImageSprite) region;
        }

        @Override
        public void draw(Batch batch) {
            emitter.capture(this);
        }
    }

    // ParticleEmitter.draw only calls blend setters on the batch.
    private static final Batch NO_RENDER = (Batch) Proxy.newProxyInstance(
            Batch.class.getClassLoader(), new Class<?>[]{Batch.class}, (proxy, method, args) -> {
                if (!method.getName().equals("setBlendFunction")) {
                    throw new UnsupportedOperationException(method.getName());
                }
                return null;
            });

    private static final class Track {
        final JSONObject regions = new JSONObject();
        final JSONArray attachment = new JSONArray();
        final JSONArray translate = new JSONArray();
        final JSONArray rotate = new JSONArray();
        final JSONArray scale = new JSONArray();
        final JSONArray rgba = new JSONArray();
        String visibleImage;

        void attachment(float time, String image, boolean force) {
            if (force && attachment.length() > 0
                    && attachment.getJSONObject(attachment.length() - 1).getFloat("time") == time) return;
            if (force || !Objects.equals(visibleImage, image)) {
                attachment.put(new JSONObject().put("time", time)
                        .put("name", image == null ? JSONObject.NULL : image));
                visibleImage = image;
            }
        }

        void sample(float time, RecordingParticle particle) {
            ImageSprite image = particle.image;
            if (!regions.has(image.region)) {
                regions.put(image.region, new JSONObject().put("width", image.image.getWidth())
                        .put("height", image.image.getHeight()));
            }
            attachment(time, image.region, false);
            float sx = particle.getScaleX();
            float sy = particle.getScaleY();
            float angle = particle.getRotation();
            float offsetX = (particle.getWidth() / 2 - particle.getOriginX()) * sx;
            float offsetY = (particle.getHeight() / 2 - particle.getOriginY()) * sy;
            double radians = Math.toRadians(angle);
            double x = particle.getX() + particle.getOriginX()
                    + offsetX * Math.cos(radians) - offsetY * Math.sin(radians);
            double y = particle.getY() + particle.getOriginY()
                    + offsetX * Math.sin(radians) + offsetY * Math.cos(radians);
            translate.put(key(time).put("x", x).put("y", y));
            rotate.put(key(time).put("value", angle));
            scale.put(key(time).put("x", sx * particle.getWidth() / image.image.getWidth())
                    .put("y", sy * particle.getHeight() / image.image.getHeight()));
            rgba.put(key(time).put("color", particle.getColor().toString()));
        }

        private static JSONObject key(float time) {
            return new JSONObject().put("time", time).put("curve", "stepped");
        }
    }
}
