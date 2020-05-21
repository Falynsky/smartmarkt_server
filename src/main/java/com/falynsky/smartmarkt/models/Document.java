package com.falynsky.smartmarkt.models;

import javax.persistence.*;
import java.util.Arrays;
import java.util.Objects;

@Entity
@Table(name = "documents")
public class Document {

    @Id
    private Integer id;
    @Column(name = "doc_name")
    private String docName;
    @Column(name = "doc_type")
    private String docType;
    @Lob
    @Column(name = "file_bytes")
    private byte[] fileBytes;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDocName() {
        return docName;
    }

    public void setDocName(String docName) {
        this.docName = docName;
    }

    public byte[] getFileBytes() {
        return fileBytes;
    }

    public void setFileBytes(byte[] file) {
        this.fileBytes = file;
    }

    public String getDocType() {
        return docType;
    }

    public void setDocType(String docType) {
        this.docType = docType;
    }

    @Override
    public String toString() {
        return "Document{" +
                "id=" + id +
                ", docName='" + docName + '\'' +
                ", docType='" + docType + '\'' +
                ", fileBytes=" + Arrays.toString(fileBytes) +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Document document = (Document) o;
        return Objects.equals(id, document.id) &&
                Objects.equals(docName, document.docName) &&
                Objects.equals(docType, document.docType) &&
                Arrays.equals(fileBytes, document.fileBytes);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(id, docName, docType);
        result = 31 * result + Arrays.hashCode(fileBytes);
        return result;
    }
}
