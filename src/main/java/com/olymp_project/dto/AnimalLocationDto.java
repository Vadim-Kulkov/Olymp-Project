package com.olymp_project.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AnimalLocationDto {

    private Long id;

    private LocalDateTime dateTimeOfVisitLocationPoint;

    private Long locationPointId;
}
