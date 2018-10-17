package jtb.supermaster.Serveur;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.EmbossMaskFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;

import io.socket.emitter.Emitter;
import jtb.supermaster.Menus.Menu_principal;
import jtb.supermaster.Modes.Mode_multijoueurs;
import jtb.supermaster.R;

public class Fin_de_partie extends Activity implements Emitter.Listener {
    private Button rejouer = null;
    private Button quitter = null;
    private String gagnant = null;
    private Sockethor mSocket = null;
    private TextView victoire = null;
    private TextView pseudo = null;
    private LinearLayout tableauScores = null;
    private ArrayList<String> pseudos;
    private boolean exaequo;
    private String mpseudo = null;
    private String mmdp = null;
    private boolean scoresDemandes = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fin_de_partie);
        gagnant = getIntent().getStringExtra("gagnant");
        pseudos = getIntent().getStringArrayListExtra("pseudos");
        exaequo = getIntent().getBooleanExtra("exaequo", false);

        mSocket = new Sockethor();
        mSocket.abonnement(this);
        mSocket.ScoresPartie();

        mpseudo = mSocket.getPseudo();
        mmdp = mSocket.getMdp();

        victoire = (TextView) findViewById(R.id.victoire);
        pseudo = (TextView) findViewById(R.id.gagnant);
        tableauScores = (LinearLayout) findViewById(R.id.tableauScores);

        if (pseudos == null) {
            if (mpseudo.equals(gagnant)) {
                victoire.setText("Victoire !");
                pseudo.setText("Vous êtes de vainqueur!");
                mSocket.setSuperMaster(true);
            } else {
                victoire.setText("Défaite...");
                pseudo.setText("Le gagnant est " + gagnant);
                mSocket.setSuperMaster(false);
            }
        } else {
            if (pseudos.size() == 1) {
                if (mpseudo.equals(pseudos.get(0))) {
                    victoire.setText("Victoire !");
                    pseudo.setText("Vous êtes le vainqueur!");
                    mSocket.setSuperMaster(true);
                } else {
                    victoire.setText("Défaite...");
                    pseudo.setText("Le gagnant est " + pseudos.get(0));
                    mSocket.setSuperMaster(false);
                }
            } else if (pseudos.contains(mpseudo)) {
                victoire.setText("Victoire Ex aequo !");
                pseudo.setText("Vous êtes l'un des vainqueurs!");
                if (mpseudo.equals(pseudos.get(0))) mSocket.setSuperMaster(true);
            } else {
                victoire.setText("Défaite...");
                pseudo.setText("Les gagnants sont " + pseudos);
                mSocket.setSuperMaster(false);
            }
        }

    }

    public void onClickResume(View v) {
        if (mSocket.getSuperMaster()) {
            mSocket.newSupermaster(mpseudo);
            reconnexion();
            mSocket.setSuperMaster(true);
        } else {
            reconnexion();
            mSocket.setSuperMaster(false);
        }

        Intent activity = new Intent(getApplicationContext(), Mode_multijoueurs.class);
        activity.putExtra("fin", false);
        activity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        startActivity(activity);
        finish();
    }

    public void onClickQuit(View v) {
        reconnexion();

        Intent activity = new Intent(getApplicationContext(), Menu_principal.class);
        activity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(activity);
        finish();
    }

    private void reconnexion() {
        mSocket.deconnexion();
        mSocket = new Sockethor();
        mSocket.setPseudo(mpseudo);
        mSocket.setMdp(mmdp);
        mSocket.connexion(mpseudo, mmdp);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent activity = new Intent(this, Menu_principal.class);
        activity.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(activity);
        finish();
    }

    @Override
    public void call(Object... args) {
        JSONObject data = (JSONObject) args[0];

        try {
            final JSONArray jsonArray = (JSONArray) data.get("pseudos");
            final JSONArray jsonArray2 = (JSONArray) data.get("scores");
            final ArrayList<String> pseudos = new ArrayList<String>();
            final ArrayList<Integer> scores = new ArrayList<Integer>();

            if (jsonArray != null) {
                int len = jsonArray.length();
                for (int i = 0; i < len; i++) pseudos.add(jsonArray.get(i).toString());
            }

            if (jsonArray2 != null) {
                int len = jsonArray2.length();
                for (int i = 0; i < len; i++) {
                    String val = jsonArray2.get(i).toString();
                    scores.add(Integer.parseInt(val));
                }
            }

            System.out.println("Size: " + pseudos.size() + " et " + scores.size());
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    int len = pseudos.size();
                    for (int i = 0; i < len; i++) {
                        TextView joueur = new TextView(getApplicationContext());
                        joueur.setText(pseudos.get(i) + " : " + scores.get(i));
                        joueur.setTextColor(Color.BLACK);
                        joueur.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                        tableauScores.addView(joueur);
                    }
                }
            });
            if(tableauScores.getChildCount() == 0) mSocket.ScoresPartie();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
