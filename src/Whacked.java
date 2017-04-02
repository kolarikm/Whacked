/**s
 * Created by Andromeda on 4/1/17.
 */

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.Random;

public class Whacked {

    static Semaphore sem;
    protected static int[][] holes;

    static class Mole extends Thread {

        int id;
        Random rand = new Random();

        Mole(int id) {
            this.id = id;
        }

        public void run() {
            try {

                System.out.println("Mole " + id + " attempting to acquire lock...");
                sem.acquire();
                System.out.println("Mole " + id + " acquired lock!");
                int time = 0;

                try {
                    System.out.println("Mole " + id + " is doing work...");
                    time = rand.nextInt((10 - 1))+1;
                    TimeUnit.SECONDS.sleep(time);
                    synchronized (holes) {
                        holes[this.id][this.id] = this.id+1;
                    }

                } finally {
                    System.out.println("Mole " + id + " releasing lock after " + time + " seconds.");
                    sem.release();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static void print() {
        for (int i = 0; i < holes.length; i++) {
            for (int j = 0; j < holes[i].length; j++) {
                System.out.print("" + holes[i][j] + " ");
            }
            System.out.println();
        }
    }

    public static void main (String[] args) throws InterruptedException {

        Whacked game = new Whacked();

        try {
            int dim = Integer.parseInt(args[0]);
            sem = new Semaphore(dim);
            holes = new int[dim][dim];
        } catch (Exception e) {
            e.printStackTrace();
        }

        for (int i = 0; i < Integer.parseInt(args[0]); i++) {
            new Mole(i).start();
        }

        TimeUnit.SECONDS.sleep(13);
        print();
    }
}
