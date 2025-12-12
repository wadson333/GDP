package com.ciatch.gdp.service.dto;

import com.ciatch.gdp.domain.enumeration.MedicalSpecialty;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.UUID;

/**
 * A public DTO for the {@link com.ciatch.gdp.domain.DoctorProfile} entity.
 * Contains only safe, non-sensitive information for public viewing.
 */
public class DoctorPublicDTO implements Serializable {

    private UUID uid;

    private String firstName;

    private String lastName;

    private MedicalSpecialty primarySpecialty;

    private String otherSpecialties;

    private String bio;

    private String spokenLanguages;

    private String university;

    private BigDecimal consultationFee;

    private BigDecimal teleconsultationFee;

    private String officeAddress;

    private String officePhone;

    private Double averageRating;

    private Integer reviewCount;

    private String photoUrl;

    // Getters and Setters

    public UUID getUid() {
        return uid;
    }

    public void setUid(UUID uid) {
        this.uid = uid;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public MedicalSpecialty getPrimarySpecialty() {
        return primarySpecialty;
    }

    public void setPrimarySpecialty(MedicalSpecialty primarySpecialty) {
        this.primarySpecialty = primarySpecialty;
    }

    public String getOtherSpecialties() {
        return otherSpecialties;
    }

    public void setOtherSpecialties(String otherSpecialties) {
        this.otherSpecialties = otherSpecialties;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getSpokenLanguages() {
        return spokenLanguages;
    }

    public void setSpokenLanguages(String spokenLanguages) {
        this.spokenLanguages = spokenLanguages;
    }

    public String getUniversity() {
        return university;
    }

    public void setUniversity(String university) {
        this.university = university;
    }

    public BigDecimal getConsultationFee() {
        return consultationFee;
    }

    public void setConsultationFee(BigDecimal consultationFee) {
        this.consultationFee = consultationFee;
    }

    public BigDecimal getTeleconsultationFee() {
        return teleconsultationFee;
    }

    public void setTeleconsultationFee(BigDecimal teleconsultationFee) {
        this.teleconsultationFee = teleconsultationFee;
    }

    public String getOfficeAddress() {
        return officeAddress;
    }

    public void setOfficeAddress(String officeAddress) {
        this.officeAddress = officeAddress;
    }

    public String getOfficePhone() {
        return officePhone;
    }

    public void setOfficePhone(String officePhone) {
        this.officePhone = officePhone;
    }

    public Double getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(Double averageRating) {
        this.averageRating = averageRating;
    }

    public Integer getReviewCount() {
        return reviewCount;
    }

    public void setReviewCount(Integer reviewCount) {
        this.reviewCount = reviewCount;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DoctorPublicDTO)) {
            return false;
        }

        DoctorPublicDTO that = (DoctorPublicDTO) o;
        return uid != null && uid.equals(that.uid);
    }

    @Override
    public int hashCode() {
        return uid != null ? uid.hashCode() : 0;
    }

    @Override
    public String toString() {
        return (
            "DoctorPublicDTO{" +
            "uid=" +
            uid +
            ", firstName='" +
            firstName +
            '\'' +
            ", lastName='" +
            lastName +
            '\'' +
            ", primarySpecialty=" +
            primarySpecialty +
            ", averageRating=" +
            averageRating +
            ", reviewCount=" +
            reviewCount +
            '}'
        );
    }
}
