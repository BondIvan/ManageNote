package Encrypting.Security.Storage;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.*;
import java.security.cert.CertificateException;

public class SaltStorage {

    private static final String PATH_TO_SALT_STORE = "C:\\My place\\Java projects\\ItsClone\\ManageNote\\Files\\Storage\\SaltStore.ks";

    private static KeyStore initializeKeyStore(String pathToSaltKeyStore, char[] storePassword) throws KeyStoreException, IOException, CertificateException, NoSuchAlgorithmException {

        KeyStore keyStore = KeyStore.getInstance(keyStoreType);

        if(Files.exists(Paths.get(pathToSaltKeyStore))) {
            FileInputStream fileInputStream = new FileInputStream(pathToSaltKeyStore);
            keyStore.load(fileInputStream, storePassword);

            return keyStore;
        }

        keyStore.load(null, storePassword);
        FileOutputStream fileOutputStream = new FileOutputStream(pathToSaltKeyStore);
        keyStore.store(fileOutputStream, storePassword);

        return keyStore;
    }

    private static void saveSalt(KeyStore keyStore, String pathKeyStoreFile, String aliasServiceName, byte[] salt, char[] storePassword) throws KeyStoreException, IOException, CertificateException, NoSuchAlgorithmException {

        SecretKey secretKey = new SecretKeySpec(salt, "AES");
        KeyStore.SecretKeyEntry secretKeyEntry = new KeyStore.SecretKeyEntry(secretKey);
        KeyStore.ProtectionParameter protectionParameter = new KeyStore.PasswordProtection(storePassword);

        keyStore.setEntry(aliasServiceName, secretKeyEntry, protectionParameter);

        FileOutputStream fileOutputStream = new FileOutputStream(pathKeyStoreFile);
        keyStore.store(fileOutputStream, storePassword);
    }

    private static byte[] loadSalt(KeyStore keyStore, String alias) throws UnrecoverableEntryException, NoSuchAlgorithmException, KeyStoreException {

        KeyStore.ProtectionParameter protectionParameter = new KeyStore.PasswordProtection(keyStorePassword.toCharArray());
        KeyStore.SecretKeyEntry secretKeyEntry = (KeyStore.SecretKeyEntry) keyStore.getEntry(alias, protectionParameter);

        SecretKey secretKey = secretKeyEntry.getSecretKey();

        return secretKey.getEncoded();
    }

    private static void deleteSalt(KeyStore keyStore, String pathKeyStoreFile, String aliasServiceName, char[] storePassword) throws IOException, KeyStoreException, CertificateException, NoSuchAlgorithmException {

        if(!keyStore.containsAlias(aliasServiceName)) {
            System.out.println("В keyStore нет такого сервиса");
            return;
        }

        keyStore.deleteEntry(aliasServiceName);

        FileOutputStream fileOutputStream = new FileOutputStream(pathKeyStoreFile);
        keyStore.store(fileOutputStream, storePassword);

        System.out.println("Ключ удален из keyStore");

    }

    public static byte[] generateSalt() {

        SecureRandom secureRandom = new SecureRandom();
        byte[] salt = new byte[16];
        secureRandom.nextBytes(salt);

        return salt;
    }

}
