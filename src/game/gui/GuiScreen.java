package game.gui;

import java.util.ArrayList;
import java.util.List;

import game.Game;
import game.InputHandler;
import game.gfx.Screen;

public class GuiScreen extends Gui {
	protected Game game;
	protected InputHandler input;

	protected List<GuiButton> buttons = new ArrayList<GuiButton>();
	private int selected = 0;

	public void validate(Game game, InputHandler input) {
		this.game = game;
		this.input = input;
		buttons.clear();
		initGui();
	}

	public void initGui() {
	}

	public void updateScreen() {
		if (input.w.clicked) selected--;
		if (input.s.clicked) selected++;

		int len = buttons.size();
		if (selected < 0) selected += len;
		if (selected >= len) selected -= len;

		if (input.space.clicked) onActionPerformed(buttons.get(selected));
	}

	public void drawScreen(Screen screen) {
		for (int i = 0; i < buttons.size(); i++) {
			GuiButton button = buttons.get(i);
			button.drawButton(screen, button.getColor(selected == i));
		}
	}

	protected void onActionPerformed(GuiButton button) {
	}

	public void onGuiClosed() {
	}

	public void drawBackground() {
		if (game.level != null) {
			// draw gradient rect
		} else {
			// draw default background image
		}
	}

	public boolean doesGuiPauseGame() {
		return true;
	}
}
