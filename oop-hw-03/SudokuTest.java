import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class SudokuTest {

    @Test
    void exceptions(){
        assertThrows(RuntimeException.class, ()-> new Sudoku(Sudoku.textToGrid(
                         "1 6 4 0 0 0 0 0 2\n"
                        +"2 0 0 4 0 3 9 1 0\n"
                        +"0 0 5 0 8 0 4 0 7\n"
                        +"0 9 0 0 0 6 5 0 0\n"
                        +"5 0 0 1 0 2 0 0 8\n"
                        +"0 0 8 9 0 0 0 3 0\n"
                        +"8 0 9 0 4 0 2 0 0\n"
                        +"abdasj0 0 1\n"
                        +"4 0 0 0 0 0 6 7 9")));

        assertThrows(RuntimeException.class, ()-> new Sudoku(Sudoku.textToGrid(
                         "1 0 0 0 0 1 0 0 0\n"
                        +"0 0 0 0 0 0 0 0 0\n"
                        +"0 0 0 0 0 0 0 0 0\n"
                        +"0 0 0 0 0 0 0 0 0\n"
                        +"0 0 0 0 0 0 0 0 0\n"
                        +"0 0 0 0 0 0 0 0 0\n"
                        +"0 0 0 0 0 0 0 0 0\n"
                        +"0 0 0 0 0 0 0 0 0\n"
                        +"0 0 0 0 0 0 0 0 0")));

        assertThrows(RuntimeException.class, ()-> new Sudoku(Sudoku.textToGrid(
                         "0 3 0 0 0 0 0 0 0\n"
                        +"0 0 0 0 0 0 0 0 0\n"
                        +"0 0 0 0 0 0 0 0 0\n"
                        +"0 0 0 0 0 0 0 0 0\n"
                        +"0 0 0 0 0 0 0 0 0\n"
                        +"0 0 0 0 0 0 0 0 0\n"
                        +"0 0 0 0 0 0 0 0 0\n"
                        +"0 3 0 0 0 0 0 0 0\n"
                        +"0 0 0 0 0 0 0 0 0")));

        assertThrows(RuntimeException.class, ()-> new Sudoku(Sudoku.textToGrid(
                         "0 0 9 0 0 0 0 0 0\n"
                        +"0 9 0 0 0 0 0 0 0\n"
                        +"0 0 0 0 0 0 0 0 0\n"
                        +"0 0 0 0 0 0 0 0 0\n"
                        +"0 0 0 0 0 0 0 0 0\n"
                        +"0 0 0 0 0 0 0 0 0\n"
                        +"0 0 0 0 0 0 0 0 0\n"
                        +"0 0 0 0 0 0 0 0 0\n"
                        +"0 0 0 0 0 0 0 0 0")));
    }

    @Test
    void main() {
        String[] arr = new String[1];
        Sudoku.main(arr);
    }

    @Test
    void solve() {
        Sudoku s = new Sudoku(Sudoku.hardGrid);
        assertEquals(1, s.solve());

        Sudoku emptyGrid = new Sudoku(Sudoku.textToGrid(
                         "0 0 0 0 0 0 0 0 0\n"
                        +"0 0 0 0 0 0 0 0 0\n"
                        +"0 0 0 0 0 0 0 0 0\n"
                        +"0 0 0 0 0 0 0 0 0\n"
                        +"0 0 0 0 0 0 0 0 0\n"
                        +"0 0 0 0 0 0 0 0 0\n"
                        +"0 0 0 0 0 0 0 0 0\n"
                        +"0 0 0 0 0 0 0 0 0\n"
                        +"0 0 0 0 0 0 0 0 0\n"));

        Sudoku hardGrid = new Sudoku(Sudoku.textToGrid(
                         "3 0 0 0 0 0 0 8 0\n"
                        +"0 0 1 0 9 3 0 0 0\n"
                        +"0 4 0 7 8 0 0 0 3\n"
                        +"0 9 3 8 0 0 0 1 2\n"
                        +"0 0 0 0 4 0 0 0 0\n"
                        +"5 2 0 0 0 6 7 9 0\n"
                        +"6 0 0 0 2 1 0 4 0\n"
                        +"0 0 0 5 3 0 9 0 0\n"
                        +"0 3 0 0 0 0 0 5 1"));

        assertEquals(6, hardGrid.solve());
        assertEquals(Sudoku.MAX_SOLUTIONS, emptyGrid.solve());

        Sudoku bad = new Sudoku(Sudoku.textToGrid(
                         "1 2 0 4 5 6 7 8 9\n"
                        +"0 3 0 0 0 0 0 0 0\n"
                        +"0 0 0 0 0 0 0 0 0\n"
                        +"0 0 0 0 0 0 0 0 0\n"
                        +"0 0 0 0 0 0 0 0 0\n"
                        +"0 0 0 0 0 0 0 0 0\n"
                        +"0 0 0 0 0 0 0 0 0\n"
                        +"0 0 0 0 0 0 0 0 0\n"
                        +"0 0 0 0 0 0 0 0 0\n"));

        assertEquals(0, bad.solve());
        assertEquals("", bad.getSolutionText());

        Sudoku solved = new Sudoku(Sudoku.textToGrid(
                 "3 7 5 1 6 2 4 8 9\n"
                +"8 6 1 4 9 3 5 2 7\n"
                +"2 4 9 7 8 5 1 6 3\n"
                +"4 9 3 8 5 7 6 1 2\n"
                +"7 1 6 2 4 9 8 3 5\n"
                +"5 2 8 3 1 6 7 9 4\n"
                +"6 5 7 9 2 1 3 4 8\n"
                +"1 8 2 5 3 4 9 7 6\n"
                +"9 3 0 6 7 8 2 5 1"));
        assertEquals(1, solved.solve());
    }
}
