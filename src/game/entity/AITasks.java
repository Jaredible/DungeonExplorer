package game.entity;

import java.util.ArrayList;
import java.util.List;

public class AITasks {
	private List<AITaskEntry> taskEntries = new ArrayList();
	private List<AITaskEntry> executingTaskEntries = new ArrayList();

	public void addTask(int priority, AIBase base) {
		taskEntries.add(new AITaskEntry(this, priority, base));
	}

	public void removeTask(AIBase base) {
		for (AITaskEntry task : taskEntries) {
			AIBase b = task.action;
			if (b == base) {
				if (executingTaskEntries.contains(task)) {
					b.resetTask();
					executingTaskEntries.remove(task);
				}
			}
			taskEntries.remove(task);
		}
	}

	public void updateTasks() {
	}
}
