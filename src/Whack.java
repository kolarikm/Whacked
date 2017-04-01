import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by Michael on 4/1/17.
 */

public class Whack {

    public ReentrantLock lock1 = new ReentrantLock();
    public ReentrantLock lock2 = new ReentrantLock();
    public ReentrantLock lock3 = new ReentrantLock();
    public ReentrantLock lock4 = new ReentrantLock();

    public int[][] board;

    public static void main (String[] args) {
        Whack game = new Whack();
        game.setupGame(args[0]);
    }

    public void setupGame(String arg) {
        board = new int[4][4];

        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                board[i][j] = 0;
                System.out.print(board[i][j] + " ");
            }
            System.out.println();
        }

        for (int i = 0; i < Integer.parseInt(arg); i++) {
            if (i < 4) {
                new Mole(i, lock1);
            }
            else if (i > 3 && i < 8) {
                new Mole(i, lock2);
            }
            else if (i > 7 && i < 12) {
                new Mole(i, lock3);
            }
            else {
                new Mole(i, lock4);
            }
        }
    }
}