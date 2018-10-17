package jtb.supermaster;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Intent;
import android.graphics.Color;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.runner.intent.IntentCallback;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;

import java.util.concurrent.TimeUnit;

import jtb.supermaster.Menus.Menu_Multi_IA;
import jtb.supermaster.Mini_Jeux.ArrowGame;
import jtb.supermaster.Mini_Jeux.Basket;
import jtb.supermaster.Mini_Jeux.Couleur_a_trouver;
import jtb.supermaster.Mini_Jeux.Enigmes_a_resoudre;
import jtb.supermaster.Mini_Jeux.Jeu_pustules;
import jtb.supermaster.Mini_Jeux.Point_connection;
import jtb.supermaster.Modes.Mode_courseAuxPoints;
import jtb.supermaster.Serveur.Super_master_config;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static junit.framework.Assert.assertNull;
import static net.bytebuddy.agent.Installer.getInstrumentation;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


@RunWith(AndroidJUnit4.class)
public class Course_aux_points_test_android {


    @Rule
    public ActivityTestRule<Mode_courseAuxPoints> activityRule
            = new ActivityTestRule<>(
            Mode_courseAuxPoints.class,
            true,     // initialTouchMode
            false);   // launchActivity. False to customize the intent

    @Before
    public void setUp() {
        InstrumentationRegistry.getInstrumentation().waitForIdleSync();

    }

    @Test
    public void TestScoreIA(){

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
        intent.putExtra("listeJeux", "" + "couleurs%");
        activityRule.launchActivity(intent);
        Mode_courseAuxPoints demo = activityRule.getActivity();


        if(demo.getPointsAjouterIA() == 1)
        {
            assertEquals(demo.scoreIA,1);
        }

        if(demo.getPointsAjouterIA() == -1){
            assertEquals(demo.scoreIA,0);
        }
    }

    @Test
    public void JeuLance(){

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
        intent.putExtra("listeJeux", "" + "couleurs%demineur%basket%arrow%enigmes%points");
        activityRule.launchActivity(intent);
        Mode_courseAuxPoints demo = activityRule.getActivity();



        assertEquals(demo.getListeMiniJeux().get(0), "couleurs");
        assertEquals(demo.getListeMiniJeux().get(1), "demineur");
        assertEquals(demo.getListeMiniJeux().get(2), "basket");
        assertEquals(demo.getListeMiniJeux().get(3), "arrow");
        assertEquals(demo.getListeMiniJeux().get(4), "enigmes");
        assertEquals(demo.getListeMiniJeux().get(5), "points");

        assertEquals(demo.getActivites().get(0), Couleur_a_trouver.class);
        assertEquals(demo.getActivites().get(1), Jeu_pustules.class);
        assertEquals(demo.getActivites().get(2), Basket.class);
        assertEquals(demo.getActivites().get(3), ArrowGame.class);
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
        Mode_courseAuxPoints demo = activityRule.getActivity();
        assertFalse(demo.getActivityResultIsReturned());
        assertNull(demo.getActivityResult());
        try {
            TimeUnit.MILLISECONDS.sleep(18000); // pour avoir le temps de voir
        } catch (InterruptedException e) {
            e.printStackTrace();}
        assertEquals(demo.getIdJeu(), 2);
    }

    @Test
    public void TestIncrementDifficulte() {
        Intent intent = new Intent();
        intent.putExtra("mode", "IA");
        intent.putExtra("difficulte", ""+0);
        intent.putExtra("difficulteIA", ""+85);
        intent.putExtra("unites", ""+50);
        intent.putExtra("listeJeux", "" + "couleurs%demineur%basket%arrow%enigmes%points");
        activityRule.launchActivity(intent);
        Mode_courseAuxPoints demo = activityRule.getActivity();
        assertEquals(demo.getDifficulte(), 0);
        try {
            TimeUnit.MILLISECONDS.sleep(20000); // pour avoir le temps de voir
        } catch (InterruptedException e) {
            e.printStackTrace();}
        if (demo.getIdJeu() == 0)
            assertEquals(demo.getDifficulte(),1);
    }
}
