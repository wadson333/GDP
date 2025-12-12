package com.ciatch.gdp.web.rest.errors;

public class NifAlreadyUsedException extends BadRequestAlertException {

    private static final long serialVersionUID = 1L;

    public NifAlreadyUsedException() {
        super("NIF is already in use!", "doctorProfile", "nifexists");
    }
}
