// JCount.java

/*
 Basic GUI/Threading exercise.
*/

import javax.swing.*;

public class JCount extends JPanel {
    public static final long DEFAULT = 1000000011;

    private JTextField bound;
    private JLabel display;
    private Worker worker;

    public JCount() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        bound = new JTextField(Long.toString(DEFAULT));
        display = new JLabel(Long.toString(0));

        JButton start = new JButton("Start");
        JButton stop = new JButton("Stop");

        add(bound);
        add(display);
        add(start);
        add(stop);

        addListeners(start, stop);
        add(Box.createVerticalStrut(40));
    }

    private void addListeners(JButton start, JButton stop) {
        start.addActionListener(e -> {
            if (worker != null) {
                worker.interrupt();
            }
            worker = new Worker();
            worker.start();
        });

        stop.addActionListener(e -> {
            if (worker != null) {
                worker.interrupt();
                worker = null;
            }
        });
    }

    static public void main(String[] args) {
        JFrame frame = new JFrame("The Count");
        frame.setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS));

        frame.add(new JCount());
        frame.add(new JCount());
        frame.add(new JCount());
        frame.add(new JCount());

        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    private class Worker extends Thread {

        @Override
        public void run() {
            final long limit = Long.parseLong(bound.getText());
            for (long i = 0; i <= limit; i++) {
                if (isInterrupted()) {
                    final long val = i;
                    SwingUtilities.invokeLater(() -> display.setText(Long.toString(val)));
                    break;
                }

                if (i % 10000 == 0) {
                    final long val = i;
                    SwingUtilities.invokeLater(() -> display.setText(Long.toString(val)));
                }

                if (i == limit) {
                    SwingUtilities.invokeLater(() -> display.setText(Long.toString(limit)));
                }
            }
        }
    }
}
