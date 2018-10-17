package Listener;

import DataIn.Identifiants;
import DataOut.LoginReply;
import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.listener.DataListener;
import main.Joueur;
import main.Serveur;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.Map;

public class Deconnexion implements DataListener<Object> {
    Map<String, String> connexions;
    Map<String, Joueur> joueurs;

    public Deconnexion() {
        this.connexions = new Serveur().getConnexions();
        this.joueurs = new Serveur().getJoueurs();
    }

    public void onData(SocketIOClient client, Object donnes, AckRequest arg2){
        System.out.println("" + connexions.get(client.getRemoteAddress().toString()) + " s'est déconnecté.");
        try {
            connexions.remove(client.getRemoteAddress().toString());
            joueurs.remove(client.getRemoteAddress().toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Map<String, String> getConnexions() {
        return connexions;
    }

    public void setConnexions(Map<String, String> connexions) {
        this.connexions = connexions;
    }

    public Map<String, Joueur> getJoueurs() {
        return joueurs;
    }

    public void setJoueurs(Map<String, Joueur> joueurs) {
        this.joueurs = joueurs;
    }
}
