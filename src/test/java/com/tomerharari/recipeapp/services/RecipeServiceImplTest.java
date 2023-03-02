package com.tomerharari.recipeapp.services;

import com.tomerharari.recipeapp.model.Recipe;
import com.tomerharari.recipeapp.repositories.RecipeRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RecipeServiceImplTest {
    RecipeService recipeService;

    @Mock
    RecipeRepository recipeRepository;

    @BeforeEach
    void setUp() {

        recipeService = new RecipeServiceImpl(recipeRepository);

    }
    @Test
    void getRecipeById()  {
        Long recipeId = 1L;
        Recipe recipe = new Recipe();
        recipe.setId(recipeId);
        Optional<Recipe> recipeOptional = Optional.of(recipe);

        when(recipeRepository.findById(anyLong())).thenReturn(recipeOptional);

        Recipe returnedRecipe = recipeService.findById(recipeId);

        assertNotNull( returnedRecipe);
        verify(recipeRepository ).findById(anyLong());
        verify(recipeRepository, never()).findAll();

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
}