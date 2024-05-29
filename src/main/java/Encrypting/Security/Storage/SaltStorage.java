package Encrypting.Security.Storage;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableEntryException;
import java.security.cert.CertificateException;

public class SaltStorage {

    private static final String PATH_TO_SALT_STORE = "C:\\My place\\Java projects\\ItsClone\\ManageNote\\Files\\Storage\\SaltStorage.ks";

    // Инициализация защищённого хранилища keyStore
    public KeyStore initializeKeyStore(char[] storePassword) throws KeyStoreException, IOException, CertificateException, NoSuchAlgorithmException {

        KeyStore keyStore = KeyStore.getInstance("PKCS12");

        if(Files.exists(Paths.get(PATH_TO_SALT_STORE))) {
            FileInputStream fileInputStream = new FileInputStream(PATH_TO_SALT_STORE);
            keyStore.load(fileInputStream, storePassword);

            return keyStore;
        }

        // Если хранилище keyStore ещё не создали, создать его пустым
        keyStore.load(null, storePassword);
        FileOutputStream fileOutputStream = new FileOutputStream(PATH_TO_SALT_STORE);
        keyStore.store(fileOutputStream, storePassword);

        return keyStore;
    }

    // Сохранение соли в keyStore
    public void saveSalt(KeyStore keyStore, String aliasServiceName, byte[] salt, char[] storePassword) throws KeyStoreException, IOException, CertificateException, NoSuchAlgorithmException {

        SecretKey secretKey = new SecretKeySpec(salt, "AES");
        KeyStore.SecretKeyEntry secretKeyEntry = new KeyStore.SecretKeyEntry(secretKey);
        KeyStore.ProtectionParameter protectionParameter = new KeyStore.PasswordProtection(storePassword);

        keyStore.setEntry(aliasServiceName, secretKeyEntry, protectionParameter);

        FileOutputStream fileOutputStream = new FileOutputStream(PATH_TO_SALT_STORE);
        keyStore.store(fileOutputStream, storePassword);
    }

    // Загрузить соль из keyStore
    public byte[] loadSalt(KeyStore keyStore, String alias, char[] storePassword) throws UnrecoverableEntryException, NoSuchAlgorithmException, KeyStoreException {

        KeyStore.ProtectionParameter protectionParameter = new KeyStore.PasswordProtection(storePassword);
        KeyStore.SecretKeyEntry secretKeyEntry = (KeyStore.SecretKeyEntry) keyStore.getEntry(alias, protectionParameter);

        SecretKey secretKey = secretKeyEntry.getSecretKey();

        return secretKey.getEncoded();
    }

    // Удалить соль из keyStore
    public void deleteSalt(KeyStore keyStore, String aliasServiceName, char[] storePassword) throws IOException, KeyStoreException, CertificateException, NoSuchAlgorithmException {

        if(!keyStore.containsAlias(aliasServiceName)) {
            System.out.println("В keyStore нет такого сервиса");
            return;
        }

        keyStore.deleteEntry(aliasServiceName);

        FileOutputStream fileOutputStream = new FileOutputStream(PATH_TO_SALT_STORE);
        keyStore.store(fileOutputStream, storePassword);

        System.out.println("Ключ удален из keyStore");
    }

}
