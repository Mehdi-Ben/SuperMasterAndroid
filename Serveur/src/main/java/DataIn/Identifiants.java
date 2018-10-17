package DataIn;

public class Identifiants {

    private String pseudo;
    private String mdp;

    public Identifiants(){}

    public Identifiants (String pseudo, String mdp){
        super();
        this.pseudo =  pseudo;
        this.mdp = mdp;
    }

    public void setPseudo(String pseudo){
        this.pseudo =  pseudo;
    }

    public String getPseudo(){
        return this.pseudo;
    }

    public void setMdp(String mdp){
        this.mdp =  mdp;
    }

    public String getMdp(){
        return this.mdp;
    }
}
