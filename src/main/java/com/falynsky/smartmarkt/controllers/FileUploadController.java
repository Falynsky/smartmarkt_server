package com.falynsky.smartmarkt.controllers;

import com.falynsky.smartmarkt.models.Document;
import com.falynsky.smartmarkt.repositories.DocumentRepository;
import com.falynsky.smartmarkt.services.DocumentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@CrossOrigin
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
    public ResponseEntity<String> uploadToLocalFileSystem(@RequestParam("file") MultipartFile file) {
        String fileName = documentService.uploadDocumentLocally(file);
        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/files/download/")
                .path(fileName)
                .toUriString();
        return ResponseEntity.ok(fileDownloadUri);
    }

    @PostMapping("/multi-upload")
    public ResponseEntity<List<Object>> multiUpload(@RequestParam("files") MultipartFile[] files) {
        List<Object> fileDownloadUrls = new ArrayList<>();
        Arrays.stream(files)
                .forEach(file -> fileDownloadUrls.add(uploadToLocalFileSystem(file).getBody()));
        return ResponseEntity.ok(fileDownloadUrls);
    }

    @PostMapping("/upload-extra-param")
    public ResponseEntity<String> uploadWithExtraParams(@RequestParam("file") MultipartFile file, @RequestParam String extraParam) {
        logger.info("Extra param " + extraParam);
        return uploadToLocalFileSystem(file);
    }

    @PostMapping("/upload/db")
    public ResponseEntity<String> uploadToDatabase(@RequestParam("file") MultipartFile file) {
        String fileName = documentService.saveDocumentToDatabase(file);
        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/files/download/")
                .path(fileName).path("/db")
                .toUriString();
        return ResponseEntity.ok(fileDownloadUri);
    }

    @GetMapping("/download/{fileName:.+}/db")
    public ResponseEntity<Object> getPictureFromDatabase(@PathVariable String fileName) {
        String[] fileNameType = fileName.split("\\.");
        Document document = documentRepository.findByDocName(fileNameType[0]);
        if (document == null) {
            return null;
        }

        MediaType mediaType = getMediaType(document);
        byte[] fileBytes = document.getFileBytes();
        return ResponseEntity.ok()
                .contentType(mediaType)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                .body(fileBytes);
    }


    @GetMapping("/download/{fileName:.+}/db/{width:.+}/{height:.+}")
    public ResponseEntity<Object> getScaledPictureUsingWidthAndHeightFromDatabase(@PathVariable String fileName, @PathVariable String width, @PathVariable String height) throws Exception {
        String[] fileNameType = fileName.split("\\.");
        Document document = documentRepository.findByDocName(fileNameType[0]);
        if (document == null) {
            return null;
        }
        byte[] pictureBytes = document.getFileBytes();
        int widthValue = Integer.parseInt(width);
        int heightValue = Integer.parseInt(height);
        byte[] scaledPictureBytes = scalePicture(pictureBytes, widthValue, heightValue, fileNameType[1]);


        MediaType mediaType = getMediaType(document);
        return ResponseEntity.ok()
                .contentType(mediaType)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                .body(scaledPictureBytes);
    }


    public byte[] scalePicture(byte[] fileData, int width, int height, String extension) throws Exception {
        ByteArrayInputStream in = new ByteArrayInputStream(fileData);
        try {
            BufferedImage img = ImageIO.read(in);
            return getScaledBytes(extension, img, width, height);
        } catch (IOException e) {
            throw new Exception();
        }
    }

    @GetMapping("/download/{fileName:.+}/db/{percent:.+}")
    public ResponseEntity<Object> getScaledPictureUsingPercentFromDatabase(@PathVariable String fileName, @PathVariable String percent) throws Exception {
        String[] fileNameType = fileName.split("\\.");
        Document document = documentRepository.findByDocName(fileNameType[0]);
        if (document == null) {
            return null;
        }
        byte[] pictureBytes = document.getFileBytes();
        double percentValue = Double.parseDouble(percent);
        byte[] scaledPictureBytes = scalePictureByPercent(pictureBytes, percentValue, fileNameType[1]);


        MediaType mediaType = getMediaType(document);
        return ResponseEntity.ok()
                .contentType(mediaType)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                .body(scaledPictureBytes);
    }

    public byte[] scalePictureByPercent(byte[] fileData, double percent, String extension) throws Exception {
        ByteArrayInputStream in = new ByteArrayInputStream(fileData);
        try {
            BufferedImage img = ImageIO.read(in);
            int width = (int) (img.getWidth() * percent);
            int height = (int) (img.getHeight() * percent);
            return getScaledBytes(extension, img, width, height);
        } catch (IOException e) {
            throw new Exception();
        }
    }

    private byte[] getScaledBytes(String extension, BufferedImage img, int width, int height) throws IOException {
        Image scaledImage = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        BufferedImage imageBuff = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        imageBuff.getGraphics().drawImage(scaledImage, 0, 0, new Color(0, 0, 0), null);

        ByteArrayOutputStream buffer = new ByteArrayOutputStream();

        ImageIO.write(imageBuff, extension, buffer);

        return buffer.toByteArray();
    }

    MediaType getMediaType(Document document) {
        File file = new File(document.getDocName() + "." + document.getDocType());
        FileNameMap fileNameMap = URLConnection.getFileNameMap();
        String mimeType = fileNameMap.getContentTypeFor(file.getName());
        return MediaType.parseMediaType(mimeType);
    }

}
