package com.example.note_taking.dto;

import com.example.note_taking.models.PermissionLevel;
import groovyjarjarpicocli.CommandLine;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
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
public class NoteShareCreateDto {
    @NotEmpty(message = "Provide valid username")
    private String profileUsername;
    @NotEmpty(message = "Set a permission level")
    private String permissionLevel;

    private LocalDateTime shareData;

}