package com.tomerharari.recipeapp.services;

import com.tomerharari.recipeapp.commands.CategoryCommand;

import java.util.Set;

public interface CategoryService {
    Set<CategoryCommand> listAllCategories();
}
