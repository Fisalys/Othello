package sample;

import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;

import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;
import java.util.List;

public class Othello
{
    private List<Place> plateau;
    private List<Place> caseJouables;
    private Color joueur;
    private static Color CRIMSON2 = Color.color(1f, 0.15f, 0.30f);
    private Group aff;
    private Color ia; // Est ce que l'on joue avec l'ia -> null pour indiquer que l'ia n'est pas la
    private List<Coup> coupJoue;
    private VBox coups;
    public static Font fontCollege = Font.loadFont("file:ressouces/college.ttf", 20);

    public Othello()
    {
        aff = new Group();
        ia = null;
        plateau = new ArrayList<>();
        caseJouables = new ArrayList<>();
        joueur = Color.BLACK;
        coupJoue = new LinkedList<>();
        coups= new VBox();
        creerPlateau();
        choixPossible(Color.BLACK);
        draw(aff);
    }

    public void creerPlateau()
    {
        int x = 350;
        int y = 100;
        int a = 0;
        Color c;
        Scanner lecteur = null;
        try {
            FileInputStream f = new FileInputStream("ressouces/Poids.txt");
            lecteur = new Scanner(f);
        }catch (IOException e)
        {
            e.printStackTrace();
        }
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
                int poids = 0;
                if (lecteur.hasNextLine()) {
                    poids = Integer.parseInt(lecteur.nextLine());
                    p.setPoids(poids);
                }

                p.setFill(c);
                p.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent mouseEvent) {
                        if(p.getFill() == CRIMSON2)
                        {
                            placerCoups(joueur, p.getPosTabY(), p.getPosTabX(), aff);
                            joueur = (joueur == Color.BLACK) ? Color.WHITE: Color.BLACK;
                            int nbChoix = choixPossible(joueur);
                            if(ia == joueur)
                            {
                                jouerIA();
                                joueur = (joueur == Color.BLACK) ? Color.WHITE: Color.BLACK;
                                nbChoix = choixPossible(joueur);
                            }
                            if(nbChoix==0)
                            {
                                joueur = (joueur == Color.BLACK) ? Color.WHITE: Color.BLACK;
                                choixPossible(joueur);
                                while(ia == joueur)
                                {
                                    jouerIA();
                                    joueur = (joueur == Color.BLACK) ? Color.WHITE: Color.BLACK;
                                    nbChoix = choixPossible(joueur);
                                    if(nbChoix == 0) {
                                        joueur = (joueur == Color.BLACK) ? Color.WHITE : Color.BLACK;
                                        if(choixPossible(joueur)==0)
                                            joueur = (joueur == Color.BLACK) ? Color.WHITE : Color.BLACK;
                                    }
                                }
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
        coups.getChildren().clear();
        for(Place p:plateau)
            root.getChildren().add(p);

        for(Place p:plateau)
                if(p.getPion() != null)
                    root.getChildren().add(p.getPion());
        for(Place p:caseJouables)
        {
            Color temp= (joueur != Color.BLACK)? Color.color(1,1,1,0.52): Color.color(0,0,0,0.52);
            Circle c =  new Circle(15, temp);
            c.setCenterX(p.getX()+50);
            c.setCenterY(p.getY()+50);
            root.getChildren().add(c);
        }

        for(Coup c: coupJoue)
        {
            coups.getChildren().add(c.getLabel());
        }


    }

    public boolean present(Place place)
    {
        for(Place p: caseJouables)
        {
            if(p.getPosTabX() == place.getPosTabX() && p.getPosTabY() == place.getPosTabY())
                return true;
        }
        return false;
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
                                if(temp != null && temp.getPion() == null && !(present(temp))) {
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
                    if(getPlace(i, col).getPion() == null)
                        return false;
                    if (getPlace(i, col) != null && getPlace(i, col).getPion() != null && getPlace(i, col).getPion().getColor() == joueur)
                        return true;
                    i--;
                }
                break;
            case 2: // Bas
                i = ligne;
                while(i < 8) {
                    if(getPlace(i, col).getPion() == null)
                        return false;
                    if (getPlace(i, col) != null && getPlace(i, col).getPion() != null && getPlace(i, col).getPion().getColor() == joueur)
                        return true;
                    i++;
                }
                break;
            case 3: // Droite
                i = col;
                while(i < 8) {
                    if(getPlace(ligne, i).getPion() == null)
                        return false;
                    if (getPlace(ligne, i)!= null && getPlace(ligne, i).getPion() != null && getPlace(ligne, i).getPion().getColor() == joueur)
                        return true;
                    i++;
                }
                break;
            case 4: // Gauche
                i = col;
                while(i >= 0) {
                    if(getPlace(ligne, i).getPion() == null)
                        return false;
                    if (getPlace(ligne, i)!= null && getPlace(ligne, i).getPion() != null && getPlace(ligne, i).getPion().getColor() == joueur)
                        return true;
                    i--;
                }
                break;
            case 5: // Haut Droite
                i = 1;
                while(ligne - i >=0 && col + i < 8) {
                    if(getPlace(ligne-i, col+i).getPion() == null)
                        return false;
                    if (getPlace(ligne-i, col+i)!=null &&getPlace(ligne-i, col+i).getPion() != null && getPlace(ligne-i, col+i).getPion().getColor() == joueur)
                        return true;
                    i++;
                }
                break;
            case 6: // Haut Gauche
                i = 1;
                while(ligne - i >=0 && col - i >= 0) {
                    if(getPlace(ligne-i, col-i).getPion() == null)
                        return false;
                    if (getPlace(ligne-i, col-i)!=null &&getPlace(ligne-i, col-i).getPion() != null && getPlace(ligne-i, col-i).getPion().getColor() == joueur)
                        return true;
                    i++;
                }
                break;
            case 7: // Bas Droite
                i = 1;
                while(ligne + i < 8 && col + i < 8) {
                    if(getPlace(ligne+i, col+i).getPion() == null)
                        return false;
                    if (getPlace(ligne+i, col+i)!= null && getPlace(ligne+i, col+i).getPion() != null && getPlace(ligne+i, col+i).getPion().getColor() == joueur)
                        return true;
                    i++;
                }
                break;
            case 8: // Bas Gauche
                i = 1;
                while(ligne + i < 8 && col - i >= 0) {
                    if(getPlace(ligne+i, col-i).getPion() == null)
                        return false;
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

    public void jouerIA()
    {
        Random rand = new Random();
        int nb = rand.nextInt(caseJouables.size());
        placerCoups(ia, caseJouables.get(nb).getPosTabY(), caseJouables.get(nb).getPosTabX(), aff);
    }

    public DefaultDirectedGraph<Coup, DefaultEdge> creerGraphePositionel(){
        Othello o = new Othello();
        Othello temp = new Othello();
        List<Coup> coup = new ArrayList<>();
        Coup sommet = new Coup();
        DefaultDirectedGraph<Coup, DefaultEdge> graph = new DefaultDirectedGraph<>(DefaultEdge.class);
        graph.addVertex(sommet);
        for(Coup c: coupJoue)
        {
            o.placerCoups(c.getJoueur(), c.getX(), c.getY(), o.getAff());
            o.setJoueur((o.getJoueur() ==  Color.WHITE)? Color.BLACK: Color.WHITE);
            o.choixPossible(o.getJoueur());

            temp.placerCoups(c.getJoueur(), c.getX(), c.getY(), temp.getAff());
            temp.setJoueur((temp.getJoueur() ==  Color.WHITE)? Color.BLACK: Color.WHITE);
            temp.choixPossible(temp.getJoueur());
        }
        
        for(Place p: o.getCaseJouables())
        {
            temp.placerCoups(temp.getJoueur(), p.getPosTabY(), p.getPosTabX(), temp.getAff());
            temp.choixPossible((temp.getJoueur()== Color.WHITE)?Color.BLACK:Color.WHITE);
            Coup co = temp.getCoupJoue().get(temp.getCoupJoue().size()-1);
            coup.add(co);
            graph.addVertex(co);
            graph.addEdge(sommet, co);

            Othello clone2 = new Othello();//On clone temp pour faire les coups parmis un coup
            for(Coup c:temp.getCoupJoue())
            {
                clone2.placerCoups(c.getJoueur(), c.getX(), c.getY(), clone2.getAff());
                clone2.setJoueur((clone2.getJoueur() ==  Color.WHITE)? Color.BLACK: Color.WHITE);
                clone2.choixPossible((c.getJoueur() == Color.WHITE)?Color.BLACK:Color.WHITE);
            }

            for(Place c:temp.getCaseJouables())//Pour chaque case jouable on place le coup dans le clone2
            {
                clone2.placerCoups(clone2.getJoueur(), c.getPosTabY(), c.getPosTabX(), clone2.getAff());
                clone2.choixPossible((clone2.getJoueur() == Color.WHITE)?Color.BLACK:Color.WHITE);
                Coup co2= clone2.getCoupJoue().get(clone2.getCoupJoue().size()-1);
                graph.addVertex(co2);
                graph.addEdge(co, co2);

                clone2 =  new Othello();
                for(Coup c2: temp.getCoupJoue())
                {
                    clone2.placerCoups(c2.getJoueur(), c2.getX(), c2.getY(), clone2.getAff());
                    clone2.choixPossible((c2.getJoueur() == Color.WHITE)?Color.BLACK:Color.WHITE);
                }
            }

            temp = new Othello();
            for(Coup c:coupJoue)
            {
                temp.placerCoups(c.getJoueur(), c.getX(), c.getY(), temp.getAff());
                temp.choixPossible((c.getJoueur() == Color.WHITE)?Color.BLACK:Color.WHITE);
            }

        }

        for(Coup p: coup)
            System.out.println(p.getPoids());
        return graph;
    }

    public void placerCoups(Color joueur, int x, int y, Group root)
    {

        if(caseJouables.contains(getPlace(y, x)))
        {
            getPlace(y, x).setFill(Color.GREEN);
            getPlace(y, x).setPion(new Pion((int)getPlace(y, x).getX()+50, (int)getPlace(y, x).getY()+50, joueur));
            Coup c = new Coup(x, y, joueur);
            c.setPoids(getPlace(y, x).getPoids());
            coupJoue.add(c);


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
                                c.setPoids(c.getPoids()+ temp.getPoids());
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
    public void resetPartie()
    {
        plateau.clear();
        caseJouables.clear();
        coupJoue.clear();
        coups.getChildren().clear();
        joueur = Color.BLACK;
        creerPlateau();
        choixPossible(joueur);
        draw(aff);
    }

    public Group getAff() {
        return aff;
    }

    public List<Place> getCaseJouables() {
        return caseJouables;
    }

    public VBox getCoups() {
        return coups;
    }

    public Color getIa() {
        return ia;
    }

    public void setIa(Color ia) {
        this.ia = ia;
        resetPartie();

    }

    public List<Place> getPlateau() {
        return plateau;
    }

    public void setPlateau(List<Place> plateau) {
        this.plateau = plateau;
    }

    public void setCaseJouables(List<Place> caseJouables) {
        this.caseJouables = caseJouables;
    }

    public Color getJoueur() {
        return joueur;
    }

    public void setJoueur(Color joueur) {
        this.joueur = joueur;
    }

    public List<Coup> getCoupJoue() {
        return coupJoue;
    }

    public void setCoupJoue(List<Coup> coupJoue) {
        this.coupJoue = coupJoue;
    }
}


