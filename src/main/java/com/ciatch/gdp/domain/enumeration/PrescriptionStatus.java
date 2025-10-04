package com.ciatch.gdp.domain.enumeration;

/**
 * Statut de prescription du médicament.
 */
public enum PrescriptionStatus {
    /**
     * Sur prescription médicale obligatoire
     */
    PRESCRIPTION,
    /**
     * Médicament en vente libre (Over The Counter)
     */
    OTC,
    /**
     * Usage réservé à l’hôpital
     */
    HOSPITAL_USE_ONLY,
}
