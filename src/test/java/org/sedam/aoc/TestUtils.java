package org.sedam.aoc;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class TestUtils {

    public static List<String> readInputFiles(int day, int part, String prefix) {
        String fileName = "day" + day + "/" + prefix + "input-" + part + ".txt";
        List<String> content = Utils.readLines(fileName);
        if (content.isEmpty()) {
            if (part > 1) {
                return readInputFiles(day, 1, prefix);
            }
            throw new RuntimeException("Input file '" + fileName + "' is empty");
        }
        return content;
    }

    public static String readExpectedResult(int day, int part) {
        try {
            String pathName = "src/main/resources/day" + day + "/expected-result-" + part + ".txt";
            String content = Files.readString(Paths.get(pathName)).trim();
            if (content.isEmpty()) {
                throw new RuntimeException("Expected result file is empty for day " + day + " part " + part);
            }
            return content;
        } catch (IOException e) {
            throw new RuntimeException("Failed to read expected result file for day " + day + " part " + part, e);
        }
    }
}
