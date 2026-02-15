package com.test.CompanyDriverPortal.repository;

import com.test.CompanyDriverPortal.model.CompanyDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompanyDetailsRepository extends JpaRepository<CompanyDetails, Long> { // extend JpaRepository to provide CRUD operations for CompanyDetails entity
}

