package com.tony.dominoes.mesh;

import java.util.List;

public final class MeshValidation {
    private final List<String> errors;

    public MeshValidation(List<String> errors) {
        this.errors = errors;
    }

    public List<String> errors() {
        return errors;
    }

    public boolean valid() {
        return errors.isEmpty();
    }

    public void requireValid() {
        if (!valid()) {
            throw new IllegalStateException(String.join("; ", errors));
        }
    }
}
