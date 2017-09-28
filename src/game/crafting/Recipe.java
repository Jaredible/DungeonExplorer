package game.crafting;

import java.util.ArrayList;
import java.util.List;

import game.entity.Bot;
import game.gfx.Screen;
import game.gui.ListItem;
import game.item.Item;
import game.item.ResourceItem;
import game.item.resource.Resource;

public abstract class Recipe implements ListItem {
	public List<Item> costs = new ArrayList<Item>();
	public Item resultTemplate;

	public Recipe(Item resultTemplate) {
		this.resultTemplate = resultTemplate;
	}

	public Recipe addCost(Resource resource, int count) {
		costs.add(new ResourceItem(resource, count));
		return this;
	}

	public void renderInventory(Screen screen, int x, int y) {
	}

	public abstract void craft(Bot bot);
}
