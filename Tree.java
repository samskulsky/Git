import java.util.HashMap;
import java.util.ArrayList;

public class Tree 
{
    private ArrayList <String> treeList;
    private ArrayList <String> sha1List;
    private ArrayList <String> fileNameList;

    public static void main (String [] args)
    {

    }

    public Tree ()
    {
        treeList = new ArrayList <String>();
        sha1List = new ArrayList <String>();
        fileNameList = new ArrayList<String>();
    }

    public void addTreeEntry (String fileType, String sha1, String fileName)
    {
        if (fileName.equals (null))
        {
            treeList.add(fileType + " : " + sha1);
            sha1List.add (sha1);

        }
        if (!fileNameList.contains (fileName))
        {
            treeList.add (fileType + " : " + sha1 + " : " + fileName);
            fileNameList.add (fileName);
            sha1List.add(sha1);
        }
    }

    public void removeTreeEntry ()
    {
        
    }

    public void saveTreeData ()
    {

    }
}