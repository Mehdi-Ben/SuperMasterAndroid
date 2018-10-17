package Listener;

import DataIn.InitPartie;
import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.DataListener;
import main.Joueur;
import main.Serveur;

import java.util.Map;

import static Listener.ConnSalle.estDansLaSalle;

public class LancerPartie implements DataListener<InitPartie> {
    Map<String, Joueur> joueurs;
    Serveur serveur;
    Object data = null;

    public LancerPartie(){
        this.serveur = new Serveur();
        this.joueurs = serveur.getJoueurs();
    }


    public void onData(SocketIOClient clnt, InitPartie donnes, AckRequest ack) {
        System.out.println("Demande de lancement de partie reçu.");
        System.out.println("Partie lancé mode " + donnes.getMode() + " de difficulté " + donnes.getDifficulte() + " avec " + donnes.getUnites() + " unites et jeux " + donnes.getListeJeux());
        serveur.setBut(donnes.getUnites());
        InitPartie reply = new InitPartie(donnes.getMode(), donnes.getUnites(), donnes.getDifficulte(), donnes.getListeJeux());

        for (SocketIOClient client : serveur.getServeur().getAllClients())
            if(estDansLaSalle(client, joueurs)) client.sendEvent("query", reply);

        data = reply;

    }

    public void setServeur(Serveur serveur) {
        this.serveur = serveur;
    }

    public Object getData() {
        return data;
    }
}
