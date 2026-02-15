package com.test.CompanyDriverPortal.dto.driver;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DriverRequestDto {
    private String firstName;
    private String lastName;
    private String email;
    private String mobile;
    private LocalDate dateOfBirth;
    private String licenseNumber;
    private Integer experienceYears;
    private String addressLine1;
    private String addressLine2;
    private String city;
    private String state;
    private String zipCode;
}