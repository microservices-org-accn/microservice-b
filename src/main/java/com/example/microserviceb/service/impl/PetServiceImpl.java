package com.example.microserviceb.service.impl;

import com.example.SwaggerCodgen.model.Pet;

import com.example.microserviceb.cache.model.CacheData;
import com.example.microserviceb.cache.repository.CacheDataRepository;
import com.example.microserviceb.service.PetService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Optional;

@Service
public class PetServiceImpl implements PetService {

    private static final Logger logger = LogManager.getLogger(PetServiceImpl.class);

    @Autowired
    private CacheDataRepository cacheDataRepository;
    @Autowired
    private RestTemplate restTemplate;
    private ObjectMapper objectMapper= new ObjectMapper();

    @Override
    public ResponseEntity<List<Pet>> findPetsByStatus(String status) {

            Optional<CacheData> optionalCacheData = cacheDataRepository.findById(status);
            // Cache hit
        try {
            if (optionalCacheData.isPresent()) {
                String petAsString = optionalCacheData.get().getValue();

                TypeReference<List<Pet>> mapType = new TypeReference<List<Pet>>() {
                };

                List<Pet> petList = objectMapper.readValue(petAsString, mapType);

                logger.info("Entré a REDIS");
                logger.info("response cache body message {} " + petList);

                return new ResponseEntity<>(petList, HttpStatus.OK);
            }
        }catch (Exception e){
            logger.info("error");
        }

            // request url
            String url = "https://petstore.swagger.io/v2/pet/findByStatus";

            //adding the query params to the URL
            UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(url)
                    .queryParam("status", status);

            HttpHeaders headers = new HttpHeaders();

            HttpEntity request = new HttpEntity(headers);

            ResponseEntity<List<Pet>> response = restTemplate.exchange(
                    uriBuilder.toUriString(),
                    HttpMethod.GET,
                    request,
                    new ParameterizedTypeReference<List<Pet>>() {
                    }
            );
// check response
            if (response.getStatusCode() == HttpStatus.OK) {
                System.out.println("Request Successful.");
                logger.info("response body message {} "+ response.getBody().toString());
                logger.info("response status code message {} "+ response.getStatusCode());
                try {
                    String petAsJsonString = objectMapper.writeValueAsString(response.getBody());
                    CacheData cacheData = new CacheData(status, petAsJsonString);

                    cacheDataRepository.save(cacheData);
                } catch (Exception e){
                    logger.info("error");
                }
                return new ResponseEntity<>(response.getBody(), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
    }

}
