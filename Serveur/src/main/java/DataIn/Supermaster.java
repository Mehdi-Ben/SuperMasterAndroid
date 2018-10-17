package DataIn;

public class Supermaster {

    private String pseudo;

    public Supermaster(){}

    public Supermaster(String pseudo){
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
