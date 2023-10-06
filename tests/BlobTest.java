package tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.stream.Stream;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import src.Git.Blob;
import src.Git.Index;

public class BlobTest {
    private static final String testFinalPath = "testfile2.txt";
    private static final String testFileContent = "Testing the index and blob classes";

    // create festfile2.txt and testFileContent in a BeforeAll method
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

        FileWriter fw = new FileWriter(testFinalPath);
        try {
            fw.write(testFileContent);
        } catch (IOException e) {
            e.printStackTrace();
        }
        fw.close();
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

    @Test
    public void testBlob() throws IOException {

        Blob blob = new Blob(testFinalPath);

        assertNotNull(blob.getSha1());

        assertTrue(checkFileExists("objects/" + blob.getSha1()));

        String originalContent = readFileContent(testFinalPath);

        String blobContent = readFileContent("objects/" + blob.getSha1());

        assertEquals(originalContent, blobContent);
    }

    @Test
    void testGetSha1() {
        String sha1 = null;
        try {
            Blob testBlob = new Blob(testFinalPath);
            String sha = testBlob.getSha1();
            sha1 = sha;
        } catch (IOException e) {
            e.printStackTrace();
        }
        assertNotNull(sha1);

    }

    @Test
    void testToSha1() {
        String sha1 = null;
        try {
            Blob blob = new Blob(testFinalPath);
            sha1 = blob.toSha1(testFileContent);

        } catch (IOException e) {
            e.printStackTrace();
        }
        assertNotNull(sha1);
    }

    private boolean checkFileExists(String filePath) {
        return Files.exists(Paths.get(filePath));
    }

    private String readFileContent(String filePath) throws IOException {
        return new String(Files.readAllBytes(Paths.get(filePath)));
    }
}
