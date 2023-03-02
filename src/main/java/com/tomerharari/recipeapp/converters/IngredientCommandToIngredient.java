package com.tomerharari.recipeapp.converters;

import com.tomerharari.recipeapp.commands.IngredientCommand;
import com.tomerharari.recipeapp.commands.UnitOfMeasureCommand;
import com.tomerharari.recipeapp.model.Ingredient;
import lombok.Synchronized;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

@Component
public class IngredientCommandToIngredient implements Converter<IngredientCommand, Ingredient> {
    private final UnitOfMeasureCommandToUnitOfMeasure uomConverter;

    public IngredientCommandToIngredient(UnitOfMeasureCommandToUnitOfMeasure uomConverter ) {
        this.uomConverter =  uomConverter;
    }

    @Synchronized
    @Nullable
    @Override
    public Ingredient convert(IngredientCommand source) {
        if (source == null) return null;
        final Ingredient ingredient = new Ingredient();
        ingredient.setId(source.getId());
        ingredient.setUom(uomConverter.convert(source.getUom()));
        ingredient.setAmount(source.getAmount());
        ingredient.setDescription(source.getDescription());
        return ingredient;
    }
}
