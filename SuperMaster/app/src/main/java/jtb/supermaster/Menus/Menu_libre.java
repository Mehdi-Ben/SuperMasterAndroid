package jtb.supermaster.Menus;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.PowerManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.content.Intent;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import jtb.supermaster.Mini_Jeux.ArrowGame;
import jtb.supermaster.Mini_Jeux.Basket;
import jtb.supermaster.Mini_Jeux.Couleur_a_trouver;
import jtb.supermaster.Mini_Jeux.Enigmes_a_resoudre;
import jtb.supermaster.Mini_Jeux.Jeu_pustules;
import jtb.supermaster.Mini_Jeux.Mini_jeu;
import jtb.supermaster.Mini_Jeux.Point_connection;
import jtb.supermaster.Mini_Jeux.Sons;
import jtb.supermaster.R;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;




import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.concurrent.ExecutionException;

public class Menu_libre extends Activity //implements View.OnClickListener
{




    //---------- Variables globales


    public final static String SCORE = "Score";
    public final static int ENIGMES = 0;
    public final static int FLECHES = 1;
    public final static int COULEURS = 2;
    public final static int POINTS = 3;
    public final static int DEMINEUR = 4;
    public final static int BASKET = 5;

    private LinearLayout layoutLibre;
    private RelativeLayout layoutFinPartie;
    private Class jeuEnCours;
    private int game;
    private TextView textscore;

    private TextView fleches;
    private TextView enigmes;
    private TextView couleurs;
    private TextView points;
    private TextView basket;
    private TextView demineur;

    private int couleurTexte;
    private int tailleTexte;
    private int nouveauScore;

    public Sons sonBulle;
    public Sons sonHighScore;
    public Sons sonGameOver;


    //---------- OnCreate


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_libre);


        //---- Sons

        sonBulle = new Sons(R.raw.bulle, this, "son");
        sonGameOver = new Sons(R.raw.gameover,  getApplicationContext(), "music");
        sonHighScore = new Sons(R.raw.high_score, getApplicationContext(), "son");

        //---- Layout

        layoutLibre = (LinearLayout) findViewById(R.id.LayooutLibre);
        layoutFinPartie = (RelativeLayout) findViewById(R.id.layoutPrincFinPartie);
        textscore = (TextView) findViewById(R.id.Textpoint);

        //---- TextView


        fleches = (TextView) findViewById(R.id.FlechesTexte);
        enigmes = (TextView) findViewById(R.id.EnigmesTexte);
        couleurs = (TextView) findViewById(R.id.CouleursTexte);
        points = (TextView) findViewById(R.id.EtoilesTexte);
        basket = (TextView) findViewById(R.id.BasketTexte);
        demineur = (TextView) findViewById(R.id.DemineurTexte);

        //---- Modifs polices


        couleurTexte = 0xFF1a237e;
        tailleTexte = 25;
        Typeface custom_font1 = Typeface.createFromAsset(getAssets(), "fonts/helsinki.ttf");

        ((TextView) findViewById(R.id.TextScore)).setTypeface(custom_font1);
        ((TextView) findViewById(R.id.Textpoint)).setTypeface(custom_font1);
        ((TextView) findViewById(R.id.TextReplay)).setTypeface(custom_font1);

        ((TextView) findViewById(R.id.TextScore)).setTextSize(tailleTexte);
        ((TextView) findViewById(R.id.Textpoint)).setTextSize(tailleTexte);
        ((TextView) findViewById(R.id.TextReplay)).setTextSize(tailleTexte);

        ((TextView) findViewById(R.id.TextScore)).setTextColor(couleurTexte);
        ((TextView) findViewById(R.id.Textpoint)).setTextColor(couleurTexte);
        ((TextView) findViewById(R.id.TextReplay)).setTextColor(couleurTexte);

        fleches.setTypeface(custom_font1);
        fleches.setTextColor(couleurTexte);
        fleches.setTextSize(tailleTexte);

        enigmes.setTypeface(custom_font1);
        enigmes.setTextColor(couleurTexte);
        enigmes.setTextSize(tailleTexte);

        couleurs.setTypeface(custom_font1);
        couleurs.setTextColor(couleurTexte);
        couleurs.setTextSize(tailleTexte);

        basket.setTypeface(custom_font1);
        basket.setTextColor(couleurTexte);
        basket.setTextSize(tailleTexte);

        points.setTypeface(custom_font1);
        points.setTextColor(couleurTexte);
        points.setTextSize(tailleTexte);

        demineur.setTypeface(custom_font1);
        demineur.setTextColor(couleurTexte);
        demineur.setTextSize(tailleTexte);
    }

    //---- On Click Jeux



    public void onClickEnigme(View v) {
        try{sonGameOver.getListeSons().get(0).stop();}catch(Exception e){}
        Intent activity = new Intent(Menu_libre.this, Enigmes_a_resoudre.class);
        try{sonBulle.play();}catch(Exception e){}
        startActivityForResult(activity, ENIGMES);
        jeuEnCours = Enigmes_a_resoudre.class;
        game = ENIGMES;
    }

    public void onClickFleche(View v) {
        try{sonGameOver.getListeSons().get(0).stop();}catch(Exception e){}
        Intent activity = new Intent(this, ArrowGame.class);
        try{sonBulle.play();}catch(Exception e){}
        startActivityForResult(activity, FLECHES);
        jeuEnCours = ArrowGame.class;
        game = FLECHES;
    }


    public void onClickCouleur(View v) {
        try{sonGameOver.getListeSons().get(0).stop();}catch(Exception e){}
        Intent activity = new Intent(this, Couleur_a_trouver.class);
        try{sonBulle.play();}catch(Exception e){}
        startActivityForResult(activity, COULEURS);
        jeuEnCours = Couleur_a_trouver.class;
        game = COULEURS;
    }


    public void onClickPoint(View v) {
        try{sonGameOver.getListeSons().get(0).stop();}catch(Exception e){}
        Intent activity = new Intent(this, Point_connection.class);
        try{sonBulle.play();}catch(Exception e){}
        startActivityForResult(activity, POINTS);
        jeuEnCours = Point_connection.class;
        game = POINTS;
    }


    public void onClickDemineur(View v) {
        try{sonGameOver.getListeSons().get(0).stop();}catch(Exception e){}
        Intent activity = new Intent(this, Jeu_pustules.class);
        try{sonBulle.play();}catch(Exception e){}
        startActivityForResult(activity, DEMINEUR);
        jeuEnCours = Jeu_pustules.class;
        game = DEMINEUR;
    }

    public void onClickBasket(View v) {
        try{sonGameOver.getListeSons().get(0).stop();}catch(Exception e){}
        Intent activity = new Intent(this, Basket.class);
        try{sonBulle.play();}catch(Exception e){}
        startActivityForResult(activity, BASKET);
        jeuEnCours = Basket.class;
        game = BASKET;
    }


    public void onClickMenuPrincipal(View v)
    {
        Intent i = new Intent(this, Menu_principal.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        try{sonBulle.play();}catch(Exception e){}
        startActivity(i);
    }

    public void onClickQuit(View v){
        Intent activity = new Intent(this, Menu_libre.class);
        activity.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        try{sonBulle.play();}catch(Exception e){}
        startActivity(activity);
    }

    public void onClickReplay(View v){
        Intent activity = new Intent(this, jeuEnCours);
        activity.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        try{sonBulle.play();}catch(Exception e){}
        startActivityForResult(activity, game);
    }

    //-----------

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case ENIGMES: {
                if (resultCode == RESULT_OK) annexe(data, "enigmes.txt");
                break;
            }
            case FLECHES: {
                if (resultCode == RESULT_OK) annexe(data, "fleches.txt");
                break;
            }
            case COULEURS: {
                if (resultCode == RESULT_OK) annexe(data, "couleurs.txt");
                break;
            }
            case POINTS: {
                if (resultCode == RESULT_OK) annexe(data, "points.txt");
                break;
            }
            case DEMINEUR: {
                if (resultCode == RESULT_OK) annexe(data, "demineur.txt");
                break;
            }
            case BASKET: {
                if (resultCode == RESULT_OK) annexe(data, "basket.txt");
                break;
            }
        }
    }

    //-----------



    private void annexe(Intent data, String file) {
        try{
            nouveauScore = Integer.parseInt(data.getStringExtra("SCORE"));
        }catch(Exception e){
            nouveauScore = 0;
        }
        int ancienScore = 0;
        Context context = getApplicationContext();
        try {
            ancienScore = Integer.parseInt(readFromFile(file, context));
        } catch (Exception e) {
            writeToFile("0", file,context);
        }

        if (nouveauScore > ancienScore) {
            writeToFile("" + nouveauScore, file,context);
            try{sonHighScore.play();}catch(Exception e){}

        } else {
            try{sonGameOver.play();}catch(Exception e){}
        }

        data.removeExtra("SCORE");
        layoutFinPartie.setVisibility(View.VISIBLE);
        textscore.setText(nouveauScore + " points");
        layoutLibre.setVisibility(View.GONE);

    }


    //-----------



    public static void writeToFile(String data, String file, Context context) {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput(file, Context.MODE_PRIVATE));
            outputStreamWriter.write(data);
            outputStreamWriter.close();
        } catch (Exception e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }


    //-----------


    public static String readFromFile(String file, Context context) {
        String ret = "";

        try {
            InputStream inputStream = context.openFileInput(file);

            if (inputStream != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ((receiveString = bufferedReader.readLine()) != null) {
                    stringBuilder.append(receiveString);
                }

                inputStream.close();
                ret = stringBuilder.toString();
            }
        } catch (Exception e) {

        }

        return ret;
    }

    //-----------


    @Override
    public void onBackPressed(){
        this.onResume();
        Intent activity = new Intent(this, Menu_principal.class);
        startActivity(activity);
        finish();
        super.onBackPressed();
    }

    //-----------


    @Override
    public void onResume()
    {
        try{
            if (sonGameOver.getListeSons().get(0)!=null && (!sonGameOver.getListeSons().get(0).getSon().isPlaying()))
                sonGameOver.getListeSons().get(0).play();}catch(Exception e){}
        super.onResume();

    }

    //-----------


    @Override
    public void onPause()
    {
        super.onPause();
        PowerManager mPowerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);

        if (!mPowerManager.isScreenOn())
            if (sonGameOver!= null && sonGameOver.getListeSons().get(0).getSon().isPlaying())
                try{sonGameOver.getListeSons().get(0).stop();}catch (Exception e){}
    }

}
