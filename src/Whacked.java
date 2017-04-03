/**
 * Created by Andromeda on 4/1/17.
 */

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.Random;
import java.util.concurrent.atomic.AtomicIntegerArray;

public class Whacked {

    static Semaphore sem;
    static volatile int[][] holes;
    //static AtomicIntegerArray[][] holes;

    static class Mole extends Thread {

        int i, j;
        Random rand = new Random();

        Mole(int i, int j) {
            this.i = i;
            this.j = j;
        }

        public void run() {
            try {
                for (int x = 3; x > 0; x--) {
                    //System.out.println("Mole " + id + " attempting to acquire lock...");
                    int time = rand.nextInt((3-1))+1;
                    TimeUnit.SECONDS.sleep(time);
                    sem.acquire();
                    //System.out.println("Mole " + id + " acquired lock!");
                    try {
                        //System.out.println("Mole " + id + " is doing work...");
                        time = rand.nextInt((5 - 1))+1;
                        TimeUnit.SECONDS.sleep(time);
                        synchronized (holes) {
                            holes[this.i][this.j] = (i + 1) * (j + 1);
                            System.out.println("Mole " + i + ", " + j + " popped up!");
                            print();
                        }
                    } finally {
                        //System.out.println("Mole " + id + " releasing lock after " + time + " seconds.");
                        sem.release();
                        synchronized (holes) {
                            //holes[this.id][this.id] = 0;
                            System.out.println("Mole " + i + ", " + j + " retreated!");
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static void print() {
        synchronized (holes) {
            for (int i = 0; i < holes.length; i++) {
                for (int j = 0; j < holes[i].length; j++) {
                    System.out.print("" + holes[i][j] + " ");
                }
                System.out.println();
            }
        }
    }

    public static void main (String[] args) throws InterruptedException {

        Whacked game = new Whacked();
        int dim = 0;

        try {
            dim = Integer.parseInt(args[0]);
            sem = new Semaphore(dim);
            holes = new int[dim][dim];
        } catch (Exception e) {
            e.printStackTrace();
        }

        for (int i = 0; i < dim; i++) {
            for (int j = 0; j < dim; j++) {
                new Mole(i, j).start();
            }
        }
    }
}
