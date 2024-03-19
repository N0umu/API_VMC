package com.example.apivmc.dao;

import com.example.apivmc.models.Architecte;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ArchitecteDAO extends JpaRepository<Architecte, Long> {

    default Optional<Architecte> delete(long index) {
        Optional<Architecte> deleted = findById(index);
        if (deleted.isPresent()) {
            delete(deleted.get());
        }
        return deleted;
    }
}
