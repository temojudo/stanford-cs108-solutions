import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.concurrent.Semaphore;

public class WebView extends JFrame {

    private static final int DEFAULT_WORKERS = 4;
    private static final int STAT_COL = 1;

    private static final String LINKS = "links.txt";
    private static final String RUNNING_LABEL = "Running: ";
    private static final String COMPLETED_LABEL = "Completed: ";
    private static final String ELAPSED_LABEL = "Elapsed: ";

    private JButton singleFetchButton;
    private JButton concurrentFetchButton;
    private JButton stopButton;

    private JLabel runningLabel;
    private JLabel completedLabel;
    private JLabel elapsedLabel;

    private DefaultTableModel model;
    private JTextField numThreadsField;
    private JProgressBar progressBar;

    private int numRunningWorkers;
    private int numCompleted;
    private long startTime;

    private final Object workerCountLock;
    private final Object workerCreateLock;
    private Semaphore workerLimitLock;

    private Worker worker;
    private ArrayList<Thread> workers;

    public WebView() {
        this(LINKS);
    }

    public WebView(String filename) {
        workerCountLock = new Object();
        workerCreateLock = new Object();
        numRunningWorkers = 0;
        numCompleted = 0;
        workers = new ArrayList<>();

        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

        initialTable(filename);
        initialStartButtons();
        initialFieldsAndLabels();
        initialProgressBar();
        initialStopButton();

        pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    // alters the numRunningWorkers variable but the passed value
    // also updates the interface
    public void changeWorkerCount(int val) {
        synchronized (workerCountLock) {
            numRunningWorkers += val;
            if (val < 0) numCompleted++;
        }
        SwingUtilities.invokeLater(() -> {
            runningLabel.setText(RUNNING_LABEL + numRunningWorkers);
            completedLabel.setText(COMPLETED_LABEL + numCompleted);
            if (numRunningWorkers == 0) {
                setReadyState();
                elapsedLabel.setText(ELAPSED_LABEL + (System.currentTimeMillis() - startTime) / 1000.0);
            }
        });
    }

    // method invoked by a worker thread when it is finished
    public void sendCompletionNotice() {
        changeWorkerCount(-1);
        workerLimitLock.release();
        SwingUtilities.invokeLater(() -> progressBar.setValue(progressBar.getValue() + 1));
    }

    // method invoked by a worker thread to update a particular
    // row in the table with the passed message
    public void updateRow(final int row, final String msg) {
        SwingUtilities.invokeLater(() -> model.setValueAt(msg, row, STAT_COL));
    }

    public static void main(String[] args) {
        new WebView().setVisible(true);
    }

    private void initialTable(String file) {
        model = new DefaultTableModel(new String[]{"url", "status"}, 0);
        parseURLs(file);

        JTable table = new JTable(model);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        JScrollPane scrollpane = new JScrollPane(table);
        scrollpane.setPreferredSize(new Dimension(600, 300));
        add(scrollpane);
    }

    private void initialStartButtons() {
        singleFetchButton = new JButton("Single Thread Fetch");
        singleFetchButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        singleFetchButton.addActionListener(e -> startWorker(1));

        concurrentFetchButton = new JButton("Concurrent Fetch");
        concurrentFetchButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        concurrentFetchButton.addActionListener(e -> startWorker(Math.max(1, Integer.parseInt(numThreadsField.getText()))));

        add(singleFetchButton);
        add(concurrentFetchButton);
    }

    // sets up the text field, labels
    private void initialFieldsAndLabels() {
        numThreadsField = new JTextField(Integer.toString(DEFAULT_WORKERS));
        numThreadsField.setMaximumSize(new Dimension(100, numThreadsField.getHeight()));
        numThreadsField.setAlignmentX(Component.LEFT_ALIGNMENT);

        runningLabel = new JLabel(RUNNING_LABEL);
        runningLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        completedLabel = new JLabel(COMPLETED_LABEL);
        completedLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        elapsedLabel = new JLabel(ELAPSED_LABEL);
        elapsedLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        add(numThreadsField);
        add(runningLabel);
        add(completedLabel);
        add(elapsedLabel);
    }

    private void initialProgressBar() {
        progressBar = new JProgressBar(0, 10);
        progressBar.setValue(0);
        progressBar.setStringPainted(true);
        add(progressBar);
    }

    private void initialStopButton() {
        stopButton = new JButton("Stop");
        stopButton.setEnabled(false);
        stopButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        stopButton.addActionListener(e -> {
            synchronized (workerCreateLock) {
                if (worker != null) {
                    worker.interrupt();
                    worker = null;
                }

                if (!workers.isEmpty()) {
                    for (Thread worker : workers) {
                        worker.interrupt();
                    }
                }
            }
        });

        add(stopButton);
    }

    private void parseURLs(String file) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            while ((line = reader.readLine()) != null) {
                model.addRow(new String[]{line, ""});
            }
        } catch (Exception ignored) {
        }
    }

    private void startWorker(int numWorkers) {
        resetInterface();
        workerLimitLock = new Semaphore(numWorkers);
        worker = new Worker(this);
        startTime = System.currentTimeMillis();
        worker.start();
        setRunningState();
    }

    private void resetInterface() {
        numCompleted = 0;
        progressBar.setValue(0);
        progressBar.setMaximum(model.getRowCount());
        runningLabel.setText(RUNNING_LABEL);
        completedLabel.setText(COMPLETED_LABEL);
        elapsedLabel.setText(ELAPSED_LABEL);
        for (int i = 0; i < model.getRowCount(); i++) {
            model.setValueAt("", i, STAT_COL);
        }
    }

    private void setReadyState() {
        singleFetchButton.setEnabled(true);
        concurrentFetchButton.setEnabled(true);
        stopButton.setEnabled(false);

        numThreadsField.setEnabled(true);
    }

    private void setRunningState() {
        singleFetchButton.setEnabled(false);
        concurrentFetchButton.setEnabled(false);
        stopButton.setEnabled(true);

        numThreadsField.setEnabled(false);
    }

    private class Worker extends Thread {

        private WebView view;

        public Worker(WebView view) {
            this.view = view;
        }

        @Override
        public void run() {
            changeWorkerCount(1);
            int numURLs = model.getRowCount();
            workers.clear();
            for (int i = 0; i < numURLs; i++) {
                try {
                    workerLimitLock.acquire();
                } catch (InterruptedException e) {
                    break;
                }
                synchronized (workerCreateLock) {
                    Thread worker = new WebWorker((String) model.getValueAt(i, 0), i, view);
                    workers.add(worker);
                    worker.start();
                }
            }
            changeWorkerCount(-1);
        }
    }
}
