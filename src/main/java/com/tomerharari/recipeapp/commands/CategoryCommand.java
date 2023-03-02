package com.tomerharari.recipeapp.commands;

import com.tomerharari.recipeapp.converters.RecipeCommandToRecipe;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Setter
@Getter
@NoArgsConstructor
public class CategoryCommand {
    private Long id;
    private String description;
    private Set<RecipeCommand> recipes;
}
