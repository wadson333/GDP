package com.ciatch.gdp.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link com.ciatch.gdp.domain.DoctorProfile} entity.
 */
@Schema(description = "Profil professionnel détaillé pour chaque médecin.\n@encryptedFields licenseNumber")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DoctorProfileDTO implements Serializable {

    private Long id;

    @NotNull
    private String specialty;

    @NotNull
    private String licenseNumber;

    private String university;

    @NotNull
    private LocalDate startDateOfPractice;

    @NotNull
    @Schema(description = "Un profil de médecin est lié à un et un seul compte utilisateur.")
    private UserDTO user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSpecialty() {
        return specialty;
    }

    public void setSpecialty(String specialty) {
        this.specialty = specialty;
    }

    public String getLicenseNumber() {
        return licenseNumber;
    }

    public void setLicenseNumber(String licenseNumber) {
        this.licenseNumber = licenseNumber;
    }

    public String getUniversity() {
        return university;
    }

    public void setUniversity(String university) {
        this.university = university;
    }

    public LocalDate getStartDateOfPractice() {
        return startDateOfPractice;
    }

    public void setStartDateOfPractice(LocalDate startDateOfPractice) {
        this.startDateOfPractice = startDateOfPractice;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DoctorProfileDTO)) {
            return false;
        }

        DoctorProfileDTO doctorProfileDTO = (DoctorProfileDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, doctorProfileDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DoctorProfileDTO{" +
            "id=" + getId() +
            ", specialty='" + getSpecialty() + "'" +
            ", licenseNumber='" + getLicenseNumber() + "'" +
            ", university='" + getUniversity() + "'" +
            ", startDateOfPractice='" + getStartDateOfPractice() + "'" +
            ", user=" + getUser() +
            "}";
    }
}
