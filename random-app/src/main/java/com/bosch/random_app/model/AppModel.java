package com.bosch.random_app.model;

import java.util.List;
import java.util.Random;

public class AppModel {
    private final List<String> options;
    private final Random random;

    public AppModel(List<String> options, Random random) {
        this.options = options;
        this.random = random;
    }

    public String getRandomOption() {
        if (options.isEmpty()) {
            throw new IllegalStateException("No options provided.");
        }
        return options.get(random.nextInt(options.size()));
    }
}
