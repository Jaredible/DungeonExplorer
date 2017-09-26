package game.entity;

public class AITaskEntry {
	public AIBase action;
	public int priority;
	final AITasks tasks;

	public AITaskEntry(AITasks tasks, int priority, AIBase base) {
		this.tasks = tasks;
		this.priority = priority;
		action = base;
	}
}
