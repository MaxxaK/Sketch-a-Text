package com.example.renderer.core;

import java.awt.Color;
import java.awt.image.BufferedImage;

public class ImageProcessors {
    public static BufferedImage convertGray(BufferedImage img){
        int width = img.getWidth();
        int height = img.getHeight();
        BufferedImage grayscale = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int rgb = img.getRGB(x, y);
                int r = (rgb >> 16) & 0xFF;
                int g = (rgb >> 8) & 0xFF;
                int b = rgb & 0xFF;
                int grayLevel = (int)(0.299 * r + 0.587 * g + 0.114 * b); //luminance formula
                int grayRGB = (grayLevel << 16) | (grayLevel << 8) | grayLevel;
                grayscale.setRGB(x, y, grayRGB);
            }
        }
        return grayscale;
    }

    /**
     * Calculates the average brightness of a block in the image.
     * @param image the source image
     * @param startX the starting x coordinate of the block
     * @param startY the starting y coordinate of the block
     * @param blockSize the size of the block
     * @return the average brightness of the block
     */
    public static double getAvgBrightness(BufferedImage image, int startX, int startY, int blockSize){
        int endX = Math.min(startX + blockSize, image.getWidth());
        int endY = Math.min(startY + blockSize, image.getHeight());
        long sum = 0;
        int count = 0;

        for(int y = startY; y < endY; ++y){
            for(int x = startX; x < endX; ++x){
                int rgb = image.getRGB(x, y) & 0xFF;
                sum += rgb;
                ++count;
            }
        }
        return (double) sum / count;
    }

    /**
     * Calculates the average color of a block in the image.
     * @param image the source image
     * @param x the starting x coordinate of the block
     * @param y the starting y coordinate of the block
     * @param blockSize the size of the block
     * @return the average color of the block
     */
    public static Color getAvgColor(BufferedImage image, int x, int y, int blockSize){
        int width = image.getWidth();
        int height = image.getHeight();

        long sumR = 0, sumG = 0, sumB = 0;
        int count = 0;

        for (int i = x; i < x + blockSize && i < width; i++) {
            for (int j = y; j < y + blockSize && j < height; j++) {
                int rgb = image.getRGB(i, j);
                Color c = new Color(rgb);
                sumR += c.getRed();
                sumG += c.getGreen();
                sumB += c.getBlue();
                count++;
            }
        }
        if (count == 0) return Color.WHITE; // fallback
        return new Color((int)(sumR / count), (int)(sumG / count), (int)(sumB / count));
    }
}
