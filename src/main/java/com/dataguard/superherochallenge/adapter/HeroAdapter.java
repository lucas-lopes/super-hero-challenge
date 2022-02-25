package com.dataguard.superherochallenge.adapter;

import com.dataguard.superherochallenge.dto.HeroDto;
import com.dataguard.superherochallenge.entity.Hero;
import java.util.Optional;
import org.springframework.stereotype.Component;

@Component
public class HeroAdapter {

    public Hero adapterHeroDtoToHero(HeroDto heroDto) {
        return Hero.builder()
            .name(heroDto.getName())
            .alias(heroDto.getAlias())
            .origin(heroDto.getOrigin())
            .powers(heroDto.getPowers())
            .weapons(heroDto.getWeapons())
            .associations(heroDto.getAssociations())
            .build();
    }

    public HeroDto adapterHeroToHeroDto(Hero hero) {
        return HeroDto.builder()
            .name(hero.getName())
            .alias(hero.getAlias())
            .origin(hero.getOrigin())
            .powers(hero.getPowers())
            .weapons(hero.getWeapons())
            .associations(hero.getAssociations())
            .build();
    }

    public Hero adapterHeroToBeUpdated(Hero hero, HeroDto heroDto) {
        return Hero.builder()
            .id(hero.getId())
            .name(checkIfIsNull(heroDto.getName()) ? heroDto.getName() : hero.getName())
            .alias(checkIfIsNull(heroDto.getAlias()) ? heroDto.getAlias() : hero.getAlias())
            .origin(checkIfIsNull(heroDto.getOrigin()) ? heroDto.getOrigin() : hero.getOrigin())
            .powers(checkIfIsNull(heroDto.getPowers()) ? heroDto.getPowers() : hero.getPowers())
            .weapons(checkIfIsNull(heroDto.getWeapons()) ? heroDto.getWeapons() : hero.getWeapons())
            .associations(checkIfIsNull(heroDto.getAssociations()) ? heroDto.getAssociations() : hero.getAssociations())
            .build();
    }

    private boolean checkIfIsNull(Object field) {
        return Optional.ofNullable(field).isPresent();
    }

}
