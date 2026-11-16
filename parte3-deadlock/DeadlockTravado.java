import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.util.concurrent.locks.ReentrantLock;

public class DeadlockTravado {

    static final ReentrantLock LOCK_A = new ReentrantLock();
    static final ReentrantLock LOCK_B = new ReentrantLock();

    public static void main(String[] args) throws Exception {
        Thread t1 = new Thread(() -> {
            LOCK_A.lock();
            System.out.println("T1 pegou LOCK_A");
            try {
                dormir(50);
                System.out.println("T1 tentando LOCK_B");
                LOCK_B.lock();
                try {
                    System.out.println("T1 concluiu");
                } finally {
                    LOCK_B.unlock();
                }
            } finally {
                LOCK_A.unlock();
            }
        }, "T1");

        Thread t2 = new Thread(() -> {
            LOCK_B.lock();
            System.out.println("T2 pegou LOCK_B");
            try {
                dormir(50);
                System.out.println("T2 tentando LOCK_A");
                LOCK_A.lock();
                try {
                    System.out.println("T2 concluiu");
                } finally {
                    LOCK_A.unlock();
                }
            } finally {
                LOCK_B.unlock();
            }
        }, "T2");

        t1.start();
        t2.start();

        ThreadMXBean bean = ManagementFactory.getThreadMXBean();
        Thread.sleep(1000);
        long[] ids = bean.findDeadlockedThreads();
        if (ids != null) {
            System.out.println("\nDEADLOCK detectado. Threads bloqueadas:");
            for (long id : ids) {
                System.out.println(" - " + bean.getThreadInfo(id).getThreadName());
            }
            if (args.length > 0 && args[0].equals("--espera")) {
                System.out.println("Mantendo o programa vivo para rodar o jstack...");
                t1.join();
            } else {
                System.exit(1);
            }
        }
    }

    static void dormir(long ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
