import java.util.Scanner;

public class Main {
    static void main() {
        Scanner input = new Scanner(System.in);

        IO.println("Press Enter to Start the Bike");
        String enter = input.nextLine();
        Bike bike = new Bike();
        bike.powerOn();


    }
}