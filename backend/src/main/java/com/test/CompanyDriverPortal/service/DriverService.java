package com.test.CompanyDriverPortal.service;

import com.test.CompanyDriverPortal.dto.driver.DriverRequestDto;
import com.test.CompanyDriverPortal.dto.driver.DriverResponseDto;
import com.test.CompanyDriverPortal.dto.driver.DriverSearchDto;
import org.springframework.data.domain.Page;

import java.util.List;

public interface DriverService {
    DriverResponseDto createDriver(DriverRequestDto requestDto);
    DriverResponseDto updateDriver(Long id, DriverRequestDto requestDto);
    DriverResponseDto getDriverById(Long id);
    List<DriverResponseDto> getAllDrivers();
    Page<DriverResponseDto> searchDrivers(DriverSearchDto searchDto);
    void deleteDriver(Long id);
}