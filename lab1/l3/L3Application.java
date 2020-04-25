package com.igordyac.igor.lab1.l3;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class L3Application implements Runnable {
    private static final ReverseLinesPrinter reverseLinePrinter = new ReverseLinesPrinter();

    public void run() {
        var file = new File("C:/Users/admin/Desktop/tempForJava.txt");

        try (var is = new FileInputStream(file)) {
            reverseLinePrinter.printReverse(is);
        } catch (IOException e) {
            System.err.println("[lab3] IOException");
        } finally {
            System.err.println("[lab3] stream closed, finally executed");
        }
    }
}
