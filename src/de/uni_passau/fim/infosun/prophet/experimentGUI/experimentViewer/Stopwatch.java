package de.uni_passau.fim.infosun.prophet.experimentGUI.experimentViewer;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.util.concurrent.TimeUnit;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.WindowConstants;

import de.uni_passau.fim.infosun.prophet.experimentGUI.util.qTree.QTreeNode;

/**
 * A <code>JLabel</code> displaying 'Caption MM:SS', the elapsed time since the <code>Stopwatch</code> was started.
 * The <code>Stopwatch</code> will save that time in seconds in the <code>QTreeNode</code> given in the
 * constructor when it is paused or stopped.
 */
public class Stopwatch extends JLabel {

    private static final String noCaptionFormat = "%02d:%02d";
    private static final String captionFormat = "%s " + noCaptionFormat;

    private final QTreeNode node;
    private final String caption;
    private final Timer timer;

    // nanosecond timestamps
    private long startTime;
    private long pauseTime;
    private long lastUpdateTime;

    /**
     * Constructs a new <code>Stopwatch</code> for the given <code>QTreeNode</code>.
     * The given <code>caption</code> will be prepended to the time the
     * <code>Stopwatch</code> displays.
     *
     * @param node the <code>QTreeNode</code> this <code>Stopwatch</code> saves its time in
     * @param caption the caption to be prepended to the displayed time or <code>null</code> for no caption
     */
    public Stopwatch(QTreeNode node, String caption) {
        this.node = node;
        this.caption = caption;
        this.timer = new Timer(200, this::update);
        this.timer.setInitialDelay(0);
        this.startTime = 0;
        updateText();
    }

    /**
     * Constructs a new <code>Stopwatch</code> with no caption for the given <code>QTreeNode</code>.
     *
     * @param node the <code>QTreeNode</code> this <code>Stopwatch</code> saves its time in
     */
    public Stopwatch(QTreeNode node) {
        this(node, null);
    }

    /**
     * Called every time the <code>timer</code> fires.
     *
     * @param event the <code>ActionEvent</code> produced by the <code>timer</code>
     */
    private void update(ActionEvent event) {
        lastUpdateTime = System.nanoTime();
        updateText();
    }

    /**
     * Updates the text of this <code>JLabel</code> to show the current elapsed time.
     */
    private void updateText() {

        long elapsedTime;
        if (startTime == 0) {
            elapsedTime = 0;
        } else {
            elapsedTime = System.nanoTime() - startTime;
        }

        long minutes = TimeUnit.NANOSECONDS.toMinutes(elapsedTime);
        long remainingSeconds = TimeUnit.NANOSECONDS.toSeconds(elapsedTime - TimeUnit.MINUTES.toNanos(minutes));

        Object[] formatArgs;
        String formatString;
        if (caption != null) {
            formatString = captionFormat;
            formatArgs = new Object[] {caption, minutes, remainingSeconds};
        } else {
            formatString = noCaptionFormat;
            formatArgs = new Object[] {minutes, remainingSeconds};
        }

        setText(String.format(formatString, formatArgs));
    }

    /**
     * Saves the current elapsed time to the <code>node</code> if there is one.
     *
     * @param now the current elapsed time in nanoseconds
     */
    private void saveTime(long now) {
        if (node != null) {
            node.setAnswerTime(TimeUnit.NANOSECONDS.toSeconds(now - startTime));
        }
    }

    /**
     * Starts (or unpauses) the <code>Stopwatch</code>.
     */
    public void start() {
        if (timer.isRunning()) {
            return;
        }

        long now = System.nanoTime();
        if (startTime == 0) {
            startTime = now;
        } else {
            startTime += now - pauseTime;
        }

        timer.start();
    }

    /**
     * Stops the <code>Stopwatch</code>.
     */
    public void stop() {
        if (!timer.isRunning()) {
            return;
        }

        saveTime(System.nanoTime());
        timer.stop();
        timer.setInitialDelay(0);

        startTime = 0;
        pauseTime = 0;
        lastUpdateTime = 0;
    }

    /**
     * Pauses the <code>Stopwatch</code>.
     */
    public void pause() {
        if (!timer.isRunning()) {
            return;
        }

        long now = System.nanoTime();

        saveTime(now);
        timer.stop();
        timer.setInitialDelay((int) TimeUnit.NANOSECONDS.toMillis(now - lastUpdateTime));

        pauseTime = now;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {

            JFrame frame = new JFrame();
            JPanel buttonPanel = new JPanel();
            Stopwatch watch = new Stopwatch(null, "Aktuelle Zeit:");

            frame.setLayout(new BorderLayout());
            frame.add(watch, BorderLayout.CENTER);

            JButton pauseBtn = new JButton("Pause");
            JButton startBtn = new JButton("Start");
            JButton stopBtn = new JButton("Stop");

            buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
            buttonPanel.add(startBtn);
            buttonPanel.add(pauseBtn);
            buttonPanel.add(stopBtn);

            pauseBtn.addActionListener(event -> {
                watch.pause();
            });

            startBtn.addActionListener(event -> {
                watch.start();
            });

            stopBtn.addActionListener(event -> {
                watch.stop();
            });

            frame.add(buttonPanel, BorderLayout.SOUTH);
            frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            frame.pack();
            frame.setVisible(true);
        });
    }
}
