import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;

public class Index {
    public static void main(String[] args) throws IOException {
        removeBlob("test.txt");
    }

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

        FileWriter fw = new FileWriter("index", true);
        PrintWriter pw = new PrintWriter(fw);

        if (!readFile("index").contains(blob.getSha1())) {
            if (readFile("index").isEmpty())
                pw.print(file.getName() + " : " + blob.getSha1());
            else
                pw.print("\n" + file.getName() + " : " + blob.getSha1());
        }

        pw.close();
        fw.close();
    }

    public static void removeBlob(String filePath) throws IOException {
        File file = new File(filePath);
        removeLine(file.getName());
    }

    public static void removeLine(String lineContent) throws IOException {
        File file = new File("index");
        File temp = new File("_temp_");
        PrintWriter out = new PrintWriter(new FileWriter(temp));
        Files.lines(file.toPath())
                .filter(line -> !line.contains(lineContent))
                .forEach(out::println);
        out.flush();
        out.close();
        temp.renameTo(file);
    }
}
