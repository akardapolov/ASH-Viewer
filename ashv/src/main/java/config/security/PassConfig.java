package config.security;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;
import javax.inject.Inject;
import javax.inject.Singleton;

@Slf4j
@Singleton
public class PassConfig {
    private Cipher encCipher;
    private Cipher decCipher;
    private SecretKey pbeKey;
    private PBEParameterSpec pbeParamSpec;
    private String algoritm = "PBEWithMD5AndDES";

    @Inject
    public PassConfig() {
        this.init();
    }

    @SneakyThrows
    private void init() {
        byte[] salt = {
                (byte)0xc7, (byte)0x73, (byte)0x21, (byte)0x8c,
                (byte)0x7e, (byte)0xc8, (byte)0xee, (byte)0x99
        };

        int count = 20;

        pbeParamSpec = new PBEParameterSpec(salt, count);

        PBEKeySpec pbeKeySpec = new PBEKeySpec(new char[]{'2','1','1','7','4'});
        SecretKeyFactory keyFac = SecretKeyFactory.getInstance(algoritm);
        pbeKey = keyFac.generateSecret(pbeKeySpec);

        encCipher = Cipher.getInstance(algoritm);

        decCipher = Cipher.getInstance(algoritm);
    }

    @SneakyThrows
    public final byte[] encodeToBytes(String text) {
        encCipher.init(Cipher.ENCRYPT_MODE, pbeKey, pbeParamSpec);
        byte[] cipherText = encCipher.doFinal(text.getBytes());
        return cipherText;
    }

    @SneakyThrows
    public final String decodeFromBytes(byte[] text) {
        decCipher.init(Cipher.DECRYPT_MODE, pbeKey, pbeParamSpec);
        byte[] cipherText = decCipher.doFinal(text);
        return new String(cipherText);
    }
}
