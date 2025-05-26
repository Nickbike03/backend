package com.unical.webapplication.back.persistence.DAO;

import java.sql.SQLException;
import java.util.List;

public interface ILikeDocumentDAO {
    
    public List<Integer> getLikesByUser(int userId) throws SQLException;
    public boolean insertLike(int userId, int documentId) throws SQLException;
}
