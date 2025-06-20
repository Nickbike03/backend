package com.unical.webapplication.back.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.unical.webapplication.back.model.Document;
import com.unical.webapplication.back.model.DocumentJsonResponse;
import com.unical.webapplication.back.service.DocumentService;
import com.unical.webapplication.back.utility.FileTypeUtility;


@RestController
@RequestMapping("/api") // potremi modificare d a questo punto per poter permettere il login in base al
                                  // ruolo che si ricopre
@CrossOrigin(origins = "http://localhost:4200")            
public class DocumentController {

    private final DocumentService documentService;

    @Autowired
    public DocumentController(DocumentService documentService) {
        this.documentService = documentService;
    }

    // Questo metodo permerre di caricare un document. Prestare attenzione al'ordine
    // e ai campi del metodo
    @PostMapping("auth/documents/upload")
    public ResponseEntity<?> uploadDocument(
            @RequestParam("data") MultipartFile file,
            @RequestParam("user_id") int userId,
            @RequestParam("name") String name,
            @RequestParam("description") String description,
            @RequestParam("course") String course) {

        //fare i controlli sia sullo user_id per vedere che l'utente è registrato
        // controllo per vedere tutti i dettagli sono inseriti
        // vedere di implementare un DTO per la gestione del body degli oggetti passati tramite la query
        
        try {
            Document document = new Document(
                    userId,
                    name,
                    description,
                    file.getBytes(),
                    course,
                    (int) file.getSize(),
                    false);
            Document savedDocument = documentService.uploadDocument(document);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedDocument);
        } catch (IOException | SQLException e) {
            return ResponseEntity.internalServerError().body("Error uploading document: " + e.getMessage());
        }
    }

    // questo metodo ritorna il document per come è caricato nel DB, compreso il
    // campo data del document
    @GetMapping("open/documents/{id}")
    public ResponseEntity<?> getDocument(@PathVariable int id) {
        try {
            Document document = documentService.getDocument(id);
            if (document == null) {
                return ResponseEntity.notFound().build();
            }
             HttpHeaders headers = new HttpHeaders();
             headers.setContentType(MediaType.APPLICATION_JSON);

            
            // headers.setContentType(MediaType.APPLICATION_PDF); // application.pdf mi permette di avere un anteprima del
            //                                                    // documento --> SOLO SE PDF
            // headers.setContentDisposition(ContentDisposition.attachment()
            //         .filename(document.getName())
            //         .build());

            return new ResponseEntity<>(document, headers, HttpStatus.OK);
        } catch (SQLException e) {
            return ResponseEntity.internalServerError().body("Error retrieving document: " + e.getMessage());
        }
    }

    // Questo metodo ritorna solo il campo "data" del document, per ottimizzare il
    // download del documento
    @GetMapping("auth/documents/download/{id}")
    public ResponseEntity<?> downloadDocument(@PathVariable int id) {
        try {
            Document document = documentService.downloadDocument(id);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM); // ritorna il documento come uno stream di dati
            headers.setContentDisposition(ContentDisposition.attachment()
                    .filename(document.getName())
                    .build());
            headers.setContentLength(document.getData().length);

            return new ResponseEntity<>(document.getData(), headers, HttpStatus.OK);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        } catch (SQLException e) {
            return ResponseEntity.internalServerError().body("Error downloading document: " + e.getMessage());
        }
    }

     @GetMapping("auth/documents/download-json/{id}")
    public ResponseEntity<?> downloadDocumentAsJson(@PathVariable int id) {
        try {
            Document document = documentService.getDocument(id);
            byte[] fileData = document.getData();
            
            // Genera nome file corretto
            String safeFileName = FileTypeUtility.buildSafeFileName(
                document.getName(), 
                fileData
            );
            
            // Estrai estensione
            String fileExtension = FileTypeUtility.detectFileExtension(fileData);
            
            // Converti in Base64
            String base64Content = java.util.Base64.getEncoder().encodeToString(fileData);
            
            // Crea risposta JSON
            DocumentJsonResponse response = new DocumentJsonResponse(
                safeFileName, 
                fileExtension, 
                base64Content
            );

            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(response);
            
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body("Errore nel download JSON: " + e.getMessage());
        }
    }

    // questo metodo ritorna il document per come è caricato nel DB, compreso il
    // campo data del document
    @GetMapping("open/documents/valid/{id}")
    public ResponseEntity<?> getValidDocument(@PathVariable int id) {
        try {
            Document document = documentService.getValidDocument(id);
            return ResponseEntity.ok(document);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (SQLException e) {
            return ResponseEntity.internalServerError().body("Database error: " + e.getMessage());
        }
    }

    // questo metodo ritorna il document per come è caricato nel DB, compreso il
    // campo data del document
    @GetMapping("open/documents/not-valid/{id}")
    public ResponseEntity<?> getNotValidDocument(@PathVariable int id) {
        try {
            Document document = documentService.getNotValidDocument(id);
            return ResponseEntity.ok(document);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (SQLException e) {
            return ResponseEntity.internalServerError().body("Database error: " + e.getMessage());
        }
    }

    // in questo metodo il campo "data" dei docuemnt non viene MAI restituito, per
    // migliorare l'efficenza delle operazioni.
    // Se si desidera avere anche il compo data bisogna utilizzare i metodi che
    // ritornano solo un documento nello specifico
    @GetMapping("/open/documents")
    public ResponseEntity<?> getAllDocuments() {
        try {
            List<Document> documents = documentService.findAllDocuments();
            return ResponseEntity.ok(documents);
        } catch (SQLException e) {
            return ResponseEntity.internalServerError()
                    .body("Error retrieving documents: " + e.getMessage());
        }
    }

    // in questo metodo il campo "data" dei docuemnt non viene MAI restituito, per
    // migliorare l'efficenza delle operazioni.
    // Se si desidera avere anche il compo data bisogna utilizzare i metodi che
    // ritornano solo un documento nello specifico
    @GetMapping("open/documents/valid")
    public ResponseEntity<?> getAllValidDocuments() {
        try {
            List<Document> documents = documentService.getValidDocuments();
            return ResponseEntity.ok(documents);
        } catch (SQLException e) {
            return ResponseEntity.internalServerError()
                    .body("Error retrieving valid documents: " + e.getMessage());
        }
    }

    // in questo metodo il campo "data" dei docuemnt non viene MAI restituito, per
    // migliorare l'efficenza delle operazioni.
    // Se si desidera avere anche il compo data bisogna utilizzare i metodi che
    // ritornano solo un documento nello specifico
    @GetMapping("auth/documents/not-valid")
    public ResponseEntity<?> getAllNotValidDocuments() {
        try {
            List<Document> documents = documentService.getNotValidDocuments();
            return ResponseEntity.ok(documents);
        } catch (SQLException e) {
            return ResponseEntity.internalServerError()
                    .body("Error retrieving non-valid documents: " + e.getMessage());
        }
    }

    // in questo metodo il campo "data" dei docuemnt non viene MAI restituito, per
    // migliorare l'efficenza delle operazioni.
    // Se si desidera avere anche il compo data bisogna utilizzare i metodi che
    // ritornano solo un documento nello specifico.
    // ATTENZIONE: Anche se lo userId non esiste viene restituita una lista vuota
    @GetMapping("auth/documents/user/{userId}")
    public ResponseEntity<?> getDocumentsByUser(@PathVariable int userId) {
        try {
            List<Document> documents = documentService.getDocumentsByUserId(userId);
            return ResponseEntity.ok(documents);
        } catch (SQLException e) {
            return ResponseEntity.internalServerError()
                    .body("Error retrieving user documents: " + e.getMessage());
        }
    }

    @GetMapping("open/documents/course/{courseName}")
    public ResponseEntity<?> getDocumentsByCourse(@PathVariable String courseName) {
        try {
            List<Document> documents = documentService.getDocumentsByCourse(courseName);
            return ResponseEntity.ok(documents);
        } catch (SQLException e) {
            return ResponseEntity.internalServerError()
                    .body("Error retrieving documents: " + e.getMessage());
        }
    }

    @DeleteMapping("auth/documents/{id}")
    public ResponseEntity<?> deleteDocument(@PathVariable int id) {
        try {
            boolean deleted = documentService.deleteDocument(id);
            if (deleted) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.notFound().build();
        } catch (SQLException e) {
            return ResponseEntity.internalServerError()
                    .body("Error deleting document: " + e.getMessage());
        }
    }

    @PatchMapping("admin/documents/{documentId}/validate/{adminId}")
    public ResponseEntity<?> validateDocument(
            @PathVariable int documentId,
            @PathVariable int adminId) {

        try {
            boolean updated = documentService.validateDocument(documentId, adminId);
            if (updated) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.status(HttpStatus.NOT_MODIFIED).build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (SQLException e) {
            return ResponseEntity.internalServerError()
                    .body("Validation error: " + e.getMessage());
        }
    }

}