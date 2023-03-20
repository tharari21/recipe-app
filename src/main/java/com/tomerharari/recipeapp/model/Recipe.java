package com.tomerharari.recipeapp.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
public class Recipe {
    // IDENTITY relies on the underlying persistence framework to generate value for us

    private String id;
    private String description;
    private int prepTime;
    private int cookTime;
    private int servings;
    private String source;
    private String url;

    private String directions;
    private Difficulty difficulty;
    private Byte[] image;
    private Set<Ingredient> ingredients = new HashSet<>();
    private Notes notes;
    private Set<Category> categories = new HashSet<>();





    public Recipe addIngredient(Ingredient ingredient) {
        ingredient.setRecipe(this);
        getIngredients().add(ingredient);
        return this;
    }


}
