package DomainTest;

import Domain.Permission;

import java.util.HashSet;
import java.util.Set;

/**
 * Tests for Domain.Permission only. Needs: TestKit.java, Domain.Permission.java
 *
 * Run just this file:
 *   javac TestKit.java Domain.Permission.java DomainTest.PermissionTests.java && java DomainTest.PermissionTests
 *
 * Every rejection must throw IllegalArgumentException.
 */
class PermissionTests extends TestKit {
    void main(String[] args) {
        run();
        TestKit.summary();
    }

    static void run() {
        constructorTests();
        equalityTests();
    }

    private static void constructorTests() {
        TestKit.section("Domain.Permission: constructor");

        TestKit.test("accepts a valid resource and action", () -> {
            new Permission("payments", "approve");
        });
        TestKit.test("rejects null resource", () -> TestKit.assertRejects(() -> new Permission(null, "approve")));
        TestKit.test("rejects empty resource", () -> TestKit.assertRejects(() -> new Permission("", "approve")));
        TestKit.test("rejects blank resource", () -> TestKit.assertRejects(() -> new Permission(" ", "approve")));
        TestKit.test("rejects null action", () -> TestKit.assertRejects(() -> new Permission("payments", null)));
        TestKit.test("rejects empty action", () -> TestKit.assertRejects(() -> new Permission("payments", "")));
        TestKit.test("rejects blank action", () -> TestKit.assertRejects(() -> new Permission("payments", " ")));
        TestKit.test("rejects * in resource", () -> TestKit.assertRejects(() -> new Permission("pay*", "approve")));
        TestKit.test("rejects * in action", () -> TestKit.assertRejects(() -> new Permission("payments", "appr*")));
        TestKit.test("rejects : in resource", () -> TestKit.assertRejects(() -> new Permission("pay:ments", "approve")));
        TestKit.test("rejects : in action", () -> TestKit.assertRejects(() -> new Permission("payments", "appr:ove")));
        TestKit.test("rejects leading space in resource", () ->
                TestKit.assertRejects(() -> new Permission(" payments", "approve")));
        TestKit.test("rejects trailing space in resource", () ->
                TestKit.assertRejects(() -> new Permission("payments ", "approve")));
        TestKit.test("rejects leading space in action", () ->
                TestKit.assertRejects(() -> new Permission("payments", " approve")));
        TestKit.test("rejects trailing space in action", () ->
                TestKit.assertRejects(() -> new Permission("payments", "approve ")));
        TestKit.test("rejects leading tab in resource", () ->
                TestKit.assertRejects(() -> new Permission("\tpayments", "approve")));
        TestKit.test("rejects trailing newline in resource", () ->
                TestKit.assertRejects(() -> new Permission("payments\n", "approve")));
        TestKit.test("rejects leading tab in action", () ->
                TestKit.assertRejects(() -> new Permission("payments", "\tapprove")));
        TestKit.test("rejects trailing carriage return in resource", () ->
                TestKit.assertRejects(() -> new Permission("payments\r", "approve")));
        TestKit.test("rejects form feed", () ->
                TestKit.assertRejects(() -> new Permission("pay\fments", "approve")));
        TestKit.test("rejects non-breaking space", () ->
                TestKit.assertRejects(() -> new Permission("pay\u00A0ments", "approve")));
        TestKit.test("rejects zero-width space", () ->
                TestKit.assertRejects(() -> new Permission("pay\u200Bments", "approve")));
        TestKit.test("rejects Cyrillic lookalike letter", () ->
                TestKit.assertRejects(() -> new Permission("p\u0430yments", "approve")));
        TestKit.test("accepts underscore", () -> { new Permission("audit_logs", "read"); });
        TestKit.test("error message names the bad field", () -> {
            try {
                new Permission("payments", "app rove");
            } catch (IllegalArgumentException e) {
                TestKit.assertTrue(e.getMessage().contains("action"));
                return;
            }
            throw new AssertionError("nothing was thrown");
        });
    }

    private static void equalityTests() {
        TestKit.section("Domain.Permission: equals, hashCode, toString");

        TestKit.test("an object equals itself", () -> {
            Permission p = new Permission("payments", "approve");
            TestKit.assertTrue(p.equals(p));
        });
        TestKit.test("two objects with the same words are equal", () -> {
            Permission p1 = new Permission("payments", "approve");
            Permission p2 = new Permission("payments", "approve");
            TestKit.assertTrue(p1.equals(p2));
        });
        TestKit.test("same resource, different action is NOT equal", () -> {
            Permission approve = new Permission("payments", "approve");
            Permission initiate = new Permission("payments", "initiate");
            TestKit.assertFalse(approve.equals(initiate));
        });
        TestKit.test("same action, different resource is NOT equal", () -> {
            Permission a = new Permission("payments", "read");
            Permission b = new Permission("reports", "read");
            TestKit.assertFalse(a.equals(b));
        });
        TestKit.test("is not equal to null", () -> {
            Permission p = new Permission("payments", "approve");
            TestKit.assertFalse(p.equals(null));
        });
        TestKit.test("is not equal to a String with the same text", () -> {
            Permission p = new Permission("payments", "approve");
            TestKit.assertFalse(p.equals("payments:approve"));
        });
        TestKit.test("equal objects have equal hash codes", () -> {
            Permission p1 = new Permission("payments", "approve");
            Permission p2 = new Permission("payments", "approve");
            TestKit.assertEquals(p1.hashCode(), p2.hashCode());
        });
        TestKit.test("HashSet keeps one copy of two equal permissions", () -> {
            Set<Permission> set = new HashSet<>();
            set.add(new Permission("payments", "approve"));
            set.add(new Permission("payments", "approve"));
            TestKit.assertEquals(1, set.size());
        });
        TestKit.test("HashSet finds a permission using a different but equal object", () -> {
            Set<Permission> set = new HashSet<>();
            set.add(new Permission("payments", "approve"));
            TestKit.assertTrue(set.contains(new Permission("payments", "approve")));
        });
        TestKit.test("toString is resource:action", () -> {
            TestKit.assertEquals("payments:approve", new Permission("payments", "approve").toString());
        });

        TestKit.test("upper and lower case are different permissions", () -> {
            Permission upper = new Permission("Payments", "approve");
            Permission lower = new Permission("payments", "approve");
            TestKit.assertFalse(upper.equals(lower));
        });
    }
}