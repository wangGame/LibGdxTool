package com.tony.dominoes.mesh;

import com.badlogic.gdx.graphics.Pixmap;

public final class MeshTracer {
    private MeshTracer() {
    }

    public static EditableMesh traceAlpha(Pixmap pixmap, MeshTraceSettings settings, float targetWidth, float targetHeight) {
        if (settings == null) {
            settings = new MeshTraceSettings();
        }
        return traceAlphaRadial(pixmap, settings.detail(), settings.alphaThreshold(), targetWidth, targetHeight, settings.padding());
    }

    public static EditableMesh traceAlphaRadial(Pixmap pixmap, int samples, int alphaThreshold, float targetWidth, float targetHeight) {
        return traceAlphaRadial(pixmap, samples, alphaThreshold, targetWidth, targetHeight, 0.0f);
    }

    public static EditableMesh traceAlphaRadial(Pixmap pixmap, int samples, int alphaThreshold, float targetWidth, float targetHeight,
                                                float padding) {
        if (samples < 8) {
            throw new IllegalArgumentException("samples must be >= 8");
        }
        int width = pixmap.getWidth();
        int height = pixmap.getHeight();
        float[] center = alphaCentroid(pixmap, alphaThreshold);
        float centerX = center[0];
        float centerY = center[1];
        EditableMesh mesh = new EditableMesh();
        int centerId = mesh.addVertex(
                centerX / width * targetWidth,
                (1.0f - centerY / height) * targetHeight,
                centerX / width,
                centerY / height
        ).id();

        int[] outlineIds = new int[samples];
        for (int i = 0; i < samples; i++) {
            double angle = Math.PI * 2.0 * i / samples;
            float[] point = lastOpaqueAlongRay(pixmap, centerX, centerY, angle, alphaThreshold);
            if (padding != 0.0f) {
                point = padded(point, centerX, centerY, padding);
            }
            outlineIds[i] = mesh.addVertex(
                    point[0] / width * targetWidth,
                    (1.0f - point[1] / height) * targetHeight,
                    point[0] / width,
                    point[1] / height
            ).id();
        }

        for (int i = 0; i < outlineIds.length; i++) {
            int next = (i + 1) % outlineIds.length;
            mesh.addTriangle(centerId, outlineIds[i], outlineIds[next]);
        }
        mesh.validate().requireValid();
        return mesh;
    }

    private static float[] alphaCentroid(Pixmap pixmap, int threshold) {
        double totalX = 0.0;
        double totalY = 0.0;
        int count = 0;
        for (int y = 0; y < pixmap.getHeight(); y++) {
            for (int x = 0; x < pixmap.getWidth(); x++) {
                if (alpha(pixmap.getPixel(x, y)) >= threshold) {
                    totalX += x;
                    totalY += y;
                    count++;
                }
            }
        }
        if (count == 0) {
            return new float[]{pixmap.getWidth() * 0.5f, pixmap.getHeight() * 0.5f};
        }
        return new float[]{(float) (totalX / count), (float) (totalY / count)};
    }

    private static float[] lastOpaqueAlongRay(Pixmap pixmap, float centerX, float centerY, double angle, int threshold) {
        float dx = (float) Math.cos(angle);
        float dy = (float) Math.sin(angle);
        float lastX = centerX;
        float lastY = centerY;
        float maxDistance = (float) Math.sqrt(pixmap.getWidth() * pixmap.getWidth() + pixmap.getHeight() * pixmap.getHeight());
        boolean seenOpaque = false;
        for (float distance = 0.0f; distance <= maxDistance; distance += 1.0f) {
            int x = Math.round(centerX + dx * distance);
            int y = Math.round(centerY + dy * distance);
            if (x < 0 || y < 0 || x >= pixmap.getWidth() || y >= pixmap.getHeight()) {
                break;
            }
            if (alpha(pixmap.getPixel(x, y)) >= threshold) {
                lastX = x;
                lastY = y;
                seenOpaque = true;
            } else if (seenOpaque) {
                break;
            }
        }
        return new float[]{lastX, lastY};
    }

    private static float[] padded(float[] point, float centerX, float centerY, float padding) {
        float dx = point[0] - centerX;
        float dy = point[1] - centerY;
        float length = (float) Math.sqrt(dx * dx + dy * dy);
        if (length == 0.0f) {
            return point;
        }
        return new float[]{point[0] + dx / length * padding, point[1] + dy / length * padding};
    }

    private static int alpha(int rgba8888) {
        return rgba8888 & 0xff;
    }
}
