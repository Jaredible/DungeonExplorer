package game.gui;

import game.gfx.Font;
import game.gfx.Screen;

public class GuiMainMenu extends GuiScreen {
	private String title = "Explorer";

	public void initGui() {
		buttons.add(new GuiButton(0, 10 * 1, 10 * 6, "Start"));
		buttons.add(new GuiButton(1, 10 * 1, 10 * 7, "Test"));
		GuiButton b = new GuiButton(2, 10 * 1, 10 * 8, "Test2");
		b.enabled = false;
		buttons.add(b);
	}

	public void updateScreen() {
		super.updateScreen();
	}

	public void drawScreen(Screen screen) {
		screen.clear();

		Font.draw(title, screen, (screen.w - title.length() * 6) / 2, 6 + (game.tickCount / 10) % 2, 0xffff00);

		super.drawScreen(screen);
	}

	protected void onActionPerformed(GuiButton button) {
		if (button.id == 0) game.startGame();
		if (button.id == 1) System.out.println("Testing");
	}
}
