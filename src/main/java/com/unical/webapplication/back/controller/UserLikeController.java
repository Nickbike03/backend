package com.unical.webapplication.back.controller;

import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.unical.webapplication.back.service.UserLikeService;

@RestController
@RequestMapping("/api/likes")
public class UserLikeController {

    private final UserLikeService likeService;

    @Autowired
    public UserLikeController(UserLikeService likeService) {
        this.likeService = likeService;
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getLikedDocumentsByUser(@PathVariable int userId) {
        try {
            List<Integer> likedDocuments = likeService.getLikedDocumentsByUser(userId);
            return ResponseEntity.ok(likedDocuments);
        } catch (SQLException e) {
            return ResponseEntity.internalServerError()
                    .body("Error retrieving liked documents: " + e.getMessage());
        }
    }

    @PostMapping("/{userId}/{documentId}")
    public ResponseEntity<?> addLike(
        @PathVariable int userId,
        @PathVariable int documentId) {
        
        try {
            boolean success = likeService.addLike(userId, documentId);
            if(success) {
                return ResponseEntity.status(HttpStatus.CREATED).build();
            }
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Like already exists");
        } catch (SQLException e) {
            return ResponseEntity.internalServerError()
                    .body("Error adding like: " + e.getMessage());
        }
    }
    
}

