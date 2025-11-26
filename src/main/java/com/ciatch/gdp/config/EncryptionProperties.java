package com.ciatch.gdp.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Configuration properties for data encryption.
 * <p>
 * Properties are configured in the {@code application.yml} file.
 * </p>
 */
@Component
@ConfigurationProperties(prefix = "application.encryption", ignoreUnknownFields = false)
public class EncryptionProperties {

    private String algorithm = "AES/GCM/NoPadding";
    private int keySize = 256;
    private int ivSize = 12;
    private int tagSize = 128;

    public String getAlgorithm() {
        return algorithm;
    }

    public void setAlgorithm(String algorithm) {
        this.algorithm = algorithm;
    }

    public int getKeySize() {
        return keySize;
    }

    public void setKeySize(int keySize) {
        this.keySize = keySize;
    }

    public int getIvSize() {
        return ivSize;
    }

    public void setIvSize(int ivSize) {
        this.ivSize = ivSize;
    }

    public int getTagSize() {
        return tagSize;
    }

    public void setTagSize(int tagSize) {
        this.tagSize = tagSize;
    }
}
