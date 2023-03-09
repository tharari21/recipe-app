package com.tomerharari.recipeapp.services;

import com.tomerharari.recipeapp.commands.CategoryCommand;
import com.tomerharari.recipeapp.converters.CategoryToCategoryCommand;
import com.tomerharari.recipeapp.repositories.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryToCategoryCommand categoryConverter = new CategoryToCategoryCommand();

    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public Set<CategoryCommand> listAllCategories() {
        return StreamSupport.stream(categoryRepository.findAll()
                        .spliterator(), false).
                        map(categoryConverter::convert).
                        collect(Collectors.toSet());
    }
}
