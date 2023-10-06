package tests;

import src.Git.Commit;
import src.Git.Index;
import src.Git.Tree;

public class Tester {
    public static void main(String[] args) throws Exception {
        Index.addDirectory("testTwoCommitsA");
        Commit commit = new Commit("", "samskulsky", "testing two commits (a)");
        commit.writeToFile(commit.generateSha1());

        Index.addDirectory("testTwoCommitsB");
        Commit commit2 = new Commit(commit.generateSha1(), "samskulsky", "testing two commits (b)");
        commit2.writeToFile(commit2.generateSha1());
    }
}
