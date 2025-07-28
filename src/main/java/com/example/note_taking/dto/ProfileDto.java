package com.example.note_taking.dto;

import com.example.note_taking.models.Note;
import com.example.note_taking.models.UserRole;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProfileDto {

    private long id;
    private String username;
    @NotEmpty(message = "First name required")
    private String firstName;
    @NotEmpty(message = "Last name required")
    private String lastName;
    private LocalDateTime created;
    private LocalDateTime lastLogin;
    private UserRole userRole;
    private List<NoteDto> ownedNotes;
    private List<NoteShareDto> sharedNotes;
}
