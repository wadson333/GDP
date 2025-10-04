package com.ciatch.gdp.domain;

import com.ciatch.gdp.domain.enumeration.PrescriptionStatus;
import com.ciatch.gdp.domain.enumeration.RiskLevel;
import com.ciatch.gdp.domain.enumeration.RouteAdmin;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * Dictionnaire central des médicaments.
 * Chaque médicament est identifié par son nom, code ATC, formulation, etc.
 */
@Entity
@Table(name = "medication")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Medication extends AbstractAuditingEntity<Long> implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    /**
     * Nom commercial du médicament (ex : Doliprane)
     */
    @NotNull
    @Size(min = 2, max = 100)
    @Column(name = "name", length = 100, nullable = false, unique = true)
    private String name;

    /**
     * Dénomination Commune Internationale (DCI), ex : Paracétamol
     */
    @Size(max = 100)
    @Column(name = "international_name", length = 100)
    private String internationalName;

    /**
     * Code ATC (Anatomical Therapeutic Chemical Classification)
     */
    @Size(max = 20)
    @Column(name = "code_atc", length = 20)
    private String codeAtc;

    /**
     * Forme pharmaceutique (ex : comprimé, gélule, sirop)
     */
    @Size(max = 50)
    @Column(name = "formulation", length = 50)
    private String formulation;

    /**
     * Dosage (ex : 500mg, 1g/5ml)
     */
    @Size(max = 50)
    @Column(name = "strength", length = 50)
    private String strength;

    /**
     * Voie d'administration (orale, injectable, etc.)
     */
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "route_of_administration", nullable = false)
    private RouteAdmin routeOfAdministration;

    /**
     * Fabricant ou laboratoire du médicament
     */
    @Size(max = 100)
    @Column(name = "manufacturer", length = 100)
    private String manufacturer;

    /**
     * Numéro d’Autorisation de Mise sur le Marché
     */
    @Size(max = 30)
    @Column(name = "marketing_authorization_number", length = 30)
    private String marketingAuthorizationNumber;

    /**
     * Date d’obtention de l’AMM
     */
    @Column(name = "marketing_authorization_date")
    private Instant marketingAuthorizationDate;

    /**
     * Présentation/conditionnement (ex : boîte de 16 comprimés)
     */
    @Size(max = 150)
    @Column(name = "packaging", length = 150)
    private String packaging;

    /**
     * Statut de prescription (prescription obligatoire, OTC, etc.)
     */
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "prescription_status", nullable = false)
    private PrescriptionStatus prescriptionStatus;

    /**
     * Description libre du médicament
     */
    @Column(name = "description")
    private String description;

    /**
     * Date d’expiration du médicament
     */
    @Column(name = "expiry_date")
    private Instant expiryDate;

    /**
     * Code-barres du médicament (EAN ou autre)
     */
    @Size(max = 50)
    @Column(name = "barcode", length = 50)
    private String barcode;

    /**
     * Conditions de stockage (ex : à conserver entre 2°C et 8°C)
     */
    @Size(max = 100)
    @Column(name = "storage_condition", length = 100)
    private String storageCondition;

    /**
     * Prix unitaire du médicament
     */
    @Column(name = "unit_price", precision = 21, scale = 2)
    private BigDecimal unitPrice;

    /**
     * URL ou chemin de l'image du médicament
     */
    @Column(name = "image")
    private String image;

    /**
     * Liste détaillée des principes actifs et excipients
     */
    @Column(name = "composition")
    private String composition;

    /**
     * Contre-indications du médicament
     */
    @Column(name = "contraindications")
    private String contraindications;

    /**
     * Effets secondaires connus
     */
    @Column(name = "side_effects")
    private String sideEffects;

    /**
     * Statut actif ou inactif (permet d’archiver un médicament)
     */
    @NotNull
    @Column(name = "active", nullable = false)
    private Boolean active;

    /**
     * Ce médicament est-il générique ?
     */
    @Column(name = "is_generic")
    private Boolean isGeneric;

    /**
     * Niveau de risque lié au médicament
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "risk_level")
    private RiskLevel riskLevel;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Medication id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Medication name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInternationalName() {
        return this.internationalName;
    }

    public Medication internationalName(String internationalName) {
        this.setInternationalName(internationalName);
        return this;
    }

    public void setInternationalName(String internationalName) {
        this.internationalName = internationalName;
    }

    public String getCodeAtc() {
        return this.codeAtc;
    }

    public Medication codeAtc(String codeAtc) {
        this.setCodeAtc(codeAtc);
        return this;
    }

    public void setCodeAtc(String codeAtc) {
        this.codeAtc = codeAtc;
    }

    public String getFormulation() {
        return this.formulation;
    }

    public Medication formulation(String formulation) {
        this.setFormulation(formulation);
        return this;
    }

    public void setFormulation(String formulation) {
        this.formulation = formulation;
    }

    public String getStrength() {
        return this.strength;
    }

    public Medication strength(String strength) {
        this.setStrength(strength);
        return this;
    }

    public void setStrength(String strength) {
        this.strength = strength;
    }

    public RouteAdmin getRouteOfAdministration() {
        return this.routeOfAdministration;
    }

    public Medication routeOfAdministration(RouteAdmin routeOfAdministration) {
        this.setRouteOfAdministration(routeOfAdministration);
        return this;
    }

    public void setRouteOfAdministration(RouteAdmin routeOfAdministration) {
        this.routeOfAdministration = routeOfAdministration;
    }

    public String getManufacturer() {
        return this.manufacturer;
    }

    public Medication manufacturer(String manufacturer) {
        this.setManufacturer(manufacturer);
        return this;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getMarketingAuthorizationNumber() {
        return this.marketingAuthorizationNumber;
    }

    public Medication marketingAuthorizationNumber(String marketingAuthorizationNumber) {
        this.setMarketingAuthorizationNumber(marketingAuthorizationNumber);
        return this;
    }

    public void setMarketingAuthorizationNumber(String marketingAuthorizationNumber) {
        this.marketingAuthorizationNumber = marketingAuthorizationNumber;
    }

    public Instant getMarketingAuthorizationDate() {
        return this.marketingAuthorizationDate;
    }

    public Medication marketingAuthorizationDate(Instant marketingAuthorizationDate) {
        this.setMarketingAuthorizationDate(marketingAuthorizationDate);
        return this;
    }

    public void setMarketingAuthorizationDate(Instant marketingAuthorizationDate) {
        this.marketingAuthorizationDate = marketingAuthorizationDate;
    }

    public String getPackaging() {
        return this.packaging;
    }

    public Medication packaging(String packaging) {
        this.setPackaging(packaging);
        return this;
    }

    public void setPackaging(String packaging) {
        this.packaging = packaging;
    }

    public PrescriptionStatus getPrescriptionStatus() {
        return this.prescriptionStatus;
    }

    public Medication prescriptionStatus(PrescriptionStatus prescriptionStatus) {
        this.setPrescriptionStatus(prescriptionStatus);
        return this;
    }

    public void setPrescriptionStatus(PrescriptionStatus prescriptionStatus) {
        this.prescriptionStatus = prescriptionStatus;
    }

    public String getDescription() {
        return this.description;
    }

    public Medication description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Instant getExpiryDate() {
        return this.expiryDate;
    }

    public Medication expiryDate(Instant expiryDate) {
        this.setExpiryDate(expiryDate);
        return this;
    }

    public void setExpiryDate(Instant expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getBarcode() {
        return this.barcode;
    }

    public Medication barcode(String barcode) {
        this.setBarcode(barcode);
        return this;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getStorageCondition() {
        return this.storageCondition;
    }

    public Medication storageCondition(String storageCondition) {
        this.setStorageCondition(storageCondition);
        return this;
    }

    public void setStorageCondition(String storageCondition) {
        this.storageCondition = storageCondition;
    }

    public BigDecimal getUnitPrice() {
        return this.unitPrice;
    }

    public Medication unitPrice(BigDecimal unitPrice) {
        this.setUnitPrice(unitPrice);
        return this;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public String getImage() {
        return this.image;
    }

    public Medication image(String image) {
        this.setImage(image);
        return this;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getComposition() {
        return this.composition;
    }

    public Medication composition(String composition) {
        this.setComposition(composition);
        return this;
    }

    public void setComposition(String composition) {
        this.composition = composition;
    }

    public String getContraindications() {
        return this.contraindications;
    }

    public Medication contraindications(String contraindications) {
        this.setContraindications(contraindications);
        return this;
    }

    public void setContraindications(String contraindications) {
        this.contraindications = contraindications;
    }

    public String getSideEffects() {
        return this.sideEffects;
    }

    public Medication sideEffects(String sideEffects) {
        this.setSideEffects(sideEffects);
        return this;
    }

    public void setSideEffects(String sideEffects) {
        this.sideEffects = sideEffects;
    }

    public Boolean getActive() {
        return this.active;
    }

    public Medication active(Boolean active) {
        this.setActive(active);
        return this;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Boolean getIsGeneric() {
        return this.isGeneric;
    }

    public Medication isGeneric(Boolean isGeneric) {
        this.setIsGeneric(isGeneric);
        return this;
    }

    public void setIsGeneric(Boolean isGeneric) {
        this.isGeneric = isGeneric;
    }

    public RiskLevel getRiskLevel() {
        return this.riskLevel;
    }

    public Medication riskLevel(RiskLevel riskLevel) {
        this.setRiskLevel(riskLevel);
        return this;
    }

    public void setRiskLevel(RiskLevel riskLevel) {
        this.riskLevel = riskLevel;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Medication)) {
            return false;
        }
        return getId() != null && getId().equals(((Medication) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Medication{" +
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
            "}";
    }
}
