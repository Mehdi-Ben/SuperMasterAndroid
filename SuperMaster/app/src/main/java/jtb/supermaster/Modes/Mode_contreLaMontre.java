package jtb.supermaster.Modes;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import io.socket.emitter.Emitter;
import jtb.supermaster.Mini_Jeux.ArrowGame;
import jtb.supermaster.Mini_Jeux.Basket;
import jtb.supermaster.Mini_Jeux.Couleur_a_trouver;
import jtb.supermaster.Menus.Menu_libre;
import jtb.supermaster.Mini_Jeux.Enigmes_a_resoudre;
import jtb.supermaster.Mini_Jeux.Jeu_pustules;
import jtb.supermaster.Mini_Jeux.Mini_jeu;
import jtb.supermaster.Mini_Jeux.Point_connection;
import jtb.supermaster.Mini_Jeux.Sons;
import jtb.supermaster.R;
import jtb.supermaster.Serveur.Fin_de_partie;
import jtb.supermaster.Serveur.Sockethor;

public class Mode_contreLaMontre extends Activity implements View.OnClickListener, Emitter.Listener{

    private ArrayList<Class> activites = new ArrayList<Class>(); // Liste des activity miniJeu.class
    CountDownTimer chrono;
    ProgressBar chronoBar; // Timer
    int score = 0;
    int scoreRecu;
    int idJeu = 0;
    int difficulte = 1;
    public int getScore(){return score;}
    public int getTempsMaitreJeu(){return tempsMaitreJeu;}
    Sockethor mSocket;
    String mini_jeu = "demineur";
    public String getMini_jeu(){return mini_jeu;}
    public ArrayList<Class> getActivites(){return activites;}
    public int getIdJeu(){return  idJeu;}
    String mode;
    Intent activity;
    int tempsMaitreJeu = 42;
    boolean partieDejaLancee = false;
    boolean fin = false;
    String[] liste;
    public String[] getListe(){return liste;}
    public final static int POINTS = 0;


    private boolean activityResultIsReturned=false;
    private String activityResult=null;
    public boolean getActivityResultIsReturned(){return activityResultIsReturned;}
    public String getActivityResult(){return activityResult;}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mode_contrelamontre);
        activityResultIsReturned = false;
        activityResult = null;
        mSocket = new Sockethor();
        mSocket.abonnement(this);
        mode = getIntent().getStringExtra("mode");

        // RECUPERATION 3 SCORES JOUEURS -> SERVEUR
        try {
            tempsMaitreJeu = Integer.parseInt(getIntent().getStringExtra("unites"));
            difficulte = Integer.parseInt(getIntent().getStringExtra("difficulte"));
            liste = getIntent().getStringExtra("listeJeux").split("[%]");
            mini_jeu = liste[0];
        }catch (Exception e){difficulte = 1;tempsMaitreJeu = 10;mini_jeu = "points";}


      //  System.out.println("Unités: " +  getIntent().getStringExtra("unites") + "\nDifficulté: " +  getIntent().getStringExtra("difficulte"));
     //   for(int i = 0; i < 6; i++)
       //     System.out.println(getIntent().getStringExtra("listeJeux").split("[%]")[i]);


        switch(mini_jeu) {
            case "arrow":
                idJeu = 0;
                break;
            case "basket":
                idJeu = 1;
                break;
            case "couleurs":
                idJeu = 2;
                break;
            case "demineur":
                idJeu = 3;
                break;
            case "enigmes":
                idJeu = 4;
                break;
            case "points":
                idJeu = 5;
                break;
        }

// Ajout des différents mini jeux à la liste des activitées
        activites.add(ArrowGame.class);
        activites.add(Basket.class);
        activites.add(Couleur_a_trouver.class);
        activites.add(Jeu_pustules.class);
        activites.add(Enigmes_a_resoudre.class);
        activites.add(Point_connection.class);

         /* Chrono */
        partieDejaLancee = false;
        int duree = 3; // Temps voulu en secondes
        final long delay = 1000 * duree;
        chrono = new CountDownTimer(delay, 1) {

            @Override
            public void onTick(long millisUntilFinished) {
                ((TextView) findViewById(R.id.TextPartie)).setText("" + ((millisUntilFinished/1000)+1)); // Mise à jour du TexteView du décompte
            }

            @Override
            public void onFinish() {
                lancerParties(idJeu);
                    //Lancement de la partie contre la montre après le décompte
            }
        };
        chrono.start();
    }

    // Crée les différentes activities correspondant aux mini jeux
    private void lancerParties(int i){
        activity = new Intent(this, this.activites.get(i));
        activity.putExtra("DIFFICULTE", this.difficulte);
        activity.putExtra("TEMPS", this.tempsMaitreJeu);
        activity.putExtra("TEMPS_ECOULE", false);
        activity.putExtra("mode", "contreLaMontre");
        startActivityForResult(activity,POINTS);
    }

    private void finDePartie(){
        int ancienScore = 0;
        try {
            ancienScore = Integer.parseInt(Menu_libre.readFromFile("multijoueurs_libre.txt", getApplicationContext()));
        } catch (Exception e) {
            Menu_libre.writeToFile("0", "multijoueurs_libre.txt", getApplicationContext());
        }

        if (score > ancienScore) {
            Menu_libre.writeToFile("" + score, "multijoueurs_libre.txt", getApplicationContext());
            try{(new Sons(R.raw.high_score,  getApplicationContext(), "music")).play();}catch(Exception e){}
        }
        else if (fin){
            ((TextView) findViewById(R.id.TextPartie)).setText("Le temps est écoulé !");
            fin = false;
        }
        else {
            ((TextView) findViewById(R.id.TextPartie)).setText("Vous avez arrêté de jouer !");
        }
        ((TextView) findViewById(R.id.viewScore)).setText(""+scoreRecu);
        (findViewById(R.id.viewScore)).setVisibility(View.VISIBLE);
        (findViewById(R.id.viewScoreFinal)).setVisibility(View.VISIBLE);
        this.score = 0;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            activityResultIsReturned = true;
            fin = Boolean.parseBoolean(data.getStringExtra("TEMPS_ECOULE"));
            scoreRecu = Integer.parseInt(data.getStringExtra("SCORE"));
            if (mode.equals("contreLaMontre")){
                if (mSocket.getSuperMaster()) mSocket.times_up();
            }
            else
            {
                finDePartie();
            }
        }else{
            finDePartie();
        }
    }

    @Override
    public void onClick(View v){
    }

    @Override
    public void onBackPressed(){
        chrono.cancel();
        finish();
        super.onBackPressed();
    }

    @Override
        public void call(Object... args) {
            JSONObject data = (JSONObject) args[0];
            try {
                final ArrayList<String> pseudos = new ArrayList<String>();
                JSONArray jsonArray = (JSONArray) data.get("pseudos");
                if (jsonArray != null) {
                    int len = jsonArray.length();
                    for (int i = 0; i < len; i++) pseudos.add(jsonArray.get(i).toString());
                }
            boolean exaequo = (boolean) data.get("exaequo");

            Intent activity = new Intent(this, Fin_de_partie.class);
            activity.putExtra("exaequo", exaequo);
            activity.putStringArrayListExtra("pseudos", pseudos);
            startActivity(activity);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}