package com.oetn.itportal.dto;

import com.oetn.itportal.model.SoftwareRequest;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;

public class SoftwareRequestDto {

    public static class CreateRequest {
        @NotBlank(message = "Le nom du logiciel est requis")
        private String softwareName;
        @NotBlank(message = "La raison est requise")
        private String reason;

        public CreateRequest() {}
        public String getSoftwareName()        { return softwareName; }
        public void setSoftwareName(String s)  { this.softwareName = s; }
        public String getReason()              { return reason; }
        public void setReason(String r)        { this.reason = r; }
    }

    public static class UpdateStatusRequest {
        private SoftwareRequest.Status status;
        public UpdateStatusRequest() {}
        public SoftwareRequest.Status getStatus()        { return status; }
        public void setStatus(SoftwareRequest.Status s)  { this.status = s; }
    }

    public static class Response {
        private Long id;
        private String softwareName, reason, status, userEmail, username;
        private LocalDateTime createdAt, updatedAt;

        public Response() {}

        public Long getId()                     { return id; }
        public void setId(Long id)              { this.id = id; }
        public String getSoftwareName()         { return softwareName; }
        public void setSoftwareName(String s)   { this.softwareName = s; }
        public String getReason()               { return reason; }
        public void setReason(String r)         { this.reason = r; }
        public String getStatus()               { return status; }
        public void setStatus(String s)         { this.status = s; }
        public String getUserEmail()            { return userEmail; }
        public void setUserEmail(String e)      { this.userEmail = e; }
        public String getUsername()             { return username; }
        public void setUsername(String u)       { this.username = u; }
        public LocalDateTime getCreatedAt()     { return createdAt; }
        public void setCreatedAt(LocalDateTime c) { this.createdAt = c; }
        public LocalDateTime getUpdatedAt()     { return updatedAt; }
        public void setUpdatedAt(LocalDateTime u) { this.updatedAt = u; }

        public static Builder builder() { return new Builder(); }

        public static class Builder {
            private Long id;
            private String softwareName, reason, status, userEmail, username;
            private LocalDateTime createdAt, updatedAt;

            public Builder id(Long id)               { this.id = id; return this; }
            public Builder softwareName(String s)    { this.softwareName = s; return this; }
            public Builder reason(String r)          { this.reason = r; return this; }
            public Builder status(String s)          { this.status = s; return this; }
            public Builder userEmail(String e)       { this.userEmail = e; return this; }
            public Builder username(String u)        { this.username = u; return this; }
            public Builder createdAt(LocalDateTime c){ this.createdAt = c; return this; }
            public Builder updatedAt(LocalDateTime u){ this.updatedAt = u; return this; }

            public Response build() {
                Response r = new Response();
                r.id = id; r.softwareName = softwareName; r.reason = reason;
                r.status = status; r.userEmail = userEmail; r.username = username;
                r.createdAt = createdAt; r.updatedAt = updatedAt;
                return r;
            }
        }
    }
}
