package com.afsec.rbac.domain;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * A Role is a named bundle of permits, with at most ONE parent.
 * A child role inherits every permit of its parent chain (limited hierarchical RBAC).
 *
 * Two roles are equal when their NAMES are equal. The name is final and never changes,
 * so the hash code is stable. Name uniqueness is enforced later, in the repository/service.
 *
 * A "system role" is built once through Role.systemRole(...) with its permits and is then
 * frozen: nobody can grant, revoke or re-parent it afterwards.
 */
public final class Role {

    private static final int MAX_NAME_LENGTH = 64;

    private final String name;
    private final boolean systemProtected;
    private final Set<Permit> permits;
    private Role parent;

    /**
     * Normal role: starts empty and can be changed.
     */
    public Role(String name) {
        this(name, new HashSet<>(), false);
    }

    /**
     * System role: permits are set NOW, then the role is frozen for good.
     * It has no parent, so its permission set can never be changed by anyone else.
     */
    public static Role systemRole(String name, Permit... initialPermits) {
        if (initialPermits == null) {
            throw new IllegalArgumentException("Permits cannot be null");
        }
        // copy one by one: later changes to the caller's array must not reach this role
        Set<Permit> copy = new HashSet<>();
        for (Permit permit : initialPermits) {
            if (permit == null) {
                throw new IllegalArgumentException("Missing permit.");
            }
            copy.add(permit);
        }
        return new Role(name, copy, true);
    }

    // the only real constructor: every other way in goes through here, so the name is always checked
    private Role(String name, Set<Permit> permits, boolean systemProtected) {
        validateName(name);
        this.name = name;
        this.permits = permits;
        this.systemProtected = systemProtected;
    }

    /**
     * Name rules: 1 to 64 characters, only a-z, 0-9, '_' and '-'.
     * Uppercase and spaces are rejected, so each role has exactly one spelling.
     */
    private static void validateName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Role name must not be blank");
        }
        if (name.length() > MAX_NAME_LENGTH) {
            throw new IllegalArgumentException("Role name is too long");
        }
        for (int index = 0; index < name.length(); index++) {
            char character = name.charAt(index);
            boolean allowed = (character >= 'a' && character <= 'z')
                    || (character >= '0' && character <= '9')
                    || character == '_'
                    || character == '-';
            if (!allowed) {
                throw new IllegalArgumentException(
                        "Role name has an invalid character at position " + index);
            }
        }
    }

    public void grant(Permit permit) {
        checkMutable();
        if (permit == null) {
            throw new IllegalArgumentException("Missing permit.");
        }
        permits.add(permit);
    }

    public void revoke(Permit permit) {
        checkMutable();
        if (permit == null) {
            throw new IllegalArgumentException("Missing permit.");
        }
        permits.remove(permit);
    }

    /**
     * Set (or clear, with null) this role's single parent.
     * Rejects any link that would create a loop.
     */
    public void setParent(Role newParent) {
        checkMutable();
        // walk UP from the proposed parent; meeting this role means a loop
        // (this also catches "a role cannot be its own parent")
        Role current = newParent;
        while (current != null) {
            if (current.equals(this)) {
                throw new IllegalArgumentException("Cycle detected in role hierarchy");
            }
            current = current.parent;
        }
        this.parent = newParent;
    }

    // a frozen role refuses every change.
    // IllegalStateException = "the object is in a state that forbids this".
    // AccessDeniedException stays reserved for denying a USER's action in the authorization layer.
    private void checkMutable() {
        if (systemProtected) {
            throw new IllegalStateException("Role '" + name + "' is system-protected and cannot be modified");
        }
    }

    /**
     * True if this role OR any ancestor grants the permit. Exact match only: no wildcards.
     */
    public boolean permitted(Permit permit) {
        if (permit == null) {
            return false;
        }
        Role current = this;
        while (current != null) {
            if (current.permits.contains(permit)) {
                return true;
            }
            current = current.parent;
        }
        return false; // default is deny
    }

    /**
     * This role plus all of its ancestors, nearest first.
     * Separation-of-duty checks need this: an inherited role must count too.
     */
    public Set<Role> effectiveRoles() {
        Set<Role> result = new LinkedHashSet<>();
        Role current = this;
        while (current != null) {
            result.add(current);
            current = current.parent;
        }
        return result; // a fresh set each call, so callers cannot change anything
    }

    /**
     * This role's OWN permits (not inherited ones), as a copy.
     */
    public Set<Permit> getPermits() {
        return new HashSet<>(permits);
    }

    public String getName() {
        return name;
    }

    public boolean isSystemProtected() {
        return systemProtected;
    }

    public Role getParent() {
        return parent;
    }

    // equality by name only: permits and parent can change, the name cannot
    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof Role that)) {
            return false;
        }
        return name.equals(that.name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public String toString() {
        return "Role[" + name + "]";
    }
}