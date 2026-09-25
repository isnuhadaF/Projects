import java.util.concurrent.atomic.AtomicInteger;

public class Account extends Bank{
    private String accountBalance;
    private AtomicInteger atomicNumber;

    public Account(AtomicInteger atomicNumber) {
        this.atomicNumber = atomicNumber;
    }
}
