package com.oetn.itportal.dto;

import com.oetn.itportal.model.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public class AdminUserDto {

    public static class UpdateRequest {
        @NotBlank(message = "Le prénom est requis")
        private String firstName;

        @NotBlank(message = "Le nom est requis")
        private String lastName;

        private String phoneNumber;

        @NotNull(message = "Le rôle est requis")
        private User.Role role;

        // New password is optional — only update if provided
        private String newPassword;

        public UpdateRequest() {}

        public String getFirstName()           { return firstName; }
        public void setFirstName(String f)     { this.firstName = f; }
        public String getLastName()            { return lastName; }
        public void setLastName(String l)      { this.lastName = l; }
        public String getPhoneNumber()         { return phoneNumber; }
        public void setPhoneNumber(String p)   { this.phoneNumber = p; }
        public User.Role getRole()             { return role; }
        public void setRole(User.Role r)       { this.role = r; }
        public String getNewPassword()         { return newPassword; }
        public void setNewPassword(String p)   { this.newPassword = p; }
    }

    public static class Response {
        private Long id;
        private String firstName;
        private String lastName;
        private String username;
        private String phoneNumber;
        private String role;
        private LocalDateTime createdAt;

        public Response() {}

        public Long getId()                        { return id; }
        public void setId(Long id)                 { this.id = id; }
        public String getFirstName()               { return firstName; }
        public void setFirstName(String f)         { this.firstName = f; }
        public String getLastName()                { return lastName; }
        public void setLastName(String l)          { this.lastName = l; }
        public String getUsername()                { return username; }
        public void setUsername(String u)          { this.username = u; }
        public String getPhoneNumber()             { return phoneNumber; }
        public void setPhoneNumber(String p)       { this.phoneNumber = p; }
        public String getRole()                    { return role; }
        public void setRole(String r)              { this.role = r; }
        public LocalDateTime getCreatedAt()        { return createdAt; }
        public void setCreatedAt(LocalDateTime c)  { this.createdAt = c; }

        public static Builder builder() { return new Builder(); }

        public static class Builder {
            private Long id;
            private String firstName, lastName, username, phoneNumber, role;
            private LocalDateTime createdAt;

            public Builder id(Long id)                 { this.id = id; return this; }
            public Builder firstName(String f)         { this.firstName = f; return this; }
            public Builder lastName(String l)          { this.lastName = l; return this; }
            public Builder username(String u)          { this.username = u; return this; }
            public Builder phoneNumber(String p)       { this.phoneNumber = p; return this; }
            public Builder role(String r)              { this.role = r; return this; }
            public Builder createdAt(LocalDateTime c)  { this.createdAt = c; return this; }

            public Response build() {
                Response r = new Response();
                r.id = id; r.firstName = firstName; r.lastName = lastName;
                r.username = username; r.phoneNumber = phoneNumber;
                r.role = role; r.createdAt = createdAt;
                return r;
            }
        }
    }
}
