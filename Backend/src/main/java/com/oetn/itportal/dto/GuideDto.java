package com.oetn.itportal.dto;

import java.time.LocalDateTime;

public class GuideDto {
    private Long id;
    private String title, fileName, downloadUrl;
    private Long fileSize;
    private LocalDateTime uploadedAt;

    public GuideDto() {}

    public Long getId()                      { return id; }
    public void setId(Long id)               { this.id = id; }
    public String getTitle()                 { return title; }
    public void setTitle(String t)           { this.title = t; }
    public String getFileName()              { return fileName; }
    public void setFileName(String f)        { this.fileName = f; }
    public String getDownloadUrl()           { return downloadUrl; }
    public void setDownloadUrl(String u)     { this.downloadUrl = u; }
    public Long getFileSize()                { return fileSize; }
    public void setFileSize(Long s)          { this.fileSize = s; }
    public LocalDateTime getUploadedAt()     { return uploadedAt; }
    public void setUploadedAt(LocalDateTime u) { this.uploadedAt = u; }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private Long id, fileSize;
        private String title, fileName, downloadUrl;
        private LocalDateTime uploadedAt;

        public Builder id(Long id)               { this.id = id; return this; }
        public Builder title(String t)           { this.title = t; return this; }
        public Builder fileName(String f)        { this.fileName = f; return this; }
        public Builder fileSize(Long s)          { this.fileSize = s; return this; }
        public Builder downloadUrl(String u)     { this.downloadUrl = u; return this; }
        public Builder uploadedAt(LocalDateTime u){ this.uploadedAt = u; return this; }

        public GuideDto build() {
            GuideDto g = new GuideDto();
            g.id = id; g.title = title; g.fileName = fileName;
            g.fileSize = fileSize; g.downloadUrl = downloadUrl;
            g.uploadedAt = uploadedAt;
            return g;
        }
    }
}
