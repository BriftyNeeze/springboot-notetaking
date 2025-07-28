package com.example.note_taking.repository;

import com.example.note_taking.models.Profile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProfileRepository extends JpaRepository<Profile, Long> {
    Profile findByEmail(String email);
    Profile findByUsername(String username);
    Profile findFirstByUsername(String username);
    Profile findById(long id);
}
