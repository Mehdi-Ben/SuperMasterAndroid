package jtb.supermaster.Menus;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import io.socket.emitter.Emitter;
import jtb.supermaster.Mini_Jeux.Mini_jeu;
import jtb.supermaster.Mini_Jeux.Sons;
import jtb.supermaster.R;
import jtb.supermaster.Serveur.Sockethor;

public class Score extends Activity implements Emitter.Listener {


    //---- Variables globales

    private TextView basket = null;
    private TextView enigmes = null;
    private TextView demineur = null;
    private TextView couleurs = null;
    private TextView points = null;
    private TextView fleches = null;
    private TextView classique = null;

    private TextView tbasket = null;
    private TextView tenigmes = null;
    private TextView tdemineur = null;
    private TextView tcouleurs = null;
    private TextView tpoints = null;
    private TextView tfleches = null;
    private TextView tclassique = null;

    public Sons sonCraie;

    private int sizeOfFont = 20;

    Sockethor mSocket = null;

    //----------


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);

        //----- Serveur

        mSocket = new Sockethor();
        mSocket.abonnement(this);

        //------ Sons


        sonCraie = new Sons(R.raw.craie, getApplicationContext(), "son");
        try{sonCraie.play();}catch (Exception e){}

        //------ TextView

        basket = (TextView) findViewById(R.id.basket);
        enigmes =(TextView) findViewById(R.id.enigmes);
        demineur = (TextView) findViewById(R.id.demineur);
        couleurs = (TextView) findViewById(R.id.couleurs);
        points = (TextView) findViewById(R.id.points);
        fleches = (TextView) findViewById(R.id.fleches);
        classique = (TextView) findViewById(R.id.classique);

        tbasket = (TextView) findViewById(R.id.tbasket);
        tenigmes =(TextView) findViewById(R.id.tenigmes);
        tdemineur = (TextView) findViewById(R.id.tdemineur);
        tcouleurs = (TextView) findViewById(R.id.tcouleurs);
        tpoints = (TextView) findViewById(R.id.tpoints);
        tfleches = (TextView) findViewById(R.id.tfleches);
        tclassique = (TextView) findViewById(R.id.tclassique);

        TextView mbasket = (TextView) findViewById(R.id.mbasket);
        TextView menigmes =(TextView) findViewById(R.id.menigmes);
        TextView mdemineur = (TextView) findViewById(R.id.mdemineur);
        TextView mcouleurs = (TextView) findViewById(R.id.mcouleurs);
        TextView mpoints = (TextView) findViewById(R.id.mpoints);
        TextView mfleches = (TextView) findViewById(R.id.mfleches);
        TextView mclassique = (TextView) findViewById(R.id.mclassique);

        TextView rbasket = (TextView) findViewById(R.id.rbasket);
        TextView renigmes =(TextView) findViewById(R.id.renigmes);
        TextView rdemineur = (TextView) findViewById(R.id.rdemineur);
        TextView rcouleurs = (TextView) findViewById(R.id.rcouleurs);
        TextView rpoints = (TextView) findViewById(R.id.rpoints);
        TextView rfleches = (TextView) findViewById(R.id.rfleches);
        TextView rclassique = (TextView) findViewById(R.id.rclassique);


        //------


        String[] fichiers = new String[]{"basket.txt", "enigmes.txt", "demineur.txt", "couleurs.txt", "points.txt", "fleches.txt", "classique.txt"};
        TextView[] views = new TextView[]{basket, enigmes, demineur, couleurs, points, fleches, classique};
        TextView[] tviews = new TextView[]{tbasket, tenigmes, tdemineur, tcouleurs, tpoints, tfleches, tclassique};
        TextView[] mviews = new TextView[]{mbasket, menigmes, mdemineur, mcouleurs, mpoints, mfleches, mclassique};
        TextView[] rviews = new TextView[]{rbasket, renigmes, rdemineur, rcouleurs, rpoints, rfleches, rclassique};
        Typeface custom_font = Typeface.createFromAsset(getAssets(),  "fonts/helsinki.ttf");

        //------


        ((TextView) findViewById(R.id.local)).setTextSize(sizeOfFont-5);
        ((TextView) findViewById(R.id.local)).setTypeface(custom_font);

        ((TextView) findViewById(R.id.meilleurScore)).setTextSize(sizeOfFont-5);
        ((TextView) findViewById(R.id.rang)).setTextSize(sizeOfFont-5);

        ((TextView) findViewById(R.id.meilleurScore)).setTypeface(custom_font);
        ((TextView) findViewById(R.id.rang)).setTypeface(custom_font);
        for (int i = 0; i < 7; i++) {
            String stringScore = (readFromFile(fichiers[i]).equals(""))? "00" : String.format("%02d",Integer.parseInt(readFromFile(fichiers[i])));
            views[i].setText(stringScore);
            views[i].setTextSize(sizeOfFont);
            views[i].setTypeface(custom_font);
            tviews[i].setTypeface(custom_font);
            tviews[i].setTextSize(sizeOfFont);
            mviews[i].setTypeface(custom_font);
            mviews[i].setTextSize(sizeOfFont);
            rviews[i].setTypeface(custom_font);
            rviews[i].setTextSize(sizeOfFont);
        }
        updateScore();
    }

    //-------

    private String readFromFile(String file) {
        Context context = getApplicationContext();
        String ret = "";

        try {
            InputStream inputStream = context.openFileInput(file);

            if (inputStream != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ((receiveString = bufferedReader.readLine()) != null) {
                    stringBuilder.append(receiveString);
                }

                inputStream.close();
                ret = stringBuilder.toString();
            }
        } catch (Exception e) {

        }

        return ret;
    }


    //---------

    @Override
    public void onBackPressed(){
        this.onResume();
        Intent activity = new Intent(this, Menu_principal.class);
        startActivity(activity);
        finish();
        super.onBackPressed();
    }

    //----------

    public void updateScore(){
        if (mSocket.getPseudo() != null) {
            String sbasket = (String) ((TextView) findViewById(R.id.basket)).getText();
            String senigmes = (String) ((TextView) findViewById(R.id.enigmes)).getText();
            String sdemineur = (String) ((TextView) findViewById(R.id.demineur)).getText();
            String scouleurs = (String) ((TextView) findViewById(R.id.couleurs)).getText();
            String spoints = (String) ((TextView) findViewById(R.id.points)).getText();
            String sfleches = (String) ((TextView) findViewById(R.id.fleches)).getText();
            String sclassique = (String) ((TextView) findViewById(R.id.classique)).getText();
            String value = sbasket + "\n" + senigmes + "\n" + sdemineur + "\n" + scouleurs + "\n" + spoints + "\n" + sfleches + "\n" + sclassique;
            mSocket.sauvegarder_scores(mSocket.getPseudo(), value);
        }

    }

    //---------

    @Override
    public void call(Object... args) {
        JSONObject data = (JSONObject) args[0];
        try {


            final ArrayList<String> rang = new ArrayList<String>();
            JSONArray jsonArray = (JSONArray) data.get("rang");
            if (jsonArray != null) {
                int len = jsonArray.length();
                for (int i = 0; i < len; i++) rang.add(jsonArray.get(i).toString());
            }

            final ArrayList<String> maxScores = new ArrayList<String>();
            jsonArray = (JSONArray) data.get("maxScores");
            if (jsonArray != null) {
                int len = jsonArray.length();
                for (int i = 0; i < len; i++) maxScores.add(jsonArray.get(i).toString());
            }

            System.out.println("Max : " + maxScores);
            System.out.println("Rang : " + rang);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    int i;
                    i = 0;
                    ((TextView) findViewById(R.id.mbasket)).setText(String.format("%02d",Integer.parseInt(maxScores.get(i))));
                    ((TextView) findViewById(R.id.rbasket)).setText(""+rang.get(i)+"e"+ ((rang.get(i) == "1")? "r" :""));

                    i = 1;
                    ((TextView) findViewById(R.id.menigmes)).setText(String.format("%02d",Integer.parseInt(maxScores.get(i))));
                    ((TextView) findViewById(R.id.renigmes)).setText(""+rang.get(i)+"e"+ ((rang.get(i) == "1")? "r" :""));

                    i = 2;
                    ((TextView) findViewById(R.id.mdemineur)).setText(String.format("%02d",Integer.parseInt(maxScores.get(i))));
                    ((TextView) findViewById(R.id.rdemineur)).setText(""+rang.get(i)+"e"+ ((rang.get(i) == "1")? "r" :""));
                    i = 3;
                    ((TextView) findViewById(R.id.mcouleurs)).setText(String.format("%02d",Integer.parseInt(maxScores.get(i))));
                    ((TextView) findViewById(R.id.rcouleurs)).setText(""+rang.get(i)+"e"+ ((rang.get(i) == "1")? "r" :""));
                    i = 4;
                    ((TextView) findViewById(R.id.mpoints)).setText(String.format("%02d",Integer.parseInt(maxScores.get(i))));
                    ((TextView) findViewById(R.id.rpoints)).setText(""+rang.get(i)+"e"+ ((rang.get(i) == "1")? "r" :""));
                    i = 5;
                    ((TextView) findViewById(R.id.mfleches)).setText(String.format("%02d",Integer.parseInt(maxScores.get(i))));
                    ((TextView) findViewById(R.id.rfleches)).setText(""+rang.get(i)+"e"+ ((rang.get(i) == "1")? "r" :""));
                    i = 6;
                    ((TextView) findViewById(R.id.mclassique)).setText(String.format("%02d",Integer.parseInt(maxScores.get(i))));
                    ((TextView) findViewById(R.id.rclassique)).setText(""+rang.get(i)+"e"+ ((rang.get(i) == "1")? "r" :""));



                }
            });


        } catch (JSONException e) {
            System.out.println("C");
            e.printStackTrace();
        }

    }
}
