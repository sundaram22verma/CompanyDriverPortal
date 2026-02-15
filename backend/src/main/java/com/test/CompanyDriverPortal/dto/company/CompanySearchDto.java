package com.test.CompanyDriverPortal.dto.company;

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
    private int page = 0;
    private int size = 10;
}