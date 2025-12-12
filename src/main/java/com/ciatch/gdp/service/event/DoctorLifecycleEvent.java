package com.ciatch.gdp.service.event;

import com.ciatch.gdp.domain.DoctorProfile;
import com.ciatch.gdp.domain.User;
import com.ciatch.gdp.domain.enumeration.NotificationType;

/**
 * Event published when a doctor profile undergoes a lifecycle change.
 * Used for asynchronous notification handling.
 */
public class DoctorLifecycleEvent {

    private final User doctorUser;
    private final DoctorProfile doctorProfile;
    private final NotificationType type;
    private final String reason;

    public DoctorLifecycleEvent(User doctorUser, DoctorProfile doctorProfile, NotificationType type, String reason) {
        this.doctorUser = doctorUser;
        this.doctorProfile = doctorProfile;
        this.type = type;
        this.reason = reason;
    }

    public User getDoctorUser() {
        return doctorUser;
    }

    public DoctorProfile getDoctorProfile() {
        return doctorProfile;
    }

    public NotificationType getType() {
        return type;
    }

    public String getReason() {
        return reason;
    }

    @Override
    public String toString() {
        return (
            "DoctorLifecycleEvent{" +
            "doctorUser=" +
            (doctorUser != null ? doctorUser.getLogin() : "null") +
            ", doctorProfileId=" +
            (doctorProfile != null ? doctorProfile.getId() : "null") +
            ", type=" +
            type +
            ", reason='" +
            reason +
            '\'' +
            '}'
        );
    }
}
