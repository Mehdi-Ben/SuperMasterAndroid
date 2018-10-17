package DataOut;

import java.util.ArrayList;

public class ExaequoReply {
    private ArrayList<String> pseudos;
    private boolean exaequo;

    public ExaequoReply() {
    }

    public ExaequoReply(ArrayList<String> pseudos, boolean exaequo) {
        this.pseudos = pseudos;
        this.exaequo = exaequo;
    }

    public ArrayList<String> getPseudos() {
        return pseudos;
    }

    public void setPseudos(ArrayList<String> pseudos) {
        this.pseudos = pseudos;
    }

    public boolean getExaequo() {
        return exaequo;
    }

    public void setExaequo(boolean exaequo) {
        this.exaequo = exaequo;
    }
}
