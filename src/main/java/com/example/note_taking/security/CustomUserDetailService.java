package com.example.note_taking.security;


import com.example.note_taking.models.Profile;
import com.example.note_taking.repository.ProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;

@Service
public class CustomUserDetailService implements UserDetailsService {
    private ProfileRepository profileRepository;

    @Autowired
    public CustomUserDetailService(ProfileRepository profileRepository) {
        this.profileRepository = profileRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Profile profile = profileRepository.findFirstByUsername(username);
        if (profile != null && !profile.isMarkedForDelete()) {
            User authUser = new User(
                    profile.getUsername(),
                    profile.getPassword(),
                    Collections.singleton(new SimpleGrantedAuthority(profile.getUserRole().withPrefix()))
            );
            profile.setLastLogin(LocalDateTime.now());
            profileRepository.save(profile);
            return authUser;
        } else {
            throw new UsernameNotFoundException("Invalid username or password");
        }

    }
}
