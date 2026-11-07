package com.afsec.rbac.domain;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

/**
 * A Session is one "sitting" of a user. The user HOLDS roles (assigned in User),
 * but a session USES only the roles that were explicitly activated.
 * This is the least-privilege rule: held roles give nothing until activated.
 *
 * Time never comes from Instant.now() inside this class. The caller passes
 * "now" in, so tests can control the clock and check exact boundaries.
 */
public class Session {

    private final String id;
    private final User user;
    private final Set<Role> activeRoles;
    private final Instant createdAt;
    private final Instant expiresAt;
    private boolean revoked;

    public Session(String id, User user, Instant createdAt, Instant expiresAt) {
        // validate every input: reject null / blank / impossible dates
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("Session ID cannot be null or blank");
        }
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }
        if (createdAt == null || expiresAt == null) {
            throw new IllegalArgumentException("Session times cannot be null");
        }
        // a session that expires at or before its creation could never be valid
        if (!expiresAt.isAfter(createdAt)) {
            throw new IllegalArgumentException("Session must expire after it is created");
        }
        this.id = id;
        this.user = user;
        this.createdAt = createdAt;
        this.expiresAt = expiresAt;
        this.activeRoles = new HashSet<>(); // starts EMPTY: nothing is active by default
        this.revoked = false;
    }

    public String getId() {
        return id;
    }

    // expose the id only, not the User object, so outside code cannot reach in and change the user
    public String getUserId() {
        return user.getId();
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getExpiresAt() {
        return expiresAt;
    }

    public boolean isRevoked() {
        return revoked;
    }

    /**
     * Turn on a role the user holds. Turning on the same role twice is harmless.
     */
    public void activate(Role role) {
        if (role == null) {
            throw new IllegalArgumentException("Role cannot be null");
        }
        // a dead session must not gain power
        if (revoked) {
            throw new IllegalStateException("Cannot activate a role on a revoked session");
        }
        // a user can only turn on roles they actually hold.
        // Roles are equal by NAME, so find the user's OWN instance and store that one,
        // never the object that was passed in (it could be an impostor with the same name
        // but different permits).
        Role held = null;
        for (Role candidate : user.getRoles()) {
            if (candidate.equals(role)) {
                held = candidate;
                break;
            }
        }
        if (held == null) {
            throw new IllegalArgumentException("User does not hold this role");
        }
        activeRoles.add(held); // a Set ignores duplicates, so this is idempotent
    }

    /**
     * Turn a role off. Dropping a role that is not active changes nothing.
     */
    public void drop(Role role) {
        if (role == null) {
            throw new IllegalArgumentException("Role cannot be null");
        }
        activeRoles.remove(role);
    }

    /**
     * Kill the session for good. There is no un-revoke.
     */
    public void revoke() {
        this.revoked = true;
    }

    /**
     * Valid means: not revoked AND strictly before the expiry moment.
     * At exactly expiresAt the session is already invalid.
     * A null "now" is invalid: when unsure, deny (fail closed).
     */
    public boolean isValid(Instant now) {
        if (now == null) {
            return false;
        }
        return !revoked && now.isBefore(expiresAt);
    }

    /**
     * True only if the session is valid AND an ACTIVE role grants the permit
     * AND the user still holds that role.
     *
     * The "still holds" check means: if an admin removes a role from the user,
     * that role stops working inside live sessions at once.
     */
    public boolean hasPermission(Permit permit, Instant now) {
        if (permit == null || !isValid(now)) {
            return false;
        }
        // Walk the roles the user holds RIGHT NOW. A held role counts only if it is active
        // (matched by name). Permits are always read from the user's own instance,
        // so a stale or look-alike object stored earlier can never decide the answer.
        for (Role heldRole : user.getRoles()) {
            if (activeRoles.contains(heldRole) && heldRole.permitted(permit)) {
                return true; // first match is enough
            }
        }
        return false; // default is deny
    }

    /**
     * Returns a copy, so outside code cannot change the real set.
     */
    public Set<Role> getActiveRoles() {
        return new HashSet<>(activeRoles);
    }
}