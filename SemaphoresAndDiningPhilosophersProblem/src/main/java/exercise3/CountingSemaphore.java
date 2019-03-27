/*
Semafor binarny jest szczegolnym przypadkiem semafora licznikowego - takim,
ktorego maksymalna liczba osblugiwanych sygnalow na raz jest 1
*/

package exercise3;

import exercise1.BinarySemaphore;

public class CountingSemaphore {
    private int state;
    private BinarySemaphore binarySemaphore = new BinarySemaphore();
    private BinarySemaphore stateNumberProtection = new BinarySemaphore();

    public CountingSemaphore() {
        this.state = 1;
    }

    public void countingSemWait() throws InterruptedException {

        binarySemaphore.semWait();
        stateNumberProtection.semWait();
        state--;
        if (state > 0)
            binarySemaphore.semSignal();
        stateNumberProtection.semSignal();

    }

    public void countingSemSignal() throws InterruptedException {

        stateNumberProtection.semWait();
        state++;
        if (state == 1)
            binarySemaphore.semSignal();
        stateNumberProtection.semSignal();

    }
}
