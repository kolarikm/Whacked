/*******************************************************
 * Whack a Mole: A Java Concurrency Project
 * Created by: Michael Kolarik
 * Greg Wolffe - CIS 452 - Winter 2017
 *
 * A simulation of the classic Whack a Mole arcade game
 * used to demonstrate the principles of concurrency
 * through the utilization threading
 ******************************************************/

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.Random;

/**
 * The game logic class for a Whack a mole implementation focusing on
 * concurrency between a user defined number of moles(threads). Gets
 * instantiated within the UI class
 */
public class Whacked {

    //Is the game currently active
    static boolean playing;

    //User defined size of the board
    int dim;

    //Semaphore used to lock threads from working
    static Semaphore sem;

    //Logical representation of the board
    static int[][] holes;

    static Mole[][] moleThreads;

    /**
     * Instantiates a new game of Whack a mole.
     * @param arg the size of the board determined by a UI dialog
     */
    public Whacked(int arg) {
        this.dim = arg;
        this.playing = false;
        startGame();
    }

    /**
     * Assigns proper values to all fields and generates the correct number of
     * mole threads as defined by the player
     */
    public void startGame() {
        try {
            sem = new Semaphore(dim);
            holes = new int[dim][dim];
            moleThreads = new Mole[dim][dim];

        } catch (Exception e) {
            e.printStackTrace();
        }

        //Generates dim*dim number of mole threads
        for (int i = 0; i < dim; i++) {
            for (int j = 0; j < dim; j++) {
                Mole mole = new Mole(i, j);
                synchronized (moleThreads) {
                    moleThreads[i][j] = mole;
                }
                mole.start();
            }
        }
        this.playing = true;
    }

    public boolean checkMoleThread(int i, int j) {
        synchronized (moleThreads) {
            if (moleThreads[i][j].isAlive()) {
                moleThreads[i][j].interrupt();
                holes[i][j] = 0;
                return true;
            }
        }
        return false;
    }

    /**
     * Mole thread class responsible for representing a mole in the
     * whack a mole game board. One thread is created for each mole
     * in the program.
     */
    static class Mole extends Thread {

        //Indices representing the moles place in the board
        int i, j;

        //Random number generator used for determining sleep times
        Random rand = new Random();

        /**
         * Creates a new mole with specific indices and calls the run method
         * @param i the row index of the mole
         * @param j the column index of the mole
         */
        Mole(int i, int j) {
            this.i = i;
            this.j = j;
        }

        /**
         * Runs the actual work for the mole thread
         * Continuously moves up and down, waiting for a turn each time
         */
        public void run ()  {
            while (playing) {
                try {
                    //Initially sleep for a random time before requesting semaphore for the first time
                    TimeUnit.MILLISECONDS.sleep(rand.nextInt(1000));
                    while (playing) {
                        try {
                            sem.acquire();

                            //Once semaphore is acquired, set the state of the board for that thread
                            //Sleep between 1 and 5 seconds before retreating
                            synchronized (holes) {
                                holes[this.i][this.j] = 1;
                            }

                            //Sleep for up to 4 seconds in the on or "up" state
                            TimeUnit.MILLISECONDS.sleep(rand.nextInt(4000));
                        } finally {
                            //Release the semaphore and update the board state for that hole
                            sem.release();
                            synchronized (holes) {
                                holes[this.i][this.j] = 0;
                            }
                            //Sleep before requesting semaphore once again
                            TimeUnit.MILLISECONDS.sleep(rand.nextInt(2000));
                        }
                    }
                } catch (InterruptedException ie) {
                    sem.release();
                    synchronized (holes) {
                        holes[this.i][this.j] = 0;
                    }
                    try {
                        //Sleep before requesting semaphore once again
                        TimeUnit.MILLISECONDS.sleep(2000);
                    } catch (Exception e) {

                    }
                }
            }
        }
    }

    /**
     * Returns the current state of the board based on which threads are up
     * @return the state of the board
     */
    public int[][] getBoard() {
        return this.holes;
    }
}
