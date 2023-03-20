package com.tomerharari.recipeapp.controllers;

import com.tomerharari.recipeapp.commands.RecipeCommand;
import com.tomerharari.recipeapp.exceptions.NotFoundException;
import com.tomerharari.recipeapp.services.CategoryService;
import com.tomerharari.recipeapp.services.ImageService;
import com.tomerharari.recipeapp.services.RecipeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.ModelAndView;

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
    public String showById(@PathVariable String id, Model model) {

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
    public String updateRecipe(@PathVariable String id, Model model) {

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
    public String deleteRecipe(@PathVariable String id) {
        log.debug("Deleting id: " + id);
        recipeService.deleteById(id);
        return "redirect:/";
    }
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NotFoundException.class)
    public ModelAndView handleNotFound(Exception exception) {
        log.error("Handling not found exception");
        log.error(exception.getMessage());
        ModelAndView mav = new ModelAndView("404error");
        mav.addObject("exception", exception);
        return mav;
    }
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ModelAndView handleInvalidTypeInUrl(MethodArgumentTypeMismatchException e) {
        log.error("Handling invalid type in URL exception");
        log.error(e.getMessage());
        ModelAndView mav = new ModelAndView("400error");
        mav.addObject("exception", e);
        return mav;
    }

}
