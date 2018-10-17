package DataOut;

public class MeilleursScoresReply {
    int score1;
    int score2;
    int score3;
    String pseudo1;
    String pseudo2;
    String pseudo3;

    public MeilleursScoresReply() {}

    public MeilleursScoresReply(int score1, int score2, int score3, String pseudo1, String pseudo2, String pseudo3) {
        this.score1 = score1;
        this.score2 = score2;
        this.score3 = score3;
        this.pseudo1 = pseudo1;
        this.pseudo2 = pseudo2;
        this.pseudo3 = pseudo3;
    }

    public int getScore1() {
        return score1;
    }

    public void setScore1(int score1) {
        this.score1 = score1;
    }

    public int getScore2() {
        return score2;
    }

    public void setScore2(int score2) {
        this.score2 = score2;
    }

    public int getScore3() {
        return score3;
    }

    public void setScore3(int score3) {
        this.score3 = score3;
    }

    public String getPseudo1() {
        return pseudo1;
    }

    public void setPseudo1(String pseudo1) {
        this.pseudo1 = pseudo1;
    }

    public String getPseudo2() {
        return pseudo2;
    }

    public void setPseudo2(String pseudo2) {
        this.pseudo2 = pseudo2;
    }

    public String getPseudo3() {
        return pseudo3;
    }

    public void setPseudo3(String pseudo3) {
        this.pseudo3 = pseudo3;
    }
}
