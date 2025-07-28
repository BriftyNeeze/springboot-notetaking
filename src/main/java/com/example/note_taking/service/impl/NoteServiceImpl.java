package com.example.note_taking.service.impl;

import com.example.note_taking.dto.NoteDto;
import com.example.note_taking.dto.ProfileDto;
import com.example.note_taking.mapper.NoteMapper;
import com.example.note_taking.mapper.ProfileMapper;
import com.example.note_taking.models.Note;
import com.example.note_taking.models.NoteShare;
import com.example.note_taking.models.Profile;
import com.example.note_taking.repository.NoteRepository;
import com.example.note_taking.repository.ProfileRepository;
import com.example.note_taking.security.SecurityUtil;
import com.example.note_taking.service.NoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class NoteServiceImpl implements NoteService {
    private NoteRepository noteRepository;
    private ProfileRepository profileRepository;

    @Autowired
    public NoteServiceImpl(NoteRepository noteRepository, ProfileRepository profileRepository) {
        this.noteRepository = noteRepository;
        this.profileRepository = profileRepository;
    }

    @Override
    public List<NoteDto> findAllNotes() {
        List<Note> notes = noteRepository.findAll();
        return notes.stream().map((note) -> NoteMapper.mapToNoteDto(note)).collect(Collectors.toList());
    }

    @Override
    public Note findFirstById(Long id) {
        return noteRepository.findFirstById(id);
    }

    @Override
    public NoteDto findFirstByIdDto(Long id) {
        Note note = noteRepository.findFirstById(id);
        if (note == null) {
            return null;
        }
        return NoteMapper.mapToNoteDto(note);
    }

    @Override
    public List<NoteDto> findByProfileIdAndKeyword(Long profileId, String query) {
        List<Note> notes = noteRepository.findByProfileIdAndKeyword(profileId, query);
        return notes.stream().map((note) -> NoteMapper.mapToNoteDto(note)).collect(Collectors.toList());
    }

    @Override
    public Note saveNote(NoteDto noteDto) {
        String username = SecurityUtil.getSessionUser();
        System.out.println(username);
        Profile profile = profileRepository.findByUsername(username);
        Note note = NoteMapper.noteDtoToNote(noteDto);
        note.setOwnerProfile(profile);
        profile.getOwnedNotes().add(note);
        note.setNoteShares(new ArrayList<>());
        return noteRepository.save(note);
    }

    @Override
    public void updateNote(NoteDto noteDto) {
        String username = SecurityUtil.getSessionUser();
        Profile profile = profileRepository.findByUsername(username);
        Note note = NoteMapper.noteDtoToNote(noteDto);
        note.setOwnerProfile(profile);
        profile.getOwnedNotes().add(note);
        noteRepository.save(note);
    }

    @Override
    public void delete(Long noteId) {
        noteRepository.deleteById(noteId);
    }






}
