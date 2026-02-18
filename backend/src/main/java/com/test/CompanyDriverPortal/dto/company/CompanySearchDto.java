package com.test.CompanyDriverPortal.dto.company;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CompanySearchDto {
    private String companyName;
    private String registrationNumber;
    private String city;
    private String state;
    private String primaryContactEmail;

    @Min(0)
    private int page = 0;

    @Min(1)
    @Max(100)
    private int size = 10;
}