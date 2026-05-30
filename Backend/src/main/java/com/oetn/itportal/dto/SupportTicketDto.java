package com.oetn.itportal.dto;

import com.oetn.itportal.model.SupportTicket;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;

public class SupportTicketDto {

    public static class CreateRequest {
        @NotBlank(message = "Le titre est requis")
        private String title;
        @NotBlank(message = "La description est requise")
        private String description;

        public CreateRequest() {}
        public String getTitle()          { return title; }
        public void setTitle(String t)    { this.title = t; }
        public String getDescription()    { return description; }
        public void setDescription(String d) { this.description = d; }
    }

    public static class UpdateStatusRequest {
        private SupportTicket.Status status;
        public UpdateStatusRequest() {}
        public SupportTicket.Status getStatus()       { return status; }
        public void setStatus(SupportTicket.Status s) { this.status = s; }
    }

    public static class Response {
        private Long id;
        private String title, description, status, userEmail, username;
        private LocalDateTime createdAt, updatedAt;

        public Response() {}

        public Long getId()                     { return id; }
        public void setId(Long id)              { this.id = id; }
        public String getTitle()                { return title; }
        public void setTitle(String t)          { this.title = t; }
        public String getDescription()          { return description; }
        public void setDescription(String d)    { this.description = d; }
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
            private String title, description, status, userEmail, username;
            private LocalDateTime createdAt, updatedAt;

            public Builder id(Long id)               { this.id = id; return this; }
            public Builder title(String t)           { this.title = t; return this; }
            public Builder description(String d)     { this.description = d; return this; }
            public Builder status(String s)          { this.status = s; return this; }
            public Builder userEmail(String e)       { this.userEmail = e; return this; }
            public Builder username(String u)        { this.username = u; return this; }
            public Builder createdAt(LocalDateTime c){ this.createdAt = c; return this; }
            public Builder updatedAt(LocalDateTime u){ this.updatedAt = u; return this; }

            public Response build() {
                Response r = new Response();
                r.id = id; r.title = title; r.description = description;
                r.status = status; r.userEmail = userEmail; r.username = username;
                r.createdAt = createdAt; r.updatedAt = updatedAt;
                return r;
            }
        }
    }
}
