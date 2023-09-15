import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import static org.junit.jupiter.api.Assertions.*;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class IndexTester {

    private static final String testFinalPath = "testfile2.txt";
    private static final String testFileContent = "Testing the index and blob classes";

    @BeforeEach
    public void setUp() {
        try {
            FileWriter fileWriter = new FileWriter(testFinalPath);
            fileWriter.write(testFileContent);
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
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

    
    private boolean checkFileExists(String filePath) {
        return Files.exists(Paths.get(filePath));
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

        try 
        {
            assertTrue(fileExists("objects/" + getSha1(testFileContent)));
        }
        catch (NoSuchAlgorithmException e)
        {
            e.printStackTrace();
        }

        
        String indexContents = readFileContent("index");
        assertTrue(indexContents.contains(testFinalPath));
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

        } 
        catch (NoSuchAlgorithmException e)
        {
            e.printStackTrace();
        }
        return hashtext;
    }
}
