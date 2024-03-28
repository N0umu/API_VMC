package com.example.apivmc.dao;

import com.example.apivmc.models.Batiment;
import com.example.apivmc.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BatimentDAO extends JpaRepository<Batiment, Long> {

    @Query("select b from Batiment b where b.lat = :lat and b.lon = :lon")
    Optional<Batiment> findExistingBatimentWhereLatAndLonLike(@Param("lat") String lat, @Param("lon") String lon);
    default Optional<Batiment> delete(long index) {
        Optional<Batiment> deleted = findById(index);
        if (deleted.isPresent()) {
            delete(deleted.get());
        }
        return deleted;
    }
}
