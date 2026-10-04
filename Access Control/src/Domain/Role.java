package Domain;

import java.util.HashSet;
import java.util.Set;

public class Role {
    private final String name;
    private final Set<Permission> permissions;
    private Role parent;

    public Role(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Domain.Role name must not be blank");
        }
        this.name = name;
        this.permissions = new HashSet<>();
    }

    public void grant(Permission permit) {
        if (permit == null) {
            throw new IllegalArgumentException("Domain.Permission cannot be null");
        }
        permissions.add(permit);
    }

    public void revoke(Permission permit) {
        if (permit == null) {
            throw new IllegalArgumentException("Domain.Permission cannot be null");
        }
        permissions.remove(permit);
    }

    public void setParent(Role newParent) {
        if (newParent == this) {
            throw new IllegalArgumentException("A role cannot be its own parent");
        }

        // Walk up from proposed parent to catch cycles
        Role current = newParent;
        while (current != null) {
            if (current == this) {
                throw new IllegalArgumentException("Cycle detected in role hierarchy");
            }
            current = current.parent;
        }

        this.parent = newParent;
    }

    public boolean permitted(Permission permit) {
        if (permit == null) {
            return false;
        }

        Role current = this;
        while (current != null) {
            if (current.permissions.contains(permit)) {
                return true;
            }
            current = current.parent;
        }
        return false;
    }

    public String getName() {
        return name;
    }

    public Role getParent() {
        return parent;
    }
}