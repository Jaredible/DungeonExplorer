package game.level.tile;

import game.entity.Bot;
import game.entity.Entity;
import game.gfx.Screen;
import game.item.Item;
import game.level.Level;

public class Tile {
	public static int tickCount = 0;

	public static Tile[] tiles = new Tile[256];
	public static Tile air = new AirTile(0);
	public static Tile ground = new GroundTile(1);
	public static Tile bridge = new BridgeTile(2);

	public final byte id;

	public Tile(int id) {
		this.id = (byte) id;
		if (tiles[id] != null) throw new RuntimeException("Duplicate tile ids!");
		tiles[id] = this;
	}

	public void tick(Level level, int x, int y) {
	}

	public void render(Screen screen, Level level, int x, int y) {
	}

	public boolean connectsTo(Tile t) {
		return t == this;
	}

	public boolean mayPass(Level level, int x, int y, Entity e) {
		return true;
	}

	public void steppedOn(Level level, int xt, int yt, Entity entity) {
	}

	public boolean interact(Level level, int xt, int yt, Bot bot, Item item, int attackDir) {
		return false;
	}

	public void bumpedInto(Level level, int xt, int yt, Entity entity) {
	}

	public boolean use(Level level, int xt, int yt, Bot bot, int attackDir) {
		return false;
	}
}
