import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class MetropolisBrainTest {
    MetropolisBrain model;

    @BeforeEach
    public void setUp() {
        model = new MetropolisBrain();
    }

    @Test
    public void testSearch() {
        model.search("", "", 20400000,false, false);
        assertEquals(model.getRowCount(), 2);

        model.search("rme", "", -1, false, false);
        assertEquals(0, model.getRowCount());

        model.search("ome", "", -1, false, true);
        assertEquals(1, model.getRowCount());

        model.search("rome", "", -1, false, true);
        assertEquals(1, model.getRowCount());

        model.search("rme", "", 30, false, false);
        assertEquals(0, model.getRowCount());

        model.search("rome", "euro", 50000, true, false);
        assertEquals(0, model.getRowCount());

        model.search("london", "", 46455, false, true);
        assertEquals(1, model.getRowCount());

        model.search("rme", "5466", -1, false, true);
        assertEquals(0, model.getRowCount());

        model.search("rme", "444", -1, false, true);
        assertEquals(0, model.getRowCount());

        model.search("rome", "euro", 50000, false, true);
        assertEquals(1, model.getRowCount());

        model.search("", "asdasd", 50000, true, false);
        assertEquals(0, model.getRowCount());

        model.search("", "euro", 50000, false, true);
        assertEquals(3, model.getRowCount());
    }

    @Test
    public void testAdd() {
        int count = model.getRowCount();

        model.add("", "", -1);
        assertEquals(count, model.getRowCount());

        model.add("asdasd", "", -1);
        assertEquals(count, model.getRowCount());

        model.add("mklasmd", "masndjlasnd", -1);
        assertEquals(count, model.getRowCount());

        model.add("tbilisi", "europe", 1500000);
        assertEquals(count + 1, model.getRowCount());
        assertEquals("population", model.getColumnName(2));

        assertNull(model.getValueAt(700, 2));
        assertNull(model.getValueAt(3, 17));
        assertNotNull(model.getValueAt(0, 0));

        assertFalse(model.isCellEditable(9, 9));
    }

}
