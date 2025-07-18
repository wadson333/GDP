package com.ciatch.gdp.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * Préférences et configurations de sécurité de l'utilisateur.
 * @encryptedFields twoFactorSecret
 */
@Entity
@Table(name = "user_configuration")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class UserConfiguration implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "two_factor_enabled", nullable = false)
    private Boolean twoFactorEnabled;

    @Column(name = "two_factor_secret")
    private String twoFactorSecret;

    @NotNull
    @Column(name = "receive_email_notifs", nullable = false)
    private Boolean receiveEmailNotifs;

    /**
     * Chaque utilisateur a une et une seule page de configuration.
     */
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @NotNull
    @JoinColumn(unique = true)
    private User user;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public UserConfiguration id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getTwoFactorEnabled() {
        return this.twoFactorEnabled;
    }

    public UserConfiguration twoFactorEnabled(Boolean twoFactorEnabled) {
        this.setTwoFactorEnabled(twoFactorEnabled);
        return this;
    }

    public void setTwoFactorEnabled(Boolean twoFactorEnabled) {
        this.twoFactorEnabled = twoFactorEnabled;
    }

    public String getTwoFactorSecret() {
        return this.twoFactorSecret;
    }

    public UserConfiguration twoFactorSecret(String twoFactorSecret) {
        this.setTwoFactorSecret(twoFactorSecret);
        return this;
    }

    public void setTwoFactorSecret(String twoFactorSecret) {
        this.twoFactorSecret = twoFactorSecret;
    }

    public Boolean getReceiveEmailNotifs() {
        return this.receiveEmailNotifs;
    }

    public UserConfiguration receiveEmailNotifs(Boolean receiveEmailNotifs) {
        this.setReceiveEmailNotifs(receiveEmailNotifs);
        return this;
    }

    public void setReceiveEmailNotifs(Boolean receiveEmailNotifs) {
        this.receiveEmailNotifs = receiveEmailNotifs;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public UserConfiguration user(User user) {
        this.setUser(user);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UserConfiguration)) {
            return false;
        }
        return getId() != null && getId().equals(((UserConfiguration) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UserConfiguration{" +
            "id=" + getId() +
            ", twoFactorEnabled='" + getTwoFactorEnabled() + "'" +
            ", twoFactorSecret='" + getTwoFactorSecret() + "'" +
            ", receiveEmailNotifs='" + getReceiveEmailNotifs() + "'" +
            "}";
    }
}
