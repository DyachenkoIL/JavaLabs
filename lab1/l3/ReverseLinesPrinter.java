package com.igordyac.igor.lab1.l3;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Stack;

public class ReverseLinesPrinter {

    public void printReverse(InputStream inputStream) {
        try {
            var lines = readLines(inputStream);
            printReverse(lines);
        } catch (IOException e) {
            throw new ReverseLinesPrinterException("[lab3] problem while reading from stream");
        }
    }

    private void printReverse(Stack<String> lines) {
        while (!lines.isEmpty()) {
            System.out.println(lines.pop());
        }
    }

    private Stack<String> readLines(InputStream inputStream) throws IOException {
        var bufStream = new BufferedInputStream(inputStream);
        var reader = new BufferedReader(new InputStreamReader(bufStream, StandardCharsets.UTF_8));
        Stack<String> lines = new Stack<>();
        while(reader.ready()) {
            lines.add(reader.readLine());
        }
        return lines;
    }
}
