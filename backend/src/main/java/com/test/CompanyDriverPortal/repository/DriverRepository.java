package com.test.CompanyDriverPortal.repository;

import com.test.CompanyDriverPortal.model.Driver;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DriverRepository extends JpaRepository<Driver, Long> {

    Optional<Driver> findByEmail(String email);
    Optional<Driver> findByLicenseNumber(String licenseNumber);

    @Query("SELECT d FROM Driver d " +
            "LEFT JOIN d.driverDetails dd " +
            "WHERE (:firstName IS NULL OR LOWER(d.firstName) LIKE LOWER(CONCAT('%', :firstName, '%'))) " +
            "AND (:lastName IS NULL OR LOWER(d.lastName) LIKE LOWER(CONCAT('%', :lastName, '%'))) " +
            "AND (:email IS NULL OR LOWER(d.email) LIKE LOWER(CONCAT('%', :email, '%'))) " +
            "AND (:licenseNumber IS NULL OR LOWER(d.licenseNumber) LIKE LOWER(CONCAT('%', :licenseNumber, '%'))) " +
            "AND (:city IS NULL OR LOWER(dd.city) LIKE LOWER(CONCAT('%', :city, '%'))) " +
            "AND (:state IS NULL OR LOWER(dd.state) LIKE LOWER(CONCAT('%', :state, '%')))")
    Page<Driver> searchDrivers(
            @Param("firstName") String firstName,
            @Param("lastName") String lastName,
            @Param("email") String email,
            @Param("licenseNumber") String licenseNumber,
            @Param("city") String city,
            @Param("state") String state,
            Pageable pageable
    );
}