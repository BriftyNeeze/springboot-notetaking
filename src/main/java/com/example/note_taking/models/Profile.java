package com.example.note_taking.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.beans.factory.annotation.Value;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "profiles")
public class Profile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    @CreationTimestamp
    private LocalDateTime created;
    @UpdateTimestamp
    private LocalDateTime lastLogin;

    @OneToMany(mappedBy = "ownerProfile", cascade = CascadeType.REMOVE)
    private List<Note> ownedNotes = new ArrayList<>();

    @OneToMany(mappedBy = "sharedWithProfile", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<NoteShare> sharedNotes = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private UserRole userRole;
    @Value("false")
    private boolean markedForDelete;
    @UpdateTimestamp
    private LocalDateTime requestDeleteDate;
}
