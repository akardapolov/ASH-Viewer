package config.security;

import lombok.SneakyThrows;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class PassConfigTest {
    private PassConfig passConfig;
    private String passwordStr = "test";

    @Before
    public void setUp() {
        passConfig = new PassConfig();
    }

    @Test
    @SneakyThrows
    public void encryptDecryptForLocal() {
        String passEncode = passConfig.encrypt(passwordStr);
        String passDecode = passConfig.decrypt(passEncode);

        assertEquals(passwordStr, passDecode);
    }

}