import java.util.ArrayList;

public class Teacher {
    private final String teacherName;
    private final int teacherID;
    private final String teacherPassword;
    private ArrayList<Subject> studentScores;

    public Teacher(String teacherName, int teacherID, String teacherPassword) {
        this.teacherName = teacherName;
        this.teacherID = teacherID;
        this.teacherPassword = teacherPassword;
        this.studentScores = new ArrayList<>();
    }

    public String getTeacherName() {
        return teacherName;
    }

    public int getTeacherID() {
        return teacherID;
    }

    public String getTeacherPassword() {
        return teacherPassword;
    }

    public ArrayList<Subject> getStudentScores() {
        return studentScores;
    }

    public void setStudentScores(ArrayList<Subject> studentScores) {
        this.studentScores = studentScores;
    }

//    public double calculateClassAverage(ArrayList<Subject> studentScores) {
//
//        }

}
