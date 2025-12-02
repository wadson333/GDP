package com.ciatch.gdp.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "application", ignoreUnknownFields = false)
public class ApplicationProperties {

    private final Liquibase liquibase = new Liquibase();
    private final Security security = new Security();
    private final Encryption encryption = new Encryption();

    public Liquibase getLiquibase() {
        return liquibase;
    }

    public Security getSecurity() {
        return security;
    }

    public Encryption getEncryption() {
        return encryption;
    }

    // --- Nested Classes ---

    public static class Security {

        private String contentEncryptionKey;

        public String getContentEncryptionKey() {
            return contentEncryptionKey;
        }

        public void setContentEncryptionKey(String contentEncryptionKey) {
            this.contentEncryptionKey = contentEncryptionKey;
        }
    }

    public static class Encryption {

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

    public static class Liquibase {

        private Boolean asyncStart;

        public Boolean getAsyncStart() {
            return asyncStart;
        }

        public void setAsyncStart(Boolean asyncStart) {
            this.asyncStart = asyncStart;
        }
    }
}
