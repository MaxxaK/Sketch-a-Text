package com.example.renderer.core;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TextLoader {
    //legacy method for loading text from file paths
    public static List<String> loadText(String path){
        try {
            String text = Files.readString(Paths.get(path));
            return Arrays.asList(text.split("\\s+"));
        } catch (IOException e) {
            throw new RuntimeException("could not load text file" + path);
        }
    }

    //legacy method for loading text from file paths
    public static List<String> loadInputText(String input){
        return splitToWords(input);
    }

    //legacy method for loading text from file paths
    public static List<String> loadTextAsChar(String path){
        try {
            String text = Files.readString(Paths.get(path));
            return Arrays.asList(text.split(""));
        } catch (IOException e) {
            throw new RuntimeException("could not load text file" + path);
        }
    }

    //legacy method for loading text from file paths
    public static List<String>loadInputTextAsChar(String input){
        return splitToChar(input);
    }

    //legacy method for loading text from file paths
    private static List<String> splitToWords(String input){
        String[] tokens = input.split("\\s+");
        return Arrays.asList(tokens);
    }

    //legacy method for loading text from file paths
    private static List<String> splitToChar(String input){
        List<String> chars = new ArrayList<>();
        for(char c : input.toCharArray()){
            chars.add(String.valueOf(c));
        }
        return chars;
    }

    /**
     * Splits the input text into a list of words.
     * @param input the input text
     * @return a list of words
     */
    public static List<String> byWord(String input) {
        return Arrays.stream(input.split("\\s+"))
            .filter(s -> !s.isBlank())
            .toList();
    }

    /**
     * Splits the input text into a list of characters.
     * @param input the input text
     * @return a list of characters
     */
    public static List<String> byCharacter(String input) {
        return input.chars()
            .mapToObj(c -> String.valueOf((char) c))
            .filter(s -> !s.isBlank())
            .toList();
    }
}
