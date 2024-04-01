package com.example.apivmc.dao;

import com.example.apivmc.models.Batiment;
import com.example.apivmc.models.Photo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PhotoDAO extends JpaRepository<Photo, Long> {
    List<Photo> findByBatiment(Batiment batiment);
}
