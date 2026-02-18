package com.test.CompanyDriverPortal.service.impl;

import com.test.CompanyDriverPortal.dto.driver.DriverRequestDto;
import com.test.CompanyDriverPortal.dto.driver.DriverResponseDto;
import com.test.CompanyDriverPortal.dto.driver.DriverSearchDto;
import com.test.CompanyDriverPortal.globalException.ResourceNotFoundException;
import com.test.CompanyDriverPortal.model.Driver;
import com.test.CompanyDriverPortal.model.DriverDetails;
import com.test.CompanyDriverPortal.repository.DriverRepository;
import com.test.CompanyDriverPortal.service.DriverService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DriverServiceImpl implements DriverService {

    @Autowired
    private DriverRepository driverRepository;

    @Override
    @Transactional
    public DriverResponseDto createDriver(DriverRequestDto requestDto) {

        // Check if email already exists
        if (driverRepository.findByEmail(requestDto.getEmail()).isPresent()) {
            throw new RuntimeException("Driver with this email already exists");
        }

        // Check if license number already exists
        if (driverRepository.findByLicenseNumber(requestDto.getLicenseNumber()).isPresent()) {
            throw new RuntimeException("Driver with this license number already exists");
        }

        // Additional business validation: Experience years cannot be negative
        if(requestDto.getExperienceYears() != null && requestDto.getExperienceYears() < 0) {
            throw new RuntimeException("Experience years cannot be negative");
        }

        // Create Driver entity and set fields from request DTO because we have to save the details in the database and then return the response DTO
        Driver driver = new Driver();
        driver.setFirstName(requestDto.getFirstName());
        driver.setLastName(requestDto.getLastName());
        driver.setEmail(requestDto.getEmail());
        driver.setMobile(requestDto.getMobile());
        driver.setDateOfBirth(requestDto.getDateOfBirth());
        driver.setLicenseNumber(requestDto.getLicenseNumber());
        driver.setExperienceYears(requestDto.getExperienceYears());

        // Create DriverDetails entity and set fields from request DTO because we have to save the details in the database and then return the response DTO
        DriverDetails details = new DriverDetails();
        details.setDriver(driver);
        details.setAddressLine1(requestDto.getAddressLine1());
        details.setAddressLine2(requestDto.getAddressLine2());
        details.setCity(requestDto.getCity());
        details.setState(requestDto.getState());
        details.setZipCode(requestDto.getZipCode());

        driver.setDriverDetails(details);

        Driver savedDriver = driverRepository.save(driver);
        return convertToResponseDto(savedDriver);
    }

    @Override
    @Transactional
    public DriverResponseDto updateDriver(Long id, DriverRequestDto requestDto) {
        Driver driver = driverRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Driver not found with id: " + id));

        // Check if email is being changed and if it already exists
        if (!driver.getEmail().equals(requestDto.getEmail())) {
            if (driverRepository.findByEmail(requestDto.getEmail()).isPresent()) {
                throw new RuntimeException("Driver with this email already exists");
            }
        }

        // Check if license number is being changed and if it already exists
        if (!driver.getLicenseNumber().equals(requestDto.getLicenseNumber())) {
            if (driverRepository.findByLicenseNumber(requestDto.getLicenseNumber()).isPresent()) {
                throw new RuntimeException("Driver with this license number already exists");
            }
        }

        driver.setFirstName(requestDto.getFirstName());
        driver.setLastName(requestDto.getLastName());
        driver.setEmail(requestDto.getEmail());
        driver.setMobile(requestDto.getMobile());
        driver.setDateOfBirth(requestDto.getDateOfBirth());
        driver.setLicenseNumber(requestDto.getLicenseNumber());
        driver.setExperienceYears(requestDto.getExperienceYears());

        DriverDetails details = driver.getDriverDetails();
        if (details == null) {
            details = new DriverDetails();
            details.setDriver(driver);
        }

        details.setAddressLine1(requestDto.getAddressLine1());
        details.setAddressLine2(requestDto.getAddressLine2());
        details.setCity(requestDto.getCity());
        details.setState(requestDto.getState());
        details.setZipCode(requestDto.getZipCode());

        driver.setDriverDetails(details);

        Driver updatedDriver = driverRepository.save(driver);
        return convertToResponseDto(updatedDriver);
    }

    @Override
    public DriverResponseDto getDriverById(Long id) {
        Driver driver = driverRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Driver not found with id: " + id));
        return convertToResponseDto(driver);
    }

    @Override
    public List<DriverResponseDto> getAllDrivers() {
        return driverRepository.findAll(Sort.by(Sort.Direction.DESC, "createdAt"))
                .stream()
                .map(this::convertToResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public Page<DriverResponseDto> searchDrivers(DriverSearchDto searchDto) {
        // Create Pageable object for pagination and sorting
        Pageable pageable = PageRequest.of(
                searchDto.getPage(),
                searchDto.getSize(),
                Sort.by(Sort.Direction.DESC, "createdAt")
        );

        // Call the repository method to search drivers based on the provided criteria
        Page<Driver> drivers = driverRepository.searchDrivers(
                searchDto.getFirstName(),
                searchDto.getLastName(),
                searchDto.getEmail(),
                searchDto.getLicenseNumber(),
                searchDto.getCity(),
                searchDto.getState(),
                pageable
        );

        return drivers.map(this::convertToResponseDto);
    }

    @Override
    @Transactional
    public void deleteDriver(Long id) {
        if (!driverRepository.existsById(id)) {
            throw new ResourceNotFoundException("Driver not found with id: " + id);
        }
        driverRepository.deleteById(id);
    }

    // Helper method to convert Driver entity to DriverResponseDto so that we can show the details in the response
    private DriverResponseDto convertToResponseDto(Driver driver) {
        DriverResponseDto dto = new DriverResponseDto();
        dto.setId(driver.getId());
        dto.setFirstName(driver.getFirstName());
        dto.setLastName(driver.getLastName());
        dto.setEmail(driver.getEmail());
        dto.setMobile(driver.getMobile());
        dto.setDateOfBirth(driver.getDateOfBirth());
        dto.setLicenseNumber(driver.getLicenseNumber());
        dto.setExperienceYears(driver.getExperienceYears());

        if (driver.getDriverDetails() != null) {
            DriverDetails details = driver.getDriverDetails();
            dto.setAddressLine1(details.getAddressLine1());
            dto.setAddressLine2(details.getAddressLine2());
            dto.setCity(details.getCity());
            dto.setState(details.getState());
            dto.setZipCode(details.getZipCode());
        }

        return dto;
    }
}