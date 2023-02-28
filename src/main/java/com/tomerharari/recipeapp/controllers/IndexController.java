package com.tomerharari.recipeapp.controllers;

import com.tomerharari.recipeapp.services.RecipeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Slf4j
public class IndexController {
    private final RecipeService recipeService;
    public IndexController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @RequestMapping({"/", "", "/index"})
    public String getIndexPage(Model model) {
        System.out.println("Running index controller");
        model.addAttribute("recipes", recipeService.getRecipes());
        return "index";
    }
}
