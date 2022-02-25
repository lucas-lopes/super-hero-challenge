package com.dataguard.superherochallenge.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import com.dataguard.superherochallenge.adapter.HeroAdapter;
import com.dataguard.superherochallenge.dto.HeroDto;
import com.dataguard.superherochallenge.entity.Hero;
import com.dataguard.superherochallenge.repository.HeroRepository;
import com.dataguard.superherochallenge.service.HeroService;
import com.dataguard.superherochallenge.service.exception.BadRequestException;
import com.dataguard.superherochallenge.service.exception.ConflictException;
import com.dataguard.superherochallenge.service.exception.ObjectNotFoundException;
import com.dataguard.superherochallenge.service.impl.HeroServiceImpl;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
class HeroServiceImplTest {

    HeroService heroService;

    @MockBean
    HeroRepository heroRepository;

    @MockBean
    HeroAdapter heroAdapter;

    @BeforeEach
    public void setup() {
        this.heroService = new HeroServiceImpl(heroRepository, heroAdapter);
    }

    @Test
    @DisplayName("Should save a new hero with success")
    void addNewHeroTest_shouldSaveNewHero() {
        var newHeroDto = buildValidHeroDto();
        var heroSaved = buildValidHero(1L);

        buildHeroToAdapter();
        when(heroRepository.findByName(Mockito.anyString())).thenReturn(Optional.empty());
        when(heroRepository.save(Mockito.any(Hero.class))).thenReturn(heroSaved);

        var hero = heroService.addNewHero(newHeroDto);

        assertThat(hero).isNotNull();
        assertThat(hero.getName()).isEqualTo("Carol Danvers");
        assertThat(hero.getAlias()).isEqualTo("Captain Marvel");
        assertThat(hero.getOrigin()).isEqualTo("Exposed to Space Stone reactor overload");
        assertThat(hero.getPowers()).hasSize(4);
        assertThat(hero.getWeapons()).isEmpty();
        assertThat(hero.getAssociations()).hasSize(5);
    }

    @Test
    @DisplayName("Should throw bad request exception to try saving a new hero invalid")
    void addNewHeroTest_shouldThrowBadRequestException() {
        var hero = buildValidHero(1L);

        Throwable exception = Assertions.catchThrowable(() -> heroService.addNewHero(null));

        assertThat(exception)
            .isInstanceOf(BadRequestException.class)
            .hasMessage("Missing the hero object");

        Mockito.verify(heroRepository, Mockito.never()).save(hero);
    }

    @Test
    @DisplayName("Should throw conflict exception to try saving a new hero existing")
    void addNewHeroTest_shouldThrowConflictException() {
        var newHeroDto = buildValidHeroDto();
        var heroSaved = buildValidHero(1L);

        buildHeroToAdapter();
        when(heroRepository.findByName(Mockito.anyString())).thenReturn(Optional.of(heroSaved));

        Throwable exception = Assertions.catchThrowable(() -> heroService.addNewHero(newHeroDto));

        assertThat(exception)
            .isInstanceOf(ConflictException.class)
            .hasMessage("Hero already exists");
    }

    @Test
    @DisplayName("Should list all heroes")
    void findAllHeroesTest_shouldListAllHeroes() throws Exception {
        var hero1 = buildValidHero(1L);
        var hero2 = buildValidHero(2L);

        List<Hero> heroes = Arrays.asList(hero1, hero2);

        when(heroRepository.findAll()).thenReturn(heroes);

        var allHeroes = heroService.findAllHeroes();

        assertThat(allHeroes).isNotNull();
        assertThat(allHeroes).hasSize(2);
    }

    @Test
    @DisplayName("Should get all heroes by property 'power' and value 'flight'")
    void findHeroesByPropertyTest_shouldGetAllHeroesWithPropertyAndValue() {
        var hero1 = buildValidHero(1L);
        var hero2 = buildValidHero(2L);

        List<Hero> heroes = Arrays.asList(hero1, hero2);
        var propertyValue = "flight";

        buildHeroToAdapter();
        when(heroRepository.findByPower(propertyValue)).thenReturn(heroes);

        var heroesPowerFlight = heroService.findHeroesByProperty("power", propertyValue);

        heroesPowerFlight.forEach(hero -> {
            assertThat(hero).isNotNull();
            assertThat(hero.getName()).isEqualTo("Carol Danvers");
            assertThat(hero.getAlias()).isEqualTo("Captain Marvel");
            assertThat(hero.getOrigin()).isEqualTo("Exposed to Space Stone reactor overload");
            assertThat(hero.getPowers()).hasSize(4);
            assertThat(hero.getWeapons()).isEmpty();
            assertThat(hero.getAssociations()).hasSize(5);
        });

        assertThat(heroesPowerFlight).hasSize(2);
    }

    @Test
    @DisplayName("Should find a hero by name")
    void findHeroByNameTest_shouldFindAHeroByName() {
        var heroSaved = buildValidHero(1L);

        buildHeroToAdapter();
        when(heroRepository.findByName(Mockito.anyString())).thenReturn(Optional.of(heroSaved));

        var hero = heroService.findHeroByName("Carol Danvers");

        assertThat(hero).isNotNull();
        assertThat(hero.isPresent()).isTrue();
        assertThat(hero.get().getName()).isEqualTo("Carol Danvers");
        assertThat(hero.get().getAlias()).isEqualTo("Captain Marvel");
        assertThat(hero.get().getOrigin()).isEqualTo("Exposed to Space Stone reactor overload");
        assertThat(hero.get().getPowers()).hasSize(4);
        assertThat(hero.get().getWeapons()).isEmpty();
        assertThat(hero.get().getAssociations()).hasSize(5);
    }

    @Test
    @DisplayName("Should throw a BadRequestException if hero doesn't exists")
    void findHeroByNameTest_shouldThrowBadRequestException() {
        Throwable exception = Assertions.catchThrowable(() -> heroService.findHeroByName(null));

        assertThat(exception)
            .isInstanceOf(BadRequestException.class)
            .hasMessage("Missing field name");
    }

    @Test
    @DisplayName("Should find a hero by id")
    void findHeroByIdTest_shouldFindAHeroById() {
        var heroSaved = buildValidHero(1L);

        buildHeroToAdapter();
        when(heroRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(heroSaved));

        var hero = heroService.findHeroById(1L);

        assertThat(hero).isNotNull();
        assertThat(hero.getName()).isEqualTo("Carol Danvers");
        assertThat(hero.getAlias()).isEqualTo("Captain Marvel");
        assertThat(hero.getOrigin()).isEqualTo("Exposed to Space Stone reactor overload");
        assertThat(hero.getPowers()).hasSize(4);
        assertThat(hero.getWeapons()).isEmpty();
        assertThat(hero.getAssociations()).hasSize(5);
    }

    @Test
    @DisplayName("Should throw a ObjectNotFoundException if the hero by id doesn't exist")
    void findHeroByIdTest_shouldThrowObjectNotFoundException() {
        Throwable exception = Assertions.catchThrowable(() -> heroService.findHeroById(2L));

        assertThat(exception)
            .isInstanceOf(ObjectNotFoundException.class)
            .hasMessage("Hero doesn't found");
    }

    @Test
    @DisplayName("Should throw a BadRequest if id to find the hero wasn't informed")
    void findHeroByIdTest_shouldThrowBadRequestException() {
        Throwable exception = Assertions.catchThrowable(() -> heroService.findHeroById(null));

        assertThat(exception)
            .isInstanceOf(BadRequestException.class)
            .hasMessage("Missing field id to find the hero");
    }

    @Test
    @DisplayName("Should find a hero by id")
    void updateHeroTest_shouldUpdateAHeroWithSuccess() {
        var heroId = 1L;
        var heroSaved = buildValidHero(heroId);
        var heroDto = buildHeroDtoToBeUpdated();
        var heroUpdated = buildHeroToBeUpdated(heroId);

        when(heroAdapter.adapterHeroToBeUpdated(Mockito.any(Hero.class), Mockito.any(HeroDto.class)))
            .thenReturn(heroUpdated);

        when(heroRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(heroSaved));
        when(heroRepository.save(Mockito.any(Hero.class))).thenReturn(heroUpdated);
        when(heroAdapter.adapterHeroToHeroDto(Mockito.any(Hero.class))).thenReturn(heroDto);

        var hero = heroService.updateHero(heroId, heroDto);

        assertThat(hero).isNotNull();
        assertThat(hero.getName()).isNotEqualTo("Carol Danvers");
        assertThat(hero.getName()).isEqualTo("Lucas Barbosa");
        assertThat(hero.getAlias()).isNotEqualTo("Captain Marvel");
        assertThat(hero.getAlias()).isEqualTo("Captain IT");
        assertThat(hero.getOrigin()).isNotEqualTo("Exposed to Space Stone reactor overload");
        assertThat(hero.getOrigin()).isEqualTo("Exposed to Java and programming logic");
        assertThat(hero.getPowers()).hasSize(3);
        assertThat(hero.getWeapons()).hasSize(4);
        assertThat(hero.getAssociations()).hasSize(1);
    }

    private HeroDto buildValidHeroDto() {
        return HeroDto.builder()
            .name("Carol Danvers")
            .alias("Captain Marvel")
            .origin("Exposed to Space Stone reactor overload")
            .powers(new String[]{"photon-blast", "flight", "super-strength", "healing"})
            .associations(new String[]{"space-stone", "skrulls", "photon", "kree", "avengers"})
            .weapons(new String[0])
            .build();
    }

    private Hero buildValidHero(Long id) {
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

    private Hero buildHeroToBeUpdated(Long id) {
        return Hero.builder()
            .id(id)
            .name("Lucas Barbosa")
            .alias("Captain IT")
            .origin("Exposed to Java and programming logic")
            .powers(new String[]{"smart", "speed", "team-worker"})
            .associations(new String[]{"DataGuard"})
            .weapons(new String[]{"Java", "MacBook", "Git", "Microservices"})
            .build();
    }

    private HeroDto buildHeroDtoToBeUpdated() {
        return HeroDto.builder()
            .name("Lucas Barbosa")
            .alias("Captain IT")
            .origin("Exposed to Java and programming logic")
            .powers(new String[]{"smart", "speed", "team-worker"})
            .associations(new String[]{"DataGuard"})
            .weapons(new String[]{"Java", "MacBook", "Git", "Microservices"})
            .build();
    }

    private void buildHeroToAdapter() {
        var hero = Hero.builder()
            .name("Carol Danvers")
            .alias("Captain Marvel")
            .origin("Exposed to Space Stone reactor overload")
            .powers(new String[]{"photon-blast", "flight", "super-strength", "healing"})
            .associations(new String[]{"space-stone", "skrulls", "photon", "kree", "avengers"})
            .weapons(new String[0])
            .build();

        var heroDto = HeroDto.builder()
            .name("Carol Danvers")
            .alias("Captain Marvel")
            .origin("Exposed to Space Stone reactor overload")
            .powers(new String[]{"photon-blast", "flight", "super-strength", "healing"})
            .associations(new String[]{"space-stone", "skrulls", "photon", "kree", "avengers"})
            .weapons(new String[0])
            .build();

        when(heroAdapter.adapterHeroToHeroDto(Mockito.any(Hero.class))).thenReturn(heroDto);
        when(heroAdapter.adapterHeroDtoToHero(Mockito.any(HeroDto.class))).thenReturn(hero);
    }

}
