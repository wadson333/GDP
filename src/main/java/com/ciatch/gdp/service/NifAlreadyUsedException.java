package com.ciatch.gdp.service;

import com.ciatch.gdp.web.rest.errors.BadRequestAlertException;
import com.ciatch.gdp.web.rest.errors.ErrorConstants;

public class NifAlreadyUsedException extends BadRequestAlertException {

    private static final long serialVersionUID = 1L;

    public NifAlreadyUsedException() {
        super(ErrorConstants.DEFAULT_TYPE, "NIF is already in use!", "patient", "nifexists");
    }
}
