package tests;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import src.Git.Index;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Comparator;
import java.util.stream.Stream;

public class IndexTester {

    private static final String testFinalPath = "testfile.txt";
    private static final String testFileContent = "Testing the index and blob classes";

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

        try {
            FileWriter fileWriter = new FileWriter(testFinalPath);
            fileWriter.write(testFileContent);
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
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

    private String readFileContent(String filePath) throws IOException {
        return new String(Files.readAllBytes(Paths.get(filePath)));
    }

    @Test
    public void testInit() throws IOException {

        Index.init();

        assertTrue(folderExists("objects"));
    }

    @Test
    public void testAddBlob() throws IOException {

        Index.init();

        Index.addBlob(testFinalPath);

        try {
            assertTrue(fileExists("objects/" + getSha1(testFileContent)));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        String indexContents = readFileContent("index");
        assertTrue(indexContents.contains(testFinalPath));
    }

    @Test
    public void testRemoveBlob() throws IOException {
        Index.init();

        Index.addBlob(testFinalPath);

        Index.removeBlob(testFinalPath);

        String sha1 = null;
        try {
            sha1 = getSha1(Index.readFile(testFinalPath));
        } catch (NoSuchAlgorithmException e) {
            fail("SHA-1 algorithm not available");
        }

        String indexContents = readFileContent("index");
        assertFalse(indexContents.contains(testFinalPath + " : " + sha1), "Index entry should not exist");
    }

    private boolean folderExists(String directoryPath) {
        return Files.isDirectory(Paths.get(directoryPath));
    }

    private boolean fileExists(String filePath) {
        return Files.exists(Paths.get(filePath));
    }

    private String getSha1(String input) throws NoSuchAlgorithmException {
        String hashtext = "";
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");

            byte[] messageDigest = md.digest(input.getBytes());

            BigInteger no = new BigInteger(1, messageDigest);

            hashtext = no.toString(16);

            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return hashtext;
    }
}
