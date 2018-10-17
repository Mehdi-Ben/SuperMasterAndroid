package jtb.supermaster.Mini_Jeux;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;

/* Accéléromètre */

import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

import jtb.supermaster.Menus.Menu_libre;
import jtb.supermaster.Menus.Menu_principal;
import jtb.supermaster.Menus.Pause;
import jtb.supermaster.Modes.Mode_courseAuxPoints;
import jtb.supermaster.R;

/*
 * Created by Maxime on 23/02/2017.
 */

public class ArrowGame extends Mini_jeu
{

    //------- Variables globales


    Temps tempsT;
    public Arrow[] listofArrow;
    boolean partieDejaLancee = true;
    ImageView[] listofView;
    public int nombreFleche = 1;
    public int maxTime = 1000 * temps;
    private int sens;
    public Sons sonBulle;
    int scoreIA = 0;


    private LinearLayout layoutScores = null;
    private LinearLayout layoutActionBack = null;

    int layoutLargeur;
    int layoutHauteur;
    private Vector2D startVector;
    private Vector2D endVector;
    public Vector2D vectMouvement;
    private ProgressBar chronoBar;
    float randomT = 0 ;

    //-------


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_arrow_games);
        this.setPauseBouton();

        //-----


        sonBulle = new Sons(R.raw.bulle, this, "son");
        Typeface custom_font2 = Typeface.createFromAsset(getAssets(), "fonts/helsinki.ttf");
        tscore2 = (TextView) findViewById(R.id.viewScoreJoueur2);
        tscore2.setTypeface(custom_font2);
        tscore2.setTextSize(20);
        setHUD(0xFF000000);

        //------- Mode Multi


        if (!partieContreLaMontre){}

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

        //------- Temps


        chronoBar = (ProgressBar) findViewById(R.id.chronometre);
        maxTime = 1000 * temps;
        System.out.println("Max time : " + maxTime + " | Time : " + temps);
        tempsT = new Temps(20) {
            @Override
            public void update(){
                System.out.println("Max time : " + maxTime + " | Time : " + temps);
                if (layoutHauteur == 0 && layoutLargeur == 0)
                {
                    layoutHauteur = findViewById(R.id.layoutAction).getMeasuredWidth();
                    layoutLargeur = findViewById(R.id.layoutAction).getMeasuredHeight();
                }
                else
                {
                    updatePosition();
                    double ratio =  ((maxTime - tempsT.getTime()) / (double)maxTime);
                    chronoBar.setProgress((int) (ratio * 100d));
                    if (maxTime <= tempsT.getTime())
                    {
                        tempsEcoule = true;
                        Lose();

                    }
                }

                //------- Mode IA


                if(mode.equals("IA")) {
                    tscore2.setText("Score IA: "+Mode_courseAuxPoints.getScoreIA());
                }

            }
        };

        //-------


        listofArrow = new Arrow[] {Arrow.UP, Arrow.DOWN, Arrow.LEFT, Arrow.RIGHT};
        listofView = new ImageView[] {(ImageView) findViewById(R.id.imageViewA),
                (ImageView) findViewById(R.id.imageViewB),
                (ImageView) findViewById(R.id.imageViewC),
                (ImageView) findViewById(R.id.imageViewD)};
        for (ImageView imageView : listofView)
        {
            imageView.setVisibility(View.INVISIBLE);
        }
        makeSizeArrow();
        listofArrow = shuffleArrow(listofArrow);


        //--- Lancement du jeu


        if (partieContreLaMontre){partieDejaLancee = true;}
        if ((partieContreLaMontre)||(partieCourseAuxPoints)){
            findViewById(R.id.vie).setVisibility(View.INVISIBLE);
            findViewById(R.id.fois).setVisibility(View.INVISIBLE);
            findViewById(R.id.imageView).setVisibility(View.INVISIBLE);}

        //---- Mode IA

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

        //----

        init();
    }

    //-------

    @Override
    public void onStart(){
        super.onStart();
        ((TextView)findViewById(R.id.score)).setText(String.format("%02d",score));
        ((TextView)findViewById(R.id.vie)).setText(""+vie);


        listofArrow = shuffleArrow(listofArrow);
        setNumberArrowByLevel(difficulte);
        System.out.println("X : " + layoutLargeur);
        System.out.println("Y : " + layoutHauteur);

        for (int i = 0; i < listofArrow.length; i++)
        {
            listofView[i].setImageResource(listofArrow[i].img);
            if (i < nombreFleche)
            {
                listofView[i].setVisibility(View.VISIBLE);
            }
            else
            {
                listofView[i].setVisibility(View.INVISIBLE);
            }

        }

    }

    //-------

    @Override
    public void onPause() {
        super.onPause();
        tempsT.stop();

    }
    @Override
    public void onResume(){
        super.onResume();
        tempsT.resume();
    }

    //-------


    @Override
    public boolean onTouchEvent(MotionEvent e) {
        for (int id = 0; id < e.getPointerCount(); id++) {
            if (e.findPointerIndex(0) == e.getPointerId(0)) {

                if (e.getAction() == MotionEvent.ACTION_DOWN) {
                    startVector = new Vector2D(e.getX(), e.getY());
                }
                if (startVector != null && e.getAction() == MotionEvent.ACTION_UP) {
                    endVector = new Vector2D(e.getX(), e.getY());
                    vectMouvement = new Vector2D(startVector, endVector);
                }

                if (vectMouvement != null && vectMouvement.magnitude() > 150) {
                    Arrow res = findGest(vectMouvement);
                    if (res != null && gestureIsCorrect(res, this.listofArrow, this.nombreFleche)) {
                        Win();
                    } else {
                        Lose();
                    }
                }
            }
        }
        return true;
    }

    //-------


    public void makeSizeArrow()
    {
        for (ImageView iv : listofView)
        {
            iv.setMaxHeight((int)(layoutHauteur * 0.75f));
            iv.setMaxWidth((int)(layoutHauteur * 0.75f));
        }
    }

    //-------

    public boolean gestureIsCorrect(Arrow gest, Arrow[] listofArrow, int nombreArrow) {
        for (int i = 0; i < nombreFleche; i++) {
            if (gest.img == listofArrow[i].img) {
                return false;
            }
        }
        return true;
    }

    //------- On Click


    public void onClickPause(View v){

        Intent activity = new Intent(this, Pause.class);
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
            tempsT.stop();
            try{sonBulle.play();}catch(Exception e){}
            startActivityForResult(activity, 0);
        }
    }



    public void onClickResume(View v){

        tempsT.resume();
    }

    public void onClickQuit(View v){
        finish();
    }

    //-------

    public Arrow findGest(Vector2D vectMouvement) {
        double deltaT = 0.5;
        vectMouvement.normalized();
        Vector2D t = new Vector2D(100000, 100000);
        Arrow res = null;
        for (Arrow a : new Arrow[]{Arrow.UP, Arrow.LEFT, Arrow.RIGHT, Arrow.DOWN}) {
            Vector2D v = new Vector2D(vectMouvement, a.vect);
            if (t.magnitude() > v.magnitude() && v.magnitude() <= deltaT) {
                t = v;
                res = a;
            }
        }
        return res;
    }


    protected void finOperation() {
        if (!partieContreLaMontre){
            tempsT.reset();
            tempsT.stop();
        }

        finPartie(); //Methode commune à tout les mini-jeux => classe Mini-jeu

    }

    //-------


    public void Lose() {

        Perdu();
        finOperation();
    }


    public void Win() {
        difficulte++;
        Gagne();
        finOperation();

    }


    //-------


    public void setTimeByLevel(int level, long baseTime, float coefTimer){
        this.maxTime =  (int)(baseTime * Math.pow(coefTimer ,level - 1));
    }


    public void setNumberArrowByLevel(int level){
        if(level < 5) { this.nombreFleche = 1; }
        else if(level < 14) { this.nombreFleche = 2; }
        else { this.nombreFleche = 3; }
    }
    public void updatePosition(){
        for (int i = 0 ; i < listofArrow.length ; i ++)
        {
            float a = (layoutLargeur * 0.3f);
            float b = (layoutHauteur * 0.3f);
            Vector2D centre = new Vector2D(layoutLargeur * 0.35f,layoutHauteur * 0.35f );

            float speed = (difficulte < 17)? 0 : (difficulte / 20f);
            speed = speed * sens;
            float currentT = (tempsT.getTime() *0.001f) * speed;
            currentT = (float)((currentT + i * 0.5f * Math.PI) % (2* Math.PI));
            Vector2D pos = ellipseFunction(centre, a, b,randomT+currentT);

            listofView[i].setX((float)pos.getX());
            listofView[i].setY((float)pos.getY());
        }
    }
    public Vector2D ellipseFunction(Vector2D center, float a, float b, float t){
        float X = (float)(center.getX() + a * Math.cos(t));
        float Y = (float)(center.getY() + b * Math.sin(t));
        return new Vector2D(X, Y);
    }
    public Arrow[] shuffleArrow(Arrow[] liste){
        for (int i = 0 ; i < liste.length ; i ++)
        {
            Arrow tmp = liste[i];
            int j = (int)(Math.random() * liste.length);
            liste[i] = liste[j];
            liste[j] = tmp;
         }
        return liste;
    }


    //-------



    @Override
    public void onBackPressed(){
        backpressed = true;
        if (!(mode.equals("courseAuxPoints") || mode.equals("contreLaMontre"))) tempsT.destroy();
        sauver_et_quitter();

    }


    //-------



    @Override
    protected void init() {
        sens = (int)Math.pow(-1, (int)(Math.random() * 2));

        if ((partieContreLaMontre) && (partieDejaLancee)) {
            tempsT.reset();
            tempsT.resume();
            partieDejaLancee = false;
        }
        else if (!partieContreLaMontre){
            tempsT.reset();
            tempsT.resume();
        }
        makeSizeArrow();
        ((TextView)findViewById(R.id.score)).setText(String.format("%02d",score));
        ((TextView)findViewById(R.id.vie)).setText(""+vie);


        listofArrow = shuffleArrow(listofArrow);
        if (!partieContreLaMontre) {setTimeByLevel(difficulte, 5000, 0.985f);}
        setNumberArrowByLevel(difficulte);
        randomT = (float)((int)(Math.random() * 4) * Math.PI);
        for (int i = 0; i < listofArrow.length; i++)
        {
            listofView[i].setVisibility(View.INVISIBLE);
            listofView[i].setImageResource(listofArrow[i].img);
            if (i < nombreFleche)
            {
                listofView[i].setVisibility(View.VISIBLE);
            }

        }
        makeSizeArrow();
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
        tscore2.setText( "Score IA : " + scoreIA);
        J2 = "Score IA : " + scoreIA;
        if(tscore2.getVisibility() == View.GONE) tscore2.setVisibility(View.VISIBLE);

    }
}

