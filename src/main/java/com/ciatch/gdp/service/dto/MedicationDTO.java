package com.ciatch.gdp.service.dto;

import com.ciatch.gdp.domain.enumeration.PrescriptionStatus;
import com.ciatch.gdp.domain.enumeration.RiskLevel;
import com.ciatch.gdp.domain.enumeration.RouteAdmin;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.ciatch.gdp.domain.Medication} entity.
 */
@Schema(description = "Dictionnaire central des médicaments.\nChaque médicament est identifié par son nom, code ATC, formulation, etc.")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class MedicationDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(min = 2, max = 100)
    @Schema(description = "Nom commercial du médicament (ex : Doliprane)", requiredMode = Schema.RequiredMode.REQUIRED)
    private String name;

    @Size(max = 100)
    @Schema(description = "Dénomination Commune Internationale (DCI), ex : Paracétamol")
    private String internationalName;

    @Size(max = 20)
    @Schema(description = "Code ATC (Anatomical Therapeutic Chemical Classification)")
    private String codeAtc;

    @Size(max = 50)
    @Schema(description = "Forme pharmaceutique (ex : comprimé, gélule, sirop)")
    private String formulation;

    @Size(max = 50)
    @Schema(description = "Dosage (ex : 500mg, 1g/5ml)")
    private String strength;

    @NotNull
    @Schema(description = "Voie d'administration (orale, injectable, etc.)", requiredMode = Schema.RequiredMode.REQUIRED)
    private RouteAdmin routeOfAdministration;

    @Size(max = 100)
    @Schema(description = "Fabricant ou laboratoire du médicament")
    private String manufacturer;

    @Size(max = 30)
    @Schema(description = "Numéro d’Autorisation de Mise sur le Marché")
    private String marketingAuthorizationNumber;

    @Schema(description = "Date d’obtention de l’AMM")
    private Instant marketingAuthorizationDate;

    @Size(max = 150)
    @Schema(description = "Présentation/conditionnement (ex : boîte de 16 comprimés)")
    private String packaging;

    @NotNull
    @Schema(description = "Statut de prescription (prescription obligatoire, OTC, etc.)", requiredMode = Schema.RequiredMode.REQUIRED)
    private PrescriptionStatus prescriptionStatus;

    @Schema(description = "Description libre du médicament")
    private String description;

    @Schema(description = "Date d’expiration du médicament")
    private Instant expiryDate;

    @Size(max = 50)
    @Schema(description = "Code-barres du médicament (EAN ou autre)")
    private String barcode;

    @Size(max = 100)
    @Schema(description = "Conditions de stockage (ex : à conserver entre 2°C et 8°C)")
    private String storageCondition;

    @Schema(description = "Prix unitaire du médicament")
    private BigDecimal unitPrice;

    @Schema(description = "URL ou chemin de l'image du médicament")
    private String image;

    @Schema(description = "Liste détaillée des principes actifs et excipients")
    private String composition;

    @Schema(description = "Contre-indications du médicament")
    private String contraindications;

    @Schema(description = "Effets secondaires connus")
    private String sideEffects;

    @NotNull
    @Schema(description = "Statut actif ou inactif (permet d’archiver un médicament)", requiredMode = Schema.RequiredMode.REQUIRED)
    private Boolean active;

    @Schema(description = "Ce médicament est-il générique ?")
    private Boolean isGeneric;

    @Schema(description = "Niveau de risque lié au médicament")
    private RiskLevel riskLevel;

    private String createdBy;

    private Instant createdDate;

    private String lastModifiedBy;

    private Instant lastModifiedDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInternationalName() {
        return internationalName;
    }

    public void setInternationalName(String internationalName) {
        this.internationalName = internationalName;
    }

    public String getCodeAtc() {
        return codeAtc;
    }

    public void setCodeAtc(String codeAtc) {
        this.codeAtc = codeAtc;
    }

    public String getFormulation() {
        return formulation;
    }

    public void setFormulation(String formulation) {
        this.formulation = formulation;
    }

    public String getStrength() {
        return strength;
    }

    public void setStrength(String strength) {
        this.strength = strength;
    }

    public RouteAdmin getRouteOfAdministration() {
        return routeOfAdministration;
    }

    public void setRouteOfAdministration(RouteAdmin routeOfAdministration) {
        this.routeOfAdministration = routeOfAdministration;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getMarketingAuthorizationNumber() {
        return marketingAuthorizationNumber;
    }

    public void setMarketingAuthorizationNumber(String marketingAuthorizationNumber) {
        this.marketingAuthorizationNumber = marketingAuthorizationNumber;
    }

    public Instant getMarketingAuthorizationDate() {
        return marketingAuthorizationDate;
    }

    public void setMarketingAuthorizationDate(Instant marketingAuthorizationDate) {
        this.marketingAuthorizationDate = marketingAuthorizationDate;
    }

    public String getPackaging() {
        return packaging;
    }

    public void setPackaging(String packaging) {
        this.packaging = packaging;
    }

    public PrescriptionStatus getPrescriptionStatus() {
        return prescriptionStatus;
    }

    public void setPrescriptionStatus(PrescriptionStatus prescriptionStatus) {
        this.prescriptionStatus = prescriptionStatus;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Instant getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Instant expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getStorageCondition() {
        return storageCondition;
    }

    public void setStorageCondition(String storageCondition) {
        this.storageCondition = storageCondition;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getComposition() {
        return composition;
    }

    public void setComposition(String composition) {
        this.composition = composition;
    }

    public String getContraindications() {
        return contraindications;
    }

    public void setContraindications(String contraindications) {
        this.contraindications = contraindications;
    }

    public String getSideEffects() {
        return sideEffects;
    }

    public void setSideEffects(String sideEffects) {
        this.sideEffects = sideEffects;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Boolean getIsGeneric() {
        return isGeneric;
    }

    public void setIsGeneric(Boolean isGeneric) {
        this.isGeneric = isGeneric;
    }

    public RiskLevel getRiskLevel() {
        return riskLevel;
    }

    public void setRiskLevel(RiskLevel riskLevel) {
        this.riskLevel = riskLevel;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Instant getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public String getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public Instant getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(Instant lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MedicationDTO)) {
            return false;
        }

        MedicationDTO medicationDTO = (MedicationDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, medicationDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MedicationDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", internationalName='" + getInternationalName() + "'" +
            ", codeAtc='" + getCodeAtc() + "'" +
            ", formulation='" + getFormulation() + "'" +
            ", strength='" + getStrength() + "'" +
            ", routeOfAdministration='" + getRouteOfAdministration() + "'" +
            ", manufacturer='" + getManufacturer() + "'" +
            ", marketingAuthorizationNumber='" + getMarketingAuthorizationNumber() + "'" +
            ", marketingAuthorizationDate='" + getMarketingAuthorizationDate() + "'" +
            ", packaging='" + getPackaging() + "'" +
            ", prescriptionStatus='" + getPrescriptionStatus() + "'" +
            ", description='" + getDescription() + "'" +
            ", expiryDate='" + getExpiryDate() + "'" +
            ", barcode='" + getBarcode() + "'" +
            ", storageCondition='" + getStorageCondition() + "'" +
            ", unitPrice=" + getUnitPrice() +
            ", image='" + getImage() + "'" +
            ", composition='" + getComposition() + "'" +
            ", contraindications='" + getContraindications() + "'" +
            ", sideEffects='" + getSideEffects() + "'" +
            ", active='" + getActive() + "'" +
            ", isGeneric='" + getIsGeneric() + "'" +
            ", riskLevel='" + getRiskLevel() + "'" +
            ", createdBy=" + createdBy +
            ", createdDate=" + createdDate +
            ", lastModifiedBy='" + lastModifiedBy + '\'' +
            ", lastModifiedDate=" + lastModifiedDate +
            "}";
    }
}
