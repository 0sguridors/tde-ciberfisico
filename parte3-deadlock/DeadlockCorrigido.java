import java.util.concurrent.locks.ReentrantLock;

public class DeadlockCorrigido {

    static final ReentrantLock LOCK_A = new ReentrantLock();
    static final ReentrantLock LOCK_B = new ReentrantLock();

    public static void main(String[] args) throws Exception {
        Thread t1 = new Thread(() -> executar("T1"), "T1");
        Thread t2 = new Thread(() -> executar("T2"), "T2");

        t1.start();
        t2.start();
        t1.join();
        t2.join();
        System.out.println("Programa terminou sem travar");
    }

    static void executar(String nome) {
        LOCK_A.lock();
        System.out.println(nome + " pegou LOCK_A");
        try {
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            LOCK_B.lock();
            System.out.println(nome + " pegou LOCK_B");
            try {
                System.out.println(nome + " concluiu");
            } finally {
                LOCK_B.unlock();
            }
        } finally {
            LOCK_A.unlock();
        }
    }
}
