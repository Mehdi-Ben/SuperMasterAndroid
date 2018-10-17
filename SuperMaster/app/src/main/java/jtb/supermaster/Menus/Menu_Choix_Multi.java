package jtb.supermaster.Menus;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.PowerManager;
import android.view.View;
import android.widget.TextView;

import jtb.supermaster.Mini_Jeux.Sons;
import jtb.supermaster.Modes.Mode_multijoueurs;
import jtb.supermaster.R;

public class Menu_Choix_Multi extends Activity implements View.OnClickListener {


    //-------- Variables globales

    private TextView mode;
    private TextView local;
    private TextView ligne;
    private int couleurTexte;
    private int tailleTexte;
    private Sons sonMJ;


    //---------- OnCreate

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_choix_multi);
        sonMJ = new Sons(R.raw.boxeur, this, "music");


        local = (TextView) findViewById(R.id.Local);
        local.setOnClickListener(this);

        ligne = (TextView) findViewById(R.id.Ligne);
        ligne.setOnClickListener(this);


        couleurTexte = 0xFF1a237e;
        tailleTexte = 25;
        Typeface custom_font1 = Typeface.createFromAsset(getAssets(),  "fonts/helsinki.ttf");

        local.setTypeface(custom_font1);
        local.setTextColor(couleurTexte);
        local.setTextSize(tailleTexte);

        ligne.setTypeface(custom_font1);
        ligne.setTextColor(couleurTexte);
        ligne.setTextSize(tailleTexte);

    }


    //---------- OnClick



    @Override
    public void onClick(View v) {

        if (v.getId() == local.getId()) {
            Intent activity = new Intent(this, Menu_Multi_IA.class);
            startActivity(activity);
        }

        if (v.getId() == ligne.getId()) {
            try{sonMJ.getListeSons().get(0).stop();}catch (Exception e){}
            Intent activity = new Intent(this, Mode_multijoueurs.class);
            startActivity(activity);
        }

    }


    //---------- OnBack


    @Override
    public void onBackPressed() {
        this.onResume();
        Intent activity = new Intent(this, Menu_principal.class);
        startActivity(activity);
        finish();
        super.onBackPressed();
    }


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
            try{
            if (sonMJ!= null && sonMJ.getListeSons().get(0).getSon().isPlaying())
                try{sonMJ.getListeSons().get(0).stop();}catch (Exception e){}}catch (Exception e){}
    }

}
