package game.gui;

import game.gfx.Font;
import game.gfx.Screen;

public class GuiButton extends Gui {
	public int id;
	private int x;
	private int y;
	public String displayString;
	public boolean enabled = true;

	public GuiButton(int id, int x, int y, String displayString) {
		this.id = id;
		this.x = x;
		this.y = y;
		this.displayString = displayString;
	}

	public void drawButton(Screen screen, int color) {
		Font.draw(displayString, screen, (screen.w - displayString.length() * 6) / 2, y, color);
	}

	public int getColor(boolean selected) {
		int color = 0x7f7f7f;

		if (!enabled) color = 0x222222;
		else if (selected) color = 0xffffff;

		return color;
	}
}
