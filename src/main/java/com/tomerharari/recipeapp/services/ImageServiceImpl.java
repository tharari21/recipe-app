package com.tomerharari.recipeapp.services;


import com.tomerharari.recipeapp.model.Recipe;
import com.tomerharari.recipeapp.repositories.RecipeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;


@Service
@Slf4j
public class ImageServiceImpl implements ImageService {
    private final RecipeRepository recipeRepository;

    public ImageServiceImpl(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    @Override
    public void saveImageFile(Long id, MultipartFile file) {
        log.debug("Saving a file");
        Optional<Recipe> recipeOptional = recipeRepository.findById(id);

        if (recipeOptional.isPresent()) {
            Recipe recipe = recipeOptional.get();
            try {
                // file.getBytes returns primitive byte array but we want wrapper class Byte array
                // so we iterate over byte array and transfer to Byte array
                Byte[] byteObjects = new Byte[file.getBytes().length];
                int i = 0;

                for (byte b : file.getBytes()) {
                    byteObjects[i++] = b;
                }

                recipe.setImage(byteObjects);

                recipeRepository.save(recipe);
            } catch (IOException e) {
                //todo handle better
                log.error("Error occurred", e);
                e.printStackTrace();
            }
        }
    }
}
