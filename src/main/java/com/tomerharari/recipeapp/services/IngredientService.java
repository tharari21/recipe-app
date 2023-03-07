package com.tomerharari.recipeapp.services;

import com.tomerharari.recipeapp.commands.IngredientCommand;

public interface IngredientService {
    IngredientCommand findByRecipeIdAndIngredientId(Long recipeId, Long id);

    IngredientCommand saveIngredientCommand(IngredientCommand ingredientCommand);
    void deleteByRecipeIdAndIngredientId(Long recipeId, Long ingredientId);
}
