package exercise1;

public class BinarySemaphore {
    private boolean state;

    public BinarySemaphore() {
        this.state = true;
    }

    public synchronized void semWait() throws InterruptedException {
        while (!this.state) {
            this.wait();
        }
        this.state = false;
    }

    public synchronized void semSignal() {
        this.state = true;
        this.notify();
    }
}
