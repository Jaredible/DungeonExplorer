package game.entity;

import java.util.List;

import game.Game;
import game.InputHandler;
import game.gfx.Screen;
import game.item.BridgeItem;
import game.item.Item;

public class Bot extends Living {
	private Game game;
	private InputHandler input;
	private int attackDir;
	public Item attackItem;
	public Item activeItem = new BridgeItem();

	public Bot(Game game, InputHandler input) {
		this.game = game;
		this.input = input;
	}

	public void tick() {
		super.tick();

		if (game.player == this) {
			int xa = 0;
			int ya = 0;
			if (input.w.down) ya--;
			if (input.s.down) ya++;
			if (input.a.down) xa--;
			if (input.d.down) xa++;
			game.player.move(xa, ya);

			if (input.space.clicked) game.player.attack();

			if (input.c.clicked) game.player.noClip = !game.player.noClip;

			if (input.x.clicked) {
				if (!game.player.use()) {
					// set menu screen here
				}
			}
		}
	}

	public void render(Screen screen) {
		super.render(screen);

		int xt = 0;
		int yt = 0;
		int xo = x - 8;
		int yo = y - 13;

		if (dir > 1) xt += walkDist / 2 % 4;

		screen.renderEntity(xo, yo, xt + yt * 32, 1 - (dir + 2));
	}

	protected void touchedBy(Entity entity) {
		// if (entity == game.player) entity.hurt(this, 0, dir); // TODO
		// else entity.touchedBy(this);
	}

	public void attack() {
		walkDist += 8;
		attackDir = dir;
		attackItem = activeItem;
		boolean done = false;

		if (activeItem != null) {
			int yo = -2;
			int range = 12;
			if (dir == 0 && interact(x - 8, y + 4 + yo, x + 8, y + range + yo)) done = true;
			if (dir == 1 && interact(x - 8, y - range + yo, x + 8, y - 4 + yo)) done = true;
			if (dir == 2 && interact(x + 4, y - 8 + yo, x + range, y + 8 + yo)) done = true;
			if (dir == 3 && interact(x - range, y - 8 + yo, x - 4, y + 8 + yo)) done = true;
			if (done) return;

			int xt = x >> 4;
			int yt = (y + yo) >> 4;
			int r = 12;
			if (attackDir == 0) yt = (y + r + yo) >> 4;
			if (attackDir == 1) yt = (y - r + yo) >> 4;
			if (attackDir == 2) xt = (x - r) >> 4;
			if (attackDir == 3) xt = (x + r) >> 4;

			if (level.validLocation(xt, yt)) {
				if (activeItem.interactOn(level.getTile(xt, yt), level, xt, yt, this, attackDir)) {
					System.out.println(1);
					done = true;
				} else if (level.getTile(xt, yt).interact(level, xt, yt, this, activeItem, attackDir)) done = true;
				if (activeItem.isDepleted()) activeItem = null;
			}
		}

		if (done) return;

		if (activeItem == null || activeItem.canAttack()) {
		}
	}

	private boolean interact(int minX, int minY, int maxX, int maxY) {
		List<Entity> entities = level.getEntities(minX, minY, maxX, maxY);
		for (int i = 0; i < entities.size(); i++) {
			Entity e = entities.get(i);
			if (e != this) if (e.interact(this, null, attackDir)) return true;
		}
		return false;
	}

	public boolean use() {
		int yo = -2;
		if (dir == 0 && use(x - 8, y + 4 + yo, x + 8, y + 12 + yo)) return true;
		if (dir == 1 && use(x - 8, y - 12 + yo, x + 8, y - 4 + yo)) return true;
		if (dir == 3 && use(x + 4, y - 8 + yo, x + 12, y + 8 + yo)) return true;
		if (dir == 2 && use(x - 12, y - 8 + yo, x - 4, y + 8 + yo)) return true;

		int xt = x >> 4;
		int yt = (y + yo) >> 4;
		int r = 12;
		if (attackDir == 0) yt = (y + r + yo) >> 4;
		if (attackDir == 1) yt = (y - r + yo) >> 4;
		if (attackDir == 2) xt = (x - r) >> 4;
		if (attackDir == 3) xt = (x + r) >> 4;

		if (level.validLocation(xt, yt)) if (level.getTile(xt, yt).use(level, xt, yt, this, attackDir)) return true;

		return false;
	}

	private boolean use(int minX, int minY, int maxX, int maxY) {
		List<Entity> entities = level.getEntities(minX, minY, maxX, maxY);
		for (int i = 0; i < entities.size(); i++) {
			Entity e = entities.get(i);
			if (e != this) if (e.use(this, dir)) return true;
		}
		return false;
	}

	public boolean use(Bot bot, int attackDir) {
		// game.setGuiScreen(new GuiChangePlayer(this));
		return false;
	}
}
