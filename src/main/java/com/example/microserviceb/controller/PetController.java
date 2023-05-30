package com.example.microserviceb.controller;

import com.example.SwaggerCodgen.api.PetApi;
import com.example.SwaggerCodgen.model.Pet;
import com.example.microserviceb.service.PetService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class PetController implements PetApi {

    private static final Logger logger = LogManager.getLogger(PetController.class);


    @Autowired
    PetService service;

    @Override
    public ResponseEntity<List<Pet>> findPetsByStatus(String status) {
        logger.info("Welcome to ELK demo service info");
        return service.findPetsByStatus(status);
    }
}
