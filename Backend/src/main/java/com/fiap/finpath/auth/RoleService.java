package com.fiap.finpath.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RoleService {

    @Autowired
    private RolesRepository rolesRepository;

    public Roles createRole(String name, String description) {
        Roles newRole = new Roles(name, description);
        System.out.println("RoleService: Creating new role: " + newRole.getName() + " with ID: " + newRole.getId());
        return rolesRepository.save(newRole);
    }

    public Optional<Roles> getRoleById(String roleId) {
        System.out.println("RoleService: Retrieving role with ID: " + roleId);
        return rolesRepository.findById(roleId);
    }

    public Optional<Roles> getRoleByName(String name) {
        System.out.println("RoleService: Retrieving role with name: " + name);
        return rolesRepository.findByName(name);
    }

    public Roles updateRole(String roleId, String newName, String newDescription) {
        System.out.println("RoleService: Updating role " + roleId + ". New name: " + newName);
        Optional<Roles> optionalRole = rolesRepository.findById(roleId);
        if (optionalRole.isPresent()) {
            Roles role = optionalRole.get();
            role.setName(newName);
            role.setDescription(newDescription);
            return rolesRepository.save(role);
        }
        return null;
    }

    public void deleteRole(String roleId) {
        System.out.println("RoleService: Deleting role with ID: " + roleId);
        rolesRepository.deleteById(roleId);
    }

    public List<Roles> getAllRoles() {
        return rolesRepository.findAll();
    }

    public boolean existsByName(String name) {
        return rolesRepository.existsByName(name);
    }
}