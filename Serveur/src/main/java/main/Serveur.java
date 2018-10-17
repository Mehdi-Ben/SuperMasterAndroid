package main;

import Listener.Connexion;
import Listener.Inscription;
import Listener.InfoSalle;
import com.corundumstudio.socketio.*;
import DataIn.*;
import Listener.*;
import com.corundumstudio.socketio.listener.ConnectListener;

import java.net.Socket;
import java.util.*;
import java.util.concurrent.TimeUnit;


public class Serveur {
    static Map<String, String> connexions = new LinkedHashMap<String, String>(); // Liste des pseudo/adresse des joueurs connectés
    static Map<String, Joueur> joueurs = new LinkedHashMap<String, Joueur>(); // Liste des adresses/joueurs dans la salle
    static ArrayList<String> scoresEnvoyes = new ArrayList<String>(); // Liste des adresses/joueurs dans la salle
    static ArrayList<String> pseudosPartie = new ArrayList<String>();
    static ArrayList<Integer> scoresPartie = new ArrayList<Integer>();
    static boolean disponible = true;
    static boolean salle_ouverte = false;
    static int but = 0;
    static String superMaster;

    public static void main(String[] args) {
        Serveur s = new Serveur("192.168.1.46", 10000);
        s.serveur.start();
        try { TimeUnit.SECONDS.sleep(Integer.MAX_VALUE); } catch (Exception e) { e.printStackTrace();}
        s.serveur.stop();
    }

    static SocketIOServer serveur ;

    public Serveur(){}

    public Serveur(String h, int p) {
        Configuration config = new Configuration();
        config.setHostname(h);
        config.setPort(p);
        SocketConfig sockConfig = new SocketConfig();
        sockConfig.setReuseAddress(true);
        config.setSocketConfig(sockConfig);
        serveur = new SocketIOServer(config);
        serveur.addEventListener("subscription",    Identifiants.class, new Inscription()); // Insciption d'un client
        serveur.addEventListener("login",           Identifiants.class, new Connexion());   // Listener.Connexion d'un client
        serveur.addEventListener("deconnexion",     Object.class,       new Deconnexion());   // Listener.Connexion d'un client
        serveur.addEventListener("quitter",         Object.class,       new Quitter());   // Listener.Connexion d'un client
        serveur.addEventListener("monReset",        Object.class,       new Reset());   // Listener.Connexion d'un client
        serveur.addEventListener("testSalle",       Object.class,       new InfoSalle());   // Informe le client de l'état de la salle
        serveur.addEventListener("rejoindreSalle",  Object.class,       new ConnSalle());   // Listener.Connexion d'un client à la salle
        serveur.addEventListener("joueursSalle",    Object.class,       new JoueursSalle());// Listener.Connexion d'un client à la salle
        serveur.addEventListener("gagne",           Object.class,       new Gagne());       // Listener.Connexion d'un client à la salle
        serveur.addEventListener("perdu",           Object.class,       new Perdu());       // Listener.Connexion d'un client à la salle
        serveur.addEventListener("lancerPartie",    InitPartie.class,   new LancerPartie()); // Listener.Connexion d'un client à la salle
        serveur.addEventListener("fermer_salle",    Object.class,       new FermerSalle()); // Listener.Connexion d'un client à la salle
        serveur.addEventListener("ouvrir_salle",    Object.class,       new OuvrirSalle()); // Listener.Connexion d'un client à la salle
        serveur.addEventListener("times_up",        Object.class,       new Times_up()); // Listener.Connexion d'un client à la salle
        serveur.addEventListener("supermaster",     Object.class,       new SuperMaster()); // Listener.Connexion d'un client à la salle
        serveur.addEventListener("setSupermaster",  Supermaster.class,  new SetSuperMaster()); // Listener.Connexion d'un client à la salle
        serveur.addEventListener("scoresPartie",    Object.class,       new ScoresPartie()); // Listener.Connexion d'un client à la salle
        serveur.addEventListener("meilleursScores", Object.class,       new MeilleursScores()); // Listener.Connexion d'un client à la salle
        serveur.addEventListener("sauvegardeScores", Sauvegarder.class, new SauvegardeScores()); // Listener.Connexion d'un client à la salle
    }


    public Map<String, String> getConnexions() { return connexions; }
    public Map<String, Joueur> getJoueurs() { return joueurs; }
    public boolean getDisponible() {
        return disponible;
    }
    public boolean getSalle_ouverte() {
        return salle_ouverte;
    }
    public SocketIOServer getServeur(){ return serveur;}
    public static int getBut() { return but; }
    public static void setBut(int but) { Serveur.but = but; }

    public static void setServeur(SocketIOServer serveur) {
        Serveur.serveur = serveur;
    }

    public static void setDisponible(boolean disponible) {
        Serveur.disponible = disponible;
    }

    public static void setSalle_ouverte(boolean salle_ouverte) {
        Serveur.salle_ouverte = salle_ouverte;
    }

    public static String getSuperMaster() {
        return superMaster;
    }

    public static void setSuperMaster(String superMaster) {
        Serveur.superMaster = superMaster;
    }

    public static ArrayList<String> getPseudosPartie() {
        return pseudosPartie;
    }

    public static void setPseudosPartie(ArrayList<String> pseudosPartie) {
        Serveur.pseudosPartie = pseudosPartie;
    }

    public static ArrayList<Integer> getScoresPartie() {
        return scoresPartie;
    }

    public static void setScoresPartie(ArrayList<Integer> scoresPartie) {
        Serveur.scoresPartie = scoresPartie;
    }

    public static ArrayList<String> getScoresEnvoyes() {
        return scoresEnvoyes;
    }

    public static void setScoresEnvoyes(ArrayList<String> scoresEnvoyes) {
        Serveur.scoresEnvoyes = scoresEnvoyes;
    }

    public void monReset(){
        this.but = 0;
        this.disponible = true;
        this.salle_ouverte = false;
        this.connexions.clear();
        this.joueurs.clear();
        this.superMaster = null;
        this.pseudosPartie = null;
        this.scoresPartie = null;
        this.scoresEnvoyes = null;
    }

    public void resetPartie(){
        this.but = 0;
        this.disponible = true;
        this.salle_ouverte = false;
        this.joueurs.clear();
        this.superMaster = null;
        this.scoresEnvoyes.clear();
    }
}
