package com.unical.webapplication.back.persistence.implementation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.unical.webapplication.back.model.Document;
import com.unical.webapplication.back.persistence.DAO.ILikeDocumentDAO;
import com.unical.webapplication.back.persistence.DBManager;

@Repository
public class UserLikeDocumentsDaoImpl implements ILikeDocumentDAO{



   

    @Override
    public List<Document> getLikesByUser(int userId) throws SQLException {
        List<Document> documents = new ArrayList<>();
        String sql = "SELECT d.id, d.user_id, validated_admin, d.name, data, description, course, size, validated from document d , user_like_documents uld, utente u   WHERE d.id = uld.document_id and u.id = uld.user_id and u.id = ?";

        try (PreparedStatement stmt = DBManager.getConnection().prepareStatement(sql)) {
            stmt.setInt(1, userId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Document doc = new Document();
                    doc.setId(rs.getInt("id"));
                    doc.setUser_id(rs.getInt("user_id"));
                    doc.setData(rs.getBytes("data"));
                    doc.setValidated_admin(rs.getInt("validated_admin"));
                    doc.setName(rs.getString("name"));
                    doc.setDescription(rs.getString("description"));
                    doc.setCourse(rs.getString("course"));
                    doc.setSize(rs.getInt("size"));
                    doc.setValidated(rs.getBoolean("validated"));

                    documents.add(doc);
                }
            }
        }
        return documents;
    
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