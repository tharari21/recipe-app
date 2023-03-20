package com.tomerharari.recipeapp.controllers;

import com.tomerharari.recipeapp.commands.IngredientCommand;
import com.tomerharari.recipeapp.commands.RecipeCommand;
import com.tomerharari.recipeapp.commands.UnitOfMeasureCommand;
import com.tomerharari.recipeapp.model.Recipe;
import com.tomerharari.recipeapp.services.IngredientService;
import com.tomerharari.recipeapp.services.RecipeService;
import com.tomerharari.recipeapp.services.UnitOfMeasureServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/recipe")
@Slf4j
@Controller
public class IngredientController {
    private final RecipeService recipeService;
    private final IngredientService ingredientService;
    private final UnitOfMeasureServiceImpl unitOfMeasureService;

    public IngredientController(RecipeService recipeService, IngredientService ingredientService, UnitOfMeasureServiceImpl unitOfMeasureService) {
        this.recipeService = recipeService;
        this.ingredientService = ingredientService;
        this.unitOfMeasureService = unitOfMeasureService;
    }

    @GetMapping("/{recipeId}/ingredients")
    public String listIngredients(@PathVariable String recipeId, Model model) {
        log.debug("Getting ingredient list for recipe " + recipeId);
        model.addAttribute("recipe", recipeService.findCommandById(recipeId));

        return "recipe/ingredient/list";
    }
    @GetMapping("/{recipeId}/ingredient/{id}/show")
    public String showById(@PathVariable String recipeId,
                           @PathVariable String id, Model model) {
        model.addAttribute("ingredient", ingredientService.findByRecipeIdAndIngredientId(recipeId, id));
        return "recipe/ingredient/show";
    }
    @GetMapping("/{recipeId}/ingredient/{id}/update")
    public String updateRecipeIngredient(@PathVariable String recipeId,
                               @PathVariable String id, Model model) {
        model.addAttribute("ingredient", ingredientService.findByRecipeIdAndIngredientId(recipeId, id));
        model.addAttribute("uomList", unitOfMeasureService.listAllUoms());
        return "recipe/ingredient/ingredientform";
    }
    @PostMapping("/{recipeId}/ingredient")
    public String saveOrUpdate(@ModelAttribute IngredientCommand ingredientCommand) {
        System.out.println("IDDDDDDD: " + ingredientCommand.getId());
        IngredientCommand savedCommand = ingredientService.saveIngredientCommand(ingredientCommand);

        log.debug("saved recipe id: " + savedCommand.getRecipeId());
        log.debug("saved ingredient id: " + savedCommand.getId());

        return "redirect:/recipe/" + savedCommand.getRecipeId() + "/ingredient/" + savedCommand.getId() + "/show";
    }
    @GetMapping("/{recipeId}/ingredient/new")
    public String newIngredient(@PathVariable String recipeId, Model model) {
        //make sure we have a good id value
        RecipeCommand recipeCommand = recipeService.findCommandById(recipeId);
        //todo raise exception if null


        IngredientCommand command = new IngredientCommand();
        command.setRecipeId(recipeId);
        model.addAttribute("ingredient", command );

        // Need to initialize uom so that it's not null
        command.setUom(new UnitOfMeasureCommand());
        model.addAttribute("uomList", unitOfMeasureService.listAllUoms());
        return "recipe/ingredient/ingredientform";
    }
    @GetMapping("/{recipeId}/ingredient/{id}/delete")
    public String deleteIngredient(@PathVariable String recipeId, @PathVariable String id) {
        log.debug("deleting ingredient id:" + id);
        ingredientService.deleteByRecipeIdAndIngredientId(recipeId, id);
        return "redirect:/recipe/"+ recipeId + "/ingredients";
    }
}
