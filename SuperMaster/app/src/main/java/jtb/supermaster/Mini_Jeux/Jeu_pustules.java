package jtb.supermaster.Mini_Jeux;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;

import android.os.CountDownTimer;
import android.os.Vibrator;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import jtb.supermaster.Menus.Menu_libre;
import jtb.supermaster.Menus.Menu_principal;
import jtb.supermaster.Menus.Pause;
import jtb.supermaster.Modes.Mode_courseAuxPoints;
import jtb.supermaster.R;
import jtb.supermaster.Serveur.Fin_de_partie;

public class Jeu_pustules extends Mini_jeu implements View.OnClickListener {


    //-------- Variables globales


    int scoreIA = 0;
    Temps gameTimer;


    /*Variables globales taille*/


    private int sizePoint = 200;
    boolean partieDejaLancee = false;
    private int widthPrinc;
    private int heightPrinc;
    private int xr;
    private int yr;
    private ImageView iE1;
    int count = 0;
    boolean b = false;
    int count2 = 0;
    int countNiv20 = 6;
    int negx;
    int negy;
    int randomx;
    int randomy;
    public int getDifficulte(){return difficulte;}
    public void setDifficulte(int dif){difficulte = dif;}
    private ArrayList<ImageView> listeView;
    public ArrayList<ImageView> getListeView(){return listeView;}
    public int getScore(){return this.score;}

    private LinearLayout layoutScores = null;
    private LinearLayout layoutActionBack = null;

    ArrayList<ImageView> listeRondsNoirs =  new ArrayList<>();
    ArrayList<ImageView> listeRondsRouges = new ArrayList<>();
    ArrayList<ImageView> listeRondsBleus = new ArrayList<>();

    //-------- Nombre d'images sur le layout
    private int nombreImageView = 0;

    public int getVies(){return vie;}

    //-------- Nombre de bombes désactivées
    private int nbeCarresTouches = 0;
    public void setNbeCarresTouches(int nbeCarresTouches) {
        this.nbeCarresTouches = nbeCarresTouches;
    }
    public int getNbeCarresTouches() {
        return nbeCarresTouches;
    }

    //-------- Incrément pour les bombes noires qu'il faut toucher 3 fois avant qu'elles se désactivent
    private ArrayList<Integer> inc = new ArrayList<Integer>();


    //-------- Random du nombre de bombes à désactiver à chaque tour
    private int nbImagesAToucher = 1;
    public int getnbImagesAToucher() {
        return nbImagesAToucher;
    }

    private ProgressBar chronoBar;

    private RelativeLayout layout;
    public RelativeLayout getLayout(){return layout;}

    //*****************************************************************************************//
    //**************************** CREATION DE L'ACTIVITE ************************************//
    //***************************************************************************************//
    Vibrator vb;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        System.out.println("1");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pustules);
        this.setPauseBouton();
        layout = (RelativeLayout) findViewById(R.id.relativeLayout);
        vb = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        Typeface custom_font2 = Typeface.createFromAsset(getAssets(), "fonts/helsinki.ttf");
        tscore2 = (TextView) findViewById(R.id.viewScoreJoueur2);
        tscore2.setTypeface(custom_font2);
        tscore2.setTextSize(20);

        chronoBar = (ProgressBar) findViewById(R.id.chronometre);

        //-------- Mode Multi


        if (!partieContreLaMontre){temps = 5;}
        if (mode.equals("courseAuxPoints") || mode.equals("contreLaMontre") ) {
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

            System.out.println("J1**** " +J1);
            System.out.println("J2**** " +J2);
            System.out.println("J3**** " +J3);

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

        iE1 = new ImageView(this);

        ViewTreeObserver vto = layout.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                    layout.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                } else {
                    layout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
                widthPrinc  = layout.getMeasuredWidth();
                heightPrinc = layout.getMeasuredHeight();
                sizePoint = widthPrinc/10;
                System.out.println("MJ LIBRE1 " + partieContreLaMontre);
                if (partieContreLaMontre){partieDejaLancee = true;}
                if ((partieContreLaMontre)||(partieCourseAuxPoints)){
                    findViewById(R.id.vie).setVisibility(View.INVISIBLE);
                    findViewById(R.id.fois).setVisibility(View.INVISIBLE);
                    findViewById(R.id.imageView).setVisibility(View.INVISIBLE);}


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
                setHUD(0xFFFFFFFF);
                init();
            }
        });

        //--------


        if (finPartie == false){difficulte += 1;}

        int duree = temps; // Temps voulu en secondes
        final long delay = 1000 * duree;
        gameTimer = new Temps(100){
            @Override
            public void update(){
                double ratio = (double) (delay - gameTimer.getTime()) / delay;

                if (ratio >= 0) {
                    int progress = (int) (ratio * 100d);
                    chronoBar.setProgress(progress);
                    if (nbeCarresTouches == nbImagesAToucher){finOperation();}
                }
                else {
                    tempsEcoule = true;
                    onFinish();
                }

                if(mode.equals("IA")) {
                    tscore2.setText("Score IA: "+Mode_courseAuxPoints.getScoreIA());
                }

            }

            public void onFinish() {
                finOperation();
            }

        };
    }

    //--------


    public void init(){
        nombreImageView = 0;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ((TextView)findViewById(R.id.score)).setText(String.format("%02d",score));
                ((TextView)findViewById(R.id.vie)).setText(""+vie);
            }
        });

        if (!(partieContreLaMontre)) {
            gameTimer.reset();
            gameTimer.resume();
        }
        listeView = new ArrayList<>();
        listeRondsBleus = new ArrayList<>();
        listeRondsNoirs = new ArrayList<>();
        listeRondsRouges = new ArrayList<>();
        nbImagesAToucher = 0;

        //--------


        //chrono.start();
        nbImagesAToucher = 0;
        if (difficulte <= 10) {
            creationRond("bleu", difficulte, listeRondsBleus);
        }
        else if ((difficulte > 10)&&(difficulte <= 20)){
            creationRond("bleu", 6, listeRondsBleus);
            creationRond("noir", 2, listeRondsNoirs);
        }
        else if ((difficulte > 20)&&(difficulte <= 30)){
            creationRond("bleu",6,listeRondsBleus);
            creationRond("noir", 3, listeRondsNoirs);
        }
        else if ((difficulte > 30)&&(difficulte <= 40)){
            creationRond("rouge",2,listeRondsRouges);
            creationRond("bleu",6,listeRondsBleus);
            creationRond("noir", 3, listeRondsNoirs);
        }
        else if ((difficulte > 40) && (difficulte <= 50)){
            creationRond("rouge",2,listeRondsRouges);
            creationRond("bleu",6,listeRondsBleus);
            creationRond("noir", 4, listeRondsNoirs);}

        else if ((difficulte > 50)&&(difficulte <= 60)){
            creationRond("rouge",2,listeRondsRouges);
            creationRond("bleu",7,listeRondsBleus);
            creationRond("noir", 4, listeRondsNoirs);
        }
        else{
            creationRond("rouge",3,listeRondsRouges);
            creationRond("bleu",7,listeRondsBleus);
            creationRond("noir", 5, listeRondsNoirs);
        }

        nbeCarresTouches = 0;
        if ((partieContreLaMontre) && (partieDejaLancee)) {
            lancerChrono();
            partieDejaLancee = false;
        }
        else if (!partieContreLaMontre){
            lancerChrono();
        }
    }


    //--------



    public void lancerChrono(){

        randomx = (int) (Math.random() * 5) + 2;
        randomy = (int) (Math.random() * 5) + 2;
        negx = 1;
        negy = 1;
        /* Chrono */


    }


    //--------


    public void creationRond(String s, int dif, ArrayList<ImageView> l){
        for (int i = 0; i < dif; i++) {
            xr = (int) (Math.abs(Math.random() * (widthPrinc - sizePoint)));
            yr = (int) (Math.abs(Math.random() * (heightPrinc - sizePoint)));
            iE1 = new ImageView(this);
            if (s == "bleu"){
                iE1.setImageResource(getResources().getIdentifier("rond_bleu7", "drawable", getPackageName()));
                iE1.setTag(R.drawable.rond_bleu7);
                nbImagesAToucher += 1;
            }
            else if (s == "rouge"){
                iE1.setImageResource(getResources().getIdentifier("rond_rouge", "drawable", getPackageName()));
                iE1.setTag(R.drawable.rond_rouge);
            }
            else {
                iE1.setImageResource(getResources().getIdentifier("rond_noir3b", "drawable", getPackageName()));
                iE1.setTag(R.drawable.rond_noir3b);
                nbImagesAToucher += 1;
            }
            iE1.setId(listeView.size());
            listeView.add(iE1);
            iE1.setOnClickListener(this);
            iE1.setX(xr);
            iE1.setY(yr);
            l.add(iE1);
            nombreImageView += 1;

            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    if(iE1.getParent()!=null)
                        ((ViewGroup)iE1.getParent()).removeView(iE1);
                    layout.addView(iE1);
                }
            });
        }
    }


    //***************************************************************************************//
    //********************************* ON BACK PRESS **************************************//
    //*************************************************************************************//

    @Override
    public void onBackPressed(){
        backpressed = true;
        if (!(mode.equals("courseAuxPoints") || mode.equals("contreLaMontre"))) gameTimer.destroy();
        sauver_et_quitter();
    }


    //*****************************************************************************************//
    //*********************************** ON CLICK *******************************************//
    //***************************************************************************************//

    @Override
    public void onClick(View v) {
        if (listeRondsRouges.contains(v)) {
            vb.vibrate(100);
            finOperation();
        }
        else if (listeRondsNoirs.contains(v))
        {if (sonCorrect.vibrationsOn) {vb.vibrate(50); miseAJourRondNoir(v);}}
        else {
            if (sonCorrect.vibrationsOn) {vb.vibrate(50);}
            v.setAnimation(null);
            final View vs = v;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    vs.setVisibility(View.INVISIBLE);
                    nbeCarresTouches += 1;
                }
            });
        }
    }
    protected void son_bombeRouge(){
        try{(new Sons(R.raw.explosion, getApplicationContext(), "son")).play();}catch(Exception e){}
    }
    protected void son_bombeRouge1(){
        try{(new Sons(R.raw.click, getApplicationContext(), "son")).play();}catch(Exception e){}
    }


    //***************************************************************************************//
    //******************************** FIN OPERATION ***************************************//
    //*************************************************************************************//

    protected void finOperation(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                layout.removeAllViews();
            }
        });
        //Si le nombre de bombes touchées est égal au nombre de bombes à désactiver
        if(nbeCarresTouches == nbImagesAToucher){
            Gagne();
        }
        else {
            Perdu();
        }

        if (!partieContreLaMontre){
            gameTimer.reset();
            gameTimer.stop();
        }
        difficulte += 1;
        finPartie();
    }
    public void setNewResourceElt(ImageView v, int resId){
        final ImageView vs = v;
        final int res = resId;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                vs.setImageResource(res);
            }
        });
        v.setTag(resId);
    }
    public void miseAJourRondNoir(View v){
        int srcRond3 = getResources().getIdentifier("rond_noir3b", "drawable", getPackageName());
        int srcRond2 = getResources().getIdentifier("rond_noir2b", "drawable", getPackageName());
        if ((Integer)v.getTag() == srcRond3) {
            System.out.println("AVANT : " + srcRond3);
            setNewResourceElt((ImageView) v, R.drawable.rond_noir2b);
            v.setOnClickListener(this);
            System.out.println("COUCOU IMAGE2 : " + v.getTag());
        }
        else if ((Integer)v.getTag() == srcRond2){
            System.out.println("HEY " + (Integer)v.getTag());
            setNewResourceElt((ImageView) v, R.drawable.rond_noir1b);
            v.setOnClickListener(this);
            System.out.println("Image1 " + v.getTag());
        }
        else {
            final View vs = v;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    vs.setVisibility(View.INVISIBLE);
                    nbeCarresTouches += 1;
                }
            });
        }
    }


    //--------  Mode Multi


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

    //-------- Mode IA


    public void AffichageScoreIA() {
        scoreIA = Integer.parseInt(getIntent().getStringExtra("ScoreIA"));
        tscore2.setText( "Score IA : " + scoreIA);
        J2 = "Score IA : " + scoreIA;
        if(tscore2.getVisibility() == View.GONE) tscore2.setVisibility(View.VISIBLE);
    }

////////////////
// MENU PAUSE //
////////////////
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
            //try{sonBulle.play();}catch(Exception e){}
            startActivityForResult(activity, 0);
        }
    }
}

