package com.fiap.finpath.auth;

import java.util.List;

public class ActionService {

    public Action createAction(String name, String description) {
        Action newAction = new Action(name, description);
        System.out.println("ActionService: Creating new action: " + newAction.getName() + " with ID: " + newAction.getId());
        return newAction;
    }

    public Action getActionById(String actionId) {
        System.out.println("ActionService: Retrieving action with ID: " + actionId);
        return new Action(actionId, "read_expenses", "Action para ler gastos.");
    }

    public Action getActionByName(String actionName) {
        System.out.println("ActionService: Retrieving action by name: " + actionName);
        return new Action("actionId", actionName, "A placeholder action by name.");
    }

    public void updateAction(String actionId, String newDescription) {
        System.out.println("ActionService: Updating description for action " + actionId);
    }
}