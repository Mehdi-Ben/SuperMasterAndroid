package jtb.supermaster;

import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

import jtb.supermaster.Mini_Jeux.ArrowGame;
import jtb.supermaster.Mini_Jeux.Basket;
import jtb.supermaster.Mini_Jeux.Couleur_a_trouver;
import jtb.supermaster.Mini_Jeux.Enigmes_a_resoudre;
import jtb.supermaster.Mini_Jeux.Jeu_pustules;
import jtb.supermaster.Mini_Jeux.Point_connection;
import jtb.supermaster.Modes.Mode_contreLaMontre;
import jtb.supermaster.Modes.Mode_courseAuxPoints;

import static junit.framework.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;

/**
 * Created by blanc on 24/04/2017.
 */

public class Contre_la_montre_test {
    @Rule
    public ActivityTestRule<Mode_contreLaMontre> activityRule
            = new ActivityTestRule<>(
            Mode_contreLaMontre.class,
            true,     // initialTouchMode
            false);   // launchActivity. False to customize the intent

    @Before
    public void setUp() {
        InstrumentationRegistry.getInstrumentation().waitForIdleSync();

    }

    @Test
    public void JeuCouleurLance(){

        try {
            TimeUnit.MILLISECONDS.sleep(300); // pour avoir le temps de voir
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Intent intent = new Intent();
        intent.putExtra("mode", "IA");
        intent.putExtra("difficulte", ""+0);
        intent.putExtra("difficulteIA", ""+85);
        intent.putExtra("unites", ""+15);
        intent.putExtra("mini_jeu", "demineur");
        activityRule.launchActivity(intent);
        Mode_contreLaMontre demo = activityRule.getActivity();

        assertEquals(demo.getActivites().get(0), ArrowGame.class);
        assertEquals(demo.getActivites().get(1), Basket.class);
        assertEquals(demo.getActivites().get(2), Couleur_a_trouver.class);
        assertEquals(demo.getActivites().get(3), Jeu_pustules.class);
        assertEquals(demo.getActivites().get(4), Enigmes_a_resoudre.class);
        assertEquals(demo.getActivites().get(5), Point_connection.class);
    }

    @Test
    public void TestOnActivityResult(){
        Intent intent = new Intent();
        intent.putExtra("mode", "IA");
        intent.putExtra("difficulte", ""+0);
        intent.putExtra("difficulteIA", ""+85);
        intent.putExtra("unites", ""+15);
        intent.putExtra("listeJeux", "" + "couleurs%demineur%basket%arrow%enigmes%points");
        activityRule.launchActivity(intent);
        Mode_contreLaMontre demo = activityRule.getActivity();
        assertFalse(demo.getActivityResultIsReturned());
        assertNull(demo.getActivityResult());
        try {
            TimeUnit.MILLISECONDS.sleep(18000); // pour avoir le temps de voir
        } catch (InterruptedException e) {
            e.printStackTrace();}
        assertEquals(demo.getIdJeu(), 2);
    }

    @Test
    public void TestOnActivityResultTemps(){
        Intent intent = new Intent();
        intent.putExtra("mode", "IA");
        intent.putExtra("difficulte", ""+0);
        intent.putExtra("difficulteIA", ""+85);
        intent.putExtra("unites", ""+15);
        intent.putExtra("listeJeux", "" + "couleurs%demineur%basket%arrow%enigmes%points");
        activityRule.launchActivity(intent);
        Mode_contreLaMontre demo = activityRule.getActivity();
        try {
            TimeUnit.MILLISECONDS.sleep(18000); // pour avoir le temps de voir
        } catch (InterruptedException e) {
            e.printStackTrace();}
        assertEquals(demo.getTempsMaitreJeu(), 15);
    }
}
