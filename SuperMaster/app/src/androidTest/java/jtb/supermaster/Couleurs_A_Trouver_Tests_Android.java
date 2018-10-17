package jtb.supermaster;

import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.*;


import org.junit.Before;
import org.junit.runner.RunWith;


import android.graphics.Color;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;
import android.widget.TextView;


import java.util.concurrent.TimeUnit;

import jtb.supermaster.Mini_Jeux.Couleur_a_trouver;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withId;



@RunWith(AndroidJUnit4.class)
public class Couleurs_A_Trouver_Tests_Android {


    @Rule
    public ActivityTestRule<Couleur_a_trouver> mActivityRule = new ActivityTestRule<>(Couleur_a_trouver.class);


    @Before
    public void setUp() {

        InstrumentationRegistry.getInstrumentation().waitForIdleSync();

    }


    @Test
    public void TestComparaisonCouleurs(){

        Couleur_a_trouver demo = mActivityRule.getActivity();

        int couleurJuste = Color.RED;
        int couleurFausse = Color.MAGENTA;
        String strJuste = "Rouge";


        assertTrue(demo.comparaisonCouleurString(strJuste, couleurJuste));
        assertFalse(demo.comparaisonCouleurString(strJuste, couleurFausse));
    }

    @Test
    public void ClickCouleurCorrecte() {

        try {
            TimeUnit.MILLISECONDS.sleep(300); // pour avoir le temps de voir
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Couleur_a_trouver demo = mActivityRule.getActivity();
        onView(withId(demo.getIDCorrect().getId())).perform(click());

        assertEquals(demo.getScore(),1);
        assertEquals(demo.getVie(), 3);

    }

    @Test
    public void ClickCouleurIncorrecte() {

        try {
            TimeUnit.MILLISECONDS.sleep(300); // pour avoir le temps de voir
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Couleur_a_trouver demo = mActivityRule.getActivity();
        TextView idClick = demo.getListofTextView()[0];

        for(int i = 0; i < demo.getNombreCouleur(); i++){

            if(i != demo.getIdTextViewCorrect()) {
                idClick = demo.getListofTextView()[i];
                break;
            }
        }


        onView(withId(idClick.getId())).perform(click());

        assertEquals(demo.getScore(),0);
        assertEquals(demo.getVie(), 2);

    }




    @Test
    public void TestCouleurCorrecte(){

        //Va tester si il y a bien une seule couleur correcte affichée et si la couleur correspond à la string

        Couleur_a_trouver demo = mActivityRule.getActivity();

        for (int i = 0; i < demo.getNombreCouleur(); i++){

            if (i == demo.getIdTextViewCorrect()) { //Si l'id est correct alors la string couleur est associe au bon entier
                assertTrue(demo.comparaisonCouleurString((String) demo.getListofTextView()[i].getText(), demo.getListofTextView()[i].getCurrentTextColor()));
            }
            else { //Sinon ça n'est pas le cas
                assertFalse(demo.comparaisonCouleurString((String) demo.getListofTextView()[i].getText(), demo.getListofTextView()[i].getCurrentTextColor()));
            }
        }
        }


    @Test
    public void TestAffichage(){

        // Va tester qu'on affiche bien le bon nombre de couleurs à l'écran

        Couleur_a_trouver demo = mActivityRule.getActivity();

        for (int i = 0; i < demo.getNombreTextView(); i++){

            if(i < demo.getNombreCouleur()) {
                assertEquals(demo.getListofTextView()[i].getVisibility(), View.VISIBLE);
            }
            else {
                assertEquals(demo.getListofTextView()[i].getVisibility(), View.GONE);

            }
        }
    }


    
    //Fin Operation => Implementation du score

}
