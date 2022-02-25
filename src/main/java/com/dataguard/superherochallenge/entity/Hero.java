package com.dataguard.superherochallenge.entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OrderColumn;
import javax.persistence.SequenceGenerator;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Hero implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(
        name = "hero_sequence",
        sequenceName = "hero_sequence"
    )
    @GeneratedValue(
        strategy = GenerationType.SEQUENCE,
        generator = "hero_sequence"
    )
    private Long id;

    @Column(nullable = false)
    private String alias;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String origin;

    @ElementCollection
    @OrderColumn(name = "position")
    private String[] powers;

    @ElementCollection
    @OrderColumn(name = "position")
    private String[] weapons;

    @ElementCollection
    @OrderColumn(name = "position")
    private String[] associations;

}