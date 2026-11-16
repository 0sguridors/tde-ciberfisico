import java.util.concurrent.Semaphore;

public class FilosofosHierarquia {

    static final int N = 5;
    static final int REFEICOES = 10;
    static final Semaphore[] garfos = new Semaphore[N];
    static final int[] refeicoes = new int[N];

    public static void main(String[] args) throws Exception {
        for (int i = 0; i < N; i++) {
            garfos[i] = new Semaphore(1, true);
        }

        Thread[] threads = new Thread[N];
        for (int i = 0; i < N; i++) {
            final int id = i;
            threads[i] = new Thread(() -> filosofo(id), "Filosofo-" + id);
            threads[i].start();
        }
        for (Thread t : threads) {
            t.join();
        }

        System.out.println("\nTodos terminaram. Refeicoes por filosofo:");
        for (int i = 0; i < N; i++) {
            System.out.println("Filosofo " + i + ": " + refeicoes[i]);
        }
    }

    static void filosofo(int id) {
        int a = id;
        int b = (id + 1) % N;
        int menor = Math.min(a, b);
        int maior = Math.max(a, b);
        try {
            for (int k = 0; k < REFEICOES; k++) {
                System.out.println("[Filosofo " + id + "] pensando");
                Thread.sleep((long) (Math.random() * 100));

                System.out.println("[Filosofo " + id + "] com fome");
                garfos[menor].acquire();
                Thread.sleep(100);
                garfos[maior].acquire();

                System.out.println("[Filosofo " + id + "] comendo (refeicao " + (k + 1) + ")");
                refeicoes[id]++;
                Thread.sleep(50);

                garfos[maior].release();
                garfos[menor].release();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
