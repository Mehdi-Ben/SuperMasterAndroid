package Listener;

import DataIn.InitPartie;
import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.listener.DataListener;
import main.Joueur;
import main.Serveur;

import java.util.Map;
import java.util.Objects;

public class FermerSalle implements DataListener<Object> {
    Serveur serveur;

    public FermerSalle(){
        this.serveur = new Serveur();
    }

    public void onData(SocketIOClient clnt, Object donnes, AckRequest ack) throws Exception {
        System.out.println("Fermeture de la salle.");
        serveur.setDisponible(false);
    }

}
