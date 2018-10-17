package Listener;

import DataOut.MeilleursScoresReply;
import DataOut.PointsReply;
import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.listener.DataListener;
import main.Joueur;
import main.Serveur;

import java.util.ArrayList;
import java.util.Map;

import static Listener.MeilleursScores.idsMax;
import static Listener.MeilleursScores.pseudosMax;

public class Perdu implements DataListener<Object> {
    Map<String, Joueur> joueurs;
    Serveur serveur;
    Object data = null;

    public Perdu(){
        this.serveur = new Serveur();
        this.joueurs = this.serveur.getJoueurs();
    }

    private Boolean estDansLaSalle(SocketIOClient client){
        if(this.joueurs.get(client.getRemoteAddress().toString()) != null) return true;
        return false;
    }


    public void onData(SocketIOClient client, Object donnes, AckRequest ack) {
        Joueur joueur = joueurs.get(client.getRemoteAddress().toString());
        if(joueur != null) {
            int nouveauScore = joueur.getScore() - 1;
            joueur.setScore(nouveauScore);
            System.out.println(joueur.getPseudo() + " a un score de " + joueur.getScore());

            ArrayList<String> pseudos = new ArrayList<String>();
            ArrayList<Integer> scores = new ArrayList<Integer>();
            for (Joueur joueur2 : joueurs.values()) {
                pseudos.add(joueur2.getPseudo());
                scores.add(joueur2.getScore());
            }

            int id = Math.min(pseudos.size(), 3);
            int[] maxIds = idsMax(scores, id);
            String[] maxPseudos = pseudosMax(maxIds, pseudos);

            if (maxIds[0] == -1) maxIds[0] = 0;
            if (maxIds[1] == -1) maxIds[1] = 0;
            if (maxIds[2] == -1) maxIds[2] = 0;

            MeilleursScoresReply reply = new MeilleursScoresReply(scores.get(maxIds[0]), scores.get(maxIds[1]), scores.get(maxIds[2]), maxPseudos[0], maxPseudos[1], maxPseudos[2]);

            for (SocketIOClient clnt : serveur.getServeur().getAllClients())
                if (estDansLaSalle(clnt)) clnt.sendEvent("query", reply);

            data = reply;
        }
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
