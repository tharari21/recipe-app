package com.tomerharari.recipeapp.services;

import com.tomerharari.recipeapp.model.Recipe;

import java.util.Set;


public interface RecipeService {
    Set<Recipe> getRecipes();
    Recipe findById(Long id);

}
