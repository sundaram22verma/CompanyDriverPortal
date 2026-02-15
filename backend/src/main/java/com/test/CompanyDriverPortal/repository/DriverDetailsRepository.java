package com.test.CompanyDriverPortal.repository;

import com.test.CompanyDriverPortal.model.DriverDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DriverDetailsRepository extends JpaRepository<DriverDetails, Long> {
}