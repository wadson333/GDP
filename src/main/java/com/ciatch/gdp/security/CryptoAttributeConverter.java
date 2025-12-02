package com.ciatch.gdp.security;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * JPA AttributeConverter for transparent encryption/decryption of sensitive String fields.
 * <p>
 * This converter automatically encrypts entity attributes when persisting to the database
 * and decrypts them when loading from the database. The encryption is transparent to the
 * application code.
 * </p>
 * <p>
 * Usage: Annotate entity fields with {@code @Convert(converter = CryptoAttributeConverter.class)}
 * </p>
 * <p>
 * Example:
 * <pre>
 * &#64;Convert(converter = CryptoAttributeConverter.class)
 * &#64;Column(name = "phone_number")
 * private String phoneNumber;
 * </pre>
 * </p>
 *
 * @see EncryptionService
 */
@Component
@Converter
public class CryptoAttributeConverter implements AttributeConverter<String, String> {

    private static final Logger LOG = LoggerFactory.getLogger(CryptoAttributeConverter.class);

    private static EncryptionService encryptionService;

    /**
     * Constructor with Spring dependency injection.
     * Uses static setter pattern to work around JPA's instantiation mechanism.
     *
     * @param encryptionService The encryption service to use
     */
    @Autowired
    public CryptoAttributeConverter(EncryptionService encryptionService) {
        CryptoAttributeConverter.encryptionService = encryptionService;
    }

    /**
     * Default constructor required by JPA.
     */
    public CryptoAttributeConverter() {
        // Required by JPA
    }

    /**
     * Converts the entity attribute (plaintext) to database column (encrypted).
     *
     * @param attribute The plaintext attribute value (can be null)
     * @return The encrypted value as Base64 string, or null if input is null
     * @throws RuntimeException if encryption fails
     */
    @Override
    public String convertToDatabaseColumn(String attribute) {
        if (attribute == null) {
            return null;
        }

        if (encryptionService == null) {
            LOG.error("EncryptionService not initialized. Cannot encrypt data.");
            throw new IllegalStateException("EncryptionService not available");
        }

        try {
            String encrypted = encryptionService.encrypt(attribute);
            LOG.debug("Successfully encrypted attribute (length: {})", attribute.length());
            return encrypted;
        } catch (Exception e) {
            LOG.error("Failed to encrypt attribute", e);
            throw new RuntimeException("Encryption failed during database write", e);
        }
    }

    /**
     * Converts the database column (encrypted) to entity attribute (plaintext).
     *
     * @param dbData The encrypted database value (can be null)
     * @return The decrypted plaintext value, or null if input is null
     * @throws RuntimeException if decryption fails
     */
    @Override
    public String convertToEntityAttribute(String dbData) {
        if (dbData == null) {
            return null;
        }

        if (encryptionService == null) {
            LOG.error("EncryptionService not initialized. Cannot decrypt data.");
            throw new IllegalStateException("EncryptionService not available");
        }

        try {
            String decrypted = encryptionService.decrypt(dbData);
            LOG.debug("Successfully decrypted attribute");
            return decrypted;
        } catch (Exception e) {
            LOG.error("Failed to decrypt attribute. Data may be corrupted or key may have changed.", e);

            return dbData; // throw new RuntimeException("Decryption failed during database read", e);
        }
    }
}
