package jtb.supermaster;

import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;
import android.widget.ImageView;

import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.TimeUnit;

import jtb.supermaster.Mini_Jeux.Arrow;
import jtb.supermaster.Mini_Jeux.ArrowGame;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.action.ViewActions.swipeDown;
import static android.support.test.espresso.action.ViewActions.swipeLeft;
import static android.support.test.espresso.action.ViewActions.swipeRight;
import static android.support.test.espresso.action.ViewActions.swipeUp;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;


@RunWith(AndroidJUnit4.class)
public class Arrow_Games_Android_Test {


    ArrowGame demo;
    @Rule
    public ActivityTestRule<ArrowGame> mActivityRule = new ActivityTestRule<>(ArrowGame.class);


    @Before
    public void setUp(){
        InstrumentationRegistry.getInstrumentation().waitForIdleSync();
        demo = mActivityRule.getActivity();
    }

    @Test
    public void gestureDown_isCorrect(){
        onView(withId(R.id.layoutAction)).perform(swipeDown());
        assertEquals(Arrow.DOWN, demo.findGest(demo.vectMouvement));
    }

    @Test
    public void gestureUp_isCorrect(){
        onView(withId(R.id.layoutAction)).perform(swipeUp());
        assertEquals(Arrow.UP, demo.findGest(demo.vectMouvement));
    }

    @Test
    public void gestureLeft_isCorrect(){
        onView(withId(R.id.layoutAction)).perform(swipeLeft());
        assertEquals(Arrow.LEFT, demo.findGest(demo.vectMouvement));
    }

    @Test
    public void gestureRight_isCorrect(){
        onView(withId(R.id.layoutAction)).perform(swipeRight());
        assertEquals(Arrow.RIGHT, demo.findGest(demo.vectMouvement));
    }

    @Test
    public void game_isWinning(){
        if (demo.listofArrow[0] == Arrow.DOWN) {
            onView(withId(R.id.layoutAction)).perform(swipeRight());
        }
        else{
            onView(withId(R.id.layoutAction)).perform(swipeDown());
        }
        assertEquals(1, demo.score);
    }





}
