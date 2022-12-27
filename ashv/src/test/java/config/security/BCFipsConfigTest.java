package config.security;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNotNull;

import java.security.GeneralSecurityException;
import javax.crypto.SecretKey;
import org.junit.Before;
import org.junit.Test;

public class BCFipsConfigTest {
    private BCFipsConfig bcFipsConfig;

    @Before
    public void setUp() {
        bcFipsConfig = new BCFipsConfig();
    }

    @Test
    public void testCfbEncryptDecrypt() throws GeneralSecurityException {
        SecretKey key = bcFipsConfig.generateKey();
        String passwordStr = "test!@#$%^&*()1234567890";
        byte[] plaintext = passwordStr.getBytes();

        byte[][] ivCiphertext = bcFipsConfig.cfbEncrypt(key, plaintext);

        byte[] iv = ivCiphertext[0];
        assertNotNull(iv);
        byte[] ciphertext = ivCiphertext[1];
        assertNotNull(ciphertext);

        byte[] derivedPlainText = bcFipsConfig.cfbDecrypt(key, iv, ciphertext);
        assertNotNull(derivedPlainText);
        assertArrayEquals(plaintext, derivedPlainText);
    }

}