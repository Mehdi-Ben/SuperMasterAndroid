package jtb.supermaster.Mini_Jeux;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.widget.AppCompatTextView;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import io.socket.emitter.Emitter;
import jtb.supermaster.Menus.Menu_principal;
import jtb.supermaster.Menus.Pause;
import jtb.supermaster.Modes.Mode_courseAuxPoints;
import jtb.supermaster.R;
import jtb.supermaster.Serveur.Sockethor;

import static android.graphics.Color.WHITE;

public class Point_connection extends Mini_jeu implements ViewTreeObserver.OnGlobalLayoutListener, Emitter.Listener {

    Sockethor mSocket = new Sockethor();

    public class Point {

        private int id;
        private int x;
        private int y;
        private boolean nonvisible;
        private boolean validate;

        public int getX() {
            return x;
        }

        public void setX(int x) {
            this.x = x;
        }

        public int getY() {
            return y;
        }

        public void setY(int y) {
            this.y = y;
        }

        Point(int id, int x, int y, boolean nonvisible) {
            this.id = id;
            this.x = x;
            this.y = y;
            this.nonvisible = nonvisible;
            this.validate = false;
        }

    }


    public class MyTextView extends AppCompatTextView {
        Point_connection activity;

        MyTextView(Context context, Point p, Point_connection activity) {
            super(context);
            this.activity = activity;
            this.setBackgroundResource(R.drawable.asteroid);
            this.setId(p.id);
            this.setX(p.x);
            this.setY(p.y);
            this.setLayoutParams(new FrameLayout.LayoutParams(sizePoint, sizePoint));
            this.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            this.setText(String.valueOf(p.id));
            this.setTextColor(WHITE);
            Typeface custom_font1 = Typeface.createFromAsset(getAssets(), "fonts/Death Star.otf");
            this.setTypeface(custom_font1);
            this.setTextSize(20);
            this.setGravity(Gravity.CENTER);
            if (p.nonvisible) {
                this.setVisibility(View.INVISIBLE);
            }
        }

        @Override
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
        @Override
        public boolean onTouchEvent(MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    if (this instanceof TextView && ((Integer) this.getId() == 1)) {
                        final TextView l = (TextView) this;
                        listOfPoints.get((this.getId() - 1)).validate = true;
                        if (sonCorrect.vibrationsOn) {vb.vibrate(100);}
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                l.setBackgroundResource(R.drawable.goalasteroid);
                                l.setText("");
                            }
                        });

                        (new Sons(R.raw.boom, getApplicationContext(), "son")).play();
                    } else {
                       activity.perdu();
                    }
                    break;
                case MotionEvent.ACTION_MOVE:
                   if (this instanceof TextView && ((Integer) this.getId() == 1)) {
                        updatePoints(event.getX() + this.getX(), event.getY() + this.getY());
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    if (this instanceof TextView && ((Integer) this.getId() == 1)) {
                        if (listOfPoints.size() != 0 && !listOfPoints.get(activity.getDifficulte() - 1).validate) {
                            activity.perdu();
                        } else if(listOfPoints.size() != 0){
                            activity.verifGagne();
                        }else{
                            activity.perdu();
                        }
                    }
                    break;
            }
            return true;
        }

        // Test si les positions x et y correspondent à un point
        // si oui et si son prédécesseur est validé, le valide
        // si son prédécesseur n'est pas validé, la partie est perdue
        private void updatePoints(float x, float y) {
            ArrayList<TextView> listOfTextView = activity.getListOfTextView();
            ArrayList<Point> listOfPoints = activity.getListOfPoints();
            for (int i = 0; i < listOfPoints.size(); i++) {
                pTemp = listOfPoints.get(i);
                if (((x >= pTemp.x) && (x <= (pTemp.x + sizePoint))) && ((y >= pTemp.y) && (y <= (pTemp.y + sizePoint))) && (i + 1) != 1) {
                    if (!listOfPoints.get(i).validate && listOfPoints.get((i - 1)).validate) {
                        final ArrayList<TextView> lOfTextView = listOfTextView;
                        final int j = i;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                lOfTextView.get(j).setBackgroundResource(R.drawable.goalasteroid);
                                lOfTextView.get(j).setText("");
                            }
                        });

                        pTemp.validate = true;
                        if (sonCorrect.vibrationsOn){vb.vibrate(100);}
                        try{(new Sons(R.raw.boom, getApplicationContext(), "son")).play();}catch(Exception e){}
                    } else if (!listOfPoints.get(i - 1).validate) {
                        activity.perdu();
                    }
                }
            }
        }
    }

    /*Variables globales*/
    private Point pTemp;
    private int scoreIA = 0;
    private int nbToursLibre = 0;
    private int difficulteMax = 10;
    private ProgressBar chronoBar;
    Vibrator vb;
    Temps gameTimer;

    /*Variables globales taille*/
    private boolean MJ_libre;
    private int sizePoint = 200;
    private int widthPrinc;
    private int heightPrinc;

    /*Variables globales affichage*/
    private LinearLayout layoutScores = null;
    private LinearLayout layoutActionBack = null;

    private ArrayList<TextView> listOfTextView;
    private ArrayList<Point> listOfPoints;
    private RelativeLayout layout;
    private TextView text1;

    public int getDifficulte() {
        return this.difficulte;
    }

    public void setDifficulte(int d) {
        this.difficulte = d;
    }

    public ArrayList<Point> getListOfPoints() {
        return listOfPoints;
    }

    public void setListOfPoints(ArrayList<Point> listOfPoints) {
        this.listOfPoints = listOfPoints;
    }

    public RelativeLayout getLayout() {
        return layout;
    }

    public void setLayout(RelativeLayout layout) {
        this.layout = layout;
    }

    public ArrayList<TextView> getListOfTextView() {
        return listOfTextView;
    }

    public int getScore() {
        return this.score;
    }

    public int getVies() {
        return vie;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_point_connection);

        this.setPauseBouton();

        vb = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        Typeface custom_font2 = Typeface.createFromAsset(getAssets(), "fonts/helsinki.ttf");
        tscore2 = (TextView) findViewById(R.id.viewScoreJoueur2);
        tscore2.setTypeface(custom_font2);
        tscore2.setTextSize(20);

        if (!partieContreLaMontre) {
            temps = 6;
        }
        //Si on est en mode en multijoueur, on affiche les TextView des meilleurs scores a l'ecran
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

            //On modifie le poids du layout de jeu en consequence
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

        layout = (RelativeLayout) findViewById(R.id.ralativeLayout);

            final FrameLayout layout2 = (FrameLayout) findViewById(R.id.layoutPrinc);
            ViewTreeObserver vto = layout2.getViewTreeObserver();
            vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                        layout2.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                    } else {
                        layout2.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    }

                    /* Récupération de la taille du layout principal */

                    widthPrinc = layout2.getMeasuredWidth();
                    heightPrinc = layout2.getMeasuredHeight();
                    sizePoint = widthPrinc / 6;

                    /* Modification de l'interface en fonction du mode de jeu */

                    if (partieContreLaMontre) {
                        MJ_libre = true;
                    }

                    if ((MJ_libre) || (partieCourseAuxPoints)) {
                        findViewById(R.id.vie).setVisibility(View.INVISIBLE);
                        findViewById(R.id.fois).setVisibility(View.INVISIBLE);
                        findViewById(R.id.imageView).setVisibility(View.INVISIBLE);
                    }

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

                    /* Modification de l'interface superieur de l'ecran */

                    setHUD(0xFFFFFFFF);

                    /* Initialisation de la partie */

                    init();

                }
            });

         /* Barre du chrono */

            chronoBar = (ProgressBar) findViewById(R.id.chronometre);
            int duree = temps; // Temps voulu en secondes
            final long delay = 1000 * duree;
            gameTimer = new Temps(50) {
            boolean aller = true;

            @Override
            public void update(){
                double ratio = (double) (delay - gameTimer.getTime()) / delay;
                if (ratio >= 0)
                {
                    chronoBar.setProgress((int) (ratio * 100d));
                }
                else {
                    onFinish();
                }

                if(mode.equals("IA")) {
                    try{tscore2.setText("Score IA: "+Mode_courseAuxPoints.getScoreIA());}catch(Exception e){}
                }
            }

            public void onFinish() {
                    tempsEcoule = true;
                    perdu();

            }
        };
            gameTimer.resume();

        }

        /* Initialisation de la partie */

    protected void init() {

        this.listOfTextView = new ArrayList<TextView>();
        this.listOfPoints = new ArrayList<Point>();

        /* Mise à jour des TextView de point et de vis */
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ((TextView)findViewById(R.id.score)).setText(String.format("%02d",score));
                ((TextView)findViewById(R.id.vie)).setText(""+vie);
            }
        });

        if (!(partieContreLaMontre)) {
            gameTimer.reset();
        }

        if (this.difficulte == 0) {
            this.difficulte = 1;
        }
        if (!finPartie) {
            gestionDifficulte();
        }
        this.difficulte = Math.max(this.difficulte, 1);
        this.difficulte = Math.min(this.difficulte, this.difficulteMax);

        /* Mise a jour des objets visibles dans le layout de jeu */
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                layout.removeAllViews();
                addPoint();
            }
        });

        if ((partieContreLaMontre) && (MJ_libre)) {
            chronoBar.setProgress(100);
            gameTimer.resume();
            MJ_libre = false;
        } else if (!partieContreLaMontre) {
            chronoBar.setProgress(100);
            gameTimer.resume();
        }
    }

    /* Gestion de l'augmentation progressive de la difficulte */
    public void gestionDifficulte() {
        if (nbToursLibre < 3) {
            nbToursLibre++;
        } else {
            this.difficulte++;
            nbToursLibre = 1;
        }
    }

    /* Gestion de l'appui sur le bouton retour arriere */
    @Override
    public void onBackPressed() {
        backpressed = true;
        if (!(mode.equals("courseAuxPoints") || mode.equals("contreLaMontre"))) gameTimer.destroy();
        sauver_et_quitter();
    }

    /* Verification de victoire de la partie */
    private void verifGagne() {
        if (listOfPoints.get(this.difficulte - 1).validate) {
            Gagne();
            finPartie();
        }
    }

    /* Appelee en cas de perte de la partie */
    private void perdu() {
        Perdu();
        finPartie();

    }


    /* Creation et ajout dynamique des Views sur le layout de jeu en fonction de la difficulte */
    public void addPoint() {
        for (int i = 0; i < this.difficulte; i++) {
            Point p = new Point(i + 1, 0, 0, false);
            randomStart(p);

            /* Boucle permettant d'empecher la superposition des Views dans le layout de jeu */
            boolean presencePoint = true;
            do {
                randomStart(p);
                if (p.id != 1) {
                    for (int j = 0; j < i; j++) {
                        Point pTest = listOfPoints.get(j);
                        presencePoint = true;
                        if ((p.x > (pTest.x + sizePoint)) || (p.x < (pTest.x - sizePoint)) || (p.y > (pTest.y + sizePoint)) || (p.y < (pTest.y - sizePoint))) {
                            presencePoint = false;
                            continue;
                        } else {
                            break;
                        }
                    }
                } else {
                    presencePoint = false;
                }
            } while (presencePoint);


            text1 = new MyTextView(this, p,this);
            listOfTextView.add(text1);
            listOfPoints.add(p);
            getLayout().addView(text1);
        }
    }

    /* Calcul de coordonnees aleatoire comprise dans le layout du jeu */
    private void randomStart(Point p) {
        p.x = (int) (Math.abs(Math.random() * (this.widthPrinc - sizePoint)));
        p.y = (int) (Math.abs(Math.random() * (this.heightPrinc - sizePoint - 200)));
    }

    @Override
    public void onGlobalLayout() {
    }

    /* Actualise les scores en mode multijoueur */
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
    /* Gestion de l'affichage du score de l'IA sur le layout du jeu */
    public void AffichageScoreIA() {
        scoreIA = Integer.parseInt(getIntent().getStringExtra("ScoreIA"));
        tscore2.setText( "Score IA : " + scoreIA);
        J2 = "Score IA : " + scoreIA;
        if(tscore2.getVisibility() == View.GONE) tscore2.setVisibility(View.VISIBLE);

    }

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

    /* Gestion de la mise en pause du jeu */
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
