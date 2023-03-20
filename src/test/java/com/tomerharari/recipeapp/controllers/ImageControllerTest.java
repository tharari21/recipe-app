package com.tomerharari.recipeapp.controllers;

import com.tomerharari.recipeapp.commands.RecipeCommand;
import com.tomerharari.recipeapp.model.Recipe;
import com.tomerharari.recipeapp.services.ImageService;
import com.tomerharari.recipeapp.services.RecipeService;
import jdk.jfr.Enabled;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@ExtendWith(MockitoExtension.class)
public class ImageControllerTest {
    MockMvc mockMvc;
    ImageController controller;
    @Mock
    ImageService imageService;
    @Mock
    RecipeService recipeService;


    @BeforeEach
    void setUp() {

        controller = new ImageController(recipeService, imageService);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    public void getImageForm() throws Exception {
        RecipeCommand recipeCommand = new RecipeCommand();
        recipeCommand.setId("1");

        when(recipeService.findCommandById(anyString())).thenReturn(recipeCommand);

        mockMvc.perform(get("/recipe/1/image"))
                .andExpect(status().isOk())
                .andExpect(view().name("recipe/imageuploadform"))
                .andExpect(model().attributeExists("recipe"));

        verify(recipeService).findCommandById(anyString());
    }
    @Test
    public void handleImagePost() throws Exception {
        MockMultipartFile multipartFile =
                new MockMultipartFile("imagefile", "testing.txt", "text/plain",
                        "Spring Framework Guru".getBytes());

        mockMvc.perform(multipart("/recipe/1/image").file(multipartFile))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", "/recipe/1/show"));

        verify(imageService, times(1)).saveImageFile(anyString(), any());

    }
    @Test
    public void renderImageFromDB() throws Exception {
        RecipeCommand command = new RecipeCommand();
        command.setId("1");

        byte[] byteArr = "Fake image text".getBytes();
        Byte[] bytesBoxed = new Byte[byteArr.length];
        int i = 0;
        for (byte b: byteArr) {
            bytesBoxed[i++] = b;
        }
        command.setImage(bytesBoxed);

        when(recipeService.findCommandById(anyString())).thenReturn(command);

        MockHttpServletResponse response = mockMvc.perform(get("/recipe/1/recipeimage"))
                .andExpect(status().isOk())
                .andReturn().getResponse();

        byte[] responseBytes = response.getContentAsByteArray();

        assertEquals(byteArr.length, responseBytes.length);
    }
}

