package game.level;

public class LevelInfo {
	private int width;
	private int height;
	private int islandCount;

	public LevelInfo(int width, int height, int islandCount) {
		this.width = width;
		this.height = height;
		this.islandCount = islandCount;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public int getIslandCount() {
		return islandCount;
	}
}
