package com.example.note_taking.repository;

import com.example.note_taking.models.Note;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface NoteRepository extends JpaRepository<Note, Long> {
    Note findFirstById(Long id);

    @Query("SELECT n FROM Note n WHERE n.ownerProfile.id = :profileId AND (LOWER(n.title) LIKE %:query% OR LOWER(n.content) LIKE %:query%)")
    List<Note> findByProfileIdAndKeyword(@Param("profileId") Long profileId, @Param("query") String query);
}
