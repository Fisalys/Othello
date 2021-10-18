package sample;

import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Othello
{
    private List<Place> plateau;
    private List<Place> caseJouables;
    private Color joueur;
    private static Color CRIMSON2 = Color.color(1f, 0.15f, 0.30f);
    private Group aff;

    public Othello(Scene scene)
    {
        aff = new Group();

        plateau = new ArrayList<>();
        caseJouables = new ArrayList<>();
        joueur = Color.BLACK;
        creerPlateau(scene);
        choixPossible(Color.BLACK);
        draw(aff);
    }

    public void creerPlateau(Scene scene)
    {
        int x = 350;
        int y = 100;
        int a = 0;
        Color c;
        for(int i = 0; i<8; i++)
        {
            for(int j = 0; j<8; j++)
            {
                c = (a == 0)?Color.web("#D09494"):Color.web("#FF5733");
                Place p = new Place(i, j, c);

                p.setX(x);
                p.setY(y);
                p.setHeight(100);
                p.setWidth(100);
                p.setStroke(Color.BLACK);

                p.setFill(c);
                p.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent mouseEvent) {
                        if(p.getFill() == CRIMSON2)
                        {
                            placerCoups(joueur, p.getPosTabY(), p.getPosTabX(), aff);

                            joueur = (joueur == Color.BLACK) ? Color.WHITE: Color.BLACK;
                            if(choixPossible(joueur) ==0)
                            {
                                joueur = (joueur == Color.BLACK) ? Color.WHITE: Color.BLACK;
                                if(choixPossible(joueur) == 0) {
                                    System.out.println("La partie est terminée");
                                }
                            }
                            draw(aff);
                        }
                    }
                });
                
                if((i == 3 && j == 3) || (i == 4 && j == 4)) //Ajout des position de départ des pion
                    p.setPion(new Pion(x+50, y+50, Color.WHITE));
                if((i == 3 && j == 4) || (i == 4 && j == 3))
                    p.setPion(new Pion(x+50, y+50, Color.BLACK));
                plateau.add(p);
                a = (a==0)?1:0;
                y += 100;
            }
            y = 100;
            a = (a==0)?1:0;
            x += 100;
        }
    }

    public Place getPlace(int x, int y)
    {
        Place result = null;
        for(Place p: plateau)
            if(p.getPosTabX()==x && p.getPosTabY() == y)
                result = p;

        return result;
    }

    public void draw(Group root)
    {
        root.getChildren().clear();
        for(Place p:plateau)
            root.getChildren().add(p);

        for(Place p:plateau)
                if(p.getPion() != null)
                    root.getChildren().add(p.getPion());
        for(Place p:caseJouables)
        {
            Color temp= (joueur != Color.BLACK)? Color.color(1,1,1,0.25): Color.color(0,0,0,0.25);
            Circle c =  new Circle(15, temp);
            c.setCenterX(p.getX()+50);
            c.setCenterY(p.getY()+50);
            root.getChildren().add(c);

        }
    }

    public int choixPossible(Color color)
    {
        int nb = 0;
        this.joueur = color;
        for(Place p:plateau){
            if(p.getPion() != null && p.getPion().getColor() == color)
            {
                for(int i = 1; i >= -1; i--)
                    for(int j = 1; j >= -1; j--)
                    {
                        if(i != 0 || j != 0)
                        {
                            Place temp = getPlace(p.getPosTabX()+i, p.getPosTabY()+j);
                            if(temp != null &&temp.getPion() != null)
                            {
                                while(temp != null && temp.getPion() != null && temp.getPion().getColor() != color) {
                                    temp = getPlace(temp.getPosTabX() + i, temp.getPosTabY() + j);
                                }
                                if(temp != null && temp.getPion() == null) {
                                    temp.setFill(CRIMSON2);
                                    caseJouables.add(temp);
                                    nb++;
                                }
                            }
                        }
                    }
            }
        }

        return nb;
    }

    boolean estPossible(int ligne, int col, Color joueur, int dir)
    {
        int i;
        switch (dir) {
            case 1: // Haut
                i = ligne;
                while(i >=0) {
                    if (getPlace(i, col) != null && getPlace(i, col).getPion() != null && getPlace(i, col).getPion().getColor() == joueur)
                        return true;
                    i--;
                }
                break;
            case 2: // Bas
                i = ligne;
                while(i < 8) {
                    if (getPlace(i, col) != null && getPlace(i, col).getPion() != null && getPlace(i, col).getPion().getColor() == joueur)
                        return true;
                    i++;
                }
                break;
            case 3: // Droite
                i = col;
                while(i < 8) {
                    if (getPlace(ligne, i)!= null && getPlace(ligne, i).getPion() != null && getPlace(ligne, i).getPion().getColor() == joueur)
                        return true;
                    i++;
                }
                break;
            case 4: // Gauche
                i = col;
                while(i >= 0) {
                    if (getPlace(ligne, i)!= null && getPlace(ligne, i).getPion() != null && getPlace(ligne, i).getPion().getColor() == joueur)
                        return true;
                    i--;
                }
                break;
            case 5: // Haut Droite
                i = 1;
                while(ligne - i >=0 && col + i < 8) {
                    if (getPlace(ligne-i, col+i)!=null &&getPlace(ligne-i, col+i).getPion() != null && getPlace(ligne-i, col+i).getPion().getColor() == joueur)
                        return true;
                    i++;
                }
                break;
            case 6: // Haut Gauche
                i = 1;
                while(ligne - i >=0 && col - i >= 0) {
                    if (getPlace(ligne-i, col-i)!=null &&getPlace(ligne-i, col-i).getPion() != null && getPlace(ligne-i, col-i).getPion().getColor() == joueur)
                        return true;
                    i++;
                }
                break;
            case 7: // Bas Droite
                i = 1;
                while(ligne + i < 8 && col + i < 8) {
                    if (getPlace(ligne+i, col+i)!= null && getPlace(ligne+i, col+i).getPion() != null && getPlace(ligne+i, col+i).getPion().getColor() == joueur)
                        return true;
                    i++;
                }
                break;
            case 8: // Bas Gauche
                i = 1;
                while(ligne + i < 8 && col - i >= 0) {
                    if (getPlace(ligne+i, col-i)!=null &&getPlace(ligne+i, col-i).getPion() != null && getPlace(ligne+i, col-i).getPion().getColor() == joueur)
                        return true;
                    i++;
                }
                break;
            default:
                return false;
        }
        return false;
    }

    public void placerCoups(Color joueur, int x, int y, Group root)
    {
        Color c;

        if(caseJouables.contains(getPlace(y, x)))
        {
            getPlace(y, x).setFill(Color.GREEN);
            getPlace(y, x).setPion(new Pion((int)getPlace(y, x).getX()+50, (int)getPlace(y, x).getY()+50, joueur));
            for(int i = -1; i <= 1; i++)
            {
                for(int j = -1; j <= 1; j++)
                {
                    Place temp = getPlace(y+i, x+j);
                    int dir = (i == -1 && j == -1)  ? 6 :
                            (i == -1 && j == 0) ? 1 :
                                    (i == -1 && j == 1) ? 5:
                                            (i == 0 && j == -1) ? 4:
                                                    (i == 0 && j == 1)? 3:
                                                            (i == 1 && j == -1)? 8:
                                                                    (i == 1 && j == 0)?2:
                                                                            (i == 1 && j == 1)? 7:0;

                    if(dir!=0&&temp != null && temp.getPion() != null && temp.getPion().getColor() != joueur)
                    {
                        if(estPossible(y+i, x+j, joueur, dir)){

                            while(temp != null && temp.getPion() != null && temp.getPion().getColor() != joueur)
                            {
                                temp.getPion().setFill(joueur);
                                temp.getPion().setColor(joueur);
                                temp = getPlace(temp.getPosTabX()+i, temp.getPosTabY()+j);


                            }
                        }
                    }
                }
            }
        }
        for(Place p: caseJouables){
            p.setFill(p.getColor());

        }
        caseJouables.clear();

    }
    public void resetPartie(Scene scene)
    {
        plateau.clear();
        caseJouables.clear();
        joueur = Color.BLACK;
        creerPlateau(scene);
        choixPossible(joueur);
        draw(aff);
    }

    public Group getAff() {
        return aff;
    }


}


