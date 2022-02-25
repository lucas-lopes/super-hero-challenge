package com.dataguard.superherochallenge.adapter;

import com.dataguard.superherochallenge.dto.HeroDto;
import com.dataguard.superherochallenge.entity.Hero;
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

}
