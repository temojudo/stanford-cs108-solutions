// Account.java

/*
 Simple, thread-safe Account class encapsulates
 a balance and a transaction count.
*/
public class Account {
    private static final int BALANCE = 1000;

    private final int id;
    private int balance;
    private int transactions;

    public Account(int id) {
        this(id, BALANCE);
    }

    public Account(int id, int balance) {
        this.id = id;
        this.balance = balance;
        transactions = 0;
    }

    public synchronized void change(int amount) {
        transactions++;
        balance += amount;
    }

    public String toString() {
        return "acct:" + id + " bal:" + balance + " trans:" + transactions;
    }
}
