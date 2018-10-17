package jtb.supermaster.Serveur;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.PowerManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import io.socket.emitter.Emitter;
import jtb.supermaster.Mini_Jeux.Sons;
import jtb.supermaster.Modes.Mode_contreLaMontre;
import jtb.supermaster.Modes.Mode_courseAuxPoints;
import jtb.supermaster.R;

import static android.graphics.Color.BLACK;
import static android.graphics.Color.GREEN;
import static android.graphics.Color.TRANSPARENT;
import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class Super_master_config extends Activity implements View.OnClickListener, Emitter.Listener {


    private TextView nbPoints;
    private TextView nbTemps;
    private TextView nbDiff;

    private TextView valider;
    private TextView choixPoints;
    private TextView choixMontre;

    private TextView pointsTotal;
    private TextView difficulteDepart;
    private TextView flecheMenu;
    private TextView couleursMenu;
    private TextView enigmesMenu;
    private TextView basketMenu;
    private TextView demineurMenu;
    private TextView etoilesMenu;

    private int couleurTexte;
    private int tailleTexte;

    private LinearLayout menuChoixMontre;
    private LinearLayout menuChoixPoints;

    private TextView tempsDepart;

    private TextView tempsTotal;

    private ImageView flecheHautPoints;
    private ImageView flecheBasPoints;
    private ImageView flecheHautDiff;
    private ImageView flecheBasDiff;
    private ImageView flecheHautTemps;
    private ImageView flecheBasTemps;

    private ImageView jeuFleche;
    private ImageView jeuBasket;
    private ImageView jeuCouleur;
    private ImageView jeuEnigme;
    private ImageView jeuPustule;
    private ImageView jeuEtoile;



    private TextView valideFleche;
    private TextView valideBasket;
    private TextView valideCouleur;
    private TextView valideEnigme;
    private TextView validePustule;
    private TextView valideEtoile;

    public int niveauDifficulteIA = 0;
    public String mode;

    private Sons sonConfig;


    private ArrayList<TextView> listeValidView = new ArrayList<TextView>();
    private ArrayList<String> listeNomJeux = new ArrayList<String>();
    private ImageView retour;
    private Sockethor mSocket;
    private ArrayList<ImageView> listJeu = new ArrayList<ImageView>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_super_master_config);
        sonConfig = new Sons(R.raw.boxeur, this, "music");

        //------- IA

        Intent intent = getIntent();
        this.mode = intent.getStringExtra("mode");
        if (mode.equals("IA"))
            this.niveauDifficulteIA = Integer.parseInt(intent.getStringExtra("difficulteIA"));
        System.out.println("MODE: " + mode);

        //-------


        mSocket = new Sockethor();


        mSocket.superMaster = true;
        this.nbPoints = (TextView) findViewById(R.id.NbPointsSuper);
        this.nbTemps = (TextView) findViewById(R.id.NbTempsSuper);
        this.nbDiff = (TextView) findViewById(R.id.NbDiffSuper);

        this.valider = (TextView) findViewById(R.id.Valider);
        this.choixPoints = (TextView) findViewById(R.id.boutonClassique);
        this.choixMontre = (TextView) findViewById(R.id.boutonQuitter);

        this.menuChoixMontre = (LinearLayout) findViewById(R.id.reglageTempsClM);
        this.menuChoixPoints = (LinearLayout) findViewById(R.id.reglagePointsChrono);

        this.flecheHautPoints = (ImageView) findViewById(R.id.flecheHautPoints);
        this.flecheBasPoints = (ImageView) findViewById(R.id.flecheBasPoints);
        this.flecheHautDiff = (ImageView) findViewById(R.id.flecheHautDiff);
        this.flecheBasDiff = (ImageView) findViewById(R.id.flecheBasDiff);
        this.flecheHautTemps = (ImageView) findViewById(R.id.flecheHautTemps);
        this.flecheBasTemps = (ImageView) findViewById(R.id.flecheBasTemps);

        this.jeuFleche = (ImageView) findViewById(R.id.Fleches);
        this.jeuBasket = (ImageView) findViewById(R.id.Basket);
        this.jeuCouleur = (ImageView) findViewById(R.id.Couleurs);
        this.jeuEnigme = (ImageView) findViewById(R.id.Enigmes);
        this.jeuPustule = (ImageView) findViewById(R.id.Demineur);
        this.jeuEtoile = (ImageView) findViewById(R.id.Points);

        this.valideFleche = (TextView) findViewById(R.id.valideFleche);
        this.valideBasket = (TextView) findViewById(R.id.valideBasket);
        this.valideCouleur = (TextView) findViewById(R.id.valideCouleurs);
        this.valideEnigme = (TextView) findViewById(R.id.valideEnigme);
        this.validePustule = (TextView) findViewById(R.id.valideDemineur);
        this.valideEtoile = (TextView) findViewById(R.id.valideEtoiles);

        this.valider.setOnClickListener(this);
        this.choixPoints.setOnClickListener(this);
        this.choixMontre.setOnClickListener(this);

        this.flecheHautPoints.setOnClickListener(this);
        this.flecheBasPoints.setOnClickListener(this);
        this.flecheHautDiff.setOnClickListener(this);
        this.flecheBasDiff.setOnClickListener(this);
        this.flecheHautTemps.setOnClickListener(this);
        this.flecheBasTemps.setOnClickListener(this);

        this.jeuFleche.setOnClickListener(this);
        this.jeuBasket.setOnClickListener(this);
        this.jeuCouleur.setOnClickListener(this);
        this.jeuEnigme.setOnClickListener(this);
        this.jeuPustule.setOnClickListener(this);
        this.jeuEtoile.setOnClickListener(this);


        this.retour = (ImageView) findViewById(R.id.back);

        couleurTexte = 0xFF1a237e;
        Typeface custom_font1 = Typeface.createFromAsset(getAssets(), "fonts/helsinki.ttf");

        pointsTotal =   (TextView) findViewById(R.id.nbViesConfig);
        difficulteDepart = (TextView) findViewById(R.id.nbDifficulte);
        tempsDepart = (TextView) findViewById(R.id.NbTempsSuper);

        flecheMenu =  (TextView) findViewById(R.id.FlechesTexte);
        couleursMenu =  (TextView) findViewById(R.id.CouleursTexte);
        enigmesMenu =  (TextView) findViewById(R.id.EnigmesTexte);
        basketMenu = (TextView) findViewById(R.id.BasketTexte);
        demineurMenu = (TextView) findViewById(R.id.DemineurTexte);
        etoilesMenu = (TextView) findViewById(R.id.EtoilesTexte);
        tempsTotal = (TextView) findViewById(R.id.TpsTotal);


        choixPoints.setTypeface(custom_font1);
        choixPoints.setTextColor(couleurTexte);
        choixPoints.setTextSize(35);

        choixMontre.setTypeface(custom_font1);
        choixMontre.setTextColor(couleurTexte);
        choixMontre.setTextSize(35);

        pointsTotal.setTypeface(custom_font1);
        pointsTotal.setTextColor(couleurTexte);
        pointsTotal.setTextSize(25);

        tempsTotal.setTypeface(custom_font1);
        tempsTotal.setTextColor(couleurTexte);
        tempsTotal.setTextSize(25);

        difficulteDepart.setTypeface(custom_font1);
        difficulteDepart.setTextColor(couleurTexte);
        difficulteDepart.setTextSize(25);

        tempsDepart.setTypeface(custom_font1);
        tempsDepart.setTextColor(couleurTexte);
        tempsDepart.setTextSize(25);

        flecheMenu.setTypeface(custom_font1);
        flecheMenu.setTextColor(couleurTexte);
        flecheMenu.setTextSize(20);

        couleursMenu.setTypeface(custom_font1);
        couleursMenu.setTextColor(couleurTexte);
        couleursMenu.setTextSize(20);

        enigmesMenu.setTypeface(custom_font1);
        enigmesMenu.setTextColor(couleurTexte);
        enigmesMenu.setTextSize(20);

        basketMenu.setTypeface(custom_font1);
        basketMenu.setTextColor(couleurTexte);
        basketMenu.setTextSize(20);

        demineurMenu.setTypeface(custom_font1);
        demineurMenu.setTextColor(couleurTexte);
        demineurMenu.setTextSize(20);

        etoilesMenu.setTypeface(custom_font1);
        etoilesMenu.setTextColor(couleurTexte);
        etoilesMenu.setTextSize(20);

        valider.setTypeface(custom_font1);
        valider.setTextColor(couleurTexte);
        valider.setTextSize(25);


        if (mode.equals("IA")) {
            this.choixMontre.setVisibility(GONE);
            this.choixPoints.setVisibility(GONE);
            System.out.println("ON NE FERME PAS LA SALLE");

        } else {
            mSocket.fermer_salle();
            System.out.println("ON FERME LA SALLE");
        }

        this.retour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Super_master_config.super.onBackPressed();
                finish();


            }
        });

        listeValidView.add(valideBasket);
        listeValidView.add(valideCouleur);
        listeValidView.add(valideEnigme);
        listeValidView.add(valideEtoile);
        listeValidView.add(valideFleche);
        listeValidView.add(validePustule);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.flecheHautPoints:
                this.nbPoints.setText(Integer.toString(Integer.parseInt((String) this.nbPoints.getText()) + 1));
                break;
            case R.id.flecheBasPoints:
                this.nbPoints.setText(Integer.toString(Math.max((Integer.parseInt((String) this.nbPoints.getText()) - 1), 1)));
                break;
            case R.id.flecheHautTemps:
                this.nbTemps.setText(Integer.toString(Integer.parseInt((String) this.nbTemps.getText()) + 1));
                break;
            case R.id.flecheBasTemps:
                this.nbTemps.setText(Integer.toString(Math.max((Integer.parseInt((String) this.nbTemps.getText()) - 1), 1)));
                break;
            case R.id.flecheHautDiff:
                this.nbDiff.setText(Integer.toString(Integer.parseInt((String) this.nbDiff.getText()) + 1));
                break;
            case R.id.flecheBasDiff:
                this.nbDiff.setText(Integer.toString(Math.max((Integer.parseInt((String) this.nbDiff.getText()) - 1), 0)));
                break;
            case R.id.Valider: {
                try{sonConfig.getListeSons().get(0).stop();}catch(Exception e){}
                String stringJeux = "";
                for (ImageView mini_jeu : listJeu) {
                    switch (mini_jeu.getId()) {
                        case R.id.Fleches:
                            stringJeux = stringJeux + "arrow" + "%";
                            break;
                        case R.id.Basket:
                            stringJeux = stringJeux + "basket" + "%";
                            break;
                        case R.id.Couleurs:
                            stringJeux = stringJeux + "couleurs" + "%";
                            break;
                        case R.id.Enigmes:
                            stringJeux = stringJeux + "enigmes" + "%";
                            break;
                        case R.id.Demineur:
                            stringJeux = stringJeux + "demineur" + "%";
                            break;
                        case R.id.Points:
                            stringJeux = stringJeux + "points" + "%";
                            break;
                    }
                }

                if (!stringJeux.equals("")) {
                    if (mode.equals("IA")) {
                        Intent activity = new Intent(this, Mode_courseAuxPoints.class);
                        activity.putExtra("listeJeux", "" + stringJeux);
                        activity.putExtra("difficulte", "" + Integer.parseInt(this.nbDiff.getText().toString()));
                        activity.putExtra("unites", "" + Integer.parseInt(this.nbPoints.getText().toString()));
                        activity.putExtra("difficulteIA", "" + this.niveauDifficulteIA);
                        activity.putExtra("mode", "IA");


                        startActivity(activity);

                    } else {


                        JSONObject data = new JSONObject();
                        try {
                            if (((ColorDrawable) choixPoints.getBackground()).getColor() == GREEN) {
                                data.put("mode", "point");
                                data.put("unites", Integer.parseInt(this.nbPoints.getText().toString()));

                            } else {
                                data.put("mode", "temps");
                                data.put("unites", Integer.parseInt(this.nbTemps.getText().toString()));
                            }
                            data.put("difficulte", Integer.parseInt(this.nbDiff.getText().toString()));
                            data.put("listeJeux", stringJeux);
                            mSocket.getSocket().emit("lancerPartie", data);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                        break;
                    }

                }
            }

            case R.id.boutonClassique:


                this.choixMontre.setBackgroundColor(TRANSPARENT);
                this.choixPoints.setBackgroundColor(GREEN);
                this.menuChoixMontre.setVisibility(GONE);
                this.menuChoixPoints.setVisibility(VISIBLE);
                this.nbPoints.setText("1");
                this.nbDiff.setText("0");
                this.nbTemps.setText("1");
                this.removeValidViews();
                this.listJeu = new ArrayList<ImageView>();
                break;
            case R.id.boutonQuitter:
                this.choixPoints.setBackgroundColor(TRANSPARENT);
                this.choixMontre.setBackgroundColor(GREEN);
                this.menuChoixPoints.setVisibility(GONE);
                this.menuChoixMontre.setVisibility(VISIBLE);
                this.nbPoints.setText("1");
                this.nbDiff.setText("0");
                this.nbTemps.setText("1");
                this.removeValidViews();
                this.listJeu = new ArrayList<ImageView>();
                break;

            default:
                if (((ColorDrawable) v.getBackground()).getColor() == BLACK) {
                    v.setBackgroundColor(GREEN);
                    if (((ColorDrawable) choixPoints.getBackground()).getColor() == GREEN) {
                        updateNumberModePoints((ImageView) v, true);
                    } else {
                        updateNumberModeMontre((ImageView) v, true);
                    }
                } else {
                    v.setBackgroundColor(BLACK);
                    if (((ColorDrawable) choixPoints.getBackground()).getColor() == GREEN) {
                        updateNumberModePoints((ImageView) v, false);
                    } else {
                        updateNumberModeMontre((ImageView) v, false);
                    }
                }

                break;
        }

    }

    public void updateNumberModeMontre(ImageView v, boolean selectionne) {
        removeValidViews();
        if (selectionne) {
            updateValidViewGreen(v, 0);
            if (listJeu.size() > 0) {
                this.listJeu.remove(0);
            }
            this.listJeu.add(v);
        } else {
            if (listJeu.size() > 0) {
                this.listJeu.remove(0);
            }
        }
    }

    public void updateNumberModePoints(ImageView v, boolean selectionne) {
        ImageView im;
        if (selectionne) {
            listJeu.add(v);
        } else {
            switch (v.getId()) {
                case R.id.Fleches:
                    this.valideFleche.setVisibility(View.INVISIBLE);
                    break;
                case R.id.Basket:
                    this.valideBasket.setVisibility(View.INVISIBLE);
                    break;
                case R.id.Couleurs:
                    this.valideCouleur.setVisibility(View.INVISIBLE);
                    break;
                case R.id.Enigmes:
                    this.valideEnigme.setVisibility(View.INVISIBLE);
                    break;
                case R.id.Demineur:
                    this.validePustule.setVisibility(View.INVISIBLE);
                    break;
                case R.id.Points:
                    this.valideEtoile.setVisibility(View.INVISIBLE);
                    break;
            }
            listJeu.remove(v);
        }
        for (int i = 0; i < listJeu.size(); i++) {
            im = listJeu.get(i);
            updateValidViewGreen(im, i);
        }


    }

    public void updateValidViewGreen(ImageView im, int i) {
        switch (im.getId()) {
            case R.id.Fleches:
                this.valideFleche.setText(Integer.toString(i + 1));
                this.valideFleche.setVisibility(VISIBLE);
                break;
            case R.id.Basket:
                this.valideBasket.setText(Integer.toString(i + 1));
                this.valideBasket.setVisibility(VISIBLE);
                break;
            case R.id.Couleurs:
                this.valideCouleur.setText(Integer.toString(i + 1));
                this.valideCouleur.setVisibility(VISIBLE);
                break;
            case R.id.Enigmes:
                this.valideEnigme.setText(Integer.toString(i + 1));
                this.valideEnigme.setVisibility(VISIBLE);
                break;
            case R.id.Demineur:
                this.validePustule.setText(Integer.toString(i + 1));
                this.validePustule.setVisibility(VISIBLE);
                break;
            case R.id.Points:
                this.valideEtoile.setText(Integer.toString(i + 1));
                this.valideEtoile.setVisibility(VISIBLE);
                break;
        }
    }

    public void removeValidViews() {
        for (ImageView jeu : listJeu) {
            jeu.setBackgroundColor(BLACK);
        }
        for (TextView valid : this.listeValidView) {
            valid.setVisibility(View.INVISIBLE);
        }
        this.listJeu = new ArrayList<ImageView>();
    }

    @Override
    public void call(Object... args) {
        JSONObject data = (JSONObject) args[0];

        final String mode;
        final int unites;
        final int difficulte;
        final String listeJeux;

        try {
            mode = (String) data.get("mode");
            unites = (int) data.get("unites");
            difficulte = (int) data.get("difficulte");
            listeJeux = (String) data.get("listeJeux");
            Intent activity;// =  new Intent(this, Mode_courseAuxPoints.class);

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
            startActivity(activity);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed(){
        if(!mode.equals("IA")){
            mSocket.ouvrir_salle();
            finish();
        }
    }



    @Override
    public void onResume()
    {
        try{
            if (sonConfig.getListeSons().get(0)!=null && (!sonConfig.getListeSons().get(0).getSon().isPlaying()))
                sonConfig.getListeSons().get(0).play();}catch(Exception e){}
        super.onResume();

    }

    @Override
    public void onPause()
    {
        super.onPause();
        PowerManager mPowerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);

        if (!mPowerManager.isScreenOn())
            if (sonConfig!= null && sonConfig.getListeSons().get(0).getSon().isPlaying())
                try{sonConfig.getListeSons().get(0).stop();}catch (Exception e){}
    }

}
