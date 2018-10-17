package jtb.supermaster;

import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.view.MotionEvent;
import android.view.View;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Random;
import java.util.concurrent.TimeUnit;

import jtb.supermaster.Mini_Jeux.Point_connection;
import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class Point_connection_android_test {

    @Mock
    Context mMockContext;

    @Rule
    public ActivityTestRule<Point_connection> activityRule
            = new ActivityTestRule<>(
            Point_connection.class,
            true,     // initialTouchMode
            false);   // launchActivity. False to customize the inten

    @Before
    public void setUp() {
        InstrumentationRegistry.getInstrumentation().waitForIdleSync();
    }

    public static void lancementPartie(int difficulte , ActivityTestRule<Point_connection> activityRule) {
        Intent intent = new Intent();
        intent.putExtra("DIFFICULTE", difficulte);
        activityRule.launchActivity(intent);
        Point_connection demo1 = activityRule.getActivity();

        if(difficulte == 0){
            assertEquals(demo1.getListOfTextView().size(), 1);
        }else if(difficulte < 11){
            assertEquals(demo1.getListOfTextView().size(), difficulte);
        }else {
            assertEquals(demo1.getListOfTextView().size(), 10);
        }
    }

    /* Test de la creation du bon nombre de points en fonction */
    /* d'une difficulte inferieur a 10 */
    @Test
    public void InitialisationPointsTestNombreDiffInfDix() {
        lancementPartie(5,activityRule);
    }

    /* Test de la creation du bon nombre de points en fonction */
    /* d'une difficulte superieur au nombre de Views maximum */
    @Test
    public void InitialisationPointsTestNombreDiffMax() {
        lancementPartie(11,activityRule);
    }

    /* Test l'incrementation du score en cas de victoire */
    @Test
    public void monTestIncrementPoint() {
        Intent intent = new Intent();
        activityRule.launchActivity(intent);
        Point_connection demo1 = activityRule.getActivity();
        long downTime = SystemClock.uptimeMillis();
        long eventTime = SystemClock.uptimeMillis();
        float y = demo1.getListOfTextView().get(0).getY();
        float x = demo1.getListOfTextView().get(0).getX();

        MotionEvent event = MotionEvent.obtain(downTime, eventTime, MotionEvent.ACTION_DOWN, x, y, 0);
        demo1.getListOfTextView().get(0).onTouchEvent(event);
        eventTime = SystemClock.uptimeMillis();
        event = MotionEvent.obtain(downTime, eventTime, MotionEvent.ACTION_UP, x, y, 0);
        demo1.getListOfTextView().get(0).onTouchEvent(event);

        assertEquals(demo1.getScore(), 1);
    }

    /* Fonction permettant d'effectiuer une serie de onTouchEvent */
    /* correspondant a un drag reussi dans l'odre sur toutes les Views du layout de jeu */
    public static void drag(View v,Point_connection dem, int k) {
        long downTime = SystemClock.uptimeMillis();
        long eventTime = SystemClock.uptimeMillis();

        float y = dem.getListOfTextView().get(0).getY();
        float x = dem.getListOfTextView().get(0).getX();

        MotionEvent event = MotionEvent.obtain(downTime, eventTime, MotionEvent.ACTION_DOWN, x, y, 0);
        v.onTouchEvent(event);
        for (int i = 2; i <= dem.getListOfPoints().size(); i++) {
            float y1 = dem.getListOfTextView().get(i-1).getY();
            float x1 = dem.getListOfTextView().get(i-1).getX();
            eventTime = SystemClock.uptimeMillis();
            event = MotionEvent.obtain(downTime, eventTime, MotionEvent.ACTION_MOVE, x1 - x, y1 - y, 0);
            v.onTouchEvent(event);
        }

        y = dem.getListOfTextView().get(0).getY();
        x = dem.getListOfTextView().get(0).getX();
        eventTime = SystemClock.uptimeMillis();
        event = MotionEvent.obtain(downTime, eventTime, MotionEvent.ACTION_UP, x, y, 0);
        v.onTouchEvent(event);

        assertEquals(dem.getScore(), k);
        assertEquals(dem.getVies(),3);
    }

    /* Test une serie de niveau reussi en verifiant l'incrementation du score  */
    /* et le fait que le nombre de vies reste inchange */
    @Test
    public void testDragsGagnant() {
        Intent intent = new Intent();
        activityRule.launchActivity(intent);
        Point_connection demo1 = activityRule.getActivity();
        for(int k = 1 ;k< 15;k++) {
            try {
                TimeUnit.MILLISECONDS.sleep(400); // pour avoir le temps de voir
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            drag(demo1.getListOfTextView().get(0), demo1, k);
        }
    }


    /* Fonction permettant d'effectiuer une serie de onTouchEvent correspondant a un drag rate sur les Views du layout de jeu*/
    /*  le "doigt" passe ou non sur une View sans respecter l'ordre ce qui cause une defaite aleatoirement */
    public static int[] dragPerduParTouchSurMauvaisBouton(View v,Point_connection dem, int pointsCourant, int vies) {
        long downTime = SystemClock.uptimeMillis();
        long eventTime = SystemClock.uptimeMillis();

        float y = v.getY();
        float x = v.getX();
        float y1 = 0;
        float x1 = 0;

        boolean boolPass = false;
        boolean partiePerdante = false;
        Random rand = new Random();
        int randomNum = rand.nextInt((dem.getListOfPoints().size() - 1) + 1) + 1;

        if (randomNum % 2 == 1  && dem.getListOfPoints().size() > 1) {
            partiePerdante = true;
        }
        MotionEvent event = MotionEvent.obtain(downTime, eventTime, MotionEvent.ACTION_DOWN, x, y, 0);
        v.onTouchEvent(event);
        for (int i = 1; i <= dem.getListOfPoints().size(); i++) {
            if(partiePerdante && i != randomNum){
                boolPass = true;
                y1 = dem.getListOfTextView().get(randomNum-1).getY();
                x1 = dem.getListOfTextView().get(randomNum-1).getX();
            }else{
                y1 = dem.getListOfTextView().get(i-1).getY();
                x1 = dem.getListOfTextView().get(i-1).getX();
            }
            eventTime = SystemClock.uptimeMillis();
            event = MotionEvent.obtain(downTime, eventTime, MotionEvent.ACTION_MOVE, x1 - x, y1 - y, 0);
            v.onTouchEvent(event);
        }

        if(partiePerdante && boolPass){
            eventTime = SystemClock.uptimeMillis();
            event = MotionEvent.obtain(downTime, eventTime, MotionEvent.ACTION_UP, x1, y1, 0);
            v.onTouchEvent(event);
            assertEquals(dem.getScore(), pointsCourant);
            try {
                TimeUnit.MILLISECONDS.sleep(300); // pour avoir le temps de voir
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return new int[]{pointsCourant,vies - 1};
        }else {
            y = v.getY();
            x = v.getX();
            eventTime = SystemClock.uptimeMillis();
            event = MotionEvent.obtain(downTime, eventTime, MotionEvent.ACTION_UP, x, y, 0);
            v.onTouchEvent(event);
            assertEquals(dem.getScore(), pointsCourant + 1);
            assertEquals(dem.getVies(), vies);
            return new int[]{pointsCourant + 1,vies};
        }
    }

    /* Test une serie de niveau reussi ou non aleatoirement en verifiant l'incrementation du score en cas de victoire */
    /* et la decrementation du nombre de vies en cas de defaite */
    @Test
    public void testDragsPerdantParTouchSurMauvaisBouton() {
        Intent intent = new Intent();
        activityRule.launchActivity(intent);
        Point_connection demo1 = activityRule.getActivity();
        int points = 0;
        int vies = 3;
        int[] tabResult;
        while(vies > 0) {
            try {
                TimeUnit.MILLISECONDS.sleep(300); // pour avoir le temps de voir
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
                tabResult = dragPerduParTouchSurMauvaisBouton(demo1.getListOfTextView().get(0), demo1, points,vies);
                points = tabResult[0];
                vies = tabResult[1];
        }
    }

    /* Fonction permettant d'effectiuer une serie de onTouchEvent correspondant a un drag rate sur les Views du layout de jeu*/
    /*  le "doigt" se releve trop tot ou non ce qui cause une defaite aleatoirement */
    public static int[] dragPerduParTouchRelevageDuDoigtTropTot(View v,Point_connection dem, int pointsCourant, int vies) {
        long downTime = SystemClock.uptimeMillis();
        long eventTime = SystemClock.uptimeMillis();

        boolean partiePerdante = false;

        float y = v.getY();
        float x = v.getX();
        float y1 = 0;
        float x1 = 0;

        boolean boolPerdu = false;
        Random rand = new Random();
        int randomNum = rand.nextInt((dem.getListOfPoints().size() - 1) + 1) + 1;

        if (randomNum % 2 == 1  && dem.getListOfPoints().size() > 1) {
            partiePerdante = true;
        }
        MotionEvent event = MotionEvent.obtain(downTime, eventTime, MotionEvent.ACTION_DOWN, x, y, 0);
        v.onTouchEvent(event);
        for (int i = 1; i <= dem.getListOfPoints().size(); i++) {
            y1 = dem.getListOfTextView().get(i-1).getY();
            x1 = dem.getListOfTextView().get(i-1).getX();
            if(partiePerdante && i != randomNum){
                boolPerdu = true;
                eventTime = SystemClock.uptimeMillis();
                event = MotionEvent.obtain(downTime, eventTime, MotionEvent.ACTION_UP, x, y, 0);
                v.onTouchEvent(event);
            }else{
                eventTime = SystemClock.uptimeMillis();
                event = MotionEvent.obtain(downTime, eventTime, MotionEvent.ACTION_MOVE, x1 - x, y1 - y, 0);
                v.onTouchEvent(event);
            }
        }

        if(partiePerdante && boolPerdu){
            assertEquals(dem.getScore(), pointsCourant);
            assertEquals(dem.getVies(), vies - 1);
            return new int[]{pointsCourant,vies - 1};
        }else {
            y = v.getY();
            x = v.getX();
            eventTime = SystemClock.uptimeMillis();
            event = MotionEvent.obtain(downTime, eventTime, MotionEvent.ACTION_UP, x, y, 0);
            v.onTouchEvent(event);
            assertEquals(dem.getScore(), pointsCourant + 1);
            assertEquals(dem.getVies(), vies);
            return new int[]{pointsCourant + 1,vies};
        }
    }

    /* Test une serie de niveau reussi ou non aleatoirement en verifiant l'incrementation du score en cas de victoire */
    /* et la decrementation du nombre de vies en cas de defaite */
    @Test
    public void testDragsPerdantParRelevageDuDoigtTropTot() {
        Intent intent = new Intent();
        activityRule.launchActivity(intent);
        Point_connection demo1 = activityRule.getActivity();
        int points = 0;
        int vies = 3;
        int[] tabResult;
        while(vies > 0) {
            try {
                TimeUnit.MILLISECONDS.sleep(1000); // pour avoir le temps de voir
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            tabResult = dragPerduParTouchRelevageDuDoigtTropTot(demo1.getListOfTextView().get(0), demo1, points,vies);
            points = tabResult[0];
            vies = tabResult[1];
        }
    }
}