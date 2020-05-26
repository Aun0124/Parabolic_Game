/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shootarrow;

import com.sun.glass.ui.Screen;
import static java.lang.Math.round;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.AnimationTimer;
import javafx.animation.PathTransition;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Orientation;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import javax.swing.UIManager;
import static javax.swing.text.StyleConstants.Background;

/**
 *
 * @author Administrator
 */
public class Shootarrow extends Application {

    public static Scene scene;
    double x = 0.0;
    double y = 0.0;
    double t = 0.0;
    int k = 0;
    double xVelocity;
    double yVelocity;
    double totalTime;
    double timeIncrement;
    double xIncrement;
    double angle;
    int anglesubstract = 1;

    public static final double ACCELERATION = -9.81;
    private Image arrowimage, background;
    private Node body,hands,bow,arrow;
    private ArrayList<Node> arrows = new ArrayList<Node>();
    private Group board;
    Text healthtext;
    private int health = 100;
    boolean shooting;

    @Override
    public void start(Stage stage) throws Exception {
        //  Parent root = FXMLLoader.load(getClass().getResource("FXMLDocument.fxml"));
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        arrowimage = new Image("arrow.jpeg");
        arrow=new ImageView(arrowimage);
        body= new ImageView(new Image("body.jpeg"));
        hands=new ImageView(new Image("hands.jpeg"));
        bow=new ImageView(new Image("bow.jpeg"));
        ImagePattern pattern = new ImagePattern(new Image("background.jpeg"));
        ScrollBar sc=new ScrollBar();
        
        board = new Group();
        healthtext = new Text(50, 50, "Health: " + health);
        healthtext.setFont(Font.font("verdana", 20));
        board.getChildren().addAll(sc,healthtext,body,hands,bow,arrow);// add stickman
         // ScrollPane pane=new ScrollPane(board);

        scene = new Scene(board, Screen.getMainScreen().getWidth() * 0.9, Screen.getMainScreen().getHeight() * 0.8);
        scene.setFill(pattern);
        arrow.relocate(x+140, scene.getHeight() / 1.55 - y);arrow.rotateProperty().set(-20);
        body.relocate(x+50, scene.getHeight() / 2.2 - y);
        hands.relocate( x + 78, scene.getHeight() / 1.6 - y);
        bow.relocate( x + 116, scene.getHeight() / 1.78 - y);
        sc.setLayoutY(scene.getHeight()-5);
        sc.setPrefWidth(scene.getWidth()+10);
        sc.setFocusTraversable(true);
        sc.setMin(0);
        sc.setMax(100);
        sc.setValue(20);
        sc.setOrientation(Orientation.HORIZONTAL);
        sc.setPrefHeight(15);
        
        scene.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (shooting && y > 0)
                    ; else {
                    x = 0.0;
                    y = 0.0;
                    t = 0.0;
                    k++;
                    double velocity = 59 + k * 10;
                    angle = Math.toRadians(55);
                    int steps = 35;
                    xVelocity = velocity * Math.cos(angle);
                    yVelocity = velocity * Math.sin(angle);
                    double finalv=Math.sqrt((Math.pow(yVelocity, 2)+2.0*ACCELERATION*(-50)));
                    totalTime = (-1.0 * finalv -yVelocity) / ACCELERATION;
                    timeIncrement = totalTime / steps;
                    xIncrement = xVelocity * timeIncrement;

                    ImageView anarrow = new ImageView(arrowimage);
                    Node newarrow = anarrow;
                    newarrow.relocate(x+140, scene.getHeight() / 1.55 - y);
                    arrows.add(newarrow);// maybe no need this line
                    board.getChildren().add(newarrow);
                    shooting = true;
                }
            }
        });
        stage.setScene(scene);
        stage.show();
        stage.setResizable(false);
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (shooting) {
                    t += timeIncrement;
                    x += xIncrement;
                    y = yVelocity * t + 0.5 * ACCELERATION * t * t;
                    anglesubstract = (int) ((int) (Math.toDegrees(angle)) * (t / totalTime) * 2);

                    projectilemotion();
                    System.out.println("\t" + round(x) + "\t" + round(y) + "\t" + round(t));
                    if (round (y) <= -50) {
                        shooting = false;
                    }
                }

                // checkhit();
                if (health <= 0) {
                    Text GameOver = new Text(450, 300, "Game Over!");
                    GameOver.setFill(Color.RED);
                    GameOver.setFont(Font.font("Verdana", 20));
                    board.getChildren().add(GameOver);
                    this.stop();
                }
            }
        };

        timer.start();
    }

    private void projectilemotion() {
        arrows.get(arrows.size() - 1).relocate(x+140, scene.getHeight() / 1.55 - y);
        arrows.get(arrows.size() - 1).rotateProperty().setValue(Math.toDegrees(-angle) + anglesubstract);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
