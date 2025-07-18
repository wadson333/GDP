package com.ciatch.gdp.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.ciatch.gdp.domain.UserConfiguration} entity.
 */
@Schema(description = "Préférences et configurations de sécurité de l'utilisateur.\n@encryptedFields twoFactorSecret")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class UserConfigurationDTO implements Serializable {

    private Long id;

    @NotNull
    private Boolean twoFactorEnabled;

    private String twoFactorSecret;

    @NotNull
    private Boolean receiveEmailNotifs;

    @NotNull
    @Schema(description = "Chaque utilisateur a une et une seule page de configuration.")
    private UserDTO user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getTwoFactorEnabled() {
        return twoFactorEnabled;
    }

    public void setTwoFactorEnabled(Boolean twoFactorEnabled) {
        this.twoFactorEnabled = twoFactorEnabled;
    }

    public String getTwoFactorSecret() {
        return twoFactorSecret;
    }

    public void setTwoFactorSecret(String twoFactorSecret) {
        this.twoFactorSecret = twoFactorSecret;
    }

    public Boolean getReceiveEmailNotifs() {
        return receiveEmailNotifs;
    }

    public void setReceiveEmailNotifs(Boolean receiveEmailNotifs) {
        this.receiveEmailNotifs = receiveEmailNotifs;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UserConfigurationDTO)) {
            return false;
        }

        UserConfigurationDTO userConfigurationDTO = (UserConfigurationDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, userConfigurationDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UserConfigurationDTO{" +
            "id=" + getId() +
            ", twoFactorEnabled='" + getTwoFactorEnabled() + "'" +
            ", twoFactorSecret='" + getTwoFactorSecret() + "'" +
            ", receiveEmailNotifs='" + getReceiveEmailNotifs() + "'" +
            ", user=" + getUser() +
            "}";
    }
}
