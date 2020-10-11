import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

class BankTest {
    @Test
    void testMain() {
        String[] argsSmall = new String[]{"small.txt", "20"};
        Bank.main(argsSmall);

        String[] argsMid = new String[]{"5k.txt"};
        Bank.main(argsMid);

        String[] argsHard = new String[]{"100k.txt", "13", "200"};
        Bank.main(argsHard);

        Bank.main(new String[]{});
    }

    @Test
    void testTransaction() {
        Transaction tr = new Transaction(3, 5, 500);
        assertEquals("from:3 to:5 amt:500", tr.toString());
    }
}