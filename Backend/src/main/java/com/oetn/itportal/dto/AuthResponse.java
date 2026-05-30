package com.oetn.itportal.dto;

public class AuthResponse {
    private String token;
    private String username;
    private String role;
    private Long userId;

    public AuthResponse() {}

    public AuthResponse(String token, String username, String role, Long userId) {
        this.token = token;
        this.username = username;
        this.role = role;
        this.userId = userId;
    }

    public String getToken()       { return token; }
    public void setToken(String t) { this.token = t; }
    public String getUsername()    { return username; }
    public void setUsername(String u) { this.username = u; }
    public String getRole()        { return role; }
    public void setRole(String r)  { this.role = r; }
    public Long getUserId()        { return userId; }
    public void setUserId(Long id) { this.userId = id; }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private String token, username, role;
        private Long userId;

        public Builder token(String t)    { this.token = t; return this; }
        public Builder username(String u) { this.username = u; return this; }
        public Builder role(String r)     { this.role = r; return this; }
        public Builder userId(Long id)    { this.userId = id; return this; }

        public AuthResponse build() {
            return new AuthResponse(token, username, role, userId);
        }
    }
}
