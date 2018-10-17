package jtb.supermaster.Mini_Jeux;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.hardware.SensorManager;
import android.hardware.Sensor;
import android.hardware.SensorEventListener;
import android.content.Context;
import android.hardware.SensorEvent;
import android.os.CountDownTimer;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import jtb.supermaster.Menus.Menu_libre;
import jtb.supermaster.Menus.Menu_principal;
import jtb.supermaster.Menus.Pause;
import jtb.supermaster.Modes.Mode_courseAuxPoints;
import jtb.supermaster.R;

public class Enigmes_a_resoudre extends Mini_jeu implements SensorEventListener {


    //----- Variables globables

    private final Enigmes enigmes = new Enigmes();
    private Enigme monEnigme = null;
    int scoreIA = 0;
    private TextView enigme = null;
    private TextView gauche = null;
    private TextView droite = null;
    private String correct = null;
    private Boolean aRepondu = true;
    private Boolean aClique = false;
    private FrameLayout layoutAction = null;
    private ProgressBar chronoBar;
    private long delay;
    private boolean partieMJ = false;
    private SensorManager sensorManager = null;
    private Sensor accelerometer = null;
    Temps gameTimer;

    private LinearLayout layoutScores = null;
    private LinearLayout layoutActionBack = null;

    //--------


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enigmes_a_resoudre);
        this.setPauseBouton();

        Typeface custom_font2 = Typeface.createFromAsset(getAssets(), "fonts/helsinki.ttf");
        tscore2 = (TextView) findViewById(R.id.viewScoreJoueur2);
        tscore2.setTypeface(custom_font2);
        tscore2.setTextSize(20);

        /* Affichage des scores */

        //-------- Mode Multi

        if (mode.equals("courseAuxPoints") || mode.equals("contreLaMontre") ) {
            findViewById(R.id.vie).setVisibility(View.INVISIBLE);
            findViewById(R.id.fois).setVisibility(View.INVISIBLE);
            findViewById(R.id.imageView).setVisibility(View.INVISIBLE);

            tscore1 = (TextView) findViewById(R.id.viewScoreJoueur1);
            tscore2 = (TextView) findViewById(R.id.viewScoreJoueur2);
            tscore3 = (TextView) findViewById(R.id.viewScoreJoueur3);

            Typeface custom_font1 = Typeface.createFromAsset(getAssets(), "fonts/helsinki.ttf");

            tscore1.setTypeface(custom_font1);
            tscore2.setTypeface(custom_font1);
            tscore3.setTypeface(custom_font1);

            tscore1.setTextSize(20);
            tscore2.setTextSize(20);
            tscore3.setTextSize(20);

            layoutActionBack = (LinearLayout) findViewById(R.id.layoutActionBack);
            layoutScores = (LinearLayout) findViewById(R.id.layoutScores);
            if(layoutScores.getVisibility() == View.GONE){
                layoutScores.setVisibility(View.VISIBLE);
                LinearLayout.LayoutParams newParam  = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0);
                newParam.weight = 74;
                layoutActionBack.setLayoutParams(newParam);
            }

            String nJ1 = J1.split("[:]")[0];
            String nJ2 = J2.split("[:]")[0];
            String nJ3 = J3.split("[:]")[0];

            if (!nJ1.equals("None") && !nJ1.equals("")) {
                tscore1.setText(J1);
                if(tscore1.getVisibility() == View.GONE) tscore1.setVisibility(View.VISIBLE);
            }
            if (!nJ2.equals("None") && !nJ2.equals("")) {
                tscore2.setText(J2);
                if(tscore2.getVisibility() == View.GONE) tscore2.setVisibility(View.VISIBLE);
            }
            if (!nJ3.equals("None") && !nJ3.equals("")) {
                tscore3.setText(J3);
                if(tscore3.getVisibility() == View.GONE) tscore3.setVisibility(View.VISIBLE);
            }
        }
        if (partieContreLaMontre){
            partieMJ = true;
        }

        //--------


        /* Accéléromètre */
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        /* Enigmes */
        enigme = (TextView) findViewById(R.id.enigme);
        droite = (TextView) findViewById(R.id.droite);
        gauche = (TextView) findViewById(R.id.gauche);

        Typeface custom_font = Typeface.createFromAsset(getAssets(),  "fonts/manuscrit.ttf");
        enigme.setTypeface(custom_font);
        droite.setTypeface(custom_font);
        gauche.setTypeface(custom_font);

        /* Chrono */
        chronoBar = (ProgressBar) findViewById(R.id.chronometre);

        /* Autre */
        layoutAction = (FrameLayout) findViewById(R.id.layoutAction);


        //-------- Mode IA


        if(mode.equals("IA")) {

            layoutActionBack = (LinearLayout) findViewById(R.id.layoutActionBack);
            layoutScores = (LinearLayout) findViewById(R.id.layoutScores);
            if(layoutScores.getVisibility() == View.GONE){
                layoutScores.setVisibility(View.VISIBLE);
                LinearLayout.LayoutParams newParam  = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0);
                newParam.weight = 74;
                layoutActionBack.setLayoutParams(newParam);
            }

            AffichageScoreIA();
        }

        //-------- Mode Multi


        if (!partieContreLaMontre) {
            double duree = 15 - (difficulte * 0.4);
            delay = (long) (1000 * duree);
        }
        else delay = (long) (1000 * temps);
        gameTimer = new Temps(20) {
            boolean aller = true;


            //-------- Temps


            @Override
            public void update(){
                //System.out.println("TEMPS : " + getTime());
                double ratio = (double) (delay - gameTimer.getTime()) / delay;
                if (ratio >= 0) {
                    if (ratio >= 0) chronoBar.setProgress((int) (ratio * 100d));
                    if ((delay - gameTimer.getTime()) < (delay - 100)) aRepondu = false;
                }
                else {
                    onFinish();
                }

                if(mode.equals("IA")) {
                    tscore2.setText("Score IA: "+Mode_courseAuxPoints.getScoreIA());
                }            }

            public void onFinish() {
                tempsEcoule = true;
                finOperation(false);
            }
        };
        setHUD(0xFF000000);
        init();
    }

    //--------


    protected void init() {
        ((TextView)findViewById(R.id.score)).setText(String.format("%02d",score));
        ((TextView)findViewById(R.id.vie)).setText(""+vie);
        if (!(partieContreLaMontre)) {
            gameTimer.reset();
        }
        abonnementAccelerometre(sensorManager, accelerometer);

        //--------


        monEnigme = this.enigmes.next();
        enigme.setText(monEnigme.getQuestion());
        droite.setText(monEnigme.getDroite());
        gauche.setText(monEnigme.getGauche());
        correct = monEnigme.getReponse();
        aRepondu = true;
        aClique = false;

        //-------- Mode multi


        if (!partieContreLaMontre) {
            double duree = 15 - (difficulte * 0.4);
            delay = (long) (1000 * duree);
        }
        else delay = (long) (1000 * temps);


        if ((partieContreLaMontre) && (partieMJ)) {
            gameTimer.resume();
            partieMJ = false;
        }
        else if (!partieContreLaMontre){
            gameTimer.resume();
        }
    }

    //--------


    protected void finOperation(boolean juste) {
        if (juste) {
            Gagne();

        } else {
            Perdu();
        }
        desabonnementAccelerometre(sensorManager, accelerometer);
        finPartie();
    }

    //--------


    @Override
    protected void sauver_et_quitter(){
        if (!((mode.equals("courseAuxPoints") || mode.equals("contreLaMontre")) && backpressed)) {
            desabonnementAccelerometre(sensorManager, accelerometer);
            gameTimer.destroy();
        }
        super.sauver_et_quitter();
    }

    public void abonnementAccelerometre(SensorManager sensorManager, Sensor accelerometer) {
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_UI);
    }

    public void desabonnementAccelerometre(SensorManager sensorManager, Sensor accelerometer) {
        sensorManager.unregisterListener(this, accelerometer);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accurency) {
    }

    //--------


    @Override
    public void onSensorChanged(SensorEvent event) {
        final float x;
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            x = event.values[0];

            if (x > 3){
                gauche.setTextColor(Color.RED);
                droite.setTextColor(Color.BLACK);
            }
            else if (x < -3){
                droite.setTextColor(Color.RED);
                gauche.setTextColor(Color.BLACK);
            }
            else {
                gauche.setTextColor(Color.BLACK);
                droite.setTextColor(Color.BLACK);
            }

            layoutAction.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if ((!aRepondu) && ((x > 3) || (x < -3)) && event.getAction() == MotionEvent.ACTION_DOWN) {
                        if ((((x > 3) && correct.equals("gauche")) || ((x < -3) && correct.equals("droite"))))
                            finOperation(true);
                        else
                            finOperation(false);
                        aClique = true;
                    }
                    return true;
                }
            });
        }
    }

    //--------


    public void setEnigme(Enigme e) {
        this.monEnigme = e;
        this.correct = monEnigme.getReponse();
    }

    //--------


    public int getScore(){ return this.score; }

    public int getVie(){return this.vie;}

    public int getColorDroite(){ return this.droite.getCurrentTextColor(); }

    public int getColorGauche(){ return this.gauche.getCurrentTextColor(); }

    //-------- Mode Multi


    protected void actualiserScores(String pseudo1, String pseudo2, String pseudo3, int score1, int score2, int score3) {
        final String npseudo1 = pseudo1;
        final String npseudo2 = pseudo2;
        final String npseudo3 = pseudo3;
        final int nscore1 = score1;
        final int nscore2 = score2;
        final int nscore3 = score3;

        try {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
                    if (!npseudo1.equals("None")) {
                        tscore1.setText(npseudo1 + " : " + nscore1);
                        J1 = npseudo1 + " : " + nscore1;
                        if(tscore1.getVisibility() == View.GONE) tscore1.setVisibility(View.VISIBLE);
                        System.out.println("Ecriture score 1");
                    }
                    if (!npseudo2.equals("None")) {
                        tscore2.setText(npseudo2 + " : " + nscore2);
                        J2 = npseudo2 + " : " + nscore2;
                        if(tscore2.getVisibility() == View.GONE) tscore2.setVisibility(View.VISIBLE);
                        System.out.println("Ecriture score 2");
                    }
                    if (!npseudo3.equals("None")) {
                        tscore3.setText(npseudo3 + " : " + nscore3);
                        J3 = npseudo3 + " : " + nscore3;
                        if(tscore3.getVisibility() == View.GONE) tscore3.setVisibility(View.VISIBLE);
                        System.out.println("Ecriture score 3");
                    }
                }catch (Exception e2){
                    finish();
                }}});
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    //-------- Mode IA


    public void AffichageScoreIA() {
        scoreIA = Integer.parseInt(getIntent().getStringExtra("ScoreIA"));
        tscore2.setText( "Score IA : " + scoreIA);
        J2 = "Score IA : " + scoreIA;
        if(tscore2.getVisibility() == View.GONE) tscore2.setVisibility(View.VISIBLE);

    }

    //--------


    @Override
    public void onPause() {
        super.onPause();
        gameTimer.stop();
    }

    @Override
    public void onResume(){
        super.onResume();
        gameTimer.resume();
    }

    //--------


    public void onClickPause(View v){

        if (mode.equals("courseAuxPoints") || mode.equals("contreLaMontre"))
        {
            onBackPressed();
        }
        else if ( mode.equals("IA"))
        {
            Intent activity2 = new Intent(this, Menu_principal.class);
            activity2.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(activity2);
            finish();
        }
        else{
            gameTimer.stop();
            Intent activity = new Intent(this, Pause.class);
           // try{sonBulle.play();}catch(Exception e){}
            startActivityForResult(activity, 0);
        }
    }

    //--------


    @Override
    public void onBackPressed() {
        if (!(mode.equals("courseAuxPoints") || mode.equals("contreLaMontre"))) gameTimer.destroy();
        backpressed = true;
        sauver_et_quitter();
    }

}

