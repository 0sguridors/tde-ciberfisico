import java.util.concurrent.Semaphore;

public class FilosofosIngenuo {

    static final int N = 5;
    static final Semaphore[] garfos = new Semaphore[N];
    static final String[] estado = new String[N];
    static volatile long ultimaRefeicao = System.currentTimeMillis();

    public static void main(String[] args) throws Exception {
        for (int i = 0; i < N; i++) {
            garfos[i] = new Semaphore(1);
            estado[i] = "pensando";
        }

        for (int i = 0; i < N; i++) {
            final int id = i;
            Thread t = new Thread(() -> filosofo(id), "Filosofo-" + id);
            t.setDaemon(true);
            t.start();
        }

        while (true) {
            Thread.sleep(500);
            if (System.currentTimeMillis() - ultimaRefeicao > 3000) {
                System.out.println("\nDEADLOCK: ninguem come ha mais de 3s");
                for (int i = 0; i < N; i++) {
                    System.out.println("Filosofo " + i + " -> " + estado[i]);
                }
                System.exit(1);
            }
        }
    }

    static void filosofo(int id) {
        int esq = id;
        int dir = (id + 1) % N;
        try {
            while (true) {
                log(id, "pensando");
                Thread.sleep((long) (Math.random() * 100));

                log(id, "com fome");
                garfos[esq].acquire();
                log(id, "pegou garfo " + esq + " (esquerda)");
                Thread.sleep(100); 
                garfos[dir].acquire();
                log(id, "pegou garfo " + dir + " (direita)");

                log(id, "comendo");
                ultimaRefeicao = System.currentTimeMillis();
                Thread.sleep(50);

                garfos[dir].release();
                garfos[esq].release();
            }
        } catch (InterruptedException e) {
            return;
        }
    }

    static void log(int id, String msg) {
        if (msg.equals("pensando") || msg.equals("com fome") || msg.equals("comendo")) {
            estado[id] = msg;
        }
        System.out.println("[Filosofo " + id + "] " + msg);
    }
}
