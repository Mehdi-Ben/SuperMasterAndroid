package jtb.supermaster.Menus;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import jtb.supermaster.R;

public class Regles extends Activity {

    //---- Variables globales


    ImageView titreActivity;

    TextView titreEnigme;
    TextView titreBasket;
    TextView titreCouleur;
    TextView titreDemineur;
    TextView titreFleche;
    TextView titreEtoile;


    //----------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regles);

        //---- Modifications police

        titreActivity = (ImageView)findViewById(R.id.TitreActivity);

        titreBasket = (TextView)findViewById(R.id.TitreCouleur);
        setFont(titreBasket,"helsinki.ttf");
        titreCouleur = (TextView)findViewById(R.id.TitreBaskets);
        setFont(titreCouleur,"helsinki.ttf");
        titreDemineur = (TextView)findViewById(R.id.TitreDemineur);
        setFont(titreDemineur,"helsinki.ttf");
        titreEnigme = (TextView)findViewById(R.id.TitreEnigme);
        setFont(titreEnigme,"helsinki.ttf");
        titreFleche = (TextView)findViewById(R.id.TitreFleches);
        setFont(titreFleche,"helsinki.ttf");
        titreEtoile = (TextView)findViewById(R.id.TitreAsteroid);
        setFont(titreEtoile,"helsinki.ttf");
    }

    public void setFont(TextView textView, String fontName) {
        if(fontName != null){
            try {
                Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/" + fontName);
                textView.setTypeface(typeface);
            } catch (Exception e) {
                Log.e("FONT", fontName + " not found", e);
            }
        }
    }

    //----------

    @Override
    public void onBackPressed(){
        this.onResume();
        Intent activity = new Intent(this, Menu_principal.class);
        startActivity(activity);
        finish();
        super.onBackPressed();
    }

}
