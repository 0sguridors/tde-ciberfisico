import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Semaphore;

public class Contador {

    static final int T = 8;
    static final int M = 200_000;

    static int count;
    static final Semaphore sem = new Semaphore(1, true);

    public static void main(String[] args) throws Exception {
        System.out.println("T = " + T + ", M = " + M + ", esperado = " + (long) T * M);

        System.out.println("\nVersao 1: sem sincronizacao");
        for (int r = 1; r <= 3; r++) {
            rodar(false, r);
        }

        System.out.println("\nVersao 2: com semaforo binario");
        for (int r = 1; r <= 3; r++) {
            rodar(true, r);
        }
    }

    static void rodar(boolean comSemaforo, int execucao) throws Exception {
        count = 0;
        Thread[] threads = new Thread[T];
        CountDownLatch largada = new CountDownLatch(1);

        for (int i = 0; i < T; i++) {
            threads[i] = new Thread(() -> {
                try {
                    largada.await();
                } catch (InterruptedException e) {
                    return;
                }
                for (int j = 0; j < M; j++) {
                    if (comSemaforo) {
                        try {
                            sem.acquire();
                        } catch (InterruptedException e) {
                            return;
                        }
                        try {
                            count = count + 1;
                        } finally {
                            sem.release();
                        }
                    } else {
                        count = count + 1;
                    }
                }
            });
            threads[i].start();
        }
        long inicio = System.nanoTime();
        largada.countDown();
        for (Thread t : threads) {
            t.join();
        }
        long ms = (System.nanoTime() - inicio) / 1_000_000;

        System.out.println("execucao " + execucao + ": esperado = " + (long) T * M
                + ", obtido = " + count + ", tempo = " + ms + " ms");
    }
}
