package DataIn;
import java.util.ArrayList;

public class InitPartie {
    private String mode;
    private int unites;
    private int difficulte;
    private String listeJeux;

    public InitPartie(){}

    public InitPartie(String mode, int unites, int difficulte, String listeJeux) {
        this.mode = mode;
        this.unites = unites;
        this.difficulte = difficulte;
        this.listeJeux = listeJeux;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public int getUnites() {
        return unites;
    }

    public void setUnites(int unites) {
        this.unites = unites;
    }

    public int getDifficulte() {
        return difficulte;
    }

    public void setDifficulte(int difficulte) {
        this.difficulte = difficulte;
    }

    public String getListeJeux() {
        return listeJeux;
    }

    public void setListeJeux(String listeJeux) {
        this.listeJeux = listeJeux;
    }

}
