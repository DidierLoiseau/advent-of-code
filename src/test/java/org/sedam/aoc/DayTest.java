package org.sedam.aoc;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.lang.reflect.Method;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@QuarkusTest
public class DayTest {

    @ParameterizedTest
    @ValueSource(ints = {1, 2})
    public void testDay(int part) throws Exception {
        // Get the day number from the system property
        String dayNumber = System.getProperty("day");
        int day;
        if (dayNumber == null || dayNumber.isEmpty()) {
            System.out.println("WARN: Using today as day number");
            day = LocalDate.now().getDayOfMonth();
        } else {
            day = Integer.parseInt(dayNumber);
        }

        // Dynamically load the corresponding Day object
        String className = "org.sedam.aoc.Day" + day;
        Class<?> dayClass;
        try {
            dayClass = Class.forName(className);
        } catch (ClassNotFoundException e) {
            throw new IllegalArgumentException("Day " + day + " not implemented yet");
        }
        Day dayObject = (Day) dayClass.getDeclaredConstructor().newInstance();

        // Execute the test
        testPart(day, dayObject, part);
    }

    private void testPart(int day, Day dayObject, int part) throws Exception {
        // Prepare input files
        List<String> testInput = TestUtils.readInputFiles(day, part, "test-");
        List<String> realInput = TestUtils.readInputFiles(day, part, "");

        // Dynamically get the method corresponding to the part (part1, part2)
        String methodName = "part" + part;
        Method partMethod = dayObject.getClass().getMethod(methodName, List.class);

        // Test Part
        if (dayObject.hasPart2ExpectedResult()) {
            String expectedTestResult = TestUtils.readExpectedResult(day, part);
            String testPartResult = (String) partMethod.invoke(dayObject, testInput);
            assertEquals(expectedTestResult, testPartResult,
                    "\nDay " + day + " - Part " + part + " is not matching the expected result.");
        }

        // Real Part
        String partResult = (String) partMethod.invoke(dayObject, realInput);
        System.out.println("\n\n====== Day " + day + " - Part " + part + " Result: " + partResult + " ======\n\n");
    }

}
