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
        Index.addDirectory("testCommitsA");
        Commit commit = new Commit("", "samskulsky", "testing four commits (a)");
        commit.writeToFile(commit.generateSha1());

        Index.addDirectory("testCommitsB");
        Commit commit2 = new Commit(commit.generateSha1(), "samskulsky", "testing four commits (b)");
        commit2.writeToFile(commit2.generateSha1());

        Index.addDirectory("testCommitsC");
        Commit commit3 = new Commit(commit2.generateSha1(), "samskulsky", "testing four commits (c)");
        commit3.writeToFile(commit3.generateSha1());

        Index.addDirectory("testCommitsD");
        Commit commit4 = new Commit(commit3.generateSha1(), "samskulsky", "testing four commits (d)");
        commit4.writeToFile(commit4.generateSha1());
    }
}
