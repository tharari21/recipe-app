package com.tomerharari.recipeapp.controllers;

import com.tomerharari.recipeapp.commands.RecipeCommand;
import com.tomerharari.recipeapp.services.CategoryService;
import com.tomerharari.recipeapp.services.ImageService;
import com.tomerharari.recipeapp.services.RecipeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@RequestMapping("/recipe")
@Controller
public class RecipeController {
    private final RecipeService recipeService;
    private final ImageService imageService;

    private final CategoryService categoryService;
    public RecipeController(RecipeService recipeService, ImageService imageService, CategoryService categoryService) {
        this.recipeService = recipeService;
        this.imageService = imageService;
        this.categoryService = categoryService;
    }

    @GetMapping("/{id}/show")
    public String showById(@PathVariable Long id, Model model) {
        model.addAttribute(recipeService.findById(id));
        return "recipe/show";
    }
    @GetMapping("/new")
    public String newRecipe(Model model) {
        model.addAttribute("recipe", new RecipeCommand());
        model.addAttribute("categories", categoryService.listAllCategories());
        return "recipe/recipeform";
    }
    @GetMapping("/{id}/update")
    public String updateRecipe(@PathVariable Long id, Model model) {
        model.addAttribute("recipe", recipeService.findCommandById(id));
        model.addAttribute("categories", categoryService.listAllCategories());
        return "recipe/recipeform";
    }
    @PostMapping({"/", ""})
    public String saveOrUpdate(RecipeCommand command) {

        RecipeCommand savedRecipeCommand = recipeService.saveRecipeCommand(command);

        return "redirect:/recipe/"+ savedRecipeCommand.getId() + "/show"; // tells spring mvc to redirect to new url
    }
    @GetMapping("/{id}/delete")
    public String deleteRecipe(@PathVariable Long id) {
        log.debug("Deleting id: " + id);
        recipeService.deleteById(id);
        return "redirect:/";
    }

}
