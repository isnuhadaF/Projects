package com.afsec.rbac.exception;

/**
 * Thrown when a check says NO: the caller is known, but may not do this.
 * (Not knowing who the caller is belongs to a different exception.)
 *
 * The message is fixed and says nothing about WHICH permit or role was missing.
 * Details like that go to the audit log, never into an exception that may reach a client.
 */
public class AccessDeniedException extends RuntimeException {

    public AccessDeniedException() {
        super("Access denied");
    }
}