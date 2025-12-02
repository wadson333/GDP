package com.ciatch.gdp.security;

import com.ciatch.gdp.config.ApplicationProperties; // Import the main config
import java.nio.ByteBuffer;
import java.security.SecureRandom;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class EncryptionService {

    private static final Logger LOG = LoggerFactory.getLogger(EncryptionService.class);
    private static final String ENCRYPTION_ALGORITHM = "AES";

    private final SecretKey secretKey;
    private final ApplicationProperties.Encryption encryptionConfig; // Use the nested config
    private final SecureRandom secureRandom;

    // Constructor Injection using ApplicationProperties
    public EncryptionService(ApplicationProperties applicationProperties) {
        this.encryptionConfig = applicationProperties.getEncryption();
        this.secureRandom = new SecureRandom();

        // Get key from ApplicationProperties (No more @Value)
        String base64Key = applicationProperties.getSecurity().getContentEncryptionKey();

        try {
            byte[] decodedKey = Base64.getDecoder().decode(base64Key);
            if (decodedKey.length != encryptionConfig.getKeySize() / 8) {
                throw new IllegalArgumentException(
                    String.format(
                        "Invalid key size. Expected %d bytes but got %d bytes.",
                        encryptionConfig.getKeySize() / 8,
                        decodedKey.length
                    )
                );
            }
            this.secretKey = new SecretKeySpec(decodedKey, ENCRYPTION_ALGORITHM);
            LOG.info("Encryption service initialized successfully with AES-{} GCM", encryptionConfig.getKeySize());
        } catch (Exception e) {
            LOG.error("Failed to initialize encryption key: {}", e.getMessage());
            throw new IllegalStateException("Invalid encryption key configuration", e);
        }
    }

    public String encrypt(String plaintext) {
        if (plaintext == null || plaintext.isEmpty()) return null;

        try {
            byte[] iv = new byte[encryptionConfig.getIvSize()];
            secureRandom.nextBytes(iv);

            Cipher cipher = Cipher.getInstance(encryptionConfig.getAlgorithm());
            GCMParameterSpec gcmSpec = new GCMParameterSpec(encryptionConfig.getTagSize(), iv);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, gcmSpec);

            byte[] ciphertext = cipher.doFinal(plaintext.getBytes(java.nio.charset.StandardCharsets.UTF_8));
            ByteBuffer byteBuffer = ByteBuffer.allocate(iv.length + ciphertext.length);
            byteBuffer.put(iv);
            byteBuffer.put(ciphertext);

            return Base64.getEncoder().encodeToString(byteBuffer.array());
        } catch (Exception e) {
            LOG.error("Encryption failed", e);
            throw new RuntimeException("Failed to encrypt data", e);
        }
    }

    public String decrypt(String base64Ciphertext) {
        if (base64Ciphertext == null || base64Ciphertext.isEmpty()) return null;

        try {
            byte[] encryptedData = Base64.getDecoder().decode(base64Ciphertext);
            ByteBuffer byteBuffer = ByteBuffer.wrap(encryptedData);
            byte[] iv = new byte[encryptionConfig.getIvSize()];
            byteBuffer.get(iv);
            byte[] ciphertext = new byte[byteBuffer.remaining()];
            byteBuffer.get(ciphertext);

            Cipher cipher = Cipher.getInstance(encryptionConfig.getAlgorithm());
            GCMParameterSpec gcmSpec = new GCMParameterSpec(encryptionConfig.getTagSize(), iv);
            cipher.init(Cipher.DECRYPT_MODE, secretKey, gcmSpec);

            return new String(cipher.doFinal(ciphertext), java.nio.charset.StandardCharsets.UTF_8);
        } catch (Exception e) {
            LOG.error("Decryption failed", e);
            throw new RuntimeException("Failed to decrypt data", e);
        }
    }
}
