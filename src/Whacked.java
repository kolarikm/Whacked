import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.Random;

public class Whacked {

    static boolean playing;
    int dim;
    static Semaphore sem;
    static volatile int[][] holes;

    public Whacked(int arg) {
        this.dim = arg;
        this.playing = false;
        startGame();
    }

    public void startGame() {
        try {
            sem = new Semaphore(dim);
            holes = new int[dim][dim];
        } catch (Exception e) {
            e.printStackTrace();
        }

        int id = 0;
        for (int i = 0; i < dim; i++) {
            for (int j = 0; j < dim; j++) {
                new Mole(i, j, id++);
                //System.out.println(String.format("Mole @ %1$d, %2$d", i, j));
            }
        }
        this.playing = true;
    }

    public void checkMole(int x, int y) {
        System.out.println("Checking mole @: " + x + " " + y);
        if (holes[x][y] == 1) {
            System.out.println("Got that mole");
        } else {
            System.out.println("Missed");
        }
    }

    static class Mole extends Thread {

        int i, j, moleId;
        Random rand = new Random();

        Mole(int i, int j, int moleId) {
            this.i = i;
            this.j = j;
            this.moleId = moleId;
            start();
        }

        public int getMoleId() {
            return this.moleId;
        }

        public void run ()  {
            System.out.println(this.getMoleId());
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
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static void print() {
        synchronized (holes) {
            System.out.print("\033[H\033[2J");
            System.out.flush();
            for (int i = 0; i < holes.length; i++) {
                for (int j = 0; j < holes[i].length; j++) {
                    System.out.print("" + holes[i][j] + " ");
                }
                System.out.println();
            }
        }
    }

    public int[][] getBoard() {
        return this.holes;
    }
}
