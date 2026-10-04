package Domain;

import java.util.Objects;

public class Permission {
    private final String resource;
    private final String action;

    public Permission(String resource, String action) {
        validate(resource, "resource");
        validate(action, "action");
        this.resource = resource;
        this.action = action;
    }

    private static void validate(String value, String fieldName) {
        // null or empty? throw
        if (value == null || value.isEmpty()) {
            throw new IllegalArgumentException(fieldName + " must not be empty");
        }

        // look at every character; reject the first one that is not allowed
        for (int index = 0; index < value.length(); index++) {
            char character = value.charAt(index);
            boolean allowed = (character >= 'a' && character <= 'z')
                    || (character >= 'A' && character <= 'Z')
                    || (character >= '0' && character <= '9')
                    || character == '_';
            if (!allowed) {
                throw new IllegalArgumentException(
                        fieldName + " has an invalid character at position " + index);
            }
        }
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;

        Permission other = (Permission) object;
        return resource.equals(other.resource) && action.equals(other.action);
    }

    @Override
    public int hashCode() {
        return Objects.hash(resource, action);
    }

    @Override
    public String toString() {
        return resource + ":" + action;
    }
}
