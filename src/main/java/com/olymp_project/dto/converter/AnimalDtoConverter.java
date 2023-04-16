package com.olymp_project.dto.converter;

import com.olymp_project.dto.AnimalDto;
import com.olymp_project.model.Animal;
import com.olymp_project.model.AnimalLocation;
import com.olymp_project.model.AnimalType;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
public class AnimalDtoConverter implements GenericConverter<Animal, AnimalDto> {

    @Override
    public AnimalDto apply(Animal input) {

        AnimalDto output = new AnimalDto();
        output.setId(input.getId());

        Set<AnimalType> animalTypeSet = input.getAnimalTypes();
        output.setAnimalTypes(animalTypeSet.stream().map(AnimalType::getId).collect(Collectors.toSet()));

        output.setWeight(input.getWeight());
        output.setLength(input.getLength());
        output.setHeight(input.getHeight());
        output.setGender(input.getGender());
        output.setLifeStatus(input.getLifeStatus());
        output.setChippingDateTime(input.getChippingDateTime());
        output.setChipperId(input.getChipper().getId());
        output.setChippingLocationId(input.getChippingLocation().getId());
        output.setVisitedLocations(input.getVisitedLocations().stream().map(AnimalLocation::getId).collect(Collectors.toSet()));
        output.setDeathDateTime(input.getDeathDateTime());
        return output;
    }
}
