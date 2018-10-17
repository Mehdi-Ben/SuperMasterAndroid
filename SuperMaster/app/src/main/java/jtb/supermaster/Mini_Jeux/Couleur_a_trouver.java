package jtb.supermaster.Mini_Jeux;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;

import android.os.CountDownTimer;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import jtb.supermaster.Menus.Menu_principal;
import jtb.supermaster.Menus.Pause;
import jtb.supermaster.Modes.Mode_courseAuxPoints;
import jtb.supermaster.R;


public class Couleur_a_trouver extends Mini_jeu implements View.OnClickListener {



    //----- Classe couleur => Chaque couleur a un nom et un entier color associe

    class Couleur {
        String str;
        int color;

        Couleur(String str, int clr) {
            this.str = str;
            this.color = clr;}

    }


    //----- Variables globales

    private LinearLayout layoutScores = null;
    private LinearLayout layoutActionBack = null;

    //------- Parties Propres au mini-jeu -----//

    private int nombreTextView = 14;
    private TextView[] listofTextView = new TextView[nombreTextView];
    private int nombreCouleur = 2; // Nombre de couleurs affichee sur l'ecran
    private TextView texteFin = null;
    private TextView zoneCouleur1;
    private TextView zoneCouleur2;
    private TextView zoneCouleur3;
    private TextView zoneCouleur4;
    private TextView zoneCouleur5;
    private TextView zoneCouleur6;
    private TextView zoneCouleur7;
    private TextView zoneCouleur8;
    private TextView zoneCouleur9;
    private TextView zoneCouleur10;
    private TextView zoneCouleur11;
    private TextView zoneCouleur12;
    private TextView zoneCouleur13;
    private TextView zoneCouleur14;

    private int IDTextViewCorrect;
    boolean partieDejaLancee = true;
    boolean partieMultiJoueurs = false;


    private TextView IDCorrect;
    private Couleur[] listeCouleurs = new Couleur[] { //Liste des couleurs
            new Couleur("Bleu", 0xFF1565c0),
            new Couleur("Rouge", Color.RED),
            new Couleur("Rose", Color.MAGENTA),
            new Couleur("Jaune", Color.YELLOW),
            new Couleur("Cyan", Color.CYAN),
            new Couleur("Vert", 0xFF4caf50),
            new Couleur("Noir", Color.BLACK),
            new Couleur ("Violet", 0xFF8e24aa ),
            new Couleur ("Orange", 0xFFff5722 )

    };

    private int idTextViewCorrect = 0;
    private int idCouleurCorrecte = 0;

    private int nbToursLibre = 0;


    private String resultat = null; // Sert à savoir quand le jeu est fini et le résultat du joueur

    //------- Parties Communes -----//

    private ProgressBar chronoBar;
    private Toast toast = null;
    private Boolean aRepondu = true;
    Temps tempsT;
    int maxTime = 1000 * temps;
    int scoreIA = 0;

    //------------------------------//



    public void InitialisationListofTextView(){
        listofTextView = new TextView[]{
                (TextView) findViewById(R.id.ZoneCouleur1),
                (TextView) findViewById(R.id.ZoneCouleur2),
                (TextView) findViewById(R.id.ZoneCouleur3),
                (TextView) findViewById(R.id.ZoneCouleur4),
                (TextView) findViewById(R.id.ZoneCouleur5),
                (TextView) findViewById(R.id.ZoneCouleur6),
                (TextView) findViewById(R.id.ZoneCouleur7),
                (TextView) findViewById(R.id.ZoneCouleur8),
                (TextView) findViewById(R.id.ZoneCouleur9),
                (TextView) findViewById(R.id.ZoneCouleur10),
                (TextView) findViewById(R.id.ZoneCouleur11),
                (TextView) findViewById(R.id.ZoneCouleur12),
                (TextView) findViewById(R.id.ZoneCouleur13),
                (TextView) findViewById(R.id.ZoneCouleur14),

        };
    }

    //------ Getters


    public boolean getFinPartie() {
        return finPartie;
    }

    public void setFinPartie(boolean finPartie) {
        this.finPartie = finPartie;
    }

    public Couleur[] getListeCouleurs() {
        return listeCouleurs;
    }

    public TextView getTexteFin() {
        return texteFin;
    }

    public Boolean getaRepondu() {
        return aRepondu;
    }

    public String getResultat() {
        return resultat;
    }

    public int getNombreCouleur() {
        return nombreCouleur;
    }

    public int getNombreTextView() {
        return nombreTextView;
    }

    public TextView getIDCorrect() {
        return IDCorrect;
    }

    public TextView[] getListofTextView() {
        return listofTextView;
    }

    public int getIdTextViewCorrect() {
        return idTextViewCorrect;
    }

    public int getIdCouleurCorrecte() {
        return idCouleurCorrecte;
    }

    public int getScore(){ return this.score; }

    public int getVie(){return this.vie;}

    public int getIDTextViewCorrect() {
        return IDTextViewCorrect;
    }



    //----- Fonction de comparaison de couleurs

    // => Sert à savoir si une string correspond bien à l'entier couleur associé dans la liste des couleurs

    public boolean comparaisonCouleurString(String str, int couleur ){
        for(int i = 0; i < listeCouleurs.length; i++ ) {
            if (str == listeCouleurs[i].str && couleur == listeCouleurs[i].color ) {
                return true;
            }

        }
        return false;
    }


    //-----


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_couleur_a_trouver);


        //------- Mode IA


        Typeface custom_font2 = Typeface.createFromAsset(getAssets(), "fonts/helsinki.ttf");
        tscore2 = (TextView) findViewById(R.id.viewScoreJoueur2);
        tscore2.setTypeface(custom_font2);
        tscore2.setTextSize(20);


        //------- Mode Multi



        this.setPauseBouton();
        if (!partieContreLaMontre){maxTime = 5000;}
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


        //----- Initialisation des TextView et association à leurs zones couleurs respectives


        InitialisationListofTextView();

        texteFin = (TextView) findViewById(R.id.texteFin);

        zoneCouleur1 = (TextView) findViewById(R.id.ZoneCouleur1);
        zoneCouleur1.setOnClickListener(this);

        zoneCouleur2 = (TextView) findViewById(R.id.ZoneCouleur2);
        zoneCouleur2.setOnClickListener(this);

        zoneCouleur3 = (TextView) findViewById(R.id.ZoneCouleur3);
        zoneCouleur3.setOnClickListener(this);

        zoneCouleur4 = (TextView) findViewById(R.id.ZoneCouleur4);
        zoneCouleur4.setOnClickListener(this);

        zoneCouleur5 = (TextView) findViewById(R.id.ZoneCouleur5);
        zoneCouleur5.setOnClickListener(this);

        zoneCouleur6 = (TextView) findViewById(R.id.ZoneCouleur6);
        zoneCouleur6.setOnClickListener(this);

        zoneCouleur7 = (TextView) findViewById(R.id.ZoneCouleur7);
        zoneCouleur7.setOnClickListener(this);

        zoneCouleur8 = (TextView) findViewById(R.id.ZoneCouleur8);
        zoneCouleur8.setOnClickListener(this);

        zoneCouleur9 = (TextView) findViewById(R.id.ZoneCouleur9);
        zoneCouleur9.setOnClickListener(this);

        zoneCouleur10 = (TextView) findViewById(R.id.ZoneCouleur10);
        zoneCouleur10.setOnClickListener(this);

        zoneCouleur11 = (TextView) findViewById(R.id.ZoneCouleur11);
        zoneCouleur11.setOnClickListener(this);

        zoneCouleur12 = (TextView) findViewById(R.id.ZoneCouleur12);
        zoneCouleur12.setOnClickListener(this);

        zoneCouleur13 = (TextView) findViewById(R.id.ZoneCouleur13);
        zoneCouleur13.setOnClickListener(this);

        zoneCouleur14 = (TextView) findViewById(R.id.ZoneCouleur14);
        zoneCouleur14.setOnClickListener(this);


        //----- Police


        Typeface custom_font1 = Typeface.createFromAsset(getAssets(),  "fonts/marshmallowsandchocolate.ttf");


        zoneCouleur1.setTypeface(custom_font1);
        zoneCouleur2.setTypeface(custom_font1);
        zoneCouleur3.setTypeface(custom_font1);
        zoneCouleur4.setTypeface(custom_font1);
        zoneCouleur5.setTypeface(custom_font1);
        zoneCouleur6.setTypeface(custom_font1);
        zoneCouleur7.setTypeface(custom_font1);
        zoneCouleur8.setTypeface(custom_font1);
        zoneCouleur9.setTypeface(custom_font1);
        zoneCouleur10.setTypeface(custom_font1);
        zoneCouleur11.setTypeface(custom_font1);
        zoneCouleur12.setTypeface(custom_font1);
        zoneCouleur13.setTypeface(custom_font1);
        zoneCouleur14.setTypeface(custom_font1);


        //---- Vies

        ((TextView)findViewById(R.id.vie)).setText(""+vie);

        //--- Chrono

        chronoBar = (ProgressBar) findViewById(R.id.chronometre);
        tempsT = new Temps(50) {
            @Override
            public void update(){
                    double ratio =  ((maxTime - tempsT.getTime()) / (double)maxTime);
                    chronoBar.setProgress((int) (ratio * 100d));
                    if (maxTime <= tempsT.getTime())
                    {
                       // Perdu();
                        tempsEcoule = true;
                        finOperation(false);
                    }


                if(mode.equals("IA")) {

                    tscore2.setText("Score IA: "+Mode_courseAuxPoints.getScoreIA());
                }


            }
        };


        //------- Mode IA


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

        //--- Lancement du jeu
        if (partieContreLaMontre){partieMultiJoueurs = true;}
        if ((partieContreLaMontre)||(partieCourseAuxPoints)){
            findViewById(R.id.vie).setVisibility(View.INVISIBLE);
            findViewById(R.id.fois).setVisibility(View.INVISIBLE);
            findViewById(R.id.imageView).setVisibility(View.INVISIBLE);}
        setHUD(0xFF000000);
        init();

    }



    //----- Temps


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


    //----- Bouton retour


    @Override
    public void onBackPressed(){
        backpressed = true;
        if (!(mode.equals("courseAuxPoints") || mode.equals("contreLaMontre"))) tempsT.destroy();
        sauver_et_quitter();
    }


    //----- Fonction Affichage => On affiche le bon nombre de couleurs sur l'ecran

    private void Affichage() {
        System.out.println(listofTextView.length);
        for(int i = 0; i < listofTextView.length; i ++) {
            if( i < nombreCouleur) {
                System.out.println(listofTextView[i]);
                listofTextView[i].setVisibility(View.VISIBLE);
            } else {
                listofTextView[i].setVisibility(View.GONE);
            }
        }

        texteFin.setVisibility(View.VISIBLE);

    }


    //----- Initialisation


    private void CouleurDifficulte(int nb) { //Utilisee dans GestionDifficulte


        if (difficulte == 0) { //Toujours au moins 2 couleurs
            nombreCouleur = 2;
        }

        else{

            if((nb + difficulte) < 14) {
                nombreCouleur = nb + difficulte;
            }

            else { //Au max 14 couleurs
                nombreCouleur = 14;
            }
       }

    }



    public void GestionDifficulte(){

        if(finPartie) { //Mode Classique

                CouleurDifficulte(1);

        }

        else { //Mode Libre

            if(nbToursLibre < 3) {
                CouleurDifficulte(2);
                nbToursLibre++;
            }

            else{
                difficulte++;
                nbToursLibre = 1;
                CouleurDifficulte(2);
            }

        }
    }


    //----------


    public void init()

    {
        ((TextView)findViewById(R.id.score)).setText(String.format("%02d",score));
        ((TextView)findViewById(R.id.vie)).setText(""+vie);
        if ((partieContreLaMontre) && (partieDejaLancee)) {
            tempsT.reset();
            tempsT.resume();
            partieDejaLancee = false;
        }
        else if (!partieContreLaMontre){
            tempsT.reset();
            tempsT.resume();
        }

        GestionDifficulte();

        //chronoBar.setProgress(100); //Prepartion du chrono
        Affichage(); //Affichage de nos couleurs
        resultat = null; //Remise à null du résultat

        //Creation aléatoire d'id corrects

        idTextViewCorrect = (int) (Math.random() * nombreCouleur);
        idCouleurCorrecte = (int) (Math.random() * listeCouleurs.length);


        for (int i = 0; i < nombreCouleur; i++) {
            if (i == idTextViewCorrect) { // Si l'id est correct alors on associe la bonne str couleur au bon int couleur

                listofTextView[i].setText(listeCouleurs[idCouleurCorrecte].str);
                listofTextView[i].setTextColor(listeCouleurs[idCouleurCorrecte].color);

            } else {

                int a = (int) (Math.random() * listeCouleurs.length); //Pioche Couleur Aleatoire
                int b;
                do {
                    b = (int) (Math.random() * listeCouleurs.length); //Pioche autre couleur
                } while (a == b); // Tant que les couleurs ne sont pas differentes

                listofTextView[i].setText(listeCouleurs[a].str); //Prend le nom de a
                listofTextView[i].setTextColor(listeCouleurs[b].color); // et la couleur de B
            }
        }

        IDTextViewCorrect = idTextViewCorrect;
        IDCorrect = listofTextView[idTextViewCorrect]; //Recuperation du bon ID dans la liste des TextView
        if ((partieContreLaMontre) && (partieMultiJoueurs)) {
            chronoBar.setProgress(100);
            tempsT.resume();
            partieMultiJoueurs = false;
        }

        else if (!partieContreLaMontre){
            tempsT.reset();
            chronoBar.setProgress(100);
            tempsT.resume();
        }

    }


    //---- Fin de Partie


    protected void finOperation(boolean juste){
        if (!partieContreLaMontre)
            tempsT.stop();

        if(juste){ //Si le joueur a gagné, on incremente le score et on envoie un toast
            Gagne();
        }

        else{ //Sinon on envoie un autre toast
            if(finPartie){ //Si Mode classique
                ((TextView)findViewById(R.id.vie)).setText(""+vie);
            }
            Perdu();
            ((TextView)findViewById(R.id.vie)).setText(""+vie);

        }
        if ((partieContreLaMontre) && (tempsEcoule)) {tempsT.reset(); tempsT.stop(); finPartie();}
        else if ((partieContreLaMontre) && (!tempsEcoule)){init();}
        else {tempsT.reset(); finPartie();}

    }


    //---- Methode onClick

    @Override
    public void onClick(View v) {
        if(v.getId() == IDCorrect.getId()) { // Si on a clique sur le bon mot
            resultat = "Victoire";
            finOperation(true);

        }


        else { // Si mauvaise couleur
            resultat = "Defaite";
            finOperation(false);

        }

    }


    //------- Mode Multi


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

    //------- Mode IA


    public void AffichageScoreIA() {
        scoreIA = Integer.parseInt(getIntent().getStringExtra("ScoreIA"));
        tscore2.setText( "Score IA : " + scoreIA);
        J2 = "Score IA : " + scoreIA;
        if(tscore2.getVisibility() == View.GONE) tscore2.setVisibility(View.VISIBLE);

    }


    //------- Mode Multi


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
            tempsT.stop();
            Intent activity = new Intent(this, Pause.class);
            //try{sonBulle.play();}catch(Exception e){}
            startActivityForResult(activity, 0);
        }
    }

}
