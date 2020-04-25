package com.igordyac.igor.lab1.l2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class L2Application implements Runnable {
    private static final HexVerifier verifier = new HexVerifier();

    public void run() {
        var finished = false;

        while (!finished) {
            try {


                System.err.println("[lab2] Please, type color code");
                var input = readInput();
                var isHex = verifier.isValidHex(input);
                printResult(isHex);
                finished = true;

            } catch (Exception e) {
                System.err.println(String.format("[lab2] There was an error: %s", e.getMessage()));
            }
        }

        System.err.println("[lab2] Finishing...");
    }

    public void printResult(boolean res) {
        System.out.println("[lab2] color is " + (res ? "valid" : "invalid"));
    }

    private String readInput() throws IOException {
        var bf = new BufferedReader(new InputStreamReader(System.in));
        return bf.readLine();
    }
}
