package com.olymp_project.controller;

import com.olymp_project.model.Animal;
import com.olymp_project.repository.AnimalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController()
@RequestMapping("/animals")
public class AnimalController {


    private final AnimalRepository animalRepository;

    @Autowired
    public AnimalController(AnimalRepository animalRepository) {
        this.animalRepository = animalRepository;
    }

    @GetMapping("/all")
    public List<Animal> getAll() {
        return animalRepository.findAll();
    }

    @GetMapping(value = "/{id}")
    public Animal findById(@PathVariable("id") Long id) {
        return animalRepository.findById(id).orElse(null);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public long create(@RequestBody Animal resource) {
        Objects.requireNonNull(resource);
        return animalRepository.save(resource).getId();
    }
}
