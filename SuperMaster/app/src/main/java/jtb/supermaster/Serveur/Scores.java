package jtb.supermaster.Serveur;

public class Scores {

    private String pseudo = "Player";

    public Scores(){}

    public Scores(String pseudo){
        super();
        this.pseudo =  pseudo;
    }

    public void setPseudo(String pseudo){
        this.pseudo =  pseudo;
    }

    public String getPseudo(){
        return this.pseudo;
    }
}