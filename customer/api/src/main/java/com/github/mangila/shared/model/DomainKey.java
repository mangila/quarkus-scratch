package com.github.mangila.shared.model;

import io.github.mangila.ensure4j.Ensure;

public final class DomainKey {

    private final String value;

    private DomainKey(String value) {
        Ensure.strings().notBlank(value, "domain key cannot be blank");
        this.value = value;
    }

    public static DomainKey order() {
        return new DomainKey("order");
    }

    public static DomainKey product() {
        return new DomainKey("product");
    }

    public static DomainKey customer() {
        return new DomainKey("customer");
    }

    public String value() {
        return value;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        DomainKey domainKey = (DomainKey) obj;
        return value.equals(domainKey.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public String toString() {
        return value;
    }
}
