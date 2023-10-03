package src.Git;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Commit {
    private String treeSha1;
    private String parentSha1;
    private String author;
    private Date date;
    private String summary;

    public Commit(String parentSha1, String author, String summary) {
        Tree tree = new Tree();
        this.treeSha1 = tree.getSha1();
        this.parentSha1 = parentSha1;
        this.author = author;
        this.date = new Date(); // Current date and time
        this.summary = summary;
    }

    public String getTreeSha1() {
        return treeSha1;
    }

    public String getParentSha1() {
        return parentSha1;
    }

    public String getAuthor() {
        return author;
    }

    public String getDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM d, yyyy");
        return dateFormat.format(date);
    }

    public String getSummary() {
        return summary;
    }

    public String generateSha1() throws NoSuchAlgorithmException {
        StringBuilder commitData = new StringBuilder();
        commitData.append(treeSha1 + "\n");
        commitData.append(parentSha1 + "\n");
        commitData.append("\n");
        commitData.append(author + "\n");
        commitData.append(getDate() + "\n");
        commitData.append(summary + "\n");

        String commitDataString = commitData.toString();

        MessageDigest digest = MessageDigest.getInstance("SHA-1");
        byte[] sha1Bytes = digest.digest(commitDataString.getBytes(StandardCharsets.UTF_8));

        StringBuilder sha1Hex = new StringBuilder();
        for (byte b : sha1Bytes) {
            sha1Hex.append(String.format("%02x", b));
        }

        return sha1Hex.toString();
    }

    public void writeToFile(String fileName) throws IOException, NoSuchAlgorithmException {
        String sha1 = generateSha1();
        String filePath = "objects/" + sha1;

        StringBuilder commitData = new StringBuilder();
        commitData.append(treeSha1 + "\n");
        commitData.append(parentSha1 + "\n");
        commitData.append("\n");
        commitData.append(author + "\n");
        commitData.append(getDate() + "\n");
        commitData.append(summary + "\n");

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write(commitData.toString());
        }
    }
}
