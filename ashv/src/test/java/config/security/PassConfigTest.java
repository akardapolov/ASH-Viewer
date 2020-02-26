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
    public void encodeToBytes() {
        String secretKey = "Skey";
        String passEncode = passConfig.encrypt(secretKey, passwordStr);
        String passDecode = passConfig.decrypt(secretKey, passEncode);

        assertEquals(passwordStr, passDecode);
    }

}