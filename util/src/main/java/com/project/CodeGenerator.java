package com.project;

import java.time.LocalDateTime;
import java.util.Random;

public class CodeGenerator {

    private CodeGenerator() {
    }

    private static final int MIN = 10000;
    private static final int MAX = 99999;

    public static int generate(){
        Random random = new Random();
        int diff = MAX - MIN;
        int generatedValue = random.nextInt(diff) + MIN;
        return generatedValue;
    }

}
