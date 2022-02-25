package com.dataguard.superherochallenge.controller;

import com.dataguard.superherochallenge.dto.HeroDto;
import com.dataguard.superherochallenge.service.HeroService;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/heroes")
@AllArgsConstructor
public class HeroController {

    private final HeroService heroService;

    @GetMapping
    public ResponseEntity<List<HeroDto>> findAllHeroes() throws Exception {
        return ResponseEntity.ok(heroService.findAllHeroes());
    }

    @GetMapping("/{name}")
    public ResponseEntity<HeroDto> findHeroByName(@PathVariable(name = "name") String name) {
        var heroDto = heroService.findHeroByName(name);
        return ResponseEntity.ok(heroDto.orElse(null));
    }

}
