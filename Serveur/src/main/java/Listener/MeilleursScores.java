package Listener;

import DataOut.MeilleursScoresReply;
import DataOut.PseudoReply;
import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.listener.DataListener;
import main.Joueur;
import main.Serveur;

import java.util.ArrayList;
import java.util.Map;

import static Listener.ConnSalle.estDansLaSalle;

public class MeilleursScores implements DataListener<Object> {
    Map<String, Joueur> joueurs;
    Serveur serveur;
    Object data = null;

    public MeilleursScores() {
        this.serveur = new Serveur();
        this.joueurs = this.serveur.getJoueurs();
    }

    public void onData(SocketIOClient clt, Object donnes, AckRequest ack) {
        System.out.println("Demande meilleurs scores.");

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

        try {
            MeilleursScoresReply reply = new MeilleursScoresReply(scores.get(maxIds[0]), scores.get(maxIds[1]), scores.get(maxIds[2]), maxPseudos[0], maxPseudos[1], maxPseudos[2]);
            for (SocketIOClient client : serveur.getServeur().getAllClients())
                if (estDansLaSalle(client, joueurs)) client.sendEvent("query", reply);
    }catch (Exception e){}
    }

    public static int[] idsMax(ArrayList<Integer> scores, int id) {
        ArrayList<Integer> cscores = new ArrayList<>(scores);
        int[] res = new int[3];
        for (int i = 0; i < id; i++) {
            int id_temps = maxIndex(cscores);
            res[i] = id_temps;
            cscores.set(id_temps, -1);
        }
        for (int i = id; i < 3; i++) {
            res[i] = -1;
        }
        return res;
    }

    public static String[] pseudosMax(int[] maxIds, ArrayList<String> pseudos) {
        String res[] = new String[3];
        for (int i = 0; i < 3; i++) {
            if (maxIds[i] == -1) res[i] = "None";
            else res[i] = pseudos.get(maxIds[i]);
        }
        return res;
    }

    public static int maxIndex(ArrayList<Integer> list) {
        Integer i = 0, maxIndex = -1, max = null;
        for (Integer x : list) {
            if ((x != null) && ((max == null) || (x > max))) {
                max = x;
                maxIndex = i;
            }
            i++;
        }
        return maxIndex;
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
