package com.dataguard.superherochallenge.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class HeroDto {

    private final String alias;
    private final String name;
    private final String origin;
    private final String[] powers;
    private final String[] weapons;
    private final String[] associations;

}
