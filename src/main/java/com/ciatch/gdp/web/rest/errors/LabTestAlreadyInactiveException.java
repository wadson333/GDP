package com.ciatch.gdp.web.rest.errors;

public class LabTestAlreadyInactiveException extends BadRequestAlertException {

    private static final long serialVersionUID = 1L;

    public LabTestAlreadyInactiveException() {
        super(ErrorConstants.LAB_TEST_ALREADY_INACTIVE, "lab-test-catalog.validation.alreadyInactive", "labTestCatalog", "alreadyinactive");
    }
}
