package pro.pantrypilot.helpers;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PasswordHasherTest {

    @Test
    public void testPasswordGeneration() {
        String username = "testuser";
        String password = "testpassword123";

        PasswordHasher.Password hashedPassword = PasswordHasher.generatePassword(username, password);

        assertNotNull(hashedPassword);
        assertNotNull(hashedPassword.getHash());
        assertNotNull(hashedPassword.getSalt());
        assertFalse(hashedPassword.getHash().isEmpty());
        assertFalse(hashedPassword.getSalt().isEmpty());
    }

    @Test
    public void testPasswordVerification() {
        String username = "testuser";
        String correctPassword = "testpassword123";
        String wrongPassword = "wrongpassword123";

        PasswordHasher.Password hashedPassword = PasswordHasher.generatePassword(username, correctPassword);

        assertTrue(PasswordHasher.verifyPassword(
            correctPassword, 
            hashedPassword.getSalt(), 
            hashedPassword.getHash()
        ));

        assertFalse(PasswordHasher.verifyPassword(
            wrongPassword, 
            hashedPassword.getSalt(), 
            hashedPassword.getHash()
        ));
    }

    @Test
    public void testSamePasswordDifferentSalts() {
        String password = "testpassword123";
        String username1 = "user1";
        String username2 = "user2";

        PasswordHasher.Password hashedPassword1 = PasswordHasher.generatePassword(username1, password);
        PasswordHasher.Password hashedPassword2 = PasswordHasher.generatePassword(username2, password);

        assertNotEquals(hashedPassword1.getHash(), hashedPassword2.getHash());
        assertNotEquals(hashedPassword1.getSalt(), hashedPassword2.getSalt());
    }
}