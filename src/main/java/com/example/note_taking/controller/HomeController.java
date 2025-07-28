package com.example.note_taking.controller;

import com.example.note_taking.dto.ProfileDto;
import com.example.note_taking.security.SecurityUtil;
import com.example.note_taking.service.ProfileService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController {
    private ProfileService profileService;

    public HomeController(ProfileService profileService) {
        this.profileService = profileService;
    }

    @GetMapping("/")
    public String index(Model model) {
        return "index";
    }

    @GetMapping("/error-page")
    public String errorPage(Model model) {
        return "error-page";
    }

}
