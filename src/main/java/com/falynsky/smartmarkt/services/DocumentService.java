package com.falynsky.smartmarkt.services;

import com.falynsky.smartmarkt.models.DTO.ProductDTO;
import com.falynsky.smartmarkt.models.Document;
import com.falynsky.smartmarkt.repositories.DocumentRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Service
public class DocumentService {

    DocumentRepository documentRepository;
    final int FILE_NAME_INDEX = 0;
    final int FILE_TYPE_INDEX = 1;

    public DocumentService(DocumentRepository documentRepository) {
        this.documentRepository = documentRepository;
    }

    public String uploadDocumentLocally(@RequestParam("file") MultipartFile file) {
        String originalFilename = file.getOriginalFilename();
        String fileName = StringUtils.cleanPath(Objects.requireNonNull(originalFilename));
        String fileBasePath = "C:\\Users\\kamil\\Desktop\\SmartMarktProject\\smartmarkt\\src\\main\\resources\\files\\";
        Path path = Paths.get(fileBasePath + fileName);
        try {
            final InputStream inputStream = file.getInputStream();
            Files.copy(inputStream, path, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fileName;
    }

    public String saveDocumentToDatabase(@RequestParam("file") MultipartFile file) {
        Document doc = new Document();
        String originalFilename = file.getOriginalFilename();
        String fileName = StringUtils.cleanPath(Objects.requireNonNull(originalFilename));
        String[] fileNameType = fileName.split("\\.");

        doc.setDocName(fileNameType[FILE_NAME_INDEX]);

        doc.setDocType(fileNameType[FILE_TYPE_INDEX]);
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

    public Map<String, Object> getDocumentDataByDocumentId(Integer documentId ) {
        Map<String, Object> documentData = new HashMap<>();
        Optional<Document> optionalDocument = documentRepository.findById(documentId);
        if (optionalDocument.isPresent()) {
            Document document = optionalDocument.get();
            documentData.put("documentId", documentId);
            documentData.put("documentName", document.getDocName());
            documentData.put("documentType", document.getDocType());
        }
        return documentData;
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
