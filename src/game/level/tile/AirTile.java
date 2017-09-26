package game.level.tile;

import game.entity.Entity;
import game.gfx.Screen;
import game.level.Level;

public class AirTile extends Tile {
	public AirTile(int id) {
		super(id);
	}

	public void tick(Level level, int x, int y) {
	}

	public void render(Screen screen, Level level, int x, int y) {
		if (!level.getTile(x, y - 1).connectsTo(this)) {
			screen.render(x * 16 + 0, y * 16 + 0, 0 + 2 * 32, 0);
			screen.render(x * 16 + 8, y * 16 + 0, 1 + 2 * 32, 0);
			screen.render(x * 16 + 0, y * 16 + 8, 0 + 3 * 32, 0);
			screen.render(x * 16 + 8, y * 16 + 8, 1 + 3 * 32, 0);
		}
	}

	public boolean mayPass(Level level, int x, int y, Entity e) {
		return false;
	}
}
