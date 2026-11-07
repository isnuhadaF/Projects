package com.afsec.rbac.engine;

import com.afsec.rbac.domain.Permit;
import com.afsec.rbac.domain.Session;
import com.afsec.rbac.exception.AccessDeniedException;

import java.time.Instant;

/**
 * The simple yes/no gate. It decides from the SESSION, never from the user alone:
 * only roles the user activated, in a valid session, can allow anything.
 *
 * Fail closed: any null input means NO.
 *
 * This is a first, small version. A later AccessDecisionEngine will also say WHY
 * a request was denied (expired, revoked, user disabled, ...).
 */
public final class Guard {

    // static utility class: nobody should create one
    private Guard() {}

    public static boolean check(Session session, Permit permit, Instant now) {
        if (session == null || permit == null || now == null) {
            return false;
        }
        return session.hasPermission(permit, now);
    }

    /**
     * Same check, but a NO becomes an exception. Even a null session gives
     * AccessDeniedException, not a NullPointerException.
     */
    public static void enforce(Session session, Permit permit, Instant now) {
        if (!check(session, permit, now)) {
            throw new AccessDeniedException();
        }
    }
}