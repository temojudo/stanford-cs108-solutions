import javax.swing.*;
import java.awt.*;

public class JBrainTetris extends JTetris {

    private JCheckBox brainMode, animateFall;
    private JSlider adversary;
    private JLabel adversaryOk;
    private Brain brain;
    private Brain.Move brainMove;
    private boolean brainMoveComputed;

    /**
     * Creates a new JTetris where each tetris square
     * is drawn with the given number of pixels.
     *
     * @param pixels
     */
    JBrainTetris(int pixels) {
        super(pixels);
        brain = new DefaultBrain();
    }

    private void updateCounters() {
        countLabel.setText("Pieces " + count);
        scoreLabel.setText("Score " + score);
    }

    @Override
    public void tick(int verb) {
        if (!gameOn) { return; }
        if (currentPiece != null) { board.undo(); }
        if (brainMode.isSelected()) { completeBrainMove(); }

        computeNewPosition(verb);
        int result = setCurrent(newPiece, newX, newY);
        if (result ==  Board.PLACE_ROW_FILLED) { repaint(); }

        boolean failed = (result >= Board.PLACE_OUT_BOUNDS);
        if (failed) {
            if (currentPiece != null) { board.place(currentPiece, currentX, currentY); }
            repaintPiece(currentPiece, currentX, currentY);
        }

        if (failed && verb == DOWN && !moved) {
            brainMoveComputed = false;
            int cleared = board.clearRows();
            if (cleared > 0) {
                switch (cleared) {
                    case 1: score += 5;	 break;
                    case 2: score += 10;  break;
                    case 3: score += 20;  break;
                    case 4: score += 40; Toolkit.getDefaultToolkit().beep(); break;
                    default: score += 50;
                }
                updateCounters();
                repaint();
            }

            if (board.getMaxHeight() > board.getHeight() - TOP_SPACE) { stopGame(); }
                                                                 else { addNewPiece(); }
        }

        moved = (!failed && verb != DOWN);
    }

    private void completeBrainMove() {
        if (!brainMoveComputed) {
            brainMoveComputed = true;
            brainMove = brain.bestMove(board, currentPiece, board.getHeight() - currentPiece.getHeight(), null);
        }

        board.undo();
        if (brainMove == null) { stopGame(); return; }
        if (!animateFall.isSelected()) {
            currentX = brainMove.x;
            currentY = brainMove.y;
            currentPiece = brainMove.piece;
            return;
        }
        if (currentPiece != (brainMove.piece)) { currentPiece = currentPiece.fastRotation(); return; }
        if (currentX < brainMove.x) { currentX++; return; }
        if (currentX > brainMove.x) { currentX--; return; }
    }

    @Override
    public JComponent createControlPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        addTopLabels(panel);
        addStartButton(panel);
        addStopButton(panel);
        addSpeedSlider(panel);
        addBrainCheckBoxes(panel);
        addAdversarySlider(panel);

        testButton = new JCheckBox("Test sequence");
        panel.add(testButton);
        return panel;
    }

    private void addTopLabels(JPanel panel) {
        // COUNT
        countLabel = new JLabel("0");
        panel.add(countLabel);

        // SCORE
        scoreLabel = new JLabel("0");
        panel.add(scoreLabel);

        // TIME
        timeLabel = new JLabel(" ");
        panel.add(timeLabel);
        panel.add(Box.createVerticalStrut(12));
    }

    private void addStopButton(JPanel panel) {
        stopButton = new JButton("Stop");
        stopButton.setEnabled(false);
        panel.add(stopButton);
        stopButton.addActionListener(e -> stopGame());
    }

    private void addStartButton(JPanel panel) {
        startButton = new JButton("Start");
        panel.add(startButton);
        startButton.addActionListener(e -> startGame());
    }

    private void addAdversarySlider(JPanel panel) {
        JPanel row = new JPanel();
        panel.add(Box.createVerticalStrut(12));
        adversaryOk = new JLabel("chance");
        panel.add(adversaryOk);

        row.add(new JLabel("Adversary:"));
        adversary = new JSlider(0, 100, 0);
        adversary.setPreferredSize(new Dimension(100, 15));
        row.add(adversary);
        panel.add(row);
    }

    private void addBrainCheckBoxes(JPanel panel) {
        panel.add(new JLabel("Brain:"));
        brainMode = new JCheckBox("Brain active");
        animateFall = new JCheckBox("Animate Fall");
        panel.add(brainMode);
        panel.add(animateFall);
    }

    private void addSpeedSlider(JPanel panel) {
        JPanel row = new JPanel();
        panel.add(Box.createVerticalStrut(12));
        row.add(new JLabel("Speed:"));
        speed = new JSlider(0, 200, 75);
        speed.setPreferredSize(new Dimension(100, 15));
        updateTimer();
        row.add(speed);
        panel.add(row);
        speed.addChangeListener(e -> updateTimer());
    }

    @Override
    public Piece pickNextPiece() {
        int rand = random.nextInt(100);
        if (rand >= adversary.getValue()) {
            adversaryOk.setText("ok");
            return super.pickNextPiece();
        }

        double maxScore = 0;
        int pieceIndex = 0;
        for (int i = 0; i < pieces.length; i++) {
            double score = brain.bestMove(board, pieces[i], board.getHeight(), null).score;
            if(maxScore < score) { maxScore = score; pieceIndex = i; }
        }

        adversaryOk.setText("*ok*");
        return pieces[pieceIndex];
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) { }

        JTetris tetris = new JBrainTetris(16);
        JFrame frame = JBrainTetris.createFrame(tetris);
        frame.setVisible(true);
    }

}
