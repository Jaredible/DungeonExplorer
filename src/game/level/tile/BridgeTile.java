package game.level.tile;

import game.entity.Entity;
import game.gfx.Screen;
import game.level.Level;

public class BridgeTile extends Tile {
	public BridgeTile(int id) {
		super(id);
	}

	public void tick(Level level, int x, int y) {
	}

	public void render(Screen screen, Level level, int x, int y) {
		screen.render(x * 16 + 0, y * 16 + 0, 0 + 0 * 32, 0);
		screen.render(x * 16 + 8, y * 16 + 0, 1 + 0 * 32, 0);
		screen.render(x * 16 + 0, y * 16 + 8, 0 + 0 * 32, 0);
		screen.render(x * 16 + 8, y * 16 + 8, 1 + 0 * 32, 0);
	}

	public boolean mayPass(Level level, int x, int y, Entity e) {
		return true;
	}
}
