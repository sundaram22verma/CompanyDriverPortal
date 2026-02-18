package com.test.CompanyDriverPortal.dto.company;

import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.validator.constraints.URL;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CompanyRequestDto {

    @NotBlank(message = "Company name is required")
    @Size(min = 2, max = 50, message = "Company name must be between 2 and 50 characters")
    private String companyName;

    @NotBlank(message = "Registration number is required")
    @Size(min = 2, max = 50, message = "Registration number must be between 2 and 50 characters")
    private String registrationNumber;

    @PastOrPresent(message = "Established date cannot be in the future")
    private LocalDate establishedOn;

    @Size(max = 150, message = "Website must not exceed 150 characters")
    @URL(message = "Invalid website URL")
    private String website;

    @NotBlank(message = "Address Line 1 is required")
    @Size(min = 5, max = 200, message = "Address Line 1 must be between 5 and 200 characters")
    private String addressLine1;

    @Size(max = 200, message = "Address Line 2 must not exceed 200 characters")
    private String addressLine2;

    @NotBlank(message = "City is required")
    private String city;

    @NotBlank(message = "State is required")
    private String state;

    @NotBlank(message = "Zip code is required")
    @Size(min = 2, max = 20, message = "Zip code must be between 2 and 20 characters")
    private String zipCode;

    @NotBlank(message = "Primary contact first name is required")
    private String primaryContactFirstName;

    @NotBlank(message = "Primary contact last name is required")
    private String primaryContactLastName;

    @Email(message = "Invalid primary contact email")
    @NotBlank(message = "Primary contact email is required")
    private String primaryContactEmail;

    @NotBlank(message = "Primary contact mobile is required")
    @Pattern(
            regexp = "\\d{10}",
            message = "Primary contact mobile must be exactly 10 digits"
    )
    private String primaryContactMobile;
}
