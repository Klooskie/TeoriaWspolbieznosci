/*
Rozwiazanie polega na uzyciu hierarchii zasobow, tzn zawsze zaczynaniu dobierania swoich widelcy
od widelca posiadajacego mniejszy numer.
*/

package exercise4;

import exercise1.BinarySemaphore;

class Fork2 {
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


class Philosopher2 extends Thread {
    private int id;
    private Fork2[] forks;

    public Philosopher2(int id, Fork2[] forks) {
        this.id = id;
        this.forks = forks;
    }

    public void sleep() {
        System.out.println(id + " filozof zasypia");

        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println(id + " filozof budzi sie");
    }

    private void getFirstFork() {
        int fork_id;
        if (id < (id + 1) % 5)
            fork_id = id;
        else
            fork_id = (id + 1) % 5;

        System.out.println(id + " filozof chce zabrac widelec " + fork_id);
        forks[fork_id].getFork();
        System.out.println(id + " filozof zabral widelec " + fork_id);
    }

    private void getSecondFork() {
        int fork_id;
        if (id < (id + 1) % 5)
            fork_id = (id + 1) % 5;
        else
            fork_id = id;

        System.out.println(id + " filozof chce zabrac widelec " + fork_id);
        forks[fork_id].getFork();
        System.out.println(id + " filozof zabral widelec " + fork_id);
    }

    private void eat() {
        System.out.println(id + " filozof zaczyna jesc");
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        forks[id].leaveFork();
        forks[(id + 1) % 5].leaveFork();
        System.out.println(id + " filozof skonczyl jesc");
    }

    public void run() {
        while (true) {
            this.getFirstFork();
            this.getSecondFork();
            this.eat();
            this.sleep();
        }
    }
}

public class DiningPhilosophers2 {

    public static void main(String[] args) {
        Fork2[] forks = new Fork2[5];
        Philosopher2[] philosophers = new Philosopher2[5];

        for (int i = 0; i < 5; i++) {
            forks[i] = new Fork2();
            philosophers[i] = new Philosopher2(i, forks);
        }

        for (int i = 0; i < 5; i++) {
            philosophers[i].start();
        }

    }
}
