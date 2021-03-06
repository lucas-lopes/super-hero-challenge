package com.dataguard.superherochallenge.service.impl;

import com.dataguard.superherochallenge.adapter.HeroAdapter;
import com.dataguard.superherochallenge.dto.HeroDto;
import com.dataguard.superherochallenge.entity.Hero;
import com.dataguard.superherochallenge.entity.HeroProperty;
import com.dataguard.superherochallenge.repository.HeroRepository;
import com.dataguard.superherochallenge.service.HeroService;
import com.dataguard.superherochallenge.service.exception.BadRequestException;
import com.dataguard.superherochallenge.service.exception.ConflictException;
import com.dataguard.superherochallenge.service.exception.ObjectNotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class HeroServiceImpl implements HeroService {

    private final HeroRepository heroRepository;
    private final HeroAdapter heroAdapter;

    @Override
    public HeroDto addNewHero(HeroDto heroDto) {
        log.info("[addNewHero] start adding new hero");
        if (Optional.ofNullable(heroDto).isPresent()) {
            var hero = heroAdapter.adapterHeroDtoToHero(heroDto);
            var heroOptional = heroRepository.findByName(hero.getName());

            if (heroOptional.isEmpty()) {
                var heroAdded = heroRepository.save(hero);
                log.info("[addNewHero] hero {} added with success: ID {}", heroAdded.getName(), heroAdded.getId());
                return heroAdapter.adapterHeroToHeroDto(heroAdded);
            }
            throw new ConflictException("Hero already exists");
        }
        throw new BadRequestException("Missing the hero object");
    }

    @Override
    public List<HeroDto> findAllHeroes() {
        return heroRepository.findAll()
            .stream()
            .map(heroAdapter::adapterHeroToHeroDto)
            .collect(Collectors.toList());
    }

    @Override
    public List<HeroDto> findHeroesByProperty(String property, String value) {
        log.info("[findHeroesByProperty] Finding heroes with property '{}' and value '{}'", property, value);
        var heroProperty = HeroProperty.adapterStringToEnum(property);

        if (Optional.ofNullable(heroProperty).isPresent()) {
            switch (heroProperty) {
                case POWER:
                    return heroRepository.findByPower(value)
                        .stream()
                        .map(heroAdapter::adapterHeroToHeroDto)
                        .collect(Collectors.toList());
                case WEAPON:
                    return heroRepository.findByWeapon(value)
                        .stream()
                        .map(heroAdapter::adapterHeroToHeroDto)
                        .collect(Collectors.toList());
                case ASSOCIATION:
                    return heroRepository.findByAssociation(value)
                        .stream()
                        .map(heroAdapter::adapterHeroToHeroDto)
                        .collect(Collectors.toList());
            }
        }
        throw new ObjectNotFoundException("The property informed doesn't exist: try power, weapon or association");
    }

    @Override
    public Optional<HeroDto> findHeroByName(String name) {
        if (Optional.ofNullable(name).isPresent()) {
            log.info("[findHeroByName] Finding hero called '{}'", name);
            return Optional.of(heroRepository.findByName(name)
                .map(heroAdapter::adapterHeroToHeroDto)
                .orElseThrow(() -> new ObjectNotFoundException("Hero by name not found")));
        }
        throw new BadRequestException("Missing field name");
    }

    @Override
    public Hero findHeroById(Long id) {
        if (Optional.ofNullable(id).isPresent()) {
            return heroRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Hero doesn't find"));
        }
        throw new BadRequestException("Missing field id to find the hero");
    }

    @Override
    public HeroDto updateHero(Long heroId, HeroDto heroDto) {
        if (Optional.ofNullable(heroId).isPresent()) {
            log.info("[updateHero] start updating hero");
            val heroFound = findHeroById(heroId);
            val hero = heroAdapter.adapterHeroToBeUpdated(heroFound, heroDto);

            heroRepository.save(hero);
            log.info("[updateHero] hero updated with success");
            return heroAdapter.adapterHeroToHeroDto(hero);
        }
        throw new BadRequestException("Missing param id to get the user");
    }

    @Override
    public void deleteHero(Long heroId) {
        if (Optional.ofNullable(heroId).isPresent()) {
            log.info("[deleteHero] start deleting hero");
            val hero = findHeroById(heroId);

            heroRepository.delete(hero);
            log.info("[deleteHero] hero deleted with success");
        } else {
            throw new BadRequestException("Missing param id to get the user");
        }
    }
}
