package com.afsec.rbac.domain;

/**
 * One thing a role may do: an ACTION on a RESOURCE, e.g. "invoices:read".
 *
 * Rules (checked once, in the constructor, so a bad Permit can never exist):
 * - each part is 1 to 64 characters
 * - only a-z, 0-9 and '_' are allowed
 * - NO wildcards. A permit means exactly what it says, nothing more.
 * - uppercase is REJECTED, not quietly changed, so there is one spelling per permit
 *
 * Only plain ASCII passes, so look-alike letters from other alphabets
 * (for example a Cyrillic "a" inside "payments") are rejected too.
 */
public record Permit(String resource, String action) {

    private static final int MAX_PART_LENGTH = 64;

    public Permit {
        validate(resource, "resource");
        validate(action, "action");
    }

    private static void validate(String value, String fieldName) {
        if (value == null || value.isEmpty()) {
            throw new IllegalArgumentException(fieldName + " must not be empty");
        }
        if (value.length() > MAX_PART_LENGTH) {
            throw new IllegalArgumentException(fieldName + " is too long");
        }
        // check one character at a time against a short allow-list
        for (int index = 0; index < value.length(); index++) {
            char character = value.charAt(index);
            boolean allowed = (character >= 'a' && character <= 'z')
                    || (character >= '0' && character <= '9')
                    || character == '_';
            if (!allowed) {
                throw new IllegalArgumentException(
                        fieldName + " has an invalid character at position " + index);
            }
        }
    }

    @Override
    public String toString() {
        return resource + ":" + action;
    }
}