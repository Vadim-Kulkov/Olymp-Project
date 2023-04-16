package com.olymp_project.model;

import com.olymp_project.types.Gender;
import com.olymp_project.types.LifeStatus;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "animal", schema = "public")
public class Animal implements Serializable {

    @Id
    @Column(name = "id", unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinTable(
            name = "animal_animaltype",
            joinColumns = @JoinColumn(name = "animal_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "animaltype_id", referencedColumnName = "id"))
    private Set<AnimalType> animalTypes = new HashSet<>();

    @Column(name = "weight")
    private Float weight;

    @Column(name = "length")
    private Float length;

    @Column(name = "height")
    private Float height;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender")
    private Gender gender;

    @Enumerated(EnumType.STRING)
    @Column(name = "life_status")
    private LifeStatus lifeStatus;

    @Column(name = "chipping_date_time")
    private LocalDateTime chippingDateTime;


    @OneToOne(cascade = CascadeType.REFRESH)
    @JoinColumn(name = "chipper_id")
    private Account chipper;

    @ManyToOne
    @JoinColumn(name = "chipping_location_id")
    private Location chippingLocation;

    @OneToMany(mappedBy = "animal", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    private Set<AnimalLocation> visitedLocations;

    @Column(name = "death_date_time")
    private LocalDateTime deathDateTime;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Animal animal = (Animal) o;

        if (!chippingDateTime.equals(animal.chippingDateTime)) return false;
        if (!chipper.equals(animal.chipper)) return false;
        if (!chippingLocation.equals(animal.chippingLocation)) return false;
        return Objects.equals(deathDateTime, animal.deathDateTime);
    }

    @Override
    public int hashCode() {
        int result = chippingDateTime.hashCode();
        result = 31 * result + chipper.hashCode();
        result = 31 * result + chippingLocation.hashCode();
        result = 31 * result + (deathDateTime != null ? deathDateTime.hashCode() : 0);
        return result;
    }
}
