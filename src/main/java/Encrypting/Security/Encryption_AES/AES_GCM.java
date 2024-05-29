package Encrypting.Security.Encryption_AES;

import Encrypting.Security.Storage.PasswordStorage;
import Encrypting.Security.Storage.SaltStorage;

import javax.crypto.*;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.security.*;
import java.security.cert.CertificateException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Arrays;
import java.util.Base64;

public class AES_GCM {

    private static final int AES_KEY_SIZE = 256;
    private static final int GCM_IV_LENGTH = 12;
    private static final int GCM_TAG_LENGTH = 128;
    private static final int SALT_LENGTH = 16;

    // Шифрование пароля
    public String encrypt(String password, String serviceName) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, InvalidKeySpecException, CertificateException, KeyStoreException, IOException {

        byte[] salt = generateSalt();
        byte[] iv = generateIV();
        SecretKey key = generateKey(password.toCharArray(), salt);

        saveSaltToStorage(serviceName, salt);
        saveKeyToStorage(serviceName, key);

        // Создание экземплера шифра AES
        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        GCMParameterSpec gcmSpec =new GCMParameterSpec(GCM_TAG_LENGTH, iv);
        cipher.init(Cipher.ENCRYPT_MODE, key, gcmSpec);

        // Конкатенация IV и зашифрованных данных
        byte[] encrypted = cipher.doFinal(password.getBytes());
        byte[] concatenatedIvAndEncrypted = new byte[iv.length + encrypted.length];
        // Массив (arr1) источник | с какой позиции начать в arr1 | куда скопировать (arr2) | с какой позиции (arr2) начинать вставку | количество элементов, которые нужно вставить
        System.arraycopy(iv, 0, concatenatedIvAndEncrypted, 0, iv.length);
        System.arraycopy(encrypted, 0, concatenatedIvAndEncrypted, iv.length, encrypted.length);

        String base64View = Base64.getEncoder().encodeToString(concatenatedIvAndEncrypted);

        return base64View;
    }

    // Сохранить соль в защищённое хранилище
    private void saveSaltToStorage(String serviceName, byte[] salt) throws CertificateException, KeyStoreException, IOException, NoSuchAlgorithmException {
        String tmpPassword = "123";
        SaltStorage saltStorage = new SaltStorage();
        KeyStore keyStore = saltStorage.initializeKeyStore(tmpPassword.toCharArray());

        saltStorage.saveSalt(keyStore, serviceName, salt, tmpPassword.toCharArray());
    }

    // Сохранить ключ в защищённое хранилище
    private void saveKeyToStorage(String serviceName, SecretKey key) throws CertificateException, KeyStoreException, IOException, NoSuchAlgorithmException {
        String tmpPassword = "123";
        PasswordStorage passwordStorage = new PasswordStorage();
        KeyStore keyStore = passwordStorage.initializeKeyStore(tmpPassword.toCharArray());

        passwordStorage.saveKey(keyStore, serviceName, key, tmpPassword.toCharArray());
    }

    // Расшифровка пароля
    public String decrypt(String encrypted, SecretKey key) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {


        byte[] fromBase64ToByteView = Base64.getDecoder().decode(encrypted);
        byte[] iv = Arrays.copyOfRange(fromBase64ToByteView, 0, GCM_IV_LENGTH);
        byte[] encryptText = Arrays.copyOfRange(fromBase64ToByteView, GCM_IV_LENGTH, fromBase64ToByteView.length);

        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        GCMParameterSpec gcmSpec = new GCMParameterSpec(GCM_TAG_LENGTH, iv);
        cipher.init(Cipher.DECRYPT_MODE, key);
        byte[] decrypted = cipher.doFinal(encryptText);

        return new String(decrypted);
    }

    // Генерация ключа шифрования
    private SecretKey generateKey(char[] password, byte[] salt) throws NoSuchAlgorithmException, InvalidKeySpecException {

        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        KeySpec spec = new PBEKeySpec(password, salt, 65536, AES_KEY_SIZE);
        SecretKey tmp = factory.generateSecret(spec);

        // Это строка, указывающая алгоритм, для которого предназначен ключ. В данном случае мы указываем, что это ключ для алгоритма AES.
        return new SecretKeySpec(tmp.getEncoded(), "AES");
    }

    // Генерация соли
    private byte[] generateSalt() {

        SecureRandom secureRandom = new SecureRandom();
        byte[] salt = new byte[SALT_LENGTH];
        secureRandom.nextBytes(salt);

        return salt;
    }

    // Генерация IV
    private byte[] generateIV() {

        SecureRandom secureRandom = new SecureRandom();
        byte[] iv = new byte[GCM_IV_LENGTH];
        secureRandom.nextBytes(iv);

        return iv;
    }

}
