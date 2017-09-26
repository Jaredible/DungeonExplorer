package game.gfx;

public class Screen {
	public final int w, h;
	public int[] pixels;

	private SpriteSheet sheet;

	public int xOffset;
	public int yOffset;

	public static final int BIT_MIRROR_X = 0x01;
	public static final int BIT_MIRROR_Y = 0x02;

	public Screen(int w, int h, SpriteSheet sheet) {
		this.w = w;
		this.h = h;
		this.sheet = sheet;

		pixels = new int[w * h];
	}

	public void clear() {
		for (int i = 0; i < pixels.length; i++)
			pixels[i] = 0;
	}

	public void render(int xp, int yp, int tile, int bits) {
		xp -= xOffset;
		yp -= yOffset;
		boolean mirrorX = (bits & BIT_MIRROR_X) > 0;
		boolean mirrorY = (bits & BIT_MIRROR_Y) > 0;

		int xTile = tile % 32;
		int yTile = tile / 32;
		int toffs = xTile * 8 + yTile * 8 * sheet.width;

		int x, y;
		int xs, ys;
		int col;
		for (y = 0; y < 8; y++) {
			if (y + yp < 0 || y + yp >= h) continue;
			ys = y;
			if (mirrorY) ys = 7 - y;
			for (x = 0; x < 8; x++) {
				if (x + xp < 0 || x + xp >= w) continue;
				xs = x;
				if (mirrorX) xs = 7 - x;
				col = sheet.pixels[xs + ys * sheet.width + toffs];
				if (col != 0xffff7fff && col != 0xff7f007f) pixels[(x + xp) + (y + yp) * w] = col;
			}
		}
	}

	public void renderEntity(int xp, int yp, int tile, int bits) {
		xp -= xOffset;
		yp -= yOffset;
		boolean mirrorX = (bits & BIT_MIRROR_X) > 0;
		boolean mirrorY = (bits & BIT_MIRROR_Y) > 0;

		int xTile = tile % 32;
		int yTile = tile / 32;
		int toffs = xTile * 16 + yTile * 16 * SpriteSheet.entities.width;

		for (int y = 0; y < 16; y++) {
			if (y + yp < 0 || y + yp >= h) continue;
			int ys = y;
			if (mirrorY) ys = 15 - y;
			for (int x = 0; x < 16; x++) {
				if (x + xp < 0 || x + xp >= w) continue;
				int xs = x;
				if (mirrorX) xs = 15 - x;
				int col = SpriteSheet.entities.pixels[xs + ys * SpriteSheet.entities.width + toffs];
				if (col != 0xffff00ff && col != 0xffff7fff) pixels[(x + xp) + (y + yp) * w] = col;
			}
		}
	}

	public void renderFont(int xp, int yp, int tile, int color) {
		xp -= xOffset;
		yp -= yOffset;

		int xTile = tile % 32;
		int yTile = tile / 32;
		if (yTile > 2) throw new RuntimeException("Nothing exists down there!");
		int toffs = xTile * 6 + yTile * 6 * SpriteSheet.font.width;

		for (int y = 0; y < 6; y++) {
			if (y + yp < 0 || y + yp >= h) continue;
			for (int x = 0; x < 6; x++) {
				if (x + xp < 0 || x + xp >= w) continue;
				int col = SpriteSheet.font.pixels[x + y * SpriteSheet.font.width + toffs];
				if (col != 0xffff00dc) pixels[(x + xp) + (y + yp) * w] = col & color;
			}
		}
	}

	public void renderLight(int x, int y, int rad, int dampen) {
		x -= xOffset;
		y -= yOffset;
		int minX = x - rad;
		int maxX = x + rad;
		int minY = y - rad;
		int maxY = y + rad;

		if (minX < 0) minX = 0;
		if (minY < 0) minY = 0;
		if (maxX > w) maxX = w;
		if (maxY > h) maxY = h;

		int xd;
		int yd;
		int col;
		double dist;
		double intensity;
		int r, g, b;
		for (int yy = minY; yy < maxY; yy++) {
			yd = yy - y;
			for (int xx = minX; xx < maxX; xx++) {
				xd = xx - x;
				dist = Math.sqrt(xd * xd + yd * yd) + dampen;
				if (dist < rad) {
					col = pixels[xx + yy * w];
					intensity = rad / dist;
					double n = rad / dist;
					double rr = (dist / rad) * n;
					double gg = dist / rad;
					double bb = dist / rad;
					r = (int) ((col >> 16 & 255) * intensity * rr);
					g = (int) ((col >> 8 & 255) * intensity * gg);
					b = (int) ((col & 255) * intensity * bb);
					if (r > 255) r = 255;
					if (g > 255) g = 255;
					if (b > 255) b = 255;
					pixels[xx + yy * w] = ((r & 255) << 16 | (g & 255) << 8 | b & 255);
				}
			}
		}
	}

	public void setOffset(int xOffset, int yOffset) {
		this.xOffset = xOffset;
		this.yOffset = yOffset;
	}
}
