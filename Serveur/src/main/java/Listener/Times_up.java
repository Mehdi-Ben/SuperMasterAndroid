package Listener;

import DataOut.ExaequoReply;
import DataOut.PointsReply;
import DataOut.PseudoReply;
import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.listener.DataListener;
import main.Joueur;
import main.Serveur;

import java.util.ArrayList;
import java.util.Map;

import static Listener.ConnSalle.estDansLaSalle;

public class Times_up implements DataListener<Object> {
    Map<String, Joueur> joueurs;
    Serveur serveur;
    Object data;

    public Times_up() {
        this.serveur = new Serveur();
        this.joueurs = this.serveur.getJoueurs();
    }

    public void onData(SocketIOClient clt, Object donnes, AckRequest ack) {
        int max = scoreMax(joueurs);
        ArrayList<String> gagants = gagants(joueurs,max);
        boolean exaequo = false;
        if(gagants.size() > 1) exaequo = true;

        ArrayList<String> pseudos = new ArrayList<String>();
        ArrayList<Integer> scores = new ArrayList<Integer>();
        for (Joueur joueur2 : joueurs.values()) {
            pseudos.add(joueur2.getPseudo());
            scores.add(joueur2.getScore());
        }
        serveur.setPseudosPartie(pseudos);
        serveur.setScoresPartie(scores);

        ExaequoReply reply = new ExaequoReply(gagants,exaequo);
        System.out.println("Times up!");

        for (SocketIOClient client : serveur.getServeur().getAllClients())
            if (estDansLaSalle(client, joueurs)) client.sendEvent("query", reply);

        data = reply;
        serveur.resetPartie();
    }


    private int scoreMax(Map<String, Joueur> joueurs) {
        int max = 0;
        for (Map.Entry<String, Joueur> entry : joueurs.entrySet()) {
            Joueur joueur = entry.getValue();
            if (joueur.getScore() > max) {
                max = joueur.getScore();
            }
        }
        return max;
    }

    private ArrayList<String> gagants(Map<String, Joueur> joueurs, int max) {
        ArrayList<String> pseudos = new ArrayList<>();
        for (Map.Entry<String, Joueur> entry : joueurs.entrySet()) {
            Joueur joueur = entry.getValue();
            if (joueur.getScore() == max) pseudos.add(joueur.getPseudo());
        }
        return pseudos;
    }

    public void setJoueurs(Map<String, Joueur> joueurs) {
        this.joueurs = joueurs;
    }

    public void setServeur(Serveur serveur) {
        this.serveur = serveur;
    }

    public Object getData() {
        return data;
    }
}
