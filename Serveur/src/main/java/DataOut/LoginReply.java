package DataOut;

public class LoginReply {

    private boolean inscription;
    private int errcode;

    public LoginReply(){}

    public LoginReply(boolean inscription, int errcode){
        super();
        this.inscription =  inscription;
        this.errcode = errcode;
    }

    public void setInscription(boolean inscription){
        this.inscription =  inscription;
    }

    public boolean getInscription(){
        return this.inscription;
    }

    public void setErrcode(int errcode){
        this.errcode =  errcode;
    }

    public int getErrcode(){
        return this.errcode;
    }


}