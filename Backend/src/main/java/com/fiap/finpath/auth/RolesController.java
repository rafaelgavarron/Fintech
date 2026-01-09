package com.fiap.finpath.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/roles")
@CrossOrigin(origins = "*")
public class RolesController {

    @Autowired
    private RoleService roleService;

    // GET - Consultar todos os roles
    @GetMapping
    public ResponseEntity<List<Roles>> getAllRoles() {
        try {
            List<Roles> roles = roleService.getAllRoles();
            return ResponseEntity.ok(roles);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // GET - Consultar role por ID
    @GetMapping("/{id}")
    public ResponseEntity<Roles> getRoleById(@PathVariable String id) {
        try {
            Optional<Roles> role = roleService.getRoleById(id);
            if (role.isPresent()) {
                return ResponseEntity.ok(role.get());
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // GET - Consultar role por nome
    @GetMapping("/name/{name}")
    public ResponseEntity<Roles> getRoleByName(@PathVariable String name) {
        try {
            Optional<Roles> role = roleService.getRoleByName(name);
            if (role.isPresent()) {
                return ResponseEntity.ok(role.get());
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // POST - Criar novo role
    @PostMapping
    public ResponseEntity<Roles> createRole(@RequestBody RoleRequest request) {
        try {
            Roles role = roleService.createRole(request.getName(), request.getDescription());
            return ResponseEntity.status(HttpStatus.CREATED).body(role);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // PUT - Atualizar role
    @PutMapping("/{id}")
    public ResponseEntity<Roles> updateRole(@PathVariable String id, @RequestBody RoleUpdateRequest request) {
        try {
            Roles updatedRole = roleService.updateRole(id, request.getName(), request.getDescription());
            if (updatedRole != null) {
                return ResponseEntity.ok(updatedRole);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // DELETE - Deletar role
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRole(@PathVariable String id) {
        try {
            roleService.deleteRole(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Classes internas para requests
    public static class RoleRequest {
        private String name;
        private String description;

        // Getters e Setters
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
    }

    public static class RoleUpdateRequest {
        private String name;
        private String description;

        // Getters e Setters
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
    }
}
