package com.test.CompanyDriverPortal.dto.company;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CompanyRequestDto {
    private String companyName;
    private String registrationNumber;
    private LocalDate establishedOn;
    private String website;
    @NotBlank(message = "Address Line 1 is required")
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