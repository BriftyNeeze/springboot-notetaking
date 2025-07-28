package com.example.note_taking.repository;

import com.example.note_taking.models.Note;
import com.example.note_taking.models.NoteShare;
import com.example.note_taking.models.Profile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoteShareRepository extends JpaRepository<NoteShare, Long> {
    NoteShare findFirstById(Long id);
    NoteShare findFirstBySharedWithProfileAndNote(Profile profile, Note note);
}
