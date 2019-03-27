/*
Instrukcja if uzyta podczas implementacji semafora binarnego nie jest wystarczajaca, konieczne jest uzycie instrukcji while.
Latwo mozemy pokazac kontrprzyklad wystarczalnosci instrukcji if. Rozpatrzmy sytuacje, w ktorej watek nr 1 blokuje dostep
do semafora. Watek nr 2 probuje otrzymac dostep do semafora, co oczywiscie nie jest mozliwe, wiec watek nr 2 wchodzi do instrukcji
wait. Watek nr 1 zwalnia semafor i zaraz po tym watek nr 3 uzyskuje dostep do semafora. Watek nr 2 zostaje powiadomiony przez
watek nr 1 o zwolnieniu semafora i (co jest spowodowane uzyciem instrukcji if) nie sprawdza ponownie warunku zajetosci semafora,
wiec zajmuje go, mimo ze semafor jest juz zajety przez watek nr 3.

na ponizszym przykladzie role watka nr 3 pelni 'nowy' watek tworzony w petli for, a watkiem zajmujacym semafor, mimo ze ten
jest juz zajety jest ktorys z watkow utworzonych wczesniej i czekajacych na dostep do semafora. Konsekwencja jest spora szansa,
za program nie zwroci nam pozadanego countera - 1 000 000
*/

package exercise2;

public class BadRace {

    private static int counter = 0;
    private static BadBinarySemaphore sem = new BadBinarySemaphore();

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
