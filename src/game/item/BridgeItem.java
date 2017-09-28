package game.item;

import game.entity.Bot;
import game.level.Level;
import game.level.tile.Tile;

public class BridgeItem extends Item {
	public boolean interactOn(Tile tile, Level level, int xt, int yt, Bot bot, int attackDir) {
		if (level.setTile(xt, yt, Tile.bridge, 0)) return true;
		return false;
	}
}
