package jtb.supermaster.Serveur;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import io.socket.emitter.Emitter;
import jtb.supermaster.Menus.Menu_principal;
import jtb.supermaster.R;
import jtb.supermaster.Serveur.Sockethor;


public class Connexion extends Activity implements Emitter.Listener {

    TextView information = null;
    EditText pseudo = null;
    EditText mdp = null;
    Button connexion = null;
    ProgressBar chargement = null;
    Switch inscriptionS = null;
    Sockethor mSocket = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connexion);

        information = (TextView) findViewById(R.id.information);
        pseudo = (EditText) findViewById(R.id.pseudo);
        mdp = (EditText) findViewById(R.id.mdp);
        connexion = (Button) findViewById(R.id.connexion);
        chargement = (ProgressBar) findViewById(R.id.chargement);
        inscriptionS = (Switch) findViewById(R.id.inscription);
        inscriptionS.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) connexion.setText("Inscription");
                else connexion.setText("Connexion");
                connexion.setVisibility(View.VISIBLE);
            }
        });

        mSocket = new Sockethor();

        connexion.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        connexion.setVisibility(View.GONE);
                        chargement.setVisibility(View.VISIBLE);
                        connexion();
                    }

                }
        );

    }

    public void connexion() {
        mSocket.getSocket().on("query", this);
        JSONObject obj = new JSONObject();

        if (inscriptionS.isChecked())
            mSocket.inscription(pseudo.getText().toString(), mdp.getText().toString());
        else mSocket.connexion(pseudo.getText().toString(), mdp.getText().toString());
    }

    @Override
    public void call(Object... args) {
        JSONObject data = (JSONObject) args[0];

        final boolean inscription;
        final int errcode;

        try {
            inscription = (boolean) data.get("inscription");
            errcode = (int) data.get("errcode");

            if (inscription) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        chargement.setVisibility(View.GONE);
                    }
                });
                switch (errcode) {
                    case 0: {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                information.setText("L'inscription s'est bien passée.");
                                information.setTextColor(Color.GREEN);
                                inscriptionS.setChecked(false);

                            }
                        });
                        break;
                    }
                    case 1: {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                information.setText("Le pseudo existe déjà.");
                                information.setTextColor(Color.RED);
                            }
                        });
                        break;
                    }
                    case 3: {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                information.setText("Pseudo invalide.");
                                information.setTextColor(Color.RED);
                            }
                        });
                        break;
                    }
                    default: {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                information.setText("Une erreur est survenue.");
                                information.setTextColor(Color.RED);
                            }
                        });
                        break;
                    }
                }
            } else {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        chargement.setVisibility(View.GONE);
                    }
                });
                switch (errcode) {
                    case 0: {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                information.setText("Connecté.");
                                information.setTextColor(Color.GREEN);
                                mSocket.setPseudo(pseudo.getText().toString());
                                mSocket.setMdp(mdp.getText().toString());

                                Intent result = new Intent();
                                setResult(Activity.RESULT_OK, result);
                                finish();
                            }
                        });
                        break;
                    }
                    case 1: {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                information.setText("Identifiants inccorects.");
                                information.setTextColor(Color.RED);
                                connexion.setVisibility(View.VISIBLE);
                            }
                        });
                        break;
                    }
                    case 2: {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                information.setText("Utilisateur déjà connecté.");
                                information.setTextColor(Color.RED);
                                connexion.setVisibility(View.VISIBLE);
                            }
                        });
                        break;
                    }
                    default: {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                information.setText("Une erreur est survenue.");
                                information.setTextColor(Color.RED);
                            }
                        });
                        break;
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    /*information.setText("Une erreur est survenue. (JSON)");
                    information.setTextColor(Color.RED);*/
                }
            });
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent activity = new Intent(this, Menu_principal.class);
        activity.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(activity);
        finish();
    }
}
