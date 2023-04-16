package com.olymp_project.dto.converter;

import com.olymp_project.dto.AnimalLocationDto;
import com.olymp_project.model.AnimalLocation;
import org.springframework.stereotype.Component;

@Component
public class AnimalLocationDtoConverter implements GenericConverter<AnimalLocation, AnimalLocationDto> {

    @Override
    public AnimalLocationDto apply(AnimalLocation input) {
        AnimalLocationDto output = new AnimalLocationDto();
        output.setId(input.getId());
        output.setLocationPointId(input.getLocation().getId());
        output.setDateTimeOfVisitLocationPoint(input.getVisitDateTime());
        return output;
    }
}
