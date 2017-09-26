package game.item;

import game.gfx.Screen;
import game.item.resource.Resource;

public class ResourceItem extends Item {
	public Resource resource;
	public int count = 1;

	public ResourceItem(Resource resource) {
		this.resource = resource;
	}

	public ResourceItem(Resource resource, int count) {
		this.resource = resource;
		this.count = count;
	}

	public void renderIcon(Screen screen, int x, int y) {
	}
}
