package com.igordyac.igor.lab1.l1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;

public class L1Application implements Runnable {
    private final TextAnalyzer textAnalyzer = new TextAnalyzer();


    public void run() {
        var finished = false;

        while (!finished) {
            try {

                System.err.println("[lab1] Please, type some text");
                var input = readInput();
                System.out.println("lab1] result = " + run(input));
                finished = true;
            } catch (Exception e) {
                System.err.println(String.format("[lab1] There was an error: %s", e.getMessage()));
            }
        }

        System.err.println("[lab1] Finishing...");
    }

    public Map<String, Long> run(String input) {
        return textAnalyzer.countWords(input);
    }

    private String readInput() throws IOException {
        var bf = new BufferedReader(new InputStreamReader(System.in));
        return bf.readLine();
    }
}
