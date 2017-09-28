package game.item;

import game.entity.Bot;
import game.entity.Entity;
import game.gfx.Screen;
import game.level.Level;
import game.level.tile.Tile;

public class Item {
	public boolean interact(Bot bot, Entity entity, int attackDir) {
		return false;
	}

	public boolean interactOn(Tile tile, Level level, int xt, int yt, Bot bot, int attackDir) {
		return false;
	}

	public void renderIcon(Screen screen, int x, int y) {
	}

	public boolean isDepleted() {
		return false;
	}

	public boolean canAttack() {
		return false;
	}
}
