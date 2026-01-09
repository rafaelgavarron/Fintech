package com.fiap.finpath.organization;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/organizations")
@CrossOrigin(origins = "*")
public class OrganizationController {

    @Autowired
    private OrganizationService organizationService;

    // GET - Consultar todas as organizações
    @GetMapping
    public ResponseEntity<List<Organization>> getAllOrganizations() {
        try {
            List<Organization> organizations = organizationService.getAllOrganizations();
            return ResponseEntity.ok(organizations);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // GET - Consultar organizações ativas
    @GetMapping("/active")
    public ResponseEntity<List<Organization>> getActiveOrganizations() {
        try {
            List<Organization> organizations = organizationService.getActiveOrganizations();
            return ResponseEntity.ok(organizations);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // GET - Consultar organização por ID
    @GetMapping("/{id}")
    public ResponseEntity<Organization> getOrganizationById(@PathVariable String id) {
        try {
            Optional<Organization> organization = organizationService.getOrganizationById(id);
            if (organization.isPresent()) {
                return ResponseEntity.ok(organization.get());
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // POST - Criar nova organização
    @PostMapping
    public ResponseEntity<Organization> createOrganization(@RequestBody OrganizationRequest request) {
        try {
            Organization organization = organizationService.createOrganization(
                request.getName(),
                request.isActive(),
                request.getTrialExpireAt()
            );
            return ResponseEntity.status(HttpStatus.CREATED).body(organization);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // PUT - Atualizar organização
    @PutMapping("/{id}")
    public ResponseEntity<Organization> updateOrganization(@PathVariable String id, @RequestBody OrganizationUpdateRequest request) {
        try {
            Organization updatedOrganization = organizationService.updateOrganization(
                id,
                request.getName(),
                request.isActive(),
                request.getTrialExpireAt()
            );
            if (updatedOrganization != null) {
                return ResponseEntity.ok(updatedOrganization);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // DELETE - Deletar organização
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrganization(@PathVariable String id) {
        try {
            organizationService.deleteOrganization(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Classes internas para requests
    public static class OrganizationRequest {
        private String name;
        private boolean isActive;
        private long trialExpireAt;

        // Getters e Setters
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public boolean isActive() { return isActive; }
        public void setActive(boolean active) { isActive = active; }
        public void setIsActive(boolean active) { isActive = active; } // Alias para Jackson
        public long getTrialExpireAt() { return trialExpireAt; }
        public void setTrialExpireAt(long trialExpireAt) { this.trialExpireAt = trialExpireAt; }
    }

    public static class OrganizationUpdateRequest {
        private String name;
        private boolean isActive;
        private long trialExpireAt;

        // Getters e Setters
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public boolean isActive() { return isActive; }
        public void setActive(boolean active) { isActive = active; }
        public void setIsActive(boolean active) { isActive = active; }
        public long getTrialExpireAt() { return trialExpireAt; }
        public void setTrialExpireAt(long trialExpireAt) { this.trialExpireAt = trialExpireAt; }
    }
}
