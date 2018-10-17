package jtb.supermaster.Menus;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import jtb.supermaster.Mini_Jeux.Mini_jeu;
import jtb.supermaster.Mini_Jeux.Sons;
import jtb.supermaster.R;
/**
 * Created by Maxime on 09/04/2017 (19:39).
 */

public class Pause extends Activity {

    //---- Variables globales


    int currentScore;
    int currentDifficulte;
    String mode;
    public Sons sonBulle;


    //----------


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pause);
        sonBulle = new Sons(R.raw.bulle, this, "son");

    }

    public void onClickResume(View v){
        Intent intent = new Intent();
        intent.putExtra("Kill", false);
        setResult(Activity.RESULT_OK, intent);
        finish();

    }

    public void onClickQuit(View v){
        sonBulle = new Sons(R.raw.bulle, this, "son");
        Intent intent = new Intent();
        intent.putExtra("Kill", true);
        setResult(Activity.RESULT_OK, intent);
        finish();



    }

    public void onCrossQuit(){
        super.onBackPressed();

    }
    @Override
    public void onBackPressed(){
        onClickQuit(findViewById(R.id.boutonQuitter));

    }
}
