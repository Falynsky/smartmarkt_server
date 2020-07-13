package com.falynsky.smartmarkt.services;

import com.falynsky.smartmarkt.models.Document;
import com.falynsky.smartmarkt.repositories.DocumentRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class DocumentService {

    DocumentRepository documentRepository;
    private String fileBasePath = "C:\\Users\\kamil\\Desktop\\SmartMarktProject\\smartmarkt\\src\\main\\resources\\files\\";

    public DocumentService(DocumentRepository documentRepository) {
        this.documentRepository = documentRepository;
    }

    public String uploadDocumentLocally(@RequestParam("file") MultipartFile file) {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        Path path = Paths.get(fileBasePath + fileName);
        try {
            Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fileName;
    }

    public String saveDocumentToDatabase(@RequestParam("file") MultipartFile file) {
        Document doc = new Document();
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        String[] fileNameType = fileName.split("\\.");
        doc.setDocName(fileNameType[0]);
        doc.setDocType(fileNameType[1]);
        try {
            doc.setFileBytes(file.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
        Integer newDocId = getIdForNewDocument(documentRepository);
        doc.setId(newDocId);
        documentRepository.save(doc);
        return fileName;
    }

    private Integer getIdForNewDocument(DocumentRepository documentRepository) {
        Document lastDocument = documentRepository.findFirstByOrderByIdDesc();
        if (lastDocument == null) {
            return 1;
        }
        Integer lastId = lastDocument.getId();
        return ++lastId;
    }
}
