package pro.pantrypilot.helpers;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PasswordHasherTest {

    @Test
    public void testPasswordGeneration() {
        String password = "testpassword123";

        PasswordHasher.Password hashedPassword = PasswordHasher.generatePassword(password);

        assertNotNull(hashedPassword);
        assertNotNull(hashedPassword.getHashedValue());
        assertFalse(hashedPassword.getHashedValue().isEmpty());
        // BCrypt hashes should start with $2a$ or $2b$
        assertTrue(hashedPassword.getHashedValue().startsWith("$2a$") 
                || hashedPassword.getHashedValue().startsWith("$2b$"));
    }

    @Test
    public void testPasswordVerification() {
        String correctPassword = "testpassword123";
        String wrongPassword = "wrongpassword123";

        PasswordHasher.Password hashedPassword = PasswordHasher.generatePassword(correctPassword);
        String storedHash = hashedPassword.getHashedValue();

        assertTrue(PasswordHasher.verifyPassword(correctPassword, storedHash));
        assertFalse(PasswordHasher.verifyPassword(wrongPassword, storedHash));
    }

    @Test
    public void testSamePasswordDifferentHashes() {
        String password = "testpassword123";

        PasswordHasher.Password hashedPassword1 = PasswordHasher.generatePassword(password);
        PasswordHasher.Password hashedPassword2 = PasswordHasher.generatePassword(password);

        // With BCrypt, even the same password should produce different hashes due to different salts
        assertNotEquals(hashedPassword1.getHashedValue(), hashedPassword2.getHashedValue());
    }
}