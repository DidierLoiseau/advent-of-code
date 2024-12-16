package org.sedam.aoc;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class Utils {

    public static List<String> readLines(String filename) {
        try {
            return Files.readAllLines(Paths.get("src/main/resources/" + filename));
        } catch (IOException e) {
            throw new RuntimeException("Failed to read file: " + filename, e);
        }
    }

    static boolean[][] toWallArray(List<String> input) {
        return input.stream()
                .map(Utils::toWallArray)
                .toArray(boolean[][]::new);
    }

    private static boolean[] toWallArray(String s) {
        var walls = new boolean[s.length()];
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == '#') {
                walls[i] = true;
            }
        }
        return walls;
    }
}
