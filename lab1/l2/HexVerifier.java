package com.igordyac.igor.lab1.l2;

import java.util.Objects;
import java.util.regex.Pattern;

public class HexVerifier {
    private static final Pattern HEX_PATTERN = Pattern.compile("^([0-9A-Fa-f]{2})+$");


    public boolean isValidHex(String input) throws HexVerifierException {
        if (Objects.isNull(input) || input.isEmpty()) {
            throw new HexVerifierException("[lab2] argument can not be null or blank");
        }

        return HEX_PATTERN.matcher(input).matches();
    }


}
