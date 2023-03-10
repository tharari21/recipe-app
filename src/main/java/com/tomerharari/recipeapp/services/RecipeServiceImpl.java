package com.tomerharari.recipeapp.services;

import com.tomerharari.recipeapp.commands.RecipeCommand;
import com.tomerharari.recipeapp.commands.IngredientCommand;
import com.tomerharari.recipeapp.converters.RecipeCommandToRecipe;
import com.tomerharari.recipeapp.converters.RecipeToRecipeCommand;
import com.tomerharari.recipeapp.exceptions.NotFoundException;
import com.tomerharari.recipeapp.model.Recipe;
import com.tomerharari.recipeapp.repositories.RecipeRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Slf4j
@Service
public class RecipeServiceImpl implements RecipeService {
    private final RecipeRepository recipeRepository;
    private final RecipeCommandToRecipe recipeCommandToRecipe;
    private final RecipeToRecipeCommand recipeToRecipeCommand;



    public RecipeServiceImpl(RecipeRepository recipeRepository, RecipeCommandToRecipe recipeCommandToRecipe, RecipeToRecipeCommand recipeToRecipeCommand) {
        this.recipeRepository = recipeRepository;
        this.recipeCommandToRecipe = recipeCommandToRecipe;
        this.recipeToRecipeCommand = recipeToRecipeCommand;
    }

    @Override
    public Set<Recipe> getRecipes() {
        log.debug("I'm in the service");
        Set<Recipe> recipes = new HashSet<>();

        recipeRepository.findAll().iterator().forEachRemaining(recipes::add);
        return recipes;
    }
    @Override
    public Recipe findById(Long id) {
        Optional<Recipe> recipe = recipeRepository.findById(id);
        return recipe.orElseThrow(() -> new NotFoundException("Recipe Not Found for ID Value: " + id));
    }

    // Make it transactional because we will be c
    @Override
    @Transactional
    public RecipeCommand saveRecipeCommand(RecipeCommand command) {
        Recipe convertedRecipeCommand = recipeCommandToRecipe.convert(command);

        Recipe savedConvertedRecipeCommand = recipeRepository.save(convertedRecipeCommand);
        log.debug("Saved RecipeId: " + savedConvertedRecipeCommand.getId() );
        return recipeToRecipeCommand.convert(savedConvertedRecipeCommand);

    }
    @Override
    @Transactional
    public RecipeCommand findCommandById(Long id) {
        return recipeToRecipeCommand.convert(findById(id));
    }
    @Override
    public void deleteById(Long id) {
        recipeRepository.deleteById(id);
    }
    @Override
    public IngredientCommand findRecipeIngredientById(Long recipeId, Long id) {
        return findCommandById(recipeId).getIngredients().stream().filter(ingredientCommand -> ingredientCommand.getId() == id).findFirst().orElse(null);

    }
}
