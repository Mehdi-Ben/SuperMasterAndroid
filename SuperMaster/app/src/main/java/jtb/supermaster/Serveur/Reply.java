package jtb.supermaster.Serveur;

public class Reply {

    private int joueurs = 0;

    public Reply(){}

    public Reply(int joueurs){
        super();
        this.joueurs =  joueurs;
    }

    public void setJoueurs(int joueurs){
        this.joueurs =  joueurs;
    }

    public int getJoueurs(){
        return this.joueurs;
    }
}