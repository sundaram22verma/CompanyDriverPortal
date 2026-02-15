package com.test.CompanyDriverPortal.dto.driver;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DriverSearchDto {
    private String firstName;
    private String lastName;
    private String email;
    private String licenseNumber;
    private String city;
    private String state;
    private int page = 0;
    private int size = 10;
}
