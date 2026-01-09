package com.fiap.finpath.group;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "GROUP_MEMBERS")
public class GroupMembers {

    @EmbeddedId
    private GroupMembersId id;

    // Construtor padrão para JPA
    public GroupMembers() {}

    public GroupMembers(String memberId, String groupId) {
        this.id = new GroupMembersId(memberId, groupId);
    }

    public GroupMembersId getId() {
        return id;
    }

    public void setId(GroupMembersId id) {
        this.id = id;
    }

    public String getMemberId() {
        return id != null ? id.getMemberId() : null;
    }

    public String getGroupId() {
        return id != null ? id.getGroupId() : null;
    }
}
