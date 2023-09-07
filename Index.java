import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;

public class Index {

    public static void init() throws IOException {
        File in = new File("index");
        File obj = new File("objects");

        if (!in.isFile()) {
            in.createNewFile();
        }

        if (!obj.isDirectory()) {
            obj.mkdir();
        }
    }

    public static String readFile(String from) throws IOException {
        return Files.readString(Path.of(from));
    }

    public static void addBlob(String filePath) throws IOException {
        init();

        Blob blob = new Blob(filePath);
        File file = new File(filePath);

        FileWriter fw = new FileWriter("index");
        PrintWriter pw = new PrintWriter(fw);

        if (!readFile("index").contains(blob.getSha1())) {
            pw.println(file.getName() + " : " + blob.getSha1());
        }

        pw.close();
        fw.close();
    }
}
