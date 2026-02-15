package com.test.CompanyDriverPortal.controller;

import com.test.CompanyDriverPortal.dto.company.CompanyRequestDto;
import com.test.CompanyDriverPortal.dto.company.CompanyResponseDto;
import com.test.CompanyDriverPortal.dto.company.CompanySearchDto;
import com.test.CompanyDriverPortal.service.CompanyService;
import jakarta.validation.Valid;
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
@RequestMapping("/api/companies")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:5173"})
public class CompanyController {

    @Autowired
    private CompanyService companyService;

    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN')")
    @PostMapping
    public ResponseEntity<?> createCompany(@Valid @RequestBody CompanyRequestDto requestDto) {
        try {
            CompanyResponseDto response = companyService.createCompany(requestDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<?> updateCompany(@PathVariable Long id, @RequestBody CompanyRequestDto requestDto) {
        try {
            CompanyResponseDto response = companyService.updateCompany(id, requestDto);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }

    @PreAuthorize("hasAnyRole('USER','ADMIN','SUPER_ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<?> getCompanyById(@PathVariable Long id) {
        try {
            CompanyResponseDto response = companyService.getCompanyById(id);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
    }

    @PreAuthorize("hasAnyRole('USER','ADMIN','SUPER_ADMIN')")
    @GetMapping
    public ResponseEntity<List<CompanyResponseDto>> getAllCompanies() {
        List<CompanyResponseDto> companies = companyService.getAllCompanies();
        return ResponseEntity.ok(companies);
    }

    @PreAuthorize("hasAnyRole('USER','ADMIN','SUPER_ADMIN')")
    @PostMapping("/search")
    public ResponseEntity<Page<CompanyResponseDto>> searchCompanies(@RequestBody CompanySearchDto searchDto) {
        Page<CompanyResponseDto> companies = companyService.searchCompanies(searchDto);
        return ResponseEntity.ok(companies);
    }


    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCompany(@PathVariable Long id) {
        try {
            companyService.deleteCompany(id);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Company deleted successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
    }
}
