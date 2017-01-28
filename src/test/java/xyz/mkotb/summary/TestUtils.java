package xyz.mkotb.summary;

import java.util.List;

public class TestUtils {
    public static void printSummary(List<String> summary) {
        System.out.println("-------------------------- SUMMARY ------------------------");

        for (String part : summary) {
            System.out.println("\n" + part);
        }

        System.out.println("\n-----------------------------------------------------------");
    }
}
