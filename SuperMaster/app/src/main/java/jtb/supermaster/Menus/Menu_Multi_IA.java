package jtb.supermaster.Menus;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.PowerManager;
import android.view.View;
import android.widget.TextView;

import java.util.Random;

import jtb.supermaster.Mini_Jeux.Mini_jeu;
import jtb.supermaster.Mini_Jeux.Sons;
import jtb.supermaster.Mini_Jeux.Temps;
import jtb.supermaster.Modes.Mode_contreLaMontre;
import jtb.supermaster.Modes.Mode_courseAuxPoints;
import jtb.supermaster.R;
import jtb.supermaster.Serveur.Super_master_config;

import static android.graphics.Color.GREEN;

public class Menu_Multi_IA extends Activity implements View.OnClickListener{


    //------- Variables Globales


    private TextView facile;
    private TextView moyen;
    private TextView difficile;
    private TextView niveau;
    private TextView valider;


    private int couleurTexte;
    private int tailleTexte;
    private int niveauDifficulteIA = 0;
    private boolean IA = false;
    private boolean defaut = true;

    private Sons sonMJ;



    //------- Calcul des probas pour l'IA



    public static int calculProba(int difficulte) {

        // On tire parmi 100 nombres
        //Si difficile => difficulte = 85
        //Si moyen => difficulte = 75
        //Si facile => difficulte = 65

        int i = new Random().nextInt(100);
        if (i > difficulte) {
            return -1; //IA perd un point
        } else {
            return 1; //IA gagne un point
        }
    }


    //------- On Create


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_multi_ia);


        //------- TextView et Modifs police


        valider = (TextView) findViewById(R.id.Valider);
        valider.setOnClickListener(this);

        facile = (TextView) findViewById(R.id.IAFacile);
        facile.setOnClickListener(this);

        moyen = (TextView) findViewById(R.id.IAMoyen);
        moyen.setOnClickListener(this);

        difficile = (TextView) findViewById(R.id.IADifficile);
        difficile.setOnClickListener(this);


        couleurTexte = 0xFF1a237e;
        tailleTexte = 25;
        Typeface custom_font1 = Typeface.createFromAsset(getAssets(),  "fonts/helsinki.ttf");

        facile.setTypeface(custom_font1);
        facile.setTextColor(couleurTexte);
        facile.setTextSize(tailleTexte);


        moyen.setTypeface(custom_font1);
        moyen.setTextColor(couleurTexte);
        moyen.setTextSize(tailleTexte);

        difficile.setTypeface(custom_font1);
        difficile.setTextColor(couleurTexte);
        difficile.setTextSize(tailleTexte);

        valider.setTypeface(custom_font1);
        valider.setTextColor(couleurTexte);
        valider.setTextSize(35);

        sonMJ = new Sons(R.raw.boxeur, this, "music");

    }


    //-------


    public void removeCouleursNiveau(){ //Remet a 0 les couleurs quand on clique sur une difficulte differente
        facile.setTextColor(couleurTexte);
        moyen.setTextColor(couleurTexte);
        difficile.setTextColor(couleurTexte);
    }

    public void IntentValider() { //Intent envoyes quand on valide

        Intent activityMenu = new Intent(this, Super_master_config.class);
        activityMenu.putExtra("mode", "IA");
        activityMenu.putExtra("difficulteIA", ""+niveauDifficulteIA);
        startActivity(activityMenu);
    }

    //----- OnCLick


    @Override
    public void onClick(View v) {
        IA = true;
        if (v.getId() == facile.getId()) {
            niveauDifficulteIA = 65;
            removeCouleursNiveau();
            facile.setTextColor(Color.MAGENTA);
            defaut = false;
        }

        if (v.getId() == moyen.getId()) {
            niveauDifficulteIA = 75;
            removeCouleursNiveau();
            moyen.setTextColor(Color.MAGENTA);
            defaut = false;
        }

        if (v.getId() == difficile.getId()) {
            niveauDifficulteIA = 85;
            removeCouleursNiveau();
            difficile.setTextColor(Color.MAGENTA);
            defaut = false;
        }


        if (v.getId() == valider.getId()) {
            if(defaut) { //Si le joueur n'a rien selectionne mais valide
                niveauDifficulteIA = 75;
                IntentValider();
            }

            else {

                IntentValider();
            }

        }

    }



    //-------


    @Override
    public void onResume()
    {
        try{
            if (sonMJ.getListeSons().get(0)!=null && (!sonMJ.getListeSons().get(0).getSon().isPlaying()))
                sonMJ.getListeSons().get(0).play();}catch(Exception e){}
        super.onResume();

    }

    @Override
    public void onPause()
    {
        super.onPause();
        PowerManager mPowerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);

        if (!mPowerManager.isScreenOn())
            if (sonMJ!= null && sonMJ.getListeSons().get(0).getSon().isPlaying())
                try{sonMJ.getListeSons().get(0).stop();}catch (Exception e){}
    }

}