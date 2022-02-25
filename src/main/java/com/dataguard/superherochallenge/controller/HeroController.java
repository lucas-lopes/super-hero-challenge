package com.dataguard.superherochallenge.controller;

import com.dataguard.superherochallenge.dto.HeroDto;
import com.dataguard.superherochallenge.service.HeroService;
import java.net.URI;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

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

    @GetMapping(value = "/properties")
    public ResponseEntity<List<HeroDto>> findHeroesByProperty(@RequestParam(value = "property") String property,
                                                              @RequestParam(value = "value") String value) {
        var heroes = heroService.findHeroesByProperty(property, value);
        return ResponseEntity.ok(heroes);
    }

    @PostMapping
    public ResponseEntity<HeroDto> addNewHero(@RequestBody HeroDto heroDto) {
        heroDto = heroService.addNewHero(heroDto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
            .path("/{name}").buildAndExpand(heroDto.getName()).toUri();
        return ResponseEntity.created(uri).body(heroDto);
    }

}
