package Listener;

import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.listener.DataListener;
import DataOut.InfoSalleReply;

import java.util.Map;
import main.*;

public class InfoSalle implements DataListener {
    Serveur serveur;

    public InfoSalle() {
        this.serveur = new Serveur();
    }

    @Override
    public void onData(SocketIOClient client, Object o, AckRequest ackRequest) throws Exception {
        System.out.println("Etat de la salle -> Disponible : " + serveur.getDisponible() + " / Ouverte : " + serveur.getSalle_ouverte());
        InfoSalleReply reply = new InfoSalleReply(serveur.getDisponible(), serveur.getSalle_ouverte());
        client.sendEvent("query", reply);
    }
}
