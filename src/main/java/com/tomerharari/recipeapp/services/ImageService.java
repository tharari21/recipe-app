package com.tomerharari.recipeapp.services;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ImageService {
    void saveImageFile(Long id, MultipartFile file);
}
