package com.ciatch.gdp.web.rest.errors;

public class MedicalLicenseAlreadyUsedException extends BadRequestAlertException {

    private static final long serialVersionUID = 1L;

    public MedicalLicenseAlreadyUsedException() {
        super("Medical License Number is already in use!", "doctorProfile", "medicallicenseexists");
    }
}
