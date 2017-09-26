package game.entity;

import java.util.ArrayList;
import java.util.List;

import game.item.Item;
import game.item.ResourceItem;
import game.item.resource.Resource;

public class Inventory {
	public List<Item> items = new ArrayList<Item>();

	public void add(Item item) {
		add(items.size(), item);
	}

	public void add(int slot, Item item) {
		items.add(slot, item);
	}

	private ResourceItem findResource(Resource resource) {
		for (int i = 0; i < items.size(); i++)
			if (items.get(i) instanceof ResourceItem) {
				ResourceItem has = (ResourceItem) items.get(i);
				if (has.resource == resource) return has;
			}
		return null;
	}

	public boolean removeResource(Resource r, int count) {
		ResourceItem ri = findResource(r);
		if (ri == null) return false;
		if (ri.count < count) return false;
		ri.count -= count;
		if (ri.count <= 0) items.remove(ri);
		return true;
	}
}
