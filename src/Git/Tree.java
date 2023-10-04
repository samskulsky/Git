package src.Git;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.io.IOException;
import java.util.ArrayList;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;

public class Tree {
    private static ArrayList<String> treeList;
    private HashSet<String> fileNameList;
    private HashSet<String> sha1List;

    public static void main(String[] args) throws Exception {
        Tree tree = new Tree();
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("testfile.txt"))) {
            Blob blob = new Blob("testfile.txt");
            String sha1 = blob.toSha1("testFile.txt");
            tree.addTreeEntry("tree", sha1, "testfile.txt");
            Blob blob2 = new Blob("test.txt");
            String sha2 = blob2.toSha1("test.txt");
            tree.addTreeEntry("blob", sha2, "test.txt");
            System.out.println(treeList.get(0));
            System.out.println(treeList.get(1));
            tree.removeTreeEntry("test.txt");
            tree.removeBlobEntry("test.txt");
            tree.writeDataFile();

            new File("test1").mkdirs();
            FileWriter fw = new FileWriter("test1/examplefile1.txt");
            fw.write("stuff");
            fw.close();

            FileWriter fw2 = new FileWriter("test1/examplefile2.txt");
            fw2.write("random things");
            fw2.close();

            FileWriter fw3 = new FileWriter("test1/examplefile3.txt");
            fw3.write("other stuff");
            fw3.close();
            Tree tree4 = new Tree();
            tree4.addDirectory("test1");

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public Tree() {
        treeList = new ArrayList<String>();
        fileNameList = new HashSet<String>();
        sha1List = new HashSet<String>();
    }

    public void addTreeEntry(String fileType, String sha1, String fileName) {
        if (fileType.equals("tree")) {
            if (!treeList.contains(fileType + " : " + sha1)) {
                treeList.add(fileType + " : " + sha1);
                sha1List.add(sha1);
            }

        }
        if (fileType.equals("blob")) {
            if (!treeList.contains(fileType + " : " + sha1 + " : " + fileName)) {
                treeList.add(fileType + " : " + sha1 + " : " + fileName);
                fileNameList.add(fileName);
            }
        }
    }

    public String removeTreeEntry(String sha1) {
        String tree = "";
        for (int i = 0; i < treeList.size(); i++) {
            if (treeList.get(i).contains(sha1)) {
                String[] parts = treeList.get(i).split(" : ");
                sha1List.remove(parts[1]);
                tree = treeList.get(i);
                treeList.remove(i);
            }

        }
        return tree;
    }

    public void removeBlobEntry(String fileName) {
        for (int i = 0; i < treeList.size(); i++) {
            if (treeList.get(i).contains(fileName)) {
                String[] parts = treeList.get(i).split(" : ");
                if (parts.length == 3) {
                    fileNameList.remove(parts[2]);
                }
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
            for (String entry : treeList) {
                bw.write(entry);
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String generateFileName() {

        StringBuilder data = new StringBuilder();
        for (String entry : treeList) {
            data.append(entry);
            data.append("\n");
        }

        String sha1 = Blob.toSha1(data.toString());

        return "objects/" + sha1;
    }

    public ArrayList<String> getTreeList() {
        return treeList;
    }

    public String getSha1() {
        StringBuilder data = new StringBuilder();
        for (String entry : treeList) {
            data.append(entry);
            data.append("\n");
        }
        String sha1 = Blob.toSha1(data.toString());
        return sha1;
    }

    public String addDirectory(String directoryPath) throws Exception {
        try {
            String[] files = listFiles(directoryPath).toArray(new String[0]);

            String[] folders = listFolders(directoryPath).toArray(new String[0]);

            for (String file : files) {
                File f = new File(file);
                addTreeEntry("blob", Blob.toSha1(Index.readFile(file)), f.getName());
            }

            for (String file : folders) {
                Tree childTree = new Tree();
                childTree.addDirectory(file);
                childTree.writeDataFile();
                addTreeEntry("tree", childTree.getSha1(), file);
            }
        } catch (IOException exception) {
            throw new Exception("File not readable");
        }

        writeDataFile();
        return getSha1();
    }

    public Set<String> listFiles(String dir) {
        return Stream.of(new File(dir).listFiles())
                .filter(file -> !file.isDirectory())
                .map(File::getPath)
                .collect(Collectors.toSet());
    }

    public Set<String> listFolders(String dir) {
        return Stream.of(new File(dir).listFiles())
                .filter(file -> file.isDirectory())
                .map(File::getPath)
                .collect(Collectors.toSet());
    }

}