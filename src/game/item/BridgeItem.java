package game.item;

import game.entity.Player;
import game.level.Level;
import game.level.tile.Tile;

public class BridgeItem extends Item {
	public boolean interactOn(Tile tile, Level level, int xt, int yt, Player player, int attackDir) {
		if (level.setTile(xt, yt, Tile.bridge, 0)) return true;
		return false;
	}
}
