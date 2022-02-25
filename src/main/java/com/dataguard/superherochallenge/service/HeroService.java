package com.dataguard.superherochallenge.service;

import com.dataguard.superherochallenge.dto.HeroDto;
import com.dataguard.superherochallenge.entity.Hero;
import java.util.List;
import java.util.Optional;

public interface HeroService {

    HeroDto addNewHero(final HeroDto heroDto);

    List<HeroDto> findAllHeroes() throws Exception;

    List<HeroDto> findHeroesByProperty(final String property, final String value);

    Optional<HeroDto> findHeroByName(final String name);

    Hero findHeroById(final Long id);

    HeroDto updateHero(final Long heroId, final HeroDto heroDto);

    void deleteHero(final Long heroId);

}
