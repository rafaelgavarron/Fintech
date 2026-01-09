package com.fiap.finpath.auth;

public class RolesAction {
    private final String roleId;
    private final String actionId;

    public RolesAction(String roleId, String actionId) {
        this.roleId = roleId;
        this.actionId = actionId;
    }

    //* Getter Methods *//
    public String getRoleId() {
        return roleId;
    }
    public String getActionId() {
        return actionId;
    }
}
