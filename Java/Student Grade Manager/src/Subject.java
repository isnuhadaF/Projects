public class Subject {
    String name;
    private int totalScore;
    private int averageScore;

    public Subject(String name) {
        this.name = name;
    }


    public int totalScore(int[] scores) {
        int totalScores = 0;
        for (int score : scores) {
            totalScores += score;
        }

        return totalScores;
    }

    public int averageScore(int[] scores) {
        int averageScore = totalScore(scores)/scores.length;

        return averageScore;
    }
}
