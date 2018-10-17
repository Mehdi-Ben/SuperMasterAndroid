package DataOut;

import java.util.ArrayList;

public class JoueursSalleReply {
    private ArrayList<String> pseudos;

    public JoueursSalleReply(){}

    public JoueursSalleReply(ArrayList<String> pseudos){
        super();
        this.pseudos = pseudos;
    }

    public ArrayList<String> getPseudos() {
        return pseudos;
    }

    public void setPseudos(ArrayList<String> pseudos) {
        this.pseudos = pseudos;
    }
}