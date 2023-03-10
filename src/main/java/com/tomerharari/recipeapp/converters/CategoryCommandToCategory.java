package com.tomerharari.recipeapp.converters;

import com.tomerharari.recipeapp.commands.CategoryCommand;
import com.tomerharari.recipeapp.model.Category;
import lombok.Synchronized;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class CategoryCommandToCategory implements Converter<CategoryCommand, Category> {


    @Synchronized
    @Nullable
    @Override
    public Category convert(CategoryCommand source) {
        if (source == null) return null;
        final Category category = new Category();
        category.setId(source.getId());
        category.setDescription(source.getDescription());


        return category;
    }
}
