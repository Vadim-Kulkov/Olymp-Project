package com.olymp_project.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "animal_location", schema = "public")
public class AnimalLocation implements Serializable {

    @Id
    @Column(name = "id", unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "animal_id")
    Animal animal;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "location_id")
    Location location;

    @Column(name = "visit_datetime")
    private LocalDateTime visitDateTime;
}
