package com.example.note_taking.dto;

import com.example.note_taking.models.NoteShare;
import com.example.note_taking.models.Profile;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NoteDto {
    private Long id;
    private ProfileDto ownerProfile;
    @NotEmpty(message = "Title Required")
    private String title;
    private String content;
    private LocalDateTime created;
    private LocalDateTime lastEdited;
    private List<NoteShareDto> noteShares = new ArrayList<>();
}
