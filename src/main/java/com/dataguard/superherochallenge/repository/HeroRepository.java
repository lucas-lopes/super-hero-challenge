package com.dataguard.superherochallenge.repository;

import com.dataguard.superherochallenge.entity.Hero;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HeroRepository extends JpaRepository<Hero, Long> {

}
