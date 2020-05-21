package com.falynsky.smartmarkt.controllers;

import com.falynsky.smartmarkt.models.AppUser;
import com.falynsky.smartmarkt.models.Document;
import com.falynsky.smartmarkt.repositories.AppUserRepository;
import com.falynsky.smartmarkt.repositories.DocumentRepository;
import com.falynsky.smartmarkt.services.DocumentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.File;
import java.io.IOException;
import java.net.FileNameMap;
import java.net.MalformedURLException;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@CrossOrigin(origins = "http://localhost:8080", maxAge = 3600)
@RestController
@RequestMapping("/files")
public class FileUploadController {

    DocumentRepository documentRepository;
    DocumentService documentService;

    public FileUploadController(DocumentRepository documentRepository, DocumentService documentService) {
        this.documentRepository = documentRepository;
        this.documentService = documentService;
    }

    Logger logger = LoggerFactory.getLogger(FileUploadController.class);


    @PostMapping("/upload")
    public ResponseEntity uploadToLocalFileSystem(@RequestParam("file") MultipartFile file) {
        String fileName = documentService.uploadDocumentLocally(file);
        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/files/download/")
                .path(fileName)
                .toUriString();
        return ResponseEntity.ok(fileDownloadUri);
    }

    @PostMapping("/multi-upload")
    public ResponseEntity multiUpload(@RequestParam("files") MultipartFile[] files) {
        List<Object> fileDownloadUrls = new ArrayList<>();
        Arrays.asList(files)
                .stream()
                .forEach(file -> fileDownloadUrls.add(uploadToLocalFileSystem(file).getBody()));
        return ResponseEntity.ok(fileDownloadUrls);
    }

    @PostMapping("/upload-extra-param")
    public ResponseEntity uploadWithExtraParams(@RequestParam("file") MultipartFile file, @RequestParam String extraParam) {
        logger.info("Extra param " + extraParam);
        return uploadToLocalFileSystem(file);
    }

    @PostMapping("/upload/db")
    public ResponseEntity uploadToDatabase(@RequestParam("file") MultipartFile file) {
        String fileName = documentService.saveDocumentToDatabase(file);
        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/files/download/")
                .path(fileName).path("/db")
                .toUriString();
        return ResponseEntity.ok(fileDownloadUri);
    }

    @GetMapping("/download/{fileName:.+}/db")
    public ResponseEntity downloadFromDatabase(@PathVariable String fileName) {
        Document document = documentRepository.findByDocName(fileName);
        String mimeType = getMimeType(document);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(mimeType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                .body(document.getFileBytes());
    }

    private String getMimeType(Document document) {
        File file = new File(document.getDocName() + "." + document.getDocType());
        FileNameMap fileNameMap = URLConnection.getFileNameMap();
        return fileNameMap.getContentTypeFor(file.getName());
    }
}
