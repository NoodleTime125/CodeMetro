package codemetro.visualizer;

import java.util.List;
import java.util.TimerTask;


public class VariableTimerTask extends TimerTask {

	int indexWayPoint;
	int indexLocation;
	List<Train> trainManager;
	
	public VariableTimerTask (List<Train> trainmanager) {
		this.trainManager = trainmanager;
	}
	
	@Override
	public void run() {
		
	}

	
}
