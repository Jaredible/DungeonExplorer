package game;

import game.gfx.Screen;

public class PlayerController {
	private Game game;
	private InputHandler input;

	public PlayerController(Game game, InputHandler input) {
		this.game = game;
		this.input = input;
	}

	public void tick() {
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

	public void render(Screen screen) {
		// player.render(screen);
	}
}
