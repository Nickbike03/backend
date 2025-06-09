package com.unical.webapplication.back.persistence.DAO;

import java.sql.SQLException;
import java.util.List;

import com.unical.webapplication.back.model.Document;

public interface ILikeDocumentDAO {
    
    public List<Document> getLikesByUser(int userId) throws SQLException;
    public boolean insertLike(int userId, int documentId) throws SQLException;
}
