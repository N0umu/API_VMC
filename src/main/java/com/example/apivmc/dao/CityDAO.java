package com.example.apivmc.dao;

import com.example.apivmc.models.Architecte;
import com.example.apivmc.models.Batiment;
import com.example.apivmc.models.City;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CityDAO extends JpaRepository<City, Long> {

    @Query("select c from City c where c.nom = :nom")
    Optional<City> findExistingCityWhereNomLike(@Param("nom") String nom);

    @Query("select c.batiments from City c where c.nom = :nom")
    List<Batiment> findBatimentsWhereCityNomLike(@Param("nom") String nom);

    default Optional<City> delete(long index) {
        Optional<City> deleted = findById(index);
        if (deleted.isPresent()) {
            delete(deleted.get());
        }
        return deleted;
    }
}
