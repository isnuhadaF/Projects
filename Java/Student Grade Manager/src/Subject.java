public class Subject {
    String name;

    public Subject(String name) {
        this.name = name;
    }

    public int totalScore(int[] studentScores) {
        int totalScore = 0;
        for (int score : studentScores) {
            totalScore += score;
        }
        return totalScore;
    }

    public int averageScore(int[] studentScores) {
        return totalScore(studentScores)/studentScores.length;
    }
}
