package com.test.CompanyDriverPortal.service;

import com.test.CompanyDriverPortal.dto.company.CompanyRequestDto;
import com.test.CompanyDriverPortal.dto.company.CompanyResponseDto;
import com.test.CompanyDriverPortal.dto.company.CompanySearchDto;
import org.springframework.data.domain.Page;

import java.util.List;

public interface CompanyService {
    CompanyResponseDto createCompany(CompanyRequestDto requestDto);
    CompanyResponseDto updateCompany(Long id, CompanyRequestDto requestDto);
    CompanyResponseDto getCompanyById(Long id);
    List<CompanyResponseDto> getAllCompanies();
    Page<CompanyResponseDto> searchCompanies(CompanySearchDto searchDto);
    void deleteCompany(Long id);
}