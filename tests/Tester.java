package tests;

import java.io.IOException;

import src.Git.Index;
import src.Git.Tree;

public class Tester {
    public static void main(String[] args) throws Exception {
        Tree tree = new Tree();
        tree.addDirectory("testfolder");
    }
}
