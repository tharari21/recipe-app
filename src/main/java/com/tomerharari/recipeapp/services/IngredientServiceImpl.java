package com.tomerharari.recipeapp.services;

import com.tomerharari.recipeapp.commands.IngredientCommand;
import com.tomerharari.recipeapp.converters.IngredientCommandToIngredient;
import com.tomerharari.recipeapp.converters.IngredientToIngredientCommand;
import com.tomerharari.recipeapp.model.Ingredient;
import com.tomerharari.recipeapp.model.Recipe;
import com.tomerharari.recipeapp.repositories.RecipeRepository;
import com.tomerharari.recipeapp.repositories.UnitOfMeasureRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.Optional;
@Slf4j
@Service
public class IngredientServiceImpl implements IngredientService {
    private final RecipeRepository recipeRepository;
    private final IngredientToIngredientCommand ingredientToIngredientCommand;
    private final IngredientCommandToIngredient ingredientCommandToIngredient;
    private UnitOfMeasureRepository unitOfMeasureRepository;

    public IngredientServiceImpl(RecipeRepository recipeRepository,
                                 IngredientToIngredientCommand ingredientToIngredientCommand,
                                 IngredientCommandToIngredient ingredientCommandToIngredient,
                                 UnitOfMeasureRepository unitOfMeasureRepository) {
        this.recipeRepository = recipeRepository;
        this.ingredientToIngredientCommand = ingredientToIngredientCommand;
        this.ingredientCommandToIngredient = ingredientCommandToIngredient;
        this.unitOfMeasureRepository = unitOfMeasureRepository;
    }

    @Override
    public IngredientCommand findByRecipeIdAndIngredientId(Long recipeId, Long ingredientId) {
        Optional<Recipe> optionalRecipe = recipeRepository.findById(recipeId);

        if (!optionalRecipe.isPresent()) {
            log.debug("No recipe with id " + recipeId);
        }
        Optional<IngredientCommand> ingredientCommandOptional = optionalRecipe.get().getIngredients().stream()
                .filter(ingredient -> ingredient.getId().equals(ingredientId))
                .map(ingredient -> ingredientToIngredientCommand.convert(ingredient)).findFirst();
        if(!ingredientCommandOptional.isPresent()){
            //todo impl error handling
            log.error("Ingredient id not found: " + ingredientId);
        }
        return ingredientCommandOptional.get();
    }

    @Override
    @Transactional
    public IngredientCommand saveIngredientCommand(IngredientCommand command) {
        Optional<Recipe> recipeOptional = recipeRepository.findById(command.getRecipeId());

        if (!recipeOptional.isPresent()) {
            //todo toss error if not found!
            log.error("Recipe not found for id " + command.getRecipeId());
            return new IngredientCommand();
        } else {
            Recipe recipe = recipeOptional.get();

            // check if ingredient already exists
            Optional<Ingredient> ingredientOptional = recipe.
                    getIngredients()
                    .stream()
                    .filter(ingredient -> ingredient.getId().equals(command.getId()))
                    .findFirst();
            //  If it exists, update it
            if (ingredientOptional.isPresent()) {
                Ingredient ingredient = ingredientOptional.get();
                ingredient.setDescription(command.getDescription());
                ingredient.setAmount(command.getAmount());
                ingredient.setUom(unitOfMeasureRepository
                        .findById(command.getUom().getId())
                        .orElseThrow(() -> new RuntimeException())); // todo better error handling
            } else {
                // add new ingredient because it does not exist yet - not sure how this persists though
                // I think because cascade is .ALL so therefore if the parent is persisted it persists children
                Ingredient ingredient = ingredientCommandToIngredient.convert(command);
                ingredient.setRecipe(recipe);
                recipe.addIngredient(ingredient);
            }
            // save the updated recipe that we added ingredient to in the database
            Recipe savedRecipe = recipeRepository.save(recipe);

            // Get the persisted ingredient from saved recipe -- command.getId() will only have a value if the ingredient
            // already existed in the database.
            Optional<Ingredient> savedIngredientOptional = savedRecipe.getIngredients().stream()
                    .filter(recipeIngredient -> recipeIngredient.getId().equals(command.getId()))
                    .findFirst();
            // in the case that we added a new ingredient that wasn't persisted yet, command does not have an id yet so
            // We cannot get by id so in that case we get by description, amount and UOM which is not the best
            // method because we did not specify that these should be unique but for now it's cool we will address later
            // Basically we saved new ingredient but there is no way for us to know the id of that new ingredient
            // With the way things are set up so we can only get the new ingredient by matching it by its description, amount and uom
            if (!savedIngredientOptional.isPresent()) {
                savedIngredientOptional = savedRecipe.getIngredients().stream()
                        .filter(recipeIngredient -> recipeIngredient.getDescription().equals(command.getDescription()))
                        .filter(recipeIngredient -> recipeIngredient.getAmount().equals(command.getAmount()))
                        .filter(recipeIngredient -> recipeIngredient.getUom().getId().equals(command.getUom().getId()))
                        .findFirst();
            }
            // todo check for fail
            return ingredientToIngredientCommand.convert(savedIngredientOptional.get());

        }


    }
    @Override
    @Transactional
    public void deleteByRecipeIdAndIngredientId(Long recipeId, Long ingredientId) {
        Optional<Recipe> recipeOptional = recipeRepository.findById(recipeId);
        if (recipeOptional.isPresent()) {
            Recipe recipe = recipeOptional.get();
            log.debug("Found recipe!");

            Optional<Ingredient> ingredientOptional = recipe.getIngredients().stream()
                    .filter(ingredient -> ingredient.getId().equals(ingredientId)).findFirst();
            if (ingredientOptional.isPresent()) {
                log.debug("Found ingredient");
                Ingredient ingredientToDelete = ingredientOptional.get();
                ingredientToDelete.setRecipe(null);
                recipe.getIngredients().remove(ingredientToDelete);
                recipeRepository.save(recipe);
            }
            else {
                log.debug("Ingredient Id Not found. Id:" + recipeId);
            }

        } else {
            log.debug("Recipe Id Not found. Id:" + recipeId);
        }
    }
}
