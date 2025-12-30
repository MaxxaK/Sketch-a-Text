package com.example.renderer.service;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.renderer.core.CharImageRender;
import com.example.renderer.core.ImageProcessors;
import com.example.renderer.core.ImageRenderer;
import com.example.renderer.core.TextImageRender;
import com.example.renderer.core.TextLoader;
import com.example.renderer.model.ColorMode;
import com.example.renderer.model.RenderMode;

@Service
public class RenderService {

    /**
     * Renders an image based on the provided text and modes.
     *
     * @param file       the uploaded image file
     * @param text       the text to render onto the image
     * @param renderMode the mode of rendering (by word or by character)
     * @param colorMode  the color mode (color or grayscale)
     * @return a byte array representing the rendered PNG image
     * @throws IllegalArgumentException if the input is invalid
     * @throws RuntimeException         if an I/O error occurs during processing
     */
    public byte[] render(MultipartFile file, String text, RenderMode renderMode, ColorMode colorMode) {
        try {
            BufferedImage input = ImageIO.read(file.getInputStream());
            //validate input image
            if (input == null) {
                throw new IllegalArgumentException("Uploaded file is not a valid image");
            }
            //validate image size
            if (input.getWidth() > 4500 || input.getHeight() > 4500) {
                throw new IllegalArgumentException("Image is too large");
            }

            BufferedImage sourceImage =
                (colorMode == ColorMode.COLOR)
                    ? input
                    : ImageProcessors.convertGray(input);

            //validate text
            if (text == null || text.isBlank()) {
                throw new IllegalArgumentException("Text must not be empty");
            }
            if (text.length() > 10000) {
                throw new IllegalArgumentException("Text is too long");
            }

            //load text tokens based on render mode and TextLoader functions
            List<String> tokens = switch (renderMode) {
                case BY_WORD -> TextLoader.byWord(text);
                case BY_CHARACTER -> TextLoader.byCharacter(text);
            };

            //create appropriate renderer based on render mode. blockSize set based on trial and error testing
            ImageRenderer renderer = switch (renderMode) {
                case BY_WORD -> new TextImageRender(sourceImage, tokens, 22);
                case BY_CHARACTER -> new CharImageRender(sourceImage, tokens, 14);
            };

            BufferedImage output = renderer.render();

            ByteArrayOutputStream finalOutputStream = new ByteArrayOutputStream();
            ImageIO.write(output, "png", finalOutputStream);

            return finalOutputStream.toByteArray();

        } catch (IOException e) {
            throw new RuntimeException("Failed to process image", e);
        }
    }
}

