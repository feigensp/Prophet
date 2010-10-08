package test;

import javax.swing.JLabel;

public class Watch implements Runnable {

	private long time;
	private JLabel display;
	boolean isRunning;
	private long clock;
	private Thread t;

	public Watch(JLabel display) {
		this.display = display;
		t = new Thread(this);
	}

	public Watch() {
		t = new Thread(this);
	}

	public void start() {
		time = System.currentTimeMillis();
		isRunning = true;
		clock = 0;
		t.start();
	}

	public void pause() {
		isRunning = false;
	}

	public void resume() {
		synchronized (this) {
			isRunning = true;
			notify();
		}
		time = System.currentTimeMillis() - (clock * 1000);
	}

	public void stop() {
		isRunning = false;
		time = 0;
		clock = 0;
	}

	public long getClock() {
		return clock;
	}

	public boolean isActive() {
		return isRunning;
	}

	public void run() {
		while (true) {
			try {
				Thread.sleep(1000);
				synchronized (this) {
					if (!isRunning) {
						wait();
					}
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			clock = (System.currentTimeMillis() - time) / 1000;
			if(display != null) {
				//Zeiten ins display schreiben
				if (clock / 60 < 10) {
					if (clock < 10) {
						display.setText("Zeit: 0" + (clock / 60) + ":0" + clock
								+ " (5 min)");
					} else {
						display.setText("Zeit: 0" + (clock / 60) + ":" + clock
								+ " (5 min)");
					}
				} else {
					if (clock < 10) {
						display.setText("Zeit: " + (clock / 60) + ":0" + clock
								+ " (5 min)");
					} else {
						display.setText("Zeit: " + (clock / 60) + ":" + clock
								+ " (5 min)");
					}
				}
			}
		}
	}

}
