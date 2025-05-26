package com.unical.webapplication.back.persistence.DAO;

import java.sql.SQLException;
import java.util.List;

import com.unical.webapplication.back.model.Document;

public interface  IDocumentDAO {
    
    boolean insertDocument(Document document) throws SQLException; // Inserimento di un nuovo Documento

    List<Document> findAll() throws  SQLException;  // Da chiedere al professore --> Come fosso mandare i documenti solo se richiesti in questa funzione??
    
    Document findDocumentByID (int documentId) throws SQLException; //ricerca il documento nel database utilizzando l'id
    
    Document downloadDocumentById(int documentId) throws  SQLException; // ricerca il documento per id e ritorna SOLO il documento
    
    Document findValidDocumentById(int documentId) throws  SQLException; //ritorna solo i documenti che sono stati validati
    
    Document findNotValidDocumentById(int documentId) throws  SQLException;
    
    List<Document> findValidDocuments() throws SQLException;
    
    List<Document> findNotValidDocuments() throws SQLException;
    
    List<Document> findDocumentByUserId(int userId) throws SQLException;
    
    List<Document> findDocumentByCourse (String course) throws SQLException;
    
    boolean updateValid(int documentId, int adminId) throws SQLException;
    
    boolean deleteDocument(int documentId) throws SQLException;
}