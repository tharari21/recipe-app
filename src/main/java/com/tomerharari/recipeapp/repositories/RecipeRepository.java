package com.tomerharari.recipeapp.repositories;

import com.tomerharari.recipeapp.model.Recipe;
import org.springframework.data.repository.CrudRepository;

public interface RecipeRepository extends CrudRepository<Recipe, String> {

}
