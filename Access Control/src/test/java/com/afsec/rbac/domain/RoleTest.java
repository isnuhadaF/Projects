package com.afsec.rbac.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class RoleTest {

    private static final Permit READ = new Permit("invoice", "read");
    private static final Permit WRITE = new Permit("invoice", "write");
    private static final Permit DELETE = new Permit("invoice", "delete");

    // ---------- construction and name rules ----------

    @Test
    void newRoleHasNoPermitsAndNoParent() {
        Role role = new Role("clerk");

        assertThat(role.getPermits()).isEmpty();
        assertThat(role.getParent()).isNull();
        assertThat(role.isSystemProtected()).isFalse();
    }

    @Test
    void constructorRejectsNullOrBlankName() {
        assertThatThrownBy(() -> new Role(null)).isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> new Role("")).isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> new Role("   ")).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void constructorRejectsUppercaseSpacesAndSymbols() {
        assertThatThrownBy(() -> new Role("Admin")).isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> new Role("invoice reader")).isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> new Role("admin;drop")).isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> new Role("admin\n")).isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> new Role("role:admin")).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void constructorRejectsNameLongerThan64Characters() {
        assertThatThrownBy(() -> new Role("a".repeat(65))).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void constructorAcceptsLowercaseDigitsUnderscoreAndHyphen() {
        assertThat(new Role("a".repeat(64)).getName()).hasSize(64); // the limit itself is allowed
        assertThat(new Role("invoice-reader_2").getName()).isEqualTo("invoice-reader_2");
    }

    // ---------- grant / revoke ----------

    @Test
    void grantAddsThePermit() {
        Role role = new Role("clerk");

        role.grant(READ);

        assertThat(role.permitted(READ)).isTrue();
        assertThat(role.getPermits()).containsExactly(READ);
    }

    @Test
    void grantingTheSamePermitTwiceKeepsOneCopy() {
        Role role = new Role("clerk");

        role.grant(READ);
        role.grant(READ);

        assertThat(role.getPermits()).hasSize(1);
    }

    @Test
    void grantRejectsNull() {
        Role role = new Role("clerk");

        assertThatThrownBy(() -> role.grant(null)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void revokeRemovesThePermit() {
        Role role = new Role("clerk");
        role.grant(READ);

        role.revoke(READ);

        assertThat(role.permitted(READ)).isFalse();
    }

    @Test
    void revokeOfAPermitThatWasNeverGrantedChangesNothing() {
        Role role = new Role("clerk");
        role.grant(READ);

        role.revoke(WRITE);

        assertThat(role.getPermits()).containsExactly(READ);
    }

    @Test
    void revokeRejectsNull() {
        Role role = new Role("clerk");

        assertThatThrownBy(() -> role.revoke(null)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void getPermitsReturnsACopy() {
        Role role = new Role("clerk");
        role.grant(READ);

        role.getPermits().clear(); // try to wipe the real set from outside

        assertThat(role.getPermits()).hasSize(1);
    }

    @Test
    void permittedIsFalseForNull() {
        Role role = new Role("clerk");
        role.grant(READ);

        assertThat(role.permitted(null)).isFalse();
    }

    // ---------- hierarchy ----------

    @Test
    void childInheritsItsParentsPermit() {
        Role parent = new Role("reader");
        parent.grant(READ);
        Role child = new Role("writer");
        child.grant(WRITE);

        child.setParent(parent);

        assertThat(child.permitted(READ)).isTrue();  // inherited
        assertThat(child.permitted(WRITE)).isTrue(); // its own
    }

    @Test
    void parentDoesNotInheritItsChildsPermit() {
        Role parent = new Role("reader");
        parent.grant(READ);
        Role child = new Role("writer");
        child.grant(WRITE);
        child.setParent(parent);

        assertThat(parent.permitted(WRITE)).isFalse();
    }

    @Test
    void grandchildInheritsFromGrandparent() {
        Role grandparent = new Role("reader");
        grandparent.grant(READ);
        Role parent = new Role("writer");
        Role child = new Role("manager");
        parent.setParent(grandparent);
        child.setParent(parent);

        assertThat(child.permitted(READ)).isTrue();
    }

    @Test
    void setParentRejectsTheRoleItself() {
        Role role = new Role("clerk");

        assertThatThrownBy(() -> role.setParent(role)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void setParentRejectsATwoRoleLoop() {
        Role a = new Role("role-a");
        Role b = new Role("role-b");
        a.setParent(b);

        assertThatThrownBy(() -> b.setParent(a)).isInstanceOf(IllegalArgumentException.class);
        assertThat(b.getParent()).isNull(); // the failed attempt changed nothing
    }

    @Test
    void setParentRejectsALongLoop() {
        Role a = new Role("role-a");
        Role b = new Role("role-b");
        Role c = new Role("role-c");
        a.setParent(b);
        b.setParent(c);

        assertThatThrownBy(() -> c.setParent(a)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void setParentToNullClearsTheParent() {
        Role parent = new Role("reader");
        parent.grant(READ);
        Role child = new Role("writer");
        child.setParent(parent);

        child.setParent(null);

        assertThat(child.getParent()).isNull();
        assertThat(child.permitted(READ)).isFalse();
    }

    // ---------- effectiveRoles ----------

    @Test
    void effectiveRolesOfARootRoleIsOnlyItself() {
        Role role = new Role("clerk");

        assertThat(role.effectiveRoles()).containsExactly(role);
    }

    @Test
    void effectiveRolesIncludesSelfAndAllAncestorsNearestFirst() {
        Role grandparent = new Role("grandparent");
        Role parent = new Role("parent");
        Role child = new Role("child");
        parent.setParent(grandparent);
        child.setParent(parent);

        assertThat(child.effectiveRoles()).containsExactly(child, parent, grandparent);
    }

    @Test
    void effectiveRolesReturnsACopy() {
        Role role = new Role("clerk");

        role.effectiveRoles().clear();

        assertThat(role.effectiveRoles()).hasSize(1);
    }

    // ---------- equality (by name) ----------

    @Test
    void rolesWithTheSameNameAreEqualWithTheSameHashCode() {
        Role first = new Role("clerk");
        Role second = new Role("clerk");

        assertThat(first).isEqualTo(second);
        assertThat(first.hashCode()).isEqualTo(second.hashCode());
    }

    @Test
    void rolesWithDifferentNamesAreNotEqual() {
        assertThat(new Role("clerk")).isNotEqualTo(new Role("manager"));
    }

    @Test
    void equalityIgnoresPermits() {
        Role first = new Role("clerk");
        first.grant(READ);
        Role second = new Role("clerk");
        second.grant(DELETE);

        assertThat(first).isEqualTo(second);
    }

    @Test
    void sameNameRolesCollapseToOneInASet() {
        java.util.Set<Role> roles = new java.util.HashSet<>();
        roles.add(new Role("clerk"));
        roles.add(new Role("clerk"));

        assertThat(roles).hasSize(1);
    }

    // ---------- system roles (frozen) ----------

    @Test
    void systemRoleHoldsItsPermitsFromCreation() {
        Role admin = Role.systemRole("admin", READ, WRITE);

        assertThat(admin.permitted(READ)).isTrue();
        assertThat(admin.permitted(WRITE)).isTrue();
        assertThat(admin.permitted(DELETE)).isFalse();
    }

    @Test
    void systemRoleIsMarkedProtected() {
        assertThat(Role.systemRole("admin", READ).isSystemProtected()).isTrue();
    }

    @Test
    void systemRoleRejectsGrant() {
        Role admin = Role.systemRole("admin", READ);

        assertThatThrownBy(() -> admin.grant(WRITE)).isInstanceOf(IllegalStateException.class);
        assertThat(admin.permitted(WRITE)).isFalse();
    }

    @Test
    void systemRoleRejectsRevoke() {
        Role admin = Role.systemRole("admin", READ);

        assertThatThrownBy(() -> admin.revoke(READ)).isInstanceOf(IllegalStateException.class);
        assertThat(admin.permitted(READ)).isTrue();
    }

    @Test
    void systemRoleRejectsSetParent() {
        Role admin = Role.systemRole("admin", READ);

        assertThatThrownBy(() -> admin.setParent(new Role("other"))).isInstanceOf(IllegalStateException.class);
        assertThat(admin.getParent()).isNull();
    }

    @Test
    void systemRoleIsNotAffectedByLaterChangesToTheArrayPassedIn() {
        Permit[] given = {READ};
        Role admin = Role.systemRole("admin", given);

        given[0] = DELETE; // caller changes its own array afterwards

        assertThat(admin.permitted(READ)).isTrue();
        assertThat(admin.permitted(DELETE)).isFalse();
    }

    @Test
    void systemRoleRejectsANullPermit() {
        assertThatThrownBy(() -> Role.systemRole("admin", READ, null))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void normalRoleCanInheritFromASystemRole() {
        Role admin = Role.systemRole("admin", READ);
        Role helper = new Role("helper");

        helper.setParent(admin);

        assertThat(helper.permitted(READ)).isTrue();
    }

    @Test
    void normalRoleIsNotProtected() {
        assertThat(new Role("clerk").isSystemProtected()).isFalse();
    }
}