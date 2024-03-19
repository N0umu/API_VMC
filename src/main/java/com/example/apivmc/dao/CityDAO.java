package com.example.apivmc.dao;

import com.example.apivmc.models.City;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CityDAO extends JpaRepository<City, Long> {

    default Optional<City> delete(long index) {
        Optional<City> deleted = findById(index);
        if (deleted.isPresent()) {
            delete(deleted.get());
        }
        return deleted;
    }
}
