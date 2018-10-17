package jtb.supermaster.Mini_Jeux;

public class Enigme {
    private String question;
    private String droite;
    private String gauche;
    private String reponse;

    public Enigme(String question, String gauche, String droite, String reponse){
        this.question = question;
        this.droite = droite;
        this.gauche = gauche;
        this.reponse = reponse;
    }

    public String getQuestion(){
        return this.question;
    }

    public String getDroite(){
        return this.droite;
    }

    public String getGauche(){
        return this.gauche;
    }

    public String getReponse(){
        return this.reponse;
    }
}
