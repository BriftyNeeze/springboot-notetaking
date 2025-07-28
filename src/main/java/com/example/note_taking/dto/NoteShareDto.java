package com.example.note_taking.dto;

import com.example.note_taking.models.PermissionLevel;
import groovyjarjarpicocli.CommandLine;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Objects;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NoteShareDto {
    private Long id;

    private NoteDto note;
    private ProfileDto sharedWithProfile;
    private PermissionLevel permissionLevel;

    private LocalDateTime shareData;


    public String getPermissionString() {
        return permissionLevel.toString();
    }

}
