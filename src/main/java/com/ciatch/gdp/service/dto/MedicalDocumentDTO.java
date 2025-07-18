package com.ciatch.gdp.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link com.ciatch.gdp.domain.MedicalDocument} entity.
 */
@Schema(
    description = "Catalogue des documents externes (PDFs, images).\nLe fichier est stocké sur le serveur, seul le chemin est en base.\n@encryptedFields description"
)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class MedicalDocumentDTO implements Serializable {

    private Long id;

    @NotNull
    private String documentName;

    private LocalDate documentDate;

    @NotNull
    private String filePath;

    @NotNull
    private String fileType;

    private String desc;

    @NotNull
    @Schema(description = "Un document appartient à un patient.")
    private PatientDTO patient;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDocumentName() {
        return documentName;
    }

    public void setDocumentName(String documentName) {
        this.documentName = documentName;
    }

    public LocalDate getDocumentDate() {
        return documentDate;
    }

    public void setDocumentDate(LocalDate documentDate) {
        this.documentDate = documentDate;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public PatientDTO getPatient() {
        return patient;
    }

    public void setPatient(PatientDTO patient) {
        this.patient = patient;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MedicalDocumentDTO)) {
            return false;
        }

        MedicalDocumentDTO medicalDocumentDTO = (MedicalDocumentDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, medicalDocumentDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MedicalDocumentDTO{" +
            "id=" + getId() +
            ", documentName='" + getDocumentName() + "'" +
            ", documentDate='" + getDocumentDate() + "'" +
            ", filePath='" + getFilePath() + "'" +
            ", fileType='" + getFileType() + "'" +
            ", desc='" + getDesc() + "'" +
            ", patient=" + getPatient() +
            "}";
    }
}
