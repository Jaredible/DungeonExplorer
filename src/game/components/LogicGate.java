package game.components;

import game.gfx.Screen;

public class LogicGate extends Component {
	public LogicGate(int id) {
		super(id);
	}

	public void tick() {
	}

	public void render(Screen screen, int x, int y) {
		// screen.render(x * 8, y * 8, 0, 0);
		super.render(screen, x, y);
	}
}
