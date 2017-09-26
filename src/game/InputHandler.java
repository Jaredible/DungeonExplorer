package game;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;

public class InputHandler implements KeyListener {
	public List<Key> keys = new ArrayList<Key>();

	public Key w = new Key();
	public Key s = new Key();
	public Key a = new Key();
	public Key d = new Key();
	public Key x = new Key();
	public Key c = new Key();
	public Key space = new Key();

	public InputHandler(Game game) {
		game.addKeyListener(this);
	}

	public void releaseAll() {
		for (int i = 0; i < keys.size(); i++)
			keys.get(i).down = false;
	}

	public void tick() {
		for (int i = 0; i < keys.size(); i++)
			keys.get(i).tick();
	}

	private void toggle(KeyEvent e, boolean pressed) {
		if (e.getKeyCode() == KeyEvent.VK_W) w.toggle(pressed);
		if (e.getKeyCode() == KeyEvent.VK_S) s.toggle(pressed);
		if (e.getKeyCode() == KeyEvent.VK_A) a.toggle(pressed);
		if (e.getKeyCode() == KeyEvent.VK_D) d.toggle(pressed);
		if (e.getKeyCode() == KeyEvent.VK_X) x.toggle(pressed);
		if (e.getKeyCode() == KeyEvent.VK_C) c.toggle(pressed);
		if (e.getKeyCode() == KeyEvent.VK_SPACE) space.toggle(pressed);
	}

	public void keyPressed(KeyEvent e) {
		toggle(e, true);
	}

	public void keyReleased(KeyEvent e) {
		toggle(e, false);
	}

	public void keyTyped(KeyEvent e) {
	}

	public class Key {
		public int presses, absorbs;
		public boolean down, clicked;

		public Key() {
			keys.add(this);
		}

		public void toggle(boolean pressed) {
			if (pressed != down) down = pressed;
			if (pressed) presses++;
		}

		public void tick() {
			if (absorbs < presses) {
				absorbs++;
				clicked = true;
			} else clicked = false;
		}
	}
}
