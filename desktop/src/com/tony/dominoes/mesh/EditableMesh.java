package com.tony.dominoes.mesh;

import org.json.JSONArray;
import org.json.JSONObject;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Matrix3;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public final class EditableMesh {
    private final Map<Integer, MeshVertex> vertices = new LinkedHashMap<Integer, MeshVertex>();
    private final Set<MeshEdge> edges = new LinkedHashSet<MeshEdge>();
    private final List<MeshTriangle> triangles = new ArrayList<MeshTriangle>();
    private final List<LinkedEditableMesh> linkedMeshes = new ArrayList<LinkedEditableMesh>();
    private int nextVertexId;

    public static EditableMesh rectangle(float width, float height) {
        EditableMesh mesh = new EditableMesh();
        mesh.resetRectangle(width, height);
        return mesh;
    }

    public static EditableMesh grid(float width, float height, int columns, int rows) {
        EditableMesh mesh = new EditableMesh();
        mesh.generateGrid(width, height, columns, rows);
        return mesh;
    }

    public MeshVertex addVertex(float x, float y, float u, float v) {
        MeshVertex vertex = new MeshVertex(nextVertexId++, x, y, u, v);
        vertices.put(vertex.id(), vertex);
        return vertex;
    }

    public MeshVertexSnapshot vertexSnapshot(int id) {
        return vertex(id).snapshot();
    }

    public void moveVertex(int id, float x, float y) {
        vertex(id).setPosition(x, y);
    }

    public void moveSetupVertex(int id, float x, float y) {
        MeshVertex vertex = vertex(id);
        float dx = x - vertex.setupX();
        float dy = y - vertex.setupY();
        vertex.setSetupPosition(x, y);
        vertex.translate(dx, dy);
    }

    public void translateVertex(int id, float dx, float dy) {
        vertex(id).translate(dx, dy);
    }

    public void translateSetupVertex(int id, float dx, float dy) {
        MeshVertex vertex = vertex(id);
        vertex.translateSetup(dx, dy);
        vertex.translate(dx, dy);
    }

    public void deformVertex(int id, float x, float y) {
        moveVertex(id, x, y);
    }

    public void setUv(int id, float u, float v) {
        vertex(id).setUv(u, v);
    }

    public void removeVertex(int id) {
        MeshVertex removed = vertices.get(id);
        if (removed == null) {
            throw new IllegalArgumentException("unknown vertex: " + id);
        }
        List<MeshTriangle> removedTriangles = new ArrayList<MeshTriangle>();
        Set<Integer> neighborIds = new LinkedHashSet<Integer>();
        for (MeshTriangle triangle : triangles) {
            if (triangle.a() == id || triangle.b() == id || triangle.c() == id) {
                removedTriangles.add(triangle);
                if (triangle.a() != id) {
                    neighborIds.add(triangle.a());
                }
                if (triangle.b() != id) {
                    neighborIds.add(triangle.b());
                }
                if (triangle.c() != id) {
                    neighborIds.add(triangle.c());
                }
            }
        }

        vertices.remove(id);
        triangles.removeAll(removedTriangles);
        List<Integer> neighbors = sortedAround(removed, neighborIds);
        if (neighbors.size() >= 3) {
            int anchor = neighbors.get(0);
            for (int i = 1; i < neighbors.size() - 1; i++) {
                addTriangleIfNotDegenerate(anchor, neighbors.get(i), neighbors.get(i + 1));
            }
        }
        removeUnusedEdges();
    }

    public void addEdge(int a, int b) {
        requireVertex(a);
        requireVertex(b);
        edges.add(new MeshEdge(a, b));
    }

    public void removeEdge(int a, int b) {
        edges.remove(new MeshEdge(a, b));
    }

    public void clearEdges() {
        edges.clear();
    }

    public void addTriangle(int a, int b, int c) {
        requireVertex(a);
        requireVertex(b);
        requireVertex(c);
        MeshTriangle triangle = new MeshTriangle(a, b, c);
        if (area(triangle) == 0.0f) {
            throw new IllegalArgumentException("triangle area is zero: " + a + "," + b + "," + c);
        }
        triangles.add(triangle);
        addEdge(a, b);
        addEdge(b, c);
        addEdge(c, a);
    }

    public boolean removeTriangle(int a, int b, int c) {
        boolean removed = triangles.remove(new MeshTriangle(a, b, c));
        if (removed) {
            removeUnusedEdges();
        }
        return removed;
    }

    public MeshTriangle removeTriangleAt(int index) {
        MeshTriangle removed = triangles.remove(index);
        removeUnusedEdges();
        return removed;
    }

    private void addTriangleIfNotDegenerate(int a, int b, int c) {
        requireVertex(a);
        requireVertex(b);
        requireVertex(c);
        MeshTriangle triangle = new MeshTriangle(a, b, c);
        if (area(triangle) == 0.0f) {
            return;
        }
        triangles.add(triangle);
        addEdge(a, b);
        addEdge(b, c);
        addEdge(c, a);
    }

    public MeshVertex addVertexBySplittingTriangle(float x, float y, float u, float v) {
        MeshTriangle target = findTriangleContaining(x, y);
        MeshVertex vertex = addVertex(x, y, u, v);
        if (target == null) {
            return vertex;
        }
        triangles.remove(target);
        addTriangle(target.a(), target.b(), vertex.id());
        addTriangle(target.b(), target.c(), vertex.id());
        addTriangle(target.c(), target.a(), vertex.id());
        return vertex;
    }

    public void clear() {
        vertices.clear();
        edges.clear();
        triangles.clear();
        nextVertexId = 0;
    }

    public void resetRectangle(float width, float height) {
        requirePositive(width, "width");
        requirePositive(height, "height");
        clear();
        MeshVertex bottomLeft = addVertex(0, 0, 0, 1);
        MeshVertex bottomRight = addVertex(width, 0, 1, 1);
        MeshVertex topRight = addVertex(width, height, 1, 0);
        MeshVertex topLeft = addVertex(0, height, 0, 0);
        addTriangle(bottomLeft.id(), bottomRight.id(), topRight.id());
        addTriangle(bottomLeft.id(), topRight.id(), topLeft.id());
    }

    public void resetToCorners(float width, float height) {
        resetRectangle(width, height);
    }

    public void generateGrid(float width, float height, int columns, int rows) {
        requirePositive(width, "width");
        requirePositive(height, "height");
        if (columns < 1 || rows < 1) {
            throw new IllegalArgumentException("columns and rows must be >= 1");
        }
        clear();
        int[][] ids = new int[rows + 1][columns + 1];
        for (int y = 0; y <= rows; y++) {
            for (int x = 0; x <= columns; x++) {
                float px = width * x / columns;
                float py = height * y / rows;
                float u = (float) x / columns;
                float v = 1.0f - (float) y / rows;
                ids[y][x] = addVertex(px, py, u, v).id();
            }
        }
        for (int y = 0; y < rows; y++) {
            for (int x = 0; x < columns; x++) {
                int bottomLeft = ids[y][x];
                int bottomRight = ids[y][x + 1];
                int topLeft = ids[y + 1][x];
                int topRight = ids[y + 1][x + 1];
                addTriangle(bottomLeft, bottomRight, topRight);
                addTriangle(bottomLeft, topRight, topLeft);
            }
        }
    }

    public void generateVertices(float width, float height, MeshGenerateSettings settings) {
        if (settings == null) {
            settings = new MeshGenerateSettings();
        }
        if (settings.vertexCount() > settings.maxVertices()) {
            throw new IllegalStateException("The maximum number of vertices has been reached.");
        }
        generateGrid(width, height, settings.columns(), settings.rows());
    }

    public EditableMesh copy() {
        EditableMesh copy = new EditableMesh();
        for (MeshVertex vertex : vertices.values()) {
            MeshVertex copied = new MeshVertex(vertex.id(), vertex.x(), vertex.y(), vertex.u(), vertex.v());
            copied.setSetupPosition(vertex.setupX(), vertex.setupY());
            copy.vertices.put(copied.id(), copied);
            copy.nextVertexId = Math.max(copy.nextVertexId, copied.id() + 1);
        }
        copy.edges.addAll(edges);
        copy.triangles.addAll(triangles);
        return copy;
    }

    public LinkedEditableMesh createLinkedMesh(boolean inheritParentDeform) {
        LinkedEditableMesh linkedMesh = new LinkedEditableMesh(this, inheritParentDeform);
        linkedMeshes.add(linkedMesh);
        return linkedMesh;
    }

    public List<LinkedEditableMesh> linkedMeshes() {
        return Collections.unmodifiableList(linkedMeshes);
    }

    public int linkedMeshCount() {
        return linkedMeshes.size();
    }

    public int deleteLinkedMeshes() {
        int count = linkedMeshes.size();
        for (LinkedEditableMesh linkedMesh : new ArrayList<LinkedEditableMesh>(linkedMeshes)) {
            linkedMesh.detachFromParent();
        }
        linkedMeshes.clear();
        return count;
    }

    void unregisterLinkedMesh(LinkedEditableMesh linkedMesh) {
        linkedMeshes.remove(linkedMesh);
    }

    public void translate(float dx, float dy) {
        for (MeshVertex vertex : vertices.values()) {
            vertex.translate(dx, dy);
            vertex.translateSetup(dx, dy);
        }
    }

    public void scale(float scaleX, float scaleY) {
        scale(scaleX, scaleY, 0.0f, 0.0f);
    }

    public void scale(float scaleX, float scaleY, float originX, float originY) {
        for (MeshVertex vertex : vertices.values()) {
            float x = originX + (vertex.x() - originX) * scaleX;
            float y = originY + (vertex.y() - originY) * scaleY;
            vertex.setPosition(x, y);
            float setupX = originX + (vertex.setupX() - originX) * scaleX;
            float setupY = originY + (vertex.setupY() - originY) * scaleY;
            vertex.setSetupPosition(setupX, setupY);
        }
    }

    public void transform(Matrix4 matrix) {
        Vector3 temp = new Vector3();
        for (MeshVertex vertex : vertices.values()) {
            temp.set(vertex.x(), vertex.y(), 0.0f).mul(matrix);
            vertex.setPosition(temp.x, temp.y);
            temp.set(vertex.setupX(), vertex.setupY(), 0.0f).mul(matrix);
            vertex.setSetupPosition(temp.x, temp.y);
        }
    }

    public void resetDeformation() {
        for (MeshVertex vertex : vertices.values()) {
            vertex.resetDeformation();
        }
    }

    public void freezeDeformation() {
        for (MeshVertex vertex : vertices.values()) {
            vertex.freezeDeformation();
        }
    }

    public void freezeCurrentTransform() {
        freezeDeformation();
    }

    public void freezeCurrentTransform(float width, float height) {
        freezeCurrentTransform(width, height, false);
    }

    public void freezeCurrentTransform(float width, float height, boolean centered) {
        requirePositive(width, "width");
        requirePositive(height, "height");
        for (MeshVertex vertex : vertices.values()) {
            float x = vertex.u() * width;
            float y = (1.0f - vertex.v()) * height;
            if (centered) {
                x -= width * 0.5f;
                y -= height * 0.5f;
            }
            vertex.setPosition(x, y);
            vertex.freezeDeformation();
        }
    }

    public boolean hasDeformation() {
        for (MeshVertex vertex : vertices.values()) {
            if (vertex.x() != vertex.setupX() || vertex.y() != vertex.setupY()) {
                return true;
            }
        }
        return false;
    }

    public void transformUv(Matrix3 matrix) {
        Vector2 temp = new Vector2();
        for (MeshVertex vertex : vertices.values()) {
            temp.set(vertex.u(), vertex.v()).mul(matrix);
            vertex.setUv(temp.x, temp.y);
        }
    }

    public Map<Integer, Float> softSelectionWeights(int selectedVertexId, float radius) {
        requireVertex(selectedVertexId);
        requirePositive(radius, "radius");
        MeshVertex selected = vertex(selectedVertexId);
        Map<Integer, Float> weights = new LinkedHashMap<Integer, Float>();
        for (MeshVertex candidate : vertices.values()) {
            float distance = distance(selected, candidate);
            if (distance <= radius) {
                weights.put(candidate.id(), 1.0f - distance / radius);
            }
        }
        return weights;
    }

    public void translateSoftSelection(int selectedVertexId, float dx, float dy, float radius) {
        Map<Integer, Float> weights = softSelectionWeights(selectedVertexId, radius);
        for (Map.Entry<Integer, Float> entry : weights.entrySet()) {
            MeshVertex vertex = vertex(entry.getKey());
            float weight = entry.getValue();
            vertex.translate(dx * weight, dy * weight);
        }
    }

    public List<MeshVertexSnapshot> vertices() {
        List<MeshVertexSnapshot> snapshots = new ArrayList<MeshVertexSnapshot>();
        for (MeshVertex vertex : vertices.values()) {
            snapshots.add(vertex.snapshot());
        }
        return Collections.unmodifiableList(snapshots);
    }

    public List<MeshEdge> edges() {
        return Collections.unmodifiableList(new ArrayList<MeshEdge>(edges));
    }

    public List<MeshTriangle> triangles() {
        return Collections.unmodifiableList(new ArrayList<MeshTriangle>(triangles));
    }

    public int vertexCount() {
        return vertices.size();
    }

    public int edgeCount() {
        return edges.size();
    }

    public int triangleCount() {
        return triangles.size();
    }

    public float[] toPositionUvVertices() {
        float[] data = new float[vertices.size() * 4];
        int offset = 0;
        for (MeshVertex vertex : vertices.values()) {
            data[offset++] = vertex.x();
            data[offset++] = vertex.y();
            data[offset++] = vertex.u();
            data[offset++] = vertex.v();
        }
        return data;
    }

    public float[] toPolygonVertices() {
        return toPolygonVertices(0.0f, 0.0f, Color.WHITE_FLOAT_BITS);
    }

    public float[] toPolygonVertices(float originX, float originY, float colorBits) {
        float[] data = new float[vertices.size() * 5];
        int offset = 0;
        for (MeshVertex vertex : vertices.values()) {
            data[offset++] = originX + vertex.x();
            data[offset++] = originY + vertex.y();
            data[offset++] = colorBits;
            data[offset++] = vertex.u();
            data[offset++] = vertex.v();
        }
        return data;
    }

    public short[] toTriangleIndices() {
        Map<Integer, Integer> indexById = vertexIndexById();
        short[] data = new short[triangles.size() * 3];
        int offset = 0;
        for (MeshTriangle triangle : triangles) {
            data[offset++] = toShortIndex(indexById, triangle.a());
            data[offset++] = toShortIndex(indexById, triangle.b());
            data[offset++] = toShortIndex(indexById, triangle.c());
        }
        return data;
    }

    public MeshBounds bounds() {
        if (vertices.isEmpty()) {
            return new MeshBounds(0, 0, 0, 0);
        }
        float minX = Float.POSITIVE_INFINITY;
        float minY = Float.POSITIVE_INFINITY;
        float maxX = Float.NEGATIVE_INFINITY;
        float maxY = Float.NEGATIVE_INFINITY;
        for (MeshVertex vertex : vertices.values()) {
            minX = Math.min(minX, vertex.x());
            minY = Math.min(minY, vertex.y());
            maxX = Math.max(maxX, vertex.x());
            maxY = Math.max(maxY, vertex.y());
        }
        return new MeshBounds(minX, minY, maxX, maxY);
    }

    public MeshValidation validate() {
        List<String> errors = new ArrayList<String>();
        for (MeshEdge edge : edges) {
            if (!vertices.containsKey(edge.a())) {
                errors.add("edge references missing vertex " + edge.a());
            }
            if (!vertices.containsKey(edge.b())) {
                errors.add("edge references missing vertex " + edge.b());
            }
        }
        for (MeshTriangle triangle : triangles) {
            if (!vertices.containsKey(triangle.a()) || !vertices.containsKey(triangle.b()) || !vertices.containsKey(triangle.c())) {
                errors.add("triangle references missing vertex " + triangle);
                continue;
            }
            if (area(triangle) == 0.0f) {
                errors.add("triangle has zero area " + triangle);
            }
        }
        return new MeshValidation(Collections.unmodifiableList(errors));
    }

    public JSONObject toJsonSummary() {
        JSONObject root = new JSONObject();
        JSONArray vertexArray = new JSONArray();
        for (MeshVertexSnapshot vertex : vertices()) {
            JSONObject object = new JSONObject();
            object.put("id", vertex.id());
            object.put("setupX", vertex.setupX());
            object.put("setupY", vertex.setupY());
            object.put("x", vertex.x());
            object.put("y", vertex.y());
            object.put("deformX", vertex.deformX());
            object.put("deformY", vertex.deformY());
            object.put("u", vertex.u());
            object.put("v", vertex.v());
            vertexArray.put(object);
        }

        List<MeshEdge> sortedEdges = new ArrayList<MeshEdge>(edges);
        Collections.sort(sortedEdges, new Comparator<MeshEdge>() {
            @Override
            public int compare(MeshEdge first, MeshEdge second) {
                int byA = Integer.compare(first.a(), second.a());
                return byA != 0 ? byA : Integer.compare(first.b(), second.b());
            }
        });
        JSONArray edgeArray = new JSONArray();
        for (MeshEdge edge : sortedEdges) {
            JSONArray pair = new JSONArray();
            pair.put(edge.a());
            pair.put(edge.b());
            edgeArray.put(pair);
        }

        JSONArray triangleArray = new JSONArray();
        for (MeshTriangle triangle : triangles) {
            JSONArray ids = new JSONArray();
            ids.put(triangle.a());
            ids.put(triangle.b());
            ids.put(triangle.c());
            triangleArray.put(ids);
        }

        MeshBounds bounds = bounds();
        JSONObject boundsObject = new JSONObject();
        boundsObject.put("minX", bounds.minX());
        boundsObject.put("minY", bounds.minY());
        boundsObject.put("maxX", bounds.maxX());
        boundsObject.put("maxY", bounds.maxY());

        root.put("vertices", vertexArray);
        root.put("edges", edgeArray);
        root.put("triangles", triangleArray);
        root.put("bounds", boundsObject);
        return root;
    }

    private Map<Integer, Integer> vertexIndexById() {
        Map<Integer, Integer> indexById = new LinkedHashMap<Integer, Integer>();
        int index = 0;
        for (MeshVertex vertex : vertices.values()) {
            if (index > 65535) {
                throw new IllegalStateException("mesh has more than 65536 vertices");
            }
            indexById.put(vertex.id(), index++);
        }
        return indexById;
    }

    private static short toShortIndex(Map<Integer, Integer> indexById, int vertexId) {
        Integer index = indexById.get(vertexId);
        if (index == null) {
            throw new IllegalStateException("triangle references missing vertex " + vertexId);
        }
        return (short) (int) index;
    }

    private MeshVertex vertex(int id) {
        MeshVertex vertex = vertices.get(id);
        if (vertex == null) {
            throw new IllegalArgumentException("unknown vertex: " + id);
        }
        return vertex;
    }

    private void requireVertex(int id) {
        if (!vertices.containsKey(id)) {
            throw new IllegalArgumentException("unknown vertex: " + id);
        }
    }

    private float area(MeshTriangle triangle) {
        MeshVertex a = vertex(triangle.a());
        MeshVertex b = vertex(triangle.b());
        MeshVertex c = vertex(triangle.c());
        return Math.abs((b.x() - a.x()) * (c.y() - a.y()) - (c.x() - a.x()) * (b.y() - a.y())) * 0.5f;
    }

    private MeshTriangle findTriangleContaining(float x, float y) {
        for (MeshTriangle triangle : triangles) {
            MeshVertex a = vertex(triangle.a());
            MeshVertex b = vertex(triangle.b());
            MeshVertex c = vertex(triangle.c());
            if (containsPoint(a, b, c, x, y)) {
                return triangle;
            }
        }
        return null;
    }

    private static boolean containsPoint(MeshVertex a, MeshVertex b, MeshVertex c, float x, float y) {
        float d1 = sign(x, y, a.x(), a.y(), b.x(), b.y());
        float d2 = sign(x, y, b.x(), b.y(), c.x(), c.y());
        float d3 = sign(x, y, c.x(), c.y(), a.x(), a.y());
        boolean hasNegative = d1 < 0 || d2 < 0 || d3 < 0;
        boolean hasPositive = d1 > 0 || d2 > 0 || d3 > 0;
        return !(hasNegative && hasPositive);
    }

    private static float sign(float x1, float y1, float x2, float y2, float x3, float y3) {
        return (x1 - x3) * (y2 - y3) - (x2 - x3) * (y1 - y3);
    }

    private List<Integer> sortedAround(final MeshVertex center, Set<Integer> ids) {
        List<Integer> sorted = new ArrayList<Integer>(ids);
        Collections.sort(sorted, new Comparator<Integer>() {
            @Override
            public int compare(Integer firstId, Integer secondId) {
                MeshVertex first = vertex(firstId);
                MeshVertex second = vertex(secondId);
                double firstAngle = Math.atan2(first.y() - center.y(), first.x() - center.x());
                double secondAngle = Math.atan2(second.y() - center.y(), second.x() - center.x());
                return Double.compare(firstAngle, secondAngle);
            }
        });
        return sorted;
    }

    private void removeUnusedEdges() {
        Set<MeshEdge> used = new LinkedHashSet<MeshEdge>();
        for (MeshTriangle triangle : triangles) {
            used.add(new MeshEdge(triangle.a(), triangle.b()));
            used.add(new MeshEdge(triangle.b(), triangle.c()));
            used.add(new MeshEdge(triangle.c(), triangle.a()));
        }
        edges.retainAll(used);
    }

    private static float distance(MeshVertex first, MeshVertex second) {
        float dx = first.x() - second.x();
        float dy = first.y() - second.y();
        return (float) Math.sqrt(dx * dx + dy * dy);
    }

    private static void requirePositive(float value, String name) {
        if (value <= 0 || Float.isNaN(value) || Float.isInfinite(value)) {
            throw new IllegalArgumentException(name + " must be positive");
        }
    }
}
