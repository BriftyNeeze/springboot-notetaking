package com.example.note_taking.controller;

import com.example.note_taking.dto.ProfileDto;
import com.example.note_taking.dto.ProfileRegisterationDto;
import com.example.note_taking.models.Profile;
import com.example.note_taking.models.UserRole;
import com.example.note_taking.security.SecurityUtil;
import com.example.note_taking.service.ProfileService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Controller
public class ProfileController {
    private ProfileService profileService;

    public ProfileController(ProfileService profileService) {
        this.profileService = profileService;
    }

    @GetMapping("/users")
    public String listProfiles(Model model) {
        List<ProfileDto> profiles = profileService.findAllProfiles();
        model.addAttribute("users", profiles);
        return "profiles";
    }

    @GetMapping("/login")
    public String createLoginForm(Model model) {;
        return "login";
    }

    @GetMapping("/register")
    public String createProfileForm(Model model) {;
        ProfileRegisterationDto profile = new ProfileRegisterationDto();
        model.addAttribute("profile", profile);
        return "register";
    }

    @PostMapping("/register")
    public String createProfile(@Valid @ModelAttribute("profile") ProfileRegisterationDto profile, BindingResult result, Model model) {

        if (result.hasErrors()) {
            model.addAttribute("profile", profile);
            return "register";
        }
        Profile existingProfileEmail = profileService.findByEmail(profile.getEmail());
        if (existingProfileEmail != null && existingProfileEmail.getEmail() != null && !existingProfileEmail.getEmail().isEmpty()) {
            return "redirect:/register?fail";
        }
        Profile existingProfileUsername = profileService.findByUsername(profile.getUsername());
        if (existingProfileUsername != null && existingProfileUsername.getUsername() != null && !existingProfileUsername.getUsername().isEmpty()) {
            return "redirect:/register?fail";
        }
        profile.setUserRole(UserRole.USER);
        profileService.createProfile(profile);
        return "redirect:/?success";
    }

    @GetMapping("/profile")
    public String getProfilePage(Model model) {
        String username = SecurityUtil.getSessionUser();
        if (username == null) {
            return "redirect:/register?signup";
        }
        ProfileDto profileDto = profileService.getProfileDto(username);
        model.addAttribute("profile", profileDto);
        return "profile";
    }

    @DeleteMapping("/profile/{profileId}/delete")
    public String deleteProfile(@PathVariable("profileId") long profileId, Model model) {
        String username = SecurityUtil.getSessionUser();
        if (username == null) {
            return "redirect:/register?signup";
        }
        Profile profile = profileService.findById(profileId);
        Profile profile2 = profileService.findByUsername(username);
        if (profile2 == profile) {
            profile.setMarkedForDelete(true);
            profile.setRequestDeleteDate(LocalDateTime.now());
            profileService.saveProfile(profile);
            return "redirect:/logout";
        }

        return "redirect:/error-page";
    }
}
