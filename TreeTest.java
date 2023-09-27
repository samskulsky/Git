import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.junit.jupiter.api.Test;

public class TreeTest {
    private String path = "testfile.txt";

    @Test
    void testAddTreeEntry() {
        Blob blob = null;
        Tree tree = new Tree();
        try {
            blob = new Blob(path);
            tree.addTreeEntry("tree : ", blob.getSha1(), path);
        } catch (IOException e) {
            e.printStackTrace();
        }
        assertTrue(tree.getTreeList().isEmpty());
    }

    @Test
    void testRemoveBlobEntry() {

    }

    @Test
    void testRemoveTreeEntry() {
        Blob blob = null;
        Tree tree = new Tree();
        try {
            blob = new Blob(path);
            tree.addTreeEntry("tree : ", blob.getSha1(), path);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (blob != null) {
            tree.removeTreeEntry(blob.getSha1());
        }
        assertNotNull(tree.getTreeList());

    }

    @Test
    void testWriteDataFile() {
        Tree tree = new Tree();
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("testfile.txt"))) {
            Blob blob = new Blob("testfile.txt");
            String sha1 = blob.toSha1("testFile.txt");
            tree.addTreeEntry("tree", sha1, "testfile.txt");
            Blob blob2 = new Blob("test.txt");
            String sha2 = blob2.toSha1("test.txt");
            tree.addTreeEntry("blob", sha2, "test.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }
        tree.writeDataFile();
        assertTrue(fileExists(tree.generateFileName()));
        assertTrue(fileIsNotEmpty(tree.generateFileName()));
    }

    private boolean fileExists(String filePath) {
        return Files.exists(Paths.get(filePath));
    }

    private boolean fileIsNotEmpty(String filePath) {
        java.io.File file = new java.io.File(filePath);
        return file.exists() && file.length() > 0;
    }

    @Test
    void testAddDirectory() throws IOException {
        new File("test1").mkdirs();
        FileWriter fw = new FileWriter("test1/examplefile1.txt");
        fw.write("stuff");
        fw.close();

        FileWriter fw2 = new FileWriter("test1/examplefile2.txt");
        fw2.write("random things");
        fw2.close();

        FileWriter fw3 = new FileWriter("test1/examplefile3.txt");
        fw3.write("other stuff");
        fw3.close();

    }
}
