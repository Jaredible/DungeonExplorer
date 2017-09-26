package game.entity;

import game.level.Level;
import game.level.tile.Tile;

public class Living extends Entity {
	public int maxHealth = 10;
	public int health = maxHealth;
	protected int xKnockback, yKnockback;
	protected int walkDist = 0;
	protected int dir = 0;
	public int hurtTime = 0;
	public int tickTime = 0;

	public Living() {
		xr = 4;
		yr = 3;
	}

	public void tick() {
		tickTime++;

		if (health <= 0) die();
		if (hurtTime > 0) hurtTime--;
	}

	protected void die() {
		remove();
	}

	public void hurt(Tile tile, int x, int y, int dmg) {
		int attackDir = dir ^ 1;
		doHurt(dmg, attackDir);
	}

	public void hurt(Living living, int dmg, int attackDir) {
		doHurt(dmg, attackDir);
	}

	protected void doHurt(int dmg, int attackDir) {
		if (hurtTime > 0) return;

		health -= dmg;
		if (attackDir == 0) yKnockback = 6;
		if (attackDir == 1) yKnockback = -6;
		if (attackDir == 2) xKnockback = -6;
		if (attackDir == 3) xKnockback = 6;
		hurtTime = 10;
	}

	public boolean move(int xa, int ya) {
		if (xKnockback < 0) {
			move2(-1, 0);
			xKnockback++;
		}
		if (xKnockback > 0) {
			move2(1, 0);
			xKnockback--;
		}
		if (yKnockback < 0) {
			move2(0, -1);
			yKnockback++;
		}
		if (yKnockback < 0) {
			move2(0, 1);
			yKnockback--;
		}
		if (hurtTime > 0) return true;
		if (xa != 0 || ya != 0) {
			walkDist++;
			if (xa < 0) dir = 2;
			if (xa > 0) dir = 3;
			if (ya < 0) dir = 1;
			if (ya > 0) dir = 0;
		} else walkDist = 0;
		return super.move(xa, ya);
	}

	public boolean blocks(Entity e) {
		return e.isBlockableBy(this);
	}

	public boolean findStartPos(Level level) {
		setLocationAndDirection(level.startX * 16 + 8, level.startY * 16 + 8, 2);
		return true;
	}

	public void setLocationAndDirection(int x, int y, int dir) {
		setPosition(x, y);
		this.dir = dir;
	}
}
