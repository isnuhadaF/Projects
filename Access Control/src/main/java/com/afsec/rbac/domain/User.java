package com.afsec.rbac.domain;

import java.util.HashSet;
import java.util.Set;

/**
 * A User HOLDS roles. Holding a role gives no access by itself:
 * access is decided from the roles a Session has activated.
 * (That is why there is no hasPermission method here any more.)
 */
public class User {
    private final String id;
    private final Set<Role> roles;

    public User(String userID) {
        if (userID == null || userID.isBlank()) {
            throw new IllegalArgumentException("User ID cannot be null or blank");
        }
        this.id = userID;
        this.roles = new HashSet<>();
    }

    public String getId() {
        return this.id;
    }

    public void assign(Role role) {
        if (role == null) {
            throw new IllegalArgumentException("Role cannot be null");
        }
        if (this.roles.contains(role)) {
            throw new IllegalArgumentException("Role already assigned");
        }
        roles.add(role);
    }

    public void revoke(Role role) {
        if (role == null) {
            throw new IllegalArgumentException("Role cannot be null");
        }
        if (!this.roles.contains(role)) {
            throw new IllegalArgumentException("Role not assigned to user");
        }
        roles.remove(role);
    }

    public Set<Role> getRoles() {
        // Returns a safe defensive copy (HashSet) so external code
        // cannot change the internal state directly.
        return new HashSet<>(this.roles);
    }
}