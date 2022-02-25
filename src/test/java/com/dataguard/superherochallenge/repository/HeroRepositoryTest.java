package com.dataguard.superherochallenge.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.dataguard.superherochallenge.entity.Hero;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@DataJpaTest
class HeroRepositoryTest {

    @Autowired
    TestEntityManager testEntityManager;

    @Autowired
    HeroRepository heroRepository;

    @Test
    @DisplayName("Should return a Hero by name with success")
    void findByNameTest_shouldReturnHeroByNameWithSuccess() {
        var name = "Carol Danvers";
        var newHero = buildNewHero();
        testEntityManager.persist(newHero);

        var hero = heroRepository.findByName(name);

        assertThat(hero.isPresent()).isTrue();
        assertThat(hero.get().getName()).isEqualTo("Carol Danvers");
        assertThat(hero.get().getAlias()).isEqualTo("Captain Marvel");
        assertThat(hero.get().getOrigin()).isEqualTo("Exposed to Space Stone reactor overload");
        assertThat(hero.get().getPowers()).hasSize(4);
        assertThat(hero.get().getWeapons()).isEmpty();
        assertThat(hero.get().getAssociations()).hasSize(5);
    }

    @Test
    @DisplayName("Should return a Hero by property 'power' and value 'flight'")
    void findByPower_shouldReturnHeroByPropertyAndValue() {
        var powerValue = "flight";
        var heroes = buildListOfHeroes();
        testEntityManager.persist(heroes.get(0));
        testEntityManager.persist(heroes.get(1));

        var hero = heroRepository.findByPower(powerValue);

        assertThat(hero).hasSize(2);

        assertThat(hero.get(0)).isNotNull();
        assertThat(hero.get(0).getName()).isEqualTo("Carol Danvers");
        assertThat(hero.get(0).getAlias()).isEqualTo("Captain Marvel");
        assertThat(hero.get(0).getOrigin()).isEqualTo("Exposed to Space Stone reactor overload");
        assertThat(hero.get(0).getPowers()).hasSize(4);
        assertThat(hero.get(0).getWeapons()).isEmpty();
        assertThat(hero.get(0).getAssociations()).hasSize(5);

        assertThat(hero.get(1)).isNotNull();
        assertThat(hero.get(1).getName()).isEqualTo("Tony Stark");
        assertThat(hero.get(1).getAlias()).isEqualTo("Iron Man");
        assertThat(hero.get(1).getOrigin()).isEqualTo("Kidnapped in Afghanistan, created the first iron-man suit to escape.");
        assertThat(hero.get(1).getPowers()).hasSize(3);
        assertThat(hero.get(1).getWeapons()).hasSize(3);
        assertThat(hero.get(1).getAssociations()).hasSize(5);
    }

    @Test
    @DisplayName("Should return a Hero by property 'weapon' and value 'arc-reactor'")
    void findByWeapon_shouldReturnHeroByPropertyAndValue() {
        var weaponValue = "arc-reactor";
        var heroes = buildListOfHeroes();
        testEntityManager.persist(heroes.get(0));
        testEntityManager.persist(heroes.get(1));

        var hero = heroRepository.findByWeapon(weaponValue);

        assertThat(hero).hasSize(1);

        assertThat(hero.get(0)).isNotNull();
        assertThat(hero.get(0).getName()).isEqualTo("Tony Stark");
        assertThat(hero.get(0).getAlias()).isEqualTo("Iron Man");
        assertThat(hero.get(0).getOrigin()).isEqualTo("Kidnapped in Afghanistan, created the first iron-man suit to escape.");
        assertThat(hero.get(0).getPowers()).hasSize(3);
        assertThat(hero.get(0).getWeapons()).hasSize(3);
        assertThat(hero.get(0).getAssociations()).hasSize(5);
    }

    @Test
    @DisplayName("Should return a Hero by property 'association' and value 'avengers'")
    void findByAssociation_shouldReturnHeroByPropertyAndValue() {
        var associationValue = "avengers";
        var heroes = buildListOfHeroes();
        testEntityManager.persist(heroes.get(0));
        testEntityManager.persist(heroes.get(1));

        var hero = heroRepository.findByAssociation(associationValue);

        assertThat(hero).hasSize(2);

        assertThat(hero.get(0)).isNotNull();
        assertThat(hero.get(0).getName()).isEqualTo("Carol Danvers");
        assertThat(hero.get(0).getAlias()).isEqualTo("Captain Marvel");
        assertThat(hero.get(0).getOrigin()).isEqualTo("Exposed to Space Stone reactor overload");
        assertThat(hero.get(0).getPowers()).hasSize(4);
        assertThat(hero.get(0).getWeapons()).isEmpty();
        assertThat(hero.get(0).getAssociations()).hasSize(5);

        assertThat(hero.get(1)).isNotNull();
        assertThat(hero.get(1).getName()).isEqualTo("Tony Stark");
        assertThat(hero.get(1).getAlias()).isEqualTo("Iron Man");
        assertThat(hero.get(1).getOrigin()).isEqualTo("Kidnapped in Afghanistan, created the first iron-man suit to escape.");
        assertThat(hero.get(1).getPowers()).hasSize(3);
        assertThat(hero.get(1).getWeapons()).hasSize(3);
        assertThat(hero.get(1).getAssociations()).hasSize(5);
    }

    private Hero buildNewHero() {
        return Hero.builder()
            .name("Carol Danvers")
            .alias("Captain Marvel")
            .origin("Exposed to Space Stone reactor overload")
            .powers(new String[]{"photon-blast", "flight", "super-strength", "healing"})
            .associations(new String[]{"space-stone", "skrulls", "photon", "kree", "avengers"})
            .weapons(new String[0])
            .build();
    }

    private List<Hero> buildListOfHeroes() {
        var hero1 = buildNewHero();

        var hero2 = Hero.builder()
            .name("Tony Stark")
            .alias("Iron Man")
            .origin("Kidnapped in Afghanistan, created the first iron-man suit to escape.")
            .powers(new String[]{"genius-intelligence", "wealth", "flight"})
            .associations(new String[]{"war-machine", "avengers", "jarvis", "thanos", "pepper-potts"})
            .weapons(new String[]{"arc-reactor", "iron-man-suit", "iron-legion"})
            .build();

        return Arrays.asList(hero1, hero2);
    }

}
