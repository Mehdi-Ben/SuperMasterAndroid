package Listener;

import DataOut.PointsReply;
import DataOut.PseudoReply;
import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.listener.DataListener;
import main.Joueur;
import main.Serveur;

import java.util.Map;

public class SuperMaster implements DataListener<Object> {
    Serveur serveur;
    Map<String, Joueur> joueurs;

    public SuperMaster(){
         this.serveur = new Serveur();
        this.joueurs =  this.serveur.getJoueurs();
    }

    public void onData(SocketIOClient client, Object donnes, AckRequest ack) throws Exception {
        Joueur joueur = joueurs.get(client.getRemoteAddress().toString());
        if(joueur != null) {
            System.out.println("Le supermaster est " + serveur.getSuperMaster() + ". (" + joueur.getPseudo() + ")");
            PseudoReply reply = new PseudoReply(serveur.getSuperMaster(), 3);
            client.sendEvent("query", reply);
        }
    }

}
