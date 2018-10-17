package jtb.supermaster;


import org.junit.Test;

import jtb.supermaster.Mini_Jeux.Vector2D;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */

public class Vector2DTest {

    @Test
    public void constructeurVecteur_isCorrect() {
        Vector2D A = new Vector2D(1, 7);
        Vector2D B = new Vector2D(7, 5);
        Vector2D R = new Vector2D(A, B);
        assertTrue(R.equals(new Vector2D(7-1, 5-7)));
    }

    @Test
    public void add_isCorrect(){
        Vector2D A = new Vector2D(7, 2);
        Vector2D B = new Vector2D(-2, -8);
        Vector2D R = A.add(B);
        assertEquals(R, new Vector2D(5,-6));
    }

    @Test
    public void multScal_isCorrect(){
        Vector2D A = new Vector2D(4, 1);
        Vector2D R = A.multScal(2);
        assertTrue(R.equals(new Vector2D(8,2)));
    }

    @Test
    public void magnitude_isCorrect(){
        Vector2D A = new Vector2D(3,4);
        assertEquals(A.magnitude(), 5.0f, 0.001);
    }

    @Test
    public void normalized_isCorrect()
    {
        Vector2D A = new Vector2D(5,0);
        A.normalized();
        assertEquals(A, new Vector2D(1,0));
    }
}