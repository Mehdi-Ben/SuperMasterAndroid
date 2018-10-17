package jtb.supermaster;

import android.content.Context;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorManager;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.widget.FrameLayout;
import android.widget.TextView;

import jtb.supermaster.Mini_Jeux.Enigme;
import jtb.supermaster.Mini_Jeux.Enigmes;
import jtb.supermaster.Mini_Jeux.Enigmes_a_resoudre;


import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.TimeUnit;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

@RunWith(AndroidJUnit4.class)
public class enigmes_a_resoudre_Android_Test {
    Enigmes enigmes = null;
    Enigme monEnigme = null;

    @Rule
    public ActivityTestRule<Enigmes_a_resoudre> mActivityRule = new ActivityTestRule<>(Enigmes_a_resoudre.class);

    @Before
    public void startTest() {
        InstrumentationRegistry.getInstrumentation().waitForIdleSync();
        enigmes = new Enigmes();
        monEnigme = this.enigmes.next();
    }

    @Test
    public void clicSansInclinaison() {
        Enigmes_a_resoudre demo = mActivityRule.getActivity();
        onView(withId(R.id.layoutAction)).perform(click());
        assertEquals(demo.getScore(),0);
        assertEquals(demo.getVie(), 3);
    }

    @Test
    public void bonneReponse() {
        final Enigmes_a_resoudre demo = mActivityRule.getActivity();
        final TextView tengime = (TextView) demo.findViewById(R.id.enigme);
        final TextView droite = (TextView) demo.findViewById(R.id.droite);
        final TextView gauche = (TextView) demo.findViewById(R.id.gauche);
        SensorManager sensorManager = (SensorManager) demo.getSystemService(Context.SENSOR_SERVICE);

        monEnigme = getEnigmeTest();
        demo.setEnigme(monEnigme);

        demo.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                tengime.setText(monEnigme.getQuestion());
                droite.setText(monEnigme.getDroite());
                gauche.setText(monEnigme.getGauche());
            }
        });

        float v = 5;
        long timestamp = 1;
        Sensor accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        demo.desabonnementAccelerometre(sensorManager, accelerometer);
        try{
            final SensorEvent se = SensorEventAction.getSensorEvent(v, timestamp, accelerometer);
            demo.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    demo.onSensorChanged(se);
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }

        try {
            TimeUnit.MILLISECONDS.sleep(1000);
        } catch (InterruptedException e) {
           e.printStackTrace();
        }

        onView(withId(R.id.layoutAction)).perform(click());

        assertEquals(demo.getScore(), 1);
        assertEquals(demo.getVie(), 3);
    }

    @Test
    public void mauvaiseReponse() {
        final Enigmes_a_resoudre demo = mActivityRule.getActivity();
        FrameLayout layoutAction = (FrameLayout) demo.findViewById(R.id.layoutAction);
        TextView Score = (TextView) demo.findViewById(R.id.score);
        final TextView tengime = (TextView) demo.findViewById(R.id.enigme);
        final TextView droite = (TextView) demo.findViewById(R.id.droite);
        final TextView gauche = (TextView) demo.findViewById(R.id.gauche);
        SensorManager sensorManager = (SensorManager) demo.getSystemService(Context.SENSOR_SERVICE);

        monEnigme = getEnigmeTest();
        demo.setEnigme(monEnigme);

        demo.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                tengime.setText(monEnigme.getQuestion());
                droite.setText(monEnigme.getDroite());
                gauche.setText(monEnigme.getGauche());
            }
        });

        float v = -5;
        long timestamp = 1;
        Sensor accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        demo.desabonnementAccelerometre(sensorManager, accelerometer);
        try{
            final SensorEvent se = SensorEventAction.getSensorEvent(v, timestamp, accelerometer);
            demo.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    demo.onSensorChanged(se);
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }

        try {
            TimeUnit.MILLISECONDS.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        onView(withId(R.id.layoutAction)).perform(click());

        assertEquals(demo.getScore(), 0);
        assertEquals(demo.getVie(), 2);
    }

    @Test
    public void testCouleurGauche() {
        final Enigmes_a_resoudre demo = mActivityRule.getActivity();
        SensorManager sensorManager = (SensorManager) demo.getSystemService(Context.SENSOR_SERVICE);

        float v = 5;
        long timestamp = 1;
        Sensor accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        demo.desabonnementAccelerometre(sensorManager, accelerometer);
        try{
            final SensorEvent se = SensorEventAction.getSensorEvent(v, timestamp, accelerometer);
            demo.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    demo.onSensorChanged(se);
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }

        try {
            TimeUnit.MILLISECONDS.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        assertEquals(demo.getColorGauche(), Color.RED);
        assertEquals(demo.getColorDroite(), Color.BLACK);
    }

    @Test
    public void testCouleurDroite() {
        final Enigmes_a_resoudre demo = mActivityRule.getActivity();
        SensorManager sensorManager = (SensorManager) demo.getSystemService(Context.SENSOR_SERVICE);

        float v = -5;
        long timestamp = 1;
        Sensor accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        demo.desabonnementAccelerometre(sensorManager, accelerometer);
        try{
            final SensorEvent se = SensorEventAction.getSensorEvent(v, timestamp, accelerometer);
            demo.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    demo.onSensorChanged(se);
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }

        try {
            TimeUnit.MILLISECONDS.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        assertEquals(demo.getColorDroite(), Color.RED);
        assertEquals(demo.getColorGauche(), Color.BLACK);
    }

    @Test
    public void pasAssezInclinaisonGauche() {
        final Enigmes_a_resoudre demo = mActivityRule.getActivity();
        SensorManager sensorManager = (SensorManager) demo.getSystemService(Context.SENSOR_SERVICE);

        float v = 2;
        long timestamp = 1;
        Sensor accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        demo.desabonnementAccelerometre(sensorManager, accelerometer);
        try{
            final SensorEvent se = SensorEventAction.getSensorEvent(v, timestamp, accelerometer);
            demo.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    demo.onSensorChanged(se);
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }

        try {
            TimeUnit.MILLISECONDS.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        assertEquals(demo.getColorDroite(), Color.BLACK);
        assertEquals(demo.getColorGauche(), Color.BLACK);
    }

    @Test
    public void pasAssezInclinaisonDroite() {
        final Enigmes_a_resoudre demo = mActivityRule.getActivity();
        SensorManager sensorManager = (SensorManager) demo.getSystemService(Context.SENSOR_SERVICE);

        float v = -2;
        long timestamp = 1;
        Sensor accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        demo.desabonnementAccelerometre(sensorManager, accelerometer);
        try{
            final SensorEvent se = SensorEventAction.getSensorEvent(v, timestamp, accelerometer);
            demo.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    demo.onSensorChanged(se);
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }

        try {
            TimeUnit.MILLISECONDS.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        assertEquals(demo.getColorDroite(), Color.BLACK);
        assertEquals(demo.getColorGauche(), Color.BLACK);
    }

    public Enigme getEnigmeTest() {
        int nombre = enigmes.getNombre();

        for (int i = 0; i < nombre; i++) {
            if(monEnigme.getQuestion().equals("Quelle est la formule chimique de la molécule d'eau ?")){
                return monEnigme;
            }
            else
                monEnigme = enigmes.next();
        }
        return monEnigme;
    }



}
