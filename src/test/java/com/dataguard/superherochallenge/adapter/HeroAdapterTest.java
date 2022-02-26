package com.dataguard.superherochallenge.adapter;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import com.dataguard.superherochallenge.dto.HeroDto;
import com.dataguard.superherochallenge.entity.Hero;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
class HeroAdapterTest {

    HeroAdapter heroAdapter;

    @BeforeEach
    public void setup() {
        this.heroAdapter = new HeroAdapter();
    }

    @Test
    void itShouldAdapterHeroDtoToHero() {
        // given
        var heroDto = buildHeroDto();

        // when
        var hero = heroAdapter.adapterHeroDtoToHero(heroDto);

        // then
        assertThat(hero).isNotNull();
        assertThat(hero.getId()).isNull();
        assertThat(hero.getName()).isEqualTo("Carol Danvers");
        assertThat(hero.getAlias()).isEqualTo("Captain Marvel");
        assertThat(hero.getOrigin()).isEqualTo("Exposed to Space Stone reactor overload");
        assertThat(hero.getAssociations()).hasSize(5);
        assertThat(hero.getPowers()).hasSize(4);
        assertThat(hero.getWeapons()).isEmpty();
    }

    @Test
    void itShouldAdapterHeroToHeroDto() {
        // given
        var hero = buildHero(1L);

        // when
        var heroDto = heroAdapter.adapterHeroToHeroDto(hero);

        // then
        assertThat(heroDto).isNotNull();
        assertThat(hero.getId()).isNotNull();
        assertThat(hero.getId()).isEqualTo(1L);
        assertThat(heroDto.getName()).isEqualTo("Carol Danvers");
        assertThat(heroDto.getAlias()).isEqualTo("Captain Marvel");
        assertThat(heroDto.getOrigin()).isEqualTo("Exposed to Space Stone reactor overload");
        assertThat(heroDto.getAssociations()).hasSize(5);
        assertThat(heroDto.getPowers()).hasSize(4);
        assertThat(heroDto.getWeapons()).isEmpty();
    }

    @Test
    void adapterHeroToBeUpdated() {
        // given
        var heroSaved = buildHero(10L);

        var heroToBeUpdated = HeroDto.builder()
            .name("Lucas Barbosa")
            .alias("Captain IT")
            .origin("Exposed to Java and programming logic")
            .powers(new String[]{"smart", "speed", "team-worker"})
            .associations(new String[]{"DataGuard"})
            .weapons(new String[]{"Java", "MacBook", "Git", "Microservices"})
            .build();

        // when
        var hero = heroAdapter.adapterHeroToBeUpdated(heroSaved, heroToBeUpdated);

        // then
        assertThat(hero).isNotNull();
        assertThat(hero.getId()).isNotNull();
        assertThat(hero.getId()).isEqualTo(10L);
        assertThat(hero.getName()).isEqualTo("Lucas Barbosa");
        assertThat(hero.getAlias()).isEqualTo("Captain IT");
        assertThat(hero.getOrigin()).isEqualTo("Exposed to Java and programming logic");
        assertThat(hero.getAssociations()).hasSize(1);
        assertThat(hero.getPowers()).hasSize(3);
        assertThat(hero.getWeapons()).hasSize(4);
    }

    private HeroDto buildHeroDto() {
        return HeroDto.builder()
            .name("Carol Danvers")
            .alias("Captain Marvel")
            .origin("Exposed to Space Stone reactor overload")
            .powers(new String[]{"photon-blast", "flight", "super-strength", "healing"})
            .associations(new String[]{"space-stone", "skrulls", "photon", "kree", "avengers"})
            .weapons(new String[0])
            .build();
    }

    private Hero buildHero(Long id) {
        return Hero.builder()
            .id(id)
            .name("Carol Danvers")
            .alias("Captain Marvel")
            .origin("Exposed to Space Stone reactor overload")
            .powers(new String[]{"photon-blast", "flight", "super-strength", "healing"})
            .associations(new String[]{"space-stone", "skrulls", "photon", "kree", "avengers"})
            .weapons(new String[0])
            .build();
    }
}