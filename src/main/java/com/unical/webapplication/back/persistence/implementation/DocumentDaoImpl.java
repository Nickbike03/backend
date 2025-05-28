package com.unical.webapplication.back.persistence.implementation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.unical.webapplication.back.model.Document;
import com.unical.webapplication.back.persistence.DAO.IDocumentDAO;
import com.unical.webapplication.back.persistence.DBManager;



@Repository
public class DocumentDaoImpl implements IDocumentDAO {

   

    @Override
    public boolean insertDocument(Document document) throws SQLException {
        String sql = "INSERT INTO document (user_id, name, description, data, course, size, validated, validated_admin) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) { // PreparedStateman.RETURN_GENERATED_KEYS --> ritorna la
                                                            // chiave univoca generata per il docuemnto appena creato

            stmt.setInt(1, document.getUser_id());
            stmt.setString(2, document.getName());
            stmt.setString(3, document.getDescription());
            stmt.setBytes(4, document.getData());
            stmt.setString(5, document.getCourse());
            stmt.setInt(6, document.getSize());
            stmt.setBoolean(7, document.isValidated());

            // Gestione del campo nullable validated_admin
            if (document.getValidated_admin() != 0) {
                stmt.setInt(8, document.getValidated_admin());
            } else {
                stmt.setNull(8, java.sql.Types.INTEGER);
            }

            int rowsAffected = stmt.executeUpdate();

            // Recupera l'ID auto-generato in automatico dal DB e lo setto nel mio documento
            if (rowsAffected > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        document.setId(generatedKeys.getInt(1));
                    }
                }
                return true;
            }
            return false;
        }
    }

    @Override
    public Document findDocumentByID(int documentId) throws SQLException {
        String sql = "SELECT * FROM document WHERE id = ?";
        try (PreparedStatement stmt = DBManager.getConnection().prepareStatement(sql)) {
            stmt.setInt(1, documentId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Document doc = new Document(
                            rs.getInt("user_id"),
                            rs.getString("name"),
                            rs.getString("description"),
                            rs.getBytes("data"),
                            rs.getString("course"),
                            rs.getInt("size"),
                            rs.getBoolean("validated"));
                    doc.setId(rs.getInt("id"));
                    doc.setValidated_admin(rs.getInt("validated_admin"));
                    return doc;
                }
                return null;
            }
        }
    }

    @Override
    public Document downloadDocumentById(int documentId) throws SQLException {
        String sql = "SELECT name, data FROM document WHERE id = ?";
        try (PreparedStatement stmt = DBManager.getConnection().prepareStatement(sql)) {
            stmt.setInt(1, documentId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Document doc = new Document();
                    doc.setId(documentId);
                    doc.setName(rs.getString("name"));
                    doc.setData(rs.getBytes("data"));
                    return doc;
                }
                return null;
            }
        }
    }

    @Override
    public Document findValidDocumentById(int documentId) throws SQLException {
        String sql = "SELECT * FROM document WHERE id = ? AND validated = TRUE";
        try (PreparedStatement stmt = DBManager.getConnection().prepareStatement(sql)) {
            stmt.setInt(1, documentId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Document doc = new Document(
                            rs.getInt("user_id"),
                            rs.getString("name"),
                            rs.getString("description"),
                            rs.getBytes("data"),
                            rs.getString("course"),
                            rs.getInt("size"),
                            rs.getBoolean("validated"));
                    doc.setId(rs.getInt("id"));
                    doc.setValidated_admin(rs.getInt("validated_admin"));
                    return doc;
                }
                return null;
            }
        }
    }

    @Override
    public List<Document> findAll() throws SQLException {
        List<Document> documents = new ArrayList<>();
        String sql = "SELECT * FROM document";

        try (PreparedStatement stmt = DBManager.getConnection().prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Document doc = new Document(
                        rs.getInt("user_id"),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getString("course"),
                        rs.getInt("size"),
                        rs.getBoolean("validated"));
                doc.setId(rs.getInt("id"));
                doc.setValidated_admin(rs.getInt("validated_admin"));
                documents.add(doc);
            }
        }
        return documents;
    }

    @Override
    public Document findNotValidDocumentById(int documentId) throws SQLException {
        String sql = "SELECT * FROM document WHERE id = ? AND validated = FALSE";
        try (PreparedStatement stmt = DBManager.getConnection().prepareStatement(sql)) {
            stmt.setInt(1, documentId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Document doc = new Document(
                            rs.getInt("user_id"),
                            rs.getString("name"),
                            rs.getString("description"),
                            rs.getString("course"),
                            rs.getInt("size"),
                            rs.getBoolean("validated"));
                    doc.setId(rs.getInt("id"));
                    doc.setValidated_admin(rs.getInt("validated_admin"));
                    return doc;
                }
                return null;
            }
        }
    }

    // QUEste funzioni mi servono per non avere del codice ripetuto per due funzioni
    // che fanno lo stesso lavoro
    // ovvero quello di ricercare liste di documenti in base all'attributo boolenano
    // "validated"
    @Override
    public List<Document> findValidDocuments() throws SQLException {
        return getDocumentsByValidationStatus(true);
    }

    @Override
    public List<Document> findNotValidDocuments() throws SQLException {
        return getDocumentsByValidationStatus(false);
    }

    private List<Document> getDocumentsByValidationStatus(boolean validated) throws SQLException {
        List<Document> documents = new ArrayList<>();
        String sql = "SELECT * FROM document WHERE validated = ?";

        try (PreparedStatement stmt = DBManager.getConnection().prepareStatement(sql)) {
            stmt.setBoolean(1, validated);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Document doc = new Document(
                            rs.getInt("user_id"),
                            rs.getString("name"),
                            rs.getString("description"),
                            rs.getString("course"),
                            rs.getInt("size"),
                            rs.getBoolean("validated"));
                    doc.setId(rs.getInt("id"));
                    doc.setValidated_admin(rs.getInt("validated_admin"));
                    documents.add(doc);
                }
            }
        }
        return documents;
    }

    @Override
    public List<Document> findDocumentByUserId(int userId) throws SQLException {
        List<Document> documents = new ArrayList<>();
        String sql = "SELECT id, user_id, validated_admin, name, description, course, size, validated FROM document WHERE user_id = ?";

        try (PreparedStatement stmt = DBManager.getConnection().prepareStatement(sql)) {
            stmt.setInt(1, userId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Document doc = new Document();
                    doc.setId(rs.getInt("id"));
                    doc.setUser_id(rs.getInt("user_id"));
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
    public List<Document> findDocumentByCourse(String course) throws SQLException {
        List<Document> documents = new ArrayList<>();
        String sql = "SELECT id, user_id, validated_admin, name, description, course, size, validated FROM document WHERE LOWER(course) = LOWER(?)";

        try (PreparedStatement stmt = DBManager.getConnection().prepareStatement(sql)) {
            stmt.setString(1, course);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Document doc = new Document();
                    doc.setId(rs.getInt("id"));
                    doc.setUser_id(rs.getInt("user_id"));
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
    public boolean deleteDocument(int documentId) throws SQLException {
        String sql = "DELETE FROM document WHERE id = ?";

        try (PreparedStatement stmt = DBManager.getConnection().prepareStatement(sql)) {
            stmt.setInt(1, documentId);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        }
    }

    @Override
    public boolean updateValid(int documentId, int adminId) throws SQLException {
        String sql = "UPDATE document SET validated = TRUE, validated_admin = ? WHERE id = ?";

        try (PreparedStatement stmt = DBManager.getConnection().prepareStatement(sql)) {
            stmt.setInt(1, adminId);
            stmt.setInt(2, documentId);

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        }
    }
}
