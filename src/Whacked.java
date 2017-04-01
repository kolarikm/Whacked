/**s
 * Created by Andromeda on 4/1/17.
 */

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.Random;

public class Whacked {
    
    static Semaphore sem;

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
                } finally {
                    System.out.println("Mole " + id + " releasing lock after " + time + " seconds.");
                    sem.release();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void main (String[] args) {

        sem = new Semaphore(Integer.parseInt(args[1]));

        for (int i = 0; i < Integer.parseInt(args[0]); i++) {
            new Mole(i).start();
        }
    }

}
