package com.unical.webapplication.back.controller;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.unical.webapplication.back.config.SecurityUtility;
import com.unical.webapplication.back.model.Utente;
import com.unical.webapplication.back.service.UtenteService;

@RestController
@RequestMapping("/api/users")
public class UtenteController {

    private final UtenteService utenteService;

    public UtenteController(UtenteService utenteService) {
        this.utenteService = utenteService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Utente utente) {
        if(utenteService.registerUser(utente)) {
            return ResponseEntity.status(HttpStatus.CREATED).build();
        }
        return ResponseEntity.badRequest().body("Registration failed");
    }

    @GetMapping("/me")
    public ResponseEntity<Utente> getCurrentUser() {
        Utente user = (Utente) SecurityUtility.getCurrentUser();
        if(user != null){
            return ResponseEntity.ok(user);
        }
        return ResponseEntity.status(401).build();
    }
    

    @GetMapping("/check-session")
    public ResponseEntity<?> checkSession() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
 
        if (authentication != null && authentication.isAuthenticated() && !"anonymousUser".equals(authentication.getPrincipal())) {
            Utente user = (Utente) authentication.getPrincipal();
            user.setPassword(null);
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Sessione non valida");
        }
    }
}
