package Listener;

import DataOut.InfoSalleReply;
import DataOut.ScoresReply;
import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.listener.DataListener;
import main.Joueur;
import main.Serveur;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

public class ScoresPartie implements DataListener {
    Serveur serveur;
    Map<String, String> connexions;
    ArrayList<String> scoresEnvoyes;

    public ScoresPartie() {
        this.serveur = new Serveur();
        this.connexions = serveur.getConnexions();
        this.scoresEnvoyes = serveur.getScoresEnvoyes();
    }

    @Override
    public void onData(SocketIOClient client, Object o, AckRequest ackRequest) throws Exception {
        String pseudo = connexions.get(client.getRemoteAddress().toString());
        System.out.println("Demande de scores reçue. (" + pseudo + ").");
        if(!scoresEnvoyes.contains(pseudo)){
            ScoresReply reply = new ScoresReply(serveur.getPseudosPartie(), serveur.getScoresPartie());
            client.sendEvent("query", reply);
            scoresEnvoyes.add(pseudo);
        }
    }
}
