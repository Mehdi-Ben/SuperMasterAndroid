package jtb.supermaster.Modes;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.Display;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import io.socket.emitter.Emitter;
import jtb.supermaster.Serveur.Connexion;
import jtb.supermaster.Menus.Menu_principal;
import jtb.supermaster.R;
import jtb.supermaster.Serveur.Sockethor;
import jtb.supermaster.Serveur.Waiting_room;

public class Mode_multijoueurs extends Activity implements Emitter.Listener {

    Button partie = null;
    Sockethor mSocket = null;
    Boolean testCo = false;
    AlertDialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mode_multijoueurs);
        partie = (Button) findViewById(R.id.connexion);
        mSocket = new Sockethor();

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("La salle est indisponible, re-essayez plus tard.").setTitle("Salle occupée");
        builder.setPositiveButton("D'accord", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Intent activity = new Intent(Mode_multijoueurs.this, Menu_principal.class);
                activity.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(activity);

                    String mpseudo = mSocket.getPseudo();
                    String mmdp = mSocket.getMdp();
                    mSocket.deconnexion();
                    mSocket = new Sockethor();
                    mSocket.setPseudo(mpseudo);
                    mSocket.setMdp(mmdp);
                    mSocket.connexion(mpseudo, mmdp);

                finish();
            }
        });

        dialog = builder.create();

    }

    //--------

    public void connexion() {
        if (mSocket.pseudo == null) {
            Intent activity = new Intent(this, Connexion.class);
            startActivityForResult(activity, 0);
            testCo = true;
        } else if (!testCo) {
            mSocket.abonnement(this);
            mSocket.testSalle();
        }
    }

    //--------

    @Override
    public void call(Object... args) {
        JSONObject data = (JSONObject) args[0];
        final boolean disponible;
        final boolean salle_ouverte;

        try {
            disponible = (boolean) data.get("disponible");
            salle_ouverte = (boolean) data.get("salle_ouverte");
            if (disponible){
                Intent activity = new Intent(Mode_multijoueurs.this, Waiting_room.class);
                startActivity(activity);
            } else {
                new CustomTask().execute((Void[])null);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //--------

    @Override
    protected void onResume() {
        super.onResume();
        this.connexion();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (mSocket.pseudo == null) {
            Intent activity = new Intent(Mode_multijoueurs.this, Menu_principal.class);
            startActivity(activity);
        } else {
            mSocket.abonnement(this);
            mSocket.testSalle();
        }
    }

    //--------

    private class CustomTask extends AsyncTask<Void, Void, Void> {

        protected Void doInBackground(Void... param) {
            //Do some work
            return null;
        }

        protected void onPostExecute(Void param) {
            if(!isFinishing())
            {
                dialog.show();
            }

        }
    }

    //--------

    @Override
    public void onBackPressed(){
        this.onResume();
        Intent activity = new Intent(this, Menu_principal.class);
        startActivity(activity);
        finish();
        super.onBackPressed();
    }


}
