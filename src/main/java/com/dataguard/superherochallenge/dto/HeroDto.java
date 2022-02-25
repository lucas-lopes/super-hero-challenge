package com.dataguard.superherochallenge.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import javax.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@JsonInclude(Include.NON_NULL)
public class HeroDto {

    @NotBlank(message = "Alias cannot be empty or null")
    private final String alias;

    @NotBlank(message = "Name cannot be empty or null")
    private final String name;

    @NotBlank(message = "Origin cannot be empty or null")
    private final String origin;

    private final String[] powers;
    private final String[] weapons;
    private final String[] associations;

}
