package tests;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import src.Git.Commit;
import src.Git.Index;
import src.Git.Tree;

public class Tester {

    public static void addFolder(String name) {
        File testOneCommitFolder = new File(name);
        testOneCommitFolder.mkdir();
    }

    public static void addFile(String path, String content) throws IOException {
        FileWriter fw = new FileWriter(path);
        fw.write(content);
        fw.close();
    }

    public static void main(String[] args) throws Exception {
        Index.init();
        Index.addBlob("ScreenShotCommitA.png");
        Index.addBlob("ScreenShotCommitB.png");

        // Commit commit = new Commit("", "samskulsky", "testing two commits (a)");
        // commit.writeToFile(commit.generateSha1());

        // System.out.println(Tree.deleteFile("ScreenShotCommitB.png"));

    }
}
