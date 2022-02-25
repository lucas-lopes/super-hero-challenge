package com.dataguard.superherochallenge.entity;

import java.util.Arrays;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum HeroProperty {

    POWER("power"),
    WEAPON("weapon"),
    ASSOCIATION("association");

    private final String name;

    public static HeroProperty adapterStringToEnum(String property) {
        return Arrays.stream(HeroProperty.values())
            .filter(p -> p.getName().equals(property))
            .findFirst()
            .orElse(null);
    }

}
