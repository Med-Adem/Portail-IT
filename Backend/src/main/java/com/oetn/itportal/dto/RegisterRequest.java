package com.oetn.itportal.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class RegisterRequest {

    @NotBlank(message = "Le prénom est requis")
    private String firstName;

    @NotBlank(message = "Le nom est requis")
    private String lastName;

    private String phoneNumber;

    @NotBlank(message = "Le mot de passe est requis")
    @Size(min = 6, message = "Le mot de passe doit avoir au moins 6 caractères")
    private String password;

    public RegisterRequest() {}

    public String getFirstName()          { return firstName; }
    public void setFirstName(String f)    { this.firstName = f; }
    public String getLastName()           { return lastName; }
    public void setLastName(String l)     { this.lastName = l; }
    public String getPhoneNumber()        { return phoneNumber; }
    public void setPhoneNumber(String p)  { this.phoneNumber = p; }
    public String getPassword()           { return password; }
    public void setPassword(String p)     { this.password = p; }
}
