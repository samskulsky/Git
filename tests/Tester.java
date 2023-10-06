package tests;

import src.Git.Commit;
import src.Git.Index;
import src.Git.Tree;

public class Tester {
    public static void main(String[] args) throws Exception {
        Index.addDirectory("testOneCommit");
        Commit commit = new Commit("", "samskulsky", "testing one commit");
        commit.writeToFile(commit.generateSha1());
    }
}
