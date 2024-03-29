package com.tomerharari.recipeapp.services;

import com.tomerharari.recipeapp.commands.RecipeCommand;
import com.tomerharari.recipeapp.converters.RecipeCommandToRecipe;
import com.tomerharari.recipeapp.converters.RecipeToRecipeCommand;
import com.tomerharari.recipeapp.exceptions.NotFoundException;
import com.tomerharari.recipeapp.model.Recipe;
import com.tomerharari.recipeapp.repositories.RecipeRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RecipeServiceImplTest {
    @Autowired
    RecipeService recipeService;

    @Mock
    RecipeRepository recipeRepository;
    @Mock
    RecipeToRecipeCommand recipeToRecipeCommand;

    @Mock
    RecipeCommandToRecipe recipeCommandToRecipe;

    @BeforeEach
    void setUp() {
        // Initializes mock objects (in this case repository
        recipeService = new RecipeServiceImpl(recipeRepository, recipeCommandToRecipe, recipeToRecipeCommand);

    }

    @Test
    void getRecipes()  {
        Recipe newRecipe = new Recipe();
        HashSet recipesData = new HashSet();
        recipesData.add(newRecipe);

        when(recipeRepository.findAll()).thenReturn(recipesData);
        System.out.println("SIZE!!!!" + recipesData.size());

        Set<Recipe> recipes = recipeService.getRecipes();
        Assertions.assertEquals(1, recipes.size());
        verify(recipeRepository, times(1)).findAll();
    }
    @Test
    public void testDeleteById() {
        // given
        String idToDelete = "2";

        // when
        recipeService.deleteById(idToDelete);

        // then
        verify(recipeRepository, times(1)).deleteById(anyString());
    }
    @Test
    public void getRecipeById() {
        Recipe recipe = new Recipe();
        recipe.setId("1");
        Optional<Recipe> recipeOptional = Optional.of(recipe);

        when(recipeRepository.findById(anyString())).thenReturn(recipeOptional);

        Recipe returnedRecipe = recipeService.findById("1");
        assertNotNull(returnedRecipe);
        verify(recipeRepository).findById(anyString());


    }
    @Test
    public void getRecipeCommandByIdTest() throws Exception {
        Recipe recipe = new Recipe();
        recipe.setId("1");
        Optional<Recipe> recipeOptional = Optional.of(recipe);

        when(recipeRepository.findById(anyString())).thenReturn(recipeOptional);

        RecipeCommand recipeCommand = new RecipeCommand();
        recipeCommand.setId("1");

        when(recipeToRecipeCommand.convert(any())).thenReturn(recipeCommand);

        RecipeCommand commandById = recipeService.findCommandById("1");

        assertNotNull(commandById);
        verify(recipeRepository, times(1)).findById(anyString());
        verify(recipeRepository, never()).findAll();
    }

    @Test()
    public void getRecipeByIdNotFound() {
        Optional<Recipe> recipeOptional = Optional.empty();

        when(recipeRepository.findById(anyString())).thenReturn(recipeOptional);

        // should cause error
        NotFoundException notFoundException = assertThrows(
                NotFoundException.class, () -> recipeService.findById("1"),
                "Expected exception to throw an error. But it didn't"
        );

        // then
        assertTrue(notFoundException.getMessage().contains("Recipe Not Found"));


    }
}