package com.ciatch.gdp.domain.enumeration;

/**
 * Enumération des voies d’administration possibles pour un médicament.
 */
public enum RouteAdmin {
    /**
     * Administration orale (par la bouche)
     */
    ORAL,
    /**
     * Administration par voie intraveineuse
     */
    INTRAVENOUS,
    /**
     * Administration par voie sous-cutanée
     */
    SUBCUTANEOUS,
    /**
     * Administration par inhalation
     */
    INHALATION,
    /**
     * Application locale (crème, patch, etc.)
     */
    TOPICAL,
    /**
     * Autre voie non listée
     */
    OTHER,
}
