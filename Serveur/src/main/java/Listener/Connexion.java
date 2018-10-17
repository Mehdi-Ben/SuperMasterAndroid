package Listener;

import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.listener.DataListener;
import DataIn.Identifiants;
import DataOut.LoginReply;
import main.*;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.Map;

public class Connexion implements DataListener<Identifiants> {
    Map<String, String> connexions;
    private Object data = null;

    public Connexion() {
        Serveur tmp = new Serveur();
        this.connexions = tmp.getConnexions();
    }

    public static boolean login(String pseudo, String mdp) {
        try {
            FileInputStream file = new FileInputStream("users.txt");
            BufferedReader br = new BufferedReader(new InputStreamReader(file));

            String line;
            String[] parts;

            while ((line = br.readLine()) != null) {
                parts = line.split("[|]");
                if (parts[0].equals(pseudo) && parts[1].equals(mdp)) {
                    br.close();
                    return true;
                }
            }

            br.close();
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public void onData(SocketIOClient client, Identifiants donnes, AckRequest arg2) {
        boolean test = connexions.values().contains(donnes.getPseudo());

        if (login(donnes.getPseudo(), donnes.getMdp()) && !test) {
            System.out.println("" + donnes.getPseudo() + " s'est connecté.");
            connexions.put("" + client.getRemoteAddress(), donnes.getPseudo());
            LoginReply reply = new LoginReply(false, 0);
            client.sendEvent("query", reply);
            data = reply;
        }
        else if (login(donnes.getPseudo(), donnes.getMdp()) && test){
            System.out.println("Utilisateur déjà connecté.");
            LoginReply reply = new LoginReply(false, 2);
            client.sendEvent("query", reply);
            data = reply;
        }
        else {
            System.out.println("Identifiants incorrects.");
            LoginReply reply = new LoginReply(false, 1);
            client.sendEvent("query", reply);
            data = reply;
        }
    }


    public Object getData() {
        return data;
    }

    public void setConnexions(Map<String, String> connexions) {
        this.connexions = connexions;
    }
}
