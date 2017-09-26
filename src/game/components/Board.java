package game.components;

import game.InputHandler;
import game.gfx.Screen;
import game.gui.Gui;

public class Board extends Gui {
	public int size;
	private InputHandler input;

	public byte[] cells;
	public int x, y;

	public final int w = 16;
	public final int h = 12;

	public Board(int size, InputHandler input) {
		this.size = size;
		this.input = input;

		cells = new byte[size * size];

		setComponent(0, 0, Component.WIRE);
		setComponent(0, 1, Component.AND);
		setComponent(0, 2, Component.WIRE);
		for (int i = 1; i < size; i++)
			setComponent(i, 1, i % 2 == 0 ? Component.WIRE : Component.OR);
	}

	public void tick() {
		for (int i = 0; i < cells.length; i++)
			getComponent(i % size, i / size).tick();

		if (input.w.down) y--;
		if (input.s.down) y++;
		if (input.a.down) x--;
		if (input.d.down) x++;
	}

	public void render(Screen screen) {
		int xScroll = x;
		int yScroll = y;
		if (xScroll < 0) xScroll = 0;
		if (yScroll < 0) yScroll = 0;
		if (xScroll > size * 8 - w * 8) xScroll = size * 8 - w * 8;
		if (yScroll > size * 8 - h * 8) yScroll = size * 8 - h * 8;
		int xo = xScroll >> 3;
		int yo = yScroll >> 3;
		int w = (8 * this.w + 7) >> 3;
		int h = (8 * this.h + 7) >> 3;
		screen.setOffset(xScroll - 16, yScroll - 12);
		for (int y = yo; y <= h + yo; y++)
			for (int x = xo; x <= w + xo; x++)
				getComponent(x, y).render(screen, x, y);
		screen.setOffset(0, 0);

		screen.setOffset(-8, -4);
		drawFrame(screen, 0, 0, w + 1, h + 1);
		screen.setOffset(0, 0);
	}

	public Component getComponent(int x, int y) {
		if (validLocation(x, y)) return Component.components[cells[x + y * size]];
		return Component.EMPTY;
	}

	public boolean setComponent(int x, int y, Component component) {
		if (validLocation(x, y)) {
			cells[x + y * size] = component.id;
			return true;
		}
		return false;
	}

	public boolean validLocation(int x, int y) {
		return x >= 0 && y >= 0 && x < size && y < size;
	}
}
