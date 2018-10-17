package Listener;

import DataOut.InfoSalleReply;
import DataOut.PseudoReply;
import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.DataListener;
import main.Joueur;
import main.Serveur;

import java.util.Map;

public class ConnSalle implements DataListener {
    Map<String, Joueur> joueurs;
    Map<String, String> conenxions;
    Serveur serveur;
    SocketIOServer server;
    Boolean salle_ouverte;
    Object data = null;

    public ConnSalle() {
        this.serveur = new Serveur();
        this.joueurs = serveur.getJoueurs();
        this.conenxions = serveur.getConnexions();
        this.server = serveur.getServeur();
        this.salle_ouverte = serveur.getSalle_ouverte();
    }

    public static Boolean estDansLaSalle(SocketIOClient client, Map<String, Joueur> joueurs){
        if(joueurs.get(client.getRemoteAddress().toString()) != null) return true;
        return false;
    }

    @Override
    public void onData(SocketIOClient client, Object o, AckRequest ackRequest) {
        System.out.println("Demande de connexion salle reçu.");
        if (joueurs.get(client.getRemoteAddress().toString()) == null) {
            String pseudo = conenxions.get(client.getRemoteAddress().toString());
            Joueur joueur = new Joueur(pseudo, 0);
            PseudoReply reply = new PseudoReply(pseudo, 0);
            this.joueurs.put(client.getRemoteAddress().toString(), joueur);

            if (!serveur.getSalle_ouverte() && serveur.getSuperMaster() == null) {
                serveur.setSuperMaster(pseudo);
                serveur.setSalle_ouverte(true);
            }

            for (SocketIOClient clnt : server.getAllClients())
                if (estDansLaSalle(clnt, joueurs)) clnt.sendEvent("query", reply);

            data = reply;
        }
    }
    public void setConenxions(Map<String, String> conenxions) {
        this.conenxions = conenxions;
    }

    public Object getData() {
        return data;
    }

    public void setServer(SocketIOServer server) {
        this.server = server;
    }
}
