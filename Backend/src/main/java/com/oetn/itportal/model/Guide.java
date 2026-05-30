package com.oetn.itportal.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "guides")
public class Guide {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(name = "file_name", nullable = false)
    private String fileName;

    @Column(name = "file_path", nullable = false)
    private String filePath;

    @Column(name = "file_size")
    private Long fileSize;

    @Column(name = "uploaded_at")
    private LocalDateTime uploadedAt;

    public Guide() {}

    @PrePersist
    public void prePersist() { this.uploadedAt = LocalDateTime.now(); }

    // Getters & Setters
    public Long getId()                      { return id; }
    public void setId(Long id)               { this.id = id; }
    public String getTitle()                 { return title; }
    public void setTitle(String t)           { this.title = t; }
    public String getFileName()              { return fileName; }
    public void setFileName(String f)        { this.fileName = f; }
    public String getFilePath()              { return filePath; }
    public void setFilePath(String f)        { this.filePath = f; }
    public Long getFileSize()                { return fileSize; }
    public void setFileSize(Long s)          { this.fileSize = s; }
    public LocalDateTime getUploadedAt()     { return uploadedAt; }
    public void setUploadedAt(LocalDateTime u) { this.uploadedAt = u; }

    // Builder
    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private String title, fileName, filePath;
        private Long fileSize;

        public Builder title(String t)    { this.title = t; return this; }
        public Builder fileName(String f) { this.fileName = f; return this; }
        public Builder filePath(String f) { this.filePath = f; return this; }
        public Builder fileSize(Long s)   { this.fileSize = s; return this; }

        public Guide build() {
            Guide g = new Guide();
            g.title = title; g.fileName = fileName;
            g.filePath = filePath; g.fileSize = fileSize;
            return g;
        }
    }
}
