package com.olymp_project.controller;

import com.olymp_project.dto.AnimalDto;
import com.olymp_project.dto.converter.AnimalDtoConverter;
import com.olymp_project.model.Animal;
import com.olymp_project.repository.AnimalRepository;
import com.olymp_project.repository.specification.AnimalSpecification;
import com.olymp_project.repository.specification.criteria.SearchCriteria;
import com.olymp_project.types.LifeStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

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
        if (searchParams.get(CHIPPER_ID_KEY) != null && Integer.parseInt(searchParams.get(CHIPPER_ID_KEY)) <= 0) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if (searchParams.get(CHIPPING_LOCATION_ID_KEY) != null && Integer.parseInt(searchParams.get(CHIPPING_LOCATION_ID_KEY)) <= 0) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if (searchParams.get(LIFE_STATUS_KEY) != null && (searchParams.get(LIFE_STATUS_KEY) != LifeStatus.ALIVE.toString() || searchParams.get(LIFE_STATUS_KEY) != LifeStatus.DEAD.toString())) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if (searchParams.get(GENDER_KEY) != null && (searchParams.get(GENDER_KEY) != "MALE" || searchParams.get(GENDER_KEY) != "FEMALE" ||
                searchParams.get(GENDER_KEY) != "OTHER")) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        List<SearchCriteria> searchCriteria = AnimalSpecification.createCriteriaListForAllParams(searchParams);
        AnimalSpecification specification = new AnimalSpecification(searchCriteria);

        List<AnimalDto> result = dtoConverter.convert(animalRepository.findAll(specification, PageRequest.of(from, size)).getContent());
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

}
