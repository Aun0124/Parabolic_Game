/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shootarrow;

import com.sun.glass.ui.Screen;
import java.io.File;
import static java.lang.Math.round;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Random;
import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Orientation;
import javafx.scene.Cursor;
import javafx.scene.media.*;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import javax.swing.Timer;

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
    double anglesubstract = 1;
    double inix;
    double iniy;
    double pointdistance;
    boolean rotate;

    public static final double ACCELERATION = -9.8;
    private Image arrowimage;
    private ImageView background;
    private Node body, body2;
    private Node[] arrow = new Node[2];
    private Node[] hands = new Node[2];
    private Node[] bow = new Node[2];
    private ArrayList<Node> arrows = new ArrayList<Node>();
    private Group board;
    ScrollPane pane;
    ProgressBar[] power = new ProgressBar[2];
    ProgressBar[] healthbar = new ProgressBar[2];
    Text[] angletext = new Text[2];
    Text[] powertext = new Text[2];
    Text[] healthtext = new Text[2];
    Text[] damagetext = new Text[2];
    Text[] windtext = new Text[2];
    int winddirection;
    int windforce;
    private int[] health = {100, 100};
    boolean shooting, shooting2;
    int rand = new Random().nextInt(1500);
    AudioClip ac = new AudioClip(Paths.get("src/injured.mp3").toUri().toString());
    

    

    @Override
    public void start(Stage stage) throws Exception {
        //  Parent root = FXMLLoader.load(getClass().getResource("FXMLDocument.fxml"));
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        stage.setTitle("Shooting Arrow");
        arrowimage = new Image("arrow.png", 85, 25, false, false);
        arrow[0] = new ImageView(arrowimage);
        body = new ImageView(new Image("body.png"));
        hands[0] = new ImageView(new Image("hands.png"));
        bow[0] = new ImageView(new Image("bow.png"));

        arrow[1] = new ImageView(arrowimage);
        body2 = new ImageView(new Image("body.png"));
        hands[1] = new ImageView(new Image("hands.png"));
        bow[1] = new ImageView(new Image("bow.png"));

        // ImagePattern pattern = new ImagePattern(new Image("background.jpg"));
        background = new ImageView(new Image("background.jpg"));
        background.setLayoutX(-3500);
        background.setLayoutY(460);
        ScrollBar sc = new ScrollBar();
        for (int i = 0; i < 2; i++) {
            power[i] = new ProgressBar(1.0);
            power[i].setPrefWidth(150);
            power[i].setPrefHeight(20);
            power[i].setProgress(0);
            power[i].setStyle("-fx-accent: orange;");
            healthbar[i] = new ProgressBar(1.0);
            healthbar[i].setPrefWidth(200);
            healthbar[i].setPrefHeight(20);
            healthbar[i].setProgress(1.0);
            healthbar[i].setStyle("-fx-accent: red;");
            if (i == 1) {
                power[i].relocate(1030 + rand, 83);
                healthbar[i].relocate(1030 + rand, 33);
                powertext[i] = new Text(950 + rand, 100, "Power: ");
                angletext[i] = new Text(1190 + rand, 100, "Angle: " + angle);
                healthtext[i] = new Text(950 + rand, 50, "Health: ");
                damagetext[i] = new Text(900 + rand, 0, "-damage ");
                windtext[i] = new Text(1100 + rand, 150, "");
            } else {
                power[i].relocate(130, 83);
                healthbar[i].relocate(130, 33);
                powertext[i] = new Text(50, 100, "Power: ");
                angletext[i] = new Text(290, 100, "Angle: " + angle);
                healthtext[i] = new Text(50, 50, "Health: ");
                damagetext[i] = new Text(0, 0, "-damage ");
                windtext[i] = new Text(200, 150, "");
            }
            powertext[i].setFont(Font.font("verdana", 20));
            angletext[i].setFont(Font.font("verdana", 20));
            healthtext[i].setFont(Font.font("verdana", 20));
            damagetext[i].setFont(Font.font("verdana", 20));
            windtext[i].setFont(Font.font("verdana", 20));
            damagetext[i].setFill(Color.RED);
            damagetext[i].setVisible(false);
            windtext[i].setVisible(false);
        }
        board = new Group();
//        Text line = new Text(-2500, 465, "--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------"
//                + "------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------"
//                + "------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------"
//                + "------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------"
//                + "------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------"
//                + "------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------"
//                + "------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");

        board.getChildren().addAll(background, healthtext[0], healthtext[1], powertext[0], powertext[1], angletext[0], angletext[1], damagetext[0], damagetext[1],
                healthbar[0], healthbar[1], body, hands[0], bow[0], arrow[0], body2, hands[1], bow[1], arrow[1], power[0], power[1],
                windtext[0], windtext[1]);// add stickman
        pane = new ScrollPane(board);
        pane.setContent(board);

        scene = new Scene(pane, Screen.getMainScreen().getWidth() * 3, Screen.getMainScreen().getHeight() * 0.8);
        arrow[0].relocate(x + 140, scene.getHeight() / 1.57 - y);
        arrow[0].rotateProperty().set(-20);
        body.relocate(x + 50, scene.getHeight() / 2.2 - y);
        damagetext[1].relocate(body.getLayoutX() + 30, body.getLayoutY() - 50);
        hands[0].relocate(x + 78, scene.getHeight() / 1.6 - y);
        bow[0].relocate(x + 116, scene.getHeight() / 1.78 - y);

        arrow[1].relocate(x + 1000 + rand, scene.getHeight() / 1.57 - y);
        arrow[1].scaleXProperty().set(-1);
        arrow[1].setDisable(true);
        arrow[1].rotateProperty().set(20);
        body2.relocate(x + 1015 + rand, scene.getHeight() / 2.2 - y);
        body2.scaleXProperty().set(-1);
        damagetext[0].relocate(body2.getLayoutX(), body2.getLayoutY() - 50);
        hands[1].relocate(x + 1005 + rand, scene.getHeight() / 1.6 - y);
        hands[1].scaleXProperty().set(-1);
        bow[1].relocate(x + 993 + rand, scene.getHeight() / 1.78 - y);
        bow[1].scaleXProperty().set(-1);
//        sc.setLayoutY(scene.getHeight() - 5);
//        sc.setPrefWidth(scene.getWidth() + 10);
//        sc.setFocusTraversable(true);
//        sc.setMin(0);
//        sc.setMax(100);
//        sc.setValue(20);
//        sc.setOrientation(Orientation.HORIZONTAL);
//        sc.setPrefHeight(15);
        gotoplayer1();

        sc.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {

            }
        });
        for (int i = 0; i < 2; i++) {
            arrow[i].setOnMouseEntered(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    scene.setCursor(Cursor.HAND);
                }
            });
        }
        for (int i = 0; i < 2; i++) {
            arrow[i].setOnMouseExited(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    scene.setCursor(Cursor.DEFAULT);
                }
            });
        }
        for (int i = 0; i < 2; i++) {
            Node temp = arrow[i];
            Node temphands = hands[i];
            Node tempbow = bow[i];
            int number = i;
            temp.setOnMousePressed(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    damagetext[0].setVisible(false);
                    damagetext[1].setVisible(false);
                    inix = event.getSceneX();
                    iniy = scene.getHeight() - event.getSceneY();
                    temp.setOnMouseDragged(new EventHandler<MouseEvent>() {
                        @Override
                        public void handle(MouseEvent event) {
                            double angle;
                            if (number == 0) {
                                angle = (double) Math.toDegrees(Math.atan2(iniy - scene.getHeight() + event.getSceneY(), inix - event.getSceneX()));
                            } else {
                                angle = -1.0 * (double) Math.toDegrees(Math.atan2(-iniy + scene.getHeight() - event.getSceneY(), -inix + event.getSceneX()));
                            }
                            angletext[number].setText("Angle: " + round(angle));
                            if (number == 0) {
                                temphands.rotateProperty().set(-angle + 20);
                                tempbow.rotateProperty().set(-angle + 20);
                                temp.rotateProperty().set(-angle);
                            } else {
                                temphands.rotateProperty().set(angle - 20);
                                tempbow.rotateProperty().set(angle - 20);
                                temp.rotateProperty().set(angle);
                            }
                            pointdistance = Math.sqrt((iniy - scene.getHeight() + event.getSceneY()) * (iniy - scene.getHeight() + event.getSceneY())
                                    + (inix - event.getSceneX()) * (inix - event.getSceneX()));
                            if (pointdistance > 100) {
                                pointdistance = 100;
                            }
                            power[number].setProgress(pointdistance / 100.0);
                        }
                    });

                }
            });
        }
        for (int i = 0; i < 2; i++) {
            Node temp = arrow[i];
            Node temphands = hands[i];
            Node tempbow = bow[i];
            int number = i;
            temp.setOnMouseReleased(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    double finx = event.getSceneX();
                    double finy = scene.getHeight() - event.getSceneY();
                    power[number].setProgress(0);
                    angletext[number].setText("Angle: " + 0);
                    double angle1;
                    if (number == 0) {
                        angle1 = (double) Math.toDegrees(Math.atan2(iniy - finy, inix - finx));
                    } else {
                        angle1 = -1.0 * (double) Math.toDegrees(Math.atan2(-iniy + finy, -inix + finx));
                    }
                    temphands.rotateProperty().set(0);
                    tempbow.rotateProperty().set(0);
                    if (number == 0) {
                        temp.rotateProperty().set(- 20);
                    } else {
                        temp.rotateProperty().set(20);
                    }
                    if ((shooting || shooting2) && y > -50)
                    ; else {
                        x = 0.0;
                        y = 0.0;
                        t = 0.0;
                        double velocity;
                        if (winddirection == 1) {
                            if (angle1 >= -90 && angle1 <= 90) {
                                velocity = 35 + 110 * pointdistance / 100.0 + rand / 70 + windforce * 3;
                            } else {
                                velocity = 35 + 110 * pointdistance / 100.0 + rand / 70 - windforce * 3;
                            }
                        } else {
                            if (angle1 >= -90 && angle1 <= 90) {
                                velocity = 35 + 110 * pointdistance / 100.0 + rand / 70 - windforce * 3;
                            } else {
                                velocity = 35 + 110 * pointdistance / 100.0 + rand / 70 + windforce * 3;
                            }
                        }
                        pointdistance = 0;
                        angle = Math.toRadians(angle1);
                        xVelocity = velocity * Math.cos(angle);
                        yVelocity = velocity * Math.sin(angle);
                        double finalv = Math.sqrt((Math.pow(yVelocity, 2) + 2.0 * ACCELERATION * (-50)));
                        totalTime = (-1.0 * finalv - yVelocity) / ACCELERATION;
                        int steps = (int) (round(totalTime) * 3) + 3;
                        timeIncrement = totalTime / steps;
                        xIncrement = xVelocity * timeIncrement;

                        ImageView anarrow = new ImageView(arrowimage);
                        Node newarrow = anarrow;
                        newarrow.relocate(x + 140, scene.getHeight() / 1.55 - y);
                        arrows.add(newarrow);
                        board.getChildren().add(newarrow);
                        if (number == 0) {
                            shooting = true;
                        } else {
                            shooting2 = true;
                        }
                    }
                }
            });
        }
        stage.setScene(scene);
        stage.show();
        stage.setMaxWidth(Screen.getMainScreen().getWidth());
        stage.centerOnScreen();
        stage.setResizable(false);
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (shooting || shooting2) {
                    t += timeIncrement;
                    if (shooting) {
                        x += xIncrement;
                    } else {
                        x -= xIncrement;
                    }
                    y = yVelocity * t + 0.5 * ACCELERATION * t * t;
                    if (Math.toDegrees(angle) <= 90 && Math.toDegrees(angle) > 0) {
                        anglesubstract = (Math.toDegrees(angle)) * (t / totalTime) * 2;
                    } else if (Math.toDegrees(angle) >= - 90 && Math.toDegrees(angle) <= 0) {
                        anglesubstract = 5 * (t / totalTime) * 2;
                    } else if (Math.toDegrees(angle) < - 90 && Math.toDegrees(angle) >= -180) {
                        anglesubstract = -5 * (t / totalTime) * 2;
                    } else {
                        anglesubstract = (180 - (Math.toDegrees(angle))) * (t / totalTime) * -2;
                    }
//                    if (Math.toDegrees(angle) - 360 < 0 && Math.toDegrees(angle) - 360 > -60) {
//                        anglesubstract = 10 * (t / totalTime) * 2;
//                    } else if (Math.toDegrees(angle) - 360 < 0 && Math.toDegrees(angle) - 360 > -120) {
//                        anglesubstract = 2 * (t / totalTime) * 2;
//                    } else if (Math.toDegrees(angle) - 360 < 0 && Math.toDegrees(angle) - 360 > -180) {
//                        anglesubstract = 1 * (t / totalTime) * 2;
//                    } else {
//                        anglesubstract = (Math.toDegrees(angle)) * (t / totalTime) * 2;
//                    }
                    projectilemotion();
                    //     System.out.println("\t" + round(x) + "\t" + round(y) + "\t" + round(t));
                    checkhit();
                    if (round(y) <= -50) {
                        ColorAdjust c = new ColorAdjust();
                        Color target = Color.GREEN;
                        c.setBrightness(0.6); // setting the brightness of the color.   
                        c.setContrast(0.2); // setting the contrast of the color  
                        c.setHue(target.getHue() + 0.1); // setting the hue of the color  
                        c.setSaturation(1); // setting the hue of the color. 
                        arrows.get(arrows.size() - 1).setEffect(c);
                        ColorAdjust c1 = new ColorAdjust();
                        c1.setContrast(1); // setting the contrast of the color  
                        if (arrows.size() >= 2) {
                            arrows.get(arrows.size() - 2).setEffect(c1);
                        }
                        Timeline timeline;

                        if (shooting) {
                            shooting = false;
                            timeline = new Timeline(new KeyFrame(Duration.millis(500),
                                    ae -> gotoplayer2()));
                            timeline.play();
                        }
                        if (shooting2) {
                            shooting2 = false;
                            timeline = new Timeline(new KeyFrame(Duration.millis(500),
                                    ae -> gotoplayer1()));
                            timeline.play();
                        }
                    }
                }

                if (health[0] <= 0 || health[1] <= 0) {
                    Text GameOver;
                    if (health[0] == 0) {
                        GameOver = new Text(body.getLayoutX() + 100, 300, "Player 1 Lose !");

                    } else {
                        GameOver = new Text(body2.getLayoutX() - 100, 300, "Player 2 Lose !");
                    }
                    windtext[0].setVisible(false);
                    windtext[1].setVisible(false);
                    GameOver.setFill(Color.RED);
                    GameOver.setFont(Font.font("Verdana", 50));
                    board.getChildren().add(GameOver);

                    // this.stop();
                }
            }
        };

        timer.start();
    }

    private void projectilemotion() {
        if (shooting) {
            arrows.get(arrows.size() - 1).relocate(x + 140, scene.getHeight() / 1.57 - y);
            pane.setHvalue(1.0 * (3600) / (10000) + (1.0 * x) / (10000));
            //  pane.setHvalue((2640)*0.45/(Screen.getMainScreen().getWidth() * 3)+(0.7*x)/(Screen.getMainScreen().getWidth() * 3+2500));
            if (arrows.get(arrows.size() - 1).rotateProperty().getValue() != 90) {
                arrows.get(arrows.size() - 1).rotateProperty().setValue(Math.toDegrees(-angle) + anglesubstract);
            }
        }

        if (shooting2) {
            arrows.get(arrows.size() - 1).relocate(x + 1000 + rand, scene.getHeight() / 1.57 - y);
            pane.setHvalue(1.0 * (4500 + rand) / (10000) + (1.0 * rand / 100000) + (1.0 * x) / (10000));
//pane.setHvalue(((3500+rand*1.0)/(Screen.getMainScreen().getWidth() * 3+2500))+(1.0*x)/(Screen.getMainScreen().getWidth() * 3+2500));
            if (arrows.get(arrows.size() - 1).rotateProperty().getValue() != 90) {
                arrows.get(arrows.size() - 1).rotateProperty().setValue(Math.toDegrees(angle) - anglesubstract + 180);
            }
        }

    }

    private void checkhit() {
        if (shooting) {
            if (arrows.get(arrows.size() - 1).getBoundsInParent().intersects(body2.getBoundsInParent())
                    || arrows.get(arrows.size() - 1).getBoundsInParent().intersects(hands[1].getBoundsInParent())) {
                board.getChildren().remove(arrows.get(arrows.size() - 1));
                arrows.remove(arrows.size() - 1);
                ac.play(300);
                double damage = 0.5 * 0.0025 * ((xVelocity * xVelocity) + (yVelocity * yVelocity)) + 3;
                if (health[1] < damage) {
                    health[1] = 0;
                } else {
                    health[1] -= damage;
                }
                healthbar[1].setProgress(health[1] / 100.0);
                shooting = false;
                damagetext[0].setText("-damage " + round(damage));
                damagetext[0].setVisible(true);
                Timeline timeline = new Timeline(new KeyFrame(Duration.millis(500),
                        ae -> gotoplayer2()));
                timeline.play();
            }
        }
        if (shooting2) {
            if (arrows.get(arrows.size() - 1).getBoundsInParent().intersects(body.getBoundsInParent())
                    || arrows.get(arrows.size() - 1).getBoundsInParent().intersects(hands[0].getBoundsInParent())) {
                board.getChildren().remove(arrows.get(arrows.size() - 1));
                arrows.remove(arrows.size() - 1);
                ac.play(300);
                double damage = 0.5 * 0.0025 * ((xVelocity * xVelocity) + (yVelocity * yVelocity)) + 3;
                if (health[0] < damage) {
                    health[0] = 0;
                } else {
                    health[0] -= damage;
                }
                healthbar[0].setProgress(health[0] / 100.0);
                shooting2 = false;
                damagetext[1].setText("-damage " + round(damage));
                damagetext[1].setVisible(true);
                Timeline timeline = new Timeline(new KeyFrame(Duration.millis(500),
                        ae -> gotoplayer1()));
                timeline.play();
            }
        }
    }

    private void gotoplayer1() {
        pane.setHvalue(1.0 * (3600) / (10000));
        winddirection = new Random().nextInt(2);
        windforce = new Random().nextInt(6);
        windtext[0].setVisible(true);
        windtext[1].setVisible(false);
        arrow[1].setDisable(true);
        arrow[0].setDisable(false);
        if (windforce == 0) {
            windtext[0].setText(" Wind force: " + windforce * 10);
        } else if (winddirection == 1) {
            windtext[0].setText(" Wind force: ⇒ " + windforce * 10);
        } else {
            windtext[0].setText(" Wind force: ⇐ " + windforce * 10);
        }
    }

    private void gotoplayer2() {
        pane.setHvalue(1.0 * (4500 + rand) / (10000) + (1.0 * rand / 100000));
        winddirection = new Random().nextInt(2);
        windforce = new Random().nextInt(6);
        windtext[1].setVisible(true);
        windtext[0].setVisible(false);
        arrow[0].setDisable(true);
        arrow[1].setDisable(false);
        if (windforce == 0) {
            windtext[1].setText(" Wind force: " + windforce * 10);
        } else if (winddirection == 1) {
            windtext[1].setText(" Wind force: ⇐ " + windforce * 10);
        } else {
            windtext[1].setText(" Wind force: ⇒ " + windforce * 10);
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
