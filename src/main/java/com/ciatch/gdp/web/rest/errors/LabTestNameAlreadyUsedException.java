package com.ciatch.gdp.web.rest.errors;

public class LabTestNameAlreadyUsedException extends BadRequestAlertException {

    private static final long serialVersionUID = 1L;

    public LabTestNameAlreadyUsedException() {
        super(ErrorConstants.LAB_TEST_NAME_ALREADY_USED_TYPE, "Lab test name already used!", "labTestCatalog", "nameexists");
    }
}
