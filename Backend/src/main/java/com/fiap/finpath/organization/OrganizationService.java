package com.fiap.finpath.organization;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrganizationService {

    @Autowired
    private OrganizationRepository organizationRepository;

    public Organization createOrganization(String name, boolean isActive, long trialExpireAt) {
        Organization newOrganization = new Organization(name, isActive, trialExpireAt);
        System.out.println("OrganizationService: Creating new organization with ID: " + newOrganization.getId());
        return organizationRepository.save(newOrganization);
    }

    public Optional<Organization> getOrganizationById(String organizationId) {
        System.out.println("OrganizationService: Retrieving organization with ID: " + organizationId);
        return organizationRepository.findById(organizationId);
    }

    public Organization updateOrganization(String organizationId, String newName, boolean newIsActive, long newTrialExpireAt) {
        System.out.println("OrganizationService: Updating organization with ID: " + organizationId + ". New name: " + newName);
        Optional<Organization> optionalOrganization = organizationRepository.findById(organizationId);
        if (optionalOrganization.isPresent()) {
            Organization organization = optionalOrganization.get();
            organization.setName(newName);
            organization.setActive(newIsActive);
            organization.setTrialExpireAt(newTrialExpireAt);
            return organizationRepository.save(organization);
        }
        return null;
    }

    public void deleteOrganization(String organizationId) {
        System.out.println("OrganizationService: Deleting organization with ID: " + organizationId);
        organizationRepository.deleteById(organizationId);
    }

    public List<Organization> getAllOrganizations() {
        return organizationRepository.findAll();
    }

    public List<Organization> getActiveOrganizations() {
        return organizationRepository.findActiveOrganizations();
    }
}