package config.security;

import java.security.GeneralSecurityException;
import java.security.Security;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.inject.Inject;
import javax.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.jcajce.provider.BouncyCastleFipsProvider;

/**
 * This file contains API for key generation, encrypting and
 * decrypting in CFB (Cipher Feedback) mode. It is similar to CBC while
 * using a streaming block mode. However, padding is no longer required
 * as the cipher generates a stream of "noise" which is XOR'd with the data
 * to be encrypted.
 *
 * <a href="Based on source code from Indra Basak examples">https://github.com/indrabasak/bouncycastle-fips-examples</a>
 *
 * @author Indra Basak
 * @since 11/18/2017
 */

@Slf4j
@Singleton
public class BCFipsConfig {

  @Inject
  public BCFipsConfig() {}

  static {
    Security.addProvider(new BouncyCastleFipsProvider());
  }

  /**
   * Generates an AES key by providing a key size in bits.
   *
   * @return a symmetric key
   * @throws GeneralSecurityException
   */
  public SecretKey generateKey() throws GeneralSecurityException {
    KeyGenerator keyGenerator = KeyGenerator.getInstance("AES", "BCFIPS");
    keyGenerator.init(256);
    return keyGenerator.generateKey();
  }

  /**
   * Encrypts data in CFB (Cipher Feedback) mode.
   *
   * @param secretKey  the secret key used for encryption
   * @param data the plaintext to be encrypted
   * @return array with initialization vector(IV) and encrypted data
   * @throws GeneralSecurityException*
   */
  public byte[][] cfbEncrypt(SecretKey secretKey, byte[] data)
      throws GeneralSecurityException {
    Cipher cipher = Cipher.getInstance("AES/CFB/NoPadding", "BCFIPS");
    cipher.init(Cipher.ENCRYPT_MODE, secretKey);
    return new byte[][]{cipher.getIV(), cipher.doFinal(data)};
  }

  /**
   * Decrypts the data in CFB (Cipher Feedback) mode.
   *
   * @param secretKey        the secret key used for decryption
   * @param iv         initialization vector (IV)
   * @param cipherText an encrypted ciphertext
   * @return array with decrypted data
   * @throws GeneralSecurityException
   */
  public byte[] cfbDecrypt(SecretKey secretKey, byte[] iv,
      byte[] cipherText) throws GeneralSecurityException {
    Cipher cipher = Cipher.getInstance("AES/CFB/NoPadding", "BCFIPS");
    cipher.init(Cipher.DECRYPT_MODE, secretKey, new IvParameterSpec(iv));
    return cipher.doFinal(cipherText);
  }

}
