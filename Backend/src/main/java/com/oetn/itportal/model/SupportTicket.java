package com.oetn.itportal.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "support_tickets")
public class SupportTicket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

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

    public SupportTicket() {}

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        if (this.status == null) this.status = Status.OPEN;
    }

    @PreUpdate
    public void preUpdate() { this.updatedAt = LocalDateTime.now(); }

    public enum Status { OPEN, IN_PROGRESS, CLOSED }

    // Getters & Setters
    public Long getId()                  { return id; }
    public void setId(Long id)           { this.id = id; }
    public String getTitle()             { return title; }
    public void setTitle(String t)       { this.title = t; }
    public String getDescription()       { return description; }
    public void setDescription(String d) { this.description = d; }
    public Status getStatus()            { return status; }
    public void setStatus(Status s)      { this.status = s; }
    public User getUser()                { return user; }
    public void setUser(User u)          { this.user = u; }
    public LocalDateTime getCreatedAt()  { return createdAt; }
    public void setCreatedAt(LocalDateTime c) { this.createdAt = c; }
    public LocalDateTime getUpdatedAt()  { return updatedAt; }
    public void setUpdatedAt(LocalDateTime u) { this.updatedAt = u; }

    // Builder
    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private Long id;
        private String title, description;
        private Status status = Status.OPEN;
        private User user;

        public Builder id(Long id)               { this.id = id; return this; }
        public Builder title(String t)           { this.title = t; return this; }
        public Builder description(String d)     { this.description = d; return this; }
        public Builder status(Status s)          { this.status = s; return this; }
        public Builder user(User u)              { this.user = u; return this; }

        public SupportTicket build() {
            SupportTicket t = new SupportTicket();
            t.id = id; t.title = title; t.description = description;
            t.status = status; t.user = user;
            return t;
        }
    }
}
