package com.olymp_project.dto;

import com.olymp_project.types.Gender;
import com.olymp_project.types.LifeStatus;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;

@Data
public class AnimalDto {

    private Long id;

    private Set<Long> animalTypes;

    private Float weight;

    private Float length;

    private Float height;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Enumerated(EnumType.STRING)
    private LifeStatus lifeStatus;

    private LocalDateTime chippingDateTime;

    private Long chipperId;

    private Long chippingLocationId;

    private Set<Long> visitedLocations;

    private LocalDateTime deathDateTime;
}
