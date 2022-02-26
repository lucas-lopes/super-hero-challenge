package com.dataguard.superherochallenge.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.not;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.dataguard.superherochallenge.controller.exception.StandardError;
import com.dataguard.superherochallenge.dto.HeroDto;
import com.dataguard.superherochallenge.entity.Hero;
import com.dataguard.superherochallenge.service.HeroService;
import com.dataguard.superherochallenge.service.exception.BadRequestException;
import com.dataguard.superherochallenge.service.exception.ConflictException;
import com.dataguard.superherochallenge.service.exception.ObjectNotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = HeroController.class)
@AutoConfigureMockMvc
class HeroControllerTest {

    private final String HERO_API = "/api/v1/heroes";

    @Autowired
    MockMvc mvc;

    @MockBean
    HeroService heroService;

    @Test
    @DisplayName("Should create a new hero with success")
    void AddNewHeroTest_shouldCreateWithSuccess() throws Exception {
        var heroDto = buildNewHero();

        var heroSaved = HeroDto.builder()
            .name("Carol Danvers")
            .alias("Captain Marvel")
            .origin("Exposed to Space Stone reactor overload")
            .powers(new String[]{"photon-blast", "flight", "super-strength", "healing"})
            .associations(new String[]{"space-stone", "skrulls", "photon", "kree", "avengers"})
            .weapons(new String[0])
            .build();

        BDDMockito.given(heroService.addNewHero(Mockito.any(HeroDto.class))).willReturn(heroSaved);
        var json = new ObjectMapper().writeValueAsString(heroDto);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
            .post(HERO_API)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .content(json);

        mvc.perform(request)
            .andExpect(status().isCreated())
            .andExpect(jsonPath("id").doesNotExist())
            .andExpect(jsonPath("name").value(heroDto.getName()))
            .andExpect(jsonPath("alias").value(heroDto.getAlias()))
            .andExpect(jsonPath("origin").value(heroDto.getOrigin()))
            .andExpect(jsonPath("powers").isArray())
            .andExpect(jsonPath("weapons").isArray())
            .andExpect(jsonPath("associations").isArray())
            .andExpect(jsonPath("powers", hasSize(4)))
            .andExpect(jsonPath("weapons", hasSize(0)))
            .andExpect(jsonPath("associations", hasSize(5)));
    }

    @Test
    @DisplayName("Should throw bad request exception when request is invalid")
    void itShouldThrowBadRequestWhenBodyRequestIsInvalid() throws Exception {
        String[] messages = new String[]{
            "Name cannot be empty or null",
            "Alias cannot be empty or null",
            "Origin cannot be empty or null"
        };

        var standardError = buildStandardError(HttpStatus.BAD_REQUEST, messages);

        BDDMockito.given(heroService.addNewHero(Mockito.any(HeroDto.class))).willReturn(null);
        var json = new ObjectMapper().writeValueAsString(HeroDto.builder().build());

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
            .post(HERO_API)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .content(json);

        mvc.perform(request)
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("status").value(standardError.getStatus()))
            .andExpect(jsonPath("message").doesNotExist())
            .andExpect(jsonPath("error").value(standardError.getError()))
            .andExpect(jsonPath("timestamp").isNotEmpty())
            .andExpect(jsonPath("messages").isArray())
            .andExpect(jsonPath("messages", hasSize(3)));
    }

    @Test
    @DisplayName("Should throw bad request exception when hero already exists")
    void itShouldThrowBadRequestWhenHeroAlreadyExists() throws Exception {
        var heroDto = buildNewHeroDto();
        var errorMessage = "Hero already exists";
        var standardError = buildStandardError(HttpStatus.CONFLICT, new ConflictException(errorMessage));

        BDDMockito.given(heroService.addNewHero(Mockito.any(HeroDto.class)))
            .willThrow(new ConflictException(errorMessage));

        var json = new ObjectMapper().writeValueAsString(heroDto);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
            .post(HERO_API)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .content(json);

        mvc.perform(request)
            .andExpect(status().isConflict())
            .andExpect(jsonPath("status").value(standardError.getStatus()))
            .andExpect(jsonPath("message").value(standardError.getMessage()))
            .andExpect(jsonPath("error").value(standardError.getError()))
            .andExpect(jsonPath("timestamp").isNotEmpty())
            .andExpect(jsonPath("messages").doesNotExist());
    }

    @Test
    @DisplayName("Should find a hero existing by name")
    void itShouldFindAHeroByName() throws Exception {
        var heroDto = buildNewHero();

        var heroSaved = HeroDto.builder()
            .name("Carol Danvers")
            .alias("Captain Marvel")
            .origin("Exposed to Space Stone reactor overload")
            .powers(new String[]{"photon-blast", "flight", "super-strength", "healing"})
            .associations(new String[]{"space-stone", "skrulls", "photon", "kree", "avengers"})
            .weapons(new String[0])
            .build();

        BDDMockito.given(heroService.findHeroByName(Mockito.any(String.class))).willReturn(Optional.of(heroSaved));
        var json = new ObjectMapper().writeValueAsString(heroDto);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
            .get(HERO_API + "/names?name=Carol Danvers")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .content(json);

        mvc.perform(request)
            .andExpect(status().isOk())
            .andExpect(jsonPath("id").doesNotExist())
            .andExpect(jsonPath("name").value(heroDto.getName()))
            .andExpect(jsonPath("alias").value(heroDto.getAlias()))
            .andExpect(jsonPath("origin").value(heroDto.getOrigin()))
            .andExpect(jsonPath("powers").isArray())
            .andExpect(jsonPath("weapons").isArray())
            .andExpect(jsonPath("associations").isArray())
            .andExpect(jsonPath("powers", hasSize(4)))
            .andExpect(jsonPath("weapons", hasSize(0)))
            .andExpect(jsonPath("associations", hasSize(5)));
    }

    @Test
    @DisplayName("Should throw BadRequestException when param name was not informed")
    void itShouldThrowBadRequestExceptionWhenHeroNameWasNotInformed() throws Exception {
        String[] messages = new String[]{"Name is missing and is required"};
        var standardError = buildStandardError(HttpStatus.BAD_REQUEST, messages);

        BDDMockito.given(heroService.findHeroByName(null))
            .willThrow(new ObjectNotFoundException("Hero by name not found"));

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
            .get(HERO_API + "/names?name=")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON);

        mvc.perform(request)
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("status").value(standardError.getStatus()))
            .andExpect(jsonPath("message").doesNotExist())
            .andExpect(jsonPath("error").value(standardError.getError()))
            .andExpect(jsonPath("timestamp").isNotEmpty())
            .andExpect(jsonPath("messages").isArray())
            .andExpect(jsonPath("messages", hasSize(1)));
    }

    @Test
    @DisplayName("Should throw BadRequestException when param name was not informed")
    void itShouldThrowObjectNotFoundExceptionWhenFindAHeroByName() throws Exception {
        String errorMessage = "Hero by name was not found";
        var standardError =
            buildStandardError(HttpStatus.NOT_FOUND, new ObjectNotFoundException(errorMessage));

        BDDMockito.given(heroService.findHeroByName(Mockito.anyString()))
            .willThrow(new ObjectNotFoundException(errorMessage));

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
            .get(HERO_API + "/names")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .param("name", "Lucas");

        mvc.perform(request)
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("status").value(standardError.getStatus()))
            .andExpect(jsonPath("message").value(standardError.getMessage()))
            .andExpect(jsonPath("error").value(standardError.getError()))
            .andExpect(jsonPath("timestamp").isNotEmpty())
            .andExpect(jsonPath("messages").doesNotExist());
    }

    @Test
    @DisplayName("Should find all heroes")
    void itShouldFindAllHeroes() throws Exception {
        var heroes = buildListOfHeroes();

        var hero1 = HeroDto.builder()
            .name("Carol Danvers")
            .alias("Captain Marvel")
            .origin("Exposed to Space Stone reactor overload")
            .powers(new String[]{"photon-blast", "flight", "super-strength", "healing"})
            .associations(new String[]{"space-stone", "skrulls", "photon", "kree", "avengers"})
            .weapons(new String[0])
            .build();

        var hero2 = HeroDto.builder()
            .name("Iron Man")
            .alias("Tony Stark")
            .origin("Kidnapped in Afghanistan, created the first iron-man suit to escape.")
            .powers(new String[]{"genius-intelligence", "wealth", "flight"})
            .associations(new String[]{"war-machine", "avengers", "jarvis", "thanos", "pepper-potts"})
            .weapons(new String[]{"arc-reactor", "iron-man-suit", "iron-legion"})
            .build();

        BDDMockito.given(heroService.findAllHeroes()).willReturn(Arrays.asList(hero1, hero2));
        var json = new ObjectMapper().writeValueAsString(heroes);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
            .get(HERO_API)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .content(json);

        mvc.perform(request)
            .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Should get all heroes with property 'power'")
    void itShouldReturnAllHeroesByPropertyPower() throws Exception {
        var heroes = buildListOfHeroes();

        var hero1 = HeroDto.builder()
            .name("Carol Danvers")
            .alias("Captain Marvel")
            .origin("Exposed to Space Stone reactor overload")
            .powers(new String[]{"photon-blast", "flight", "super-strength", "healing"})
            .associations(new String[]{"space-stone", "skrulls", "photon", "kree", "avengers"})
            .weapons(new String[0])
            .build();

        var hero2 = HeroDto.builder()
            .name("Iron Man")
            .alias("Tony Stark")
            .origin("Kidnapped in Afghanistan, created the first iron-man suit to escape.")
            .powers(new String[]{"genius-intelligence", "wealth", "flight"})
            .associations(new String[]{"war-machine", "avengers", "jarvis", "thanos", "pepper-potts"})
            .weapons(new String[]{"arc-reactor", "iron-man-suit", "iron-legion"})
            .build();

        BDDMockito.given(heroService.findHeroesByProperty(Mockito.anyString(), Mockito.anyString()))
            .willReturn(Arrays.asList(hero1, hero2));

        var json = new ObjectMapper().writeValueAsString(heroes);

        var uri = new StringBuilder(HERO_API)
            .append("/properties?")
            .append("property=power&")
            .append("value=flight");

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
            .get(uri.toString())
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .content(json);

        mvc.perform(request)
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].id").doesNotExist())
            .andExpect(jsonPath("$[0].name").value(heroes.get(0).getName()))
            .andExpect(jsonPath("$[0].alias").value(heroes.get(0).getAlias()))
            .andExpect(jsonPath("$[0].origin").value(heroes.get(0).getOrigin()))
            .andExpect(jsonPath("$[0].powers").isArray())
            .andExpect(jsonPath("$[0].weapons").isArray())
            .andExpect(jsonPath("$[0].associations").isArray())
            .andExpect(jsonPath("$[0].powers", hasSize(4)))
            .andExpect(jsonPath("$[0].weapons", hasSize(0)))
            .andExpect(jsonPath("$[0].associations", hasSize(5)))

            .andExpect(jsonPath("$[1].id").doesNotExist())
            .andExpect(jsonPath("$[1].name").value(heroes.get(1).getName()))
            .andExpect(jsonPath("$[1].alias").value(heroes.get(1).getAlias()))
            .andExpect(jsonPath("$[1].origin").value(heroes.get(1).getOrigin()))
            .andExpect(jsonPath("$[1].powers").isArray())
            .andExpect(jsonPath("$[1].weapons").isArray())
            .andExpect(jsonPath("$[1].associations").isArray())
            .andExpect(jsonPath("$[1].powers", hasSize(3)))
            .andExpect(jsonPath("$[1].weapons", hasSize(3)))
            .andExpect(jsonPath("$[1].associations", hasSize(5)));
    }

    @Test
    @DisplayName("Should throw BadRequestException when property is invalid")
    void itShouldThrowBadRequestExceptionWhenPropertyIsInvalid() throws Exception {
        var errorMessage = "The property informed doesn't exist: try power, weapon or association";
        var standardError =
            buildStandardError(HttpStatus.BAD_REQUEST, new BadRequestException(errorMessage));

        BDDMockito.given(heroService.findHeroesByProperty(Mockito.anyString(), Mockito.anyString()))
            .willThrow(new BadRequestException(errorMessage));

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
            .get(HERO_API + "/properties")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .param("property", "POWER")
            .param("value", "flight");

        mvc.perform(request)
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("status").value(standardError.getStatus()))
            .andExpect(jsonPath("message").value(standardError.getMessage()))
            .andExpect(jsonPath("error").value(standardError.getError()))
            .andExpect(jsonPath("timestamp").isNotEmpty())
            .andExpect(jsonPath("messages").doesNotExist());
    }

    @Test
    @DisplayName("Should update a hero")
    void itShouldUpdateAHero() throws Exception {
        var heroDto = buildNewHero();

        var heroSaved = HeroDto.builder()
            .name("Lucas Barbosa")
            .alias("Captain IT")
            .origin("Exposed to Java and programming logic")
            .powers(new String[]{"smart", "speed", "team-worker"})
            .associations(new String[]{"DataGuard"})
            .weapons(new String[]{"Java", "MacBook", "Git", "Microservices"})
            .build();

        BDDMockito.given(heroService.updateHero(Mockito.anyLong(), Mockito.any(HeroDto.class)))
            .willReturn(heroSaved);

        var json = new ObjectMapper().writeValueAsString(heroSaved);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
            .put(HERO_API + "/1")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .content(json);

        mvc.perform(request)
            .andExpect(status().isOk())
            .andExpect(jsonPath("id").doesNotExist())
            .andExpect(jsonPath("name", not(heroDto.getName())))
            .andExpect(jsonPath("name").value(heroSaved.getName()))
            .andExpect(jsonPath("alias", not(heroDto.getAlias())))
            .andExpect(jsonPath("alias").value(heroSaved.getAlias()))
            .andExpect(jsonPath("origin", not(heroDto.getOrigin())))
            .andExpect(jsonPath("origin").value(heroSaved.getOrigin()))
            .andExpect(jsonPath("powers").isArray())
            .andExpect(jsonPath("weapons").isArray())
            .andExpect(jsonPath("associations").isArray())
            .andExpect(jsonPath("powers", hasSize(3)))
            .andExpect(jsonPath("weapons", hasSize(4)))
            .andExpect(jsonPath("associations", hasSize(1)));
    }

    @Test
    @DisplayName("Should delete a hero")
    void itShouldDeleteAHero() throws Exception {
        var heroSaved = Hero.builder()
            .name("Lucas Barbosa")
            .alias("Captain IT")
            .origin("Exposed to Java and programming logic")
            .powers(new String[]{"smart", "speed", "team-worker"})
            .associations(new String[]{"DataGuard"})
            .weapons(new String[]{"Java", "MacBook", "Git", "Microservices"})
            .build();

        BDDMockito.given(heroService.findHeroById(Mockito.anyLong()))
            .willReturn(heroSaved);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
            .delete(HERO_API + "/1")
            .accept(MediaType.APPLICATION_JSON);

        mvc.perform(request)
            .andExpect(status().isNoContent());
    }

    private HeroDto buildNewHero() {
        return HeroDto.builder()
            .name("Carol Danvers")
            .alias("Captain Marvel")
            .origin("Exposed to Space Stone reactor overload")
            .powers(new String[]{"photon-blast", "flight", "super-strength", "healing"})
            .associations(new String[]{"space-stone", "skrulls", "photon", "kree", "avengers"})
            .weapons(new String[0])
            .build();
    }

    private HeroDto buildNewHeroDto() {
        return HeroDto.builder()
            .name("Carol Danvers")
            .alias("Captain Marvel")
            .origin("Exposed to Space Stone reactor overload")
            .powers(new String[]{"photon-blast", "flight", "super-strength", "healing"})
            .associations(new String[]{"space-stone", "skrulls", "photon", "kree", "avengers"})
            .weapons(new String[0])
            .build();
    }

    private List<HeroDto> buildListOfHeroes() {
        HeroDto hero1 = buildNewHero();

        HeroDto hero2 = HeroDto.builder()
            .name("Iron Man")
            .alias("Tony Stark")
            .origin("Kidnapped in Afghanistan, created the first iron-man suit to escape.")
            .powers(new String[]{"genius-intelligence", "wealth", "flight"})
            .associations(new String[]{"war-machine", "avengers", "jarvis", "thanos", "pepper-potts"})
            .weapons(new String[]{"arc-reactor", "iron-man-suit", "iron-legion"})
            .build();

        return Arrays.asList(hero1, hero2);
    }

    private StandardError buildStandardError(HttpStatus httpStatus, RuntimeException e) {
        return StandardError.builder()
            .status(httpStatus.value())
            .message(e.getMessage())
            .error(httpStatus.name())
            .build();
    }

    private StandardError buildStandardError(HttpStatus httpStatus, String[] messages) {
        return StandardError.builder()
            .status(httpStatus.value())
            .messages(messages)
            .error(httpStatus.name())
            .build();
    }

}
