package com.example.note_taking.service;

import com.example.note_taking.dto.NoteDto;
import com.example.note_taking.models.Note;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface NoteService {
    List<NoteDto> findAllNotes();
    NoteDto findFirstByIdDto(Long id);
    Note findFirstById(Long id);
    Note saveNote(NoteDto note);
    void updateNote(NoteDto noteDto);

    void delete(Long noteId);

    List<NoteDto> findByProfileIdAndKeyword(Long profileId, String query);
}