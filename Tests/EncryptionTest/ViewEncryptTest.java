package Tests.EncryptionTest;

import Encrypting.MyEncrypt.Alphabet.Alphabet;
import Encrypting.MyEncrypt.Alphabet.ViewDecrypt;
import Encrypting.MyEncrypt.Alphabet.ViewEncrypt;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ViewEncryptTest {

    @Test
    public void encrypting() {

        String str1 = "5G(;E,^&*)hgkf345#";
        String str2 = "fezmAt7-tosdir-1qupzu";
        String str3 = "";
        String str4 = "";

        ViewEncrypt viewEncrypt = new ViewEncrypt(Alphabet.getAlpha());
        String encrStr1 = viewEncrypt.encrypting(str1);
        String encrStr2 = viewEncrypt.encrypting(str2);
        String encrStr3 = viewEncrypt.encrypting(str3);
        String encrStr4 = viewEncrypt.encrypting(str4);

        ViewDecrypt viewDecrypt = new ViewDecrypt(Alphabet.getAlpha());

        Assertions.assertEquals(viewDecrypt.decrypt(encrStr1), str1);
        Assertions.assertEquals(viewDecrypt.decrypt(encrStr2), str2);
        Assertions.assertEquals(viewDecrypt.decrypt(encrStr3), str3);
        Assertions.assertEquals(viewDecrypt.decrypt(encrStr4), str4);
    }

}
