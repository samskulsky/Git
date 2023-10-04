package tests;

import java.io.IOException;

import src.Git.Index;
import src.Git.Tree;

public class Tester {
    public static void main(String[] args) throws Exception {
        Index.init();
        Tree tree = new Tree();
        System.out.println(tree.addDirectory("advancedTest"));
    }
}
