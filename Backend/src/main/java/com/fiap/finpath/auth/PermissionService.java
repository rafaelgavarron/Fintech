package com.fiap.finpath.auth;

import com.fiap.finpath.organization.Member;
import com.fiap.finpath.organization.MemberService;
import java.util.Optional;

public class PermissionService {

    private final MemberService memberService;
    private final RolesActionService rolesActionService;
    private final ActionService actionService;

    public PermissionService() {
        this.memberService = new MemberService();
        this.rolesActionService = new RolesActionService();
        this.actionService = new ActionService();
    }

    public boolean hasPermission(String memberId, String organizationId, String actionName) {
        System.out.println("PermissionService: Checking permission for Member " + memberId + " to perform action " + actionName + " in organization " + organizationId);

        // 1. Retrieve the Member object to get their assigned role.
         Optional<Member> memberOptional = memberService.getMemberById(memberId);
         if (!memberOptional.isPresent()) {
             System.out.println("PermissionService: Member not found.");
             return false;
         }
         Member member = memberOptional.get();
         if (!member.getOrganizationId().equals(organizationId)) {
             System.out.println("PermissionService: Member does not belong to organization.");
             return false;
         }
         String roleId = member.getRoleId();

        // 2. Retrieve the Action object to get its ID.
         Action action = actionService.getActionByName(actionName);
         if (action == null) {
             System.out.println("PermissionService: Action '" + actionName + "' not defined.");
             return false;
         }
         String actionId = action.getId();

        // 3. Check if the role has the action assigned.
         boolean permissionGranted = rolesActionService.hasRoleAction(roleId, actionId);
         System.out.println("PermissionService: Permission granted: " + permissionGranted);
         return permissionGranted;
    }
}