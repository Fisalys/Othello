package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.awt.*;


public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        primaryStage.setTitle("Othello");
        
        //Creation de la barre de menu;
        MenuBar bar = new MenuBar();

        //Creation des menus
        Menu partie = new Menu("Partie");
        Menu affichage = new Menu("Affichage");

        //Les Items du menu Partie
        MenuItem newPartie = new MenuItem("Nouvelle Partie");
        MenuItem save = new MenuItem("Sauvegarder");
        MenuItem rejouer = new MenuItem("Revoir la partie");

        //Les items du menus Affichage
        MenuItem fullscreen = new CheckMenuItem("Plein écran");

        partie.getItems().addAll(newPartie, save, rejouer);
        affichage.getItems().addAll(fullscreen);

        bar.getMenus().addAll(partie, affichage);


        HBox hbox = new HBox();
        Scene s2 = new Scene(hbox, 100, 100);
        hbox.setSpacing(200);

        VBox vbox2 =  new VBox();
        VBox vbox3 =  new VBox();
        GraphicsDevice Gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        int width = Gd.getDisplayMode().getWidth();

        HBox hBox2 = new HBox();
        VBox vBox1_2_1 = new VBox();
        VBox vBox1_2_2 = new VBox();
        hBox2.getChildren().addAll(vBox1_2_1, vBox1_2_2);
        Button newP = new Button("Nouvelle partie");
        vBox1_2_2.getChildren().addAll(newP);
        Rectangle r = new Rectangle();
        vBox1_2_1.getChildren().addAll(r);
        r.setHeight(width);
        r.setWidth(100);
        r.setFill(Color.RED);

        newP.setStyle("-fx-border: none;\n" +
                "    -fx-background-color: rgb(231, 231, 5);\n" +
                "    -fx-border-radius: 0;\n");



        Othello o = new Othello(s2);

        vbox2.getChildren().add(o.getAff());
        vbox2.setAlignment(Pos.CENTER);

        hbox.getChildren().addAll(hBox2, vbox2, vbox3);
        newPartie.setOnAction(e -> {
            o.resetPartie(s2);
        });
        fullscreen.setOnAction(e->{
            primaryStage.setFullScreen((((CheckMenuItem)e.getSource()).isSelected()));
        });
        newP.setOnAction(e->{
            primaryStage.close();
        });
        //primaryStage.initStyle(StageStyle.UNDECORATED);
        primaryStage.setMaximized(true);
        primaryStage.setScene(s2);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
