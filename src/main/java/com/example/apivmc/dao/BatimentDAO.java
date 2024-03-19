package com.example.apivmc.dao;

import com.example.apivmc.models.Batiment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BatimentDAO extends JpaRepository<Batiment, Long> {

    default Optional<Batiment> delete(long index) {
        Optional<Batiment> deleted = findById(index);
        if (deleted.isPresent()) {
            delete(deleted.get());
        }
        return deleted;
    }
}
