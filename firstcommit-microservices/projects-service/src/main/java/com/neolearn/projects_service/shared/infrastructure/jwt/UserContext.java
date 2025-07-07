package com.neolearn.projects_service.shared.infrastructure.jwt;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserContext {
    private Long id;
    private String usuario;
    private String rol;
    private String tierSubscription;
    private Date exp;

    public boolean isExpired() {
        return exp != null && exp.before(new Date());
    }

    public boolean hasRole(String role) {
        return rol != null && rol.equalsIgnoreCase(role);
    }

    public boolean hasTier(String tier) {
        return tierSubscription != null && tierSubscription.equalsIgnoreCase(tier);
    }

    public boolean hasTierOrHigher(String minimumTier) {
        if (tierSubscription == null) return false;
        
        // Definir jerarquía de tiers (puedes ajustar según tu lógica de negocio)
        String[] tierHierarchy = {"FREE", "BASIC", "PREMIUM", "ENTERPRISE"};
        
        int currentTierIndex = getTierIndex(tierSubscription, tierHierarchy);
        int minimumTierIndex = getTierIndex(minimumTier, tierHierarchy);
        
        return currentTierIndex >= minimumTierIndex;
    }

    private int getTierIndex(String tier, String[] hierarchy) {
        for (int i = 0; i < hierarchy.length; i++) {
            if (hierarchy[i].equalsIgnoreCase(tier)) {
                return i;
            }
        }
        return -1; // Tier no encontrado
    }
}