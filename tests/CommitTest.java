package tests;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import src.Git.Commit;
import src.Git.Index;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;
import java.util.Comparator;
import java.util.Scanner;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.*;

public class CommitTest {
    @BeforeAll
    public static void setUp() throws IOException {
        File objects = new File("/objects");
        if (objects.exists()) {
            try (Stream<Path> pathStream = Files.walk(objects.toPath())) {
                pathStream.sorted(Comparator.reverseOrder())
                        .map(Path::toFile)
                        .forEach(File::delete);
            }
        }

        File index = new File("index");
        index.delete();

        Index.init();
    }

    @AfterAll
    public static void takeDown() throws IOException {
        File objects = new File("/objects");
        if (objects.exists()) {
            try (Stream<Path> pathStream = Files.walk(objects.toPath())) {
                pathStream.sorted(Comparator.reverseOrder())
                        .map(Path::toFile)
                        .forEach(File::delete);
            }
        }

        File index = new File("index");
        index.delete();
    }

    @Test // Test if generateSha1 returns the correct result
    public void testGenerateSha1() throws NoSuchAlgorithmException, IOException {
        Commit commit = new Commit("parentSha1", "Author", "Summary");
        String sha1 = commit.generateSha1();
        assertEquals("Test if generateSha1 works", "2f65c30f4a6010afc90b09780ef244db343096ad", sha1);
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

    public void addFolder(String name) {
        File testOneCommitFolder = new File(name);
        testOneCommitFolder.mkdir();
    }

    public void addFile(String path, String content) throws IOException {
        FileWriter fw = new FileWriter(path);
        fw.write(content);
        fw.close();
    }

    @Test
    public void testOneCommit() throws Exception {
        addFolder("testOneCommit");
        addFile("testOneCommit/file1", "asdasdasdasd");
        addFile("testOneCommit/file2", "asdnasd asdn asjd");

        Index.addDirectory("testOneCommit");
        Commit commit = new Commit("", "samskulsky", "testing one commit");
        commit.writeToFile(commit.generateSha1());

        FileReader fr = new FileReader("objects/" + commit.generateSha1());
        Scanner scan = new Scanner(fr);
        String treeSha = scan.nextLine();
        String prev = scan.nextLine();
        String next = scan.nextLine();
        scan.close();
        fr.close();

        assertNotNull("Tree sha is not null", treeSha);
        assertEquals("Previous sha is correct", prev, "");
        assertEquals("Next sha is correct", next, "");
    }

    @Test
    public void testTwoCommits() throws Exception {
        addFolder("testTwoCommitsA");
        addFolder("testTwoCommitsB");

        addFile("testTwoCommitsA/a", "aa");
        addFile("testTwoCommitsA/b", "bb");
        addFile("testTwoCommitsB/c", "cc");
        addFile("testTwoCommitsB/d", "dd");

        Index.addDirectory("testTwoCommitsA");
        Commit commit = new Commit("", "samskulsky", "testing two commits (a)");
        commit.writeToFile(commit.generateSha1());

        Index.addDirectory("testTwoCommitsB");
        Commit commit2 = new Commit(commit.generateSha1(), "samskulsky", "testing two commits (b)");
        commit2.writeToFile(commit2.generateSha1());

        FileReader fr1 = new FileReader("objects/" + commit.generateSha1());
        Scanner scan1 = new Scanner(fr1);
        String treeSha1 = scan1.nextLine();
        String prev1 = scan1.nextLine();
        String next1 = scan1.nextLine();
        scan1.close();
        fr1.close();

        FileReader fr2 = new FileReader("objects/" + commit2.generateSha1());
        Scanner scan2 = new Scanner(fr2);
        String treeSha2 = scan2.nextLine();
        String prev2 = scan2.nextLine();
        String next2 = scan2.nextLine();
        scan2.close();
        fr2.close();

        assertEquals("New commit's previous sha is old", prev2, commit.generateSha1());
        assertEquals("Old commit's next sha is new", next1, commit2.generateSha1());

    }
}
