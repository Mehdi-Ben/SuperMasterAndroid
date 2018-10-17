package jtb.supermaster.Modes;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import io.socket.emitter.Emitter;
import jtb.supermaster.Menus.Menu_Multi_IA;
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
import jtb.supermaster.Mini_Jeux.Temps;
import jtb.supermaster.R;
import jtb.supermaster.Serveur.Fin_de_partie;
import jtb.supermaster.Serveur.Sockethor;

import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

public class Mode_courseAuxPoints extends Activity implements View.OnClickListener{


    //-------- Variables globables


    private static MediaPlayer mp1;
    private ArrayList<Class> activites = new ArrayList<Class>(); // Liste des activity miniJeu.class
    public ArrayList<Class> getActivites(){return activites;}
    CountDownTimer chrono;                                    // TextPartier
    int score = 0;
    boolean fin = false;
    int scoreRecu;
    int pointsAAtteindre = 15;
    boolean partieCourseAuxPoints = false;
    ArrayList<String> listeMiniJeux = new ArrayList<String>();
    int idJeu = 0;
    public int getIdJeu(){return idJeu;}
    boolean sonOn;
    boolean joueurAGagne = false;
    boolean IAAGagne = false;
    private int pointsAjouterIA;


    Sockethor mSocket;
    int difficulte = 1;
    public int getDifficulte(){return difficulte;}
    public final static int POINTS = 0;
    public int difficulteIA;
    public static int scoreIA = 0;
    private Toast toast = null;
    public Sons sonCourseAuxPoints;
    public Sons sonHighScore;
    String mode;
    String J1 = "";
    String J2 = "";
    String J3 = "";

    private Temps tpsIA;

    private boolean activityResultIsReturned=false;
    private String activityResult=null;
    public boolean getActivityResultIsReturned(){return activityResultIsReturned;}
    public String getActivityResult(){return activityResult;}


    //-------- Getters


    public static int getScoreIA() {
        return scoreIA;
    }

    public int getPointsAjouterIA() {
        return pointsAjouterIA;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public boolean isIAAGagne() {
        return IAAGagne;
    }

    public boolean isFin() {
        return fin;
    }

    public ArrayList<String> getListeMiniJeux() {
        return listeMiniJeux;
    }


    //--------



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mode_contrelamontre);
        activityResultIsReturned = false;
        activityResult = null;
        try{sonCourseAuxPoints = new Sons(R.raw.heroes, this, "music");}catch(Exception e){}
        try{sonHighScore = new Sons(R.raw.high_score, getApplicationContext(), "music");}catch (Exception e){}
        try{sonOn = Boolean.parseBoolean(getIntent().getStringExtra("son"));}catch(Exception e){}
        mSocket = new Sockethor();



        mode = getIntent().getStringExtra("mode");


        //-------- Si Mode IA

        if (mode.equals("IA")) {

            scoreIA = 0;

            this.difficulteIA = Integer.parseInt(getIntent().getStringExtra("difficulteIA"));
            pointsAAtteindre = Integer.parseInt(getIntent().getStringExtra("unites"));
            difficulte = Integer.parseInt(getIntent().getStringExtra("difficulte"));
            String[] liste = getIntent().getStringExtra("listeJeux").split("[%]");
            for (int i = 0; i < liste.length; i++) {
                listeMiniJeux.add(liste[i]);
            }



        }

        //-------- Si autre Mode

        else {
            pointsAAtteindre = Integer.parseInt(getIntent().getStringExtra("unites"));
            difficulte = Integer.parseInt(getIntent().getStringExtra("difficulte"));
            String[] liste = getIntent().getStringExtra("listeJeux").split("[%]");

            for (int i = 0; i < liste.length; i++) {
                listeMiniJeux.add(liste[i]);
            }
        }
        // Ajout des différents mini jeux à la liste des activitées

        for (String s : listeMiniJeux) {
            switch (s) {
                case "couleurs":
                    activites.add(Couleur_a_trouver.class);
                    break;
                case "basket":
                    activites.add(Basket.class);
                    break;
                case "demineur":
                    activites.add(Jeu_pustules.class);
                    break;
                case "arrow":
                    activites.add(ArrowGame.class);
                    break;
                case "enigmes":
                    activites.add(Enigmes_a_resoudre.class);
                    break;
                case "points":
                    activites.add(Point_connection.class);
                    break;
            }
        }


        //--------


        chrono = new CountDownTimer(3000, 1) {

            @Override
            public void onTick(long millisUntilFinished) {
                ((TextView) findViewById(R.id.TextPartie)).setText("" + ((millisUntilFinished / 1000) + 1)); // Mise à jour du TexteView du décompte
            }

            @Override
            public void onFinish() {
                lancerParties(idJeu); //Lancement de la partie course aux points après le décompte
            }
        };
        if (sonOn) {
            try{sonCourseAuxPoints.play();}catch(Exception e){}
        }
        chrono.start();
    }

    // Crée les différentes activities correspondant aux mini jeux
    private void lancerParties(int i) {
        activityResultIsReturned = false;
        Intent activity = new Intent(this, this.activites.get(i));


        //-------- Temps IA et Calcul du Score

        if (mode.equals("IA")) {
            tpsIA = new Temps(20) {
                @Override
                public void update() {
                    if(tpsIA.getTime() >= 1250){
                        System.out.println("Score IA : " + scoreIA+"/"+pointsAAtteindre);
                        pointsAjouterIA = Menu_Multi_IA.calculProba(difficulteIA);
                        scoreIA += pointsAjouterIA;
                        if (scoreIA == -1) {
                            scoreIA = 0;
                        }
                        if (scoreIA >= pointsAAtteindre)
                        {
                            fin = true;
                            IAAGagne = true;

                            finDePartie();
                            tpsIA.destroy();

                        }
                        tpsIA.reset();

                    }

                }
            };
            tpsIA.resume();

            activity.putExtra("NB_POINTS_A_ATTEINDRE", pointsAAtteindre);
        }


        //-------- Si mode Multi

        if (mode.equals("courseAuxPoints")) {
            activity.putExtra("J1", J1);
            activity.putExtra("J2", J2);
            activity.putExtra("J3", J3);
        }
        activity.putExtra("SCORE", score);
        activity.putExtra("mode", mode);
        activity.putExtra("ScoreIA", "" + scoreIA);
        activity.putExtra("DIFFICULTE", this.difficulte);
        startActivityForResult(activity, POINTS);
    }

    //--------


    // Mise à jour TextView affiche score final mode classique
    private void finDePartie() {
        if (sonOn) try{mp1.stop();}catch(Exception e){}
        int ancienScore = 0;
        try {
            ancienScore = Integer.parseInt(Menu_libre.readFromFile("course_aux_points.txt", getApplicationContext()));
        } catch (Exception e) {
            Menu_libre.writeToFile("0", "course_aux_points.txt", getApplicationContext());
        }
        if (score > ancienScore) {
            Menu_libre.writeToFile("" + score, "course_aux_points.txt", getApplicationContext());
            try{sonHighScore.play();}catch (Exception e){}

            //-------- Si fin de partie:


        } else if (fin) { // Victoire IA et Joueur
            if (IAAGagne && joueurAGagne){
                ((TextView) findViewById(R.id.TextPartie)).setText("~ Fin de partie ~\nVous êtes ex-aequo !");
                ((TextView) findViewById(R.id.viewScore)).setText("" + score);
                ((TextView) findViewById(R.id.viewScore)).setVisibility(View.VISIBLE);
                ((TextView) findViewById(R.id.viewScoreIA)).setText(""+scoreIA);
                ((TextView) findViewById(R.id.viewScoreIA)).setVisibility(View.VISIBLE);
                (findViewById(R.id.viewScoreFinalIA)).setVisibility(View.VISIBLE);
            }
            else if (IAAGagne&&(!joueurAGagne)){ //Victoire IA
                ((TextView) findViewById(R.id.TextPartie)).setText("~ Fin de partie ~\nVous avez perdu...");
                ((TextView) findViewById(R.id.viewScore)).setText("" + score);
                ((TextView) findViewById(R.id.viewScore)).setVisibility(View.VISIBLE);
                ((TextView) findViewById(R.id.viewScoreIA)).setText(""+scoreIA);
                ((TextView) findViewById(R.id.viewScoreIA)).setVisibility(View.VISIBLE);
                (findViewById(R.id.viewScoreFinalIA)).setVisibility(View.VISIBLE);

            }
            else if(joueurAGagne&&(!IAAGagne)) { //Victoire Joueur
                ((TextView) findViewById(R.id.TextPartie)).setText("~ Fin de partie ~\nVous avez gagné !");
                ((TextView) findViewById(R.id.viewScore)).setText("" + score);
                ((TextView) findViewById(R.id.viewScore)).setVisibility(View.VISIBLE);
                ((TextView) findViewById(R.id.viewScoreIA)).setText(""+scoreIA);
                ((TextView) findViewById(R.id.viewScoreIA)).setVisibility(View.VISIBLE);
                (findViewById(R.id.viewScoreFinalIA)).setVisibility(View.VISIBLE);

            }
            fin = false;
        } else {
            ((TextView) findViewById(R.id.TextPartie)).setText("Vous avez arrêté de jouer !");
        }
        if (!mode.equals("IA"))
            ((TextView) findViewById(R.id.viewScore)).setText("" + scoreRecu);
        (findViewById(R.id.viewScore)).setVisibility(View.VISIBLE);
        (findViewById(R.id.viewScoreFinal)).setVisibility(View.VISIBLE);
        this.score = 0;
    }

    //--------


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            activityResultIsReturned = true;
            this.onResume();
            idJeu = (idJeu + 1) % activites.size();
            if (idJeu == 0) {
                this.difficulte++;
            }
            scoreRecu = Integer.parseInt(data.getStringExtra("SCORE"));
            if (mode.equals("courseAuxPoints")) {
                J1 = data.getStringExtra("J1");
                J2 = data.getStringExtra("J2");
                J3 = data.getStringExtra("J3");
            }

            //-------- Si IA

            if(!mode.equals("IA")) {

                if (scoreRecu <= this.score) {
                    if (this.score > 0) {
                        mSocket.decr_points_serveur();
                        this.score = scoreRecu;
                    }
                } else  {
                    this.score = scoreRecu;
                    mSocket.incr_points_serveur();
                }
                lancerParties(idJeu);

            }

            else { //Si mode IA
                if (scoreRecu <= this.score) {
                    if (this.score > 0) {
                        this.score = scoreRecu;
                    }
                } else  {
                    this.score = scoreRecu;
                }

                if (score != pointsAAtteindre && scoreIA != pointsAAtteindre) {
                    lancerParties(idJeu);
                } else if ((score == pointsAAtteindre)&&(scoreIA != pointsAAtteindre)){
                    fin = true;
                    joueurAGagne = true;
                    if (mode.equals("IA")) {
                        tpsIA.destroy();
                        finDePartie();
                    }
                }
                else if ((scoreIA >= pointsAAtteindre)&&(score != pointsAAtteindre)){
                    fin = true;
                    IAAGagne = true;
                    if (mode.equals("IA")){
                        tpsIA.destroy();
                        finDePartie();
                    }
                }
                else{
                    fin = true;
                    IAAGagne = true;
                    joueurAGagne = true;
                    if (mode.equals("IA")){
                        tpsIA.destroy();
                        finDePartie();
                    }
                }

            }



        } else {
            this.onResume();
        }
    }

    //--------

    @Override
    public void onClick(View v) {
    }

    @Override
    public void onBackPressed() {
        if (sonOn) try{mp1.stop();}catch(Exception e){}
        chrono.cancel();
        this.onResume();
        Intent activity = new Intent(this, Menu_principal.class);
        startActivity(activity);
        finish();
        super.onBackPressed();
    }
}