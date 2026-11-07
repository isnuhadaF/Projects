package com.afsec.rbac.engine;

import com.afsec.rbac.domain.Permit;
import com.afsec.rbac.domain.Role;
import com.afsec.rbac.domain.Session;
import com.afsec.rbac.domain.User;
import com.afsec.rbac.exception.AccessDeniedException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class GuardTest {

    // Fixed times: every run gives the same result.
    private static final Instant CREATED = Instant.parse("2026-01-01T10:00:00Z");
    private static final Instant EXPIRES = Instant.parse("2026-01-01T11:00:00Z");
    private static final Instant DURING = Instant.parse("2026-01-01T10:30:00Z");

    private static final Permit READ_INVOICES = new Permit("invoices", "read");
    private static final Permit DELETE_INVOICES = new Permit("invoices", "delete");

    private User user;
    private Role accountant;
    private Session session;

    @BeforeEach
    void setUp() {
        user = new User("usr_01");
        accountant = new Role("accountant");
        accountant.grant(READ_INVOICES);
        user.assign(accountant);
        session = new Session("sess_01", user, CREATED, EXPIRES);
    }

    // ---------- check ----------

    @Test
    void allowsAccessWhenAnActiveRoleGrantsThePermit() {
        session.activate(accountant);

        assertThat(Guard.check(session, READ_INVOICES, DURING)).isTrue();
    }

    @Test
    void deniesAccessWhenTheRoleIsHeldButNotActivated() {
        // the user HOLDS the role, but never turned it on: least privilege
        assertThat(Guard.check(session, READ_INVOICES, DURING)).isFalse();
    }

    @Test
    void deniesAccessWhenNoActiveRoleGrantsThePermit() {
        session.activate(accountant);

        assertThat(Guard.check(session, DELETE_INVOICES, DURING)).isFalse();
    }

    @Test
    void deniesAccessWhenTheSessionHasExpired() {
        session.activate(accountant);

        assertThat(Guard.check(session, READ_INVOICES, EXPIRES)).isFalse();
    }

    @Test
    void deniesAccessWhenTheSessionIsRevoked() {
        session.activate(accountant);
        session.revoke();

        assertThat(Guard.check(session, READ_INVOICES, DURING)).isFalse();
    }

    @Test
    void failSecureOnNullSessionPermitOrTime() {
        session.activate(accountant);

        assertThat(Guard.check(null, READ_INVOICES, DURING)).isFalse();
        assertThat(Guard.check(session, null, DURING)).isFalse();
        assertThat(Guard.check(session, READ_INVOICES, null)).isFalse();
        assertThat(Guard.check(null, null, null)).isFalse();
    }

    // ---------- enforce ----------

    @Test
    void enforceThrowsAccessDeniedExceptionWhenDenied() {
        assertThatThrownBy(() -> Guard.enforce(session, READ_INVOICES, DURING))
                .isInstanceOf(AccessDeniedException.class);
    }

    @Test
    void enforceMessageRevealsNothingAboutThePermit() {
        assertThatThrownBy(() -> Guard.enforce(session, READ_INVOICES, DURING))
                .hasMessage("Access denied")
                .hasMessageNotContaining("invoices");
    }

    @Test
    void enforceGivesAccessDeniedNotNullPointerForNullSession() {
        assertThatThrownBy(() -> Guard.enforce(null, READ_INVOICES, DURING))
                .isInstanceOf(AccessDeniedException.class);
    }

    @Test
    void enforcePassesSilentlyWhenAllowed() {
        session.activate(accountant);

        assertThatCode(() -> Guard.enforce(session, READ_INVOICES, DURING))
                .doesNotThrowAnyException();
    }
}