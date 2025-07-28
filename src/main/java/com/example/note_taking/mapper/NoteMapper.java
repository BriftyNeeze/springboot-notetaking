package com.example.note_taking.mapper;

import com.example.note_taking.dto.NoteDto;
import com.example.note_taking.dto.NoteShareDto;
import com.example.note_taking.dto.ProfileDto;
import com.example.note_taking.models.Note;
import com.example.note_taking.models.NoteShare;
import com.example.note_taking.models.Profile;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class NoteMapper {
    public static NoteDto mapToNoteDto(Note note) {
        ProfileDto profileDto = ProfileMapper.mapToProfileDto(note.getOwnerProfile());
//        NoteDto noteDto = NoteDto.builder()
//                .id(note.getId())
//                .title(note.getTitle())
//                .content(note.getContent())
//                .created(note.getCreated())
//                .lastEdited(note.getLastEdited())
//                .ownerProfile(profileDto)
//                .build();
//        noteDto.setNoteShares(note.getNoteShares().stream().map(noteShare -> NoteShareMapper.mapToNoteShareDto(noteShare, profileDto, noteDto)).collect(Collectors.toList()));
        NoteDto foundNoteDto;
        for (NoteDto noteDto : profileDto.getOwnedNotes()) {
            if (Objects.equals(noteDto.getId(), note.getId())) {
                foundNoteDto = noteDto;
                return foundNoteDto;
            }
        }
        // should not happen
        return null;
    }

    public static Note noteDtoToNote(NoteDto noteDto) {
        ProfileDto profileDto = noteDto.getOwnerProfile();
        Profile profile = Profile.builder()
                .id(profileDto.getId())
                .firstName(profileDto.getFirstName())
                .lastName(profileDto.getLastName())
                .username(profileDto.getUsername())
                .created(profileDto.getCreated())
                .lastLogin(profileDto.getLastLogin())
                .userRole(profileDto.getUserRole())
                .build();
        Note note = Note.builder()
                .id(noteDto.getId())
                .title(noteDto.getTitle())
                .content(noteDto.getContent())
                .lastEdited(noteDto.getLastEdited())
                .created(noteDto.getCreated())
                .ownerProfile(profile)
                .build();
        List<NoteShare> noteShares = new ArrayList<>();
        for (NoteShareDto noteShareDto : noteDto.getNoteShares()) {
            noteShares.add(NoteShare.builder()
                    .id(noteShareDto.getId())
                    .note(note)
                    .sharedWithProfile(profile)
                    .permissionLevel(noteShareDto.getPermissionLevel())
                    .shareData(noteShareDto.getShareData())
                    .build());
        }
        note.setNoteShares(noteShares);
        return note;
    }
}
