package game.entity;

import game.gfx.Screen;

public class Pushable extends Entity {
	private int pushTime = 0;
	private int pushDir = -1;

	public Pushable(int x, int y) {
		this.x = x;
		this.y = y;

		xr = 3;
		yr = 3;
	}

	public void tick() {
		if (pushDir == 0) move(0, 1);
		if (pushDir == 1) move(0, -1);
		if (pushDir == 2) move(-1, 0);
		if (pushDir == 3) move(1, 0);
		pushDir = -1;
		if (pushTime > 0) pushTime--;
	}

	public void render(Screen screen) {
		int xo = x - 8;
		int yo = y - 12;
		screen.render(xo, yo, 1, 0);
		screen.render(xo + 8, yo, 2, 0);
		screen.render(xo, yo + 8, 2, 0);
		screen.render(xo + 8, yo + 8, 1, 0);
	}

	public boolean blocks(Entity e) {
		return true;
	}

	protected void touchedBy(Entity entity) {
		if (entity instanceof Bot && pushTime == 0) {
			pushDir = ((Bot) entity).dir;
			pushTime = 10;
		}
	}
}
