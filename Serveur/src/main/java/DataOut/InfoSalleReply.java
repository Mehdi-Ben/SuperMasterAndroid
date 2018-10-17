package DataOut;

public class InfoSalleReply {

    private boolean disponible;
    private boolean salle_ouverte;

    public InfoSalleReply(){}

    public InfoSalleReply(boolean disponible, boolean salle_ouverte){
        super();
        this.disponible = disponible;
        this.salle_ouverte =  salle_ouverte;
    }

    public void setDisponible(boolean disponible){
        this.disponible =  disponible;
    }

    public boolean getDisponible(){
        return this.disponible;
    }

    public void setSalle_ouverte(boolean salle_ouverte){ this.salle_ouverte = salle_ouverte; }

    public boolean getSalle_ouverte(){
        return this.salle_ouverte;
    }
}