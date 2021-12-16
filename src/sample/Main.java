package sample;

import com.mxgraph.layout.hierarchical.mxHierarchicalLayout;
import com.mxgraph.layout.mxCircleLayout;
import com.mxgraph.layout.mxCompactTreeLayout;
import com.mxgraph.layout.mxIGraphLayout;
import com.mxgraph.model.mxCell;
import com.mxgraph.model.mxICell;
import com.mxgraph.util.mxCellRenderer;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.NodeOrientation;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.scene.control.CheckBox;
import org.jgrapht.Graphs;
import org.jgrapht.ext.JGraphXAdapter;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;


import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.*;
import java.util.List;


public class Main extends Application {
/*
    @Override
    public void start(Stage primaryStage) throws Exception{


            primaryStage.setTitle("Othello");
            BorderPane root = new BorderPane();
            root.setStyle("-fx-background-color: #d6a2a2");
            GraphicsDevice Gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
            int height = Gd.getDisplayMode().getHeight();
            Scene s2 = new Scene(root, 1600, 1000);


            //Border pane Gauche
            //Possède un Hbox et un Group
            HBox bordGroup = new HBox();
            Group barreGauche = new Group();


            //Le groupe possède un rectangle pour la barre
            Rectangle fill = new Rectangle();
            fill.setWidth(75);
            fill.setHeight(height-35);
            fill.setFill(Color.web("#8a3d3d"));


            //On ajoute le rectangle dans le groupe
            barreGauche.getChildren().add(fill);
            //On ajoute le group dans la box.
            bordGroup.getChildren().addAll(barreGauche);

            //Ajout des images pour les menu
            FileInputStream temp = new FileInputStream("src/sample/ressource/home.png");
            Image temp2 = new Image(temp);
            ImageView home = new ImageView(temp2);
            FileInputStream temp3 = new FileInputStream("src/sample/ressource/homeClicked.png");
            Image temp4 = new Image(temp3);
            ImageView homeClicked = new ImageView(temp4);

            Label homeView = new Label("");
            homeView.setGraphic(home);
            homeView.setTranslateX(7);
            homeView.setTranslateY(50);
            barreGauche.getChildren().add(homeView);

            Group menu = new Group();

            Rectangle background = new Rectangle(300, 1022, Color.web("ad4d4d"));

            menu.getChildren().add(background);
            menu.setVisible(false);

            
            homeView.setOnMouseClicked(e->{
                    menu.setVisible(!menu.isVisible());
                    homeView.setGraphic((homeView.getGraphic() == home)?homeClicked:home);

            });


            VBox test= new VBox();
            test.setTranslateY(60);

            TitledPane one = new TitledPane();
            one.setExpanded(false);
            one.setText("JOUER");

            one.setMinWidth(300);
            one.setFont(Othello.fontCollege);
            one.setStyle("-fx-background-color: #ad4d4d");
            VBox contentPartie = new VBox();
            contentPartie.setMinWidth(300);

            Button newPartie= new Button("Nouvelle Partie");
            newPartie.setFont(Othello.fontCollege);
            //newPartie.setMinWidth(300);
            CheckBox ia= new CheckBox("Jouer contre l'IA");
            ia.setFont(Othello.fontCollege);
            //ia.setMinWidth(300);
            contentPartie.getChildren().addAll(newPartie, ia)   ;
            one.setContent(contentPartie);

            TitledPane two = new TitledPane();
            two.setText("SAUVEGARDER");
            two.setFont(Othello.fontCollege);
            two.setExpanded(false);
            Button b2= new Button("Sauvegarder la partie");
            b2.setFont(Othello.fontCollege);
            b2.setMinWidth(300);

            two.setContent(b2);
            test.getChildren().addAll(one, two);
            menu.getChildren().add(test);
            bordGroup.getChildren().add(menu);

            //On instancie le jeu et on le place au centre
            Othello o = new Othello();
            newPartie.setOnAction(e->{
                o.resetPartie();
            });
            ia.setOnAction(e->{
                    if(ia.isSelected())
                    {
                            o.setIa(Color.WHITE);
                    }else
                    {
                            o.setIa(null);
                    }
            });

            Button graphe = new Button("Graphe");
            graphe.setFont(Othello.fontCollege);
            graphe.setTranslateY(400);
            barreGauche.getChildren().add(graphe);
            graphe.setOnAction(e ->
            {

                    Coup coup = new Coup();
                    int p = 6;
                    o.creerGraphe(1, p, o, coup, 2);
                    System.out.println(coup.getLabel());

            });

            root.setCenter(o.getAff());
            //On place le hbox du menu a gauche
            root.setLeft(bordGroup);

            //On crée la barre de titre
            ToolBar bordHaut = new ToolBar();
            bordHaut.setMinHeight(50);
            bordHaut.setStyle("-fx-background-color:#622b2b");
            //On crée le bouton pour sortir de l'application
            Button exit = new Button("X");
            exit.setOnAction(e->{
                primaryStage.close();
            });
            //On ajoute notre bouton à la barre de titre
            bordHaut.getItems().add(exit);
            //Permet que le bouton se retrouve à droite.
            bordHaut.setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
            root.setTop(bordHaut);

            // On crée le bord droite pour la liste des coups
            VBox bordDroite = new VBox();

            HBox bordDroite2 = new HBox();
            bordDroite.getChildren().add(bordDroite2);
            VBox coups = new VBox();
            Group t1 = new Group();
            t1.setTranslateY(120);
            t1.setTranslateX(-100);
            ScrollPane scrollPane = new ScrollPane();

            scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
            scrollPane.setMaxHeight(692);
            scrollPane.setMinWidth(192);
            scrollPane.setContent(o.getCoups());
            scrollPane.setTranslateX(4);
            scrollPane.setTranslateY(4);
            scrollPane.setStyle("-fx-background: d6a2a2;" +
                    "-fx-background-color: d6a2a2;");

            coups.setAlignment(Pos.CENTER);

            Rectangle r2 = new Rectangle(200, 700, Color.web("#d6a2a2"));
            r2.setStyle("-fx-fill: #d6a2a2; -fx-stroke: black; -fx-stroke-width: 4;");

            t1.getChildren().add(r2);
            t1.getChildren().add(scrollPane);

            bordDroite2.getChildren().add(t1);
            bordDroite2.getChildren().add(new Rectangle(200, 100, Color.web("#d6a2a2")));

            root.setRight(bordDroite);


            //On ajoute l'icone
            FileInputStream input = new FileInputStream("src/sample/ressource/icon.png");
            Image image = new Image(input);
            ImageView icon = new ImageView(image);
            Label iconView = new Label("");
            iconView.setGraphic(icon);
            iconView.setPadding(new Insets(0, 0, 0, 1825));
            bordHaut.getItems().add(iconView);
            primaryStage.initStyle(StageStyle.UNDECORATED);
            primaryStage.setMaximized(true);


            primaryStage.setScene(s2);
            System.out.println(primaryStage.getScene());
            primaryStage.show();

    }

 */






    public int jouer2IA(int choixStrategie)// IA Aleatoire joue les blancs et l'autre joue les noirs en fonction d'un strategie chosie -> 1 pour absolu, 2 pour positionnel, 3 pour mobilité
    {
            Othello o = new Othello();
            int finie = 0;
            int tour = 0;
            while(finie != 2)
            {
                 finie = 0;
                 o.jouerIA(o.getJoueur());
                 o.setJoueur((o.getJoueur() == Color.WHITE)? Color.BLACK: Color.WHITE);
                 int nb = o.choixPossible(o.getJoueur());
                 if(nb <= 0)
                 {
                         finie=1;
                         o.setJoueur((o.getJoueur() == Color.WHITE)? Color.BLACK: Color.WHITE);
                         nb = o.choixPossible(o.getJoueur());
                         if(nb <= 0)
                                 finie=2;
                 }else
                 {
                         while(o.getJoueur() == Color.WHITE)
                         {
                                 Random r = new Random();
                                 int choix = r.nextInt(nb);
                                 o.placerCoups(o.getJoueur(), o.getCaseJouables().get(choix).getPosTabY(), o.getCaseJouables().get(choix).getPosTabX(), o.getAff());
                                 o.setJoueur((o.getJoueur() == Color.WHITE)? Color.BLACK: Color.WHITE);
                                 nb = o.choixPossible(o.getJoueur());
                                 if(nb <= 0)
                                 {
                                         finie=1;
                                         o.setJoueur((o.getJoueur() == Color.WHITE)? Color.BLACK: Color.WHITE);
                                         nb = o.choixPossible(o.getJoueur());
                                         if(nb <= 0) {
                                             finie = 2;
                                             o.setJoueur((o.getJoueur() == Color.WHITE)? Color.BLACK: Color.WHITE);
                                         }
                                 }
                         }
                 }
                 tour++;

            }
            Color gagnant = o.quiGagne();
            if(gagnant == Color.WHITE)
                    return 1;
            else if (gagnant == Color.BLACK)
                    return -1;
            else
                    return 0;
    }

    public void jouerPlusieursPartie(int choixStrategie)//1 pour absolu, 2 pour positionnel, 3 pour mobilité
    {
        long debut = System.currentTimeMillis();
        if(choixStrategie == 1)
            System.out.println("Strategy = Absolu");
        if(choixStrategie == 2)
            System.out.println("Strategy = Positionnel");
        if(choixStrategie == 3)
            System.out.println("Strategy = Mobilité");
        int victoireBlanche = 0;
        int victoireNoire = 0;
        int egalite = 0;
        int temp;
        for(int i =1; i <= 1000; i++)
        {
            temp = jouer2IA(choixStrategie);
            if(temp == 1)
                victoireBlanche++;
            else if(temp == -1)
                victoireNoire++;
            else
                egalite++;
            System.out.println("Partie n°"+ i + " terminée");

            if(i % 100 == 0)
            {
                System.out.println("Victoire Noire : " + victoireNoire + "\n" + "Victoire Blanche : " + victoireBlanche + "\n" + "Egalité : " + egalite);
                long fin = System.currentTimeMillis();
                double second = (fin-debut) /1000F;
                System.out.println("Temps d'éxécution : "+second);
            }
        }

    }


    public static void main(String[] args) {
        launch(args);
    }


    @Override
    public void start(Stage stage) throws Exception {
            jouerPlusieursPartie(2);
    }












}
