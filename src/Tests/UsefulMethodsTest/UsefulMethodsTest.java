package Tests.UsefulMethodsTest;

import Tools.UsefulMethods;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class UsefulMethodsTest extends UsefulMethods {

    @Test
    void testChangingNameWhenAdd() {
    }

    @Test
    void testChangingNameWhenRemove() {
    }

    @Test
    void testGetAllAccountsForOneService() {
    }

    @Test
    void testGetAccountFromServiceByLogin() {
    }

    @Test
    void testMakeArgsTrue() {

        String postfixGet = "   Vk.com  ";
        String postfixAdd = "  Vk.com  first_vk.account@gmail.com     password_vk_1   ";
        String postfixDelete = "  Vk.com  second_vk.account@gmail.com  ";
        String postfixReplace = "  Vk.com  third_vk.account@gmail.com   newString  ";

        String[] argsGet = UsefulMethods.makeArgsTrue(postfixGet);
        String[] argsAdd = UsefulMethods.makeArgsTrue(postfixAdd);
        String[] argsDelete = UsefulMethods.makeArgsTrue(postfixDelete);
        String[] argsReplace = UsefulMethods.makeArgsTrue(postfixReplace);

        Assertions.assertArrayEquals(new String[] {"Vk.com"}, argsGet);
        Assertions.assertArrayEquals(new String[] {"Vk.com", "first_vk.account@gmail.com", "password_vk_1"}, argsAdd);
        Assertions.assertArrayEquals(new String[] {"Vk.com", "second_vk.account@gmail.com"}, argsDelete);
        Assertions.assertArrayEquals(new String[] {"Vk.com", "third_vk.account@gmail.com", "newString"}, argsReplace);
    }

    @Test
    void testSortNoteEntityByServiceName() {
    }

    @Test
    void testGetAllUniqueServiceName() {
    }
}