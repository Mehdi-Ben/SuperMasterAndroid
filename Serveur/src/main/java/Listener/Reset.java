package Listener;

import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.listener.DataListener;
import main.Joueur;
import main.Serveur;

import java.util.Map;

public class Reset implements DataListener<Object> {
    Serveur serveur;
    Map<String, String> connexions;
    Map<String, Joueur> joueurs;

    public Reset() {
        this.serveur = new Serveur();
    }

    public void onData(SocketIOClient client, Object donnes, AckRequest arg2) throws Exception {
        final String ANSI_CLS = "\u001b[2J";
        final String ANSI_HOME = "\u001b[H";
        System.out.print(ANSI_CLS + ANSI_HOME);
        System.out.flush();
        System.out.println("**** SERVEUR RESET ****");
        serveur.monReset();
    }
}
