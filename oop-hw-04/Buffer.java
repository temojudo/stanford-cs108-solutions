// Buffer.java

import java.util.ArrayList;
import java.util.concurrent.Semaphore;

/*
 Holds the transactions for the worker
 threads.
*/
public class Buffer {
    public static final int SIZE = 64;

    private final ArrayList<Transaction> transactions;

    private final Semaphore canAdd;
    private final Semaphore canRemove;
    private final Object alterLock;

    public Buffer() {
        transactions = new ArrayList<>();
        canAdd = new Semaphore(SIZE);
        canRemove = new Semaphore(0);
        alterLock = new Object();
    }

    public void add(Transaction t) {
        try {
            canAdd.acquire();
        } catch (InterruptedException ignored) { }

        synchronized (alterLock) {
            transactions.add(t);
        }

        canRemove.release();
    }

    public Transaction remove() {
        try {
            canRemove.acquire();
        } catch (InterruptedException ignored) { }

        Transaction t;
        synchronized (alterLock) {
            t = transactions.remove(0);
        }

        canAdd.release();
        return t;
    }
}
