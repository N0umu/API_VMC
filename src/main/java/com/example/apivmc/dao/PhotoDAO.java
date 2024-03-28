package com.example.apivmc.dao;

import com.example.apivmc.models.Photo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PhotoDAO extends JpaRepository<Photo, Long> {
}
