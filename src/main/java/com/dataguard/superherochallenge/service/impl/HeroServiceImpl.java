package com.dataguard.superherochallenge.service.impl;

import com.dataguard.superherochallenge.dto.HeroDto;
import com.dataguard.superherochallenge.entity.Hero;
import com.dataguard.superherochallenge.repository.HeroRepository;
import com.dataguard.superherochallenge.service.HeroService;
import java.util.List;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class HeroServiceImpl implements HeroService {

    private final HeroRepository heroRepository;

    @Override
    public HeroDto addNewHero(HeroDto heroDto) {
        return null;
    }

    @Override
    public List<HeroDto> findAllHeroes() throws Exception {
        return null;
    }

    @Override
    public List<HeroDto> findHeroesByProperty(String property, String value) {
        return null;
    }

    @Override
    public Optional<HeroDto> findHeroByName(String name) {
        return Optional.empty();
    }

    @Override
    public Hero findHeroById(Long id) {
        return null;
    }

    @Override
    public HeroDto updateHero(Long heroId, HeroDto heroDto) {
        return null;
    }

    @Override
    public void deleteHero(Long heroId) {

    }
}
