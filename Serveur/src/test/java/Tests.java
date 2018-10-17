import DataIn.Identifiants;
import DataIn.InitPartie;
import DataIn.Sauvegarder;
import DataOut.*;
import Listener.*;
import com.corundumstudio.socketio.*;
import com.corundumstudio.socketio.protocol.Packet;
import main.Joueur;
import main.Serveur;
import org.junit.Test;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

import java.io.File;
import java.net.SocketAddress;
import java.util.*;

import static Listener.MeilleursScores.maxIndex;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;
import static org.junit.Assert.assertEquals;

public class Tests {

    @Test
    public void test_fonction_login() {
        assertEquals(Connexion.login("test","test"), true);
    }

    @Test
    public void test_fonction_existe_utilisateur(){
        assertEquals(Inscription.existeUtilisateur("test"), true);
        assertEquals(Inscription.existeUtilisateur("????"), false);
    }

    @Test
    public void test_fonction_pseudo_valide(){
        assertEquals(Inscription.pseudoValide("test"), true);
        assertEquals(Inscription.pseudoValide("????"), false);
        assertEquals(Inscription.pseudoValide(""), false);
        assertEquals(Inscription.pseudoValide("aa"), false);
    }

    @Test
    public void test_maxIndex(){
        ArrayList<Integer> maListe = new ArrayList<Integer>();
        maListe.add(0,1);
        maListe.add(1,0);
        maListe.add(2,6);
        maListe.add(3,4);
        assertEquals(maxIndex(maListe), 2);
    }



    @Test
    public void test_est_dans_la_salle(){
        Map<String, Joueur> joueurs = new LinkedHashMap<String, Joueur>();
        joueurs.put("123456", new Joueur("test",0));
        SocketAddress mAdress = mock(SocketAddress.class);
        SocketIOClient mClient = mock(SocketIOClient.class);
        when(mAdress.toString()).thenReturn("123456");
        when(mClient.getRemoteAddress()).thenReturn(mAdress);
        assertEquals(ConnSalle.estDansLaSalle(mClient, joueurs), true);
    }


    /* Listener */

    /* Connexion.class */
    @Test
    public void test_listener_Connexion_reussie(){
        Connexion monListener = new Connexion();
        SocketIOClient monClient = mock(SocketIOClient.class);
        SocketAddress mAdress = mock(SocketAddress.class);
        when(mAdress.toString()).thenReturn("123456");
        when(monClient.getRemoteAddress()).thenReturn(mAdress);
        AckRequest ack = mock(AckRequest.class);
        monListener.onData(monClient, new Identifiants("test", "test"), ack);
        LoginReply result = (LoginReply) monListener.getData();
        assertEquals(result.getErrcode(), 0);
    }

    @Test
    public void test_listener_Connexion_mauvaisID(){
        Connexion monListener = new Connexion();
        SocketIOClient monClient = mock(SocketIOClient.class);
        SocketAddress mAdress = mock(SocketAddress.class);
        when(mAdress.toString()).thenReturn("123456");
        when(monClient.getRemoteAddress()).thenReturn(mAdress);
        AckRequest ack = mock(AckRequest.class);
        monListener.onData(monClient, new Identifiants("test", "fail"), ack);
        LoginReply result = (LoginReply) monListener.getData();
        assertEquals(result.getErrcode(), 1);
    }

    @Test
    public void test_listener_Connexion_dejaCo(){
        Map<String, String> connexions = new LinkedHashMap<String, String>();
        connexions.put("123456", "test");
        Connexion monListener = new Connexion();
        monListener.setConnexions(connexions);
        SocketIOClient monClient = mock(SocketIOClient.class);
        SocketAddress mAdress = mock(SocketAddress.class);
        when(mAdress.toString()).thenReturn("123456");
        when(monClient.getRemoteAddress()).thenReturn(mAdress);
        AckRequest ack = mock(AckRequest.class);
        monListener.onData(monClient, new Identifiants("test", "test"), ack);
        LoginReply result = (LoginReply) monListener.getData();
        assertEquals(result.getErrcode(), 2);
    }

    /* ConnSalle.class */
    @Test
    public void test_listener_ConnSalle(){
        Map<String, String> connexions = new LinkedHashMap<String, String>();
        connexions.put("123456", "test");
        ConnSalle monListener = new ConnSalle();
        monListener.setConenxions(connexions);

        SocketIOServer monServer = mock(SocketIOServer.class);
        when(monServer.getAllClients()).thenReturn(new ArrayList<SocketIOClient>());
        monListener.setServer(monServer);

        SocketIOClient monClient = mock(SocketIOClient.class);
        SocketAddress mAdress = mock(SocketAddress.class);
        when(mAdress.toString()).thenReturn("123456");
        when(monClient.getRemoteAddress()).thenReturn(mAdress);
        AckRequest ack = mock(AckRequest.class);

        monListener.onData(monClient, new Object(), ack);

        PseudoReply result = (PseudoReply) monListener.getData();
        assertEquals(result.getPseudo(), "test");
    }

    /* Deconnexion.class */
    @Test
    public void test_listener_Deconnexion(){
        Map<String, String> connexions = new LinkedHashMap<String, String>();
        connexions.put("123456", "test");

        Map<String, Joueur> joueurs = new LinkedHashMap<String, Joueur>();
        joueurs.put("123456", new Joueur("test",0));

        assertEquals(connexions.size(), 1);
        assertEquals(joueurs.size(), 1);


        Deconnexion monListener = new Deconnexion();
        monListener.setConnexions(connexions);
        monListener.setJoueurs(joueurs);

        SocketIOClient monClient = mock(SocketIOClient.class);
        SocketAddress mAdress = mock(SocketAddress.class);
        when(mAdress.toString()).thenReturn("123456");
        when(monClient.getRemoteAddress()).thenReturn(mAdress);
        AckRequest ack = mock(AckRequest.class);

        monListener.onData(monClient, new Object(), ack);
        assertEquals(monListener.getConnexions().size(), 0);
        assertEquals(monListener.getJoueurs().size(), 0);

    }

    /* Gagne.class */
    @Test
    public void test_listener_Gagne_pasEncoreVainqueur(){
        Map<String, Joueur> joueurs = new LinkedHashMap<String, Joueur>();
        joueurs.put("123456", new Joueur("test",0));
        joueurs.put("123344", new Joueur("test2",0));

        Serveur monServeur = new Serveur();
        SocketIOServer monSocketIOServer = mock(SocketIOServer.class);
        when(monSocketIOServer.getAllClients()).thenReturn(new ArrayList<SocketIOClient>());
        monServeur.setServeur(monSocketIOServer);
        monServeur.setBut(2);

        Gagne monListener = new Gagne();
        monListener.setJoueurs(joueurs);
        monListener.setServeur(monServeur);

        SocketIOClient monClient = mock(SocketIOClient.class);
        SocketAddress mAdress = mock(SocketAddress.class);
        when(mAdress.toString()).thenReturn("123456");
        when(monClient.getRemoteAddress()).thenReturn(mAdress);
        AckRequest ack = mock(AckRequest.class);

        monListener.onData(monClient, new Object(), ack);

        assertEquals(monListener.getJoueurs().get("123456").getScore(), 1);

    }

    @Test
    public void test_listener_Gagne_vainqueur(){
        Map<String, Joueur> joueurs = new LinkedHashMap<String, Joueur>();
        joueurs.put("123456", new Joueur("test",1));
        joueurs.put("123344", new Joueur("test2",0));

        Serveur monServeur = new Serveur();
        SocketIOServer monSocketIOServer = mock(SocketIOServer.class);
        when(monSocketIOServer.getAllClients()).thenReturn(new ArrayList<SocketIOClient>());
        monServeur.setServeur(monSocketIOServer);
        monServeur.setBut(2);

        Gagne monListener = new Gagne();
        monListener.setJoueurs(joueurs);
        monListener.setServeur(monServeur);

        SocketIOClient monClient = mock(SocketIOClient.class);
        SocketAddress mAdress = mock(SocketAddress.class);
        when(mAdress.toString()).thenReturn("123456");
        when(monClient.getRemoteAddress()).thenReturn(mAdress);
        AckRequest ack = mock(AckRequest.class);

        monListener.onData(monClient, new Object(), ack);
        PseudoReply result = (PseudoReply) monListener.getData();
        assertEquals(result.getCode(), 1);

    }
 /* Maxime super master chez les autre pas chez nous */
 /* On back pressed supermaster */
 /* Score non reçu */

 /* Croix en ligne */
 /* Score IA affichage */

    /* Inscription.class */
    @Test
    public void test_listener_Inscription_pseudoInvalide(){
        Inscription monListener = new Inscription();

        SocketIOClient monClient = mock(SocketIOClient.class);
        SocketAddress mAdress = mock(SocketAddress.class);
        when(mAdress.toString()).thenReturn("123456");
        when(monClient.getRemoteAddress()).thenReturn(mAdress);
        AckRequest ack = mock(AckRequest.class);

        monListener.onData(monClient, new Identifiants("?", "test"), ack);
        LoginReply result = (LoginReply) monListener.getData();
        assertEquals(result.getErrcode(), 3);
    }

    @Test
    public void test_listener_Inscription_pseudoExiste(){
        Inscription monListener = new Inscription();

        SocketIOClient monClient = mock(SocketIOClient.class);
        SocketAddress mAdress = mock(SocketAddress.class);
        when(mAdress.toString()).thenReturn("123456");
        when(monClient.getRemoteAddress()).thenReturn(mAdress);
        AckRequest ack = mock(AckRequest.class);

        monListener.onData(monClient, new Identifiants("test", "test"), ack);
        LoginReply result = (LoginReply) monListener.getData();
        assertEquals(result.getErrcode(), 1);
    }

    /* JoueursSalle.class */
    @Test
    public void test_listener_JoueursSalle(){
        Map<String, Joueur> joueurs = new LinkedHashMap<String, Joueur>();
        joueurs.put("123456", new Joueur("test",1));
        joueurs.put("123344", new Joueur("test2",0));

        JoueursSalle monListener = new JoueursSalle();
        monListener.setJoueurs(joueurs);

        SocketIOClient monClient = mock(SocketIOClient.class);
        SocketAddress mAdress = mock(SocketAddress.class);
        when(mAdress.toString()).thenReturn("123456");
        when(monClient.getRemoteAddress()).thenReturn(mAdress);
        AckRequest ack = mock(AckRequest.class);

        monListener.onData(monClient, new Object(), ack);
        JoueursSalleReply result = (JoueursSalleReply) monListener.getData();

        assertEquals(result.getPseudos().size(), 2);
        assertEquals(result.getPseudos().get(0), "test");
        assertEquals(result.getPseudos().get(1), "test2");

    }

    /* LancerPartie.class */
    @Test
    public void test_listener_LancerPartie(){
        LancerPartie monListener = new LancerPartie();

        Serveur monServeur = new Serveur();
        SocketIOServer monSocketIOServer = mock(SocketIOServer.class);
        when(monSocketIOServer.getAllClients()).thenReturn(new ArrayList<SocketIOClient>());
        monServeur.setServeur(monSocketIOServer);
        monListener.setServeur(monServeur);

        SocketIOClient monClient = mock(SocketIOClient.class);
        SocketAddress mAdress = mock(SocketAddress.class);
        when(mAdress.toString()).thenReturn("123456");
        when(monClient.getRemoteAddress()).thenReturn(mAdress);
        AckRequest ack = mock(AckRequest.class);

        InitPartie monLoginReply = new InitPartie("point",0,0,"couleurs%");
        monListener.onData(monClient, monLoginReply, ack);
        InitPartie result = (InitPartie) monListener.getData();

        assertEquals(result.getMode(), monLoginReply.getMode());
        assertEquals(result.getUnites(), monLoginReply.getUnites());
        assertEquals(result.getDifficulte(), monLoginReply.getDifficulte());
        assertEquals(result.getListeJeux(), monLoginReply.getListeJeux());
    }

    /* Perdu.class */
    @Test
    public void test_listener_Perdu(){
        Map<String, Joueur> joueurs = new LinkedHashMap<String, Joueur>();
        joueurs.put("123456", new Joueur("test",4));
        joueurs.put("123344", new Joueur("test2",2));

        Serveur monServeur = new Serveur();
        SocketIOServer monSocketIOServer = mock(SocketIOServer.class);
        when(monSocketIOServer.getAllClients()).thenReturn(new ArrayList<SocketIOClient>());
        monServeur.setServeur(monSocketIOServer);
        monServeur.setBut(2);

        Perdu monListener = new Perdu();
        monListener.setJoueurs(joueurs);
        monListener.setServeur(monServeur);

        SocketIOClient monClient = mock(SocketIOClient.class);
        SocketAddress mAdress = mock(SocketAddress.class);
        when(mAdress.toString()).thenReturn("123456");
        when(monClient.getRemoteAddress()).thenReturn(mAdress);
        AckRequest ack = mock(AckRequest.class);

        monListener.onData(monClient, new Object(), ack);
        MeilleursScoresReply result = (MeilleursScoresReply) monListener.getData();

        assertEquals(result.getScore1(), 3);
        assertEquals(result.getScore2(), 2);
        assertEquals(result.getPseudo1(), "test");
        assertEquals(result.getPseudo2(), "test2");
        assertEquals(result.getPseudo3(), "None");

    }

    /* Quitter.class */
    @Test
    public void test_listener_Quitter(){
        Map<String, Joueur> joueurs = new LinkedHashMap<String, Joueur>();
        joueurs.put("123456", new Joueur("test",0));

        assertEquals(joueurs.size(), 1);

        Quitter monListener = new Quitter();
        monListener.setJoueurs(joueurs);

        Serveur monServeur = new Serveur();
        SocketIOServer monSocketIOServer = mock(SocketIOServer.class);
        when(monSocketIOServer.getAllClients()).thenReturn(new ArrayList<SocketIOClient>());
        monServeur.setServeur(monSocketIOServer);
        monListener.setServeur(monServeur);

        SocketIOClient monClient = mock(SocketIOClient.class);
        SocketAddress mAdress = mock(SocketAddress.class);
        when(mAdress.toString()).thenReturn("123456");
        when(monClient.getRemoteAddress()).thenReturn(mAdress);
        AckRequest ack = mock(AckRequest.class);

        monListener.onData(monClient, new Object(), ack);

        assertEquals(monListener.getJoueurs().size(), 0);
        assertEquals(monListener.getServeur().getDisponible(), true);
        assertEquals(monListener.getServeur().getSalle_ouverte(), false);

    }

    /* Times_up.class */
    @Test
    public void test_listener_Times_up_exaequo(){
        Map<String, Joueur> joueurs = new LinkedHashMap<String, Joueur>();
        joueurs.put("123456", new Joueur("test",1));
        joueurs.put("123344", new Joueur("test2",1));

        Serveur monServeur = new Serveur();
        SocketIOServer monSocketIOServer = mock(SocketIOServer.class);
        when(monSocketIOServer.getAllClients()).thenReturn(new ArrayList<SocketIOClient>());
        monServeur.setServeur(monSocketIOServer);

        Times_up monListener = new Times_up();
        monListener.setJoueurs(joueurs);
        monListener.setServeur(monServeur);

        SocketIOClient monClient = mock(SocketIOClient.class);
        SocketAddress mAdress = mock(SocketAddress.class);
        when(mAdress.toString()).thenReturn("123456");
        when(monClient.getRemoteAddress()).thenReturn(mAdress);
        AckRequest ack = mock(AckRequest.class);

        monListener.onData(monClient, new Object(), ack);
        ExaequoReply result = (ExaequoReply) monListener.getData();

        assertEquals(result.getExaequo(), true);
        assertEquals(result.getPseudos().size(), 2);
        assertEquals(result.getPseudos().get(0), "test");
        assertEquals(result.getPseudos().get(1), "test2");
    }

    @Test
    public void test_listener_Times_up_pasExaequo(){
        Map<String, Joueur> joueurs = new LinkedHashMap<String, Joueur>();
        joueurs.put("123456", new Joueur("test",1));
        joueurs.put("123344", new Joueur("test2",0));

        Serveur monServeur = new Serveur();
        SocketIOServer monSocketIOServer = mock(SocketIOServer.class);
        when(monSocketIOServer.getAllClients()).thenReturn(new ArrayList<SocketIOClient>());
        monServeur.setServeur(monSocketIOServer);

        Times_up monListener = new Times_up();
        monListener.setJoueurs(joueurs);
        monListener.setServeur(monServeur);

        SocketIOClient monClient = mock(SocketIOClient.class);
        SocketAddress mAdress = mock(SocketAddress.class);
        when(mAdress.toString()).thenReturn("123456");
        when(monClient.getRemoteAddress()).thenReturn(mAdress);
        AckRequest ack = mock(AckRequest.class);

        monListener.onData(monClient, new Object(), ack);
        ExaequoReply result = (ExaequoReply) monListener.getData();

        assertEquals(result.getExaequo(), false);
        assertEquals(result.getPseudos().size(), 1);
        assertEquals(result.getPseudos().get(0), "test");
    }

    @Test
    public void test_listener_receive_score(){
        Serveur monServer = new Serveur();
        SauvegardeScores sauvegardeScores = new SauvegardeScores();

        SocketIOClient monClient = mock(SocketIOClient.class);
        SocketAddress mAdress = mock(SocketAddress.class);
        when(mAdress.toString()).thenReturn("123456");
        when(monClient.getRemoteAddress()).thenReturn(mAdress);
        AckRequest ack = mock(AckRequest.class);

        sauvegardeScores.onData(monClient, new Sauvegarder("test", "10\n07\n52\n26\n14\n02\n36\n"), ack);
        int[] expected = new int[]{10,7,52,26,14,2,36};
        int[] received = sauvegardeScores.getPlayerScore();

        (new File("records_mini_games_test.txt")).delete();
        (new File("listOfPlayerPseudo.txt")).delete();
        for (int i = 0; i < expected.length ; i ++)
        {
            assertEquals(expected[i], received[i]);
        }

    }

    @Test
    public void test_listener_receive_maxScore(){
        Serveur monServer = new Serveur();
        SauvegardeScores sauvegardeScores = new SauvegardeScores();

        SocketIOClient monClient = mock(SocketIOClient.class);
        SocketAddress mAdress = mock(SocketAddress.class);
        when(mAdress.toString()).thenReturn("123456");
        when(monClient.getRemoteAddress()).thenReturn(mAdress);
        AckRequest ack = mock(AckRequest.class);

        (new SauvegardeScores()).onData(monClient, new Sauvegarder("testA", "10\n07\n52\n26\n14\n02\n37\n"), ack);
        (new SauvegardeScores()).onData(monClient, new Sauvegarder("testB", "09\n07\n00\n47\n10\n01\n37\n"), ack);
        sauvegardeScores.onData(monClient, new Sauvegarder("testC", "02\n08\n51\n32\n10\n00\n37\n"), ack);
        int[] expected = new int[]{10,8,52,47,14,02,37};
        int[] received = sauvegardeScores.getMaxScore();

        (new File("records_mini_games_testA.txt")).delete();
        (new File("records_mini_games_testB.txt")).delete();
        (new File("records_mini_games_testC.txt")).delete();
        (new File("listOfPlayerPseudo.txt")).delete();
        for (int i = 0; i < expected.length ; i ++)
        {
            assertEquals(expected[i], received[i]);
        }

    }

    @Test
    public void test_listener_receive_rang(){
        Serveur monServer = new Serveur();
        SauvegardeScores sauvegardeScores = new SauvegardeScores();

        SocketIOClient monClient = mock(SocketIOClient.class);
        SocketAddress mAdress = mock(SocketAddress.class);
        when(mAdress.toString()).thenReturn("123456");
        when(monClient.getRemoteAddress()).thenReturn(mAdress);
        AckRequest ack = mock(AckRequest.class);

        (new SauvegardeScores()).onData(monClient, new Sauvegarder("testA", "10\n07\n52\n26\n14\n02\n37\n"), ack);
        (new SauvegardeScores()).onData(monClient, new Sauvegarder("testB", "09\n07\n00\n47\n10\n01\n37\n"), ack);
                sauvegardeScores.onData(monClient, new Sauvegarder("testC", "02\n08\n51\n32\n10\n00\n37\n"), ack);
        int[] expected = new int[]{3,1,2,2,2,3,1};
        int[] received = sauvegardeScores.getRang();
        System.out.println(" " + received);
        (new File("records_mini_games_testA.txt")).delete();
        (new File("records_mini_games_testB.txt")).delete();
        (new File("records_mini_games_testC.txt")).delete();
        (new File("listOfPlayerPseudo.txt")).delete();
        for (int i = 0; i < expected.length ; i ++)
        {
            assertEquals(expected[i], received[i]);
        }

    }

}


