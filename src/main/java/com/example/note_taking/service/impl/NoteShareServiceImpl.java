package com.example.note_taking.service.impl;

import com.example.note_taking.dto.NoteShareDto;
import com.example.note_taking.models.Note;
import com.example.note_taking.models.NoteShare;
import com.example.note_taking.models.Profile;
import com.example.note_taking.repository.NoteRepository;
import com.example.note_taking.repository.NoteShareRepository;
import com.example.note_taking.repository.ProfileRepository;
import com.example.note_taking.service.NoteShareService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NoteShareServiceImpl implements NoteShareService {
    private NoteRepository noteRepository;
    private ProfileRepository profileRepository;
    private NoteShareRepository noteShareRepository;

    @Autowired
    public NoteShareServiceImpl(NoteRepository noteRepository, ProfileRepository profileRepository, NoteShareRepository noteShareRepository) {
        this.noteRepository = noteRepository;
        this.profileRepository = profileRepository;
        this.noteShareRepository = noteShareRepository;
    }


    @Override
    public void createNoteShare(NoteShare noteShare) {
        noteShareRepository.save(noteShare);
    }

    @Override
    public NoteShare findFirstById(Long id) {
        return noteShareRepository.findFirstById(id);
    }

    @Override
    public NoteShare findFirstBySharedWithProfileAndNote(Profile profile, Note note) {
        return noteShareRepository.findFirstBySharedWithProfileAndNote(profile, note);
    }

    @Override
    public void deleteById(Long id) {
        noteShareRepository.deleteById(id);
    }
}
