package DataIn;

public class Sauvegarder {

    private String pseudo;

    public Sauvegarder(String pseudo, String value) {
        this.pseudo = pseudo;
        this.value = value;
    }

    private String value;

    public Sauvegarder(){}

    public Sauvegarder(String pseudo){
        super();
        this.pseudo =  pseudo;
    }

    public void setPseudo(String pseudo){
        this.pseudo =  pseudo;
    }

    public String getPseudo(){
        return this.pseudo;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
