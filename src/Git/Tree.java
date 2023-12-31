package src.Git;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

public class Tree {
    private static ArrayList<String> treeList;
    private static String foundTree;

    public Tree() throws IOException {
        Index.init();

        FileWriter fw = new FileWriter("__temp__", false);
        FileReader fr = new FileReader("index");
        Scanner scan = new Scanner(fr);

        while (scan.hasNextLine()) {
            String l = scan.nextLine();

            if (!l.contains("*deleted*")) {
                fw.write(l);
            }

            if (scan.hasNextLine()) {
                fw.write("\n");
            }
        }

        fw.close();
        fr.close();
        scan.close();

        File temp = new File("__temp__");
        File index = new File("index");
        temp.renameTo(index);

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

    public void writeDataFile() throws IOException {
        StringBuilder contents = new StringBuilder();
        for (int i = 0; i < treeList.size(); i++) {
            contents.append(treeList.get(i));
            if (i < treeList.size() - 1) {
                contents.append("\n");
            }
        }

        FileWriter fw = new FileWriter(generateFileName(), false);
        fw.write(contents.toString());
        fw.close();
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
            contents.append(treeList.get(i));
            if (i < treeList.size() - 1) {
                contents.append("\n");
            }
        }
        String sha1 = Blob.toSha1(contents.toString());
        return sha1;
    }

    public String addDirectory(String directoryPath) throws Exception {
        File dir = new File(directoryPath);
        if (!dir.isDirectory()) {
            throw new Exception("Not a directory");
        }
        File[] files = dir.listFiles();

        for (File file : files) {
            if (file.isDirectory()) {
                Tree childTree = new Tree();
                childTree.addDirectory(file.getPath());
                add("tree : " + childTree.getSha1() + " : " + file.getName());
            }

            if (file.isFile()) {
                Blob blob = new Blob(file.getPath());
                add("blob : " + blob.getSha1() + " : " + file.getName());
            }
        }

        writeDataFile();

        return getSha1();
    }

    public static String findDeletedFile(String deletedFileName, String treeSha) throws Exception {
        FileReader treeReader = new FileReader("objects/" + treeSha);
        try (Scanner scan = new Scanner(treeReader)) {
            while (scan.hasNextLine()) {
                String line = scan.nextLine();
                if (line.split(" : ")[0].equals("tree")) {
                    String a = findDeletedFile(deletedFileName, line.split(" : ")[1]);
                    if (!a.isEmpty())
                        return a;
                } else if (line.contains(deletedFileName)) {
                    foundTree = treeSha;
                }
            }
        }

        treeReader.close();

        return foundTree;
    }

    public static String deleteFile(String deletedFileName) throws Exception {
        FileReader frHead = new FileReader("HEAD");
        Scanner frScan = new Scanner(frHead);
        String latestCommit = frScan.nextLine();
        frScan.close();
        frHead.close();

        FileReader latestTree = new FileReader("objects/" + latestCommit);
        Scanner latestScan = new Scanner(latestTree);
        String latestTreeSha = latestScan.nextLine();
        latestScan.close();
        latestTree.close();

        return findDeletedFile(deletedFileName, latestTreeSha);
    }

}