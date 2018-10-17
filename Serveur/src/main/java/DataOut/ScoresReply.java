package DataOut;

import java.util.ArrayList;

public class ScoresReply {
    private ArrayList<String> pseudos;
    private ArrayList<Integer> scores;

    public ScoresReply() {
    }

    public ScoresReply(ArrayList<String> pseudos, ArrayList<Integer> scores) {
        this.pseudos = pseudos;
        this.scores = scores;
    }

    public ArrayList<String> getPseudos() {
        return pseudos;
    }

    public void setPseudos(ArrayList<String> pseudos) {
        this.pseudos = pseudos;
    }

    public ArrayList<Integer> getScores() {
        return scores;
    }

    public void setScores(ArrayList<Integer> scores) {
        this.scores = scores;
    }
}
