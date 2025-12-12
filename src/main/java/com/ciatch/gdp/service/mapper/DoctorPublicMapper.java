package com.ciatch.gdp.service.mapper;

import com.ciatch.gdp.domain.DoctorProfile;
import com.ciatch.gdp.service.dto.DoctorPublicDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link DoctorProfile} and its public DTO {@link DoctorPublicDTO}.
 * Maps only safe, non-sensitive fields for public viewing.
 */
@Mapper(componentModel = "spring")
public interface DoctorPublicMapper extends EntityMapper<DoctorPublicDTO, DoctorProfile> {
    @Mapping(target = "photoUrl", source = "user.imageUrl")
    DoctorPublicDTO toDto(DoctorProfile doctorProfile);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "codeClinic", ignore = true)
    @Mapping(target = "medicalLicenseNumber", ignore = true)
    @Mapping(target = "birthDate", ignore = true)
    @Mapping(target = "gender", ignore = true)
    @Mapping(target = "bloodType", ignore = true)
    @Mapping(target = "graduationYear", ignore = true)
    @Mapping(target = "startDateOfPractice", ignore = true)
    @Mapping(target = "consultationDurationMinutes", ignore = true)
    @Mapping(target = "acceptingNewPatients", ignore = true)
    @Mapping(target = "allowsTeleconsultation", ignore = true)
    @Mapping(target = "websiteUrl", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "isVerified", ignore = true)
    @Mapping(target = "verifiedAt", ignore = true)
    @Mapping(target = "nif", ignore = true)
    @Mapping(target = "ninu", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "lastModifiedBy", ignore = true)
    @Mapping(target = "lastModifiedDate", ignore = true)
    DoctorProfile toEntity(DoctorPublicDTO dto);

    default DoctorProfile fromId(Long id) {
        if (id == null) {
            return null;
        }
        DoctorProfile doctorProfile = new DoctorProfile();
        doctorProfile.setId(id);
        return doctorProfile;
    }
}
