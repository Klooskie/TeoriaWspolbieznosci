/*
Rozwiazanie polega na wykorzystaniu kelnera. Jego zadaniem jest przydzielanie widelcy konkretnemu filozofowi,
w momencie gdy takowy czeka na widelce oraz obydwa widelce sa nieuzywane.
*/

package exercise4;

class Waiter {
    private boolean[] forks_occupied;

    public Waiter() {
        forks_occupied = new boolean[5];
        for (int i = 0; i < 5; i++) {
            forks_occupied[i] = false;
        }
    }

    public synchronized void giveForks(int i) throws InterruptedException {
        while (forks_occupied[i] || forks_occupied[(i + 1) % 5]) {
            this.wait();
        }
        forks_occupied[i] = true;
        forks_occupied[(i + 1) % 5] = true;
    }

    public synchronized void getForksBack(int i) {
        forks_occupied[i] = false;
        forks_occupied[(i + 1) % 5] = false;
        this.notify();
    }
}


class Philosopher1 extends Thread {
    private int id;
    private Waiter waiter;

    public Philosopher1(int id, Waiter waiter) {
        this.id = id;
        this.waiter = waiter;
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

    private void getForks() {
        System.out.println(id + " filozof chce zebrac widelce " + id + " " + (id + 1) % 5);

        try {
            waiter.giveForks(id);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println(id + " filozof chce otrzymal widelce " + id + " " + (id + 1) % 5);

    }

    private void eat() {
        System.out.println(id + " filozof zaczyna jesc");
        try {
            Thread.sleep(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        waiter.getForksBack(id);
        System.out.println(id + " filozof skonczyl jesc");
    }

    public void run() {
        while (true) {
            this.getForks();
            this.eat();
            this.sleep();
        }
    }
}

public class DiningPhilosophers1 {

    public static void main(String[] args) {
        Philosopher1[] philosophers = new Philosopher1[5];
        Waiter waiter = new Waiter();

        for (int i = 0; i < 5; i++) {
            philosophers[i] = new Philosopher1(i, waiter);
        }

        for (int i = 0; i < 5; i++) {
            philosophers[i].start();
        }

    }

}
