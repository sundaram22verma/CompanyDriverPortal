package com.test.CompanyDriverPortal.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "company_details")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CompanyDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //Foreign key to Company, one-to-one relationship
    @OneToOne
    @JoinColumn(name = "company_id", nullable = false, unique = true)
    private Company company;

    @Column(name = "address_line1", nullable = false, length = 200)
    private String addressLine1;

    @Column(name = "address_line2", length = 200)
    private String addressLine2;

    @Column(nullable = false, length = 100)
    private String city;

    @Column(nullable = false, length = 100)
    private String state;

    @Column(name = "zip_code", nullable = false, length = 20)
    private String zipCode;

    @Column(name = "primary_contact_first_name", nullable = false, length = 100)
    private String primaryContactFirstName;

    @Column(name = "primary_contact_last_name", nullable = false, length = 100)
    private String primaryContactLastName;

    @Column(name = "primary_contact_email", nullable = false, length = 150)
    private String primaryContactEmail;

    @Column(name = "primary_contact_mobile", nullable = false, length = 20)
    private String primaryContactMobile;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}