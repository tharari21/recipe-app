package com.tomerharari.recipeapp.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
@Entity
public class Recipe {
    // IDENTITY relies on the underlying persistence framework to generate value for us
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    private String description;
    private int prepTime;
    private int cookTime;
    private int servings;
    private String source;
    private String url;
    @Lob
    private String directions;
    @Enumerated(EnumType.STRING)
    private Difficulty difficulty;
    @Lob
    private Byte[] image;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "recipe")
    private Set<Ingredient> ingredients = new HashSet<>();
    @OneToOne(cascade = CascadeType.ALL)
    private Notes notes;
    @ManyToMany
    @JoinTable(name="recipe_category",
        joinColumns = @JoinColumn(name="recipe_id"),
        inverseJoinColumns = @JoinColumn(name="category_id"))
    private Set<Category> categories = new HashSet<>();





    public Recipe addIngredient(Ingredient ingredient) {
        ingredient.setRecipe(this);
        getIngredients().add(ingredient);
        return this;
    }


}
