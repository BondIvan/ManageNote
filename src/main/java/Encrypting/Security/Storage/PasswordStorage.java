package Encrypting.Security.Storage;

import javax.crypto.SecretKey;
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

public class PasswordStorage {

    private static final String PATH_TO_KEY_STORE = "C:\\My place\\Java projects\\ItsClone\\ManageNote\\Files\\Storage\\KeysStorage.ks";

    // Инициализация защищённого хранилища keyStore
    public KeyStore initializeKeyStore(char[] storePassword) throws KeyStoreException, IOException, CertificateException, NoSuchAlgorithmException {

        KeyStore keyStore = KeyStore.getInstance("PKCS12");

        if(Files.exists(Paths.get(PATH_TO_KEY_STORE))) {
            FileInputStream fileInputStream = new FileInputStream(PATH_TO_KEY_STORE);
            keyStore.load(fileInputStream, storePassword);

            return keyStore;
        }

        // Если хранилище keyStore ещё не создали, создать его пустым
        keyStore.load(null, storePassword);
        FileOutputStream fileOutputStream = new FileOutputStream(PATH_TO_KEY_STORE);
        keyStore.store(fileOutputStream, storePassword);

        return keyStore;
    }

    // Сохранение ключа в keyStore
    public void saveKey(KeyStore keyStore, String aliasServiceName, SecretKey key, char[] storePassword) throws KeyStoreException, IOException, CertificateException, NoSuchAlgorithmException {

        KeyStore.SecretKeyEntry secretKeyEntry = new KeyStore.SecretKeyEntry(key);
        KeyStore.ProtectionParameter protectionParameter = new KeyStore.PasswordProtection(storePassword);
        keyStore.setEntry(aliasServiceName, secretKeyEntry, protectionParameter);

        FileOutputStream fileOutputStream = new FileOutputStream(PATH_TO_KEY_STORE);
        keyStore.store(fileOutputStream, storePassword);
    }

    // Загрузить ключ из keyStore
    public SecretKey loadKey(KeyStore keyStore, String aliasServiceName, char[] storePassword) throws UnrecoverableEntryException, NoSuchAlgorithmException, KeyStoreException {

        KeyStore.ProtectionParameter protectionParameter = new KeyStore.PasswordProtection(storePassword);
        KeyStore.SecretKeyEntry secretKeyEntry = (KeyStore.SecretKeyEntry) keyStore.getEntry(aliasServiceName, protectionParameter);

        SecretKey secretKey = secretKeyEntry.getSecretKey();

        return secretKey;
    }

    // Удалить ключ из keyStore
    public void deleteKey(KeyStore keyStore, String aliasServiceName, char[] storePassword) throws KeyStoreException, IOException, CertificateException, NoSuchAlgorithmException {

        if(!keyStore.containsAlias(aliasServiceName)) {
            System.out.println("В keyStore нет такого сервиса");
            return;
        }

        keyStore.deleteEntry(aliasServiceName);

        FileOutputStream fileOutputStream = new FileOutputStream(PATH_TO_KEY_STORE);
        keyStore.store(fileOutputStream, storePassword);

        System.out.println("Ключ удален из keyStore");
    }

}
