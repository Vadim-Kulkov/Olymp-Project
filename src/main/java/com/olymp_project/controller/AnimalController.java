package com.olymp_project.controller;

import com.olymp_project.dto.AnimalDto;
import com.olymp_project.dto.converter.AnimalDtoConverter;
import com.olymp_project.model.Animal;
import com.olymp_project.repository.AnimalRepository;
import com.olymp_project.repository.specification.AnimalSpecification;
import com.olymp_project.repository.specification.criteria.SearchCriteria;
import com.olymp_project.types.Gender;
import com.olymp_project.types.LifeStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

import static com.olymp_project.repository.specification.AnimalSpecification.*;

@RestController()
@RequestMapping("/animals")
public class AnimalController {

    private final AnimalRepository animalRepository;
    private final AnimalDtoConverter dtoConverter;

    @Autowired
    public AnimalController(AnimalRepository animalRepository, AnimalDtoConverter dtoConverter) {
        this.animalRepository = animalRepository;
        this.dtoConverter = dtoConverter;
    }

    @GetMapping(value = "/{animalId}")
    public ResponseEntity<AnimalDto> getById(@PathVariable("animalId") String receivedId) {
        long id;
        try {
            id = Long.parseLong(receivedId);
        } catch (NumberFormatException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if (id <= 0) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Optional<Animal> receivedAnimal = animalRepository.findById(id);
        if (receivedAnimal.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        AnimalDto result = dtoConverter.convert(receivedAnimal.get());
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping(value = "/search")
    public ResponseEntity<List<AnimalDto>> searchByParameters(@RequestParam Map<String, String> searchParams) {
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

        if (searchParams.get(CHIPPER_ID_KEY) != null) {
            if (receivedValueIsLessThanOrEqualToZero(searchParams.get(CHIPPER_ID_KEY))) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        }
        if (searchParams.get(CHIPPING_LOCATION_ID_KEY) != null) {
            if (receivedValueIsLessThanOrEqualToZero(searchParams.get(CHIPPING_LOCATION_ID_KEY))) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        }
        if (searchParams.get(CHIPPING_DATE_TIME) != null) {
            if (receivedValueIsNotEmptyAndNotNull(searchParams.get(CHIPPING_DATE_TIME))) {
                if (isIsoDateTime(searchParams.get(CHIPPING_DATE_TIME))) {
                    return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
                }
            }
        }

        if (searchParams.get(LIFE_STATUS_KEY) != null) {
            if (lifeStatusIsIncorrect(searchParams.get(LIFE_STATUS_KEY))) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        }
        if (searchParams.get(GENDER_KEY) != null) {
            if (genderIsIncorrect(searchParams.get(GENDER_KEY))) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        }

        List<SearchCriteria> searchCriteria = AnimalSpecification.createCriteriaListForAllParams(searchParams);
        AnimalSpecification specification = new AnimalSpecification(searchCriteria);

        List<AnimalDto> result = dtoConverter.convert(animalRepository.findAll(specification, PageRequest.of(from, size)).getContent());
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    private static boolean lifeStatusIsIncorrect(String lifeStatus) {
        if (receivedValueIsNotEmptyAndNotNull(lifeStatus) && Arrays.asList(LifeStatus.values()).contains(LifeStatus.valueOf(lifeStatus))) {
            return false;
        }
        return true;
    }


    private static boolean genderIsIncorrect(String gender) {
        if (receivedValueIsNotEmptyAndNotNull(gender)
                && Arrays.asList(Gender.values()).contains(Gender.valueOf(gender))) {
            return false;
        }
        return true;
    }

    private static boolean receivedValueIsLessThanOrEqualToZero(String value) {
        return !receivedValueIsNotEmptyAndNotNull(value) || Integer.parseInt(value) > 0;
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
