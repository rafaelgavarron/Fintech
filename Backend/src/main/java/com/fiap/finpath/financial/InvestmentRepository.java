package com.fiap.finpath.financial;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InvestmentRepository extends JpaRepository<Investment, String> {

    List<Investment> findByOrganizationId(String organizationId);

    List<Investment> findByMemberId(String memberId);

    @Query("SELECT SUM(i.amount) FROM Investment i WHERE i.organizationId = :organizationId")
    Long getTotalAmountByOrganization(@Param("organizationId") String organizationId);
}

