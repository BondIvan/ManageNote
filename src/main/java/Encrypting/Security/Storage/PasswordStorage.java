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
import java.util.Arrays;

public class PasswordStorage {

    private static final String PATH_TO_KEY_STORE = "C:\\My place\\Java projects\\ItsClone\\ManageNote\\Files\\Storage\\KeysStorage.ks";

    // Инициализация защищённого хранилища keyStore
    public KeyStore initializeKeyStore(char[] storePassword) throws KeyStoreException, IOException, CertificateException, NoSuchAlgorithmException {

        KeyStore keyStore = KeyStore.getInstance("PKCS12");

        try {
            if(Files.exists(Paths.get(PATH_TO_KEY_STORE))) {
                try (FileInputStream fileInputStream = new FileInputStream(PATH_TO_KEY_STORE)) {
                    keyStore.load(fileInputStream, storePassword);
                }
            } else {
                // Если хранилище keyStore ещё не создали, создать его пустым
                keyStore.load(null, storePassword);
                try (FileOutputStream fileOutputStream = new FileOutputStream(PATH_TO_KEY_STORE)) {
                    keyStore.store(fileOutputStream, storePassword);
                }
            }
        } finally { // Очистка чувствиельных данных из памяти
            Arrays.fill(storePassword, '\0');
            // Благодаря блоку try-with-resources fileOutputStream поток закроется автоматически, даже если произойдёт исключение
        }

        System.out.println("Текущий размер keyStore = " + keyStore.size());

        return keyStore;
    }

    // Сохранение ключа в keyStore
    public void saveKey(KeyStore keyStore, String aliasID, SecretKey key, char[] storePassword) throws KeyStoreException, IOException, CertificateException, NoSuchAlgorithmException {

        KeyStore.SecretKeyEntry secretKeyEntry = new KeyStore.SecretKeyEntry(key);
        KeyStore.ProtectionParameter protectionParameter = new KeyStore.PasswordProtection(storePassword);
        keyStore.setEntry(aliasID, secretKeyEntry, protectionParameter);

        try (FileOutputStream fileOutputStream = new FileOutputStream(PATH_TO_KEY_STORE)) {
            keyStore.store(fileOutputStream, storePassword);

            System.out.println("Размер keyStore после метода saveKey = " + keyStore.size());
        } finally { // Очистка чувствиельных данных из памяти
            Arrays.fill(storePassword, '\0');
            secretKeyEntry = null;
        }

    }

    // Загрузить ключ из keyStore
    public SecretKey loadKey(KeyStore keyStore, String aliasID, char[] storePassword) throws UnrecoverableEntryException, NoSuchAlgorithmException, KeyStoreException {

        KeyStore.ProtectionParameter protectionParameter = new KeyStore.PasswordProtection(storePassword);
        KeyStore.SecretKeyEntry secretKeyEntry = (KeyStore.SecretKeyEntry) keyStore.getEntry(aliasID, protectionParameter);

        SecretKey secretKey = secretKeyEntry.getSecretKey();

        // Очистка чувствиельных данных из памяти
        Arrays.fill(storePassword, '\0');
        secretKeyEntry = null;

        return secretKey;
    }

    // Удалить ключ из keyStore
    public void deleteKey(KeyStore keyStore, String aliasID, char[] storePassword) throws KeyStoreException, IOException, CertificateException, NoSuchAlgorithmException {

        if(!keyStore.containsAlias(aliasID)) {
            System.out.println("В keyStore нет такого ID");
            return;
        }

        keyStore.deleteEntry(aliasID);

        try (FileOutputStream fileOutputStream = new FileOutputStream(PATH_TO_KEY_STORE)) {
            keyStore.store(fileOutputStream, storePassword);

            System.out.println("Размер keyStore после метода deleteKey = " + keyStore.size());
        } finally { // Очистка чувствиельных данных из памяти
            Arrays.fill(storePassword, '\0');
        }

        System.out.println("Ключ удален из keyStore");
    }

}
