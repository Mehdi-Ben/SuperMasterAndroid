package jtb.supermaster.Mini_Jeux;

import jtb.supermaster.R;

/**
 * Created by Maxime on 12/04/2017.
 */

public enum Arrow{
    UP(R.drawable.uparrow, new Vector2D(0, -1)),
    RIGHT(R.drawable.rightarrow, new Vector2D(1, 0)),
    DOWN(R.drawable.downarrow, new Vector2D(0, 1)),
    LEFT(R.drawable.leftarrow, new Vector2D(-1, 0));


    int img;
    Vector2D vect;

    Arrow(int img, Vector2D vect)
    {
        this.img = img;
        this.vect = vect;
    }

}
