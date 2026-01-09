package com.fiap.finpath.organization;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MemberService {

    @Autowired
    private MemberRepository memberRepository;

    public Member addMemberToOrganization(String organizationId, String userId, String roleId) {
        // Validar inputs
        if (organizationId == null || organizationId.trim().isEmpty()) {
            throw new IllegalArgumentException("Organization ID cannot be null or empty");
        }
        if (userId == null || userId.trim().isEmpty()) {
            throw new IllegalArgumentException("User ID cannot be null or empty");
        }
        if (roleId == null || roleId.trim().isEmpty()) {
            throw new IllegalArgumentException("Role ID cannot be null or empty");
        }

        // Verificar se já existe membro para este usuário e organização
        if (memberRepository.existsByUserIdAndOrganizationId(userId, organizationId)) {
            List<Member> existing = memberRepository.findByUserId(userId);
            Optional<Member> member = existing.stream()
                .filter(m -> m.getOrganizationId().equals(organizationId))
                .findFirst();
            if (member.isPresent()) {
                System.out.println("MemberService: Member already exists, returning existing member: " + member.get().getId());
                return member.get();
            }
        }

        Member newMember = new Member(organizationId, userId, roleId);
        System.out.println("MemberService: Adding User " + userId + " to Organization " + organizationId + " with Role " + roleId + ". Member ID: " + newMember.getId());
        
        try {
            Member savedMember = memberRepository.save(newMember);
            System.out.println("MemberService: Member saved successfully with ID: " + savedMember.getId());
            return savedMember;
        } catch (Exception e) {
            System.err.println("MemberService: Error saving member - " + e.getClass().getSimpleName() + ": " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to create member: " + e.getMessage(), e);
        }
    }

    public Optional<Member> getMemberById(String memberId) {
        System.out.println("MemberService: Retrieving member with ID: " + memberId);
        return memberRepository.findById(memberId);
    }

    public Optional<Member> getMemberByUserAndOrganization(String userId, String organizationId) {
        System.out.println("MemberService: Retrieving member for User " + userId + " in Organization " + organizationId);
        List<Member> members = memberRepository.findByUserId(userId);
        return members.stream()
                .filter(member -> member.getOrganizationId().equals(organizationId))
                .findFirst();
    }

    public Member updateMemberRole(String memberId, String newRoleId) {
        System.out.println("MemberService: Updating role for member ID: " + memberId + " to new role: " + newRoleId);
        Optional<Member> optionalMember = memberRepository.findById(memberId);
        if (optionalMember.isPresent()) {
            Member member = optionalMember.get();
            member.setRoleId(newRoleId);
            return memberRepository.save(member);
        }
        return null;
    }

    public void removeMemberFromOrganization(String memberId) {
        System.out.println("MemberService: Removing member with ID: " + memberId + " from organization.");
        memberRepository.deleteById(memberId);
    }

    public List<Member> getMembersByOrganization(String organizationId) {
        System.out.println("MemberService: Fetching all members for Organization ID: " + organizationId);
        return memberRepository.findByOrganizationId(organizationId);
    }

    public List<Member> getAllMembers() {
        return memberRepository.findAll();
    }

    public List<Member> getMembersByUserId(String userId) {
        return memberRepository.findByUserId(userId);
    }

    public List<Member> getMembersByRoleId(String roleId) {
        return memberRepository.findByRoleId(roleId);
    }

    public boolean existsByUserIdAndOrganizationId(String userId, String organizationId) {
        return memberRepository.existsByUserIdAndOrganizationId(userId, organizationId);
    }
}