package Tests.EncryptionTest;

import Encrypting.MyEncrypt.Alphabet.Alphabet;
import Encrypting.MyEncrypt.Alphabet.ViewDecrypt;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ViewDecryptTest {

    @Test
    public void decrypt() {

        ViewDecrypt viewDecrypt = new ViewDecrypt(Alphabet.getAlpha());
        String str1 = "1214086546555655142423998487096.01810136909630.01023822344651150544054706589";
        String str2 = "143007566666656675982342634213608$42004920500729532429532405734.014127592.013161998.018";
        String str3 = "151507656555656555414242337506011151968073902795287113533.01735986!1439448616383122081095575";
        String str4 = "151507565555.01506589110492%%8464396807(;86163,*(39027#";

        Assertions.assertEquals("VIh8Jb0npNtd", viewDecrypt.decrypt(str1));
        Assertions.assertEquals("pRC$ZALLg4w3z8", viewDecrypt.decrypt(str2));
        Assertions.assertEquals("VraGsFM7q!WECcg", viewDecrypt.decrypt(str3));
        Assertions.assertEquals("5dK%%DG(;E,*(s#", viewDecrypt.decrypt(str4));
    }

}
