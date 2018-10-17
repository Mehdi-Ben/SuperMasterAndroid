package DataOut;

public class ChargerScore {
    int[] maxScores;
    int[] rang;

    public ChargerScore(int[] maxScores,int[] rang) {
        this.maxScores = maxScores;
        this.rang = rang;
    }

    public int[] getMaxScores() {
        return maxScores;
    }

    public void setMaxScores(int[] maxScores) {
        this.maxScores = maxScores;
    }

    public int[] getRang() {
        return rang;
    }

    public void setRang(int[] rang) {
        this.rang = rang;
    }



    public ChargerScore() {}

}
