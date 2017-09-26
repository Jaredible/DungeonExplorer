package game.crafting;

import java.util.ArrayList;
import java.util.List;

import game.item.resource.Resource;

public class Crafting {
	public static final List<Recipe> workbenchRecipes = new ArrayList<Recipe>();
	public static final List<Recipe> furnaceRecipes = new ArrayList<Recipe>();

	static {
		try {
			furnaceRecipes.add(new ResourceRecipe(Resource.glass, 1).addCost(Resource.sand, 1));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
