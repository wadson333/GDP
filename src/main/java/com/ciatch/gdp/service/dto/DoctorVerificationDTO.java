package com.ciatch.gdp.service.dto;

import jakarta.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * A DTO for doctor verification (approval/rejection).
 */
public class DoctorVerificationDTO implements Serializable {

    @NotNull
    private Boolean approved;

    private String comment;

    public DoctorVerificationDTO() {}

    public DoctorVerificationDTO(Boolean approved, String comment) {
        this.approved = approved;
        this.comment = comment;
    }

    public Boolean getApproved() {
        return approved;
    }

    public void setApproved(Boolean approved) {
        this.approved = approved;
    }

    public Boolean isApproved() {
        return approved;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @Override
    public String toString() {
        return "DoctorVerificationDTO{" + "approved=" + approved + ", comment='" + comment + '\'' + '}';
    }
}
