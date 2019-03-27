/*
Rozwiazanie polega na kozystaniu ze stolu tylko i wylacznie przez jednego filozofa w danym momencie.
*/

package exercise4;

import exercise1.BinarySemaphore;

class Fork3 {
    private BinarySemaphore sem = new BinarySemaphore();

    public void getFork() {
        try {
            sem.semWait();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void leaveFork() {
        sem.semSignal();
    }
}


class Philosopher3 extends Thread {
    private int id;
    private Fork3[] forks;
    BinarySemaphore tableOccupied;

    public Philosopher3(int id, Fork3[] forks, BinarySemaphore tableOccupied) {
        this.id = id;
        this.forks = forks;
        this.tableOccupied = tableOccupied;
    }

    public void sleep() {
        System.out.println(id + " filozof zasypia");

        try {
            Thread.sleep(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println(id + " filozof budzi sie");
    }

    private void getFirstFork() {
        try {
            tableOccupied.semWait();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(id + " filozof chce zabrac widelec " + id);
        forks[id].getFork();
        System.out.println(id + " filozof zabral widelec " + id);
    }

    private void getSecondFork() {
        System.out.println(id + " filozof chce zabrac widelec " + (id + 1) % 5);
        forks[(id + 1) % 5].getFork();
        System.out.println(id + " filozof zabral widelec " + (id + 1) % 5);
    }

    private void eat() {
        System.out.println(id + " filozof zaczyna jesc");
        try {
            Thread.sleep(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        forks[id].leaveFork();
        forks[(id + 1) % 5].leaveFork();
        System.out.println(id + " filozof skonczyl jesc");
        tableOccupied.semSignal();
    }

    public void run() {
        while(true) {
            this.getFirstFork();
            this.getSecondFork();
            this.eat();
            this.sleep();
        }
    }
}

public class DiningPhilosophers3 {

    public static void main(String[] args) {
        Fork3[] forks = new Fork3[5];
        Philosopher3[] philosophers = new Philosopher3[5];
        BinarySemaphore tableOccupied = new BinarySemaphore();

        for (int i = 0; i < 5; i++) {
            forks[i] = new Fork3();
            philosophers[i] = new Philosopher3(i, forks, tableOccupied);
        }

        for (int i = 0; i < 5; i++) {
            philosophers[i].start();
        }

    }

}
