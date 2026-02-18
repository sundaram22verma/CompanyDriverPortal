package com.test.CompanyDriverPortal.controller;

import com.test.CompanyDriverPortal.dto.driver.DriverRequestDto;
import com.test.CompanyDriverPortal.dto.driver.DriverResponseDto;
import com.test.CompanyDriverPortal.dto.driver.DriverSearchDto;
import com.test.CompanyDriverPortal.service.DriverService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/drivers")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:5173"})
public class DriverController {

    @Autowired
    private DriverService driverService;

    // CREATE DRIVER
    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN')")
    public ResponseEntity<DriverResponseDto> createDriver(
            @Valid @RequestBody DriverRequestDto requestDto) {

        DriverResponseDto response = driverService.createDriver(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // UPDATE DRIVER
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN')")
    public ResponseEntity<DriverResponseDto> updateDriver(
            @PathVariable Long id,
            @Valid @RequestBody DriverRequestDto requestDto) {

        DriverResponseDto response = driverService.updateDriver(id, requestDto);
        return ResponseEntity.ok(response);
    }

    // GET DRIVER BY ID
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER','ADMIN','SUPER_ADMIN')")
    public ResponseEntity<DriverResponseDto> getDriverById(@PathVariable Long id) {

        DriverResponseDto response = driverService.getDriverById(id);
        return ResponseEntity.ok(response);
    }

    // GET ALL DRIVERS
    @GetMapping
    @PreAuthorize("hasAnyRole('USER','ADMIN','SUPER_ADMIN')")
    public ResponseEntity<List<DriverResponseDto>> getAllDrivers() {

        return ResponseEntity.ok(driverService.getAllDrivers());
    }

    // SEARCH DRIVERS
    @PostMapping("/search")
    @PreAuthorize("hasAnyRole('USER','ADMIN','SUPER_ADMIN')")
    public ResponseEntity<Page<DriverResponseDto>> searchDrivers(
            @RequestBody DriverSearchDto searchDto) {

        return ResponseEntity.ok(driverService.searchDrivers(searchDto));
    }

    // DELETE DRIVER
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<Map<String, String>> deleteDriver(@PathVariable Long id) {

        driverService.deleteDriver(id);
        return ResponseEntity.ok(Map.of("message", "Driver deleted successfully"));
    }
}
