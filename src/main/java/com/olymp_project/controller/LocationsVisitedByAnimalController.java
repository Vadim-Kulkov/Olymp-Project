package com.olymp_project.controller;

import com.olymp_project.model.Location;
import com.olymp_project.repository.LocationRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/animals/{animalId}/locations")
public class LocationsVisitedByAnimalController {

    LocationRepository locationRepository;

    @GetMapping
    public ResponseEntity<Location> findLocations(@PathVariable("animalId") String receivedId, @RequestParam Map<String, String> searchParams) {
        long id;
        try {
            id = Long.parseLong(receivedId);
        } catch (NumberFormatException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if (id <= 0) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Optional<Location> result = locationRepository.findById(id);
        return result.map(location -> new ResponseEntity<>(location, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}
