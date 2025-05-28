package com.unical.webapplication.back.persistence.implementation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.unical.webapplication.back.persistence.DAO.ILikeDocumentDAO;
import com.unical.webapplication.back.persistence.DBManager;

@Repository
public class UserLikeDocumentsDaoImpl implements ILikeDocumentDAO{



   

    @Override
    public List<Integer> getLikesByUser(int userId) throws SQLException {
        List<Integer> likedDocuments = new ArrayList<>();
        String sql = "SELECT document_id FROM user_like_documents WHERE user_id = ?";
        
        try (Connection conn = DBManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    likedDocuments.add(rs.getInt("document_id"));
                }
            }
        }
        return likedDocuments;
    }

    @Override
    public boolean insertLike(int userId, int documentId) throws SQLException {
        String sql = "INSERT INTO user_like_documents (user_id, document_id) VALUES (?, ?)";
        
        try (Connection conn = DBManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, userId);
            stmt.setInt(2, documentId);
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLIntegrityConstraintViolationException e) {
            return false;
        }
    }
}