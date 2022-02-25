package com.dataguard.superherochallenge.repository;

import com.dataguard.superherochallenge.entity.Hero;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface HeroRepository extends JpaRepository<Hero, Long> {

    Optional<Hero> findByName(final String name);

    @Query("select h from Hero h WHERE :power in elements(h.powers)")
    List<Hero> findByPower(@Param("power") final String value);

    @Query("select h from Hero h WHERE :weapon in elements(h.weapons)")
    List<Hero> findByWeapon(@Param("weapon") final String value);

    @Query("select h from Hero h WHERE :association in elements(h.associations)")
    List<Hero> findByAssociation(@Param("association") final String value);

}
