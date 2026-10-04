package Domain;

import java.util.HashSet;
import java.util.Set;

public class User {
    private final String id;
    private final Set<Role> roles = new HashSet<>();

    public User(String userID) {
        if (userID == null || userID.isBlank()) {
            throw new IllegalArgumentException("Domain.User ID cannot be null or blank");
        }
        this.id = userID;
    }

    public String getId() {
        return this.id;
    }

    public void assign(Role role) {
        if (role == null) {
            throw new IllegalArgumentException("Domain.Role cannot be null");
        }
        roles.add(role);
    }

    public void revoke(Role role) {
        if (role == null) {
            throw new IllegalArgumentException("Domain.Role cannot be null");
        }
        roles.remove(role);
    }

    public Set<Role> getRoles() {
        return new HashSet<>(this.roles);
    }
}