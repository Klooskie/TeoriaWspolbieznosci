package exercise1;

public class Race {

    private static int counter = 0;
    private static BinarySemaphore sem = new BinarySemaphore();

    public static void main(String[] args) {

        for (int i = 0; i < 100; i++) {

            new Thread() {
                public void run() {
                    for (int i = 0; i < 10000; i++) {
                        try {
                            sem.semWait();
                            counter++;
                            sem.semSignal();
                        } catch (InterruptedException e) {
                            System.out.println("Wystapil blad podczas czekania na semafor");
                        }
                    }
                }
            }.start();

        }

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            System.out.println("Wystapil blad podczas czekania w mainie");
        }

        System.out.println(counter);

    }


}
