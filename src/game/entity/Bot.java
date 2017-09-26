package game.entity;

import game.gfx.Screen;

public class Bot extends Living {
	public void tick() {
		super.tick();
	}

	public void render(Screen screen) {
		int xt = 0;
		int yt = 0;
		int xo = x - 8;
		int yo = y - 13;

		if (dir > 1) xt += walkDist / 2 % 4;

		screen.renderEntity(xo, yo, xt + yt * 32, 1 - (dir + 2));
	}

	protected void touchedBy(Entity entity) {
	}
}
