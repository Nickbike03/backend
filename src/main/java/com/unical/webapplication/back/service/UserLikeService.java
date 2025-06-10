package com.unical.webapplication.back.service;

import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.unical.webapplication.back.model.Document;
import com.unical.webapplication.back.persistence.implementation.UserLikeDocumentsDaoImpl;

@Service
public class UserLikeService {
    private final UserLikeDocumentsDaoImpl likeDao;

    @Autowired
    public UserLikeService(UserLikeDocumentsDaoImpl likeDao) {
        this.likeDao = likeDao;
    }

    public List<Document> getLikedDocumentsByUser(int userId) throws SQLException {
        return likeDao.getLikesByUser(userId);
    }

    public boolean addLike(int userId, int documentId) throws SQLException {
        return likeDao.insertLike(userId, documentId);
    }
    
    public boolean deleteLike (int userId, int documentId) throws  SQLException{
        return likeDao.deleteLike(userId, documentId);
    }
}



