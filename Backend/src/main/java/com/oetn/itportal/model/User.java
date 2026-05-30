package com.oetn.itportal.model;

import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "users")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "username", unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    public User() {}

    public User(Long id, String firstName, String lastName, String phoneNumber,
                String username, String password, Role role, LocalDateTime createdAt) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.username = username;
        this.password = password;
        this.role = role;
        this.createdAt = createdAt;
    }

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
        if (this.role == null) this.role = Role.USER;
        if (this.username == null && this.firstName != null && this.lastName != null) {
            this.username = (this.firstName + "." + this.lastName).toLowerCase();
        }
    }

    public enum Role { USER, ADMIN }

    // Getters & Setters
    public Long getId()                       { return id; }
    public void setId(Long id)                { this.id = id; }
    public String getFirstName()              { return firstName; }
    public void setFirstName(String f)        { this.firstName = f; }
    public String getLastName()               { return lastName; }
    public void setLastName(String l)         { this.lastName = l; }
    public String getPhoneNumber()            { return phoneNumber; }
    public void setPhoneNumber(String p)      { this.phoneNumber = p; }
    public void setUsername(String u)         { this.username = u; }
    public void setPassword(String p)         { this.password = p; }
    public Role getRole()                     { return role; }
    public void setRole(Role r)               { this.role = r; }
    public LocalDateTime getCreatedAt()       { return createdAt; }
    public void setCreatedAt(LocalDateTime c) { this.createdAt = c; }
    public String getFullName()               { return firstName + " " + lastName; }

    // UserDetails
    @Override public String getUsername()  { return username; }
    @Override public String getPassword()  { return password; }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + role.name()));
    }

    @Override public boolean isAccountNonExpired()     { return true; }
    @Override public boolean isAccountNonLocked()      { return true; }
    @Override public boolean isCredentialsNonExpired() { return true; }
    @Override public boolean isEnabled()               { return true; }

    // Builder
    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private Long id;
        private String firstName, lastName, phoneNumber, username, password;
        private Role role = Role.USER;
        private LocalDateTime createdAt;

        public Builder id(Long id)                { this.id = id; return this; }
        public Builder firstName(String f)        { this.firstName = f; return this; }
        public Builder lastName(String l)         { this.lastName = l; return this; }
        public Builder phoneNumber(String p)      { this.phoneNumber = p; return this; }
        public Builder username(String u)         { this.username = u; return this; }
        public Builder password(String p)         { this.password = p; return this; }
        public Builder role(Role r)               { this.role = r; return this; }
        public Builder createdAt(LocalDateTime c) { this.createdAt = c; return this; }

        public User build() {
            return new User(id, firstName, lastName, phoneNumber, username, password, role, createdAt);
        }
    }
}
