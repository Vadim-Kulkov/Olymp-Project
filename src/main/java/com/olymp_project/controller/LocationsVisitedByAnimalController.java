package com.olymp_project.controller;

import com.olymp_project.dto.AnimalLocationDto;
import com.olymp_project.dto.converter.AnimalLocationDtoConverter;
import com.olymp_project.model.Animal;
import com.olymp_project.repository.AnimalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.olymp_project.repository.specification.AnimalSpecification.*;

@RestController
@RequestMapping("/animals/{animalId}/locations")
public class LocationsVisitedByAnimalController {

    private final AnimalRepository animalRepository;
    private final AnimalLocationDtoConverter animalLocationDtoConverter;

    @Autowired
    public LocationsVisitedByAnimalController(AnimalRepository animalRepository, AnimalLocationDtoConverter animalLocationDtoConverter) {
        this.animalRepository = animalRepository;
        this.animalLocationDtoConverter = animalLocationDtoConverter;
    }

    @GetMapping
    public ResponseEntity<List<AnimalLocationDto>> findLocations(@PathVariable("animalId") String receivedId, @RequestParam Map<String, String> searchParams) {
        long id;
        try {
            id = Long.parseLong(receivedId);
        } catch (NumberFormatException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if (id <= 0) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        String receivedFrom = searchParams.get(FROM_KEY);
        String receivedSize = searchParams.get(SIZE_KEY);
        int from;
        int size;
        try {
            from = Objects.nonNull(receivedFrom) ? Integer.parseInt(receivedFrom) : 0;
            size = Objects.nonNull(receivedSize) ? Integer.parseInt(receivedSize) : 10;
        } catch (NumberFormatException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if (from < 0 || size <= 0) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if (!animalRepository.existsById(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        Animal matcher = animalRepository.getReferenceById(id);
        List<AnimalLocationDto> result = animalLocationDtoConverter.convert(matcher.getVisitedLocations());

        String receivedStartDateTime = searchParams.get(START_DATE_TIME_KEY);
        String receivedEndDateTime = searchParams.get(END_DATE_TIME_KEY);

        if (receivedValueIsNotEmptyAndNotNull(receivedStartDateTime)) {
            if (!isIsoDateTime(receivedStartDateTime)) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            LocalDateTime startDateTime = LocalDateTime.parse(receivedStartDateTime);
            result = getAllAnimalWhereDateTimeOfVisitLocationPointAfter(result, startDateTime);
        }

        if (receivedValueIsNotEmptyAndNotNull(receivedEndDateTime)) {
            if (!isIsoDateTime(receivedEndDateTime)) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            LocalDateTime endDateTime = LocalDateTime.parse(receivedEndDateTime);
            result = getAllAnimalWhereDateTimeOfVisitLocationPointBefore(result, endDateTime);
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    private static List<AnimalLocationDto> getAllAnimalWhereDateTimeOfVisitLocationPointAfter(List<AnimalLocationDto> animalLocationDtos, LocalDateTime startDateTime) {
        List<AnimalLocationDto> result = new ArrayList<>();
        for (AnimalLocationDto animalLocation : animalLocationDtos) {
            if (animalLocation.getDateTimeOfVisitLocationPoint().isAfter(startDateTime)) {
                result.add(animalLocation);
            }
        }
        return result;
    }

    private static List<AnimalLocationDto> getAllAnimalWhereDateTimeOfVisitLocationPointBefore(List<AnimalLocationDto> animalLocationDtos, LocalDateTime endDateTIme) {
        List<AnimalLocationDto> result = new ArrayList<>();
        for (AnimalLocationDto animalLocation : animalLocationDtos) {
            if (animalLocation.getDateTimeOfVisitLocationPoint().isBefore(endDateTIme)) {
                result.add(animalLocation);
            }
        }
        return result;
    }


    private static boolean receivedValueIsNotEmptyAndNotNull(String value) {
        return value != null && !value.isEmpty();
    }

    public static boolean isIsoDateTime(String date) {
        try {
            LocalDateTime.parse(date, DateTimeFormatter.ISO_DATE_TIME);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }
}
