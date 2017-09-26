package game.gui;

import game.gfx.Screen;

public class Gui {
	public void drawFrame(Screen screen, int minX, int minY, int maxX, int maxY) {
		for (int y = minY; y <= maxY; y++)
			for (int x = minX; x <= maxX; x++) {
				// System.out.println(1);
				if (x == minX && y == minY) screen.render(x * 8, y * 8, 4, 0);
				else if (x == maxX && y == minY) screen.render(x * 8, y * 8, 4, 0);
				else if (x == minX && y == maxY) screen.render(x * 8, y * 8, 4, 0);
				else if (x == maxX && y == maxY) screen.render(x * 8, y * 8, 4, 0);
				else if (y == minY) screen.render(x * 8, y * 8, 5, 0);
				else if (y == maxY) screen.render(x * 8, y * 8, 5, 0);
				else if (x == minX) screen.render(x * 8, y * 8, 5, 0);
				else if (x == maxX) screen.render(x * 8, y * 8, 5, 0);
				// else screen.render(x * 8, y * 8, 1, 0);
			}
	}

	public void drawLoadingBar(Screen screen, int x, int y, int progress) {
		for (int i = 0; i < progress; i++)
			screen.pixels[(x + i) + y * screen.w] = 0x00ff00;
	}
}
