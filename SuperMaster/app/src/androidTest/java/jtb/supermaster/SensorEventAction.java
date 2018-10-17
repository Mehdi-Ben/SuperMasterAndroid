package jtb.supermaster;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.view.View;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class SensorEventAction {

    public static SensorEvent getSensorEvent(float v, long timestamp, Sensor sensor) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        SensorEvent e = null;

            Constructor<SensorEvent> constructor = SensorEvent.class.getDeclaredConstructor(int.class) ;

            // int.class);
            constructor.setAccessible(true);
             e = constructor.newInstance(3);

            e.values[0] = v;
            e.timestamp = timestamp;
            e.sensor = sensor;

        return e;
    }

    public static ViewAction Inclinaison(final float v, final long timestamp, final Sensor sensor) {
        return new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return new TypeSafeMatcher<View>() {
                    @Override
                    public void describeTo(Description description) {
                        description.appendText("is expected to be a SensorListener");
                    }

                    @Override
                    protected boolean matchesSafely(View item) {
                        return SensorEventListener.class.isInstance(item);
                    }
                };
            }

            @Override
            public String getDescription() {
                return "action pour tester une inclinaison : " + v;
            }

            @Override
            public void perform(UiController uiController, View view) {
                SensorEvent e = null;
                try {
                    e = getSensorEvent(v, timestamp, sensor);
                } catch (NoSuchMethodException e1) {
                    e1.printStackTrace();
                } catch (IllegalAccessException e1) {
                    e1.printStackTrace();
                } catch (InvocationTargetException e1) {
                    e1.printStackTrace();
                } catch (InstantiationException e1) {
                    e1.printStackTrace();
                }

                SensorEventListener asListener = (SensorEventListener) view;
                asListener.onSensorChanged(e);

            }
        };
    }
}
