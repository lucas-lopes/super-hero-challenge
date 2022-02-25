package com.dataguard.superherochallenge.repository;

import com.dataguard.superherochallenge.entity.Hero;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HeroRepository extends JpaRepository<Hero, Long> {

    Optional<Hero> findByName(final String name);

}
