package com.ciatch.gdp.service.dto;

import com.ciatch.gdp.domain.enumeration.NotificationType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A DTO for the {@link com.ciatch.gdp.domain.Notification} entity.
 */
@Schema(description = "Notifications \"in-app\" pour les utilisateurs.")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class NotificationDTO implements Serializable {

    private Long id;

    @Lob
    private String message;

    @NotNull
    private Boolean isRead;

    @NotNull
    private NotificationType notificationType;

    @NotNull
    private ZonedDateTime creationDate;

    private Long relatedEntityId;

    @NotNull
    @Schema(description = "Une notification est destinée à un utilisateur.")
    private UserDTO targetUser;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Boolean getIsRead() {
        return isRead;
    }

    public void setIsRead(Boolean isRead) {
        this.isRead = isRead;
    }

    public NotificationType getNotificationType() {
        return notificationType;
    }

    public void setNotificationType(NotificationType notificationType) {
        this.notificationType = notificationType;
    }

    public ZonedDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(ZonedDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public Long getRelatedEntityId() {
        return relatedEntityId;
    }

    public void setRelatedEntityId(Long relatedEntityId) {
        this.relatedEntityId = relatedEntityId;
    }

    public UserDTO getTargetUser() {
        return targetUser;
    }

    public void setTargetUser(UserDTO targetUser) {
        this.targetUser = targetUser;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof NotificationDTO)) {
            return false;
        }

        NotificationDTO notificationDTO = (NotificationDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, notificationDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "NotificationDTO{" +
            "id=" + getId() +
            ", message='" + getMessage() + "'" +
            ", isRead='" + getIsRead() + "'" +
            ", notificationType='" + getNotificationType() + "'" +
            ", creationDate='" + getCreationDate() + "'" +
            ", relatedEntityId=" + getRelatedEntityId() +
            ", targetUser=" + getTargetUser() +
            "}";
    }
}
