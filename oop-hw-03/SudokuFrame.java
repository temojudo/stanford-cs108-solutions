import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.*;
import java.awt.*;

public class SudokuFrame extends JFrame {

	JTextArea source;
	JTextArea solution;
	JButton checkButton;
	JCheckBox autoCheck;

	public SudokuFrame() {
		super("Sudoku Solver");
		
		// YOUR CODE HERE
		setLayout(new BorderLayout(5, 5));
		addTextAreas();
		addOptions();

		// Could do this:
		setLocationByPlatform(true);
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		pack();
	}

	private void addTextAreas() {
		source = new JTextArea(15, 20);
		source.setBorder(new TitledBorder("Puzzle"));

		source.getDocument().addDocumentListener(new DocumentListener() {
			public void changedUpdate(DocumentEvent e) {
				if(autoCheck.isSelected()) {
					solve();
				}
			}

			public void insertUpdate(DocumentEvent e) {
				if(autoCheck.isSelected()) {
					solve();
				}
			}

			public void removeUpdate(DocumentEvent e) {
				if(autoCheck.isSelected()) {
					solve();
				}
			}
		});

		solution = new JTextArea(15, 20);
		solution.setBorder(new TitledBorder("Solution"));
		solution.setEditable(false);

		add(source, BorderLayout.WEST);
		add(solution, BorderLayout.EAST);
	}

	// adds the check button and auto check box to the frame
	private void addOptions() {
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));

		checkButton = new JButton("Check");
		checkButton.addActionListener(e -> solve());

		autoCheck = new JCheckBox("Auto Check");
		autoCheck.setSelected(true);
		autoCheck.addActionListener(e -> {
			if(((JCheckBox) e.getSource()).isSelected()) {
				solve();
			}
		});

		panel.add(checkButton);
		panel.add(autoCheck);
		add(panel, BorderLayout.SOUTH);
	}

	private void solve() {
		try {
			Sudoku sudoku = new Sudoku(Sudoku.textToGrid(source.getText()));
			int numSolutions = sudoku.solve();
			if(numSolutions > 0) {
				solution.setText(sudoku.getSolutionText());
				solution.append("solutions: " + numSolutions + "\n");
				solution.append("elapsed: " + sudoku.getElapsed() + "ms\n");
			} else {
				solution.setText("No solutions found\n");
			}
		} catch (RuntimeException e) {
			solution.setText("Parsing error\n");
		}
	}

	public static void main(String[] args) {
		// GUI Look And Feel
		// Do this incantation at the start of main() to tell Swing
		// to use the GUI LookAndFeel of the native platform. It's ok
		// to ignore the exception.
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception ignored) { }

		SudokuFrame frame = new SudokuFrame();
		frame.setVisible(true);
	}

}
