package src.Git;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

public class Commit {
    private String treeSha1;
    private String parentSha1;
    private String author;
    private Date date;
    private String summary;

    public Commit(String parentSha1, String author, String summary) throws IOException, NoSuchAlgorithmException {
        Tree tree = new Tree();

        this.parentSha1 = parentSha1;
        this.author = author;
        this.date = new Date(); // Current date and time
        this.summary = summary;

        FileReader fr = new FileReader("index");
        Scanner scan = new Scanner(fr);

        while (scan.hasNextLine()) {
            String entry = scan.nextLine();
            tree.add(entry);
        }

        scan.close();
        fr.close();

        new FileWriter("index", false).close();

        if (!parentSha1.isEmpty()) {
            try {
                tree.add("tree : " + getPreviousTreeSha1());

                String oldContents = Index.readFile(getCommitTreeSha1(parentSha1));
                Scanner s = new Scanner(oldContents);

                StringBuilder newContents = new StringBuilder();

                FileWriter fw = new FileWriter(parentSha1, false);

                int line = 0;
                while (s.hasNextLine()) {
                    String cur = s.nextLine();
                    if (line == 3) {
                        newContents.append(generateSha1() + "\n");
                    } else if (s.hasNextLine()) {
                        newContents.append(cur + "\n");
                    } else {
                        newContents.append(cur);
                    }
                    line++;
                }
                s.close();

                fw.write(newContents.toString());

                fw.close();
            } catch (Exception e) {
                // could not find last one
            }
        }

        this.treeSha1 = tree.getSha1();
        tree.writeDataFile();
    }

    public String getPreviousTreeSha1() throws IOException {
        return Blob.toSha1(Index.readFile(parentSha1));
    }

    public static String getCommitTreeSha1(String commitSha1) throws IOException {
        FileReader fr = new FileReader("objects/" + commitSha1);
        Scanner scan = new Scanner(fr);
        String sha = scan.next();
        scan.close();
        fr.close();

        return sha;
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
        commitData.append(summary);

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
        commitData.append(summary);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write(commitData.toString());
        }
    }
}
