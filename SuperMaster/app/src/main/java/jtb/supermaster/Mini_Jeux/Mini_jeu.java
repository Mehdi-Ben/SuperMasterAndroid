package jtb.supermaster.Mini_Jeux;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import io.socket.emitter.Emitter;
import jtb.supermaster.Menus.Menu_principal;
import jtb.supermaster.Modes.Mode_classique;
import jtb.supermaster.R;
import jtb.supermaster.Serveur.Fin_de_partie;
import jtb.supermaster.Serveur.Sockethor;

abstract public class Mini_jeu extends Activity implements Emitter.Listener {
    Boolean finPartie = false;
    boolean partieContreLaMontre;
    protected AlertDialog dialog;

    public boolean getPartieContreLaMontre() {
        return partieContreLaMontre;
    }


    //-------- Variables globales communes aux jeux


    boolean partieCourseAuxPoints;
    boolean tempsEcoule = false;
    boolean backpressed = false;
    int temps = 20;
    int pointsAAtteindre;
    public int score = 0;
    public int difficulte = 0;
    int vie = 3;
    String mode = " ";
    Sockethor mSocket;
    public Sons sonCorrect;
    public Sons sonErreur;

    String J1 = "";
    String J2 = "";
    String J3 = "";

    TextView tscore1;
    TextView tscore2;
    TextView tscore3;


    //--------


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Récupère la valeur booléenne si vrai partie classique si faux jeu libre
        sonCorrect = new Sons(R.raw.correct, this, "son");
        sonErreur = new Sons(R.raw.erreur, this, "son");

        //--------

        mSocket = new Sockethor();
        mSocket.abonnement(this);
        Intent intent = getIntent();
        mode = intent.getStringExtra("mode");

        //--------


        if (mode == null) mode = "";
        System.out.println("Mode : " + mode);
        if (mode.equals("courseAuxPoints") || mode.equals("contreLaMontre"))
            mSocket.MeilleursScores();

        //--------


        if (intent != null && intent.getExtras() != null) {

            if (mode.equals("classique")) {
                finPartie = true;
                this.vie = intent.getExtras().getInt("NB_VIES", 0);
            } else if (mode.equals("contreLaMontre")) {
                partieContreLaMontre = true;
                this.temps = intent.getExtras().getInt("TEMPS", 20);
                this.tempsEcoule = intent.getExtras().getBoolean("TEMPS_ECOULE", false);
            } else if (mode.equals("courseAuxPoints")) {
                partieCourseAuxPoints = true;
                this.J1 = intent.getStringExtra("J1");
                this.J2 = intent.getStringExtra("J2");
                this.J3 = intent.getStringExtra("J3");

                if (J1 == null) J1 = "";
                if (J2 == null) J2 = "";
                if (J3 == null) J3 = "";

            } else {
                partieCourseAuxPoints = true;
                this.pointsAAtteindre = intent.getExtras().getInt("NB_POINTS_A_ATTEINDRE", 10);
            }

            this.score = intent.getExtras().getInt("SCORE", 0);
            this.difficulte = intent.getExtras().getInt("DIFFICULTE", 1);

        }

    }


    //--------


    abstract protected void init();

    protected void finPartie() {
        if (finPartie || (vie <= 0) || (mode.equals("contreLaMontre") && tempsEcoule) || (mode.equals("IA")) || (mode.equals("courseAuxPoints")))
            sauver_et_quitter();
        else
            init();
    }


    //--------


    public void setPauseBouton() {
        if (mode.equals("courseAuxPoints") || mode.equals("contreLaMontre") || mode.equals("IA")) {
            ((ImageView) findViewById(R.id.pause)).setImageResource(R.drawable.img_cross_deconnect);
        } else {
            ((ImageView) findViewById(R.id.pause)).setImageResource(R.drawable.pause);
        }
    }


    //--------


    public void saveWhenQuitOnPause(int score, int difficulte) {
        this.score = score;
        this.difficulte = difficulte;
        sauver_et_quitter();
    }


    //--------


    protected void sauver_et_quitter() {
        if ((mode.equals("courseAuxPoints") || mode.equals("contreLaMontre")) && backpressed) {
            backpressed = false;
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Etes-vous sur de vouloir quitter la partie?").setTitle("Quitter la partie");
            builder.setPositiveButton("Oui", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    mSocket.quitter();
                    Intent activity = new Intent(getApplicationContext(), Menu_principal.class);
                    activity.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(activity);
                    finish();
                }
            });
            builder.setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    backpressed = false;
                }
            });

            dialog = builder.create();
            dialog.show();
        } else {
            Intent result = new Intent();
            if (finPartie) {
                result.putExtra("NB_VIES", "" + vie);
                if(backpressed) {
                    Intent activity = new Intent(this, Menu_principal.class);
                    activity.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(activity);
                    finish();
                }
            } else if (mode.equals("courseAuxPoints")) {
                if (J1.split("[ : ]")[0].equals(mSocket.getPseudo()))
                    J1 = mSocket.getPseudo() + " : " + score;
                else if (J2.split("[ : ]")[0].equals(mSocket.getPseudo()))
                    J2 = mSocket.getPseudo() + " : " + score;
                else if (J3.split("[ : ]")[0].equals(mSocket.getPseudo()))
                    J3 = mSocket.getPseudo() + " : " + score;

                result.putExtra("J1", J1);
                result.putExtra("J2", J2);
                result.putExtra("J3", J3);
            }

            result.putExtra("SCORE", "" + score);
            result.putExtra("DIFFICULTE", "" + difficulte);

            setResult(Activity.RESULT_OK, result);
            finish();
        }
    }


    //--------


    final protected void Gagne() {
        try {
            sonCorrect.play();
        } catch (Exception e) {
        }
        if (partieContreLaMontre) {
            mSocket.incr_points_serveur();
        }
        score++;
    }

    //--------


    final protected void Perdu() {
        if (!partieContreLaMontre && !partieCourseAuxPoints) {
            try {
                sonErreur.play();
            } catch (Exception e) {
            }
            if (this.vie > 0)
                this.vie--;
            else
                finPartie();
        } else if (partieContreLaMontre && (score > 0) && !tempsEcoule) {
            score--;
            mSocket.decr_points_serveur();
            try {
                sonErreur.play();
            } catch (Exception e) {
            }
        } else if (partieCourseAuxPoints) {
            if (score > 0) {
                score--;
                System.out.println("PASSSSS!!:::");
                try {
                    sonErreur.play();
                } catch (Exception e) {
                }
            }
        } else {
            try {
                sonErreur.play();
            } catch (Exception e) {
            }
        }

    }


    //--------



    @Override
    public void call(Object... args) {
        JSONObject data = (JSONObject) args[0];
        try {
            final String pseudo = (String) data.get("pseudo");
            final int code = (int) data.get("code");
            if (code == 1) {
                Intent activity = new Intent(this, Fin_de_partie.class);
                activity.putExtra("gagnant", pseudo);
                activity.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(activity);
            }
            else{
                finish();
            }
        } catch (JSONException e) {
            try {
                final String pseudo1 = (String) data.get("pseudo1");
                final String pseudo2 = (String) data.get("pseudo2");
                final String pseudo3 = (String) data.get("pseudo3");
                final int score1 = (int) data.get("score1");
                final int score2 = (int) data.get("score2");
                final int score3 = (int) data.get("score3");

                actualiserScores(pseudo1, pseudo2, pseudo3, score1, score2, score3);

            } catch (JSONException e2) {
                e2.printStackTrace();
            }
        }
    }


    //--------


    abstract protected void actualiserScores(String pseudo1, String pseudo2, String pseudo3, int score1, int score2, int score3);

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if ((data.getExtras() != null) && (data.getExtras().getBoolean("Kill"))) {
            sauver_et_quitter();
            super.onBackPressed();
        }
    }

    //--------


    public void setHUD(int couleurTexte){
        int tailleTexte = 25;
        Typeface custom_font1 = Typeface.createFromAsset(getAssets(), "fonts/helsinki.ttf");

        int[] views = new int[]{R.id.nom_mini_jeu, R.id.vie, R.id.score};
        for (int v : views) {
            ((TextView) findViewById(v)).setTypeface(custom_font1);
            ((TextView) findViewById(v)).setTextSize(tailleTexte);
            ((TextView) findViewById(v)).setTextColor(couleurTexte);
        }
    }


}
