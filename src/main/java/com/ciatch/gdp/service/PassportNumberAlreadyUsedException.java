package com.ciatch.gdp.service;

import com.ciatch.gdp.web.rest.errors.BadRequestAlertException;
import com.ciatch.gdp.web.rest.errors.ErrorConstants;

public class PassportNumberAlreadyUsedException extends BadRequestAlertException {

    private static final long serialVersionUID = 1L;

    public PassportNumberAlreadyUsedException() {
        super(ErrorConstants.DEFAULT_TYPE, "Passport number is already in use!", "patient", "passportexists");
    }
}
