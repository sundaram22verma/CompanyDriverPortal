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

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/companies")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:5173"})
public class CompanyController {

    @Autowired
    private CompanyService companyService;

    // CREATE COMPANY
    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN')")
    public ResponseEntity<CompanyResponseDto> createCompany(
            @Valid @RequestBody CompanyRequestDto requestDto) {

        CompanyResponseDto response = companyService.createCompany(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // UPDATE COMPANY
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN')")
    public ResponseEntity<CompanyResponseDto> updateCompany(
            @PathVariable Long id,
            @Valid @RequestBody CompanyRequestDto requestDto) {

        CompanyResponseDto response = companyService.updateCompany(id, requestDto);
        return ResponseEntity.ok(response);
    }

    // GET COMPANY BY ID
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER','ADMIN','SUPER_ADMIN')")
    public ResponseEntity<CompanyResponseDto> getCompanyById(@PathVariable Long id) {

        CompanyResponseDto response = companyService.getCompanyById(id);
        return ResponseEntity.ok(response);
    }

    // GET ALL COMPANIES
    @GetMapping
    @PreAuthorize("hasAnyRole('USER','ADMIN','SUPER_ADMIN')")
    public ResponseEntity<List<CompanyResponseDto>> getAllCompanies() {

        return ResponseEntity.ok(companyService.getAllCompanies());
    }

    // SEARCH COMPANIES
    @PostMapping("/search")
    @PreAuthorize("hasAnyRole('USER','ADMIN','SUPER_ADMIN')")
    public ResponseEntity<Page<CompanyResponseDto>> searchCompanies(
            @RequestBody CompanySearchDto searchDto) {

        return ResponseEntity.ok(companyService.searchCompanies(searchDto));
    }

    // DELETE COMPANY
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<Map<String, String>> deleteCompany(@PathVariable Long id) {

        companyService.deleteCompany(id);
        return ResponseEntity.ok(Map.of("message", "Company deleted successfully"));
    }
}
