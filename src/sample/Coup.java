package sample;

import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;



public class Coup {

    private int X;
    private int Y;
    private Color joueur;
    private Label label;
    private int poids;

    public Coup()
    {
        this.X = 0;
        this.Y = 0;
        this.joueur = null;
    }

    public Coup(int x, int y, Color joueur)
    {
        this.X = x;
        this.Y = y;
        this.joueur = joueur;
        this.label = new Label(((joueur == Color.WHITE)?"BLANC":"NOIR  ") + " X: " + (X+1) + " Y: " + (Y+1));
        this.label.setFont(Othello.fontCollege);
    }

    public int getX() {
        return X;
    }

    public void setX(int x) {
        X = x;
    }

    public int getY() {
        return Y;
    }

    public void setY(int y) {
        Y = y;
    }

    public Color getJoueur() {
        return joueur;
    }

    public void setJoueur(Color joueur) {
        this.joueur = joueur;
    }

    public Label getLabel() {
        return label;
    }

    public void setLabel(Label label) {
        this.label = label;
    }

    public int getPoids() {
        return poids;
    }

    public void setPoids(int poids) {
        this.poids = poids;
    }
}
