package com.ciatch.gdp.web.rest.errors;

public class NinuAlreadyUsedException extends BadRequestAlertException {

    private static final long serialVersionUID = 1L;

    public NinuAlreadyUsedException() {
        super("NINU is already in use!", "doctorProfile", "ninuexists");
    }
}
