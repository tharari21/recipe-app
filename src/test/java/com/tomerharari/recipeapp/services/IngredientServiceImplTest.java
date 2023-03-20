package com.tomerharari.recipeapp.services;

import com.tomerharari.recipeapp.commands.IngredientCommand;
import com.tomerharari.recipeapp.converters.IngredientCommandToIngredient;
import com.tomerharari.recipeapp.converters.IngredientToIngredientCommand;
import com.tomerharari.recipeapp.converters.UnitOfMeasureCommandToUnitOfMeasure;
import com.tomerharari.recipeapp.converters.UnitOfMeasureToUnitOfMeasureCommand;
import com.tomerharari.recipeapp.model.Ingredient;
import com.tomerharari.recipeapp.model.Recipe;
import com.tomerharari.recipeapp.repositories.RecipeRepository;
import com.tomerharari.recipeapp.repositories.UnitOfMeasureRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.swing.text.html.Option;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class IngredientServiceImplTest {
    IngredientService ingredientService;
    @Mock
    RecipeRepository recipeRepository;
    @Mock
    UnitOfMeasureRepository unitOfMeasureRepository;

    private final IngredientToIngredientCommand ingredientToIngredientCommand = new IngredientToIngredientCommand(new UnitOfMeasureToUnitOfMeasureCommand());
    private final IngredientCommandToIngredient ingredientCommandToIngredient = new IngredientCommandToIngredient(new UnitOfMeasureCommandToUnitOfMeasure());


    @BeforeEach
    void setUp() {
        ingredientService = new IngredientServiceImpl(recipeRepository, ingredientToIngredientCommand, ingredientCommandToIngredient, unitOfMeasureRepository);
    }

    @Test
    void findByRecipeIdAndIngredientId() {

    }
    //"Happy Path" is a term to indicate you are testing a situation where you do not expect an error.
    @Test
    void findByRecipeIdAndIngredientIdHappyPath() {
        // given
        Recipe recipe = new Recipe();
        recipe.setId("1");


        Ingredient ingredient1 = new Ingredient();
        ingredient1.setId("1");
        Ingredient ingredient2 = new Ingredient();
        ingredient2.setId("2");
        Ingredient ingredient3 = new Ingredient();
        ingredient3.setId("3");

        recipe.addIngredient(ingredient1);
        recipe.addIngredient(ingredient2);
        recipe.addIngredient(ingredient3);


        when(recipeRepository.findById(anyString())).thenReturn(Optional.of(recipe));

        IngredientCommand ingredientCommand = ingredientService.findByRecipeIdAndIngredientId("1", "3");

        assertEquals("3", ingredientCommand.getId());
        assertEquals("1", ingredientCommand.getRecipeId());
        verify(recipeRepository, times(1)).findById(anyString());


    }
    @Test
    void testSaveIngredientCommand() {
        IngredientCommand command = new IngredientCommand();
        command.setId("1");
        command.setRecipeId("1");

        Optional<Recipe> recipeOptional = Optional.of(new Recipe());

        Recipe savedRecipe = new Recipe();
        savedRecipe.setId("1");
        Ingredient savedIngredient = new Ingredient();
        savedIngredient.setId("1");
        savedRecipe.addIngredient(savedIngredient);

        when(recipeRepository.findById(anyString())).thenReturn(recipeOptional);
        when(recipeRepository.save(any())).thenReturn(savedRecipe);

        IngredientCommand savedCommand = ingredientService.saveIngredientCommand(command);

        assertEquals("1", savedCommand.getId());
        verify(recipeRepository).findById(anyString());
        verify(recipeRepository).save(any(Recipe.class));

    }
    @Test
    void testDeleteIngredientByRecipeIdAndIngredientId() {
        Recipe recipe = new Recipe();
        recipe.setId("1");
        Ingredient ingredient = new Ingredient();
        ingredient.setId("1");
        recipe.addIngredient(ingredient);

        when(recipeRepository.findById(anyString())).thenReturn(Optional.of(recipe));

        ingredientService.deleteByRecipeIdAndIngredientId("1", "1");

        verify(recipeRepository, times(1)).findById(anyString());
        verify(recipeRepository, times(1)).save(any(Recipe.class));

        assertThrows(NoSuchElementException.class, () -> ingredientService.findByRecipeIdAndIngredientId("1", "1"));
    }
}