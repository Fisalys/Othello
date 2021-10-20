package sample;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.NodeOrientation;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.awt.*;
import java.io.FileInputStream;


public class Main extends Application {

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
            FileInputStream temp = new FileInputStream("ressouces/home.png");
            Image temp2 = new Image(temp);
            ImageView home = new ImageView(temp2);
            Label homeView = new Label("");
            homeView.setGraphic(home);
            homeView.setTranslateX(7);
            homeView.setTranslateY(100);
            barreGauche.getChildren().add(homeView);


            Accordion menu = new Accordion();
            menu.setVisible(false);
            menu.setTranslateY(100);
            menu.setMinWidth(300);


            homeView.setOnMouseEntered(e->{

                    menu.setVisible(!(menu.isVisible()));
            });
            TitledPane one = new TitledPane();

            one.setText("Test");

            menu.getPanes().add(one);

            bordGroup.getChildren().add(menu);
            //On instancie le jeu et on le place au centre
            Othello o = new Othello(s2);
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

            //On ajoute l'icone
            FileInputStream input = new FileInputStream("ressouces/icon.png");
            Image image = new Image(input);
            ImageView icon = new ImageView(image);
            Label iconView = new Label("");
            iconView.setGraphic(icon);
            iconView.setPadding(new Insets(0, 0, 0, primaryStage.getWidth()*0.94));
            bordHaut.getItems().add(iconView);
            primaryStage.initStyle(StageStyle.UNDECORATED);
            primaryStage.setMaximized(true);

            primaryStage.setScene(s2);
            System.out.println(primaryStage.getScene());
            primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
