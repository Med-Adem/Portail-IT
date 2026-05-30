package com.oetn.itportal.dto;

import jakarta.validation.constraints.NotBlank;

public class LoginRequest {

    // Login avec username = FirstName.LastName
    @NotBlank(message = "Le nom d'utilisateur est requis")
    private String username;

    @NotBlank(message = "Le mot de passe est requis")
    private String password;

    public LoginRequest() {}

    public String getUsername()        { return username; }
    public void setUsername(String u)  { this.username = u; }
    public String getPassword()        { return password; }
    public void setPassword(String p)  { this.password = p; }
}
