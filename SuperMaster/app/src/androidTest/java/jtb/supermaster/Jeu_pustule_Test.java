package jtb.supermaster;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorManager;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import jtb.supermaster.Mini_Jeux.Enigme;
import jtb.supermaster.Mini_Jeux.Enigmes;
import jtb.supermaster.Mini_Jeux.Enigmes_a_resoudre;
import jtb.supermaster.Mini_Jeux.Jeu_pustules;
import jtb.supermaster.Mini_Jeux.Point_connection;


import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import static android.support.test.espresso.Espresso.getIdlingResources;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

@RunWith(AndroidJUnit4.class)
public class Jeu_pustule_Test {

    int score = 0;
    Jeu_pustules demo;
    boolean bombeRougeDejaTouchee = false;

    @Rule
    public ActivityTestRule<Jeu_pustules> activityRule
            = new ActivityTestRule<>(
            Jeu_pustules.class,
            true,     // initialTouchMode
            false);   // launchActivity. False to customize the intent

    @Before
    public void startTest() {
        InstrumentationRegistry.getInstrumentation().waitForIdleSync();
    }

    //****************METTRE ICI LE SCORE ET FAIRE ENSUITE
    // ASSERTeQUALS(demo.getScore(), score + 1);
    // score = demo.getScore();

    @Test
    public void monTest() {
        Intent intent = new Intent();
        activityRule.launchActivity(intent);
        demo = activityRule.getActivity();
        TextView Score = (TextView) demo.findViewById(R.id.score);
        onView(withId(R.id.layoutAction)).perform(click());
        assertEquals(demo.getScore(), 0);
    }

    @Test
    public void testGagnant(){
        Intent intent = new Intent();
        activityRule.launchActivity(intent);
        demo = activityRule.getActivity();
        for (int i = 0; i < 50; i++) {
            try {
                TimeUnit.MILLISECONDS.sleep(400); // pour avoir le temps de voir
            } catch (InterruptedException e) {
                e.printStackTrace();}
            for (int j = 0; j < demo.getListeView().size(); j++) {
                if ((Integer) demo.getListeView().get(j).getTag() == demo.getResources().getIdentifier("rond_noir3b", "drawable", demo.getPackageName())){
                    for (int k = 0; k < 3; k++){
                        demo.onClick(demo.getListeView().get(j));
                    }
                }
                else if ((Integer) demo.getListeView().get(j).getTag() != demo.getResources().getIdentifier("rond_rouge", "drawable", demo.getPackageName())){
                    demo.onClick(demo.getListeView().get(j));
                }
            }
            if (i > 0)
                assertEquals(demo.getScore(), i);
            if (i == 0){assertEquals(demo.getScore(), 0);}
        }
    }

    @Test
    public void testPerdantScore(){
        Intent intent = new Intent();
        activityRule.launchActivity(intent);
        demo = activityRule.getActivity();
        for (int i = 0; i < 32; i++) {
            bombeRougeDejaTouchee = false;
            try {
                TimeUnit.MILLISECONDS.sleep(400); // pour avoir le temps de voir
            } catch (InterruptedException e) {
                e.printStackTrace();}
            for (int j = 0; j < demo.getListeView().size(); j++) {
                if ((Integer) demo.getListeView().get(j).getTag() == demo.getResources().getIdentifier("rond_noir3b", "drawable", demo.getPackageName())){
                    for (int k = 0; k < 3; k++){
                        demo.onClick(demo.getListeView().get(j));
                    }
                }
                else if (((Integer) demo.getListeView().get(j).getTag() == demo.getResources().getIdentifier("rond_rouge", "drawable", demo.getPackageName()))&&(!bombeRougeDejaTouchee)){
                    demo.onClick(demo.getListeView().get(j));
                    bombeRougeDejaTouchee = true;
                }
                else {
                    demo.onClick(demo.getListeView().get(j));
                }
            }
            if ((i > 0)&&(i<=31))
                {assertEquals(demo.getScore(), i);}
            else if (i<= 0)
                {assertEquals(demo.getScore(), 0);}
        }
        assertEquals(demo.getVies(), 0);
    }

    @Test
    public void testGagnantVies(){
        Intent intent = new Intent();
        activityRule.launchActivity(intent);
        demo = activityRule.getActivity();
        for (int i = 0; i < 33; i++) {
            try {
                TimeUnit.MILLISECONDS.sleep(400); // pour avoir le temps de voir
            } catch (InterruptedException e) {
                e.printStackTrace();}
            for (int j = 0; j < demo.getListeView().size(); j++) {
                if ((Integer) demo.getListeView().get(j).getTag() == demo.getResources().getIdentifier("rond_noir3b", "drawable", demo.getPackageName())){
                    for (int k = 0; k < 3; k++){
                        demo.onClick(demo.getListeView().get(j));
                    }
                }
                else if ((Integer) demo.getListeView().get(j).getTag() != demo.getResources().getIdentifier("rond_rouge", "drawable", demo.getPackageName())){
                    demo.onClick(demo.getListeView().get(j));
                }
            }
        }
        assertEquals(demo.getVies(), 3);
    }


    @Test
    public void testPerdantVies(){
        Intent intent = new Intent();
        activityRule.launchActivity(intent);
        demo = activityRule.getActivity();
        for (int i = 0; i < 35; i++) {
            bombeRougeDejaTouchee = false;
            try {
                TimeUnit.MILLISECONDS.sleep(400); // pour avoir le temps de voir
            } catch (InterruptedException e) {
                e.printStackTrace();}
            for (int j = 0; j < demo.getListeView().size(); j++) {
                if ((Integer) demo.getListeView().get(j).getTag() == demo.getResources().getIdentifier("rond_noir3b", "drawable", demo.getPackageName())){
                    for (int k = 0; k < 3; k++){
                        demo.onClick(demo.getListeView().get(j));
                    }
                }
                else if (((Integer) demo.getListeView().get(j).getTag() == demo.getResources().getIdentifier("rond_rouge", "drawable", demo.getPackageName()))&&(!bombeRougeDejaTouchee)){
                    demo.onClick(demo.getListeView().get(j));
                    bombeRougeDejaTouchee = true;
                }
                else {
                    demo.onClick(demo.getListeView().get(j));
                }
            }
        }
        assertEquals(demo.getVies(), 0);
    }

    @Test
    public void testPerdant1FoisSur60Parties(){
        Intent intent = new Intent();
        activityRule.launchActivity(intent);
        demo = activityRule.getActivity();
        for (int i = 0; i < 60; i++) {
            try {
                TimeUnit.MILLISECONDS.sleep(400); // pour avoir le temps de voir
            } catch (InterruptedException e) {
                e.printStackTrace();}
            for (int j = 0; j < demo.getListeView().size(); j++) {
                if ((Integer) demo.getListeView().get(j).getTag() == demo.getResources().getIdentifier("rond_noir3b", "drawable", demo.getPackageName())){
                    for (int k = 0; k < 3; k++){
                        demo.onClick(demo.getListeView().get(j));
                    }
                }
                else if (((Integer) demo.getListeView().get(j).getTag() == demo.getResources().getIdentifier("rond_rouge", "drawable", demo.getPackageName()))&&(!bombeRougeDejaTouchee)){
                    demo.onClick(demo.getListeView().get(j));
                    bombeRougeDejaTouchee = true;
                    try {
                        TimeUnit.MILLISECONDS.sleep(1000); // pour avoir le temps de voir
                    } catch (InterruptedException e) {
                        e.printStackTrace();}
                }
                else if ((Integer) demo.getListeView().get(j).getTag() != demo.getResources().getIdentifier("rond_rouge", "drawable", demo.getPackageName())){
                    demo.onClick(demo.getListeView().get(j));
                }
            }
        }
        assertEquals(demo.getScore(), 59);
        assertEquals(demo.getVies(), 2);
    }

    @Test
    public void testBonNombreDeViewsEnFonctionDuNiveau(){
        Intent intent = new Intent();
        activityRule.launchActivity(intent);
        demo = activityRule.getActivity();
        for (int i = 0; i < 70; i++) {
            try {
                TimeUnit.MILLISECONDS.sleep(400); // pour avoir le temps de voir
            } catch (InterruptedException e) {
                e.printStackTrace();}
            for (int j = 0; j < demo.getListeView().size(); j++) {
                if ((Integer) demo.getListeView().get(j).getTag() == demo.getResources().getIdentifier("rond_noir3b", "drawable", demo.getPackageName())){
                    for (int k = 0; k < 3; k++){
                        demo.onClick(demo.getListeView().get(j));
                    }
                }
                else if ((Integer) demo.getListeView().get(j).getTag() != demo.getResources().getIdentifier("rond_rouge", "drawable", demo.getPackageName())){
                    demo.onClick(demo.getListeView().get(j));
                }
            }
            if (i==0){assertEquals(demo.getnbImagesAToucher(), 1);}
            else {
                if (i < 10){assertEquals(demo.getnbImagesAToucher(), i+1);}
                else if ((i >= 10)&&(i < 20)){assertEquals(demo.getnbImagesAToucher(), 8);}
                else if ((i >= 20)&&(i < 30)){assertEquals(demo.getnbImagesAToucher(), 9);}
                else if ((i >= 30)&&(i < 40)){assertEquals(demo.getnbImagesAToucher(), 9);}
                else if ((i >= 40)&&(i < 50)){assertEquals(demo.getnbImagesAToucher(), 10);}
                else if ((i >= 50)&&(i < 60)){assertEquals(demo.getnbImagesAToucher(), 11);}
                else{assertEquals(demo.getnbImagesAToucher(), 12);}
            }
        }
    }
}
