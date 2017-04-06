import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.TimeUnit;

import javax.swing.*;

public class Panel extends JFrame implements ActionListener {

    public static int dim;
    public static MoleButton[][] moles;

    public Panel() {
        setTitle("Whack-a-Mole");
        Container pane = getContentPane();

        String input = JOptionPane.showInputDialog(pane, "Desired board dimension?");

        try {
            dim = Integer.parseInt(input);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }

        pane.setLayout(new GridLayout(dim, dim));
        moles = new MoleButton[dim][dim];

        int id = 1;
        for (int i = 0; i < dim; i++) {
            for (int j = 0; j < dim; j++) {
                MoleButton button = new MoleButton();
                pane.add(button);
                button.addActionListener(this);
                moles[i][j] = button;
            }
        }
    }

    public static void whack(Whacked game) throws InterruptedException {
        //Every 1/4 second, check state of the game.board and update all buttons based on that state
        boolean playing = true;
        while (playing) {
            for (int i = 0; i < dim; i++) {
                for (int j = 0; j < dim; j++) {
                    int[][] state = game.getBoard();
                    if (state[i][j] == 1) {
                        moles[i][j].up();
                    }
                }
            }
            TimeUnit.MILLISECONDS.sleep(1000);
        }
    }

    private static class MoleButton extends JButton {
        public boolean up;
        public MoleButton() {
            super();
            this.up = false;
            this.setText("");
        }
        public void up() {
            this.up = true;
            this.setText("UP!");
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
        JButton b = (JButton)e.getSource();
        ///Check
    }
}
