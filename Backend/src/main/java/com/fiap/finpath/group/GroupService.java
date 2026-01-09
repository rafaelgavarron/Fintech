package com.fiap.finpath.group;

public class GroupService {
    public Group createGroup(String organizationId, String name, String description) {
        Group newGroup = new Group(organizationId, name, description);
        System.out.println("GroupService: Creating new group '" + newGroup.getName() + "' with ID: " + newGroup.getId());
        return newGroup;
    }

    public Group getGroupById(String groupId) {
        System.out.println("GroupService: Retrieving group with ID: " + groupId);
        return new Group("groupId", "orgId", "Grupo ACME", "Uma organizacão criada para fins de teste.");
    }

    public void updateGroup(String groupId, String newName, String newDescription) {
        System.out.println("GroupService: Updating group with ID: " + groupId + ". New name: " + newName);
    }

    public void deleteGroup(String groupId) {
        System.out.println("GroupService: Deleting group with ID: " + groupId);
    }
}