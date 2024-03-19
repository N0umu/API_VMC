package com.example.apivmc.dao;

import com.example.apivmc.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserDAO extends JpaRepository<User, Long> {

    default Optional<User> delete(long index) {
        Optional<User> deleted = findById(index);
        if (deleted.isPresent()) {
            delete(deleted.get());
        }
        return deleted;
    }
}
