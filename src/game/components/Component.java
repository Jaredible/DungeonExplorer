package game.components;

import game.gfx.Screen;

public class Component {
	public static Component[] components = new Component[16];
	public static Component EMPTY = new Component(0);
	public static LogicGate AND = new LogicGate(1);
	public static LogicGate OR = new LogicGate(2);
	public static LogicGate NOT = new LogicGate(3);
	public static LogicGate NAND = new LogicGate(4);
	public static LogicGate NOR = new LogicGate(5);
	public static LogicGate EXOR = new LogicGate(6);
	public static LogicGate EXNOR = new LogicGate(7);
	public static Wire WIRE = new Wire(8);

	public final byte id;

	public Component(int id) {
		this.id = (byte) id;
		if (components[id] != null) throw new RuntimeException("Duplicate component ids!");
		components[id] = this;
	}

	public void tick() {
	}

	public void render(Screen screen, int x, int y) {
		screen.render(x * 8, y * 8, id + 32, 0);
	}
}
