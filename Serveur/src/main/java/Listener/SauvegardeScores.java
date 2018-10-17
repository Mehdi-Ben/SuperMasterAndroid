package Listener;

import DataIn.InitPartie;
import DataIn.Sauvegarder;
import DataOut.ChargerScore;
import DataOut.LoginReply;
import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.listener.DataListener;
import main.Joueur;
import main.Serveur;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.Map;

import static Listener.ConnSalle.estDansLaSalle;

public class SauvegardeScores implements DataListener<Sauvegarder> {

    public int[] getMaxScore() {
        return maxScore;
    }

    public void setMaxScore(int[] maxScore) {
        this.maxScore = maxScore;
    }

    public int[] getRang() {
        return rang;
    }

    public void setRang(int[] rang) {
        this.rang = rang;
    }

    public int[] getPlayerScore() {
        return playerScore;
    }

    public void setPlayerScore(int[] playerScore) {
        this.playerScore = playerScore;
    }

    private int[] playerScore;
    private int[] maxScore;
    private int[] rang;

    public SauvegardeScores(){
    }


    public void onData(SocketIOClient clnt, Sauvegarder donnes, AckRequest ack) {
        String pseudo = donnes.getPseudo();
        String valeur = donnes.getValue();
        File flist = new File("listOfPlayerPseudo.txt");
        if (!flist.exists())
        {
            try {
                Files.write(Paths.get("listOfPlayerPseudo.txt"), "".getBytes(), StandardOpenOption.CREATE);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        String fileAdresseForPlayer = "records_mini_games_" + pseudo + ".txt";
        File f = new File(fileAdresseForPlayer);
        if (f.exists()) {
            try {
                Files.write(Paths.get(fileAdresseForPlayer), valeur.getBytes(), StandardOpenOption.WRITE);
                System.out.println("" + donnes.getPseudo() + " a envoyé ses scores.");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else
        {
                try {
                Files.write(Paths.get(fileAdresseForPlayer), valeur.getBytes(), StandardOpenOption.CREATE);

                Files.write(Paths.get("listOfPlayerPseudo.txt"), (pseudo+"\n").getBytes(), StandardOpenOption.APPEND);
                System.out.println("" + donnes.getPseudo() + " a creer un nouveau fichier et a envoyé ses scores.");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        playerScore = new int[]{0,0,0,0,0,0,0};
        maxScore = new int[]{0,0,0,0,0,0,0};
        rang = new int[]{1,1,1,1,1,1,1};


        try {
            for (String player : Files.readAllLines(Paths.get("listOfPlayerPseudo.txt")))
            {
                List<String> listofValeur = Files.readAllLines(Paths.get("records_mini_games_" + player + ".txt"));
                for (int index = 0 ; index < maxScore.length ; index++)
                {
                    playerScore[index] = Integer.parseInt(listofValeur.get(index));
                    List<String> listofScorePlayer = Files.readAllLines(Paths.get(fileAdresseForPlayer));
                    // TROUVER LE MEILLEUR SCORE
                    if (maxScore[index] < Integer.parseInt(listofValeur.get(index)))
                    {
                        maxScore[index] = Integer.parseInt(listofValeur.get(index));
                    }

                    // CONNAITRE LE RANG DU JOUEUR
                    if ( Integer.parseInt(listofScorePlayer.get(index)) < Integer.parseInt(listofValeur.get(index)))
                    {
                        rang[index] ++;
                    }
                }
            }
            for (int i = 0; i < rang.length ; i ++)
            {
                System.out.println("MAX : " + maxScore[i] + " RANG :" + rang[i]);
            }

            ChargerScore reply = new ChargerScore(maxScore, rang);
            clnt.sendEvent("query", reply);
            System.out.println("Envoie des meilleurs score et rang");
        } catch (IOException e) {
            e.printStackTrace();
        }





    }

}
