package com.ciatch.gdp.service;

import com.ciatch.gdp.web.rest.errors.BadRequestAlertException;
import com.ciatch.gdp.web.rest.errors.ErrorConstants;

public class NinuAlreadyUsedException extends BadRequestAlertException {

    private static final long serialVersionUID = 1L;

    public NinuAlreadyUsedException() {
        super(ErrorConstants.DEFAULT_TYPE, "NINU is already in use!", "patient", "ninuexists");
    }
}
