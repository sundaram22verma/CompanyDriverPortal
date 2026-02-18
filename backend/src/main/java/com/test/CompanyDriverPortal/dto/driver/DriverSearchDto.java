package com.test.CompanyDriverPortal.dto.driver;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
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

    @Min(0)
    private int page = 0;

    @Min(1)
    @Max(100)
    private int size = 10;
}
