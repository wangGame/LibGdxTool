package com.example.trace;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ShortArray;
import com.badlogic.gdx.utils.async.AsyncExecutor;
import com.badlogic.gdx.utils.async.AsyncTask;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Clean-room LibGDX implementation of an image alpha trace workflow:
 * alpha mask -> outline loops -> polygon simplification -> EarClippingTriangulator mesh.
 */
public class PixmapTracer {
    private final AsyncExecutor executor;
    private volatile CancelToken currentToken;

    public PixmapTracer() {
        this("PixmapTracer");
    }

    public PixmapTracer(String threadName) {
        this.executor = new AsyncExecutor(1, threadName);
    }

    /** Cancel the previous trace if it is still running. */
    public synchronized void cancel() {
        if (currentToken != null) currentToken.cancelled = true;
    }

    public void dispose() {
        cancel();
        executor.dispose();
    }

    /**
     * Starts a background trace. The Pixmap is read once at call time, so it may be safely disposed later.
     * The callback is posted to Gdx.app when available.
     */
    public synchronized void trace(final Pixmap pixmap, final TraceOptions options, final TraceCallback callback) {
        if (pixmap == null) throw new IllegalArgumentException("pixmap cannot be null");
        if (callback == null) throw new IllegalArgumentException("callback cannot be null");

        cancel();
        final CancelToken token = new CancelToken();
        currentToken = token;

        final TraceOptions opt = (options == null ? new TraceOptions() : options.copy()).clamp();
        final int width = pixmap.getWidth();
        final int height = pixmap.getHeight();
        final boolean[] mask = readAlphaMask(pixmap, opt.alphaThreshold);

        executor.submit(new AsyncTask<Void>() {
            @Override
            public Void call() {
                try {
                    TraceResult result = traceMask(mask, width, height, opt, token);
                    if (token.cancelled) {
                        post(callback::onTraceCancelled);
                    } else {
                        post(() -> callback.onTraceFinished(result));
                    }
                } catch (Throwable t) {
                    if (token.cancelled) {
                        post(callback::onTraceCancelled);
                    } else {
                        post(() -> callback.onTraceFailed(t));
                    }
                }
                return null;
            }
        });
    }

    /** Synchronous version, useful for tools/tests. */
    public TraceResult traceNow(Pixmap pixmap, TraceOptions options) {
        if (pixmap == null) throw new IllegalArgumentException("pixmap cannot be null");
        TraceOptions opt = (options == null ? new TraceOptions() : options.copy()).clamp();
        return traceMask(readAlphaMask(pixmap, opt.alphaThreshold), pixmap.getWidth(), pixmap.getHeight(), opt, new CancelToken());
    }

    private static boolean[] readAlphaMask(Pixmap pixmap, int alphaThreshold) {
        int w = pixmap.getWidth();
        int h = pixmap.getHeight();
        boolean[] mask = new boolean[w * h];
        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                int rgba = pixmap.getPixel(x, y);
                int alpha = rgba & 0xff; // Pixmap#getPixel returns RGBA8888.
                mask[y * w + x] = alpha > alphaThreshold;
            }
        }
        return mask;
    }

    private static TraceResult traceMask(boolean[] mask, int width, int height, TraceOptions opt, CancelToken token) {
        long start = System.currentTimeMillis();
        TraceResult result = new TraceResult(width, height);
        result.flipY = opt.flipY;

        ArrayList<ArrayList<P>> loops = buildBoundaryLoops(mask, width, height, token);
        if (token.cancelled) return result;

        ArrayList<Poly> polygons = new ArrayList<>();
        for (ArrayList<P> loop : loops) {
            loop = cleanupLoop(loop);
            if (loop.size() < 3) continue;

            float sourceArea = signedArea(loop);
            float absArea = Math.abs(sourceArea);
            if (absArea < opt.minArea) continue;

            float epsilon = computeEpsilon(width, height, opt);
            ArrayList<P> simplified = simplifyWithLimit(loop, epsilon, opt.maxVerticesPerPolygon);
            for (int i = 0, passes = 1 + Math.round(opt.refinement * 3f); i < passes; i++) {
                simplified = cleanupLoop(simplified);
            }
            if (simplified.size() < 3) continue;

            if (opt.padding > 0f) applyRadialPadding(simplified, opt.padding);

            float[] vertices = toFloatArray(simplified, height, opt.flipY);
            float areaAfter = TraceResult.area(vertices);
            if (Math.abs(areaAfter) < opt.minArea) continue;

            boolean hole = opt.detectHoles && sourceArea < 0f;
            polygons.add(new Poly(vertices, Math.abs(areaAfter), hole));
        }

        Collections.sort(polygons, Comparator.comparingDouble((Poly p) -> p.area).reversed());

        if (!opt.traceAllIslands && !polygons.isEmpty()) {
            Poly largestOuter = null;
            for (Poly p : polygons) {
                if (!p.hole) {
                    largestOuter = p;
                    break;
                }
            }
            polygons.clear();
            if (largestOuter != null) polygons.add(largestOuter);
        }

        for (Poly p : polygons) {
            if (p.hole) {
                result.hasHoles = true;
                result.holes.add(p.vertices);
            } else {
                result.outlines.add(p.vertices);
            }
        }

        buildMeshData(result);
        result.elapsedMillis = System.currentTimeMillis() - start;
        return result;
    }

    private static ArrayList<ArrayList<P>> buildBoundaryLoops(boolean[] mask, int w, int h, CancelToken token) {
        HashMap<Long, ArrayDeque<Edge>> byStart = new HashMap<>();
        int edgeCount = 0;

        for (int y = 0; y < h; y++) {
            if (token.cancelled) return new ArrayList<>();
            for (int x = 0; x < w; x++) {
                if (!solid(mask, w, h, x, y)) continue;
                if (!solid(mask, w, h, x, y - 1)) { addEdge(byStart, new Edge(x, y, x + 1, y)); edgeCount++; }
                if (!solid(mask, w, h, x + 1, y)) { addEdge(byStart, new Edge(x + 1, y, x + 1, y + 1)); edgeCount++; }
                if (!solid(mask, w, h, x, y + 1)) { addEdge(byStart, new Edge(x + 1, y + 1, x, y + 1)); edgeCount++; }
                if (!solid(mask, w, h, x - 1, y)) { addEdge(byStart, new Edge(x, y + 1, x, y)); edgeCount++; }
            }
        }

        ArrayList<ArrayList<P>> loops = new ArrayList<>();
        int safety = Math.max(16, edgeCount * 4);
        while (edgeCount > 0 && safety-- > 0) {
            if (token.cancelled) return loops;
            Edge first = takeAnyEdge(byStart);
            if (first == null) break;
            edgeCount--;

            ArrayList<P> loop = new ArrayList<>();
            loop.add(new P(first.sx, first.sy));
            Edge current = first;
            long startKey = key(first.sx, first.sy);

            int loopSafety = Math.max(16, edgeCount + 4);
            while (loopSafety-- > 0) {
                loop.add(new P(current.ex, current.ey));
                long endKey = key(current.ex, current.ey);
                if (endKey == startKey) break;

                Edge next = takeNextEdge(byStart, current);
                if (next == null) break;
                edgeCount--;
                current = next;
            }

            if (loop.size() >= 4) {
                P a = loop.get(0);
                P b = loop.get(loop.size() - 1);
                if (a.x == b.x && a.y == b.y) loop.remove(loop.size() - 1);
                if (loop.size() >= 3) loops.add(loop);
            }
        }
        return loops;
    }

    private static boolean solid(boolean[] mask, int w, int h, int x, int y) {
        return x >= 0 && y >= 0 && x < w && y < h && mask[y * w + x];
    }

    private static void addEdge(HashMap<Long, ArrayDeque<Edge>> byStart, Edge e) {
        long k = key(e.sx, e.sy);
        ArrayDeque<Edge> q = byStart.get(k);
        if (q == null) {
            q = new ArrayDeque<>(2);
            byStart.put(k, q);
        }
        q.add(e);
    }

    private static Edge takeAnyEdge(HashMap<Long, ArrayDeque<Edge>> byStart) {
        Iterator<Map.Entry<Long, ArrayDeque<Edge>>> it = byStart.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<Long, ArrayDeque<Edge>> entry = it.next();
            ArrayDeque<Edge> q = entry.getValue();
            Edge e = q.pollFirst();
            if (q.isEmpty()) it.remove();
            if (e != null) return e;
        }
        return null;
    }

    private static Edge takeNextEdge(HashMap<Long, ArrayDeque<Edge>> byStart, Edge previous) {
        long k = key(previous.ex, previous.ey);
        ArrayDeque<Edge> q = byStart.get(k);
        if (q == null || q.isEmpty()) return null;

        // Most pixels have only one outgoing edge. At diagonal touching points there may be two.
        // Pick the edge with the strongest right-turn preference to keep loops separated.
        Edge best = null;
        int bestScore = Integer.MIN_VALUE;
        int prevDx = Integer.compare(previous.ex - previous.sx, 0);
        int prevDy = Integer.compare(previous.ey - previous.sy, 0);
        for (Edge e : q) {
            int dx = Integer.compare(e.ex - e.sx, 0);
            int dy = Integer.compare(e.ey - e.sy, 0);
            int cross = prevDx * dy - prevDy * dx;
            int dot = prevDx * dx + prevDy * dy;
            int score = -cross * 10 + dot; // right turn > straight > left turn > back.
            if (score > bestScore) {
                bestScore = score;
                best = e;
            }
        }
        q.remove(best);
        if (q.isEmpty()) byStart.remove(k);
        return best;
    }

    private static ArrayList<P> cleanupLoop(ArrayList<P> input) {
        ArrayList<P> pts = new ArrayList<>(input.size());
        for (P p : input) {
            if (pts.isEmpty()) {
                pts.add(new P(p.x, p.y));
            } else {
                P last = pts.get(pts.size() - 1);
                if (!same(last, p)) pts.add(new P(p.x, p.y));
            }
        }
        if (pts.size() > 1 && same(pts.get(0), pts.get(pts.size() - 1))) pts.remove(pts.size() - 1);

        boolean changed = true;
        while (changed && pts.size() > 3) {
            changed = false;
            for (int i = 0; i < pts.size(); i++) {
                P a = pts.get((i + pts.size() - 1) % pts.size());
                P b = pts.get(i);
                P c = pts.get((i + 1) % pts.size());
                float cross = (b.x - a.x) * (c.y - b.y) - (b.y - a.y) * (c.x - b.x);
                float len1 = dist2(a, b);
                float len2 = dist2(b, c);
                if (Math.abs(cross) < 0.0001f || len1 < 0.0001f || len2 < 0.0001f) {
                    pts.remove(i);
                    changed = true;
                    break;
                }
            }
        }
        return pts;
    }

    private static float computeEpsilon(int w, int h, TraceOptions opt) {
        float maxDim = Math.max(w, h);
        float loose = 2.0f + maxDim * 0.018f;
        float tight = 0.35f;
        float eps = loose + (tight - loose) * opt.detail;
        eps *= 1f - opt.concavity * 0.55f;
        return Math.max(0.15f, eps);
    }

    private static ArrayList<P> simplifyWithLimit(ArrayList<P> loop, float epsilon, int maxVertices) {
        ArrayList<P> simplified = simplifyClosedRdp(loop, epsilon);
        if (maxVertices <= 0) return simplified;

        float eps = epsilon;
        int guard = 0;
        while (simplified.size() > maxVertices && guard++ < 24) {
            eps *= 1.25f;
            simplified = simplifyClosedRdp(loop, eps);
        }
        return simplified;
    }

    private static ArrayList<P> simplifyClosedRdp(ArrayList<P> pts, float epsilon) {
        if (pts.size() <= 3) return new ArrayList<>(pts);

        int start = 0;
        for (int i = 1; i < pts.size(); i++) {
            P a = pts.get(i);
            P b = pts.get(start);
            if (a.x < b.x || (a.x == b.x && a.y < b.y)) start = i;
        }

        int far = start;
        float best = -1f;
        P sp = pts.get(start);
        for (int i = 0; i < pts.size(); i++) {
            float d = dist2(sp, pts.get(i));
            if (d > best) {
                best = d;
                far = i;
            }
        }
        if (far == start) return new ArrayList<>(pts);

        ArrayList<P> a = collectCyclic(pts, start, far);
        ArrayList<P> b = collectCyclic(pts, far, start);
        ArrayList<P> sa = simplifyOpenRdp(a, epsilon);
        ArrayList<P> sb = simplifyOpenRdp(b, epsilon);

        ArrayList<P> out = new ArrayList<>(sa.size() + sb.size());
        for (int i = 0; i < sa.size() - 1; i++) out.add(sa.get(i));
        for (int i = 0; i < sb.size() - 1; i++) out.add(sb.get(i));
        return cleanupLoop(out);
    }

    private static ArrayList<P> collectCyclic(ArrayList<P> pts, int from, int to) {
        ArrayList<P> out = new ArrayList<>();
        int i = from;
        while (true) {
            out.add(pts.get(i));
            if (i == to) break;
            i = (i + 1) % pts.size();
        }
        return out;
    }

    private static ArrayList<P> simplifyOpenRdp(ArrayList<P> pts, float epsilon) {
        boolean[] keep = new boolean[pts.size()];
        keep[0] = true;
        keep[pts.size() - 1] = true;
        rdpMark(pts, 0, pts.size() - 1, epsilon * epsilon, keep);
        ArrayList<P> out = new ArrayList<>();
        for (int i = 0; i < pts.size(); i++) if (keep[i]) out.add(pts.get(i));
        return out;
    }

    private static void rdpMark(ArrayList<P> pts, int from, int to, float epsilon2, boolean[] keep) {
        if (to <= from + 1) return;
        P a = pts.get(from);
        P b = pts.get(to);
        float maxD = -1f;
        int index = -1;
        for (int i = from + 1; i < to; i++) {
            float d = pointSegmentDistance2(pts.get(i), a, b);
            if (d > maxD) {
                maxD = d;
                index = i;
            }
        }
        if (maxD > epsilon2 && index != -1) {
            keep[index] = true;
            rdpMark(pts, from, index, epsilon2, keep);
            rdpMark(pts, index, to, epsilon2, keep);
        }
    }

    private static void applyRadialPadding(ArrayList<P> pts, float padding) {
        float cx = 0f, cy = 0f;
        for (P p : pts) {
            cx += p.x;
            cy += p.y;
        }
        cx /= pts.size();
        cy /= pts.size();
        for (P p : pts) {
            float dx = p.x - cx;
            float dy = p.y - cy;
            float len = (float)Math.sqrt(dx * dx + dy * dy);
            if (len > 0.0001f) {
                p.x += dx / len * padding;
                p.y += dy / len * padding;
            }
        }
    }

    private static float[] toFloatArray(ArrayList<P> pts, int imageHeight, boolean flipY) {
        float[] out = new float[pts.size() * 2];
        int idx = 0;
        for (P p : pts) {
            out[idx++] = p.x;
            out[idx++] = flipY ? imageHeight - p.y : p.y;
        }
        return out;
    }

    private static void buildMeshData(TraceResult result) {
        Array<Float> vertexData = new Array<>();
        ShortArray indexData = new ShortArray();
        int vertexOffset = 0;

        for (float[] polygon : result.outlines) {
            if (polygon.length < 6) continue;

            float[] polyForTri = polygon;
            ShortArray local = TraceResult.triangulate(polyForTri);
            if (local.size == 0) {
                polyForTri = reversed(polygon);
                local = TraceResult.triangulate(polyForTri);
            }
            if (local.size == 0) continue;

            int points = polyForTri.length / 2;
            if (vertexOffset + points > 65535) {
                throw new IllegalStateException("Too many trace vertices for short indices: " + (vertexOffset + points));
            }

            for (int i = 0; i < polyForTri.length; i += 2) {
                float x = polyForTri[i];
                float y = polyForTri[i + 1];
                float sourceY = result.flipY ? result.imageHeight - y : y;
                float u = result.imageWidth == 0 ? 0f : x / result.imageWidth;
                float v = result.imageHeight == 0 ? 0f : sourceY / result.imageHeight;
                vertexData.add(x);
                vertexData.add(y);
                vertexData.add(u);
                vertexData.add(v);
            }
            for (int i = 0; i < local.size; i++) {
                indexData.add((short)(local.get(i) + vertexOffset));
            }
            vertexOffset += points;
        }

        result.vertices = new float[vertexData.size];
        for (int i = 0; i < vertexData.size; i++) result.vertices[i] = vertexData.get(i);
        result.indices = indexData.toArray();
    }

    private static float[] reversed(float[] polygon) {
        float[] out = new float[polygon.length];
        int dst = 0;
        for (int i = polygon.length - 2; i >= 0; i -= 2) {
            out[dst++] = polygon[i];
            out[dst++] = polygon[i + 1];
        }
        return out;
    }

    private static void post(Runnable runnable) {
        if (Gdx.app != null) Gdx.app.postRunnable(runnable);
        else runnable.run();
    }

    private static boolean same(P a, P b) {
        return Math.abs(a.x - b.x) < 0.0001f && Math.abs(a.y - b.y) < 0.0001f;
    }

    private static float signedArea(ArrayList<P> pts) {
        float sum = 0f;
        for (int i = 0; i < pts.size(); i++) {
            P a = pts.get(i);
            P b = pts.get((i + 1) % pts.size());
            sum += a.x * b.y - b.x * a.y;
        }
        return sum * 0.5f;
    }

    private static float dist2(P a, P b) {
        float dx = a.x - b.x;
        float dy = a.y - b.y;
        return dx * dx + dy * dy;
    }

    private static float pointSegmentDistance2(P p, P a, P b) {
        float dx = b.x - a.x;
        float dy = b.y - a.y;
        float len2 = dx * dx + dy * dy;
        if (len2 < 0.000001f) return dist2(p, a);
        float t = ((p.x - a.x) * dx + (p.y - a.y) * dy) / len2;
        t = Math.max(0f, Math.min(1f, t));
        float px = a.x + dx * t;
        float py = a.y + dy * t;
        float ox = p.x - px;
        float oy = p.y - py;
        return ox * ox + oy * oy;
    }

    private static long key(int x, int y) {
        return (((long)x) << 32) ^ (y & 0xffffffffL);
    }

    private static final class CancelToken {
        volatile boolean cancelled;
    }

    private static final class Edge {
        final int sx, sy, ex, ey;
        Edge(int sx, int sy, int ex, int ey) {
            this.sx = sx;
            this.sy = sy;
            this.ex = ex;
            this.ey = ey;
        }
    }

    private static final class P {
        float x, y;
        P(float x, float y) {
            this.x = x;
            this.y = y;
        }
    }

    private static final class Poly {
        final float[] vertices;
        final float area;
        final boolean hole;
        Poly(float[] vertices, float area, boolean hole) {
            this.vertices = vertices;
            this.area = area;
            this.hole = hole;
        }
    }
}
