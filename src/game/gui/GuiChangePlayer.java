package game.gui;

import game.entity.Bot;
import game.gfx.Screen;

public class GuiChangePlayer extends GuiScreen {
	private int progress = 0;
	private Bot newBot;

	public GuiChangePlayer(Bot newBot) {
		this.newBot = newBot;
	}

	public void updateScreen() {
		if (progress++ == 100) {
			// game.setPlayerTo(newBot);
			game.setGuiScreen(null);
		}
	}

	public void drawScreen(Screen screen) {
		drawLoadingBar(screen, (screen.w - 100) / 2, (screen.h - 1) / 2, progress);

		if (progress == 100) screen.clear();
	}
}
