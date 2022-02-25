package com.dataguard.superherochallenge.service.impl;

import com.dataguard.superherochallenge.adapter.HeroAdapter;
import com.dataguard.superherochallenge.dto.HeroDto;
import com.dataguard.superherochallenge.entity.Hero;
import com.dataguard.superherochallenge.repository.HeroRepository;
import com.dataguard.superherochallenge.service.HeroService;
import com.dataguard.superherochallenge.service.exception.BadRequestException;
import com.dataguard.superherochallenge.service.exception.ObjectNotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class HeroServiceImpl implements HeroService {

    private final HeroRepository heroRepository;
    private final HeroAdapter heroAdapter;

    @Override
    public HeroDto addNewHero(HeroDto heroDto) {
        return null;
    }

    @Override
    public List<HeroDto> findAllHeroes() throws Exception {
        try {
            return heroRepository.findAll()
                .stream()
                .map(heroAdapter::adapterHeroToHeroDto)
                .collect(Collectors.toList());
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    @Override
    public List<HeroDto> findHeroesByProperty(String property, String value) {
        return null;
    }

    @Override
    public Optional<HeroDto> findHeroByName(String name) {
        try {
            if (Optional.ofNullable(name).isPresent()) {
                log.info("[findHeroByName] Finding hero called '{}'", name);
                return Optional.of(heroRepository.findByName(name)
                    .map(heroAdapter::adapterHeroToHeroDto)
                    .orElseThrow(() -> new ObjectNotFoundException("Hero by name not found")));
            }
            throw new BadRequestException("Missing field name");
        } catch (BadRequestException e) {
            throw new BadRequestException(e.getMessage());
        }
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
