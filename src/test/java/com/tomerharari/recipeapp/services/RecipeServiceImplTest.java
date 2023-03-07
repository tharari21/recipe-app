package com.tomerharari.recipeapp.services;

import com.tomerharari.recipeapp.converters.RecipeCommandToRecipe;
import com.tomerharari.recipeapp.converters.RecipeToRecipeCommand;
import com.tomerharari.recipeapp.model.Recipe;
import com.tomerharari.recipeapp.repositories.RecipeRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashSet;
import java.util.Set;

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
        Long idToDelete = 2L;

        // when
        recipeService.deleteById(idToDelete);

        // then
        verify(recipeRepository, times(1)).deleteById(anyLong());
    }
}