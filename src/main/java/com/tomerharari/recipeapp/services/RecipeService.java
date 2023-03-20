package com.tomerharari.recipeapp.services;

import com.tomerharari.recipeapp.commands.IngredientCommand;
import com.tomerharari.recipeapp.commands.RecipeCommand;
import com.tomerharari.recipeapp.model.Recipe;

import java.util.Set;


public interface RecipeService {
    Set<Recipe> getRecipes();
    Recipe findById(String id);
    RecipeCommand saveRecipeCommand(RecipeCommand command);
    RecipeCommand findCommandById(String id);
    void deleteById(String id);
    public IngredientCommand findRecipeIngredientById(String recipeId, String id);

}
