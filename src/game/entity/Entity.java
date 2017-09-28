package game.entity;

import java.util.List;
import java.util.Random;

import game.gfx.Screen;
import game.item.Item;
import game.level.Level;
import game.level.tile.Tile;

public class Entity {
	protected final Random random = new Random();
	public int x, y;
	public Level level;
	public boolean removed;
	public int xr = 6;
	public int yr = 6;
	public boolean noClip = false;

	public final void init(Level level) {
		this.level = level;
	}

	public void tick() {
	}

	public void render(Screen screen) {
	}

	public void remove() {
		removed = true;
	}

	public boolean intersects(int minX, int minY, int maxX, int maxY) {
		return !(x + xr < minX || y + yr < minY || x - xr > maxX || y - yr > maxY);
	}

	public boolean blocks(Entity e) {
		return false;
	}

	public void hurt(Tile tile, int x, int y, int dmg) {
	}

	public void hurt(Living living, int dmg, int attackDir) {
	}

	public boolean move(int xa, int ya) {
		if (xa != 0 || ya != 0) {
			boolean stopped = true;
			if (xa != 0 && move2(xa, 0)) stopped = false;
			if (ya != 0 && move2(0, ya)) stopped = false;
			if (!stopped && !noClip) {
				int xt = x >> 4;
				int yt = y >> 4;
				level.getTile(xt, yt).steppedOn(level, xt, yt, this);
			}
			return !stopped;
		}
		return true;
	}

	protected boolean move2(int xa, int ya) {
		if (!noClip) {
			if (xa != 0 && ya != 0) throw new IllegalArgumentException("Move2 can only move along one axis at a time!");

			int xto0 = (x - xr) >> 4;
			int yto0 = (y - yr) >> 4;
			int xto1 = (x + xr) >> 4;
			int yto1 = (y + yr) >> 4;

			int xt0 = (x + xa - xr) >> 4;
			int yt0 = (y + ya - yr) >> 4;
			int xt1 = (x + xa + xr) >> 4;
			int yt1 = (y + ya + yr) >> 4;
			boolean blocked = false;
			for (int yt = yt0; yt <= yt1; yt++)
				for (int xt = xt0; xt <= xt1; xt++) {
					if (xt >= xto0 && xt <= xto1 && yt >= yto0 && yt <= yto1) continue;
					level.getTile(xt, yt).bumpedInto(level, xt, yt, this);
					if (!level.getTile(xt, yt).mayPass(level, xt, yt, this)) {
						blocked = true;
						return false;
					}
				}
			if (blocked) return false;

			List<Entity> wasInside = level.getEntities(x - xr, y - yr, x + xr, y + yr);
			List<Entity> isInside = level.getEntities(x + xa - xr, y + ya - yr, x + xa + xr, y + ya + yr);
			int i;
			Entity e;
			for (i = 0; i < isInside.size(); i++) {
				e = isInside.get(i);
				if (e == this) continue;

				e.touchedBy(this);
			}
			isInside.removeAll(wasInside);
			for (i = 0; i < isInside.size(); i++) {
				e = isInside.get(i);
				if (e == this) continue;

				if (e.blocks(this)) return false;
			}
		}

		x += xa;
		y += ya;
		return true;
	}

	protected void touchedBy(Entity entity) {
	}

	public boolean isBlockableBy(Living living) {
		return true;
	}

	public boolean interact(Bot bot, Item item, int attackDir) {
		return item.interact(bot, this, attackDir);
	}

	public boolean use(Bot bot, int attackDir) {
		return false;
	}

	public void setPosition(int x, int y) {
		this.x = x;
		this.y = y;
	}
}
