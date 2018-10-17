package jtb.supermaster.Modes;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.PowerManager;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;

import jtb.supermaster.Menus.Menu_principal;
import jtb.supermaster.Mini_Jeux.ArrowGame;
import jtb.supermaster.Mini_Jeux.Basket;
import jtb.supermaster.Mini_Jeux.Couleur_a_trouver;
import jtb.supermaster.Menus.Menu_libre;
import jtb.supermaster.Mini_Jeux.Enigmes_a_resoudre;
import jtb.supermaster.Mini_Jeux.Jeu_pustules;
import jtb.supermaster.Mini_Jeux.Mini_jeu;
import jtb.supermaster.Mini_Jeux.Point_connection;
import jtb.supermaster.Mini_Jeux.Sons;
import jtb.supermaster.R;

public class Mode_classique extends Activity implements View.OnClickListener{

    private ArrayList<Class> activites = new ArrayList<Class>(); // Liste des activity miniJeu.class
    CountDownTimer chrono;                                       // Timer
    int score = 0;                                               // Score total du joueur sur un enchainement de mini jeux
    int nbVies = 3;
    int idJeu = 0;

    int difficulte = 1;
    public final static int POINTS = 0;                          // Variable correspondant au requestCode des mini jeux

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mode_classique);

        // Ajout des différents mini jeux à la liste des activitées
        activites.add(Couleur_a_trouver.class);
        activites.add(Basket.class);
        activites.add(Jeu_pustules.class);
        activites.add(ArrowGame.class);
        activites.add(Enigmes_a_resoudre.class);
        activites.add(Point_connection.class);

        Collections.shuffle(activites);

        //-------- Police

        int couleurTexte = 0xFF000000;
        int tailleTexte = 25;
        Typeface custom_font1 = Typeface.createFromAsset(getAssets(), "fonts/helsinki.ttf");

        findViewById(R.id.quit).setVisibility(View.INVISIBLE);
        findViewById(R.id.replay).setVisibility(View.INVISIBLE);
        findViewById(R.id.textReplay).setVisibility(View.INVISIBLE);
        ((TextView)findViewById(R.id.textReplay)).setTypeface(custom_font1);
        ((TextView)findViewById(R.id.textReplay)).setTextSize(tailleTexte);
        ((TextView)findViewById(R.id.textReplay)).setTextColor(couleurTexte);
        ((TextView)findViewById(R.id.Time)).setTypeface(custom_font1);
        ((TextView)findViewById(R.id.Time)).setTextSize(tailleTexte);
        ((TextView)findViewById(R.id.Time)).setTextColor(couleurTexte);
        findViewById(R.id.quit).setOnClickListener(this);
        findViewById(R.id.replay).setOnClickListener(this);


         /* Chrono */
        int duree = 3; // Temps voulu en secondes
        final long delay = 1000 * duree;
        chrono = new CountDownTimer(delay, 1) {

            @Override
            public void onTick(long millisUntilFinished) {
                ((TextView) findViewById(R.id.Time)).setText("" + ((millisUntilFinished/1000)+1)); // Mise à jour du TexteView du décompte
            }

            @Override
            public void onFinish() {
                lancerParties(idJeu);    //Lancement de la partie classique après le décompte
            }
        };
        chrono.start();
    }


    //--------

    // Crée les différentes activities correspondant aux mini jeux
    private void lancerParties(int i){
        Intent activity = new Intent(this, this.activites.get(i));
        activity.putExtra("SCORE", this.score);
        activity.putExtra("NB_VIES", this.nbVies);
        activity.putExtra("DIFFICULTE", this.difficulte);
        activity.putExtra("mode","classique");
        startActivityForResult(activity,POINTS);
    }

    //--------

    // Mise à jour TextView affiche score final mode classique
    private void finDePartie(){
        ((TextView) findViewById(R.id.Time)).setText("Score final\n" + score);
        ((TextView) findViewById(R.id.Time)).setTextSize(60);
        findViewById(R.id.quit).setVisibility(View.VISIBLE);
        findViewById(R.id.replay).setVisibility(View.VISIBLE);
        findViewById(R.id.textReplay).setVisibility(View.VISIBLE);

        int ancienScore = 0;
        try {
            ancienScore = Integer.parseInt(Menu_libre.readFromFile("classique.txt", getApplicationContext()));
        } catch (Exception e) {
            Menu_libre.writeToFile("0", "classique.txt", getApplicationContext());
        }

        if (score > ancienScore) {
            Menu_libre.writeToFile("" + score, "classique.txt", getApplicationContext());
            try{(new Sons(R.raw.high_score,  getApplicationContext(), "music")).play();}catch(Exception e){}
        }

        this.score = 0;
        this.nbVies = 3;
    }

    //--------

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            findViewById(R.id.textReplay).setVisibility(View.INVISIBLE);
            this.onResume();
            idJeu = (idJeu + 1) % activites.size();
            if(idJeu == 0){
                this.difficulte++;
            }
            int scoreRecu = Integer.parseInt(data.getStringExtra("SCORE"));
            if(scoreRecu == this.score){
                this.nbVies--;
            }else {
                this.score = scoreRecu;
            }
            if(this.nbVies > 0){
                lancerParties(idJeu);
            }else{
                finDePartie();
            }
        }else{
            this.onResume();
            finDePartie();
        }
    }

    //--------

    @Override
    public void onClick(View v){
        if(v.getId() == R.id.quit) {
            finish();
        } else if(v.getId() == R.id.replay){
            findViewById(R.id.quit).setVisibility(View.INVISIBLE);
            findViewById(R.id.replay).setVisibility(View.INVISIBLE);
            this.score = 0;
            this.nbVies = 3;
            this.difficulte = 1;
            this.idJeu = 0;
            chrono.start();
        }
    }

    //--------

    @Override
    public void onBackPressed(){
        chrono.cancel();
        this.onResume();
        Intent activity = new Intent(this, Menu_principal.class);
        startActivity(activity);
        finish();
        super.onBackPressed();
    }


}