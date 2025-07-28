package com.example.note_taking.mapper;

import com.example.note_taking.dto.NoteDto;
import com.example.note_taking.dto.NoteShareDto;
import com.example.note_taking.dto.ProfileDto;
import com.example.note_taking.models.NoteShare;

public class NoteShareMapper {
    public static NoteShareDto mapToNoteShareDto(NoteShare noteShare, ProfileDto profile, NoteDto note) {
        NoteShareDto noteShareDto = NoteShareDto.builder()
                .id(noteShare.getId())
                .shareData(noteShare.getShareData())
                .permissionLevel(noteShare.getPermissionLevel())
                .sharedWithProfile(profile)
                .note(note)
                .build();

        return noteShareDto;
    }
}
