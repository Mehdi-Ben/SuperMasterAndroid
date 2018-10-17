package DataOut;

public class PseudoReply {
    String pseudo;
    int code;

    public PseudoReply() {
    }

    public PseudoReply(String pseudo, int code) {
        this.pseudo = pseudo;
        this.code = code;
    }

    public String getPseudo() {
        return pseudo;
    }

    public void setPseudo(String pseudo) {
        this.pseudo = pseudo;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
