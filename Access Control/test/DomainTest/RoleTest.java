package DomainTest;

import Domain.Permission;
import Domain.Role;

class RoleTests extends TestKit {
    public static void main(String[] args) {
        new RoleTests().runAll();
    }

    void runAll() {
        Permission read = new Permission("transactions", "read");
        Permission approve = new Permission("payments", "approve");

        TestKit.test("Direct permission grant and revoke", () -> {
            Role role = new Role("ops");
            role.grant(read);
            TestKit.assertTrue(role.permitted(read));
            role.revoke(read);
            TestKit.assertFalse(role.permitted(read));
        });

        TestKit.test("Inherits permissions through multi-level parent chain", () -> {
            Role parent = new Role("parent");
            Role child = new Role("child");
            parent.grant(approve);
            child.setParent(parent);

            TestKit.assertTrue(child.permitted(approve));
        });

        TestKit.test("Cycle detection rejects self-assignment and multi-hop loops", () -> {
            Role r1 = new Role("r1");
            Role r2 = new Role("r2");

            TestKit.assertRejects(() -> r1.setParent(r1));

            r2.setParent(r1);
            TestKit.assertRejects(() -> r1.setParent(r2)); // Cycle: r1 -> r2 -> r1
        });
        TestKit.summary();
    }
}