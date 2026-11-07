package com.afsec.rbac.engine;

import java.util.Objects;

public class Scope {
    private final String key;
    private final String value;

    public Scope(String key, String value) {
        if (key == null || key.isBlank()) {
            throw new IllegalArgumentException("Scope key cannot be null or blank");
        }
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Scope value cannot be null or blank");
        }
        this.key = key.trim().toLowerCase();
        this.value = value.trim().toLowerCase();
    }

    public static Scope global() {
        return new Scope("global", "*");
    }

    public boolean matches(Scope target) {
        if (target == null) return false;
        if (this.key.equals("*") || this.value.equals("*")) return true;
        return this.key.equals(target.key) && this.value.equals(target.value);
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Scope scope = (Scope) o;
        return key.equals(scope.key) && value.equals(scope.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(key, value);
    }

    @Override
    public String toString() {
        return key + ":" + value;
    }
}
