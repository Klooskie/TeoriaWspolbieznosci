package exercise4;

import exercise1.BinarySemaphore;

class Fork {
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


class Philosopher extends Thread {
    private int id;
    private Fork[] forks;

    public Philosopher(int id, Fork[] forks) {
        this.id = id;
        this.forks = forks;
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

public class BadDiningPhilosophers {

    public static void main(String[] args) {
        Fork[] forks = new Fork[5];
        Philosopher[] philosophers = new Philosopher[5];

        for (int i = 0; i < 5; i++) {
            forks[i] = new Fork();
            philosophers[i] = new Philosopher(i, forks);
        }

        for (int i = 0; i < 5; i++) {
            philosophers[i].start();
        }

    }

}
