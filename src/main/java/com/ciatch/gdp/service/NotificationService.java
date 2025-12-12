package com.ciatch.gdp.service;

import com.ciatch.gdp.domain.Notification;
import com.ciatch.gdp.domain.User;
import com.ciatch.gdp.domain.enumeration.NotificationType;
import com.ciatch.gdp.repository.NotificationRepository;
import com.ciatch.gdp.service.dto.NotificationDTO;
import com.ciatch.gdp.service.mapper.NotificationMapper;
import java.time.ZonedDateTime;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.ciatch.gdp.domain.Notification}.
 */
@Service
@Transactional
public class NotificationService {

    private static final Logger LOG = LoggerFactory.getLogger(NotificationService.class);

    private final NotificationRepository notificationRepository;

    private final NotificationMapper notificationMapper;

    public NotificationService(NotificationRepository notificationRepository, NotificationMapper notificationMapper) {
        this.notificationRepository = notificationRepository;
        this.notificationMapper = notificationMapper;
    }

    /**
     * Save a notification.
     *
     * @param notificationDTO the entity to save.
     * @return the persisted entity.
     */
    public NotificationDTO save(NotificationDTO notificationDTO) {
        LOG.debug("Request to save Notification : {}", notificationDTO);
        Notification notification = notificationMapper.toEntity(notificationDTO);
        notification = notificationRepository.save(notification);
        return notificationMapper.toDto(notification);
    }

    /**
     * Update a notification.
     *
     * @param notificationDTO the entity to save.
     * @return the persisted entity.
     */
    public NotificationDTO update(NotificationDTO notificationDTO) {
        LOG.debug("Request to update Notification : {}", notificationDTO);
        Notification notification = notificationMapper.toEntity(notificationDTO);
        notification = notificationRepository.save(notification);
        return notificationMapper.toDto(notification);
    }

    /**
     * Partially update a notification.
     *
     * @param notificationDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<NotificationDTO> partialUpdate(NotificationDTO notificationDTO) {
        LOG.debug("Request to partially update Notification : {}", notificationDTO);

        return notificationRepository
            .findById(notificationDTO.getId())
            .map(existingNotification -> {
                notificationMapper.partialUpdate(existingNotification, notificationDTO);

                return existingNotification;
            })
            .map(notificationRepository::save)
            .map(notificationMapper::toDto);
    }

    /**
     * Get all the notifications.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<NotificationDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all Notifications");
        return notificationRepository.findAll(pageable).map(notificationMapper::toDto);
    }

    /**
     * Get all the notifications with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<NotificationDTO> findAllWithEagerRelationships(Pageable pageable) {
        return notificationRepository.findAllWithEagerRelationships(pageable).map(notificationMapper::toDto);
    }

    /**
     * Get one notification by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<NotificationDTO> findOne(Long id) {
        LOG.debug("Request to get Notification : {}", id);
        return notificationRepository.findOneWithEagerRelationships(id).map(notificationMapper::toDto);
    }

    /**
     * Delete the notification by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Notification : {}", id);
        notificationRepository.deleteById(id);
    }

    /**
     * Create and persist a notification for a user.
     *
     * @param targetUser the user who will receive the notification
     * @param type the type of notification
     * @param message the notification message
     * @param relatedEntityId optional ID of related entity (e.g., DoctorProfile ID)
     * @return the persisted notification
     */
    @Transactional
    public Notification create(User targetUser, NotificationType type, String message, Long relatedEntityId) {
        LOG.debug("Creating notification for user '{}' of type '{}'", targetUser.getLogin(), type);

        Notification notification = new Notification();
        notification.setTargetUser(targetUser);
        notification.setNotificationType(type);
        notification.setMessage(message);
        notification.setRelatedEntityId(relatedEntityId);
        notification.setIsRead(false);
        notification.setCreationDate(ZonedDateTime.now());

        notification = notificationRepository.save(notification);
        LOG.debug("Created notification with ID: {}", notification.getId());

        return notification;
    }

    /**
     * Create and persist a notification for a user without related entity.
     *
     * @param targetUser the user who will receive the notification
     * @param type the type of notification
     * @param message the notification message
     * @return the persisted notification
     */
    @Transactional
    public Notification create(User targetUser, NotificationType type, String message) {
        return create(targetUser, type, message, null);
    }
}
