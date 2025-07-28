package com.example.note_taking.mapper;

import com.example.note_taking.dto.NoteDto;
import com.example.note_taking.dto.NoteShareDto;
import com.example.note_taking.dto.ProfileDto;
import com.example.note_taking.models.Note;
import com.example.note_taking.models.NoteShare;
import com.example.note_taking.models.Profile;

import java.util.ArrayList;
import java.util.List;

public class ProfileMapper {
    public static ProfileDto mapToProfileDto(Profile profile) {
        ProfileDto profileDto = ProfileDto.builder()
                .id(profile.getId())
                .username(profile.getUsername())
                .firstName(profile.getFirstName())
                .lastName(profile.getLastName())
                .created(profile.getCreated())
                .lastLogin(profile.getLastLogin())
                .userRole(profile.getUserRole())
                .build();
        List<Note> notes = profile.getOwnedNotes();
        List<NoteDto> noteDtos = new ArrayList<>();
        for (Note note : notes) {
            noteDtos.add(NoteDto.builder()
                    .title(note.getTitle())
                    .content(note.getContent())
                    .id(note.getId())
                    .ownerProfile(profileDto)
                    .created(note.getCreated())
                    .lastEdited(note.getLastEdited())
                    .build());
            List<NoteShare> noteShares = note.getNoteShares();
            List<NoteShareDto> noteShareDtos = new ArrayList<>();
            for (NoteShare noteShare : noteShares) {
                Profile otherProfile = noteShare.getSharedWithProfile();
                ProfileDto otherProfileDto = ProfileDto.builder()
                        .id(otherProfile.getId())
                        .username(otherProfile.getUsername())
                        .firstName(otherProfile.getFirstName())
                        .lastName(otherProfile.getLastName())
                        .created(otherProfile.getCreated())
                        .lastLogin(otherProfile.getLastLogin())
                        .userRole(otherProfile.getUserRole())
                        .build();
                noteShareDtos.add(NoteShareMapper.mapToNoteShareDto(noteShare, otherProfileDto, noteDtos.getLast()));
            }
            noteDtos.getLast().setNoteShares(noteShareDtos);
        }

        profileDto.setOwnedNotes(noteDtos);
        profileDto.setSharedNotes(new ArrayList<>());
        for (NoteShare noteShare : profile.getSharedNotes()) {
            Note sharedNote = noteShare.getNote();
            profileDto.getSharedNotes().add(NoteShareMapper.mapToNoteShareDto(noteShare, profileDto, NoteDto.builder()
                    .id(sharedNote.getId())
                    .title(sharedNote.getTitle())
                    .lastEdited(sharedNote.getLastEdited())
                    .created(sharedNote.getCreated()).ownerProfile(ProfileDto.builder()
                            .id(sharedNote.getOwnerProfile().getId())
                            .username(sharedNote.getOwnerProfile().getUsername())
                            .build())
                    .build()));
        }

        return profileDto;
    }
}
