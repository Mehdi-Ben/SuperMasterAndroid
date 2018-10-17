package Listener;

import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.listener.DataListener;
import main.Serveur;

public class OuvrirSalle implements DataListener<Object> {
    Serveur serveur;

    public OuvrirSalle(){
        this.serveur = new Serveur();
    }

    public void onData(SocketIOClient clnt, Object donnes, AckRequest ack) throws Exception {
        System.out.println("Ouverture de la salle.");
        serveur.setDisponible(true);
    }

}
