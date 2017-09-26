package game.level;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Room {
	public static final int MIN_WIDTH = 7;
	public static final int MIN_HEIGHT = 5;
	public static final int WIDTH_RANGE = 2;
	public static final int HEIGHT_RANGE = 2;

	private int x;
	private int y;
	private int width;
	private int height;
	private HashMap<Room, RoomLink> connected = new HashMap<>();
	private List<Room> connectedRooms = new ArrayList<>();
	private boolean joined;

	public Room(int x, int y, int width, int height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}

	public void connect(Room other, int x, int y) {
		RoomLink record = new RoomLink(x, y);

		if (connected.get(other) == null) {
			connected.put(other, record);
			connectedRooms.add(other);
			other.connect(this, x, y);
		}
	}

	public boolean contains(int xp, int yp) {
		return xp >= x && yp >= y && xp < x + width && yp < y + height;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public int getCenterX() {
		return x + width / 2;
	}

	public int getCenterY() {
		return y + height / 2;
	}

	public HashMap<Room, RoomLink> connected() {
		return connected;
	}

	public boolean isJoined() {
		return joined;
	}

	public void setJoined(boolean j) {
		joined = j;
	}
}
