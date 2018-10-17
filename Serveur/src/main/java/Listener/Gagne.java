package Listener;

import DataOut.MeilleursScoresReply;
import DataOut.PseudoReply;
import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.listener.DataListener;
import main.Joueur;
import main.*;
import java.util.ArrayList;
import java.util.Map;

import static Listener.ConnSalle.estDansLaSalle;
import static Listener.MeilleursScores.idsMax;
import static Listener.MeilleursScores.pseudosMax;

public class Gagne  implements DataListener<Object> {
    Map<String, Joueur> joueurs;
    Serveur serveur;
    Object data = null;

    public Gagne(){
        this.serveur = new Serveur();
        this.joueurs =  this.serveur.getJoueurs();
    }

    public void onData(SocketIOClient clt, Object donnes, AckRequest ack) {
        Joueur joueur = joueurs.get(clt.getRemoteAddress().toString());
        int nouveauScore = joueur.getScore() + 1;
        joueur.setScore(nouveauScore);
        System.out.println(joueur.getPseudo() + " a un score de " + joueur.getScore());

        ArrayList<String> pseudos = new ArrayList<String>();
        ArrayList<Integer> scores = new ArrayList<Integer>();
        for (Joueur joueur2 : joueurs.values()) {
            pseudos.add(joueur2.getPseudo());
            scores.add(joueur2.getScore());
        }

        if (nouveauScore == serveur.getBut()) {
            System.out.println(joueur.getPseudo() + " a gagné!");

            serveur.setPseudosPartie(pseudos);
            serveur.setScoresPartie(scores);
            PseudoReply reply = new PseudoReply(joueur.getPseudo(), 1);

            for (SocketIOClient client : serveur.getServeur().getAllClients()) {
                if (estDansLaSalle(client, joueurs)) {
                    System.out.println("" + joueur.getPseudo() + " Info gagant envoyé -> " + joueurs.size());
                    client.sendEvent("query", reply);
                }
            }


            data = reply;
            serveur.resetPartie();
        } else {

            int id = Math.min(pseudos.size(), 3);
            int[] maxIds = idsMax(scores, id);
            String[] maxPseudos = pseudosMax(maxIds, pseudos);

            if (maxIds[0] == -1) maxIds[0] = 0;
            if (maxIds[1] == -1) maxIds[1] = 0;
            if (maxIds[2] == -1) maxIds[2] = 0;

            try {
                MeilleursScoresReply reply = new MeilleursScoresReply(scores.get(maxIds[0]), scores.get(maxIds[1]), scores.get(maxIds[2]), maxPseudos[0], maxPseudos[1], maxPseudos[2]);
                for (SocketIOClient client : serveur.getServeur().getAllClients())
                    if (estDansLaSalle(client, joueurs)) client.sendEvent("query", reply);
            } catch (Exception e) {
            }
        }
    }


    public void setJoueurs(Map<String, Joueur> joueurs) {
        this.joueurs = joueurs;
    }

    public void setServeur(Serveur serveur) {
        this.serveur = serveur;
    }

    public Map<String, Joueur> getJoueurs() {
        return joueurs;
    }

    public Object getData() {
        return data;
    }
}
