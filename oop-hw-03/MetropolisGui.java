import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;


public class MetropolisGui extends JFrame {
    private static final JComboBox<String> POPULATION_COMBO_BOX = new JComboBox<>(new String[] { "Population Less Than", "Population Larger Than" });
    private static final JComboBox<String> MATCH_COMBO_BOX = new JComboBox<>(new String[] { "Partial Match", "Exact Match" });

    public MetropolisGui(String title) {
        super(title);
        setLayout(new BorderLayout());

        MetropolisBrain brain = new MetropolisBrain();
        addTable(brain);

        Box northPanel = Box.createHorizontalBox();
        add(northPanel, BorderLayout.NORTH);

        JTextField tfMetropolis = addTextFieldWithLabels(northPanel, "Metropolis: ");
        JTextField tfContinent = addTextFieldWithLabels(northPanel, "Continent: ");
        JTextField tfPopulation = addTextFieldWithLabels(northPanel, "Population: ");
        addButtons(tfMetropolis, tfContinent, tfPopulation, brain);

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        pack();
    }

    private void addTable(MetropolisBrain brain) {
        JTable table = new JTable(brain);
        JScrollPane scroll = new JScrollPane(table);
        scroll.setPreferredSize(new Dimension(300, 500));
        add(scroll, BorderLayout.CENTER);
    }

    private JTextField addTextFieldWithLabels(Box panel, String label) {
        panel.add(new JLabel(label));

        JTextField tf = new JTextField(15);
        panel.add(tf);

        panel.add(Box.createHorizontalStrut(50));
        return tf;
    }

    private void addButtons(JTextField metropolis, JTextField continent, JTextField population, MetropolisBrain brain) {
        Box panel = Box.createVerticalBox();
        add(panel, BorderLayout.EAST);

        panel.add(Box.createVerticalStrut(10));
        addRowButton(panel, metropolis, continent, population, brain);

        panel.add(Box.createVerticalStrut(10));
        addSearchButton(panel, metropolis, continent, population, brain);

        panel.add(Box.createVerticalStrut(30));
        panel.add(getSearchOptions());
    }

    private JPanel getSearchOptions() {
        JPanel searchOptions = new JPanel();
        searchOptions.setMaximumSize(new Dimension(200, 85));
        searchOptions.setLayout(new BoxLayout(searchOptions, BoxLayout.Y_AXIS));

        searchOptions.setBorder(new TitledBorder("Search Options"));
        searchOptions.add(POPULATION_COMBO_BOX);

        searchOptions.add(Box.createVerticalStrut(5));
        searchOptions.add(MATCH_COMBO_BOX);

        return searchOptions;
    }

    private void addSearchButton(Box panel, JTextField metroText, JTextField continentText, JTextField populationText, MetropolisBrain model) {
        JButton searchBtn = new JButton("Search");
        JPanel pnl = new JPanel();

        pnl.setLayout(new BorderLayout());
        pnl.add(searchBtn);
        pnl.setMaximumSize(new Dimension(200, 30));
        panel.add(pnl);

        searchBtn.addActionListener(e -> {
            String population = populationText.getText();
            int pop = -1;
            if (population != null && population.length() > 0) {
                pop = Integer.parseInt(population);
            }
            model.search(metroText.getText(), continentText.getText(), pop,
                    POPULATION_COMBO_BOX.getSelectedIndex() == 0, MATCH_COMBO_BOX.getSelectedIndex() == 0);
        });
    }

    private void addRowButton(Box panel, JTextField metroText, JTextField continentText, JTextField populationText, MetropolisBrain model) {
        JButton rowBtn = new JButton("Add");
        JPanel pnl = new JPanel();

        pnl.setLayout(new BorderLayout());
        pnl.add(rowBtn);
        pnl.setMaximumSize(new Dimension(200, 30));
        panel.add(pnl);

        rowBtn.addActionListener(e -> {
            String population = populationText.getText();
            int pop = -1;
            if (population != null && population.length() > 0) {
                pop = Integer.parseInt(population);
            }
            model.add(metroText.getText(), continentText.getText(), pop);
        });
    }

    public static void main(String[] args) {
        new MetropolisGui("Metropolis Viewer").setVisible(true);
    }
}