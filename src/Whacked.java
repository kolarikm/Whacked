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

        for (int i = 0; i < dim; i++) {
            for (int j = 0; j < dim; j++) {
                new Mole(i, j).start();
                System.out.println(String.format("Mole @ %1$d, %2$d", i, j));
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

        int i, j;
        Random rand = new Random();

        Mole(int i, int j) {
            this.i = i;
            this.j = j;
        }

        public void run () {
            while (playing) {
                //Set a random time for the thread to sleep before requesting semaphore

                //Once semaphore is acquired, set the state of the board for that thread
                //Sleep between 1 and 5 seconds before retreating

                //Once semaphore has been released, set state of board back to 0
                //Sleep between 1-3 seconds before requesting semaphore again
                holes[i][j] = 1;
            }
        }

//        public void run() {
//            try {
//                for (int x = 10; x > 0; x--) {
//                    int time = rand.nextInt(300);
//                    TimeUnit.MILLISECONDS.sleep(time);
//                    sem.acquire();
//                    try {
//                        time = rand.nextInt(600);
//                        TimeUnit.SECONDS.sleep(time);
//                        synchronized (holes) {
//                            holes[this.i][this.j] = 1;
//                            System.out.println("Setting mole " + this.getName());
//                        }
//                    } finally {
//                        sem.release();
//                        synchronized (holes) {
//                            holes[this.i][this.j] = 0;
//                            System.out.println("Removing mole " + this.getName());
//                            time = rand.nextInt(150);
//                            TimeUnit.MILLISECONDS.sleep(time);
//                        }
//                    }
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
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

    public static void main(String[] args) throws InterruptedException {

    }
}
