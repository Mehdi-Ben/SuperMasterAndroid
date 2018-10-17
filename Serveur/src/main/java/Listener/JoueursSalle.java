package Listener;

import DataOut.JoueursSalleReply;
import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.DataListener;
import main.Joueur;
import main.Serveur;

import java.util.ArrayList;
import java.util.Map;

public class JoueursSalle implements DataListener {
    Map<String, Joueur> joueurs;
    Serveur serveur;
    Object data = null;

    public JoueursSalle() {
        this.serveur = new Serveur();
        this.joueurs = serveur.getJoueurs();
    }

    @Override
    public void onData(SocketIOClient client, Object o, AckRequest ackRequest) {
        System.out.println("Demande des joueurs reçu.");
            ArrayList<String> pseudos = new ArrayList<String>();
            for (Joueur joueur2 : joueurs.values())
                pseudos.add(joueur2.getPseudo());
            JoueursSalleReply reply = new JoueursSalleReply(pseudos);
            client.sendEvent("query", reply);
            data = reply;
    }

    public void setJoueurs(Map<String, Joueur> joueurs) {
        this.joueurs = joueurs;
    }

    public Object getData() {
        return data;
    }
}
