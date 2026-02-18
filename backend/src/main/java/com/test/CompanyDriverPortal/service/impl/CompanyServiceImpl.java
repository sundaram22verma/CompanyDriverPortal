package com.test.CompanyDriverPortal.service.impl;

import com.test.CompanyDriverPortal.dto.company.CompanyRequestDto;
import com.test.CompanyDriverPortal.dto.company.CompanyResponseDto;
import com.test.CompanyDriverPortal.dto.company.CompanySearchDto;
import com.test.CompanyDriverPortal.globalException.ResourceNotFoundException;
import com.test.CompanyDriverPortal.model.Company;
import com.test.CompanyDriverPortal.model.CompanyDetails;
import com.test.CompanyDriverPortal.repository.CompanyRepository;
import com.test.CompanyDriverPortal.service.CompanyService;
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
public class CompanyServiceImpl implements CompanyService {

    @Autowired
    private CompanyRepository companyRepository;

    @Override
    @Transactional
    public CompanyResponseDto createCompany(CompanyRequestDto requestDto) {
        // Check if registration number already exists
        if (companyRepository.findByRegistrationNumber(requestDto.getRegistrationNumber()).isPresent()) {
            throw new RuntimeException("Company with this registration number already exists");
        }

        // Create Company entity from request DTO
        Company company = new Company();
        company.setCompanyName(requestDto.getCompanyName());
        company.setRegistrationNumber(requestDto.getRegistrationNumber());
        company.setEstablishedOn(requestDto.getEstablishedOn());
        company.setWebsite(requestDto.getWebsite());

        // Create CompanyDetails entity and set it to Company
        CompanyDetails details = new CompanyDetails();
        details.setCompany(company);
        details.setAddressLine1(requestDto.getAddressLine1());
        details.setAddressLine2(requestDto.getAddressLine2());
        details.setCity(requestDto.getCity());
        details.setState(requestDto.getState());
        details.setZipCode(requestDto.getZipCode());
        details.setPrimaryContactFirstName(requestDto.getPrimaryContactFirstName());
        details.setPrimaryContactLastName(requestDto.getPrimaryContactLastName());
        details.setPrimaryContactEmail(requestDto.getPrimaryContactEmail());
        details.setPrimaryContactMobile(requestDto.getPrimaryContactMobile());

        company.setCompanyDetails(details);

        Company savedCompany = companyRepository.save(company);
        return convertToResponseDto(savedCompany);
    }

    @Override
    @Transactional
    public CompanyResponseDto updateCompany(Long id, CompanyRequestDto requestDto) {
        Company company = companyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Company not found with id: " + id));

        // Check if registration number is being changed and if it already exists
        if (!company.getRegistrationNumber().equals(requestDto.getRegistrationNumber())) {
            if (companyRepository.findByRegistrationNumber(requestDto.getRegistrationNumber()).isPresent()) {
                throw new RuntimeException("Company with this registration number already exists");
            }
        }

        company.setCompanyName(requestDto.getCompanyName());
        company.setRegistrationNumber(requestDto.getRegistrationNumber());
        company.setEstablishedOn(requestDto.getEstablishedOn());
        company.setWebsite(requestDto.getWebsite());

        CompanyDetails details = company.getCompanyDetails();
        if (details == null) {
            details = new CompanyDetails();
            details.setCompany(company);
        }

        details.setAddressLine1(requestDto.getAddressLine1());
        details.setAddressLine2(requestDto.getAddressLine2());
        details.setCity(requestDto.getCity());
        details.setState(requestDto.getState());
        details.setZipCode(requestDto.getZipCode());
        details.setPrimaryContactFirstName(requestDto.getPrimaryContactFirstName());
        details.setPrimaryContactLastName(requestDto.getPrimaryContactLastName());
        details.setPrimaryContactEmail(requestDto.getPrimaryContactEmail());
        details.setPrimaryContactMobile(requestDto.getPrimaryContactMobile());

        company.setCompanyDetails(details);

        Company updatedCompany = companyRepository.save(company);
        return convertToResponseDto(updatedCompany);
    }

    @Override
    public CompanyResponseDto getCompanyById(Long id) {
        Company company = companyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Company not found with id: " + id));
        return convertToResponseDto(company);
    }

    @Override
    public List<CompanyResponseDto> getAllCompanies() {
        return companyRepository.findAll(Sort.by(Sort.Direction.DESC, "createdAt"))
                .stream()
                .map(this::convertToResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public Page<CompanyResponseDto> searchCompanies(CompanySearchDto searchDto) {
        // Create Pageable object based on page number, size, and sorting
        Pageable pageable = PageRequest.of(
                searchDto.getPage(), // Page number (0-based)
                searchDto.getSize(), // Page size
                Sort.by(Sort.Direction.DESC, "createdAt") // Sort by createdAt in descending order
        );

        // Call the repository method to search companies based on the provided criteria and pagination
        Page<Company> companies = companyRepository.searchCompanies(
                searchDto.getCompanyName(),
                searchDto.getRegistrationNumber(),
                searchDto.getCity(),
                searchDto.getState(),
                searchDto.getPrimaryContactEmail(),
                pageable
        );
        return companies.map(this::convertToResponseDto);
    }

    @Override
    @Transactional
    public void deleteCompany(Long id) {
        if (!companyRepository.existsById(id)) {
            throw new ResourceNotFoundException("Company not found with id: " + id);
        }
        companyRepository.deleteById(id);
    }

    // Helper method to convert Company entity to CompanyResponseDto So that we can reuse it in multiple places and show consistent response structure
    private CompanyResponseDto convertToResponseDto(Company company) {
        CompanyResponseDto dto = new CompanyResponseDto();
        dto.setId(company.getId());
        dto.setCompanyName(company.getCompanyName());
        dto.setRegistrationNumber(company.getRegistrationNumber());
        dto.setEstablishedOn(company.getEstablishedOn());
        dto.setWebsite(company.getWebsite());

        if (company.getCompanyDetails() != null) {
            CompanyDetails details = company.getCompanyDetails();
            dto.setAddressLine1(details.getAddressLine1());
            dto.setAddressLine2(details.getAddressLine2());
            dto.setCity(details.getCity());
            dto.setState(details.getState());
            dto.setZipCode(details.getZipCode());
            dto.setPrimaryContactFirstName(details.getPrimaryContactFirstName());
            dto.setPrimaryContactLastName(details.getPrimaryContactLastName());
            dto.setPrimaryContactEmail(details.getPrimaryContactEmail());
            dto.setPrimaryContactMobile(details.getPrimaryContactMobile());
        }

        return dto;
    }
}