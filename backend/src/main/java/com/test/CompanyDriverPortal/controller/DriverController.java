package com.test.CompanyDriverPortal.controller;

import com.test.CompanyDriverPortal.dto.driver.DriverRequestDto;
import com.test.CompanyDriverPortal.dto.driver.DriverResponseDto;
import com.test.CompanyDriverPortal.dto.driver.DriverSearchDto;
import com.test.CompanyDriverPortal.service.DriverService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/drivers")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:5173"})
public class DriverController {

    @Autowired
    private DriverService driverService;

    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN')")
    @PostMapping
    public ResponseEntity<?> createDriver(@RequestBody DriverRequestDto requestDto) {
        try {
            DriverResponseDto response = driverService.createDriver(requestDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<?> updateDriver(@PathVariable Long id, @RequestBody DriverRequestDto requestDto) {
        try {
            DriverResponseDto response = driverService.updateDriver(id, requestDto);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }

    @PreAuthorize("hasAnyRole('USER','ADMIN','SUPER_ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<?> getDriverById(@PathVariable Long id) {
        try {
            DriverResponseDto response = driverService.getDriverById(id);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
    }

    @PreAuthorize("hasAnyRole('USER','ADMIN','SUPER_ADMIN')")
    @GetMapping
    public ResponseEntity<List<DriverResponseDto>> getAllDrivers() {
        List<DriverResponseDto> drivers = driverService.getAllDrivers();
        return ResponseEntity.ok(drivers);
    }


    @PreAuthorize("hasAnyRole('USER','ADMIN','SUPER_ADMIN')")
    @PostMapping("/search")
    public ResponseEntity<Page<DriverResponseDto>> searchDrivers(@RequestBody DriverSearchDto searchDto) {
        Page<DriverResponseDto> drivers = driverService.searchDrivers(searchDto);
        return ResponseEntity.ok(drivers);
    }

    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteDriver(@PathVariable Long id) {
        try {
            driverService.deleteDriver(id);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Driver deleted successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
    }
}
