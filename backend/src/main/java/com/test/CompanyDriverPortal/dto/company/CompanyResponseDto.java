package com.test.CompanyDriverPortal.dto.company;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CompanyResponseDto {
    private Long id;
    private String companyName;
    private String registrationNumber;
    private LocalDate establishedOn;
    private String website;
    private String addressLine1;
    private String addressLine2;
    private String city;
    private String state;
    private String zipCode;
    private String primaryContactFirstName;
    private String primaryContactLastName;
    private String primaryContactEmail;
    private String primaryContactMobile;
}