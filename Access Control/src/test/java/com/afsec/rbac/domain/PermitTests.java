package com.afsec.rbac.domain;

import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PermitTest {

    // ---------- valid input ----------

    @Test
    void acceptsValidResourceAndAction() {
        Permit permit = new Permit("payments", "approve");
        assertThat(permit).isNotNull();
    }

    @Test
    void acceptsUnderscoreAndDigits() {
        assertThat(new Permit("audit_logs", "read")).isNotNull();
        assertThat(new Permit("report2", "export_v2")).isNotNull();
    }

    @Test
    void acceptsPartsOfExactly64Characters() {
        assertThat(new Permit("a".repeat(64), "b".repeat(64))).isNotNull();
    }

    // ---------- invalid input ----------

    @Test
    void rejectsNullOrBlankResource() {
        assertThatThrownBy(() -> new Permit(null, "approve"))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> new Permit("", "approve"))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> new Permit(" ", "approve"))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void rejectsNullOrBlankAction() {
        assertThatThrownBy(() -> new Permit("payments", null))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> new Permit("payments", ""))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> new Permit("payments", " "))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void rejectsWildcards() {
        assertThatThrownBy(() -> new Permit("*", "approve"))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> new Permit("payments", "*"))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> new Permit("*", "*"))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void rejectsUppercaseInsteadOfQuietlyChangingIt() {
        assertThatThrownBy(() -> new Permit("Payments", "approve"))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> new Permit("payments", "APPROVE"))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void rejectsInvalidCharactersAndSpacing() {
        assertThatThrownBy(() -> new Permit("pay:ments", "approve"))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> new Permit("payments", "appr:ove"))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> new Permit(" payments", "approve"))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> new Permit("payments ", "approve"))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> new Permit("pay-ments", "approve"))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void rejectsControlCharactersAndNewlines() {
        assertThatThrownBy(() -> new Permit("payments\n", "approve"))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> new Permit("pay\tments", "approve"))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> new Permit("payments", "app\u0000rove"))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void rejectsLookAlikeLettersFromOtherAlphabets() {
        // \u0430 is the Cyrillic "a": it looks like Latin "a" but is a different character
        assertThatThrownBy(() -> new Permit("p\u0430yments", "approve"))
                .isInstanceOf(IllegalArgumentException.class);
        // \u0130 is the Turkish capital dotted I
        assertThatThrownBy(() -> new Permit("payments", "approve\u0130"))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void rejectsPartsLongerThan64Characters() {
        assertThatThrownBy(() -> new Permit("a".repeat(65), "read"))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> new Permit("payments", "a".repeat(65)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void errorMessageNamesTheBadField() {
        assertThatThrownBy(() -> new Permit("payments", "app rove"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("action");
    }

    // ---------- equality ----------

    @Test
    void objectEqualsItself() {
        Permit p = new Permit("payments", "approve");
        assertThat(p).isEqualTo(p);
    }

    @Test
    void twoObjectsWithSameWordsAreEqual() {
        Permit p1 = new Permit("payments", "approve");
        Permit p2 = new Permit("payments", "approve");
        assertThat(p1).isEqualTo(p2);
    }

    @Test
    void sameResourceDifferentActionAreNotEqual() {
        Permit approve = new Permit("payments", "approve");
        Permit initiate = new Permit("payments", "initiate");
        assertThat(approve).isNotEqualTo(initiate);
    }

    @Test
    void equalObjectsHaveEqualHashCodes() {
        Permit p1 = new Permit("payments", "approve");
        Permit p2 = new Permit("payments", "approve");
        assertThat(p1.hashCode()).isEqualTo(p2.hashCode());
    }

    @Test
    void hashSetKeepsOneCopyOfTwoEqualPermissions() {
        Set<Permit> set = new HashSet<>();
        set.add(new Permit("payments", "approve"));
        set.add(new Permit("payments", "approve"));
        assertThat(set).hasSize(1);
    }

    // ---------- text form ----------

    @Test
    void toStringIsResourceColonAction() {
        Permit permit = new Permit("payments", "approve");
        assertThat(permit.toString()).isEqualTo("payments:approve");
    }
}