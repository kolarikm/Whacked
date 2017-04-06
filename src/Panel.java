import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.TimeUnit;

import javax.swing.*;

public class Panel extends JFrame implements ActionListener {

    public static int dim;
    public static int score;
    public static int time = 30;
    public static JLabel scoreLabel;
    public static JLabel timeLabel;
    public static Container pane;
    public static MoleButton[][] moles;

    public Panel() {
        setTitle("Whack-a-Mole");
        pane = getContentPane();

        String input = JOptionPane.showInputDialog(pane, "Desired board dimension?");

        try {
            dim = Integer.parseInt(input);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }

        pane.setLayout(new GridLayout(dim+1, dim, 5, 5));
        moles = new MoleButton[dim][dim];

        int id = 1;
        for (int i = 0; i < dim; i++) {
            for (int j = 0; j < dim; j++) {
                MoleButton button = new MoleButton(new Tuple<>(i, j));
                pane.add(button);
                button.addActionListener(this);
                moles[i][j] = button;
            }
        }

        scoreLabel = new JLabel("Score: " + score);
        timeLabel = new JLabel("Time: " + time);
        pane.add(scoreLabel);
        pane.add(timeLabel);
    }

    public static void whack(Whacked game) throws InterruptedException {
        //Every 1/4 second, check state of the game.board and update all buttons based on that state
        score = 0;
        int slept = 0;
        boolean playing = true;
        while (playing) {
            for (int i = 0; i < dim; i++) {
                for (int j = 0; j < dim; j++) {
                    int[][] state = game.getBoard();
                    if (state[i][j] == 1) {
                        moles[i][j].up();
                    } else {
                        moles[i][j].down();
                    }
                }
            }
            scoreLabel.setText("Score: " + score);
            TimeUnit.MILLISECONDS.sleep(250);
            slept++;
            if (slept % 4 == 0) {
                time--;
                timeLabel.setText("Time: " + time);
            }

            if (time == 0) {
                playing = false;
            }
        }

        //Game is over, present the score and then exit
        JOptionPane.showMessageDialog(pane, "Game Over!\nYour score was: " + score);
        System.exit(0);
    }

    private class Tuple<X, Y> {
        public final X x;
        public final Y y;

        public Tuple(X x, Y y) {
            this.x = x;
            this.y = y;
        }
    }

    private static class MoleButton extends JButton {
        public boolean up;
        public int x, y;

        public MoleButton(Tuple coords) {
            super();
            this.up = false;
            this.setText("");
            this.x = (int)coords.x;
            this.y = (int)coords.y;
        }

        public void up() {
            this.up = true;
            this.setText("UP!");
        }

        public void down() {
            this.up = false;
            this.setText("");
        }

        public boolean check() {
            return this.up;
        }
    }

    public static void main(String[] args) {
        Panel gt = new Panel();
        gt.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        gt.pack();
        gt.setVisible(true);
        Whacked game = new Whacked(dim);
        game.startGame();
        try {
            whack(game);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void actionPerformed(ActionEvent e) {
        MoleButton mb = (MoleButton)e.getSource();
        if (mb.check()) {
            mb.setText("");
            score++;
        }
    }
}
