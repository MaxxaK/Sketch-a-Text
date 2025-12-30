package com.example.renderer.core;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.List;

public class TextImageRender implements ImageRenderer{
    private final BufferedImage sourceImage;
    private final List<String> words;
    private final int gridSize;
    //smaller font size = more detailed image. fontSize should be <= gridSize
    private final Font baseFont = new Font("Times New Roman", Font.PLAIN, 24);

    public TextImageRender(BufferedImage src, List<String> text){
        sourceImage = src;
        words = text;
        gridSize = words.get(0).length();
    }

    public TextImageRender(BufferedImage src, List<String> text, int blockSize){
        sourceImage = src;
        words = text;
        this.gridSize = blockSize;
    }

    public TextImageRender(){
        sourceImage = null;
        words = null;
        gridSize = 0;
    }

    public BufferedImage getSourceImage(){
        return sourceImage;
    }

    @Override public BufferedImage render(){
        int width = sourceImage.getWidth();
        int height = sourceImage.getHeight();
        BufferedImage output = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        Graphics2D g = output.createGraphics();
        g.setColor(Color.WHITE);
        g.setFont(baseFont);
        g.fillRect(0, 0, width, height);
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        float step = gridSize * 1.2f;

        int wordIndex = 0;

        int fixedFontSize = Math.max(12, gridSize);
        Font uniformFont = baseFont.deriveFont((float) fixedFontSize);
        g.setFont(uniformFont);

        for(int y = 0; y < height; y += step){

            for(int x = 0; x < width; x += step){
                String word = words.get(wordIndex % words.size());

                Color avgColor = ImageProcessors.getAvgColor(sourceImage, x, y, gridSize);

                int fontSize = Math.max(gridSize - 2, 18);

                if (fontSize > gridSize * 2)
                    fontSize = gridSize / 2;

                Font dynamicFont = baseFont.deriveFont((float) fontSize);
                g.setFont(dynamicFont);

                FontMetrics fm = g.getFontMetrics();

                while (fm.stringWidth(word) > gridSize && fontSize > 8) {
                    fontSize--;
                    dynamicFont = baseFont.deriveFont((float) fontSize);
                    g.setFont(dynamicFont);
                    fm = g.getFontMetrics();
                }

                int wordWidth = fm.stringWidth(word);
                int wordHeight = fm.getAscent();

                g.setColor(avgColor);

                int drawX = x - wordWidth / 2;
                int drawY = y + wordHeight / 2;

                // --- SAFETY: only draw if within image bounds
                if (drawX >= 0 && drawY >= 0 && drawX + wordWidth < width && drawY + wordHeight < height) {
                    g.drawString(word, drawX, drawY);
                }

                ++wordIndex;
            }

        }

        g.dispose();
        return output;
    }
}
