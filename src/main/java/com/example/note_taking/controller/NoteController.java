package com.example.note_taking.controller;

import com.example.note_taking.dto.NoteDto;
import com.example.note_taking.dto.NoteShareCreateDto;
import com.example.note_taking.dto.NoteShareDto;
import com.example.note_taking.dto.ProfileDto;
import com.example.note_taking.mapper.NoteMapper;
import com.example.note_taking.models.Note;
import com.example.note_taking.models.NoteShare;
import com.example.note_taking.models.PermissionLevel;
import com.example.note_taking.models.Profile;
import com.example.note_taking.security.SecurityUtil;
import com.example.note_taking.service.NoteService;
import com.example.note_taking.service.NoteShareService;
import com.example.note_taking.service.ProfileService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Controller
public class NoteController {
    private NoteService noteService;
    private ProfileService profileService;
    private NoteShareService noteShareService;

    public NoteController(NoteService noteService, ProfileService profileService, NoteShareService noteShareService) {
        this.noteService = noteService;
        this.profileService = profileService;
        this.noteShareService = noteShareService;
    }

    @GetMapping("/create-notes")
    public String createNoteForm(Model model) {
        String username = SecurityUtil.getSessionUser();
        if (username == null) {
            return "redirect:/register?signup";
        }
        NoteDto note = new NoteDto();
        model.addAttribute("note", note);
        return "create-notes";
    }

    @PostMapping("/create-notes")
    public String saveProfile(@Valid @ModelAttribute("note") NoteDto note, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("note", note);
            return "create-notes";
        }
        String username = SecurityUtil.getSessionUser();
        ProfileDto profileDto = profileService.getProfileDto(username);
        note.setOwnerProfile(profileDto);
        noteService.saveNote(note);
        return "redirect:/notes";
    }

    @GetMapping("/notes")
    public String listNotes(Model model) {
        String username = SecurityUtil.getSessionUser();
        if (username == null) {
            return "redirect:/register?signup";
        }
        ProfileDto profileDto = profileService.getProfileDto(username);
        if (profileDto == null) {
            return "redirect:/register?signup";
        }
        model.addAttribute("profile", profileDto);
        model.addAttribute("notes", profileDto.getOwnedNotes());

        return "notes";
    }

    @GetMapping("/notes/search")
    public String listSearchedNotes(@RequestParam(value="query") String query, Model model) {
        String username = SecurityUtil.getSessionUser();
        if (username == null) {return "redirect:/register?signup";}
        ProfileDto profileDto = profileService.getProfileDto(username);
        if (profileDto == null) {return "redirect:/register?signup";}
        List<NoteDto> notes = noteService.findByProfileIdAndKeyword(profileDto.getId(), query);
        model.addAttribute("profile", profileDto);
        model.addAttribute("notes", notes);

        return "notes";
    }


    @GetMapping("/notes/{noteId}")
    public String noteEdit(@PathVariable("noteId") Long noteId, Model model) {
        String username = SecurityUtil.getSessionUser();
        if (username == null) {
            return "redirect:/register?signup";
        }
        ProfileDto profileDto = profileService.getProfileDto(username);
        NoteDto noteDto = noteService.findFirstByIdDto(noteId);

        if (noteDto == null || noteDto.getOwnerProfile().getId() != profileDto.getId()) {
            return "redirect:/error-page";
        }
        //model.addAttribute("noteId", noteId);
        model.addAttribute("note", noteDto);
        return "notes-edit";
    }

    @PostMapping("/notes/{noteId}")
    public String updateNote(@PathVariable("noteId") Long noteId, @Valid @ModelAttribute("note") NoteDto note, BindingResult result, Model model) {

        String username = SecurityUtil.getSessionUser();
        if (username == null) {
            return "redirect:/register";
        }
        if (result.hasErrors()) {
            model.addAttribute("note", note);
            return "notes-edit";
        }
        ProfileDto profileDto = profileService.getProfileDto(username);
        NoteDto noteDto = noteService.findFirstByIdDto(noteId);
        note.setId(noteId);
        note.setOwnerProfile(noteDto.getOwnerProfile());
        note.setCreated(noteDto.getCreated());
        if (note.getOwnerProfile().getId() != profileDto.getId()) {
            return "redirect:/error-page";
        }

        noteService.updateNote(note);

        return "redirect:/notes/{noteId}";
    }


    @DeleteMapping("/notes/{noteId}")
    public String deleteNote(@PathVariable("noteId") Long noteId, Model model) {
        String username = SecurityUtil.getSessionUser();
        if (username == null) {
            return "redirect:/register";
        }
        ProfileDto profileDto = profileService.getProfileDto(username);
        NoteDto noteDto = noteService.findFirstByIdDto(noteId);
        if (profileDto == null || noteDto == null) {
            return "redirect:/notes?error";
        }
        if (noteDto.getOwnerProfile().getId() != profileDto.getId()) {
            return "redirect:/notes?error";
        }
        noteService.delete(noteId);
        return "redirect:/notes?deleted";
    }

    @GetMapping("/notes/{noteId}/share")
    public String shareNotePage(@PathVariable("noteId") Long noteId, Model model) {
        String username = SecurityUtil.getSessionUser();
        if (username == null) {
            return "redirect:/register?signup";
        }
        ProfileDto profileDto = profileService.getProfileDto(username);
        NoteDto noteDto = noteService.findFirstByIdDto(noteId);

        if (noteDto == null || noteDto.getOwnerProfile().getId() != profileDto.getId()) {
            return "redirect:/error-page";
        }
        model.addAttribute("note", noteDto);
        model.addAttribute("newNoteShare", new NoteShareCreateDto());
        return "notes-share";
    }

    @PostMapping("/notes/{noteId}/share")
    public String shareNote(@PathVariable("noteId") Long noteId, @Valid @ModelAttribute("newNoteShare") NoteShareCreateDto noteShareCreateDto, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("noteShare", noteShareCreateDto);
            return "notes-share";
        }
        String username = SecurityUtil.getSessionUser();
        if (username == null) {
            return "redirect:/register";
        }
        Profile profile = profileService.findByUsername(username);
        Profile otherUser = profileService.findByUsername(noteShareCreateDto.getProfileUsername());
        if (otherUser== null || otherUser.getId() == profile.getId()) {
            return "redirect:/notes/{noteId}/share?notFound";
        }

        Note note = noteService.findFirstById(noteId);
        NoteShare existingNoteShare = noteShareService.findFirstBySharedWithProfileAndNote(otherUser, note);
        if (existingNoteShare != null) {
            return "redirect:/notes/{noteId}/share?alreadyShared";
        }

        NoteShare noteShare = NoteShare.builder()
                .sharedWithProfile(otherUser)
                .note(note)
                .permissionLevel(PermissionLevel.valueOf(noteShareCreateDto.getPermissionLevel()))
                .build();
        noteShareService.createNoteShare(noteShare);
        return "redirect:/notes/{noteId}/share?success";
    }

    @GetMapping("/notes/shared-with")
    public String getSharedWithNotes(Model model) {
        String username = SecurityUtil.getSessionUser();
        if (username == null) {
            return "redirect:/register?signup";
        }
        ProfileDto profileDto = profileService.getProfileDto(username);
        model.addAttribute("profile", profileDto);
        List<NoteShareDto> noteShareDtoList = new ArrayList<>();
        for (NoteShareDto noteShareDto : profileDto.getSharedNotes()) {
            Profile profile = profileService.findById(noteShareDto.getNote().getOwnerProfile().getId());
            if (!profile.isMarkedForDelete()) {
                noteShareDtoList.add(noteShareDto);
            }
        }
        model.addAttribute("noteShares", noteShareDtoList);
        return "shared-with";
    }

    @GetMapping("/notes/shared-with/{noteId}")
    public String getSharedNote(@PathVariable("noteId") Long noteId, Model model) {
        String username = SecurityUtil.getSessionUser();
        if (username == null) {
            return "redirect:/register?signup";
        }
        ProfileDto profileDto = profileService.getProfileDto(username);
        NoteDto noteDto = noteService.findFirstByIdDto(noteId);
        if (profileService.findById(noteDto.getOwnerProfile().getId()).isMarkedForDelete()) {
            return "redirect:/error-page";
        }
        List<NoteShareDto> noteShareDtoList = profileDto.getSharedNotes();
        for (NoteShareDto noteShareDto : noteShareDtoList) {
            if (Objects.equals(noteShareDto.getNote().getId(), noteId)) {
                model.addAttribute("note", noteDto);
                model.addAttribute("noteShare", noteShareDto);
                return "shared-note";
            }
        }
        //model.addAttribute("noteId", noteId);

        return "redirect:/error-page";
    }

    @DeleteMapping("/notes/{noteId}/{noteShareId}/remove")
    public String removeShare(@PathVariable("noteId") Long noteId, @PathVariable("noteShareId") Long noteShareId, Model model) {
        String username = SecurityUtil.getSessionUser();
        if (username == null) {
            return "redirect:/register?signup";
        }
        Profile profile = profileService.findByUsername(username);
        NoteShare noteShare = noteShareService.findFirstById(noteShareId);
        if (profile.getId() != noteShare.getNote().getOwnerProfile().getId()) {
            return "redirect:/error-page";
        }

        noteShareService.deleteById(noteShareId);
        return "redirect:/notes/{noteId}/share?removed";
    }
}