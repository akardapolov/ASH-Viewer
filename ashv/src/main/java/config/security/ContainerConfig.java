package config.security;

import gui.model.RegistryKey;
import java.util.Base64;
import java.util.prefs.Preferences;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.inject.Inject;
import javax.inject.Singleton;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Singleton
public class ContainerConfig {
  private final Preferences projectPreferences;
  @Inject
  public ContainerConfig() {
    Preferences prefsRoot = Preferences.userRoot();
    projectPreferences = prefsRoot.node("ASH-Viewer");
  }

  public String getRegistryValue(String profileName, RegistryKey registryKey) {
    return projectPreferences.get(getKey(profileName, registryKey), "");
  }

  public void setRegistryValue(String profileName, RegistryKey registryKey, String value) {
    projectPreferences.put(getKey(profileName, registryKey), value);
  }

  private String getKey(String profileName, RegistryKey registryKey) {
    return profileName + "_" + registryKey;
  }

  public String convertSecretKeyToString(SecretKey secretKey) {
    byte[] rawData = secretKey.getEncoded();
    return Base64.getEncoder().encodeToString(rawData);
  }

  public SecretKey convertStringToSecretKey(String encodedKey) {
    byte[] decodedKey = Base64.getDecoder().decode(encodedKey);
    return new SecretKeySpec(decodedKey, 0, decodedKey.length, "AES");
  }

  public String convertByteToString(byte[] rawData) {
    return Base64.getEncoder().encodeToString(rawData);
  }

  public byte[] convertStringToByte(String encodedKey) {
    return Base64.getDecoder().decode(encodedKey);
  }

}
