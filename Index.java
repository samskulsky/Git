import java.io.File;
import java.io.IOException;

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
}
