package Listener;

import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.listener.DataListener;
import DataIn.Identifiants;
import DataOut.LoginReply;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;


public class Inscription implements DataListener<Identifiants> {
    Object data = null;

    public static boolean existeUtilisateur(String pseudo){
        try {
            FileInputStream file = new FileInputStream("users.txt");

            BufferedReader br = new BufferedReader(new InputStreamReader(file));

            String line;
            String[] parts;

            while ((line = br.readLine()) != null){
               parts = line.split("[|]");
               if(parts[0].equals(pseudo)){
                   br.close();
                   return true;
               }
            }

            br.close();
            return false;
        }
        catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public static boolean pseudoValide(String pseudo){
        if(pseudo.length() < 3 || !pseudo.matches("[A-Za-z0-9]+"))
            return false;
        return true;
    }

    public void onData(SocketIOClient client, Identifiants donnes, AckRequest ack) {
        if(!pseudoValide(donnes.getPseudo())){
            System.out.println("Le pseudo est invalide.");
            LoginReply reply = new LoginReply(true, 3);
            client.sendEvent("query", reply);
            data = reply;
        }
        else if(!existeUtilisateur(donnes.getPseudo())) {
            String utilisateur = donnes.getPseudo() + "|" + donnes.getMdp() + System.getProperty("line.separator");
            try {
                Files.write(Paths.get("users.txt"), utilisateur.getBytes(), StandardOpenOption.APPEND);
                System.out.println("" + donnes.getPseudo() + " s'est inscrit.");
                LoginReply reply = new LoginReply(true, 0);
                client.sendEvent("query", reply);
                data = reply;
            } catch (IOException e) {
                System.out.println("Erreur lors de l'inscription de " + donnes.getPseudo());
                LoginReply reply = new LoginReply(true, 2);
                client.sendEvent("query", reply);
                data = reply;
            }
        }
        else{
            System.out.println("L'uilisateur existe déjà dans la base de donnée.");
            LoginReply reply = new LoginReply(true, 1);
            client.sendEvent("query", reply);
            data = reply;
        }
    }

    public Object getData() {
        return data;
    }
}
