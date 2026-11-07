package com.afsec.rbac.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class SessionTest {

    // Fixed times. No Instant.now() anywhere, so every test gives the same result every run.
    private static final Instant CREATED = Instant.parse("2026-01-01T10:00:00Z");
    private static final Instant EXPIRES = Instant.parse("2026-01-01T11:00:00Z");
    private static final Instant DURING = Instant.parse("2026-01-01T10:30:00Z");

    private static final Permit READ_INVOICE = new Permit("invoice", "read");
    private static final Permit WRITE_INVOICE = new Permit("invoice", "write");

    private User user;
    private Role invoiceReader;
    private Session session;

    @BeforeEach
    void setUp() {
        user = new User("user-1");
        invoiceReader = roleGranting("invoice-reader", READ_INVOICE);
        user.assign(invoiceReader);
        session = new Session("session-1", user, CREATED, EXPIRES);
    }

    /*
     * ASSUMPTION: I have not seen Role.java. This helper is the ONLY place the tests
     * build a role. If your Role constructor or "give this role a permit" method has
     * a different name, change it here and nowhere else.
     */
    private static Role roleGranting(String name, Permit permit) {
        Role role = new Role(name);
        role.grant(permit);
        return role;
    }

    // ---------- construction ----------

    @Test
    void newSessionHasNoActiveRoles() {
        assertThat(session.getActiveRoles()).isEmpty();
    }

    @Test
    void constructorRejectsExpiryThatIsNotAfterCreation() {
        assertThatThrownBy(() -> new Session("s", user, CREATED, CREATED))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> new Session("s", user, EXPIRES, CREATED))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void constructorRejectsNullOrBlankInputs() {
        assertThatThrownBy(() -> new Session(null, user, CREATED, EXPIRES))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> new Session("  ", user, CREATED, EXPIRES))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> new Session("s", null, CREATED, EXPIRES))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> new Session("s", user, null, EXPIRES))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> new Session("s", user, CREATED, null))
                .isInstanceOf(IllegalArgumentException.class);
    }

    // ---------- activate / drop ----------

    @Test
    void activateRejectsRoleTheUserDoesNotHold() {
        Role stranger = roleGranting("stranger", WRITE_INVOICE);

        assertThatThrownBy(() -> session.activate(stranger))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void activateRejectsNullRole() {
        assertThatThrownBy(() -> session.activate(null))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void activatingTheSameRoleTwiceKeepsOneCopy() {
        session.activate(invoiceReader);
        session.activate(invoiceReader);

        assertThat(session.getActiveRoles()).hasSize(1);
    }

    @Test
    void activateOnRevokedSessionIsRejected() {
        session.revoke();

        assertThatThrownBy(() -> session.activate(invoiceReader))
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    void droppingARoleThatIsNotActiveChangesNothing() {
        session.drop(invoiceReader);

        assertThat(session.getActiveRoles()).isEmpty();
    }

    // ---------- the least-privilege rule ----------

    @Test
    void heldButNotActivatedRoleGrantsNothing() {
        // the user HOLDS invoiceReader, but never activated it
        assertThat(session.hasPermission(READ_INVOICE, DURING)).isFalse();
    }

    @Test
    void activatedRoleGrantsItsPermission() {
        session.activate(invoiceReader);

        assertThat(session.hasPermission(READ_INVOICE, DURING)).isTrue();
    }

    @Test
    void activatedRoleDoesNotGrantAPermissionItLacks() {
        session.activate(invoiceReader);

        assertThat(session.hasPermission(WRITE_INVOICE, DURING)).isFalse();
    }

    @Test
    void droppedRoleStopsGranting() {
        session.activate(invoiceReader);
        session.drop(invoiceReader);

        assertThat(session.hasPermission(READ_INVOICE, DURING)).isFalse();
    }

    @Test
    void roleRevokedFromUserStopsWorkingInALiveSession() {
        session.activate(invoiceReader);
        user.revoke(invoiceReader); // admin removes the role while the session is open

        assertThat(session.hasPermission(READ_INVOICE, DURING)).isFalse();
    }

    @Test
    void nullPermitIsDenied() {
        session.activate(invoiceReader);

        assertThat(session.hasPermission(null, DURING)).isFalse();
    }

    // ---------- role hierarchy (Role.setParent: a child inherits its parent's permits) ----------

    @Test
    void activeChildRoleInheritsItsParentsPermission() {
        Role invoiceWriter = roleGranting("invoice-writer", WRITE_INVOICE);
        invoiceWriter.setParent(invoiceReader); // writer inherits reader
        user.assign(invoiceWriter);

        session.activate(invoiceWriter);

        assertThat(session.hasPermission(WRITE_INVOICE, DURING)).isTrue(); // its own
        assertThat(session.hasPermission(READ_INVOICE, DURING)).isTrue();  // inherited
    }

    @Test
    void activeParentRoleDoesNotGainItsChildsPermission() {
        Role invoiceWriter = roleGranting("invoice-writer", WRITE_INVOICE);
        invoiceWriter.setParent(invoiceReader);
        user.assign(invoiceWriter);

        session.activate(invoiceReader); // only the parent is active

        assertThat(session.hasPermission(WRITE_INVOICE, DURING)).isFalse();
    }

    // ---------- role identity (roles are equal by name) ----------

    @Test
    void activatingAnImpostorRoleWithTheSameNameUsesTheUsersOwnRole() {
        // same name as the real role, but it grants WRITE instead of READ
        Role impostor = roleGranting("invoice-reader", WRITE_INVOICE);

        session.activate(impostor);

        assertThat(session.hasPermission(WRITE_INVOICE, DURING)).isFalse(); // impostor's permit is ignored
        assertThat(session.hasPermission(READ_INVOICE, DURING)).isTrue();   // the real role's permit works
        assertThat(session.getActiveRoles()).allSatisfy(r -> assertThat(r).isSameAs(invoiceReader));
    }

    // ---------- validity and time ----------

    @Test
    void isValidBeforeExpiry() {
        assertThat(session.isValid(DURING)).isTrue();
    }

    @Test
    void isNotValidExactlyAtExpiry() {
        assertThat(session.isValid(EXPIRES)).isFalse();
    }

    @Test
    void isNotValidAfterExpiry() {
        assertThat(session.isValid(EXPIRES.plusSeconds(1))).isFalse();
    }

    @Test
    void nullNowIsNotValid() {
        assertThat(session.isValid(null)).isFalse();
    }

    @Test
    void revokedSessionIsNotValidEvenBeforeExpiry() {
        session.revoke();

        assertThat(session.isValid(DURING)).isFalse();
    }

    @Test
    void expiredSessionDeniesPermissionEvenWithActiveRole() {
        session.activate(invoiceReader);

        assertThat(session.hasPermission(READ_INVOICE, EXPIRES)).isFalse();
    }

    @Test
    void revokedSessionDeniesPermissionEvenWithActiveRole() {
        session.activate(invoiceReader);
        session.revoke();

        assertThat(session.hasPermission(READ_INVOICE, DURING)).isFalse();
    }

    // ---------- encapsulation ----------

    @Test
    void getActiveRolesReturnsACopy() {
        session.activate(invoiceReader);

        session.getActiveRoles().clear(); // try to wipe the real set from outside

        assertThat(session.getActiveRoles()).hasSize(1);
    }
}