package jtb.supermaster.Serveur;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

import io.socket.client.Ack;
import io.socket.emitter.Emitter;
import jtb.supermaster.Menus.Menu_principal;
import jtb.supermaster.Mini_Jeux.Mini_jeu;
import jtb.supermaster.Mini_Jeux.Sons;
import jtb.supermaster.Modes.Mode_contreLaMontre;
import jtb.supermaster.Modes.Mode_courseAuxPoints;
import jtb.supermaster.R;

public class Waiting_room extends Activity implements Emitter.Listener {
    LinearLayout listeJoueurs = null;
    Sockethor mSocket = null;
    Button lancerPartie = null;
    MediaPlayer sonWifi = null;
    Map<String, TextView> mapJoueurs = null;
    String pseudo_supermaser = null;
    int nombreDeJoueurs = 0;
    boolean connecte = false;
    boolean demande = false;
    boolean bolcon = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waiting_room);
        mSocket = new Sockethor();
        mSocket.abonnement(this);
        mSocket.joueursSalle();

        mapJoueurs = new HashMap<String, TextView>();
        try{sonWifi = (new Sons(R.raw.wifi, getApplicationContext(), "music")).play();}catch (Exception e){}
        listeJoueurs = (LinearLayout) findViewById(R.id.listeJoueurs);
        lancerPartie = (Button) findViewById(R.id.lancerPartie);
        lancerPartie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent activity = new Intent(Waiting_room.this, Super_master_config.class);
                try {
                    sonWifi.stop();
                } catch (Exception e5) {
                    e5.printStackTrace();
                }
                activity.putExtra("mode", "serveur");
                startActivity(activity);
            }
        });


    }


    @Override
    public void call(Object... args) {
        JSONObject data = (JSONObject) args[0];

        try { // Arrivé dans la salle : récupération des joueurs connectés
            final ArrayList<String> pseudos = new ArrayList<String>();
            JSONArray jsonArray = (JSONArray) data.get("pseudos");
            if (jsonArray != null) {
                int len = jsonArray.length();
                for (int i = 0; i < len; i++) pseudos.add(jsonArray.get(i).toString());
            }
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    for (String pseudo : pseudos) {
                        TextView joueur = new TextView(getApplicationContext());
                        joueur.setText(pseudo);
                        joueur.setTextColor(Color.BLACK);
                        joueur.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                        for (int i = 0; i < listeJoueurs.getChildCount(); i++) {
                            TextView temp = (TextView) listeJoueurs.getChildAt(i);
                            if (temp.getText().equals(pseudo)) {
                                demande = true;
                            }
                        }
                        if (!demande) {
                            listeJoueurs.addView(joueur);
                            mapJoueurs.put(pseudo, joueur);
                            nombreDeJoueurs++;
                        }
                        demande = false;
                    }
                }
            });
            if(!bolcon){
                bolcon = true;
                mSocket.connSalle();
            }
        } catch (JSONException e) { // Connexion ou déconnexion d'un autre joueur ou demande de supermaster
            try {
                final String pseudo = (String) data.get("pseudo");
                final int code = (int) data.get("code");

                switch (code) {
                    case 0: // Connexion
                    {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                TextView joueur = new TextView(getApplicationContext());
                                joueur.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                                joueur.setText(pseudo);
                                joueur.setTextColor(Color.BLACK);
                                listeJoueurs.addView(joueur);
                                mapJoueurs.put(pseudo, joueur);
                                nombreDeJoueurs++;
                                if (mSocket.superMaster && nombreDeJoueurs >= 2)
                                    lancerPartie.setVisibility(View.VISIBLE);
                            }
                        });
                        mSocket.supermaster();
                        break;
                    }
                    case 2:  // Déconnexion
                    {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                nombreDeJoueurs--;
                                if (pseudo.equals(pseudo_supermaser)) mSocket.supermaster();
                                else if (nombreDeJoueurs < 2) lancerPartie.setVisibility(View.GONE);
                                listeJoueurs.removeView(mapJoueurs.get(pseudo));
                                mapJoueurs.remove(pseudo);
                            }
                        });
                        break;
                    }
                    case 3: // supermaster
                    {
                        pseudo_supermaser = pseudo;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                for (TextView maView : mapJoueurs.values()) maView.setTextColor(Color.BLACK);
                                mapJoueurs.get(pseudo).setTextColor(Color.BLUE);
                            }
                        });
                        if (pseudo_supermaser.equals(mSocket.getPseudo())) { // Si je devient le nouveau supermaster
                            mSocket.setSuperMaster(true);
                            if (nombreDeJoueurs >= 2)
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        lancerPartie.setVisibility(View.VISIBLE);
                                    }
                                });
                        } else {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {

                                    mapJoueurs.get(mSocket.getPseudo()).setTextColor(Color.BLACK);
                                    mSocket.setSuperMaster(false);
                                    lancerPartie.setVisibility(View.INVISIBLE);
                                }
                            });
                        }


                    }
                }
            } catch (JSONException e2) { // Lancement de la partie
                try {
                    final String mode;
                    final int unites;
                    final int difficulte;
                    final String listeJeux;

                    mode = (String) data.get("mode");
                    unites = (int) data.get("unites");
                    difficulte = (int) data.get("difficulte");
                    listeJeux = (String) data.get("listeJeux");
                    Intent activity;

                    if (mode.equals("point")) {
                        activity = new Intent(this, Mode_courseAuxPoints.class);
                        activity.putExtra("mode", "courseAuxPoints");
                    } else {
                        activity = new Intent(this, Mode_contreLaMontre.class);
                        activity.putExtra("mode", "contreLaMontre");
                    }
                    activity.putExtra("unites", "" + unites);
                    activity.putExtra("difficulte", "" + difficulte);
                    activity.putExtra("listeJeux", listeJeux);
                    try {
                        try {
                            sonWifi.stop();
                        } catch (Exception e5) {
                            e5.printStackTrace();
                        }

                    } catch (Exception e5) {
                        e5.printStackTrace();
                    }
                    activity.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(activity);
                    finish();
                } catch (JSONException e4) {
                    e4.printStackTrace();
                }
            }
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        try {
            sonWifi.stop();
        } catch (Exception e5) {
            e5.printStackTrace();
        }
        mSocket.setSuperMaster(false);
        mSocket.quitter();
        Intent activity = new Intent(this, Menu_principal.class);
        activity.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(activity);
        finish();

    }
}