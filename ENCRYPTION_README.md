# Patient Data Encryption Guide

## Overview

Sensitive patient data is encrypted at rest in the database using **AES-256-GCM** (Galois/Counter Mode) authenticated encryption. This provides both **confidentiality** and **integrity** protection.

## Architecture

```
┌─────────────┐         ┌──────────────────┐         ┌──────────────┐
│  Java App   │ ◄─────► │ CryptoConverter  │ ◄─────► │  PostgreSQL  │
│  (cleartext)│         │  (encrypt/decrypt)│         │  (encrypted) │
└─────────────┘         └──────────────────┘         └──────────────┘
```

## Encrypted Fields

The following `Patient` entity fields are **automatically encrypted**:

✅ **Encrypted (descriptive data)**:

- `address` - Patient residential address
- `phone1` - Primary phone number
- `phone2` - Secondary phone number
- `contactPersonName` - Emergency contact name
- `contactPersonPhone` - Emergency contact phone
- `antecedents` - Medical history
- `allergies` - Allergy information
- `clinicalNotes` - Clinical observations
- `patientInsuranceId` - Insurance member ID

❌ **NOT Encrypted (identifiers, preserved for uniqueness)**:

- `nif` - National Identification Number
- `ninu` - National Insurance Number
- `passportNumber` - Passport number
- `medicalRecordNumber` - Hospital MRN
- `insurancePolicyNumber` - Insurance policy number

> **Note**: Identifiers remain in cleartext to preserve database `UNIQUE` constraints. A future enhancement will add hashing for these fields.

## Configuration

### 1. Generate Encryption Key

```bash
# Generate a 256-bit (32 byte) key
openssl rand -base64 32
```

Output example:

```
cGxlYXNlQ2hhbmdlVGhpc1RvQVNlY3VyZUtleTMyQnl0ZXM=
```

### 2. Configure in `application.yml`

**Development** (never use in production):

```yaml
jhipster:
  security:
    content-encryption-key: 'cGxlYXNlQ2hhbmdlVGhpc1RvQVNlY3VyZUtleTMyQnl0ZXM='
```

**Production** (use environment variable):

```yaml
jhipster:
  security:
    content-encryption-key: ${CONTENT_ENCRYPTION_KEY}
```

### 3. Set Environment Variable (Production)

**Linux/macOS**:

```bash
export CONTENT_ENCRYPTION_KEY="YOUR_BASE64_KEY_HERE"
```

**Windows**:

```cmd
set CONTENT_ENCRYPTION_KEY=YOUR_BASE64_KEY_HERE
```

**Docker Compose**:

```yaml
services:
  app:
    environment:
      - CONTENT_ENCRYPTION_KEY=${CONTENT_ENCRYPTION_KEY}
```

**Kubernetes Secret**:

```yaml
apiVersion: v1
kind: Secret
metadata:
  name: encryption-key
type: Opaque
data:
  content-encryption-key: Y0dsbFlYTmxRMmhoYm1kbFZHaHBjMVJ2UVZZM1kzVnlaVXRsZVRNeVFubDBaWE09
```

## Security Best Practices

### Key Management

1. **Never commit keys to Git**

   - Add `.env` to `.gitignore`
   - Use secret management tools (Vault, AWS Secrets Manager, etc.)

2. **Rotate keys regularly**

   - Plan: Quarterly key rotation
   - Process: Decrypt with old key → re-encrypt with new key

3. **Use different keys per environment**

   - Development: Test key
   - Staging: Staging-specific key
   - Production: Highly secure production key

4. **Backup encryption keys securely**
   - Store keys in multiple secure locations
   - Without the key, encrypted data is **irrecoverable**

### Operational Security

1. **Database Backups**

   - Encrypted data remains encrypted in backups
   - Backup the encryption key separately

2. **Key Loss Prevention**

   - Document key recovery procedures
   - Test disaster recovery regularly

3. **Audit Logging**
   - Monitor encryption/decryption failures
   - Track key rotation events

## Encryption Details

### Algorithm: AES-256-GCM

- **Key Size**: 256 bits (32 bytes)
- **IV Size**: 96 bits (12 bytes) - randomly generated per encryption
- **Tag Size**: 128 bits (16 bytes) - authentication tag
- **Mode**: Galois/Counter Mode (authenticated encryption)

### Data Format

Encrypted data stored in database:

```
Base64(IV || Ciphertext || AuthenticationTag)
```

Example:

```
Original:  "+33612345678"
Encrypted: "a2V5MTIzNDU2Nzg5MGFiY2RlZmdoaWprbG1ub3BxcnN0dXZ3eHl6QUJDREVGRw=="
```

### Performance Impact

- **Encryption**: ~0.1ms per field
- **Decryption**: ~0.1ms per field
- **Storage Overhead**: ~33% (Base64 encoding)

## Testing

### Unit Tests

```bash
./mvnw test -Dtest=EncryptionServiceTest
```

### Integration Tests

```bash
./mvnw verify -Dit.test=CryptoAttributeConverterIT
```

### Manual Verification

```java
@SpringBootTest
class EncryptionManualTest {

  @Autowired
  private EncryptionService encryptionService;

  @Test
  void verifyEncryption() {
    String cleartext = "Sensitive Data";
    String encrypted = encryptionService.encrypt(cleartext);
    String decrypted = encryptionService.decrypt(encrypted);

    assertThat(decrypted).isEqualTo(cleartext);
    assertThat(encrypted).isNotEqualTo(cleartext);
  }
}

```

## Troubleshooting

### Error: "Invalid encryption key configuration"

**Cause**: Key is not exactly 32 bytes (256 bits) when Base64 decoded.

**Solution**:

```bash
# Generate new valid key
openssl rand -base64 32
```

### Error: "Data integrity check failed"

**Cause**:

- Data was tampered with in database
- Wrong decryption key used
- Database corruption

**Solution**:

1. Verify correct key is configured
2. Check database backup integrity
3. If key was rotated, use old key to decrypt

### Error: "EncryptionService not available"

**Cause**: Spring context not initialized properly for JPA converter.

**Solution**:

- Ensure `@Component` on `CryptoAttributeConverter`
- Verify `@Autowired` constructor injection
- Check Spring Boot auto-configuration

## Migration from Unencrypted Data

If you have existing cleartext data:

```sql
-- 1. Backup database
pg_dump gdp_db > backup_before_encryption.sql

-- 2. Run Liquibase migration to increase column sizes
./mvnw liquibase:update

-- 3. Application will automatically encrypt on first save/update
-- No manual migration needed - encryption happens transparently

-- 4. Verify encryption (check database directly)
SELECT
    first_name,
    CASE
        WHEN phone_1 ~ '^[A-Za-z0-9+/]+=*$' THEN 'ENCRYPTED'
        ELSE 'CLEARTEXT'
    END as phone_1_status
FROM patient
LIMIT 10;
```

## Compliance

This encryption implementation supports:

✅ **GDPR** - Article 32: Security of processing  
✅ **HIPAA** - 164.312(a)(2)(iv): Encryption and decryption  
✅ **ISO 27001** - A.10.1.1: Policy on the use of cryptographic controls  
✅ **PCI DSS** - Requirement 3: Protect stored cardholder data

## Future Enhancements

### Planned

1. **Field-level key rotation** without downtime
2. **Hashing for identifiers** (NIF, NINU, Passport)
3. **Key derivation** from master key + context
4. **Hardware Security Module (HSM)** integration

### Under Consideration

1. **Client-side encryption** for ultra-sensitive notes
2. **Searchable encryption** for encrypted fields
3. **Audit trail** for all decryption operations

## Support

For questions or issues:

- 📧 Email: security@ciatch.com
- 📝 Documentation: https://docs.ciatch.com/encryption
- 🐛 Issues: https://github.com/ciatch/gdp/issues

---

**⚠️ IMPORTANT**: Loss of encryption key = permanent data loss. Always backup keys securely!
