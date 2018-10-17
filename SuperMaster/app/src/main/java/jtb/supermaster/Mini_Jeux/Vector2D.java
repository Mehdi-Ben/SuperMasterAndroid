package jtb.supermaster.Mini_Jeux;

/**
 * Created by Maxime on 26/02/2017.
 */

public class Vector2D
{
    // Creation d'un vecteur avec deux points
    private double x,y;

    public Vector2D(double x, double y) // Createur avec deux réels
    {
        this.x = x;
        this.y = y;
    }

    public Vector2D(Vector2D v, Vector2D w) // Creation d'un vecteur a partir de deux autres vecteurs
    {
        this.x = w.getX() - v.getX();
        this.y = w.getY() - v.getY();
    }

    public Vector2D add(Vector2D v)
    {
        // Retourne la somme du vecteur v avec le vecteur courant
        return new Vector2D(this.x + v.getX(), this.y + v.getY());
    }

    public Vector2D multScal(float k)
    {
        // Retourne la multiplacation par un scalaire
        return new Vector2D(this.x * k, this.y * k);
    }

    public double magnitude()
    {
        /* Retourne la magnitude (longueur) du vecteur */
        return Math.sqrt( this.x * this.x + this.y*this.y );
    }

    public void normalized()
    {
        /* Modifie le vecteurs afin que sa distance soit égale à 1*/
        double m = this.magnitude();
        this.x /= m;
        this.y /= m;
    }

    /* GETTER & SETTER */

    public double getX() {
        return x;
    }
    public double getY() {
        return y;
    }

    public void setX(float x) {
        this.x = x;
    }
    public void setY(float y) {
        this.y = y;
    }
    
    @Override
    public String toString() {
        return "Vector2D{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        double delta = 0.0000001;
        if (obj instanceof Vector2D) {
            Vector2D v = (Vector2D) obj;
            return (Math.abs(this.x - v.x) <= delta) && (Math.abs(this.y - v.y) <= delta);
        }
        return false;
    }
}
