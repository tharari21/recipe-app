package com.tomerharari.recipeapp.controllers;

import com.tomerharari.recipeapp.commands.RecipeCommand;
import com.tomerharari.recipeapp.services.ImageService;
import com.tomerharari.recipeapp.services.RecipeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RequestMapping("/recipe")
@Controller
public class RecipeController {
    private final RecipeService recipeService;
    private final ImageService imageService;

    public RecipeController(RecipeService recipeService, ImageService imageService) {
        this.recipeService = recipeService;
        this.imageService = imageService;
    }

    @GetMapping("/{id}/show")
    public String showById(@PathVariable Long id, Model model) {
        model.addAttribute(recipeService.findById(id));
        return "recipe/show";
    }
    @GetMapping("/new")
    public String newRecipe(Model model) {
        model.addAttribute("recipe", new RecipeCommand());
        return "recipe/recipeform";
    }
    @GetMapping("/{id}/update")
    public String updateRecipe(@PathVariable Long id, Model model) {
        model.addAttribute("recipe", recipeService.findCommandById(id));
        return "recipe/recipeform";
    }
    @PostMapping({"/", ""})
    public String saveOrUpdate(@ModelAttribute RecipeCommand command) {

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
