package game.item.resource;

import game.entity.Player;
import game.level.Level;
import game.level.tile.Tile;

public class Resource {
	public static Resource copper = new Resource("Copper");
	public static Resource nickel = new Resource("Nickel");
	public static Resource chromium = new Resource("Chromium");
	public static Resource aluminium = new Resource("Aluminium");
	public static Resource lead = new Resource("Lead");
	public static Resource silver = new Resource("Silver");
	public static Resource tin = new Resource("Tin");
	public static Resource silicon = new Resource("Silicon");
	public static Resource gold = new Resource("Gold");
	public static Resource sand = new Resource("Sand");
	public static Resource glass = new Resource("Glass");

	public final String name;

	public Resource(String name) {
		this.name = name;
	}

	public boolean interactOn(Tile tile, Level level, int xt, int yt, Player player, int attackDir) {
		return false;
	}
}
