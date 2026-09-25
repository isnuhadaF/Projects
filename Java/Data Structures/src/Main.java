import java.util.Scanner;
import java.util.List;
import java.util.Deque;
import java.util.Queue;
import java.util.Set;
import java.util.Map;

//ArrayList
//(7)
public int getEvenSum(List<Integer> numbers) {
   int sumEven = 0;
   for (int number : numbers) {
      if (number % 2 == 0) {
         sumEven += number;
      }
   }
   return sumEven;
}

void main(String[] args) {
    Scanner scanner = new Scanner(System.in);
    //ArrayList
    //(1)
    ArrayList<String> favouriteFoods = new ArrayList<>();
    IO.println("Enter your top five favourite foods below:");
    favouriteFoods.add(scanner.nextLine());
    favouriteFoods.add(scanner.nextLine());
    favouriteFoods.add(scanner.nextLine());
    favouriteFoods.add(scanner.nextLine());
    favouriteFoods.add(scanner.nextLine());

    IO.println(favouriteFoods.toString());

    //(2)
    IO.println(favouriteFoods.getFirst());
    IO.println(favouriteFoods.get(2));

    //(3)
    favouriteFoods.add(2, "pizza");
    IO.println(favouriteFoods.toString());

    //(4)
   IO.println(favouriteFoods.contains("pizza"));
    IO.println(favouriteFoods.indexOf("pizza"));

    //(5)
    ArrayList<Integer> numbers = new ArrayList<>();
    numbers.add(10);
    numbers.add(20);
    numbers.add(30);
    numbers.add(40);

    int index = numbers.indexOf(20);
    numbers.remove(index);
    IO.println(numbers.toString());

    //(6)
   for (int i = 0; i < favouriteFoods.size(); i++) {
      IO.println(i + ": " + favouriteFoods.get(i));
   }

    //(7)
   IO.println(getEvenSum(numbers));



   //LinkedList
    List<String> asList = new LinkedList<>();
    Deque<String> asDeque = new LinkedList<>();
    Queue<String> asQueue = new LinkedList<>();


    //



}
