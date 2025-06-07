package com.unical.webapplication.back.service;

import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.unical.webapplication.back.model.Document;
import com.unical.webapplication.back.persistence.implementation.DocumentDaoImpl;


@Service
public class DocumentService {

    private final DocumentDaoImpl documentDAO;

    @Autowired
    public DocumentService(DocumentDaoImpl documentDAO) {
        this.documentDAO = documentDAO;
    }

    public Document uploadDocument(Document document) throws SQLException {
        boolean success = documentDAO.insertDocument(document);
        if (!success) {
            throw new RuntimeException("Failed to upload document");
        }
        return document;
    }

    public Document getDocument(int documentId) throws SQLException {
        return documentDAO.findDocumentByID(documentId);
    }

    public Document downloadDocument(int documentId) throws SQLException {
        Document document = documentDAO.downloadDocumentById(documentId);
        if (document == null || document.getData() == null) {
            throw new RuntimeException("Document not found");
        }
        return document;
    }

    public Document getValidDocument(int documentId) throws SQLException {
        Document document = documentDAO.findValidDocumentById(documentId);
        if (document == null) {
            throw new RuntimeException("Document not found or not validated");
        }
        return document;
    }

    public Document getNotValidDocument(int documentId) throws SQLException {
        Document document = documentDAO.findNotValidDocumentById(documentId);
        if (document == null) {
            throw new RuntimeException("Document not found");
        }
        return document;
    }

    public List<Document> findAllDocuments() throws SQLException {
        return documentDAO.findAll();
    }

    public List<Document> getValidDocuments() throws SQLException {
        return documentDAO.findValidDocuments();
    }

    public List<Document> getNotValidDocuments() throws SQLException {
        return documentDAO.findNotValidDocuments();
    }

    public List<Document> getDocumentsByUserId(int userId) throws SQLException {
        return documentDAO.findDocumentByUserId(userId);
    }

    public List<Document> getDocumentsByCourse(String course) throws SQLException {
        return documentDAO.findDocumentByCourse(course);
    }

    public boolean deleteDocument(int documentId) throws SQLException {
        return documentDAO.deleteDocument(documentId);
    }

    public boolean validateDocument(int documentId, int adminId) throws SQLException {
        // Verifica esistenza documento
        Document doc = documentDAO.findDocumentByID(documentId);
        if (doc == null) {
            throw new RuntimeException("Document not found");
        }
        return documentDAO.updateValid(documentId, adminId);
    }
}
