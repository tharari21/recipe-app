package com.tomerharari.recipeapp.controllers;

import com.tomerharari.recipeapp.commands.IngredientCommand;
import com.tomerharari.recipeapp.commands.RecipeCommand;
import com.tomerharari.recipeapp.model.Ingredient;
import com.tomerharari.recipeapp.model.Recipe;
import com.tomerharari.recipeapp.repositories.UnitOfMeasureRepository;
import com.tomerharari.recipeapp.services.IngredientService;
import com.tomerharari.recipeapp.services.RecipeService;
import com.tomerharari.recipeapp.services.UnitOfMeasureService;
import com.tomerharari.recipeapp.services.UnitOfMeasureServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.HashSet;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class IngredientControllerTest {
    @Mock
    RecipeService recipeService;
    @Mock
    IngredientService ingredientService;
    @Mock
    UnitOfMeasureServiceImpl uomService;
    MockMvc mockMvc;
    IngredientController controller;
    @BeforeEach
    void setUp() {
        controller = new IngredientController(recipeService, ingredientService, uomService);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    public void testListIngredients() throws Exception {
        RecipeCommand recipeCommand = new RecipeCommand();
        when(recipeService.findCommandById(anyLong())).thenReturn(recipeCommand);

        mockMvc.perform(get("/recipe/1/ingredients"))
                .andExpect(status().isOk())
                .andExpect(view().name("recipe/ingredient/list"))
                .andExpect(model().attributeExists("recipe"));

        verify(recipeService, times(1)).findCommandById(anyLong());

    }
    @Test
    public void testShowIngredient() throws Exception {
        IngredientCommand ingredientCommand = new IngredientCommand();
        when(ingredientService.findByRecipeIdAndIngredientId(anyLong(), anyLong())).thenReturn(ingredientCommand);

        mockMvc.perform(get("/recipe/1/ingredient/2/show"))
                .andExpect(status().isOk())
                .andExpect(view().name("recipe/ingredient/show"))
                .andExpect(model().attributeExists("ingredient"));
    }
    @Test
    public void testUpdateIngredientForm() throws Exception {
        IngredientCommand command = new IngredientCommand();
        command.setId(1L);
        command.setRecipeId(1L);

        when(ingredientService.findByRecipeIdAndIngredientId(anyLong(), anyLong())).thenReturn(command);
        when(uomService.listAllUoms()).thenReturn(new HashSet<>());
        mockMvc.perform(get("/recipe/1/ingredient/1/update"))
                .andExpect(status().isOk())
                .andExpect(view().name("recipe/ingredient/ingredientform"))
                .andExpect(model().attributeExists("ingredient"))
                .andExpect(model().attributeExists("uomList"));
    }
    @Test
    public void testGetNewIngredientForm() throws Exception {
        RecipeCommand recipeCommand = new RecipeCommand();
        recipeCommand.setId(1L);

        when(recipeService.findCommandById(anyLong())).thenReturn(recipeCommand);
        when(uomService.listAllUoms()).thenReturn(new HashSet<>());

        mockMvc.perform(get("/recipe/1/ingredient/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("recipe/ingredient/ingredientform"))
                .andExpect(model().attributeExists("ingredient"))
                .andExpect(model().attributeExists("uomList"));

        verify(recipeService).findCommandById(anyLong());
    }
    @Test
    public void testSaveOrUpdate() throws Exception {

        IngredientCommand command = new IngredientCommand();
        command.setId(1L);
        command.setRecipeId(1L);

        when(ingredientService.saveIngredientCommand(any())).thenReturn(command);

        mockMvc.perform(post("/recipe/1/ingredient")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/recipe/1/ingredient/1/show"));
    }
    @Test
    public void testDeleteIngredient() throws Exception{


        mockMvc.perform(get("/recipe/1/ingredient/1/delete"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/recipe/1/ingredients"));

        verify(ingredientService, times(1)).deleteByRecipeIdAndIngredientId(anyLong(), anyLong());

    }

}

