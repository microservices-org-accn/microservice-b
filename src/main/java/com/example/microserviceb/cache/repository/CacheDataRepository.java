package com.example.microserviceb.cache.repository;

import com.example.microserviceb.cache.model.CacheData;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CacheDataRepository extends CrudRepository<CacheData, String> {
}
