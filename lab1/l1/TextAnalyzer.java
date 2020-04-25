package com.igordyac.igor.lab1.l1;

import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

public class TextAnalyzer {

    public Map<String, Long> countWords(String text) {
        if (Objects.isNull(text) || text.isEmpty()) {
            throw new IllegalArgumentException("text can not be null");
        }
        return Arrays.stream(text.split(" "))
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
    }
}
