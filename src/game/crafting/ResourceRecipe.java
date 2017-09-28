package game.crafting;

import game.entity.Bot;
import game.item.ResourceItem;
import game.item.resource.Resource;

public class ResourceRecipe extends Recipe {
	private Resource resource;

	public ResourceRecipe(Resource resource) {
		super(new ResourceItem(resource, 1));
		this.resource = resource;
	}

	public ResourceRecipe(Resource resource, int count) {
		super(new ResourceItem(resource, count));
		this.resource = resource;
	}

	public void craft(Bot bot) {
	}
}
