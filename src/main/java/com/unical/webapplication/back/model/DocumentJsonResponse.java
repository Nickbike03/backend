package com.unical.webapplication.back.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class DocumentJsonResponse {
    @JsonProperty("fileName")
    private String fileName;
    
    @JsonProperty("fileExtension")
    private String fileExtension;
    
    @JsonProperty("base64Content")
    private String base64Content;

    public DocumentJsonResponse(String fileName, String fileExtension, String base64Content) {
        this.fileName = fileName;
        this.fileExtension = fileExtension;
        this.base64Content = base64Content;
    }

    // Getters
    public String getFileName() {
        return fileName;
    }

    public String getFileExtension() {
        return fileExtension;
    }

    public String getBase64Content() {
        return base64Content;
    }
}