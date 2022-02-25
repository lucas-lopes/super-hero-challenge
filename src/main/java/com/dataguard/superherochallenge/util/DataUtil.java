package com.dataguard.superherochallenge.util;

import com.dataguard.superherochallenge.entity.Hero;
import com.dataguard.superherochallenge.repository.HeroRepository;
import java.util.Arrays;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class DataUtil implements CommandLineRunner {

    private final HeroRepository heroRepository;

    // Initial data to be easier to test
    @Override
    public void run(String... args) throws Exception {
        Hero hero1 = Hero.builder()
            .name("Carol Danvers")
            .alias("Captain Marvel")
            .origin("Exposed to Space Stone reactor overload")
            .powers(new String[]{"photon-blast", "flight", "super-strength", "healing"})
            .associations(new String[]{"space-stone", "skrulls", "photon", "kree", "avengers"})
            .weapons(new String[0])
            .build();

        Hero hero2 = Hero.builder()
            .name("Tony Stark")
            .alias("Iron Man")
            .origin("Kidnapped in Afghanistan, created the first iron-man suit to escape.")
            .powers(new String[]{"genius-intelligence", "wealth", "flight"})
            .associations(new String[]{"war-machine", "avengers", "jarvis", "thanos", "pepper-potts"})
            .weapons(new String[]{"arc-reactor", "iron-man-suit", "iron-legion"})
            .build();

        heroRepository.saveAll(Arrays.asList(hero1, hero2));
    }
}
