package game.gfx;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import game.Game;

public class SpriteSheet {
	public static SpriteSheet icons;
	public static SpriteSheet tiles;
	public static SpriteSheet font;
	public static SpriteSheet entities;
	public int width, height;
	public int[] pixels;

	public SpriteSheet(BufferedImage image) {
		width = image.getWidth();
		height = image.getHeight();
		pixels = image.getRGB(0, 0, width, height, null, 0, width);
	}

	private SpriteSheet darken() {
		int col;
		int r, g, b;
		for (int i = 0; i < pixels.length; i++) {
			col = pixels[i];
			r = (int) ((col >> 16 & 255) * 0.25);
			g = (int) ((col >> 8 & 255) * 0.25);
			b = (int) ((col & 255) * 0.25);
			pixels[i] = (r & 255) << 16 | (g & 255) << 8 | b & 255;
		}
		return this;
	}

	static {
		try {
			icons = new SpriteSheet(ImageIO.read(Game.class.getResourceAsStream("/icons.png")));
			tiles = new SpriteSheet(ImageIO.read(Game.class.getResourceAsStream("/tiles.png")));
			font = new SpriteSheet(ImageIO.read(Game.class.getResourceAsStream("/font.png")));
			entities = new SpriteSheet(ImageIO.read(Game.class.getResourceAsStream("/entities.png")));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
