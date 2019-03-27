package exercise2;

public class BadBinarySemaphore {
    private boolean state;

    public BadBinarySemaphore() {
        this.state = true;
    }

    public synchronized void semWait() throws InterruptedException {
        if (!this.state) {
            this.wait();
        }
        this.state = false;
    }

    public synchronized void semSignal() {
        this.state = true;
        this.notify();

    }
}