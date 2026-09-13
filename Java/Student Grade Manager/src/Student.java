public class Student {
    private String name;
    private Score[] studentScores;

    public Student(String name, int numberOfSubjects) {
        this.name = name;
        this.studentScores = new Score[numberOfSubjects];

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Score getScore(int subject) {
        this.studentScores[subject] = studentScores[subject];

        return studentScores[subject];
    }

    public void setScore(int studentScore, int subject) {
        studentScores[subject] = new Score(studentScore);
        subject++;
    }
}
