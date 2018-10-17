package jtb.supermaster;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorManager;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.TimeUnit;

import jtb.supermaster.Mini_Jeux.Basket;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

@RunWith(AndroidJUnit4.class)
public class Basket_Android_Test {


    @Rule
    public ActivityTestRule<Basket> mActivityRule = new ActivityTestRule<>(Basket.class);

    @Before
    public void startTest() {
        InstrumentationRegistry.getInstrumentation().waitForIdleSync();
    }

    @Test
    public void panier() {
        Basket demo = mActivityRule.getActivity();

        onView(withId(R.id.layoutAction)).perform(click());

        try {
            TimeUnit.MILLISECONDS.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        assertNotEquals(demo.getScore(), 0);
        assertEquals(demo.getVie(), 3);
    }

    @Test
    public void pasPanierGauche() {
        final Basket demo = mActivityRule.getActivity();

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

        onView(withId(R.id.layoutAction)).perform(click());

        try {
            TimeUnit.MILLISECONDS.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        assertEquals(demo.getScore(), 0);
        assertEquals(demo.getVie(), 2);
    }

    @Test
    public void pasPanierDroite() {
        final Basket demo = mActivityRule.getActivity();

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

        onView(withId(R.id.layoutAction)).perform(click());

        try {
            TimeUnit.MILLISECONDS.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        assertEquals(demo.getScore(), 0);
        assertEquals(demo.getVie(), 2);
    }
}
