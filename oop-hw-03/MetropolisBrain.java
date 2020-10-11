import javax.swing.table.AbstractTableModel;
import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MetropolisBrain extends AbstractTableModel {
    private static final String EXAMPLE_DATABASE = "metropolises.sql";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "admin";
    private static final String SCHEMA = "oop_hw_03"; // oop_hw_03 -> my schema name
    private static final String URL = "jdbc:mysql://localhost:3306/" + SCHEMA;
    private static final String DATABASE = "metropolises"; // database name

    private static final String[] COL_NAMES = {"metropolis", "continent", "population"};

    private Statement stmt;
    private Connection con;

    private List<List<String>> data;

    public MetropolisBrain() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            stmt = con.createStatement();
            data = new ArrayList<>();
            fillDataWithSomeExample();
        } catch (Exception ignore) { }
    }

    private void fillDataWithSomeExample() throws Exception {
        StringBuilder buff = new StringBuilder();
        BufferedReader rd = new BufferedReader(new FileReader(EXAMPLE_DATABASE));
        while (true) {
            String str = rd.readLine();
            if (str == null) {
                break;
            }
            buff.append(str).append("\n");
        }

        rd.close();
        String[] queries = buff.toString().split(";");

        for (String query : queries) {
            if (!query.trim().equals("")) {
                stmt.executeUpdate(query);
            }
        }

        search("", "", -1, true, true);
    }

    @Override
    public int getRowCount() {
        return data.size();
    }

    @Override
    public int getColumnCount() {
        return COL_NAMES.length;
    }

    @Override
    public String getColumnName(int column) {
        return COL_NAMES[column];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        if (columnIndex >= getColumnCount() || rowIndex >= getRowCount()) {
            return null;
        }
        return data.get(rowIndex).get(columnIndex);
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }

    public void add(String metropolis, String continent, int population) {
        if (metropolis.isEmpty() || continent.isEmpty() || population < 0) {
            return;
        }

        try {
            PreparedStatement statement = con.prepareStatement(
                    "INSERT INTO " + DATABASE + " (metropolis, continent, population) VALUES (?, ?, ?);");

            statement.setString(1, metropolis);
            statement.setString(2, continent);
            statement.setInt(3, population);

            statement.executeUpdate();
            statement.close();
        } catch (Exception ignore) { }

        List<String> row = new ArrayList<>();
        row.add(metropolis);
        row.add(continent);
        row.add(Integer.toString(population));

        data.add(row);
        fireTableStructureChanged();
    }

    public void search(String metropolis, String continent, int population, boolean isLess, boolean isPartialMatch) {
        data.clear();
        StringBuilder query = new StringBuilder();
        query.append("SELECT * FROM ").append(DATABASE);

        if (!metropolis.isEmpty() || !continent.isEmpty() || population > 0) {
            query.append(" WHERE");
        }

        if (!metropolis.isEmpty()) {
            if (isPartialMatch) {
                query.append(" ").append(COL_NAMES[0]).append(" LIKE \"%").append(metropolis).append("%\"");
            } else {
                query.append(" ").append(COL_NAMES[0]).append(" = \"").append(metropolis).append("\"");
            }
        }

        if (!continent.isEmpty()) {
            if (!metropolis.isEmpty()) {
                query.append(" AND");
            }

            if (isPartialMatch) {
                query.append(" ").append(COL_NAMES[1]).append(" LIKE \"%").append(continent).append("%\"");
            } else {
                query.append(" ").append(COL_NAMES[1]).append(" = \"").append(continent).append("\"");
            }
        }

        if (population >= 0) {
            if (!metropolis.isEmpty() || !continent.isEmpty()) {
                query.append(" AND");
            }

            if (isLess) {
                query.append(" ").append(COL_NAMES[2]).append(" <= ").append(population);
            } else {
                query.append(" ").append(COL_NAMES[2]).append(" >= ").append(population);
            }
        }

        try {
            query.append(";");
            ResultSet rs = stmt.executeQuery(query.toString());

            while (rs.next()) {
                List<String> row = new ArrayList<>();
                row.add(rs.getString(1));
                row.add(rs.getString(2));
                row.add(Integer.toString(rs.getInt(3)));
                data.add(row);
            }

            fireTableStructureChanged();
        } catch (Exception ignore) { }
    }

}
