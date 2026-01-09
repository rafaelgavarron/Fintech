package com.fiap.finpath.auth;

import java.util.List;

public class RolesActionService {

    public RolesAction assignActionToRole(String roleId, String actionId) {
        RolesAction newRolesAction = new RolesAction(roleId, actionId);
        System.out.println("RolesActionService: Assigning action " + actionId + " to role " + roleId);
        return newRolesAction;
    }

    public boolean hasRoleAction(String roleId, String actionId) {
        System.out.println("RolesActionService: Checking if role " + roleId + " has action " + actionId);
        return true;
    }

    public void removeActionFromRole(String roleId, String actionId) {
        System.out.println("RolesActionService: Removing action " + actionId + " from role " + roleId);
    }

    public List<Action> getActionsForRole(String roleId) {
        System.out.println("RolesActionService: Retrieving all actions for role " + roleId);
        return List.of();
    }
}