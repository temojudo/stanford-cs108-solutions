import org.junit.jupiter.api.Test;

class CrackerTest {

    @Test
    void testMain() {
        String[] args1 = new String[]{"34800e15707fae815d7c90d49de44aca97e2d759", "3", "17"};
        Cracker.main(args1);

        String[] args2 = new String[]{"bla"};
        Cracker.main(args2);

        String[] args3 = new String[]{"34800e15707fae815d7c90d49de44aca97e2d759", "3", "-1"};
        Cracker.main(args3);

        String[] args4 = new String[]{"34800e15707fae815d7c90d49de44aca97e2d759", "3"};
        Cracker.main(args4);

        String[] args5 = new String[]{"a!"};
        Cracker.main(args5);

        Cracker.main(new String[]{});
    }
}