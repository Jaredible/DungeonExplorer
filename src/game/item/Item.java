package game.item;

import game.entity.Entity;
import game.entity.Player;
import game.gfx.Screen;
import game.level.Level;
import game.level.tile.Tile;

public class Item {
	public boolean interact(Player player, Entity entity, int attackDir) {
		return false;
	}

	public boolean interactOn(Tile tile, Level level, int xt, int yt, Player player, int attackDir) {
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
