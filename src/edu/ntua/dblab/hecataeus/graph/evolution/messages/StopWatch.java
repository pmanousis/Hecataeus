package edu.ntua.dblab.hecataeus.graph.evolution.messages;

public class StopWatch {
	long timeUptoNow;
	long tillNow;

	public StopWatch() {
		timeUptoNow = 0;
	}

	public void start() {
		tillNow = System.nanoTime();
	}

	public void stop() {
		timeUptoNow += System.nanoTime() - tillNow;
	}

	@Override
	public String toString() {
		return String.valueOf(timeUptoNow);
	}

}
