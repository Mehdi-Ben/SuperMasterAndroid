package jtb.supermaster.Menus;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.Vibrator;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

import io.socket.emitter.Emitter;
import jtb.supermaster.Menus.Menu_libre;
import jtb.supermaster.Menus.Score;
import jtb.supermaster.Mini_Jeux.Mini_jeu;
import jtb.supermaster.Mini_Jeux.Sons;
import jtb.supermaster.Modes.Mode_contreLaMontre;
import jtb.supermaster.Modes.Mode_classique;
import jtb.supermaster.Modes.Mode_courseAuxPoints;
import jtb.supermaster.Modes.Mode_multijoueurs;
import jtb.supermaster.R;
import jtb.supermaster.Serveur.Connexion;
import jtb.supermaster.Serveur.Sockethor;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

public class Menu_principal extends Activity implements Emitter.Listener{

    //------- Variables Globales


    private TextView modeLibre;
    private TextView modeClassique;
    private TextView meilleursScores;
    private TextView modeMulti;
    private TextView textCo;
    private Sockethor mSocket;
    private AlertDialog dialog;
    private Sons sonBoxeur;
    private static boolean firstArrivee = true;
    ImageView connectImageView;
    public Sons sonBulle;
    Vibrator vb;
    private boolean dejaJoue = false;

    //------- OnCreate


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_principal);

        //---- Vibrations, sons et TextView sons menu principal


        vb = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        sonBulle = new Sons(R.raw.bulle, this, "son");
        sonBoxeur = new Sons(R.raw.boxeur, this, "music");

        if (firstArrivee){
            sonBoxeur.getListeSons().add(sonBoxeur);
            firstArrivee = false;
        }

        ImageView vp = (ImageView) findViewById(R.id.parametres);
        vp.setTag(R.drawable.parametre);
        ImageView vib = (ImageView) findViewById(R.id.vibrations);
        vib.setTag(R.drawable.vibration);
        connectImageView = (ImageView) findViewById(R.id.logoConnection);
        try{
            dejaJoue = sonBoxeur.getListeSons().get(0).getSon().isPlaying();}catch(Exception e){}
        if (sonBoxeur.sonMusicOn && !dejaJoue) {
            sonBoxeur.getListeSons().get(0).play();
            ImageView v = (ImageView) findViewById(R.id.sonMusique);
            v.setTag(R.drawable.son_musique);
        }
        else if(sonBoxeur.sonMusicOn && dejaJoue){
            ImageView v = (ImageView) findViewById(R.id.sonMusique);
            v.setTag(R.drawable.son_musique);
        }
        else{
            ImageView v = (ImageView) findViewById(R.id.sonMusique);
            v.setImageResource(R.drawable.son_off_musique);
            v.setTag(R.drawable.son_off_musique);
        }
        if (sonBulle.sonOn) {
            ImageView v = (ImageView) findViewById(R.id.son);
            v.setTag(R.drawable.son2);
        }
        else{
            ImageView v = (ImageView) findViewById(R.id.son);
            v.setImageResource(R.drawable.son_fin);
            v.setTag(R.drawable.son_fin);
        }
        mSocket = new Sockethor();
    }

    //---- OnClick autres menus


    public void onClickLibre(View v) {
        Intent activity = new Intent(this, Menu_libre.class);
        sonBulle.play();
        startActivity(activity);
    }

    public void onClickClassique(View v) {
        sonBoxeur.getListeSons().get(0).stop();
        Intent activity = new Intent(this, Mode_classique.class);
        sonBulle.play();
        startActivity(activity);
    }

    public void onClickParametres(View v){
        if ((Integer)(findViewById(R.id.parametres)).getTag() == getResources().getIdentifier("parametre", "drawable", getPackageName())){
            ((ImageView) (findViewById(R.id.parametres))).setImageResource(R.drawable.parametres_open);
            (findViewById(R.id.parametres)).setTag(R.drawable.parametres_open);
            (findViewById(R.id.sonMusique)).setVisibility(VISIBLE);
            (findViewById(R.id.son)).setVisibility(VISIBLE);
            (findViewById(R.id.vibrations)).setVisibility(VISIBLE);

        }
        else{
            ((ImageView) (findViewById(R.id.parametres))).setImageResource(R.drawable.parametre);
            (findViewById(R.id.parametres)).setTag(R.drawable.parametre);
            (findViewById(R.id.sonMusique)).setVisibility(INVISIBLE);
            (findViewById(R.id.son)).setVisibility(INVISIBLE);
            (findViewById(R.id.vibrations)).setVisibility(INVISIBLE);
        }
    }


    public void onClickMultijoueur(View v) {
        Intent activity = new Intent(this, Menu_Choix_Multi.class);
        sonBulle.play();
        startActivity(activity);
    }

    public void onClickScore(View v) {
        sonBoxeur.getListeSons().get(0).stop();
        Intent activity = new Intent(this, Score.class);
        sonBulle.play();
        startActivity(activity);
    }

    public void onClickRules(View v) {
        sonBoxeur.getListeSons().get(0).stop();
        Intent activity = new Intent(this, Regles.class);
        sonBulle.play();
        startActivity(activity);
    }


    //---- OnClick options sonores

    public void onClickVibrations(View v){
        if ((Integer)v.getTag() == getResources().getIdentifier("vibration", "drawable", getPackageName())){
            ((ImageView) v).setImageResource(R.drawable.son_off_musique);
            v.setTag(R.drawable.son_off_musique);
            sonBoxeur.vibrationsOn = false;
        }
        else{
            ((ImageView) v).setImageResource(R.drawable.vibration);
            v.setTag(R.drawable.vibration);
            sonBoxeur.vibrationsOn = true;
            vb.vibrate(100);

        }
    }

    public void onClickMusic(View v){
        if ((Integer)v.getTag() == getResources().getIdentifier("son_musique", "drawable", getPackageName())){
            ((ImageView) v).setImageResource(R.drawable.son_off_musique);
            v.setTag(R.drawable.son_off_musique);
            try{sonBoxeur.getListeSons().get(0).stop();}catch(Exception e){}
            sonBoxeur.sonMusicOn = false;
        }
        else{
            ((ImageView) v).setImageResource(R.drawable.son_musique);
            v.setTag(R.drawable.son_musique);
            sonBoxeur.sonMusicOn = true;
            try{sonBoxeur.getListeSons().get(0).play();}catch (Exception e){}
        }
    }

    public void onClickSound(View v){
        if ((Integer)v.getTag() == getResources().getIdentifier("son2", "drawable", getPackageName())){
            ((ImageView) v).setImageResource(R.drawable.son_fin);
            v.setTag(R.drawable.son_fin);
            sonBulle.sonOn = false;
        }
        else{
            ((ImageView) v).setImageResource(R.drawable.son2);
            v.setTag(R.drawable.son2);
            sonBulle.sonOn = true;
            try{sonBulle.play();}catch (Exception e){}
        }


    }

    //---- OnClick sur connexion serveur

    public void onClickConnect(View v) {
        try{sonBoxeur.getListeSons().get(0).stop();}catch (Exception e){}
        if (mSocket.getPseudo() == null) {
            Intent activity = new Intent(this, Connexion.class);
            try{sonBulle.play();}catch(Exception e){}
            startActivity(activity);
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Vous êtes connecté en tant que " + mSocket.getPseudo() + ", voulez-vous vous vraiment vous déconnecter?").setTitle("Déconnexion");
            builder.setPositiveButton("Oui", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    mSocket.deconnexion();
                    connectImageView.setImageResource(R.drawable.reseau_disconnect);
                }
            });
            builder.setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    // User cancelled the dialog
                }
            });

            dialog = builder.create();
            dialog.show();
        }
    }


    //----------


    @Override
    public void call(Object... args) {
        JSONObject data = (JSONObject) args[0];
    }

    @Override
    public void onResume()
    {
        for (int i = 0; i < sonBoxeur.getListeSons().size(); i++)
            System.out.println("DVSC SFD : " + sonBoxeur.getListeSons().get(i).getSon().isPlaying());
        try{
            if (sonBoxeur.getListeSons().get(0)!=null && (!sonBoxeur.getListeSons().get(0).getSon().isPlaying()))
                sonBoxeur.getListeSons().get(0).play();}catch(Exception e){}
        super.onResume();
        if (mSocket.getPseudo() == null)
        {
            connectImageView.setImageResource(R.drawable.reseau_disconnect);
        }
        else
        {
            connectImageView.setImageResource(R.drawable.reseau_connect);
        }

    }

    @Override
    public void onPause()
    {
        super.onPause();
        PowerManager mPowerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);

        if (!mPowerManager.isScreenOn())
            if (sonBoxeur!= null && sonBoxeur.getListeSons().get(0).getSon().isPlaying())
                try{sonBoxeur.getListeSons().get(0).stop();}catch (Exception e){}
    }
}
