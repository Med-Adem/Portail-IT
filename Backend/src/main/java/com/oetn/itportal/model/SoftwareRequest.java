package com.oetn.itportal.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "software_requests")
public class SoftwareRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "software_name", nullable = false)
    private String softwareName;

    @Column(columnDefinition = "TEXT")
    private String reason;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public SoftwareRequest() {}

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        if (this.status == null) this.status = Status.PENDING;
    }

    @PreUpdate
    public void preUpdate() { this.updatedAt = LocalDateTime.now(); }

    public enum Status { PENDING, APPROVED, REJECTED }

    // Getters & Setters
    public Long getId()                    { return id; }
    public void setId(Long id)             { this.id = id; }
    public String getSoftwareName()        { return softwareName; }
    public void setSoftwareName(String s)  { this.softwareName = s; }
    public String getReason()              { return reason; }
    public void setReason(String r)        { this.reason = r; }
    public Status getStatus()              { return status; }
    public void setStatus(Status s)        { this.status = s; }
    public User getUser()                  { return user; }
    public void setUser(User u)            { this.user = u; }
    public LocalDateTime getCreatedAt()    { return createdAt; }
    public void setCreatedAt(LocalDateTime c) { this.createdAt = c; }
    public LocalDateTime getUpdatedAt()    { return updatedAt; }
    public void setUpdatedAt(LocalDateTime u) { this.updatedAt = u; }

    // Builder
    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private String softwareName, reason;
        private Status status = Status.PENDING;
        private User user;

        public Builder softwareName(String s) { this.softwareName = s; return this; }
        public Builder reason(String r)       { this.reason = r; return this; }
        public Builder status(Status s)       { this.status = s; return this; }
        public Builder user(User u)           { this.user = u; return this; }

        public SoftwareRequest build() {
            SoftwareRequest req = new SoftwareRequest();
            req.softwareName = softwareName; req.reason = reason;
            req.status = status; req.user = user;
            return req;
        }
    }
}
