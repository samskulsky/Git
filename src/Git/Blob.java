package src.Git;

import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Blob {

    private String s1;

    public Blob(String path) throws IOException {
        String fileData = Index.readFile(path);

        String sha1 = toSha1(fileData);

        s1 = sha1;

        FileWriter fw = new FileWriter("objects/" + sha1, false);
        fw.write(fileData);
        fw.close();
    }

    public String getSha1() {
        return s1;
    }

    public static String toSha1(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");

            byte[] messageDigest = md.digest(input.getBytes());

            BigInteger no = new BigInteger(1, messageDigest);

            String hashtext = no.toString(16);

            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }

            return hashtext;
        }

        // For specifying wrong message digest algorithms
        catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}