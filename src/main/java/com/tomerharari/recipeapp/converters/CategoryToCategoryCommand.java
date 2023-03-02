package com.tomerharari.recipeapp.converters;

import com.tomerharari.recipeapp.commands.CategoryCommand;
import com.tomerharari.recipeapp.commands.RecipeCommand;
import com.tomerharari.recipeapp.model.Category;
import lombok.Synchronized;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class CategoryToCategoryCommand implements Converter<Category, CategoryCommand> {



    @Synchronized
    @Nullable
    @Override
    public CategoryCommand convert(Category source) {
        if (source == null) return null;
        CategoryCommand categoryCommand = new CategoryCommand();
        categoryCommand.setId(source.getId());
        categoryCommand.setDescription(source.getDescription());
        return categoryCommand;
    }
}
