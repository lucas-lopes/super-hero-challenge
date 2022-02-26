package com.dataguard.superherochallenge.controller;

import com.dataguard.superherochallenge.dto.HeroDto;
import com.dataguard.superherochallenge.service.HeroService;
import java.net.URI;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("api/v1/heroes")
@AllArgsConstructor
@Validated
public class HeroController {

    private static final String MESSAGE = "is missing and is required";

    private final HeroService heroService;

    @GetMapping
    public ResponseEntity<List<HeroDto>> findAllHeroes() {
        return ResponseEntity.ok(heroService.findAllHeroes());
    }

    @GetMapping("/names")
    public ResponseEntity<HeroDto> findHeroByName(@RequestParam(value = "name")
                                                  @NotBlank(message = "Name " + MESSAGE) String name) {
        var heroDto = heroService.findHeroByName(name);
        return ResponseEntity.ok(heroDto.orElse(null));
    }

    @GetMapping(value = "/properties")
    public ResponseEntity<List<HeroDto>> findHeroesByProperty(@RequestParam(value = "property")
                                                              @NotBlank(message = "Property " + MESSAGE) String property,
                                                              @RequestParam(value = "value")
                                                              @NotBlank(message = "Value " + MESSAGE) String value) {
        var heroes = heroService.findHeroesByProperty(property, value);
        return ResponseEntity.ok(heroes);
    }

    @PostMapping
    public ResponseEntity<HeroDto> addNewHero(@Valid @RequestBody HeroDto heroDto) {
        heroDto = heroService.addNewHero(heroDto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
            .path("/{name}").buildAndExpand(heroDto.getName()).toUri();
        return ResponseEntity.created(uri).body(heroDto);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<HeroDto> updateHero(@PathVariable(value = "id") Long id, @RequestBody HeroDto heroDto) {
        heroDto = heroService.updateHero(id, heroDto);
        return ResponseEntity.ok().body(heroDto);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<HeroDto> deleteHero(@PathVariable(value = "id") Long id) {
        heroService.deleteHero(id);
        return ResponseEntity.noContent().build();
    }

}
