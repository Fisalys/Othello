package sample;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Place extends Rectangle
{
    private int posTabX;
    private int posTabY;
    private Pion pion;
    private Color color;
    private int poids;

    public Place()
    {
        super();
        this.pion = null;
    }
    public Place(int posTabX, int posTabY, Color color)
    {
        super();
        this.posTabX = posTabX;
        this.posTabY = posTabY;
        this.color = color;
        this.pion = null;
    }
    public Place(int posTabX, int posTabY, Pion pion)
    {
        super();
        this.posTabX = posTabX;
        this.posTabY = posTabY;
        this.pion = pion;
    }

    public int getPosTabX() {
        return posTabX;
    }

    public void setPosTabX(int posTabX) {
        this.posTabX = posTabX;
    }

    public int getPosTabY() {
        return posTabY;
    }

    public void setPosTabY(int posTabY) {
        this.posTabY = posTabY;
    }

    public Pion getPion() {
        return pion;
    }

    public void setPion(Pion pion) {
        this.pion = pion;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public int getPoids() {
        return poids;
    }

    public void setPoids(int poids) {
        this.poids = poids;
    }
    public Place clone()
    {
        Place clone = new Place();
        clone.setX(this.getX());
        clone.setY(this.getY());
        clone.setPosTabX(this.getPosTabX());
        clone.setPosTabY(this.getPosTabY());
        clone.setColor(this.getColor());
        if(this.pion != null)
            clone.setPion(new Pion((int)this.pion.getCenterX(), (int)this.pion.getCenterY(), this.pion.getColor()));
        return clone;
    }
}
