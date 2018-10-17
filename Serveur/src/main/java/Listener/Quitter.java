package Listener;

import DataOut.PseudoReply;
import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.listener.DataListener;
import main.Joueur;
import main.Serveur;

import java.util.Map;

import static Listener.ConnSalle.estDansLaSalle;

public class Quitter implements DataListener<Object> {
    Map<String, Joueur> joueurs;
    Serveur serveur;

    public Quitter() {
        this.serveur = new Serveur();
        this.joueurs = this.serveur.getJoueurs();
    }


    public void onData(SocketIOClient client, Object donnes, AckRequest arg2) {
        Joueur joueur = joueurs.get(client.getRemoteAddress().toString());
        System.out.println("" + joueur.getPseudo() + " a quitté la partie.");
        PseudoReply reply = new PseudoReply(joueur.getPseudo(), 2);
        joueurs.remove(client.getRemoteAddress().toString());

        if(joueurs.size() == 0){
            System.out.println("La salle est fermée.");
            serveur.setSuperMaster(null);
            serveur.setBut(0);
            serveur.setDisponible(true);
            serveur.setSalle_ouverte(false);
        }else{
            if (joueur.getPseudo().equals(serveur.getSuperMaster())) { // Si le supermaster quitter la partie
                Map.Entry<String, Joueur> entry = joueurs.entrySet().iterator().next();
                serveur.setSuperMaster(entry.getValue().getPseudo());
                System.out.println("Le nouveau supermaster est " + entry.getValue().getPseudo() + ".");
            }
        }

        for (SocketIOClient clnt : serveur.getServeur().getAllClients())
            if(estDansLaSalle(clnt, joueurs)) clnt.sendEvent("query", reply);
    }

    public void setJoueurs(Map<String, Joueur> joueurs) {
        this.joueurs = joueurs;
    }

    public Map<String, Joueur> getJoueurs() {
        return joueurs;
    }

    public void setServeur(Serveur serveur) {
        this.serveur = serveur;
    }

    public Serveur getServeur() {
        return serveur;
    }

}
