package com.tony.dominoes.mesh;

public final class MeshTraceSettings {
    private int detail = 48;
    private float concavity = 0.0f;
    private float refinement = 0.0f;
    private int alphaThreshold = 16;
    private float padding = 0.0f;

    public int detail() {
        return detail;
    }

    public void setDetail(int detail) {
        if (detail < 8) {
            throw new IllegalArgumentException("detail must be >= 8");
        }
        this.detail = detail;
    }

    public float concavity() {
        return concavity;
    }

    public void setConcavity(float concavity) {
        this.concavity = clamp01(concavity);
    }

    public float refinement() {
        return refinement;
    }

    public void setRefinement(float refinement) {
        this.refinement = clamp01(refinement);
    }

    public int alphaThreshold() {
        return alphaThreshold;
    }

    public void setAlphaThreshold(int alphaThreshold) {
        if (alphaThreshold < 0 || alphaThreshold > 255) {
            throw new IllegalArgumentException("alphaThreshold must be 0..255");
        }
        this.alphaThreshold = alphaThreshold;
    }

    public float padding() {
        return padding;
    }

    public void setPadding(float padding) {
        this.padding = padding;
    }

    private static float clamp01(float value) {
        if (value < 0.0f) {
            return 0.0f;
        }
        if (value > 1.0f) {
            return 1.0f;
        }
        return value;
    }
}
