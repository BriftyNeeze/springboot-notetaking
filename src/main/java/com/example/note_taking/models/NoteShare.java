package com.example.note_taking.models;

import groovyjarjarpicocli.CommandLine;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name="note_shares", uniqueConstraints = {@UniqueConstraint(columnNames = {"note_id", "shared_with_id"})})
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NoteShare {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "note_id", nullable = false)
    private Note note;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shared_with_id", nullable = false)
    private Profile sharedWithProfile;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PermissionLevel permissionLevel;

    @CreationTimestamp
    @Column(nullable = false)
    private LocalDateTime shareData;
    
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NoteShare noteShare = (NoteShare) o;
        return Objects.equals(id, noteShare.id);
    }
}
