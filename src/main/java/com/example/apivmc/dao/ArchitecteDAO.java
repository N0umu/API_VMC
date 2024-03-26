package com.example.apivmc.dao;

import com.example.apivmc.models.Architecte;
import com.example.apivmc.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ArchitecteDAO extends JpaRepository<Architecte, Long> {


    @Query("select a from Architecte a where a.nom = :nom")
    Optional<Architecte> findExistingArchitecteWhereNomLike(@Param("nom") String nom);

    default Optional<Architecte> delete(long index) {
        Optional<Architecte> deleted = findById(index);
        if (deleted.isPresent()) {
            delete(deleted.get());
        }
        return deleted;
    }
}
