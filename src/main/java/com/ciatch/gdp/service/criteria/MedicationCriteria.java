package com.ciatch.gdp.service.criteria;

import com.ciatch.gdp.domain.enumeration.PrescriptionStatus;
import com.ciatch.gdp.domain.enumeration.RiskLevel;
import com.ciatch.gdp.domain.enumeration.RouteAdmin;
import java.io.Serializable;
import java.math.BigDecimal;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

public class MedicationCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private StringFilter name;
    private StringFilter codeAtc;
    private StringFilter manufacturer;
    private BigDecimalFilter unitPriceMin;
    private BigDecimalFilter unitPriceMax;
    private BooleanFilter active;
    private StringFilter formulation;
    private StringFilter strength;
    private Filter<RouteAdmin> routeOfAdministration;
    private Filter<PrescriptionStatus> prescriptionStatus;
    private Filter<RiskLevel> riskLevel;

    public MedicationCriteria() {}

    @Override
    public MedicationCriteria copy() {
        MedicationCriteria copy = new MedicationCriteria();
        copy.setName(this.name == null ? null : this.name.copy());
        copy.setCodeAtc(this.codeAtc == null ? null : this.codeAtc.copy());
        copy.setManufacturer(this.manufacturer == null ? null : this.manufacturer.copy());
        copy.setUnitPriceMin(this.unitPriceMin == null ? null : this.unitPriceMin.copy());
        copy.setUnitPriceMax(this.unitPriceMax == null ? null : this.unitPriceMax.copy());
        copy.setActive(this.active == null ? null : this.active.copy());
        copy.setFormulation(this.formulation == null ? null : this.formulation.copy());
        copy.setStrength(this.strength == null ? null : this.strength.copy());
        copy.setRouteOfAdministration(this.routeOfAdministration == null ? null : this.routeOfAdministration.copy());
        copy.setPrescriptionStatus(this.prescriptionStatus == null ? null : this.prescriptionStatus.copy());
        copy.setRiskLevel(this.riskLevel == null ? null : this.riskLevel.copy());
        return copy;
    }

    public StringFilter getName() {
        return name;
    }

    public void setName(StringFilter name) {
        this.name = name;
    }

    // Add all getters and setters
    public StringFilter getCodeAtc() {
        return codeAtc;
    }

    public void setCodeAtc(StringFilter codeAtc) {
        this.codeAtc = codeAtc;
    }

    public StringFilter getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(StringFilter manufacturer) {
        this.manufacturer = manufacturer;
    }

    public BigDecimalFilter getUnitPriceMin() {
        return unitPriceMin;
    }

    public void setUnitPriceMin(BigDecimalFilter unitPriceMin) {
        this.unitPriceMin = unitPriceMin;
    }

    public BigDecimalFilter getUnitPriceMax() {
        return unitPriceMax;
    }

    public void setUnitPriceMax(BigDecimalFilter unitPriceMax) {
        this.unitPriceMax = unitPriceMax;
    }

    public BooleanFilter getActive() {
        return active;
    }

    public void setActive(BooleanFilter active) {
        this.active = active;
    }

    public StringFilter getFormulation() {
        return formulation;
    }

    public void setFormulation(StringFilter formulation) {
        this.formulation = formulation;
    }

    public StringFilter getStrength() {
        return strength;
    }

    public void setStrength(StringFilter strength) {
        this.strength = strength;
    }

    public Filter<RouteAdmin> getRouteOfAdministration() {
        return routeOfAdministration;
    }

    public void setRouteOfAdministration(Filter<RouteAdmin> routeOfAdministration) {
        this.routeOfAdministration = routeOfAdministration;
    }

    public Filter<PrescriptionStatus> getPrescriptionStatus() {
        return prescriptionStatus;
    }

    public void setPrescriptionStatus(Filter<PrescriptionStatus> prescriptionStatus) {
        this.prescriptionStatus = prescriptionStatus;
    }

    public Filter<RiskLevel> getRiskLevel() {
        return riskLevel;
    }

    public void setRiskLevel(Filter<RiskLevel> riskLevel) {
        this.riskLevel = riskLevel;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MedicationCriteria that = (MedicationCriteria) o;
        return java.util.Objects.equals(name, that.name) && java.util.Objects.equals(active, that.active);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(name, active);
    }
}
