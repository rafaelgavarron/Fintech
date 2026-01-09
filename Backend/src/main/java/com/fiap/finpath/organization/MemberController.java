package com.fiap.finpath.organization;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/members")
@CrossOrigin(origins = "*")
public class MemberController {

    @Autowired
    private MemberService memberService;

    // GET - Consultar todos os membros
    @GetMapping
    public ResponseEntity<List<Member>> getAllMembers() {
        try {
            List<Member> members = memberService.getAllMembers();
            return ResponseEntity.ok(members);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // GET - Consultar membro por ID
    @GetMapping("/{id}")
    public ResponseEntity<Member> getMemberById(@PathVariable String id) {
        try {
            Optional<Member> member = memberService.getMemberById(id);
            if (member.isPresent()) {
                return ResponseEntity.ok(member.get());
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // GET - Consultar membros por organização
    @GetMapping("/organization/{organizationId}")
    public ResponseEntity<List<Member>> getMembersByOrganization(@PathVariable String organizationId) {
        try {
            List<Member> members = memberService.getMembersByOrganization(organizationId);
            return ResponseEntity.ok(members);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // GET - Consultar membros por usuário
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Member>> getMembersByUserId(@PathVariable String userId) {
        try {
            List<Member> members = memberService.getMembersByUserId(userId);
            return ResponseEntity.ok(members);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // GET - Consultar membros por role
    @GetMapping("/role/{roleId}")
    public ResponseEntity<List<Member>> getMembersByRoleId(@PathVariable String roleId) {
        try {
            List<Member> members = memberService.getMembersByRoleId(roleId);
            return ResponseEntity.ok(members);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // GET - Consultar membros por organização e role
    @GetMapping("/organization/{organizationId}/role/{roleId}")
    public ResponseEntity<List<Member>> getMembersByOrganizationAndRole(
            @PathVariable String organizationId,
            @PathVariable String roleId) {
        try {
            List<Member> members = memberService.getMembersByOrganization(organizationId).stream()
                    .filter(m -> m.getRoleId().equals(roleId))
                    .toList();
            return ResponseEntity.ok(members);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // GET - Consultar membro por usuário e organização
    @GetMapping("/user/{userId}/organization/{organizationId}")
    public ResponseEntity<Member> getMemberByUserAndOrganization(@PathVariable String userId, @PathVariable String organizationId) {
        try {
            Optional<Member> member = memberService.getMemberByUserAndOrganization(userId, organizationId);
            if (member.isPresent()) {
                return ResponseEntity.ok(member.get());
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // POST - Criar novo membro
    @PostMapping
    public ResponseEntity<?> createMember(@RequestBody MemberRequest request) {
        try {
            // Validar request
            if (request.getOrganizationId() == null || request.getOrganizationId().trim().isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("error", "Organization ID is required"));
            }
            if (request.getUserId() == null || request.getUserId().trim().isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("error", "User ID is required"));
            }
            if (request.getRoleId() == null || request.getRoleId().trim().isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("error", "Role ID is required"));
            }

            // Verificar se o membro já existe
            if (memberService.existsByUserIdAndOrganizationId(request.getUserId(), request.getOrganizationId())) {
                Optional<Member> existingMember = memberService.getMemberByUserAndOrganization(
                    request.getUserId(), 
                    request.getOrganizationId()
                );
                if (existingMember.isPresent()) {
                    return ResponseEntity.ok(existingMember.get());
                }
            }

            Member member = memberService.addMemberToOrganization(
                request.getOrganizationId(),
                request.getUserId(),
                request.getRoleId()
            );
            return ResponseEntity.status(HttpStatus.CREATED).body(member);
        } catch (Exception e) {
            e.printStackTrace(); // Log no console do servidor
            String errorMessage = e.getMessage() != null ? e.getMessage() : "Internal server error";
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", errorMessage, "message", "Failed to create member: " + errorMessage));
        }
    }

    // PUT - Atualizar role do membro
    @PutMapping("/{id}/role")
    public ResponseEntity<Member> updateMemberRole(@PathVariable String id, @RequestBody RoleUpdateRequest request) {
        try {
            Member updatedMember = memberService.updateMemberRole(id, request.getRoleId());
            if (updatedMember != null) {
                return ResponseEntity.ok(updatedMember);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // DELETE - Deletar membro
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMember(@PathVariable String id) {
        try {
            memberService.removeMemberFromOrganization(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // GET - Verificar se usuário é membro da organização
    @GetMapping("/check-membership")
    public ResponseEntity<Boolean> checkMembership(@RequestParam String userId, @RequestParam String organizationId) {
        try {
            boolean isMember = memberService.existsByUserIdAndOrganizationId(userId, organizationId);
            return ResponseEntity.ok(isMember);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Classes internas para requests
    public static class MemberRequest {
        private String organizationId;
        private String userId;
        private String roleId;

        // Getters e Setters
        public String getOrganizationId() { return organizationId; }
        public void setOrganizationId(String organizationId) { this.organizationId = organizationId; }
        public String getUserId() { return userId; }
        public void setUserId(String userId) { this.userId = userId; }
        public String getRoleId() { return roleId; }
        public void setRoleId(String roleId) { this.roleId = roleId; }
    }

    public static class RoleUpdateRequest {
        private String roleId;

        // Getters e Setters
        public String getRoleId() { return roleId; }
        public void setRoleId(String roleId) { this.roleId = roleId; }
    }
}
