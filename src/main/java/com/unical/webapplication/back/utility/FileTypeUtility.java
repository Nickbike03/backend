package com.unical.webapplication.back.utility;

import org.apache.tika.Tika;
import org.apache.tika.mime.MimeType;
import org.apache.tika.mime.MimeTypeException;
import org.apache.tika.mime.MimeTypes;

public class FileTypeUtility {
    private static final Tika tika = new Tika();
    private static final MimeTypes mimeTypes = MimeTypes.getDefaultMimeTypes();

    public static String detectFileExtension(byte[] data) {
        try {
            String mimeType = tika.detect(data);
            MimeType mimeTypeObj = mimeTypes.forName(mimeType);
            return mimeTypeObj.getExtension().replace(".", "");
        } catch (MimeTypeException e) {
            return "bin";
        }
    }

    public static String buildSafeFileName(String originalName, byte[] data) {
        String extension = detectFileExtension(data);
        
        // Rimuovi estensioni esistenti
        int lastDot = originalName.lastIndexOf('.');
        String baseName = (lastDot > 0) ? originalName.substring(0, lastDot) : originalName;
        
        // Sanitize del nome file
        String safeName = baseName.replaceAll("[^a-zA-Z0-9\\-]", "_");
        
        return safeName + "." + extension;
    }
}
