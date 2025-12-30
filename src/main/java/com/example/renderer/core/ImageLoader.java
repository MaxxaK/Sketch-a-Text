package com.example.renderer.core;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class ImageLoader{
    public static BufferedImage loadImage(String path){
        try {
            return ImageIO.read(new File(path));
        } catch (IOException e) {
            throw new RuntimeException("could not load image: " + path, e);
        }
    }

    public static void saveImage(BufferedImage image, String path){
        System.out.println("Using ImageLoader.saveImage from " + ImageLoader.class.getResource("ImageLoader.class"));
        try {
            ImageIO.write(image, "png", new File(path));
        } catch (IOException e) {
            throw new RuntimeException("could not save image: " + path, e);
        }
    }
}