package game.gui;

import game.Game;
import game.gfx.Font;
import game.gfx.Screen;

public class GuiIngame extends Gui {
	private Game game;

	public GuiIngame(Game game) {
		this.game = game;
	}

	public void tick() {
	}

	public void render(Screen screen) {
		for (int i = 0; i < game.player.health; i++)
			screen.pixels[(0 + i * 2) + 0 * screen.w] = 0xff0000;

		Font.draw("Hello", screen, 0, 6, 0xff0000);
	}
}
