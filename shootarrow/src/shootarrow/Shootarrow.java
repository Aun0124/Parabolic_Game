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
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
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
    int anglesubstract=1;

    public static final double ACCELERATION = -9.81;
    private Image stickman, arrowimage, background;
    private Node arrow, Background;
    private ArrayList<Node> arrows = new ArrayList<Node>();
    private Group board;
    Text healthtext;
    private int health = 100;
    boolean shooting;

    @Override
    public void start(Stage stage) throws Exception {
        //  Parent root = FXMLLoader.load(getClass().getResource("FXMLDocument.fxml"));

        arrowimage = new Image("arrow.jpeg");
        

        ImagePattern pattern = new ImagePattern(new Image("background.jpeg"));

        board = new Group();
        healthtext = new Text(50, 50, "Health: " + health);
        healthtext.setFont(Font.font("verdana",20));
        board.getChildren().addAll(healthtext);// add stickman

        scene = new Scene(board, Screen.getMainScreen().getWidth()*0.9, Screen.getMainScreen().getHeight()*0.8);
        scene.setFill(pattern);
       
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
                    totalTime = -2.0 * yVelocity / ACCELERATION;
                    timeIncrement = totalTime / steps;
                    xIncrement = xVelocity * timeIncrement;
                    

                    ImageView anarrow = new ImageView(arrowimage);
                    Node newarrow = anarrow;
                    newarrow.relocate(x + 50, scene.getHeight() / 1.4 - y);
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
                     anglesubstract=(int) ((int)(Math.toDegrees(angle))*(t/totalTime)*2);
                  
                    projectilemotion();
                    System.out.println("\t" + round(x) + "\t" + round(y) + "\t" + round(t));
                    if ((int) y <= 0) {
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
        arrows.get(arrows.size() - 1).relocate(x + 50, scene.getHeight() / 1.4 - y);
        arrows.get(arrows.size() - 1).rotateProperty().setValue(Math.toDegrees(-angle)+anglesubstract);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}