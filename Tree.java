import java.util.HashSet;
import java.io.IOException;
import java.util.ArrayList;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.File;

public class Tree 
{
    private static ArrayList <String> treeList;
    private HashSet<String> fileNameList;
    private HashSet<String> sha1List;

    public static void main (String [] args)
    {
        Tree tree = new Tree();
        try (BufferedWriter bw = new BufferedWriter (new FileWriter ("testfile.txt")))
        {
             Blob blob = new Blob ("testfile.txt");
           String sha1 = blob.toSha1 ("testFile.txt");
            tree.addTreeEntry("tree", sha1, "testfile.txt");
            Blob blob2 = new Blob ("test.txt");
           String sha2 = blob2.toSha1("test.txt");
        tree.addTreeEntry("blob", sha2, "test.txt");
        System.out.println (treeList.get(0));
        System.out.println (treeList.get(1));
        tree.writeDataFile("treeData");



            
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        
        
    
    }

    public Tree ()
    {
        treeList = new ArrayList <String>();
        fileNameList = new HashSet<String>();
        sha1List = new HashSet<String>();
    }

    public void addTreeEntry (String fileType, String sha1, String fileName)
    {
        if (fileType.equals ("tree"))
        {
            if (!treeList.contains (fileType + " : " + sha1))
            {
                treeList.add (fileType + " : " + sha1);
                sha1List.add(sha1);
            }

        }
        if (fileType.equals ("blob"))
        {
            if (!treeList.contains (fileType + " : " + sha1 + " : " + fileName))
            {
                treeList.add (fileType + " : " + sha1 + " : " + fileName);
                fileNameList.add(fileName);
            }
        }
    }

    public String removeTreeEntry (String sha1)
    {
        String tree = "";
        for (int i = 0; i < treeList.size(); i++)
        {
            if (treeList.get(i).contains(sha1))
            {
                String [] parts = treeList.get(i).split (" : ");
                sha1List.remove (parts[1]);
                tree = treeList.get(i);
                treeList.remove (i);
            }

        }
        return tree;
    }

    public void removeBlobEntry (String fileName)
    {
        for (int i = 0; i < treeList.size(); i++)
        {
            if (treeList.get(i).contains(fileName))
            {
                String [] parts = treeList.get(i).split (" : ");
                if (parts.length == 3)
                {
                    fileNameList.remove (parts[2]);
                }
                treeList.remove (i);
            }
        }
    }

    public void writeDataFile (String outputFile)
    {
        File file = new File (outputFile);
        try (BufferedWriter bw = new BufferedWriter (new FileWriter (file)))
        {
            for (int i = 0; i < treeList.size(); i++)
            {
                bw.write (treeList.get(i));
                bw.newLine();
            }
            bw.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}