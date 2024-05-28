package Encrypting.Security.Storage;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.*;
import java.security.cert.CertificateException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

public class PasswordStorage {

    private static final String PATH_TO_SALT_STORE = "C:\\My place\\Java projects\\ItsClone\\ManageNote\\Files\\Storage\\PasswordStore.ks";

    // Инициализация защищённого хранилища keyStore
    private static KeyStore initializeKeyStore(String pathToPasswordKeyStore, char[] storePassword) throws KeyStoreException, IOException, CertificateException, NoSuchAlgorithmException {

        KeyStore keyStore = KeyStore.getInstance(keyStoreType);

        if(Files.exists(Paths.get(pathToPasswordKeyStore))) {
            FileInputStream fileInputStream = new FileInputStream(pathToPasswordKeyStore);
            keyStore.load(fileInputStream, storePassword);

            return keyStore;
        }

        keyStore.load(null, storePassword);
        FileOutputStream fileOutputStream = new FileOutputStream(pathToPasswordKeyStore);
        keyStore.store(fileOutputStream, storePassword);

        return keyStore;
    }

    // Сохранение ключа в keyStore
    private static void saveKey(KeyStore keyStore, String aliasServiceName, SecretKey key, char[] storePassword) throws KeyStoreException, IOException, CertificateException, NoSuchAlgorithmException {

        KeyStore.SecretKeyEntry secretKeyEntry = new KeyStore.SecretKeyEntry(key);
        KeyStore.ProtectionParameter protectionParameter = new KeyStore.PasswordProtection(storePassword);
        keyStore.setEntry(aliasServiceName, secretKeyEntry, protectionParameter);

        FileOutputStream fileOutputStream = new FileOutputStream(PATH_TO_KS);
        keyStore.store(fileOutputStream, storePassword);
    }

    // Загрузить ключ из keyStore
    private static SecretKey loadKey(KeyStore keyStore, String aliasServiceName) throws UnrecoverableEntryException, NoSuchAlgorithmException, KeyStoreException {

        KeyStore.ProtectionParameter protectionParameter = new KeyStore.PasswordProtection(keyStorePassword.toCharArray());
        KeyStore.SecretKeyEntry secretKeyEntry = (KeyStore.SecretKeyEntry) keyStore.getEntry(aliasServiceName, protectionParameter);

        SecretKey secretKey = secretKeyEntry.getSecretKey();

        return secretKey;
    }

    private static void deleteKey(KeyStore keyStore, String aliasServiceName, char[] storePassword) throws KeyStoreException, IOException, CertificateException, NoSuchAlgorithmException {

        if(!keyStore.containsAlias(aliasServiceName)) {
            System.out.println("В keyStore нет такого сервиса");
            return;
        }

        keyStore.deleteEntry(aliasServiceName);

        FileOutputStream fileOutputStream = new FileOutputStream(PATH_TO_KS);
        keyStore.store(fileOutputStream, storePassword);

        System.out.println("Ключ удален из keyStore");
    }

    public static SecretKey generateSyncKey(char[] password) throws InvalidKeySpecException, NoSuchAlgorithmException {

        // Генерация ключа на основе пароля и соли
        SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        KeySpec spec = new PBEKeySpec(password, generateSalt(), 65536, 256);
        SecretKey tmp = secretKeyFactory.generateSecret(spec);

        // Это строка, указывающая алгоритм, для которого предназначен ключ. В данном случае мы указываем, что это ключ для алгоритма AES.
        SecretKey secretKey = new SecretKeySpec(tmp.getEncoded(), "AES");

        return secretKey;
    }

    public static byte[] generateSalt() {

        SecureRandom secureRandom = new SecureRandom();
        byte[] salt = new byte[16];
        secureRandom.nextBytes(salt);

        return salt;
    }

}
