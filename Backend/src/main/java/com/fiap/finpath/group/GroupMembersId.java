package com.fiap.finpath.group;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotBlank;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class GroupMembersId implements Serializable {

    @NotBlank
    @Column(name = "member_id", length = 36, nullable = false)
    private String memberId;

    @NotBlank
    @Column(name = "group_id", length = 36, nullable = false)
    private String groupId;

    // Construtor padrão
    public GroupMembersId() {}

    public GroupMembersId(String memberId, String groupId) {
        this.memberId = memberId;
        this.groupId = groupId;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GroupMembersId that = (GroupMembersId) o;
        return Objects.equals(memberId, that.memberId) && Objects.equals(groupId, that.groupId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(memberId, groupId);
    }
}
