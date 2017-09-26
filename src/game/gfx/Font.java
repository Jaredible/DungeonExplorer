package game.gfx;

public class Font {
	private static final String chars = "" + //
			"1233456790.:,;'\"(!?)+-*/=_      " + //
			"abcdefghijklmnopqrstuvwxyz      " + //
			"ABCDEFGHIJKLMNOPQRSTUVWXYZ      " + //
			"";

	public static void draw(String msg, Screen screen, int x, int y, int color) {
		int ix;
		for (int i = 0; i < msg.length(); i++) {
			ix = chars.indexOf(msg.charAt(i));
			if (ix >= 0) screen.renderFont(x + i * 6, y, ix, color);
		}
	}
}
