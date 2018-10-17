package Listener;

import DataIn.Supermaster;
import DataOut.PseudoReply;
import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.listener.DataListener;
import main.Joueur;
import main.Serveur;

import java.util.Map;

public class SetSuperMaster implements DataListener<Supermaster> {
    Serveur serveur;


    public SetSuperMaster(){
        this.serveur = new Serveur();
    }

    public void onData(SocketIOClient client, Supermaster donnes, AckRequest ack) throws Exception {
        serveur.setSuperMaster(donnes.getPseudo());
    }

}
