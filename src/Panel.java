/*******************************************************
 * Whack a Mole: A Java Concurrency Project
 * Created by: Michael Kolarik
 * Greg Wolffe - CIS 452 - Winter 2017
 *
 * A simulation of the classic Whack a Mole arcade game
 * used to demonstrate the principles of concurrency
 * through the utilization threading
 ******************************************************/

import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.TimeUnit;

import javax.swing.*;

/**
 * The UI class for a Whack a Mole simulation
 * Handles user interaction and updates game
 * according to inputs
 */
public class Panel extends JFrame implements ActionListener {

    //Dimension of game board
    public static int dim;

    //Represents the players current score
    public static int score;

    //The amount of seconds the game will be played
    public static int time = 30;

    //Labels for score and timer
    public static JLabel scoreLabel;
    public static JLabel timeLabel;

    //The overall container the UI resides in
    public static Container pane;

    //Array of buttons representing a mole thread
    public static MoleButton[][] moles;

    /**
     * Creates the game panel
     * Sets the layout and window configuration before generating
     * and configuring a button for each mole
     */
    public Panel() {
        setTitle("Whack-a-Mole");
        pane = getContentPane();

        //Acquire a board size from the player
        String input = JOptionPane.showInputDialog(pane, "Desired board dimension?");

        //Attempt to set the board size based on input, fail on invalid input and exit
        try {
            dim = Integer.parseInt(input);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }

        pane.setLayout(new GridLayout(dim+1, dim, 5, 5));
        moles = new MoleButton[dim][dim];

        //Generate a unique button for the number of moles
        for (int i = 0; i < dim; i++) {
            for (int j = 0; j < dim; j++) {
                MoleButton button = new MoleButton(i, j);
                pane.add(button);
                button.addActionListener(this);
                moles[i][j] = button;
            }
        }

        //Add labels to bottom of window
        scoreLabel = new JLabel("Score: " + score);
        timeLabel = new JLabel("Time: " + time);
        pane.add(scoreLabel);
        pane.add(timeLabel);
    }

    /**
     * The function which works during game play. Checks the
     * state of the game board 4 times per second and updates
     * the view accordingly. Also manages the user's score and
     * ends the game when the time has expired
     * @param game a reference to the game simulation
     * @throws InterruptedException
     */
    public static void whack(Whacked game) throws InterruptedException {
        score = 0;
        int slept = 0;
        boolean playing = true;
        //Checks status of game board, sets the button labels based on state
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

            //Sleep for quarter second before repeating update
            TimeUnit.MILLISECONDS.sleep(250);

            //Every 4 sleeps, 1 second, decrement the timer
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

    /**
     * Private extension of JButton class to allow for additional
     * fields for active state and coordinates on the grid
     */
    private static class MoleButton extends JButton {
        public boolean up;
        public int x, y;

        /**
         * Construct a mole button
         * @param x row coordinate on grid
         * @param y column coordinate on grid
         */
        public MoleButton(int x, int y) {
            super();
            this.up = false;
            this.setText("");
            this.x = x;
            this.y = y;
        }

        /**
         * Set state of button to up; mole is active
         */
        public void up() {
            this.up = true;
            this.setText("UP!");
        }

        /**
         * Set state of button to down; mole is hiding
         */
        public void down() {
            this.up = false;
            this.setText("");
        }

        /**
         * Check status of this mole
         * @return whether or not the mole is up or down
         */
        public boolean check() {
            return this.up;
        }
    }

    /**
     * Creates the game panel as well as a game simulation object
     * Begins execution of game after UI is drawn
     * @param args user program arguments
     */
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

    /**
     * Handles the user input of a mouse click on a button
     * Checks if the button is in an up state and updates text
     * and score accordingly
     * @param e the captured mouse click event
     */
    public void actionPerformed(ActionEvent e) {
        MoleButton mb = (MoleButton)e.getSource();
        if (mb.check()) {
            mb.setText("");
            score++;
        }
    }
}
