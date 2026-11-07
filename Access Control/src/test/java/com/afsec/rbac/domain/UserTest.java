package com.afsec.rbac.domain;

import org.junit.jupiter.api.Test;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class UserTest {

    @Test
    void validUserStoresIdCorrectly() {
        User user = new User("usr_payment_01");
        assertThat(user.getId()).isEqualTo("usr_payment_01");
    }

    @Test
    void constructorRejectsNullOrBlankUserId() {
        assertThatThrownBy(() -> new User(null))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> new User(""))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> new User("   "))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void assignAndRevokeRoleUpdateState() {
        User user = new User("usr_payment_01");
        Role role = new Role("ops_analyst");

        user.assign(role);
        assertThat(user.getRoles()).contains(role);

        user.revoke(role);
        assertThat(user.getRoles()).doesNotContain(role);
    }

    @Test
    void assignAndRevokeRejectNullRole() {
        User user = new User("usr_payment_01");
        assertThatThrownBy(() -> user.assign(null))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> user.revoke(null))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void getRolesReturnsDefensiveCopy() {
        User user = new User("usr_payment_01");
        Role role = new Role("ops_analyst");
        user.assign(role);

        Set<Role> externalRoles = user.getRoles();
        externalRoles.clear(); // Attempt external mutation

        // Internal set must remain untouched
        assertThat(user.getRoles()).hasSize(1);
    }

    @Test
    void assigningDuplicateRoleThrowsException() {
        User user = new User("usr_payment_01");
        Role role = new Role("ops_analyst");
        user.assign(role);
        assertThatThrownBy(() -> user.assign(role))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void revokingUnassignedRoleThrowsException() {
        User user = new User("usr_payment_01");
        Role role = new Role("ops_analyst");
        assertThatThrownBy(() -> user.revoke(role))
                .isInstanceOf(IllegalArgumentException.class);
    }
}