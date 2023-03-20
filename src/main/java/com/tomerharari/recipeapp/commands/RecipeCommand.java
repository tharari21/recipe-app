package com.tomerharari.recipeapp.commands;

import com.tomerharari.recipeapp.model.Category;
import com.tomerharari.recipeapp.model.Difficulty;
import com.tomerharari.recipeapp.model.Ingredient;
import com.tomerharari.recipeapp.model.Notes;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class RecipeCommand {
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
    private Set<IngredientCommand> ingredients = new HashSet<>();
    private NotesCommand notes;

    private Set<CategoryCommand> categories = new HashSet<>();
}
