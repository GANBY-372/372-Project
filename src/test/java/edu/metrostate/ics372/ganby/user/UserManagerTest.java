package edu.metrostate.ics372.ganby.user;

import org.junit.jupiter.api.*;

import java.io.IOException;
import java.nio.file.*;

import static org.junit.jupiter.api.Assertions.*;

class UserManagerTest {

    private static final Path TEST_USER_FILE = Paths.get("user.txt");

    @BeforeEach
    void setUp() throws IOException {
        // Ensure clean state
        Files.deleteIfExists(TEST_USER_FILE);
    }

    @AfterEach
    void tearDown() throws IOException {
        Files.deleteIfExists(TEST_USER_FILE);
    }

    @Test
    void userExists_shouldReturnFalseIfNoFile() {
        assertFalse(UserManager.userExists());
    }

    @Test
    void saveUser_shouldCreateFile() throws IOException {
        UserManager.saveUser("testuser", "testpass");
        assertTrue(Files.exists(TEST_USER_FILE));

        String content = Files.readString(TEST_USER_FILE);
        assertEquals("testuser,testpass", content.trim());
    }

    @Test
    void validateUser_shouldMatchSavedCredentials() throws IOException {
        UserManager.saveUser("alice", "wonderland");

        assertTrue(UserManager.validateUser("alice", "wonderland"));
        assertFalse(UserManager.validateUser("alice", "wrongpass"));
        assertFalse(UserManager.validateUser("bob", "wonderland"));
    }

    @Test
    void validateUser_shouldFailIfFileMissing() throws IOException {
        assertFalse(UserManager.validateUser("nonuser", "nopass"));
    }

    @Test
    void validateUser_shouldFailIfFileCorrupt() throws IOException {
        Files.writeString(TEST_USER_FILE, "badly-formatted-line");
        assertFalse(UserManager.validateUser("any", "thing"));
    }
}
