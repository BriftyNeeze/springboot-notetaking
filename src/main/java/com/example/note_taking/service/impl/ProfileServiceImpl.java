package com.example.note_taking.service.impl;

import com.example.note_taking.dto.NoteDto;
import com.example.note_taking.dto.NoteShareDto;
import com.example.note_taking.dto.ProfileDto;
import com.example.note_taking.dto.ProfileRegisterationDto;
import com.example.note_taking.mapper.ProfileMapper;
import com.example.note_taking.models.Note;
import com.example.note_taking.models.NoteShare;
import com.example.note_taking.models.Profile;
import com.example.note_taking.repository.ProfileRepository;
import com.example.note_taking.service.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProfileServiceImpl implements ProfileService {
    private ProfileRepository profileRepository;
    private PasswordEncoder passwordEncoder;


    @Autowired
    public ProfileServiceImpl(ProfileRepository profileRepository, PasswordEncoder passwordEncoder) {
        this.profileRepository = profileRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public List<ProfileDto> findAllProfiles() {
        List<Profile> profiles = profileRepository.findAll();
        return profiles.stream().map(ProfileMapper::mapToProfileDto).collect(Collectors.toList());
    }

    @Override
    public Profile saveProfile(Profile profile) {
        return profileRepository.save(profile);
    }

    @Override
    public Profile createProfile(ProfileRegisterationDto profileDto) {
        Profile profile = registerToProfile(profileDto);
        profile.setOwnedNotes(new ArrayList<Note>());
        return profileRepository.save(profile);
    }

    @Override
    public Profile findByEmail(String email) {
        return profileRepository.findByEmail(email);
    }

    @Override
    public Profile findById(long id) {
        return profileRepository.findById(id);
    }

    @Override
    public Profile findByUsername(String username) {
        return profileRepository.findByUsername(username);
    }
    @Override
    public ProfileDto getProfileDto(String username) {
        if (findByUsername(username) == null) {
            return null;
        }
        return ProfileMapper.mapToProfileDto(findByUsername(username));
    }

    private Profile registerToProfile(ProfileRegisterationDto profileRegister) {
        Profile profile = Profile.builder()
                .username(profileRegister.getUsername())
                .firstName(profileRegister.getFirstName())
                .lastName(profileRegister.getLastName())
                .email(profileRegister.getEmail())
                .password(passwordEncoder.encode(profileRegister.getPassword()))
                .userRole(profileRegister.getUserRole())
                .build();
        return profile;
    }




}
