package com.ciatch.gdp.service;

import com.ciatch.gdp.web.rest.errors.BadRequestAlertException;
import com.ciatch.gdp.web.rest.errors.ErrorConstants;

public class InsuranceIdAlreadyUsedException extends BadRequestAlertException {

    private static final long serialVersionUID = 1L;

    public InsuranceIdAlreadyUsedException() {
        super(ErrorConstants.DEFAULT_TYPE, "Insurance ID is already in use!", "patient", "insuranceidexists");
    }
}
