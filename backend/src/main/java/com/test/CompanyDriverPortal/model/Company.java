package com.test.CompanyDriverPortal.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "companies")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Company {

    @Id // Primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-increment strategy
    private Long id;

    @Column(name = "company_name", nullable = false, length = 50)
    private String companyName;

    @Column(name = "registration_number", nullable = false, unique = true, length = 50)
    private String registrationNumber;

    @Column(name = "established_on")
    private LocalDate establishedOn;

    @Column(length = 150)
    private String website;

    //orphansRemoval = true ensures that if a Company is deleted, its associated CompanyDetails will also be deleted
    //cascade = CascadeType.ALL ensures that all operations (persist, merge, remove, refresh) are cascaded to the associated CompanyDetails
    @OneToOne(mappedBy = "company", cascade = CascadeType.ALL, orphanRemoval = true)
    private CompanyDetails companyDetails;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}

