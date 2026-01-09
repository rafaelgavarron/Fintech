package com.fiap.finpath.group;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GroupMembersRepository extends JpaRepository<GroupMembers, GroupMembersId> {

    // Para chaves compostas (@EmbeddedId), usar a sintaxe id.campo
    @Query("SELECT gm FROM GroupMembers gm WHERE gm.id.memberId = :memberId")
    List<GroupMembers> findById_MemberId(@Param("memberId") String memberId);

    @Query("SELECT gm FROM GroupMembers gm WHERE gm.id.groupId = :groupId")
    List<GroupMembers> findById_GroupId(@Param("groupId") String groupId);

    @Query("SELECT gm FROM GroupMembers gm WHERE gm.id.memberId = :memberId AND gm.id.groupId = :groupId")
    Optional<GroupMembers> findById_MemberIdAndId_GroupId(@Param("memberId") String memberId,
                                                           @Param("groupId") String groupId);

    @Query("SELECT gm FROM GroupMembers gm WHERE gm.id.groupId IN (SELECT g.id FROM Group g WHERE g.organizationId = :organizationId)")
    List<GroupMembers> findByOrganizationId(@Param("organizationId") String organizationId);

    @Query("SELECT CASE WHEN COUNT(gm) > 0 THEN true ELSE false END FROM GroupMembers gm WHERE gm.id.memberId = :memberId AND gm.id.groupId = :groupId")
    boolean existsById_MemberIdAndId_GroupId(@Param("memberId") String memberId, @Param("groupId") String groupId);
}
