package com.example.note_taking.service;

import com.example.note_taking.dto.ProfileDto;
import com.example.note_taking.dto.ProfileRegisterationDto;
import com.example.note_taking.models.Profile;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ProfileService {
    List<ProfileDto> findAllProfiles();
    Profile saveProfile(Profile profile);
    Profile createProfile(ProfileRegisterationDto profile);
    ProfileDto getProfileDto(String username);
    Profile findByEmail(String email);
    Profile findByUsername(String username);
    Profile findById(long id);
}
