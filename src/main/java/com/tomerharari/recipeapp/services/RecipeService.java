package com.tomerharari.recipeapp.services;

import com.tomerharari.recipeapp.commands.IngredientCommand;
import com.tomerharari.recipeapp.commands.RecipeCommand;
import com.tomerharari.recipeapp.converters.RecipeCommandToRecipe;
import com.tomerharari.recipeapp.model.Recipe;

import java.util.Set;


public interface RecipeService {
    Set<Recipe> getRecipes();
    Recipe findById(Long id);
    RecipeCommand saveRecipeCommand(RecipeCommand command);
    RecipeCommand findCommandById(Long id);
    void deleteById(Long id);
    public IngredientCommand findRecipeIngredientById(Long recipeId, Long id);

}
