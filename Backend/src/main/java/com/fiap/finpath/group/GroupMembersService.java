package com.fiap.finpath.group;

import com.fiap.finpath.organization.Member;

import java.util.List;

public class GroupMembersService {
    public GroupMembers addMemberToGroup(String memberId, String groupId) {
        GroupMembers newMembership = new GroupMembers(memberId, groupId);
        System.out.println("GroupMembersService: Adding member '" + newMembership.getMemberId() + "' to group '" + newMembership.getGroupId() + "'");
        return newMembership;
    }

    public boolean isMemberInGroup(String memberId, String groupId) {
        System.out.println("GroupMembersService: Checking if member '" + memberId + "' is in group '" + groupId + "'");
        return true;
    }

    public List<Member> getMembersByGroup(String groupId) {
        System.out.println("GroupMemberService: Fetching all members for Group ID: " + groupId);
        return List.of();
    }

    public void removeMemberFromGroup(String memberId, String groupId) {
        System.out.println("GroupMembersService: Removing member '" + memberId + "' from group '" + groupId + "'");
    }
}