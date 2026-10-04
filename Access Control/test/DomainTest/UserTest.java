package DomainTest;

import Domain.Role;
import Domain.User;

import java.util.Set;

class UserTests extends TestKit {

    public static void main(String[] args) {
        new UserTests().runAll();
    }

    void runAll() {
        TestKit.test("1. Valid user stores ID correctly", () -> {
            User user = new User("usr_payment_01");
            TestKit.assertEquals("usr_payment_01", user.getId());
        });

        TestKit.test("2. Constructor rejects null or blank user ID", () -> {
            TestKit.assertRejects(() -> new User(null));
            TestKit.assertRejects(() -> new User(""));
            TestKit.assertRejects(() -> new User("   "));
        });

        TestKit.test("3. Assign and revoke role update state", () -> {
            User user = new User("usr_payment_01");
            Role role = new Role("ops_analyst");

            user.assign(role);
            TestKit.assertTrue(user.getRoles().contains(role));

            user.revoke(role);
            TestKit.assertFalse(user.getRoles().contains(role));
        });

        TestKit.test("4. Assign and revoke reject null role", () -> {
            User user = new User("usr_payment_01");
            TestKit.assertRejects(() -> user.assign(null));
            TestKit.assertRejects(() -> user.revoke(null));
        });

        TestKit.test("5. getRoles returns a defensive copy", () -> {
            User user = new User("usr_payment_01");
            Role role = new Role("ops_analyst");
            user.assign(role);

            Set<Role> externalRoles = user.getRoles();
            externalRoles.clear(); // Attempt external mutation

            // Internal set must remain untouched
            TestKit.assertEquals(1, user.getRoles().size());
        });

        TestKit.summary();
    }
}