package com.example.microserviceb.service;

import com.example.SwaggerCodgen.model.Pet;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface PetService {
    ResponseEntity<List<Pet>> findPetsByStatus(String status);

}
