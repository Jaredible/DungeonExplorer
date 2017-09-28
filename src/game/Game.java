package game;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

import javax.swing.JFrame;

import game.entity.Bot;
import game.gfx.Screen;
import game.gfx.SpriteSheet;
import game.gui.GuiIngame;
import game.gui.GuiMainMenu;
import game.gui.GuiScreen;
import game.level.Level;
import game.level.LevelInfo;
import game.level.tile.Tile;

public class Game extends Canvas implements Runnable {
	private static final long serialVersionUID = 1L;
	public static final String TITLE = "Dungeon Explorer";
	public static final int GAME_WIDTH = 16 * 12;
	public static final int GAME_HEIGHT = GAME_WIDTH / 4 * 3;
	public static final int SCALE = 4;
	public static boolean DEV;

	private BufferedImage image = new BufferedImage(GAME_WIDTH, GAME_HEIGHT, BufferedImage.TYPE_INT_RGB);
	private int[] pixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();
	public boolean running = false;
	private Screen screen;
	private InputHandler input = new InputHandler(this);
	public GuiScreen guiScreen;
	public GuiIngame ingameGUI;

	public int tickCount = 0;

	public Level level;
	public Bot player;

	public void start() {
		running = true;
		new Thread(this).start();
	}

	public void stop() {
		running = false;
	}

	public void startGame() {
		level = new Level(this, new LevelInfo(256, 256, 300));
		level.generate(1);

		player = new Bot(this, input);
		Bot b = new Bot(this, input);

		player.findStartPos(level);
		level.add(player);
		b.findStartPos(level);
		level.add(b);

		setGuiScreen(null);
	}

	private void init() {
		screen = new Screen(GAME_WIDTH, GAME_HEIGHT, SpriteSheet.icons);

		ingameGUI = new GuiIngame(this);

		setGuiScreen(null);
		// setGuiScreen(new GuiCreate());
	}

	public void setGuiScreen(GuiScreen guiScreen) {
		if (this.guiScreen != null) this.guiScreen.onGuiClosed();

		if (guiScreen == null && level == null) guiScreen = new GuiMainMenu();

		this.guiScreen = guiScreen;

		if (guiScreen != null) guiScreen.validate(this, input);
	}

	public void run() {
		long lastTime = System.nanoTime();
		double unprocessed = 0;
		double nsPerTick = 1e9 / 60;
		int frames = 0;
		int ticks = 0;
		long lastTimer1 = System.currentTimeMillis();

		init();

		while (running) {
			long now = System.nanoTime();
			unprocessed += (now - lastTime) / nsPerTick;
			lastTime = now;
			boolean shouldRender = false;

			while (unprocessed >= 1) {
				ticks++;
				tick();
				unprocessed--;
				shouldRender = true;
			}

			try {
				Thread.sleep(2);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			if (shouldRender) {
				frames++;
				render();
			}

			if (System.currentTimeMillis() - lastTimer1 > 1000) {
				lastTimer1 += 1000;
				// System.out.println(ticks + " ticks, " + frames + " fps");
				frames = 0;
				ticks = 0;
			}
		}
	}

	public void tick() {
		tickCount++;
		if (!hasFocus()) input.releaseAll();
		else {
			input.tick();
			ingameGUI.tick();
			if (guiScreen != null) guiScreen.updateScreen();
			else if (level != null) {
				level.tick();
				Tile.tickCount++;
			}
		}
	}

	public void render() {
		BufferStrategy bs = getBufferStrategy();
		if (bs == null) {
			createBufferStrategy(3);
			requestFocus();
			return;
		}

		int x, y;
		int xo, yo;

		if (guiScreen != null) guiScreen.drawScreen(screen);
		else {
			xo = player.x - screen.w / 2;
			yo = player.y - screen.h / 2;
			if (xo < 0) xo = 0;
			if (yo < 0) yo = 0;
			if (xo > level.w * 16 - screen.w) xo = level.w * 16 - screen.w;
			if (yo > level.h * 16 - screen.h) yo = level.h * 16 - screen.h;

			for (y = 0; y < 19; y++)
				for (x = 0; x < 25; x++)
					screen.render(x * 8 - ((xo / 4) & 7), y * 8 - ((yo / 4) & 7), 6, 0);

			level.renderLevel(screen, xo, yo);

			ingameGUI.render(screen);
		}

		for (y = 0; y < GAME_HEIGHT; y++)
			for (x = 0; x < GAME_WIDTH; x++)
				pixels[x + y * GAME_WIDTH] = screen.pixels[x + y * screen.w];

		Graphics g = bs.getDrawGraphics();
		g.fillRect(0, 0, getWidth(), getHeight());

		xo = GAME_WIDTH * SCALE;
		yo = GAME_HEIGHT * SCALE;
		x = (getWidth() - xo) / 2;
		y = (getHeight() - yo) / 2;
		g.drawImage(image, x, y, xo, yo, null);
		g.dispose();
		bs.show();
	}

	public static void main(String[] args) {
		DEV = args[0].equals("DEV");

		Game game = new Game();
		Dimension dim = new Dimension(GAME_WIDTH * SCALE, GAME_HEIGHT * SCALE);
		game.setMinimumSize(dim);
		game.setMaximumSize(dim);
		game.setPreferredSize(dim);

		JFrame frame = new JFrame(TITLE);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new BorderLayout());
		frame.add(game, BorderLayout.CENTER);
		frame.pack();
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);

		if (DEV) frame.setAlwaysOnTop(true);

		game.start();
	}
}
