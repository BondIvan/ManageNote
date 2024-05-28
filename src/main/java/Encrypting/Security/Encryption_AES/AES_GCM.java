package Encrypting.Security.Encryption_AES;

import javax.crypto.*;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Arrays;
import java.util.Base64;

public class AES_GCM {

    private static final int AES_KEY_SIZE = 256;
    private static final int GCM_IV_LENGTH = 12;
    private static final int GCM_TAG_LENGTH = 128;
    private static final int SALT_LENGTH = 16;

    public String encrypt(String password, SecretKey key, byte[] iv) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {

        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        GCMParameterSpec gcmSpec =new GCMParameterSpec(GCM_TAG_LENGTH, iv);
        cipher.init(Cipher.ENCRYPT_MODE, key, gcmSpec);

        byte[] encrypted = cipher.doFinal(password.getBytes());
        byte[] concatenated = new byte[iv.length + encrypted.length];
        System.arraycopy(iv, 0, concatenated, 0, iv.length);
        System.arraycopy(encrypted, 0, concatenated, iv.length, encrypted.length);

        String base64View = Base64.getEncoder().encodeToString(concatenated);

        return base64View;
    }

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

    private SecretKey generateKey(char[] password, byte[] salt) throws NoSuchAlgorithmException, InvalidKeySpecException {

        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        KeySpec spec = new PBEKeySpec(password, salt, 65536, AES_KEY_SIZE);
        SecretKey tmp = factory.generateSecret(spec);

        // Это строка, указывающая алгоритм, для которого предназначен ключ. В данном случае мы указываем, что это ключ для алгоритма AES.
        return new SecretKeySpec(tmp.getEncoded(), "AES");
    }

    private byte[] generateSalt() {

        SecureRandom secureRandom = new SecureRandom();
        byte[] salt = new byte[SALT_LENGTH];
        secureRandom.nextBytes(salt);

        return salt;
    }

    private byte[] generateIV() {

        SecureRandom secureRandom = new SecureRandom();
        byte[] iv = new byte[GCM_IV_LENGTH];
        secureRandom.nextBytes(iv);

        return iv;
    }

}
