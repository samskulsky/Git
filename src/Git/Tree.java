package src.Git;

import java.io.IOException;
import java.util.ArrayList;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

public class Tree {
    private static ArrayList<String> treeList;

    public Tree() {
        treeList = new ArrayList<String>();
    }

    public void add(String entry) {
        if (!treeList.contains(entry)) {
            treeList.add(entry);
        }
    }

    public void addTreeEntry(String fileType, String sha1, String fileName) {
        if (fileType.equals("tree")) {
            if (!treeList.contains(fileType + " : " + sha1)) {
                treeList.add(fileType + " : " + sha1);
            } else {
                treeList.add(fileType + " : " + sha1 + " : " + fileName);

            }
        }

        if (fileType.equals("blob")) {
            if (!treeList.contains(fileType + " : " + sha1 + " : " + fileName)) {
                treeList.add(fileType + " : " + sha1 + " : " + fileName);

            }
        }
    }

    public String removeTreeEntry(String sha1) {
        String tree = "";
        for (int i = 0; i < treeList.size(); i++) {
            if (treeList.get(i).contains(sha1)) {

                tree = treeList.get(i);
                treeList.remove(i);
            }

        }
        return tree;
    }

    public void removeBlobEntry(String fileName) {
        for (int i = 0; i < treeList.size(); i++) {
            if (treeList.get(i).contains(fileName)) {

                treeList.remove(i);
            }
        }
    }

    public String getDataString() {
        StringBuilder sb = new StringBuilder();
        for (String entry : treeList) {
            sb.append(entry + "\n");
        }
        return sb.toString();
    }

    public void writeDataFile() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(generateFileName()))) {
            for (int i = 0; i < treeList.size(); i++) {
                if (!treeList.get(i).isEmpty()) {
                    bw.write(treeList.get(i));
                    if (i < treeList.size() - 1) {
                        bw.newLine();
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String generateFileName() {
        return "objects/" + getSha1();
    }

    public ArrayList<String> getTreeList() {
        return treeList;
    }

    public String getSha1() {
        StringBuilder contents = new StringBuilder();
        for (int i = 0; i < treeList.size(); i++) {
            if (!treeList.get(i).isEmpty()) {
                contents.append(treeList.get(i));
                if (i < treeList.size() - 1) {
                    contents.append("\n");
                }
            }
        }
        String sha1 = Blob.toSha1(contents.toString());
        return sha1.trim();
    }

    public String addDirectory(String directoryPath) throws Exception {
        File dir = new File(directoryPath);
        if (!dir.isDirectory()) {
            throw new Exception("Not a directory");
        }
        File[] files = dir.listFiles();

        Tree tree = new Tree();

        for (File file : files) {
            if (file.isDirectory()) {
                Tree childTree = new Tree();
                tree.addTreeEntry("tree", childTree.addDirectory(file.getPath()), file.getName());
            } else {
                Blob blob = new Blob(file.getPath());
                tree.addTreeEntry("blob", blob.getSha1(), file.getName());
            }
        }

        tree.writeDataFile();

        return tree.getSha1();
    }

}