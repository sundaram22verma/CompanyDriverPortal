package com.test.CompanyDriverPortal.repository;

import com.test.CompanyDriverPortal.model.Company;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Long> {

    Optional<Company> findByRegistrationNumber(String registrationNumber);

    // This custom query allows searching for companies based on multiple criteria, including company name, registration number, city, state, and primary contact email.
    @Query("SELECT c FROM Company c " +
            "LEFT JOIN c.companyDetails cd " +
            "WHERE (:companyName IS NULL OR LOWER(c.companyName) LIKE LOWER(CONCAT('%', :companyName, '%'))) " +
            "AND (:registrationNumber IS NULL OR LOWER(c.registrationNumber) LIKE LOWER(CONCAT('%', :registrationNumber, '%'))) " +
            "AND (:city IS NULL OR LOWER(cd.city) LIKE LOWER(CONCAT('%', :city, '%'))) " +
            "AND (:state IS NULL OR LOWER(cd.state) LIKE LOWER(CONCAT('%', :state, '%'))) " +
            "AND (:primaryContactEmail IS NULL OR LOWER(cd.primaryContactEmail) LIKE LOWER(CONCAT('%', :primaryContactEmail, '%')))")
    Page<Company> searchCompanies(
            @Param("companyName") String companyName,
            @Param("registrationNumber") String registrationNumber,
            @Param("city") String city,
            @Param("state") String state,
            @Param("primaryContactEmail") String primaryContactEmail,
            Pageable pageable
    );
}