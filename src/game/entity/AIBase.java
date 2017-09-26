package game.entity;

public abstract class AIBase {
	public abstract boolean shouldExecute();

	public boolean continueExecuting() {
		return shouldExecute();
	}

	/**
	 * Determine if this AI Task is interruptible by a higher (= lower value)
	 * priority task.
	 */
	public boolean isInterruptible() {
		return true;
	}

	public void startExecuting() {
	}

	public void resetTask() {
	}

	public void updateTask() {
	}
}
