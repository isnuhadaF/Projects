package ServiceTest;

import java.util.Objects;

/**
 * Shared helpers for every *Tests file. Each test class "extends TestKit"
 * so it can call test(...), assertTrue(...) and friends directly.
 *
 * One bit of new syntax used by the tests: "() -> { ... }" means
 * "run this block later". test(...) uses it so that one crashing test
 * cannot stop the others.
 */
public class TestKit {

    private static int passed = 0;
    private static int failed = 0;

    static void section(String title) {
        System.out.println();
        System.out.println("--- " + title + " ---");
    }

    /** Runs one test. A wrong answer is FAIL, any crash is ERROR. */
    static void test(String name, Runnable body) {
        try {
            body.run();
            passed++;
            System.out.println("PASS   " + name);
        } catch (AssertionError e) {
            failed++;
            String detail = e.getMessage() == null ? "" : "  -> " + e.getMessage();
            System.out.println("FAIL   " + name + detail);
        } catch (Exception e) {
            failed++;
            System.out.println("ERROR  " + name + "  -> " + e);
        }
    }

    static void assertTrue(boolean condition) {
        if (!condition) throw new AssertionError("expected true but got false");
    }

    static void assertFalse(boolean condition) {
        if (condition) throw new AssertionError("expected false but got true");
    }

    static void assertEquals(Object expected, Object actual) {
        if (!Objects.equals(expected, actual)) {
            throw new AssertionError("expected " + expected + " but got " + actual);
        }
    }

    /** Passes only if the code throws IllegalArgumentException. */
    static void assertRejects(Runnable code) {
        try {
            code.run();
        } catch (IllegalArgumentException e) {
            return;
        }
        throw new AssertionError("expected IllegalArgumentException, but nothing was thrown");
    }

    /** Prints the totals. Exits with code 1 if anything failed. */
    static void summary() {
        System.out.println();
        System.out.println(passed + " passed, " + failed + " failed");
        if (failed > 0) System.exit(1);
    }
}