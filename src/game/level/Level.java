package game.level;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

import game.Game;
import game.entity.Entity;
import game.gfx.Screen;
import game.level.tile.Tile;

public class Level {
	private Game game;
	public int w, h;
	public int startX, startY;
	public int endX, endY;

	public byte[] tiles;
	public byte[] data;
	public List<Entity>[] entitiesInTiles;

	public List<Entity> entities = new ArrayList<Entity>();
	private List<Entity> rowSprites = new ArrayList<Entity>();
	private Comparator<Entity> spriteSorter = new Comparator<Entity>() {
		public int compare(Entity e0, Entity e1) {
			if (e1.y < e0.y) return 1;
			if (e1.y > e0.y) return -1;
			return 0;
		}
	};

	private Random random = new Random();
	private LevelInfo info;
	private List<Island> islands = new ArrayList<>();

	@SuppressWarnings("unchecked")
	protected Level(Game game, int w, int h) {
		this.game = game;
		this.w = w;
		this.h = h;

		tiles = new byte[w * h];
		data = new byte[w * h];

		entitiesInTiles = new ArrayList[w * h];
		for (int i = 0; i < w * h; i++)
			entitiesInTiles[i] = new ArrayList<Entity>();
	}

	public Level(Game game, LevelInfo info) {
		this(game, info.getWidth(), info.getHeight());
		this.info = info;
	}

	public void generate(long seed) {
		System.out.println("Building");
		int i = info.getIslandCount();
		random.setSeed(seed);

		boolean first = true;
		while (i > 0)
			if (canPlaceIsland(first)) {
				i--;
				first = false;
			}
		endX = startY;
		endY = startY;

		int dx, dy;
		float dist;
		float bestDist = 0;
		Island island;
		for (i = 0; i < islands.size(); i++) {
			island = islands.get(i);
			if (island.connected().size() == 1) {
				dx = island.getCenterX() - startX;
				dy = island.getCenterY() - startY;
				dist = (float) Math.sqrt(dx * dx + dy * dy);

				if (dist > bestDist) {
					bestDist = dist;
					endX = island.getCenterX();
					endY = island.getCenterY();
				}
			}
		}

		// decorate();
		// removeOddDoors();
		// scatterDetails();
	}

	private boolean canPlaceIsland(boolean first) {
		int roomWidth = Room.MIN_WIDTH + random.nextInt(Room.WIDTH_RANGE + 1) * 2;
		int roomHeight = Room.MIN_HEIGHT + random.nextInt(Room.HEIGHT_RANGE + 1) * 2;
		int x = random.nextInt(w - roomWidth - 1);
		int y = random.nextInt(h - roomHeight - 1);

		if (x <= 16 || y <= 16 || x > w - 16 || y > h - 16) return false;

		int xp;
		int yp;
		int maxXD = 0 + random.nextInt(3) + random.nextInt(3);
		int maxYD = 0 + random.nextInt(3) + random.nextInt(3);

		if (first) {
			x = (w - roomWidth) / 2;
			y = (h - roomHeight) / 2;
			startX = x + roomWidth / 2;
			startY = y + roomHeight / 2;
		} else {
			boolean foundAdjacent = false;

			Tile t;
			int d;
			for (yp = -maxYD - 1; yp <= roomHeight + maxYD; yp++)
				for (xp = -maxXD - 1; xp <= roomWidth + maxXD; xp++) {
					t = getTile(x + xp, y + yp);
					d = getData(x + xp, y + yp);
					if (t == Tile.ground && d == 1) {
						if (xp == -maxXD - 1 || xp == roomWidth + maxXD) {
							if (yp <= 0 || yp >= roomHeight - maxYD) continue;
							foundAdjacent = true;
							continue;
						}
						if (yp == -maxYD - 1 || yp == roomHeight + maxYD) {
							if (xp <= 0 || xp >= roomWidth - maxXD) continue;
							foundAdjacent = true;
							continue;
						}
					}
					if (t != Tile.air) return false;
				}
			if (!foundAdjacent) return false;
		}

		for (yp = 0; yp < roomHeight; yp++)
			for (xp = 0; xp < roomWidth; xp++)
				if (getTile(x + xp, y + yp) == Tile.air) {
					if (xp == 0 || yp == 0 || xp == roomWidth - 1 || yp == roomHeight - 1) setTile(x + xp, y + yp, Tile.ground, 1);
					else setTile(x + xp, y + yp, Tile.ground, 0);
				}

		Island island = new Island(x, y, roomWidth, roomHeight);
		islands.add(island);

		boolean test = false;

		if (test) {
			List<int[]> up = new ArrayList<>();
			List<int[]> down = new ArrayList<>();
			List<int[]> left = new ArrayList<>();
			List<int[]> right = new ArrayList<>();

			for (xp = 0; xp < roomWidth; xp++) {
				if (getTile(x + xp, y - 2) == Tile.ground) up.add(new int[] { x + xp, y - 1 });
				if (getTile(x + xp, y + roomHeight + 1) == Tile.ground) down.add(new int[] { x + xp, y + roomHeight });
			}
			for (yp = 0; yp < roomHeight; yp++) {
				if (getTile(x - 2, y + yp) == Tile.ground) left.add(new int[] { x - 1, y + yp });
				if (getTile(x + roomWidth + 1, y + yp) == Tile.ground) right.add(new int[] { x + roomWidth, y + yp });
			}

			int[] pos;
			if (left.size() != 0) {
				pos = left.get(random.nextInt(left.size()));
				setTile(pos[0], pos[1], Tile.bridge, 0);
				island.connect(getIslandAt(pos[0] - 1, pos[1]), pos[0], pos[1]);
			}
			if (right.size() != 0) {
				pos = right.get(random.nextInt(right.size()));
				setTile(pos[0], pos[1], Tile.bridge, 0);
				island.connect(getIslandAt(pos[0] + 1, pos[1]), pos[0], pos[1]);
			}
			if (up.size() != 0) {
				pos = up.get(random.nextInt(up.size()));
				setTile(pos[0], pos[1], Tile.bridge, 0);
				island.connect(getIslandAt(pos[0], pos[1] - 1), pos[0], pos[1]);
			}
			if (down.size() != 0) {
				pos = down.get(random.nextInt(down.size()));
				setTile(pos[0], pos[1], Tile.bridge, 0);
				island.connect(getIslandAt(pos[0], pos[1] + 1), pos[0], pos[1]);
			}
		}

		return true;
	}

	// private boolean canPlaceRoom2(boolean first) {
	// int maxXD = 2;
	// int maxYD = 2;
	//
	// int roomWidth = Room.MIN_WIDTH + random.nextInt(Room.WIDTH_RANGE + 1) *
	// 2;
	// int roomHeight = Room.MIN_HEIGHT + random.nextInt(Room.HEIGHT_RANGE + 1)
	// * 2;
	// int x = 1 + random.nextInt(w - roomWidth - 1);
	// int y = 1 + random.nextInt(h - roomHeight - 1);
	// int xp;
	// int yp;
	//
	// if (first) {
	// x = (w - roomWidth) / 2;
	// y = (h - roomHeight) / 2;
	// }
	//
	// if (!first) {
	// boolean foundAdjacent = false;
	//
	// Tile tile;
	// for (yp = -maxYD; yp <= roomHeight + maxYD; yp++)
	// for (xp = -maxYD; xp <= roomWidth + maxYD; xp++) {
	// tile = getTile(x + xp, y + yp);
	// if (tile == Tile.ground) {
	// if (xp == -maxXD || xp == roomWidth + maxXD) {
	// if (yp <= 0 || yp >= roomHeight - maxYD) continue;
	// foundAdjacent = true;
	// continue;
	// }
	// if (yp == -maxYD || yp == roomHeight + maxYD) {
	// if (xp <= 0 || xp >= roomWidth - maxXD) continue;
	// foundAdjacent = true;
	// continue;
	// }
	// }
	// if (tile != Tile.air) return false;
	// }
	// if (!foundAdjacent) return false;
	// }
	//
	// maxXD += 2;
	// maxYD += 2;
	//
	// for (yp = -maxYD; yp <= roomHeight + maxYD; yp++)
	// for (xp = -maxXD; xp <= roomWidth + maxXD; xp++)
	// if (getTile(x + xp, y + yp) == Tile.air) setTile(x + xp, y + yp,
	// Tile.ground, 0);
	//
	// Room room = new Room(x, y, roomWidth, roomHeight);
	// rooms.add(room);
	//
	// return true;
	// }
	//
	// private boolean canPlaceRoom(boolean first) {
	// int roomWidth = Room.MIN_WIDTH + random.nextInt(Room.WIDTH_RANGE + 1) *
	// 2;
	// int roomHeight = Room.MIN_HEIGHT + random.nextInt(Room.HEIGHT_RANGE + 1)
	// * 2;
	// int x = 1 + random.nextInt(w - roomWidth - 1);
	// int y = 1 + random.nextInt(h - roomHeight - 1);
	// int xp;
	// int yp;
	//
	// if (first) {
	// x = (w - roomWidth) / 2;
	// y = (h - roomHeight) / 2;
	// }
	//
	// if (!first) {
	// boolean foundAdjacent = false;
	//
	// Tile tile;
	// for (yp = -1; yp <= roomHeight; yp++)
	// for (xp = -1; xp <= roomWidth; xp++) {
	// tile = getTile(x + xp, y + yp);
	// if (tile == Tile.wall) {
	// if (xp == -1 || xp == roomWidth) {
	// if (yp <= 0 || yp >= roomHeight - 1) continue;
	// foundAdjacent = true;
	// continue;
	// }
	// if (yp == -1 || yp == roomHeight) {
	// if (xp <= 0 || xp >= roomWidth - 1) continue;
	// foundAdjacent = true;
	// continue;
	// }
	// }
	// if (tile != Tile.air) return false;
	// }
	// if (!foundAdjacent) return false;
	// }
	//
	// for (yp = -1; yp <= roomHeight; yp++)
	// for (xp = -1; xp <= roomWidth; xp++)
	// if (getTile(x + xp, y + yp) == Tile.air) {
	// if (xp == -1 || yp == -1 || xp == roomWidth || yp == roomHeight)
	// setTile(x + xp, y + yp, Tile.wall, 0);
	// else setTile(x + xp, y + yp, Tile.ground, 0);
	// }
	//
	// Room room = new Room(x, y, roomWidth, roomHeight);
	// rooms.add(room);
	//
	// List<int[]> up = new ArrayList<>();
	// List<int[]> down = new ArrayList<>();
	// List<int[]> left = new ArrayList<>();
	// List<int[]> right = new ArrayList<>();
	//
	// for (xp = 0; xp < roomWidth; xp++) {
	// if (getTile(x + xp, y - 2) == Tile.ground) up.add(new int[] { x + xp, y -
	// 1 });
	// if (getTile(x + xp, y + roomHeight + 1) == Tile.ground) down.add(new
	// int[] { x + xp, y + roomHeight });
	// }
	// for (yp = 0; yp < roomHeight; yp++) {
	// if (getTile(x - 2, y + yp) == Tile.ground) left.add(new int[] { x - 1, y
	// + yp });
	// if (getTile(x + roomWidth + 1, y + yp) == Tile.ground) right.add(new
	// int[] { x + roomWidth, y + yp });
	// }
	//
	// int[] pos;
	// if (left.size() != 0) {
	// pos = left.get(random.nextInt(left.size()));
	// setTile(pos[0], pos[1], Tile.door, 0);
	// room.connect(getRoomAt(pos[0] - 1, pos[1]), pos[0], pos[1]);
	// }
	// if (right.size() != 0) {
	// pos = right.get(random.nextInt(right.size()));
	// setTile(pos[0], pos[1], Tile.door, 0);
	// room.connect(getRoomAt(pos[0] + 1, pos[1]), pos[0], pos[1]);
	// }
	// if (up.size() != 0) {
	// pos = up.get(random.nextInt(up.size()));
	// setTile(pos[0], pos[1], Tile.door, 0);
	// room.connect(getRoomAt(pos[0], pos[1] - 1), pos[0], pos[1]);
	// }
	// if (down.size() != 0) {
	// pos = down.get(random.nextInt(down.size()));
	// setTile(pos[0], pos[1], Tile.door, 0);
	// room.connect(getRoomAt(pos[0], pos[1] + 1), pos[0], pos[1]);
	// }
	//
	// if (first) {
	// startX = room.getCenterX();
	// startY = room.getCenterY();
	// }
	//
	// return true;
	// }

	public Island getIslandAt(int x, int y) {
		Island island;
		for (int i = 0; i < islands.size(); i++) {
			island = islands.get(i);
			if (island.contains(x, y)) return island;
		}
		return null;
	}

	// private void decorate() {
	// int x;
	// int y;
	// int roomWidth;
	// int roomHeight;
	// Island island;
	// for (int i = 0; i < islands.size(); i++) {
	// island = islands.get(i);
	// x = island.getX();
	// y = island.getY();
	// roomWidth = island.getWidth();
	// roomHeight = island.getHeight();
	//
	// if (random.nextInt(3) == 0) if (random.nextInt(12) > 9) {
	// placeCorner(x, y);
	// placeCorner(x + roomWidth, y);
	// placeCorner(x + roomWidth, y + roomHeight);
	// placeCorner(x, y + roomHeight);
	// }
	// }
	// }

	// private void placeCorner(int x, int y) {
	// if (isDoor(x - 1, y)) return;
	// if (isDoor(x + 1, y)) return;
	// if (isDoor(x, y - 1)) return;
	// if (isDoor(x, y + 1)) return;
	// if (getTile(x - 1, y - 1) == Tile.wall) return;
	// if (getTile(x + 1, y - 1) == Tile.wall) return;
	// if (getTile(x + 1, y + 1) == Tile.wall) return;
	// if (getTile(x - 1, y + 1) == Tile.wall) return;
	//
	// setTile(x, y, Tile.wall, 0);
	// }

	// private boolean isDoor(int x, int y) {
	// Tile tile = getTile(x, y);
	// if (tile == Tile.door) return true;
	// return false;
	// }

	// private void removeOddDoors() {
	// List<int[]> doors = new ArrayList<>();
	// int x;
	// int y;
	//
	// for (y = 0; y < h; y++)
	// for (x = 0; x < w; x++)
	// if (getTile(x, y) == Tile.door) doors.add(new int[] { x, y });
	//
	// if (doors.size() == 0) return;
	//
	// int remove = doors.size() / 2;
	// int door;
	// int[] loc;
	// for (int i = 0; i < remove && doors.size() != 0; i++) {
	// door = random.nextInt(doors.size());
	// loc = doors.remove(door);
	// x = loc[0];
	// y = loc[1];
	//
	// if (!isJoinedIsland(x - 1, y)) if (!isJoinedIsland(x + 1, y)) if
	// (!isJoinedIsland(x, y - 1)) if (!isJoinedIsland(x, y + 1)) {
	// removeDoor(x, y);
	// joinIsland(x - 1, y);
	// joinIsland(x + 1, y);
	// joinIsland(x, y - 1);
	// joinIsland(x, y + 1);
	// }
	// }
	// }

	// private void removeDoor(int x, int y) {
	// if (getTile(x - 1, y - 1) == Tile.wall) return;
	// if (getTile(x + 1, y - 1) == Tile.wall) return;
	// if (getTile(x + 1, y + 1) == Tile.wall) return;
	// if (getTile(x - 1, y + 1) == Tile.wall) return;
	//
	// if (getTile(x - 1, y) == Tile.wall) {
	// int xp = 0;
	//
	// while (getTile(x - xp, y - 1) == Tile.ground && getTile(x - xp, y + 1) ==
	// Tile.ground) {
	// setTile(x - xp, y, Tile.ground, 0);
	//
	// if (++xp > 10) break;
	// }
	//
	// xp = 0;
	//
	// while (getTile(x + xp, y - 1) == Tile.ground && getTile(x + xp, y + 1) ==
	// Tile.ground) {
	// setTile(x + xp, y, Tile.ground, 0);
	//
	// if (++xp > 10) break;
	// }
	// } else if (getTile(x, y - 1) == Tile.wall) {
	// int yp = 0;
	//
	// while (getTile(x - 1, y - yp) == Tile.ground && getTile(x + 1, y - yp) ==
	// Tile.ground) {
	// setTile(x, y - yp, Tile.ground, 0);
	//
	// if (++yp > 10) break;
	// }
	//
	// yp = 0;
	//
	// while (getTile(x - 1, y + yp) == Tile.ground && getTile(x + 1, y + yp) ==
	// Tile.ground) {
	// setTile(x, y + yp, Tile.ground, 0);
	//
	// if (++yp > 10) break;
	// }
	// }
	// }

	private boolean isJoinedIsland(int x, int y) {
		Island island = getIslandAt(x, y);
		if (island == null) return false;
		return island.isJoined();
	}

	private void joinIsland(int x, int y) {
		Island island = getIslandAt(x, y);
		if (island != null) island.setJoined(true);
	}

	private void scatterDetails() {
	}

	public void tick() {
		for (int i = 0; i < entities.size(); i++) {
			Entity e = entities.get(i);
			int xto = e.x >> 4;
			int yto = e.y >> 4;

			e.tick();

			if (e.removed) remove(e);
			else {
				int xt = e.x >> 4;
				int yt = e.y >> 4;

				if (xto != xt || yto != yt) {
					removeEntity(xto, yto, e);
					insertEntity(xt, yt, e);
				}
			}
		}
	}

	private int[] tileBuffer = new int[(int) ((Math.ceil(Game.GAME_WIDTH / 16) + 1) * (Math.ceil(Game.GAME_HEIGHT / 16) + 1) * 2)];

	public void renderLevel(Screen screen, int xScroll, int yScroll) {
		int xo = xScroll >> 4;
		int yo = yScroll >> 4;
		int w = (screen.w + 15) >> 4;
		int h = (screen.h + 15) >> 4;

		screen.setOffset(xScroll, yScroll);

		int x, y;
		Tile t;
		int i = 0;
		for (y = yo; y <= h + yo; y++)
			for (x = xo; x <= w + xo; x++) {
				t = getTile(x, y);
				if (t == Tile.air) {
					tileBuffer[i] = x;
					tileBuffer[tileBuffer.length - i - 1] = y;
					i++;
				} else t.render(screen, this, x, y);
			}

		// render lights here
		screen.renderLight(game.player.x, game.player.y, 50 + random.nextInt(100) / 50, 10 + random.nextInt(4), false);
		screen.renderLight(2040, 2040, 50 + random.nextInt(100) / 50, 10 + random.nextInt(4), true);

		for (y = yo; y <= h + yo; y++) {
			for (x = xo; x <= w + xo; x++)
				if (validLocation(x, y)) rowSprites.addAll(entitiesInTiles[x + y * this.w]);
			if (rowSprites.size() > 0) sortAndRender(screen, rowSprites);
			rowSprites.clear();
		}

		for (int j = 0; j < i; j++) {
			x = tileBuffer[j];
			y = tileBuffer[tileBuffer.length - j - 1];
			tileBuffer[j] = 0;
			tileBuffer[tileBuffer.length - j - 1] = 0;
			getTile(x, y).render(screen, this, x, y);
		}

		screen.setOffset(0, 0);
	}

	private void sortAndRender(Screen screen, List<Entity> list) {
		Collections.sort(list, spriteSorter);
		for (int i = 0; i < list.size(); i++)
			list.get(i).render(screen);
	}

	public Tile getTile(int x, int y) {
		if (validLocation(x, y)) return Tile.tiles[tiles[x + y * w]];
		return Tile.air;
	}

	public boolean setTile(int x, int y, Tile t, int dataVal) {
		if (validLocation(x, y)) {
			if (getTile(x, y) == Tile.air) {
				tiles[x + y * w] = t.id;
				data[x + y * w] = (byte) dataVal;
				return true;
			}
		}
		return false;
	}

	public int getData(int x, int y) {
		if (validLocation(x, y)) return data[x + y * w] & 0xff;
		return 0;
	}

	public void setData(int x, int y, int val) {
		if (validLocation(x, y)) data[x + y * w] = (byte) val;
	}

	public void add(Entity entity) {
		entity.removed = false;
		entities.add(entity);
		entity.init(this);

		insertEntity(entity.x >> 4, entity.y >> 4, entity);
	}

	public void remove(Entity e) {
		entities.remove(e);
		removeEntity(e.x >> 4, e.y >> 4, e);
	}

	private void insertEntity(int x, int y, Entity e) {
		if (validLocation(x, y)) entitiesInTiles[x + y * w].add(e);
	}

	private void removeEntity(int x, int y, Entity e) {
		if (validLocation(x, y)) entitiesInTiles[x + y * w].remove(e);
	}

	public boolean validLocation(int x, int y) {
		return x >= 0 && y >= 0 && x < w && y < h;
	}

	public List<Entity> getEntities(int minX, int minY, int maxX, int maxY) {
		List<Entity> result = new ArrayList<Entity>();
		int minX2 = (minX >> 4) - 1;
		int minY2 = (minY >> 4) - 1;
		int maxX2 = (maxX >> 4) + 1;
		int maxY2 = (maxY >> 4) + 1;
		for (int y = minY2; y <= maxY2; y++)
			for (int x = minX2; x <= maxX2; x++) {
				if (validLocation(x, y)) {
					List<Entity> entities = entitiesInTiles[x + y * w];
					for (int i = 0; i < entities.size(); i++) {
						Entity e = entities.get(i);
						if (e.intersects(minX, minY, maxX, maxY)) result.add(e);
					}
				}
			}
		return result;
	}
}
