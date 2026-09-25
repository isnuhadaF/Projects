import java.util.Scanner;

void main() {
    Scanner input = new Scanner(System.in);
    String menu = """
            ===================
              Lagbaja schools
            
            Press ENTER to open
            the Student Gradebook
            """;
    IO.println(menu);
    String enter = input.nextLine();


    IO.println("Enter your name, ID, and password");
    String teacherName = input.nextLine();
    int teacherID = input.nextInt();
    String teacherPassword = input.nextLine();
    Teacher teacher = new Teacher(teacherName, teacherID, teacherPassword);

    if (teacherName.equals(teacher.getTeacherName()) && teacherID == teacher.getTeacherID() && teacherPassword.equals(teacher.getTeacherPassword())) {

    }

    IO.println("How many students are you grading?");
    int numberOfStudents = input.nextInt();
    input.nextLine();

    Student[] students = new Student[numberOfStudents];
    IO.println("Enter their names below:");
    for (int studentCounter = 0; studentCounter <= numberOfStudents-1; studentCounter++) {
        String studentName = input.nextLine();
        students[studentCounter] = new Student(studentName);
    }

    IO.println("How many subjects were taken?");
    int numberOfSubjects = input.nextInt();
    input.nextLine();
    Subject[] subjects = new Subject[numberOfSubjects];

    IO.println("Enter the name of the subjects taken below:");
    for (int subjectCounter = 0; subjectCounter < numberOfSubjects-1; subjectCounter++) {
        String subjectName = input.nextLine();
        subjects[subjectCounter] = new Subject(subjectName);
    }

    for (Subject subject : subjects) {
        for (Student student : students) {
            IO.println("Enter " + student + "'s score below:");
            int studentScore = input.nextInt();


        }
    }

}
