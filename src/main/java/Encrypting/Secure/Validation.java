package Encrypting.Secure;

import javax.crypto.*;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Arrays;
import java.util.Scanner;

public class Validation {

    private final String PATH_SALT = "C:\\My place\\Java projects\\ItsClone\\ManageNote\\Files\\Validation\\Salt.bin";
    private final String PATH_VALIDATION = "C:\\My place\\Java projects\\ItsClone\\ManageNote\\Files\\Validation\\Validation.bin";
    private final int AES_KEY_SIZE = 256;

    public boolean checkInputPassword(String inputPassword) throws NoSuchPaddingException, IllegalBlockSizeException, IOException, NoSuchAlgorithmException, InvalidKeySpecException, BadPaddingException, InvalidKeyException {

        if( !isExist() ) {
            createMasterPassword();
        }

        char[] input = inputPassword.toCharArray();
        byte[] salt = Files.readAllBytes(Paths.get(PATH_SALT));
        byte[] validation = Files.readAllBytes(Paths.get(PATH_VALIDATION));
        byte[] unverified = createValidation(input, salt);

        boolean result =  Arrays.equals(validation, unverified);

        Arrays.fill(input, '\0');
        Arrays.fill(salt, (byte) '\0');
        Arrays.fill(validation, (byte) '\0');
        Arrays.fill(unverified, (byte) '\0');

        return result;
    }

    // Проверить, установлен ли уже мастер-пароль (если файлы есть, значит установлен)
    private boolean isExist() {

        return Files.exists(Paths.get(PATH_SALT)) && Files.exists(Paths.get(PATH_VALIDATION));
    }

    // Создать мастер-пароль
    private void createMasterPassword() throws IOException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, InvalidKeySpecException, BadPaddingException, InvalidKeyException {

        Scanner scanner = new Scanner(System.in);
        System.out.print("Задайте мастер-пароль: ");
        char[] inputPassword = scanner.nextLine().toCharArray();

        if(!Files.exists(Paths.get(PATH_SALT)) || !Files.exists(Paths.get(PATH_VALIDATION))) {
            byte[] salt = generateSalt();
            byte[] validation = createValidation(inputPassword, salt);

            Files.write(Paths.get(PATH_SALT), salt);
            Files.write(Paths.get(PATH_VALIDATION), validation);

            Arrays.fill(inputPassword, '\0');

            System.out.println("Мастер-пароль установлен");
        }

    }

    private byte[] generateSalt() {
        SecureRandom secureRandom = new SecureRandom();
        byte[] salt = new byte[16];
        secureRandom.nextBytes(salt);

        return salt;
    }

    private byte[] createValidation(char[] input, byte[] salt) throws NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {

        // Генерация ключа на основе мастер-пароля и соли
        SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        KeySpec spec = new PBEKeySpec(input, salt, 65536, AES_KEY_SIZE);
        byte[] key = secretKeyFactory.generateSecret(spec).getEncoded();

        // Создание экземплера шифра AES
        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        SecretKeySpec secretKeySpec = new SecretKeySpec(key, "AES");
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);

        // Шифрование известного значения - "validation";
        byte[] encryptedValidation = cipher.doFinal("validation".getBytes(StandardCharsets.UTF_8));

        return encryptedValidation;
    }

}
