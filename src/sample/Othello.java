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
import org.jgrapht.Graphs;
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
    private LinkedList<Coup> coupJoue;
    private VBox coups;
    public static Font fontCollege = Font.loadFont("file:src/sample/ressource/college.ttf", 20);

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
    public Othello(String s)
    {
        aff = new Group();
        ia = null;
        plateau = new ArrayList<>();
        caseJouables = new ArrayList<>();
        joueur = Color.BLACK;
        coupJoue = new LinkedList<>();
        coups= new VBox();
    }

    public void creerPlateau()
    {
        int x = 350;
        int y = 100;
        int a = 0;
        Color c;
        Scanner lecteur = null;
        try {
            FileInputStream f = new FileInputStream("src/sample/ressource/Poids.txt");
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
                                jouerIA(ia);
                                joueur = (joueur == Color.BLACK) ? Color.WHITE: Color.BLACK;
                                nbChoix = choixPossible(joueur);
                            }
                            if(nbChoix==0)
                            {
                                joueur = (joueur == Color.BLACK) ? Color.WHITE: Color.BLACK;
                                choixPossible(joueur);
                                while(ia == joueur)
                                {
                                    jouerIA(ia);
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
                    Place p =getPlace(i, col);
                    if(p.getPion() == null)
                        return false;
                    if (p != null && p.getPion() != null && p.getPion().getColor() == joueur)
                        return true;
                    i--;
                }
                break;
            case 2: // Bas
                i = ligne;
                while(i < 8) {
                    Place p =getPlace(i, col);
                    if(p.getPion() == null)
                        return false;
                    if (p != null && p.getPion() != null && p.getPion().getColor() == joueur)
                        return true;
                    i++;
                }
                break;
            case 3: // Droite
                i = col;
                while(i < 8) {
                    Place p = getPlace(ligne, i);
                    if(p.getPion() == null)
                        return false;
                    if (p!= null && p.getPion() != null && p.getPion().getColor() == joueur)
                        return true;
                    i++;
                }
                break;
            case 4: // Gauche
                i = col;
                while(i >= 0) {
                    Place p = getPlace(ligne, i);
                    if(p.getPion() == null)
                        return false;
                    if (p!= null && p.getPion() != null && p.getPion().getColor() == joueur)
                        return true;
                    i--;
                }
                break;
            case 5: // Haut Droite
                i = 1;
                while(ligne - i >=0 && col + i < 8) {
                    Place p = getPlace(ligne-i, col+i);
                    if(p.getPion() == null)
                        return false;
                    if (p!=null &&p.getPion() != null && p.getPion().getColor() == joueur)
                        return true;
                    i++;
                }
                break;
            case 6: // Haut Gauche
                i = 1;
                while(ligne - i >=0 && col - i >= 0) {
                    Place p = getPlace(ligne-i, col-i);
                    if(p.getPion() == null)
                        return false;
                    if (p!=null &&p.getPion() != null && p.getPion().getColor() == joueur)
                        return true;
                    i++;
                }
                break;
            case 7: // Bas Droite
                i = 1;
                while(ligne + i < 8 && col + i < 8) {
                    Place p = getPlace(ligne+i, col+i);
                    if(p.getPion() == null)
                        return false;
                    if (p!= null && p.getPion() != null && p.getPion().getColor() == joueur)
                        return true;
                    i++;
                }
                break;
            case 8: // Bas Gauche
                i = 1;
                while(ligne + i < 8 && col - i >= 0) {
                    Place p = getPlace(ligne+i, col-i);
                    if(p.getPion() == null)
                        return false;
                    if (p!=null &&p.getPion() != null && p.getPion().getColor() == joueur)
                        return true;
                    i++;
                }
                break;
            default:
                return false;
        }
        return false;
    }


    public void jouerIA(Color ia)
    {
        Coup coup = new Coup();
        //this.creerGraphe(1, p, this, coup, 2);
        Resultat r = minimax(3,true, this, coup,-5000, 5000, 1, ia);
        placerCoups(ia, r.getCoup().getX(), r.getCoup().getY(), aff);
    }
    public int creerGrapheJGraph(int profondeurActuel, int profondeurMax, Othello toClone, Coup sommet,DefaultDirectedGraph<Coup, DefaultEdge> graph, int choixStrategie)
    {
        if(profondeurActuel < profondeurMax)
        {
            Othello clone = toClone.clone();
            Othello temp = toClone.clone();
            int min_max = (profondeurActuel % 2 == 0)?5000 : -5000;

            List<Integer> score = new ArrayList<>();
            for(Place p: temp.getCaseJouables())
            {
                Coup  coup = clone.placerCoups(clone.getJoueur(), p.getPosTabY(), p.getPosTabX(), clone.getAff());
                clone.choixPossible((clone.getJoueur() == Color.WHITE)? Color.BLACK:Color.WHITE);
                graph.addVertex(coup);
                graph.addEdge(sommet, coup);
                int temp1 = creerGraphe(profondeurActuel+1, profondeurMax, clone, coup, choixStrategie);
                if(profondeurActuel == 1)
                {
                    score.add(temp1);
                }else
                {
                    if(profondeurActuel % 2 == 0)
                    {
                        min_max = Math.min(temp1, min_max);
                    }else
                    {
                        min_max = Math.max(temp1, min_max);
                    }
                }
                clone = toClone.clone();
            }
            if(profondeurActuel == 1)
            {
                int max=-5000;
                for(Integer e: score)
                {
                    max = Math.max(max, e);
                }
                int index  =score.indexOf(max);
                sommet.setX(Graphs.successorListOf(graph,sommet).get(index).getX());
                sommet.setY(Graphs.successorListOf(graph,sommet).get(index).getY());
                sommet.setJoueur(Graphs.successorListOf(graph,sommet).get(index).getJoueur());
                sommet.setPoids(Graphs.successorListOf(graph,sommet).get(index).getPoids());
            }
            return min_max;
        }
        else
            return choixStrategie(choixStrategie, sommet);
    }
    public int creerGraphe(int profondeurActuel, int profondeurMax, Othello toClone, Coup sommet, int choixStrategie)
    {

        if(profondeurActuel <= profondeurMax)
        {
            Othello clone = toClone.clone();
            List<Place> caseJouable = toClone.getCaseJouables();
            int min_max = (profondeurActuel % 2 == 0)?5000 : -5000;
            Map<Integer, Coup> score = new HashMap<>();
            for(Place p: caseJouable)
            {
                Coup  coup = clone.placerCoups(clone.getJoueur(), p.getPosTabY(), p.getPosTabX(), clone.getAff());
                clone.choixPossible((clone.getJoueur() == Color.WHITE)? Color.BLACK:Color.WHITE);
                int temp1 = creerGraphe(profondeurActuel+1, profondeurMax, clone, coup, choixStrategie);
                if(profondeurActuel == 1)
                {
                   score.put(temp1, coup);
                }else
                {
                    if(profondeurActuel % 2 == 0)
                    {
                        min_max = Math.min(temp1, min_max);
                    }else
                    {
                        min_max = Math.max(temp1, min_max);
                    }
                }

            }
            if(profondeurActuel == 1)
            {
                int max=-5000;
                for(Map.Entry<Integer, Coup> map:score.entrySet())
                {
                    if(map.getKey() >= max)
                    {
                        max = map.getKey();
                    }
                }

                sommet.setX(score.get(max).getX());
                sommet.setY(score.get(max).getY());
                sommet.setJoueur(score.get(max).getJoueur());
                sommet.setPoids(score.get(max).getPoids());
            }
            return min_max;
        }
        else
            return choixStrategie(choixStrategie, sommet);
    }

    public boolean game_over(Othello game)
    {
        if(game.getCaseJouables().size() != 0)
            return false;
        else{
            Othello clone = game.clone();
            clone.setJoueur((clone.getJoueur() == Color.WHITE)? Color.BLACK:Color.WHITE);
            return clone.choixPossible(clone.getJoueur()) == 0;
        }
    }

    public Resultat minimax(int profondeur, boolean maximizePlayer, Othello toClone, Coup sommet, int alpha, int beta, int choixStrategie, Color ia)
    {
        if(profondeur == 0|| game_over(toClone)) {
            if(game_over(toClone)) {
               if(toClone.quiGagne() == ia) {
                   return new Resultat(5000, sommet);//Permet d'indiquer si le coups donne la victoire ou non
               }
            }
            else {
                if(choixStrategie == 3) return new Resultat(toClone.getCaseJouables().size(), sommet);
                else return new Resultat(choixStrategie(choixStrategie, sommet), sommet);
            }
        }
        if(maximizePlayer) {
            List<Place> caseJouable = toClone.getCaseJouables();
            Resultat resultat = new Resultat(-5000, null);
            for(Place p: caseJouable) {
                Othello clone = toClone.clone();
                Coup  coup = clone.placerCoups(clone.getJoueur(), p.getPosTabY(), p.getPosTabX(), clone.getAff());
                clone.choixPossible((clone.getJoueur() == Color.WHITE)? Color.BLACK:Color.WHITE);
                Resultat eval = minimax( profondeur-1 , false, clone, coup,alpha, beta, choixStrategie, ia);

                eval.setCoup(coup);
                if(eval.getValeur() >= resultat.getValeur()) {
                    resultat = eval;
                }
                /*
                alpha =  Math.max(alpha, eval.getValeur());
                if(beta <= alpha)
                    break;

                 */
            }
            return resultat;
        }else {
            List<Place> caseJouable = toClone.getCaseJouables();
            Resultat resultat = new Resultat(5000, null);
            for(Place p: caseJouable) {
                Othello clone = toClone.clone();
                Coup  coup = clone.placerCoups(clone.getJoueur(), p.getPosTabY(), p.getPosTabX(), clone.getAff());
                clone.choixPossible((clone.getJoueur() == Color.WHITE)? Color.BLACK:Color.WHITE);
                Resultat eval = minimax(profondeur-1, true, clone, coup, alpha, beta, choixStrategie, ia);
                eval.setCoup(coup);
                if(eval.getValeur() <= resultat.getValeur()) {
                    resultat = eval;
                }
                /*
                beta =  Math.min(beta, eval.getValeur());
                if(beta <= alpha)
                    break;

                 */
            }
            return resultat;
        }
    }

    public int choixStrategie(int choix, Coup coup)// 1 pour absolu, 2 pour positionnel, 3 pour mobilité
    {
        return switch (choix) {
            case 1 -> coup.getNbPieces();
            case 2 -> coup.getPoids();
            default -> -5000;
        };
    }

    public boolean estUnChoixPossible(Place place)
    {
        for(Place p :caseJouables)
        {
            if(p.getPosTabY() == place.getPosTabY() && p.getPosTabX() == place.getPosTabX())
                return true;
        }
        return false;
    }


    public Coup placerCoups(Color joueur, int x, int y, Group root)
    {
        Coup c = new Coup(x, y, joueur);
        Place p = getPlace(y, x);
        if(estUnChoixPossible(p))
        {

            p.setFill(Color.GREEN);
            p.setPion(new Pion((int)p.getX()+50, (int)p.getY()+50, joueur));

            c.setPoids(p.getPoids());
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
        for(Place p2: caseJouables){
            p2.setFill(p2.getColor());

        }
        int nb = 0;
        caseJouables.clear();
        for(Place o :plateau)
        {
            if(o.getPion() != null && o.getPion().getColor() == joueur)
                nb++;
        }
        c.setNbPieces(nb);
        return c;
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

    public Color quiGagne()
    {
        int nbWhite = 0;
        int nbBlack = 0;
        for(Place p: plateau)
        {
            if(p.getPion()!= null)
            {
                if(p.getPion().getColor() ==  Color.WHITE)
                    nbWhite++;
                else
                    nbBlack++;
            }
        }
        if(nbWhite > nbBlack)
            return Color.WHITE;
        else if (nbBlack > nbWhite)
            return Color.BLACK;
        else
            return null;
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

    public LinkedList<Coup> getCoupJoue() {
        return coupJoue;
    }

    public void setCoupJoue(LinkedList<Coup> coupJoue) {
        this.coupJoue = coupJoue;
    }

    @Override
    public Othello clone()
    {
        Othello clone = new Othello("test");
        clone.getCaseJouables().clear();
        for(Place p: this.plateau)
        {
            clone.getPlateau().add(p.clone());
        }
        for(Place p: this.caseJouables)
        {
            clone.getCaseJouables().add(new Place(p.getPosTabX(), p.getPosTabY(),p.getColor()));
        }
        clone.setJoueur(this.getJoueur());
        return clone;
    }
}


