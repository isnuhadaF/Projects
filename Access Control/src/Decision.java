import Domain.Permission;

import java.util.HashSet;
import java.util.Set;

public class Decision {
    public static void main(String[] args) {
        Set<Permission> set = new HashSet<>();
        set.add(new Permission("payments", "approve"));
        set.add(new Permission("payments", "approve"));
        System.out.println(set.size());
    }
}