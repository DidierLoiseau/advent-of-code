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
		return toBoolArray(input, '#');
	}

	static boolean[][] toBoolArray(List<String> input, char trueChar) {
        return input.stream()
                .map(s -> toBoolArray(s, trueChar))
                .toArray(boolean[][]::new);
    }

    static boolean[] toBoolArray(String s, char trueChar) {
        var walls = new boolean[s.length()];
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == trueChar) {
                walls[i] = true;
            }
        }
        return walls;
    }
}
