package com.olymp_project.controller;

import com.olymp_project.model.Location;
import com.olymp_project.repository.LocationRepository;
import com.olymp_project.repository.specification.LocationSpecification;
import com.olymp_project.repository.specification.criteria.SearchCriteria;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.olymp_project.repository.specification.AnimalSpecification.FROM_KEY;
import static com.olymp_project.repository.specification.AnimalSpecification.SIZE_KEY;

@RestController
@RequestMapping("/animals/{animalId}/locations")
public class LocationsVisitedByAnimalController {

    LocationRepository locationRepository;

    @GetMapping
    public ResponseEntity<List<Location>> findLocations(@PathVariable("animalId") String receivedId, @RequestParam Map<String, String> searchParams) {
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
        List<SearchCriteria> searchCriteriaList = LocationSpecification.createCriteriaListForAllParams(searchParams);
        LocationSpecification specification = new LocationSpecification(searchCriteriaList);
        List<Location> result = locationRepository.findAll(specification, PageRequest.of(from, size)).getContent();
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
