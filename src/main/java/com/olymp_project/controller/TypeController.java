package com.olymp_project.controller;


import com.olymp_project.model.AnimalType;
import com.olymp_project.repository.AnimalTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController()
@RequestMapping("/animals/")
public class TypeController {

    private final AnimalTypeRepository typeRepository;

    @Autowired
    public TypeController(AnimalTypeRepository typeRepository) {
        this.typeRepository = typeRepository;

    }

    @GetMapping(value = "types/{typeId}")
    public ResponseEntity<AnimalType> findById(@PathVariable("typeId") String receivedId) {
        long id;
        try {
            id = Long.parseLong(receivedId);
        } catch (NumberFormatException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if (id <= 0) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Optional<AnimalType> result = typeRepository.findById(id);
        return result.map(type -> new ResponseEntity<>(type, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}
