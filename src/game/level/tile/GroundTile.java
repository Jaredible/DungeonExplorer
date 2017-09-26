package game.level.tile;

import game.entity.Entity;
import game.gfx.Screen;
import game.level.Level;

public class GroundTile extends Tile {
	public GroundTile(int id) {
		super(id);
	}

	public void tick(Level level, int x, int y) {
	}

	public void render(Screen screen, Level level, int x, int y) {
		boolean u = !level.getTile(x, y - 1).connectsTo(this);
		boolean d = !level.getTile(x, y + 1).connectsTo(this);
		boolean l = !level.getTile(x - 1, y).connectsTo(this);
		boolean r = !level.getTile(x + 1, y).connectsTo(this);

		boolean ul = !level.getTile(x - 1, y - 1).connectsTo(this);
		boolean dl = !level.getTile(x - 1, y + 1).connectsTo(this);
		boolean ur = !level.getTile(x + 1, y - 1).connectsTo(this);
		boolean dr = !level.getTile(x + 1, y + 1).connectsTo(this);

		int n = level.getData(x, y) == 1 ? 1 : 0;

		if (!u && !l) {
			if (!ul) screen.render(x * 16 + 0, y * 16 + 0, n + 4 * 32, 0);
			else screen.render(x * 16 + 0, y * 16 + 0, 6 + 8 * 32, 0);
		} else screen.render(x * 16 + 0, y * 16 + 0, (l ? 0 : 2) + (u ? 8 : d ? 12 : 10) * 32, 0);

		if (!u && !r) {
			if (!ur) screen.render(x * 16 + 8, y * 16 + 0, n + 4 * 32, 0);
			else screen.render(x * 16 + 8, y * 16 + 0, 7 + 8 * 32, 0);
		} else screen.render(x * 16 + 8, y * 16 + 0, (r ? 5 : 3) + (u ? 8 : d ? 12 : 10) * 32, 0);

		if (!d && !l) {
			if (!dl) screen.render(x * 16 + 0, y * 16 + 8, n + 4 * 32, 0);
			else screen.render(x * 16 + 0, y * 16 + 8, 6 + 13 * 32, 0);
		} else screen.render(x * 16 + 0, y * 16 + 8, (l ? 0 : 2) + (d ? 13 : u ? 9 : 11) * 32, 0);

		if (!d && !r) {
			if (!dr) screen.render(x * 16 + 8, y * 16 + 8, n + 4 * 32, 0);
			else screen.render(x * 16 + 8, y * 16 + 8, 7 + 13 * 32, 0);
		} else screen.render(x * 16 + 8, y * 16 + 8, (r ? 5 : 3) + (d ? 13 : u ? 9 : 11) * 32, 0);
	}

	public boolean mayPass(Level level, int x, int y, Entity e) {
		return true;
	}
}
