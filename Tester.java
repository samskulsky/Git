import java.io.IOException;

public class Tester {
    public static void main(String[] args) throws IOException {
        Index.addBlob("testfile.txt");
        Index.addBlob("testfile1.txt");
        Index.removeBlob("testfile.txt");
        Index.addBlob("testfile2.txt");
    }
}
