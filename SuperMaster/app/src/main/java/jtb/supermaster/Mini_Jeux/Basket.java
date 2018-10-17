package jtb.supermaster.Mini_Jeux;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.concurrent.ExecutionException;

import jtb.supermaster.Menus.Menu_libre;
import jtb.supermaster.Menus.Menu_principal;
import jtb.supermaster.Menus.Pause;
import jtb.supermaster.Modes.Mode_courseAuxPoints;
import jtb.supermaster.R;

public class Basket extends Mini_jeu implements SensorEventListener {


    //------- Variables globales


    private ImageView viseur = null;
    private ImageView ballon = null;
    private ImageView panier = null;
    private ImageView panier_t = null;
    private ImageView reddot = null;
    private FrameLayout layoutAction = null;
    private LinearLayout layoutActionBack = null;
    private ProgressBar chronoBar;
    //private CountDownTimer chrono;
    private SensorManager sensorManager;
    private Sensor accelerometer;
    private LinearLayout layoutScores = null;

    int scoreIA = 0;
    public Sons sonBasket;

    private int width = 0;
    private int height = 0;
    private int up = 1;
    private float alpha = 0;
    private float x_panier;
    private float y_panier;
    private float y_panier_default;
    private float limite_x_inf_panier;
    private float limite_x_sup_panier;
    private float x_ballon_depart;
    private float y_ballon_depart;
    private double angle_degres;
    private double angle_radian;
    private double coef = Math.cos((90 * Math.PI) / 180) * 108;
    private boolean partieDejaLancee = false;
    private boolean envoyer = false;
    private boolean gagne = false;
    private boolean perdu = false;
    private boolean setcoef = true;
    private boolean place = false;


    Temps gameTimer; // En millisecondes


    //-------


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basket);
        this.setPauseBouton();
        sonBasket = new Sons(R.raw.basket, getApplicationContext(), "son");

        //------- IA


        Typeface custom_font2 = Typeface.createFromAsset(getAssets(), "fonts/helsinki.ttf");
        tscore2 = (TextView) findViewById(R.id.viewScoreJoueur2);
        tscore2.setTypeface(custom_font2);
        tscore2.setTextSize(20);

        //------- Mode Multi

        layoutAction = (FrameLayout) findViewById(R.id.layoutAction);
        layoutActionBack = (LinearLayout) findViewById(R.id.layoutActionBack);
        if (!partieContreLaMontre) {
            temps = 10;
        }
        if (mode.equals("courseAuxPoints") || mode.equals("contreLaMontre")) {
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

            layoutScores = (LinearLayout) findViewById(R.id.layoutScores);
            if (layoutScores.getVisibility() == View.GONE) {
                layoutScores.setVisibility(View.VISIBLE);
                LinearLayout.LayoutParams caca = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0);
                caca.weight = 74;
                layoutActionBack.setLayoutParams(caca);
            }


            String nJ1 = J1.split("[:]")[0];
            String nJ2 = J2.split("[:]")[0];
            String nJ3 = J3.split("[:]")[0];

            if (!nJ1.equals("None") && !nJ1.equals("")) {
                tscore1.setText(J1);
                if (tscore1.getVisibility() == View.GONE) tscore1.setVisibility(View.VISIBLE);
            }
            if (!nJ2.equals("None") && !nJ2.equals("")) {
                tscore2.setText(J2);
                if (tscore2.getVisibility() == View.GONE) tscore2.setVisibility(View.VISIBLE);
            }
            if (!nJ3.equals("None") && !nJ3.equals("")) {
                tscore3.setText(J3);
                if (tscore3.getVisibility() == View.GONE) tscore3.setVisibility(View.VISIBLE);
            }
        }

        //-------


        ballon = (ImageView) findViewById(R.id.ballon);
        panier = (ImageView) findViewById(R.id.panier);
        panier_t = (ImageView) findViewById(R.id.panier_t);
        viseur = (ImageView) findViewById(R.id.viseur);
        reddot = (ImageView) findViewById(R.id.reddot);
        chronoBar = (ProgressBar) findViewById(R.id.chronometre);


        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);


        ViewTreeObserver vto = layoutAction.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                layoutAction.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                width = layoutAction.getMeasuredWidth();
                height = layoutAction.getMeasuredHeight();


                //-------


                /* Réglages ballon */


                y_ballon_depart = (float) (height - 1.4 * ballon.getHeight());
                x_ballon_depart = width / 2 - (ballon.getWidth() / 2);
                ballon.setX(x_ballon_depart);
                ballon.setY(y_ballon_depart);
                viseur.setY((float) (y_ballon_depart - (0.15 * ballon.getWidth())));


                //-------


                /* Réglages panier */

                y_panier_default = width / 2 - (panier.getWidth() / 2);
                alpha = 0;
                panier.setY((float) (0.2 * panier.getHeight()));
                panier.setX(y_panier_default - alpha);

                panier_t.setY(panier.getY());
                panier_t.setX(panier.getX());

                x_panier = panier.getX() + (panier.getWidth() / 2) - (float) (0.02 * panier.getWidth());
                y_panier = panier.getY() + (panier.getHeight() / 2) + (float) (0.1 * panier.getHeight());
                limite_x_inf_panier = x_panier - (float) (0.25 * panier.getWidth());
                limite_x_sup_panier = x_panier + (float) (0.25 * panier.getWidth());
                reddot.setX(x_ballon_depart);
                reddot.setY(y_ballon_depart);

                if (partieContreLaMontre) {
                    partieDejaLancee = true;
                }
                if ((partieContreLaMontre) || (partieCourseAuxPoints)) {
                    findViewById(R.id.vie).setVisibility(View.INVISIBLE);
                    findViewById(R.id.fois).setVisibility(View.INVISIBLE);
                    findViewById(R.id.imageView).setVisibility(View.INVISIBLE);

                }

                //------- Mode IA


                if (mode.equals("IA")) {

                    layoutActionBack = (LinearLayout) findViewById(R.id.layoutActionBack);
                    layoutScores = (LinearLayout) findViewById(R.id.layoutScores);
                    if (layoutScores.getVisibility() == View.GONE) {
                        layoutScores.setVisibility(View.VISIBLE);
                        LinearLayout.LayoutParams caca = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0);
                        caca.weight = 74;
                        layoutActionBack.setLayoutParams(caca);
                    }

                    AffichageScoreIA();
                }

                setHUD(0xFF000000);
                init();
            }
        });


        //------- Temps


        /* Chrono */
        int duree = temps; // Temps voulu en secondes
        final long delay = 1000 * duree;

        gameTimer = new Temps(20) {
            boolean aller = true;

            @Override
            public void update() {
                //System.out.println("TEMPS : " + getTime());
                double ratio = (double) (delay - gameTimer.getTime()) / delay;
                if (ratio >= 0) {

                    chronoBar.setProgress((int) (ratio * 100d));
                    if (difficulte >= 5) {
                        if (aller) maj_position_panier(difficulte * 0.25f);
                        else maj_position_panier(-difficulte * 0.25f);

                        if (panier.getX() + panier.getWidth() > width) aller = false;
                        else if (panier.getX() < 0) aller = true;
                    } else if ((difficulte >= 5) && !place && !(gagne || perdu)) {
                        panier.setX(0);
                        maj_position_panier((float) (Math.random() * (width - panier.getWidth())));
                        place = true;
                    }
                } else {
                    onFinish();
                }
                if (envoyer) {
                    actualiser();
                    if (ballon.getY() > height) onFinish();
                }

                if (mode.equals("IA")) {
                    tscore2.setText("Score IA: " + Mode_courseAuxPoints.getScoreIA());
                }

            }

            public void onFinish() {
                tempsEcoule = true;
                if (!perdu && !gagne) {
                    finOperation();
                }
            }

        };
        gameTimer.resume();
    }

    //-------


    protected void maj_position_panier(float alpha) {
        panier.setX(panier.getX() + alpha);
        panier_t.setX(panier.getX());
        x_panier = panier.getX() + (panier.getWidth() / 2) - (float) (0.02 * panier.getWidth());
        limite_x_inf_panier = x_panier - (float) (0.25 * panier.getWidth());
        limite_x_sup_panier = x_panier + (float) (0.25 * panier.getWidth());
    }

    protected void son_lancer() {
        try {
            sonBasket.play();
        } catch (Exception e) {
        }
    }

    //-------


    protected void init() {
        ((TextView) findViewById(R.id.score)).setText(String.format("%02d", score));
        ((TextView) findViewById(R.id.vie)).setText("" + vie);
        if (!(partieContreLaMontre)) {
            gameTimer.reset();
        }
        abonnementAccelerometre(sensorManager, accelerometer);

        //-------


        panier_t.setVisibility(View.INVISIBLE);
        ballon.setX(x_ballon_depart);
        ballon.setY(y_ballon_depart);

        envoyer = false;
        up = 1;
        setcoef = true;
        gagne = false;
        perdu = false;
        place = false;
        if ((partieContreLaMontre) && (partieDejaLancee)) {
            chronoBar.setProgress(100);
            //gameTimer.start();
            partieDejaLancee = false;
        } else if (!partieContreLaMontre) {
            chronoBar.setProgress(100);
            //gameTimer.start();
        }
    }

    //-------


    @Override
    public void onBackPressed() {
        if (!(mode.equals("courseAuxPoints") || mode.equals("contreLaMontre"))) gameTimer.destroy();
        backpressed = true;
        sauver_et_quitter();
    }

    protected void finOperation() {
        gameTimer.reset();
        gameTimer.stop();
        if (!partieContreLaMontre) {
            //chrono.cancel();
        }
        Perdu();
        desabonnementAccelerometre(sensorManager, accelerometer);
        finPartie();
    }


    //-------


    public void actualiser() {
        //ballon.setY(ballon.getY() - up * 30);
        ballon.setY(ballon.getY() - up * 1500 * gameTimer.getDeltaTimeSeconde());
        System.out.println(up * 1500 * gameTimer.getDeltaTimeSeconde());
        /* J'ai rajouté un coefficient pour ralentir la balle [ - Maxime]*/

        ballon.setX(ballon.getX() + (float) coef);

        if ((ballon.getY() < 0) && (up == 1)) up = -1;

        if (collision(ballon.getX(), ballon.getY()) && !gagne) {
            Gagne();
            difficulte++;
            gagne = true;

        } else if ((up == -1) && (ballon.getY() > height) && (!gagne && !perdu)) {
            Perdu();
            perdu = true;
        }
        if ((up == -1) && (ballon.getY() > height) && (gagne || perdu)) {
            if (!partieContreLaMontre) {
                //chrono.cancel();
            }
            finPartie();
        }
    }

    //-------


    public Boolean collision(float x, float y) {
        float limite_x_inf_ballon = x;
        float limite_x_sup_ballon = x + ballon.getWidth();
        float limite_y_inf_ballon = y + ballon.getWidth();

        if (up == -1) {
            panier_t.setVisibility(View.VISIBLE);
            if ((limite_x_inf_ballon >= limite_x_inf_panier) && (limite_x_sup_ballon <= limite_x_sup_panier)) {
                if (limite_y_inf_ballon >= y_panier) {
                    return true;
                }
            }
        }

        return false;
    }

    //-------


    public void abonnementAccelerometre(SensorManager sensorManager, Sensor accelerometer) {
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_UI);
    }

    //-------


    public void desabonnementAccelerometre(SensorManager sensorManager, Sensor accelerometer) {
        sensorManager.unregisterListener(this, accelerometer);
    }

    //-------


    public int getScore() {
        return this.score;
    }

    public int getVie() {
        return this.vie;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accurency) {
    }

    //-------


    @Override
    public void onSensorChanged(SensorEvent event) {

        final float x;
        layoutAction.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                envoyer = true;
                setcoef = false;
                return true;
            }
        });
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            x = event.values[0];
            int temp = (int) (10 * x);
            viseur.setRotation((float) (-9 * temp / 10.0));
            if (setcoef) {
                angle_degres = (9 * ((int) (10 * x) / 10)) + 90;
                angle_radian = (angle_degres * Math.PI) / 180;
                coef = Math.cos(angle_radian) * 50;
            }

        }
        layoutAction.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (!envoyer) son_lancer();
                envoyer = true;
                setcoef = false;
                return true;
            }
        });
    }

    //-------


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
                            if (tscore1.getVisibility() == View.GONE)
                                tscore1.setVisibility(View.VISIBLE);
                            System.out.println("Ecriture score 1");
                        }
                        if (!npseudo2.equals("None")) {
                            tscore2.setText(npseudo2 + " : " + nscore2);
                            J2 = npseudo2 + " : " + nscore2;
                            if (tscore2.getVisibility() == View.GONE)
                                tscore2.setVisibility(View.VISIBLE);
                            System.out.println("Ecriture score 2");
                        }
                        if (!npseudo3.equals("None")) {
                            tscore3.setText(npseudo3 + " : " + nscore3);
                            J3 = npseudo3 + " : " + nscore3;
                            if (tscore3.getVisibility() == View.GONE)
                                tscore3.setVisibility(View.VISIBLE);
                            System.out.println("Ecriture score 3");
                        }
                    }catch (Exception e2){
                        finish();
                    }}});
            } catch(Exception e){
                e.printStackTrace();
            }
        }

        //------- IA


    public void AffichageScoreIA() {
        scoreIA = Integer.parseInt(getIntent().getStringExtra("ScoreIA"));
        tscore2.setText("Score IA : " + scoreIA);
        J2 = "Score IA : " + scoreIA;
        if (tscore2.getVisibility() == View.GONE) {

        }

        tscore2.setVisibility(View.VISIBLE);

    }

    //-------


    @Override
    public void onPause() {
        super.onPause();
        gameTimer.stop();

    }

    @Override
    public void onResume() {
        super.onResume();
        gameTimer.resume();
    }

    //-------


    public void onClickPause(View v) {


        if (mode.equals("courseAuxPoints") || mode.equals("contreLaMontre")) {
            backpressed = true;
            sauver_et_quitter();
        } else if (mode.equals("IA")) {
            Intent activity2 = new Intent(this, Menu_principal.class);
            activity2.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(activity2);
            finish();
        } else {
            gameTimer.stop();
            Intent activity = new Intent(this, Pause.class);
            //try{sonBulle.play();}catch(Exception e){}
            startActivityForResult(activity, 0);
        }
    }

}
