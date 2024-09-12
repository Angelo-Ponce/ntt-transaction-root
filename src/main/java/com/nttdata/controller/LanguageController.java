package com.nttdata.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.LocaleResolver;

import java.util.Locale;

@RestController
@RequestMapping("api/v1/languages")
@RequiredArgsConstructor
public class LanguageController {

    private final LocaleResolver localeResolver;
    private final HttpServletRequest request;
    private final HttpServletResponse response;

    @GetMapping("/locale/{loc}")
    @PreAuthorize("hasRole('admin_client_role')")
    public ResponseEntity<Void> changeLocale(@PathVariable("loc") String loc) {
        Locale userLocale = switch (loc) {
            case "en", "us" -> Locale.ENGLISH;
            case "fr" -> Locale.FRENCH;
            default -> Locale.ROOT;
        };

        localeResolver.setLocale(request, response, userLocale);
        return ResponseEntity.ok().build();
    }
}