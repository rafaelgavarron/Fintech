package com.fiap.finpath.group;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GroupRepository extends JpaRepository<Group, String> {

    List<Group> findByOrganizationId(String organizationId);

    List<Group> findByNameContaining(String name);

    @Query("SELECT g FROM Group g WHERE g.organizationId = :organizationId AND g.name LIKE %:name%")
    List<Group> findByOrganizationIdAndNameContaining(@Param("organizationId") String organizationId,
                                                     @Param("name") String name);
}
