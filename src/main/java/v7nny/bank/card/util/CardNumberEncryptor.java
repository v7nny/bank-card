package v7nny.bank.card.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import v7nny.bank.card.exception.CardNumberDecryptException;
import v7nny.bank.card.exception.CardNumberEncryptException;
import javax.crypto.*;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Base64;

@Component
public class CardNumberEncryptor {

    private final SecretKey secretKey;

    private final int IV_SOURCE_BUFFER_LENGTH = 12;

    private final int AUTHENTICATION_TAG_LENGTH = 128;

    private static final String ALGORITHM = "AES/GCM/NoPadding";

    private final SecureRandom secureRandom = new SecureRandom();


    public CardNumberEncryptor(@Value("${bank-card.encryption-key}") String secretKey) {
        this.secretKey = createAesKey(secretKey);
    }

    public String encryptCardNumber(String cardNumber) throws CardNumberEncryptException {
        try {
            var cipher = Cipher.getInstance(ALGORITHM);
            var ivBuffer = new byte[IV_SOURCE_BUFFER_LENGTH];
            secureRandom.nextBytes(ivBuffer);
            var parameterSpec = new GCMParameterSpec(AUTHENTICATION_TAG_LENGTH, ivBuffer);

            cipher.init(Cipher.ENCRYPT_MODE, secretKey, parameterSpec);
            byte[] encryptedCardNumber = cipher.doFinal(cardNumber.getBytes());

            var result = new byte[ivBuffer.length + encryptedCardNumber.length];
            System.arraycopy(ivBuffer, 0, result, 0, ivBuffer.length);
            System.arraycopy(encryptedCardNumber, 0, result, ivBuffer.length, encryptedCardNumber.length);

            return Base64.getEncoder().encodeToString(result);
        } catch (Exception e) {
            throw new CardNumberEncryptException(e.getMessage());
        }
    }

    public String decryptCardNumber(String encryptedResultString) throws CardNumberDecryptException {
        try {
            var cipher = Cipher.getInstance(ALGORITHM);
            var encryptedResultBytes = Base64.getDecoder().decode(encryptedResultString);
            var ivBuffer = Arrays.copyOf(encryptedResultBytes, IV_SOURCE_BUFFER_LENGTH);
            var encryptedCardNumber = Arrays.copyOfRange(
                    encryptedResultBytes, IV_SOURCE_BUFFER_LENGTH, encryptedResultBytes.length);
            var parameterSpec = new GCMParameterSpec(AUTHENTICATION_TAG_LENGTH, ivBuffer);

            cipher.init(Cipher.DECRYPT_MODE, secretKey, parameterSpec);
            byte[] decryptedCardNumber = cipher.doFinal(encryptedCardNumber);
            return new String(decryptedCardNumber);
        } catch (Exception e) {
            throw new CardNumberDecryptException(e.getMessage());
        }
    }

    public String maskCardNumber(String cardNumber) {
        return "**** **** ****" + cardNumber.substring(12);
    }

    private SecretKey createAesKey(String key) {
        return new SecretKeySpec(Base64.getDecoder().decode(key), "AES");
    }
}