package de.uni_passau.fim.infosun.prophet.experimentViewer;

import java.awt.event.ActionEvent;
import java.util.concurrent.TimeUnit;
import javax.swing.JLabel;
import javax.swing.Timer;

import de.uni_passau.fim.infosun.prophet.util.qTree.QTreeNode;

/**
 * A <code>JLabel</code> displaying 'Caption MM:SS', the elapsed time since the <code>StopwatchLabel</code> was started.
 * The <code>StopwatchLabel</code> will save that time in seconds in the <code>QTreeNode</code> given in the
 * constructor when it is paused or stopped.
 */
public class StopwatchLabel extends JLabel {

    private static final String noCaptionFormat = "%02d:%02d";
    private static final String captionFormat = "%s " + noCaptionFormat;

    private final QTreeNode node;
    private final String caption;
    private final Timer timer;

    // millisecond timestamps
    private long startTime;
    private long pauseTime;
    private long lastUpdateTime;

    /**
     * Constructs a new <code>StopwatchLabel</code> for the given <code>QTreeNode</code>.
     * The given <code>caption</code> will be prepended to the time the
     * <code>StopwatchLabel</code> displays.
     *
     * @param node the <code>QTreeNode</code> this <code>StopwatchLabel</code> saves its time in or <code>null</code> if
     *             the time should not be saved
     * @param caption the caption to be prepended to the displayed time or <code>null</code> for no caption
     */
    public StopwatchLabel(QTreeNode node, String caption) {
        this.node = node;
        this.caption = caption;
        this.timer = new Timer(200, this::update);
        this.timer.setInitialDelay(0);
        this.startTime = 0;
        updateText();
    }

    /**
     * Called every time the <code>timer</code> fires.
     *
     * @param event the <code>ActionEvent</code> produced by the <code>timer</code>
     */
    private void update(ActionEvent event) {
        lastUpdateTime = System.currentTimeMillis();
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
            elapsedTime = System.currentTimeMillis() - startTime;
        }

        long minutes = TimeUnit.MILLISECONDS.toMinutes(elapsedTime);
        long remainingSeconds = TimeUnit.MILLISECONDS.toSeconds(elapsedTime - TimeUnit.MINUTES.toMillis(minutes));

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
     * @param now the current elapsed time in milliseconds
     */
    private void saveTime(long now) {
        if (node != null) {
            node.setAnswerTime(TimeUnit.MILLISECONDS.toSeconds(now - startTime));
        }
    }

    /**
     * Starts (or unpauses) the <code>StopwatchLabel</code>.
     */
    public void start() {
        if (timer.isRunning()) {
            return;
        }

        long now = System.currentTimeMillis();
        if (startTime == 0) {
            startTime = now;
        } else {
            startTime += now - pauseTime;
        }

        timer.start();
    }

    /**
     * Stops the <code>StopwatchLabel</code>.
     */
    public void stop() {
        if (!timer.isRunning()) {
            return;
        }

        saveTime(System.currentTimeMillis());
        timer.stop();
        timer.setInitialDelay(0);

        startTime = 0;
        pauseTime = 0;
        lastUpdateTime = 0;
    }

    /**
     * Pauses the <code>StopwatchLabel</code>.
     */
    public void pause() {
        if (!timer.isRunning()) {
            return;
        }

        long now = System.currentTimeMillis();

        saveTime(now);
        timer.stop();
        timer.setInitialDelay((int) (now - lastUpdateTime));

        pauseTime = now;
    }
}
