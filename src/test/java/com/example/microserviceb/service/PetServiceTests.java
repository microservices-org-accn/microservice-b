package com.example.microserviceb.service;

import com.example.SwaggerCodgen.model.Category;
import com.example.SwaggerCodgen.model.Pet;
import com.example.SwaggerCodgen.model.Tag;
import com.example.microserviceb.cache.model.CacheData;
import com.example.microserviceb.cache.repository.CacheDataRepository;
import com.example.microserviceb.controller.PetController;
import com.example.microserviceb.service.impl.PetServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.MockitoTestExecutionListener;
import org.springframework.context.ApplicationContext;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class PetServiceTests {

    private String valueToAssert;

    @Mock
    private CacheDataRepository cacheDataRepository;

    @Mock
    private RestTemplate restTemplate = new RestTemplate();

    private UriComponentsBuilder uriComponentsBuilder;

    @InjectMocks
    private PetServiceImpl petService = new PetServiceImpl();

    private Pet pet;

    private List<Pet> petList = new ArrayList<>();

    private List<String> photoUrls = new ArrayList<>();

    private List<Tag> tagList = new ArrayList<>();

    private String jsonString;


    @BeforeEach
    public void setup(){

        valueToAssert = "available";

        photoUrls.add("string");

        Category category = new Category();
        category.setId(1L);
        category.setName("Dogs");

        Tag tag = new Tag();
        tag.setId(1L);
        tag.setName("Dogs");
        tagList.add(tag);

        Pet pet = new Pet();
        pet.setId(1L);
        pet.setName("doggie");
        pet.setCategory(category);
        pet.setPhotoUrls(photoUrls);
        pet.setTags(tagList);
        pet.setStatus(Pet.StatusEnum.AVAILABLE);
        petList.add(pet);

        jsonString = "[{\"id\":9223372036854765356,\"category\":{\"id\":0,\"name\":\"string\"},\"name\":\"fish\",\"photoUrls\":[\"string\"],\"tags\":[{\"id\":0,\"name\":\"string\"}],\"status\":\"available\"}]";

        uriComponentsBuilder = UriComponentsBuilder.fromHttpUrl("https://petstore.swagger.io/v2/pet/findByStatus")
                .queryParam("status", Pet.StatusEnum.AVAILABLE);

    }

    @Test
    public void givenMockingIsDoneByMockito_whenGetIsCalled_shouldReturnMockedObject() throws JsonProcessingException {
        //Mock del Redis
        CacheData cacheData = new CacheData("available", jsonString);
        Mockito.when(cacheDataRepository.findById("available")).thenReturn(Optional.of(cacheData));
        //Mock del Resttemplate
        /*Mockito.when(restTemplate.exchange(
                Mockito.eq(uriComponentsBuilder.toUriString()),
                Mockito.eq(HttpMethod.GET),
                Mockito.<HttpEntity<List<Pet>>>any(),
                Mockito.<ParameterizedTypeReference<List<Pet>>>any())
        ).thenReturn(new ResponseEntity(petList, HttpStatus.OK));*/

        ResponseEntity<List<Pet>> petAssert = petService.findPetsByStatus("available");

        System.out.println("Expected:" + valueToAssert);
        System.out.println("Actual:" + petAssert.getBody().get(0).getStatus().getValue());
        Assertions.assertEquals(valueToAssert, petAssert.getBody().get(0).getStatus().getValue());
    }
}
