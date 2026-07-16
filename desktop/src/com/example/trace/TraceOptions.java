package com.example.trace;

/** Options equivalent to the Trace panel: Detail, Concavity, Refinement, Alpha threshold, Padding. */
public class TraceOptions {
    /** Pixmap pixels with alpha <= this value are treated as empty. Same range as Spine's panel: 0..254. */
    public int alphaThreshold = 8;

    /** Adds a small visual gap around the traced shape, in pixels. Typical range: 0..2. */
    public float padding = 0f;

    /** 0..1. Higher keeps more vertices. Equivalent to Detail / Complexity. */
    public float detail = 0.20f;

    /** 0..1. Higher tries harder to keep concave dents/corners. */
    public float concavity = 0.50f;

    /** 0..1. Higher runs extra cleanup passes. */
    public float refinement = 0.50f;

    /** Trace every disconnected opaque island. If false, only the largest island is kept. */
    public boolean traceAllIslands = true;

    /** If true, converts Pixmap top-left Y coordinates into LibGDX local bottom-left coordinates. */
    public boolean flipY = false;

    /** Minimum polygon area in pixels. Tiny noise islands below this are ignored. */
    public float minArea = 4f;

    /** Maximum vertices per polygon after simplification. <= 0 means no hard cap. */
    public int maxVerticesPerPolygon = 256;

    /** If true, holes are reported in TraceResult.holes but are not triangulated as filled islands. */
    public boolean detectHoles = true;

    public TraceOptions copy() {
        TraceOptions o = new TraceOptions();
        o.alphaThreshold = alphaThreshold;
        o.padding = padding;
        o.detail = detail;
        o.concavity = concavity;
        o.refinement = refinement;
        o.traceAllIslands = traceAllIslands;
        o.flipY = flipY;
        o.minArea = minArea;
        o.maxVerticesPerPolygon = maxVerticesPerPolygon;
        o.detectHoles = detectHoles;
        return o;
    }

    public TraceOptions clamp() {
        alphaThreshold = Math.max(0, Math.min(254, alphaThreshold));
        padding = Math.max(0f, padding);
        detail = clamp01(detail);
        concavity = clamp01(concavity);
        refinement = clamp01(refinement);
        minArea = Math.max(0f, minArea);
        return this;
    }

    private static float clamp01(float v) {
        return Math.max(0f, Math.min(1f, v));
    }
}
