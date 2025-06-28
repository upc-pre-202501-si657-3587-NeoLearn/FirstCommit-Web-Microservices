package com.neolearn.iam_service.iam.domain.model.valueobjects;

public enum SubscriptionTier {
    FREE,
    BASIC,
    PRO;

    public static SubscriptionTier getDefaultTier() {
        return FREE;
    }

    public static SubscriptionTier fromString(String tier) {
        if (tier == null || tier.trim().isEmpty()) {
            return getDefaultTier();
        }
        try {
            return SubscriptionTier.valueOf(tier.toUpperCase());
        } catch (IllegalArgumentException e) {
            return getDefaultTier();
        }
    }
}