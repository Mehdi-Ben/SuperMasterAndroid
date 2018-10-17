package jtb.supermaster;

import org.junit.Before;
import org.junit.Test;


import jtb.supermaster.Mini_Jeux.ArrowGame;
import jtb.supermaster.Mini_Jeux.Vector2D;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;


public class ArrowGamesTest {
    ArrowGame game;

    @Before
    public void setUp()
    {
        game = new ArrowGame();
    }

    @Test
    public void numberOfArrow_isCorrect() {
        for (int level = 0; level < 30 ; level ++)
        {
            int expected;
            if (level < 5) { expected = 1;}
            else if (level < 14) {expected = 2;}
            else {expected = 3;}
            game.setNumberArrowByLevel(level);
            assertEquals(expected, game.nombreFleche);
        }
    }

    @Test
    public void settingTimeByLevel_isCorrect() {
        for (int level = 0; level < 30 ; level ++)
        {
            int base = 5000;
            float coef = 0.985f;
            int expected = (int)(base * Math.pow(coef ,level - 1));
            game.setTimeByLevel(level, base, coef);
            assertEquals(expected, game.maxTime);
        }
    }


    @Test
    public void numberOfVisibleArrow_isCorrect(){

    }
}
