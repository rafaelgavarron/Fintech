package com.fiap.finpath.organization;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrganizationRepository extends JpaRepository<Organization, String> {

    List<Organization> findByIsActive(boolean isActive);

    @Query("SELECT o FROM Organization o WHERE o.isActive = true")
    List<Organization> findActiveOrganizations();
}
