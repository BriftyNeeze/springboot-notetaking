package com.example.note_taking.service;

import com.example.note_taking.models.Note;
import com.example.note_taking.models.NoteShare;
import com.example.note_taking.models.Profile;

public interface NoteShareService {
    void createNoteShare(NoteShare noteShare);
    NoteShare findFirstById(Long id);
    NoteShare findFirstBySharedWithProfileAndNote(Profile profile, Note note);
    void deleteById(Long id);
}
