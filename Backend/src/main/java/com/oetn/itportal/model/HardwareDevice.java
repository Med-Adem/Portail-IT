package com.oetn.itportal.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "hardware_devices")
public class HardwareDevice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String username;

    @Column(name = "laptop_model")
    private String laptopModel;

    @Column(name = "serial_number", nullable = false)
    private String serialNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "submitted_at")
    private LocalDateTime submittedAt;

    public HardwareDevice() {}

    @PrePersist
    public void prePersist() { this.submittedAt = LocalDateTime.now(); }

    // Getters & Setters
    public Long getId()                       { return id; }
    public void setId(Long id)                { this.id = id; }
    public String getUsername()               { return username; }
    public void setUsername(String u)         { this.username = u; }
    public String getLaptopModel()            { return laptopModel; }
    public void setLaptopModel(String m)      { this.laptopModel = m; }
    public String getSerialNumber()           { return serialNumber; }
    public void setSerialNumber(String s)     { this.serialNumber = s; }
    public User getUser()                     { return user; }
    public void setUser(User u)               { this.user = u; }
    public LocalDateTime getSubmittedAt()     { return submittedAt; }
    public void setSubmittedAt(LocalDateTime s) { this.submittedAt = s; }

    // Builder
    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private String username, laptopModel, serialNumber;
        private User user;

        public Builder username(String u)     { this.username = u; return this; }
        public Builder laptopModel(String m)  { this.laptopModel = m; return this; }
        public Builder serialNumber(String s) { this.serialNumber = s; return this; }
        public Builder user(User u)           { this.user = u; return this; }

        public HardwareDevice build() {
            HardwareDevice d = new HardwareDevice();
            d.username = username; d.laptopModel = laptopModel;
            d.serialNumber = serialNumber; d.user = user;
            return d;
        }
    }
}
