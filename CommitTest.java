import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.*;

public class CommitTest {

    @Test // Test if generateSha1 returns the correct result
    public void testGenerateSha1() throws NoSuchAlgorithmException {
        Commit commit = new Commit("parentSha1", "Author", "Summary");
        String sha1 = commit.generateSha1();
        assertEquals("Test if generateSha1 works", "13f090f6895c9a420dd5e21385c9e5a8d36ea77e", sha1);
    }

    @Test // Test if writeToFile correctly writes
    public void testWriteToFile() throws NoSuchAlgorithmException, IOException {
        Commit commit = new Commit("parentSha1", "Author", "Summary");
        String expectedSha1 = commit.generateSha1(); // Expected SHA1 hash based on commit data
        String fileName = "test_commit";

        commit.writeToFile(fileName);

        File file = new File("objects/" + expectedSha1);
        assertTrue(file.exists());

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            StringBuilder fileContent = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                fileContent.append(line).append("\n");
            }

            StringBuilder expectedCommitData = new StringBuilder();
            expectedCommitData.append(commit.getTreeSha1() + "\n");
            expectedCommitData.append(commit.getParentSha1() + "\n");
            expectedCommitData.append("\n");
            expectedCommitData.append(commit.getAuthor() + "\n");
            expectedCommitData.append(commit.getDate() + "\n");
            expectedCommitData.append(commit.getSummary() + "\n");

            assertEquals("Test if writeToFile works", expectedCommitData.toString(), fileContent.toString());
        }

    }
}
