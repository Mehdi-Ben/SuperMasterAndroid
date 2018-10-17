package jtb.supermaster.Serveur;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import org.json.JSONException;
import org.json.JSONObject;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class Sockethor {
    static Socket socket = null;
    static public String pseudo = null;
    static public String mdp = null;
    static public Boolean superMaster = false;
    JSONObject data;

    public Sockethor(){
        if (this.socket == null) {
            try {
                this.socket = IO.socket("http://blackjedi.synology.me:10000");
            } catch (Exception e) {
                e.printStackTrace();
            }
            socket.connect();
        }
    }

    /* Requêtes */

    public void connexion(String pseudo, String mdp) {
        data = new JSONObject();
        try {
            data.put("pseudo", pseudo);
            data.put("mdp", mdp);
        } catch (Exception e) {
            e.printStackTrace();
        }
        socket.emit("login", data);
    }

    public void inscription(String pseudo, String mdp) {
        data = new JSONObject();
        try {
            data.put("pseudo", pseudo);
            data.put("mdp", mdp);
        } catch (Exception e) {
            e.printStackTrace();
        }
        socket.emit("subscription", data);
    }

    public void abonnement(Emitter.Listener fn){
        this.socket.on("query", fn);
    }

    public void desabonnement(){
        this.socket.on("query", null);
    }

    public void testSalle(){
        socket.emit("testSalle");
    }

    public void connSalle(){ socket.emit("rejoindreSalle"); }

    public void joueursSalle(){ socket.emit("joueursSalle"); }

    public void incr_points_serveur(){
        socket.emit("gagne");
    }

    public void decr_points_serveur(){
        socket.emit("perdu");
    }

    public void sauvegarder_scores(String pseudo, String value) {
        data = new JSONObject();
        try {
            data.put("pseudo", pseudo);
            data.put("value", value);

        } catch (Exception e) {
            e.printStackTrace();
        }
        socket.emit("sauvegardeScores", data);

    }

    public void deconnexion(){
        socket.emit("deconnexion");
        this.socket = null;
        this.pseudo = null;
        this.superMaster = false;
        this.data = null;
    }

    public void reset(){
        socket.emit("monReset");
        this.socket = null;
        this.pseudo = null;
        this.superMaster = false;
        this.data = null;
    }

    public void quitter(){
        socket.emit("quitter");
    }

    public void fermer_salle(){
        socket.emit("fermer_salle");
    }

    public void ouvrir_salle(){
        socket.emit("ouvrir_salle");
    }

    public void times_up(){
        socket.emit("times_up");
    }

    public void supermaster(){ socket.emit("supermaster");}

    public void ScoresPartie() { socket.emit("scoresPartie");}

    public void MeilleursScores() { socket.emit("meilleursScores");}

    /* Getters and Setters */

    public void newSupermaster(String pseudo){
        data = new JSONObject();
        try {
            data.put("pseudo", pseudo);
        }catch (Exception e){
            e.printStackTrace();
        }
        socket.emit("setSupermaster", data);
    }

    public Socket getSocket() {
        return this.socket;
    }

    public static String getPseudo() {
        return pseudo;
    }

    public static void setPseudo(String pseudo) {
        Sockethor.pseudo = pseudo;
    }

    public static String getMdp() {
        return mdp;
    }

    public static void setMdp(String mdp) {
        Sockethor.mdp = mdp;
    }

    public static Boolean getSuperMaster() {
        return superMaster;
    }

    public static void setSuperMaster(Boolean superMaster) { Sockethor.superMaster = superMaster; }
}
