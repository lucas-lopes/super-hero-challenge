package com.dataguard.superherochallenge.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.dataguard.superherochallenge.adapter.HeroAdapter;
import com.dataguard.superherochallenge.dto.HeroDto;
import com.dataguard.superherochallenge.entity.Hero;
import com.dataguard.superherochallenge.repository.HeroRepository;
import com.dataguard.superherochallenge.service.HeroService;
import com.dataguard.superherochallenge.service.exception.BadRequestException;
import com.dataguard.superherochallenge.service.exception.ConflictException;
import com.dataguard.superherochallenge.service.exception.ObjectNotFoundException;
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
    void itShouldSaveNewHero() {
        var newHeroDto = buildHeroDto();
        var heroSaved = buildHero(1L);

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
    void itShouldThrowBadRequestException() {
        var hero = buildHero(1L);

        Throwable exception = Assertions.catchThrowable(() -> heroService.addNewHero(null));

        assertThat(exception)
            .isInstanceOf(BadRequestException.class)
            .hasMessage("Missing the hero object");

        verify(heroRepository, Mockito.never()).save(hero);
    }

    @Test
    @DisplayName("Should throw conflict exception to try saving a new hero existing")
    void itShouldThrowConflictException() {
        var newHeroDto = buildHeroDto();
        var heroSaved = buildHero(1L);

        buildHeroToAdapter();
        when(heroRepository.findByName(Mockito.anyString())).thenReturn(Optional.of(heroSaved));

        Throwable exception = Assertions.catchThrowable(() -> heroService.addNewHero(newHeroDto));

        assertThat(exception)
            .isInstanceOf(ConflictException.class)
            .hasMessage("Hero already exists");
    }

    @Test
    @DisplayName("Should list all heroes")
    void itShouldListAllHeroes() {
        var hero1 = buildHero(1L);
        var hero2 = buildHero(2L);

        List<Hero> heroes = Arrays.asList(hero1, hero2);

        when(heroRepository.findAll()).thenReturn(heroes);

        var allHeroes = heroService.findAllHeroes();

        assertThat(allHeroes)
            .isNotNull()
            .hasSize(2);
    }

    @Test
    @DisplayName("Should get all heroes by property 'power' and value 'flight'")
    void itShouldGetAllHeroesWithPowerProperty() {
        var hero1 = buildHero(1L);
        var hero2 = buildHero(2L);

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
    void itShouldFindAHeroByName() {
        var heroSaved = buildHero(1L);

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
    @DisplayName("Should throw a BadRequestException when hero name does not informed")
    void itShouldThrowBadRequestExceptionWhenTheNameInformedIsNull() {
        Throwable exception = Assertions.catchThrowable(() -> heroService.findHeroByName(null));

        assertThat(exception)
            .isInstanceOf(BadRequestException.class)
            .hasMessage("Missing field name");
    }

    @Test
    @DisplayName("Should find a hero by id")
    void itShouldFindAHeroById() {
        var heroSaved = buildHero(1L);

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
    @DisplayName("Should throw a ObjectNotFoundException when hero does not exist")
    void itShouldThrowObjectNotFoundExceptionWhenHeroDoesNotExist() {
        Throwable exception = Assertions.catchThrowable(() -> heroService.findHeroById(2L));

        assertThat(exception)
            .isInstanceOf(ObjectNotFoundException.class)
            .hasMessage("Hero doesn't find");
    }

    @Test
    @DisplayName("Should throw a BadRequestException when heroId was not informed")
    void itShouldThrowBadRequestExceptionWhenHeroIdWasNotInformed() {
        Throwable exception = Assertions.catchThrowable(() -> heroService.findHeroById(null));

        assertThat(exception)
            .isInstanceOf(BadRequestException.class)
            .hasMessage("Missing field id to find the hero");
    }

    @Test
    @DisplayName("Should update a hero")
    void itShouldUpdateAHero() {
        var heroId = 1L;
        var heroSaved = buildHero(heroId);
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

    @Test
    @DisplayName("Should throw BadRequestException when try update hero with heroId null")
    void itShouldThrowBadRequestExceptionWhenHeroIdIsNull() {
        var heroDto = buildHeroDtoToBeUpdated();

        Throwable exception = Assertions.catchThrowable(() -> heroService.updateHero(null, heroDto));

        assertThat(exception)
            .isInstanceOf(BadRequestException.class)
            .hasMessage("Missing param id to get the user");
    }

    @Test
    @DisplayName("Should delete a hero by heroId")
    void itShouldDeleteAHero() {
        var heroId = 1L;
        var heroSaved = buildHero(heroId);

        when(heroRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(heroSaved));

        heroService.deleteHero(heroId);

        verify(heroRepository).delete(heroSaved);
    }

    @Test
    @DisplayName("Should throw BadRequestException when try delete hero with heroId null")
    void itShouldThrowBadRequestExceptionWhenTryDeleteHeroWithoutHeroId() {
        Throwable exception = Assertions.catchThrowable(() -> heroService.deleteHero(null));

        assertThat(exception)
            .isInstanceOf(BadRequestException.class)
            .hasMessage("Missing param id to get the user");
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
        var hero = buildHero(1L);
        var heroDto = buildHeroDto();

        when(heroAdapter.adapterHeroToHeroDto(Mockito.any(Hero.class))).thenReturn(heroDto);
        when(heroAdapter.adapterHeroDtoToHero(Mockito.any(HeroDto.class))).thenReturn(hero);
    }

}
