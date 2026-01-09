package com.fiap.finpath.organization;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MemberRepository extends JpaRepository<Member, String> {

    List<Member> findByOrganizationId(String organizationId);

    List<Member> findByUserId(String userId);

    List<Member> findByRoleId(String roleId);

    @Query("SELECT m FROM Member m WHERE m.organizationId = :organizationId AND m.roleId = :roleId")
    List<Member> findByOrganizationIdAndRoleId(@Param("organizationId") String organizationId,
                                              @Param("roleId") String roleId);

    boolean existsByUserIdAndOrganizationId(String userId, String organizationId);
}
