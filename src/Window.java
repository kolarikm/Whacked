import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.concurrent.Task;
import javafx.util.Duration;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.util.Random;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public class Window extends Application {

    static MoleButton[][] moles;
    static Semaphore sem;
    static volatile int[][] holes;

    private static class MoleButton extends Button {
        int x, y;

        public MoleButton(int x, int y) {
            super();
            setCoords(x, y);
        }

        public void setCoords(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    public void drawBoard(int[][] board) throws InterruptedException {
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                moles[i][j].setText("" + board[i][j]);
            }
        }
    }

    public void playGame(Whacked game) throws InterruptedException {
        for (int i = 60; i > 0; i--) {
            Task<Void> task = new Task<Void>() {
                @Override
                protected Void call() throws Exception {
                    drawBoard(holes);
                    return null;
                }
            };
            task.run();
            System.out.println("Running task");
            TimeUnit.SECONDS.sleep(1);
        }
    }

    @Override
    public void start(Stage primaryStage) throws Exception, InterruptedException {

        Whacked game = new Whacked(5);
        moles = new MoleButton[5][5];

        primaryStage.setTitle("Whack-A-Mole!");

        GridPane gridPane = new GridPane();

        int x = 0;
        int y = 0;

        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                MoleButton b = new MoleButton(x, y);
                moles[i][j] = b;
                gridPane.add(b, y++, x, 1, 1);
                b.setText("  ");
                b.setPadding(new Insets(10, 10, 10, 10));
                b.setOnAction(value ->  {
                    b.setText("1");
                    //game.checkMole(b.x, b.y);
                });
            }
            y = 0;
            x++;
        }
        Scene scene = new Scene(gridPane, 200, 200);
        primaryStage.setScene(scene);
        primaryStage.show();

        playGame(game);
    }
}
