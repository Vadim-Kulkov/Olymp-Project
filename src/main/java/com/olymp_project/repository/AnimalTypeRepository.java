package com.olymp_project.repository;

import com.olymp_project.model.AnimalType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface AnimalTypeRepository extends JpaRepository<AnimalType, Long>, JpaSpecificationExecutor<AnimalType> {
}
